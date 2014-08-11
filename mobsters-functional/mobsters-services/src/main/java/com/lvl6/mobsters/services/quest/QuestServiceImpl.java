package com.lvl6.mobsters.services.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository;
import com.lvl6.mobsters.dynamo.repository.QuestJobForUserRepository;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.info.QuestJob;
import com.lvl6.mobsters.info.repository.QuestJobRepository;
import com.lvl6.mobsters.info.repository.QuestRepository;
import com.lvl6.mobsters.utility.common.CollectionUtils;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersException;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode;

@Component
public class QuestServiceImpl implements QuestService, InitializingBean {
    
    private static Logger LOG = LoggerFactory.getLogger(QuestServiceImpl.class);
    
    // TODO: Figure out another way to construct a questGraph
    private static QuestGraph questGraph;

    @Autowired
    private QuestForUserRepository questForUserRepository;

    @Autowired
    private QuestJobForUserRepository questJobForUserRepository;
    
    @Autowired
    private QuestRepository questRepository;
    
    @Autowired
    private QuestJobRepository questJobRepository;
    

    //NON CRUD LOGIC******************************************************************
    List<Integer> getAvailableQuestIdsFromQuests(List<Integer> redeemedQuestIds, List<Integer> inProgressQuestIds) {
    	
    	if (null == questGraph) {
    		try {
				afterPropertiesSet();
			} catch (Exception e) {
				LOG.error("error when initializing static QuestGraph");
			}
    		return new ArrayList<Integer>();
    	} else {
    		return questGraph.getQuestsAvailable(redeemedQuestIds, inProgressQuestIds);
    	}
    }
    
    //CRUD LOGIC******************************************************************

    // BEGIN READ ONLY LOGIC******************************************************************
	
    @Override
    public List<QuestForUser> findByUserId( String userId ) {
    	//return questForUserRepository.load
    	return questForUserRepository.findByUserId(userId);
    }
    
    
    @Override
    public Map<Integer, Collection<QuestJobForUser>> findByUserIdAndQuestIdIn(
    	String userId,
    	Collection<Integer> questIds )
    {
    	List<QuestJobForUser> qjfuList = questJobForUserRepository.findByUserIdAndQuestIdIn(userId, questIds);
		
    	Map<Integer, Collection<QuestJobForUser>> questIdToQjfuList =
			new HashMap<Integer, Collection<QuestJobForUser>>();
		
    	//map by quest id to QuestJobForUser
		for (QuestJobForUser qjfu : qjfuList) {
			int questId = qjfu.getQuestId();
			
			if (!questIdToQjfuList.containsKey(questId)) {
				questIdToQjfuList.put(questId,
						new ArrayList<QuestJobForUser>());
			}
			
			Collection<QuestJobForUser> groupedQjfuList =
					questIdToQjfuList.get(questId);
			groupedQjfuList.add(qjfu);
		}

		return questIdToQjfuList;
   	}

    // END READ ONLY LOGIC******************************************************************

	@Override
	public void createQuestForUser( String userIdString, int questId )
	{
		Quest quest = questRepository.findOne( questId );
		List<QuestJob> jobs = questJobRepository.findByQuestId(questId); 
		if ( null == quest || CollectionUtils.lacksSubstance(jobs)) {
			LOG.error("null quest or quest jobs. quest="
				+ quest
				+ ", jobs="
				+ jobs);
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
		}
		checkIfUserCanAcceptQuest(userIdString, questId);
		
		//user can accept the quest
		QuestForUser qfu = new QuestForUser();
		qfu.setUserId(userIdString);
		qfu.setQuestId(questId);
		
		questForUserRepository.save(qfu);
		
		List<QuestJobForUser> qjfuList = new ArrayList<QuestJobForUser>();
		for (QuestJob qj : jobs) {
			QuestJobForUser qjfu = new QuestJobForUser(userIdString, questId,
				qj.getId(), false, 0);
			qjfuList.add(qjfu);
		}
		
		questJobForUserRepository.saveEach(qjfuList);
	}

	protected void checkIfUserCanAcceptQuest( String userIdString, int questId ) {

		List<QuestForUser> inProgressAndRedeemedUserQuests =
			questForUserRepository.findByUserId(userIdString);
		
		
		if (CollectionUtils.lacksSubstance(inProgressAndRedeemedUserQuests)) {
			return;
		}
		
		List<Integer> inProgressQuestIds = new ArrayList<Integer>();
	    List<Integer> redeemedQuestIds = new ArrayList<Integer>();

		//check if the quest is available to the user or if he has
		//started it already
	    for (QuestForUser uq : inProgressAndRedeemedUserQuests) {
	    	if (uq.isRedeemed()) {
	    		redeemedQuestIds.add(uq.getQuestId());
	    	} else {
	    		inProgressQuestIds.add(uq.getQuestId());  
	    	}
	    }
			
	    List<Integer> availableQuestIds = getAvailableQuestIdsFromQuests(
	    	redeemedQuestIds, inProgressQuestIds);
	    
	    if (CollectionUtils.lacksSubstance(availableQuestIds)
	    	|| !availableQuestIds.contains(questId)) {
	    	throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_QUEST_NOT_AVAILABLE_TO_USER);
	    }
	    
	    if (inProgressQuestIds.contains(questId)) {
	    	throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_QUEST_ALREADY_ACCEPTED);
	    }
	    LOG.info( "user can accept new quest. user="
	    	+ userIdString
	    	+ ", questId="
	    	+ questId);
	}
	
	//InitializingBean, so the private static map can be initialized
    @Override
    public void afterPropertiesSet() throws Exception {
    	questGraph = new QuestGraph(questRepository.findAll());
    }
	
	public QuestForUserRepository getQuestForUserRepository()
	{
		return questForUserRepository;
	}

	public void setQuestForUserRepository( QuestForUserRepository questForUserRepository )
	{
		this.questForUserRepository = questForUserRepository;
	}


	public QuestJobForUserRepository getQuestJobForUserRepository()
	{
		return questJobForUserRepository;
	}


	public void setQuestJobForUserRepository( QuestJobForUserRepository questJobForUserRepository )
	{
		this.questJobForUserRepository = questJobForUserRepository;
	}


	public QuestRepository getQuestRepository()
	{
		return questRepository;
	}


	public void setQuestRepository( QuestRepository questRepository )
	{
		this.questRepository = questRepository;
	}

	public QuestGraph getQuestGraph()
	{
		return questGraph;
	}

	public void setQuestGraph( QuestGraph questGraph )
	{
		QuestServiceImpl.questGraph = questGraph;
	}

	public QuestJobRepository getQuestJobRepository()
	{
		return questJobRepository;
	}

	public void setQuestJobRepository( QuestJobRepository questJobRepository )
	{
		this.questJobRepository = questJobRepository;
	}

}
