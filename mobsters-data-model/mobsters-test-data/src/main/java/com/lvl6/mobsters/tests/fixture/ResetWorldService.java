//package com.lvl6.mobsters.tests.fixture;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.lvl6.mobsters.info.repository.ClanRepository;
//import com.lvl6.mobsters.info.repository.MonsterForUserRepository;
//import com.lvl6.mobsters.info.repository.MonsterRepository;
//import com.lvl6.mobsters.info.repository.UserRepository;
//
//public class ResetWorldService {
//    @Autowired
//    UserRepository userRepo;
//    
//    @Autowired
//    ClanRepository clanRepo;
//    
//    @Autowired
//    MonsterRepository monsterRepo;
//    
//    @Autowired
//    MonsterForUserRepository monsterForUserRepo;
//    
//    @Transactional
//    public void execute() {
//    	monsterForUserRepo.deleteAllInBatch();
//    	userRepo.deleteAllInBatch();
//    	monsterRepo.deleteAllInBatch();
//    	clanRepo.deleteAllInBatch();
//    }
//}
