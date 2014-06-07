package com.lvl6.mobsters.services.minijob;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.Function;
import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.services.minijob.MiniJobServiceImpl.CreateUserMiniJobsSpecBuilderImpl;
import com.lvl6.mobsters.services.minijob.MiniJobServiceImpl.ModifyUserMiniJobsSpecBuilderImpl;
public interface MiniJobService {

    public abstract void modifyMiniJobsForUser( String userId, ModifyUserMiniJobsSpec details );

    public interface ModifyUserMiniJobsSpecBuilder {
        public ModifyUserMiniJobsSpec build();

        public ModifyUserMiniJobsSpecBuilder setUserMonsterIds( String userMiniJobId, Set<String> userMonsterIds );

        public ModifyUserMiniJobsSpecBuilder setTimeStarted( String userMiniJobId, Date timeStarted );
        
        public ModifyUserMiniJobsSpecBuilder setTimeCompleted( String userMiniJobId, Date timeCompleted );
    }
    
    interface UserMiniJobFunc extends Function<MiniJobForUser> {};

    public class ModifyUserMiniJobsSpec {
        // modification specification map
        final private Multimap<String, UserMiniJobFunc> modSpecMap;
        
        ModifyUserMiniJobsSpec(Multimap<String, UserMiniJobFunc> modSpecMap) {
            this.modSpecMap = modSpecMap;
        }

        Multimap<String, UserMiniJobFunc> getModSpecMultimap() {
            return modSpecMap;
        }

        public static ModifyUserMiniJobsSpecBuilder builder() {
            return new ModifyUserMiniJobsSpecBuilderImpl();
        }
    }

    public interface CreateUserMiniJobsSpecBuilder {
        public CreateUserMiniJobsSpec build();

        public CreateUserMiniJobsSpecBuilder setUserMonsterIds( String userMiniJobId, Set<String> userMonsterIds );
        
        public CreateUserMiniJobsSpecBuilder setTimeStarted( String userMiniJobId, Date timeStarted );

        public CreateUserMiniJobsSpecBuilder setTimeCompleted( String userMiniJobId, Date timeCompleted );
        
    }
    
    public class CreateUserMiniJobsSpec {
        // the end state: objects to be saved to db
        final private Map<String, MiniJobForUser> miniJobIdToMjfu;

        CreateUserMiniJobsSpec( Map<String, MiniJobForUser> miniJobIdToMjfu) {
            this.miniJobIdToMjfu = miniJobIdToMjfu;
        }
        
        Map<String, MiniJobForUser> getMiniJobIdToMjfu() {
            return miniJobIdToMjfu;
        }

        public static CreateUserMiniJobsSpecBuilder builder() {
            return new CreateUserMiniJobsSpecBuilderImpl();
        }
    }
}
