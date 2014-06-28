package com.lvl6.mobsters.services.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository;
import com.lvl6.mobsters.dynamo.repository.QuestJobForUserRepository;

@Component
public class QuestServiceImpl implements QuestService {
    
    private static Logger LOG = LoggerFactory.getLogger(QuestServiceImpl.class);

    @Autowired
    private QuestForUserRepository questForUserRepository;

    @Autowired
    private QuestJobForUserRepository questJobForUserRepository;

    //NON CRUD LOGIC******************************************************************
    
    
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

}
