package com.lvl6.mobsters.services.quest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository;

@Component
public class QuestServiceImpl implements QuestService {
    
    private static Logger LOG = LoggerFactory.getLogger(QuestServiceImpl.class);

    @Autowired
    private QuestForUserRepository questForUserRepository;

    //NON CRUD LOGIC******************************************************************
    
    
    //CRUD LOGIC******************************************************************

    /**************************************************************************/

    @Override
    public List<QuestForUser> findByUserId( String userId ) {
    	//return questForUserRepository.load
    	return questForUserRepository.findByUserId(userId);
    }

	public QuestForUserRepository getQuestForUserRepository()
	{
		return questForUserRepository;
	}

	public void setQuestForUserRepository( QuestForUserRepository questForUserRepository )
	{
		this.questForUserRepository = questForUserRepository;
	}
    
}
