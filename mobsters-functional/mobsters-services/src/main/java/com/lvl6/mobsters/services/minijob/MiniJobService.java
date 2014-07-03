package com.lvl6.mobsters.services.minijob;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.common.utils.Function;
import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.info.MiniJob;
import com.lvl6.mobsters.services.minijob.MiniJobServiceImpl.ModifyUserMiniJobsSpecBuilderImpl;

public interface MiniJobService {

	// NON CRUD
	// LOGIC******************************************************************
	public abstract List<MiniJob> spawnMiniJobs(int numToSpawn, int structId);

	// CRUD
	// LOGIC******************************************************************

	public abstract void modifyMiniJobsForUser(String userId,
			Director<ModifyUserMiniJobsSpecBuilder> modifyDirector);

	public interface ModifyUserMiniJobsSpecBuilder {
		public ModifyUserMiniJobsSpecBuilder startJob(String userMiniJobId,
			Set<String> userMonsterIds, Date timeStarted);

		public ModifyUserMiniJobsSpecBuilder completeJob(String userMiniJobId,
			Date timeCompleted);
	}

	interface UserMiniJobFunc extends Function<MiniJobForUser> {
	};

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

	/**************************************************************************/

	public abstract void createMiniJobsForUser(String userId,
			Director<CreateUserMiniJobsSpecBuilder> director);

	// TODO: Instead of collecting one mandatory property per call, collect them
	// all in a single call. If any of these are optional parameters, use a
	// second Director/Builder indirection and provide two builder methods, one
	// that includes the second-stage Director, for use when at least one
	// optional parameter is being set, and a second that excludes it, so there
	// is no need to provide a no-op director when only mandatory options are
	// set.
	//
	// See AlternateCreateUserMiniJobsSpecBuilder for a likely manifestation of
	// this TODO note. Observe how consolidating mandatory options into one
	// method call with optional parameters coming from a second tier allows us
	// to let the userMiniJobId be DynamoDB-generated as it is meant to be,
	// providing an even stronger guarantee of uniqueness than UUID.randomUUID()
	// is capable of.
	public interface CreateUserMiniJobsSpecBuilder {
		public CreateUserMiniJobsSpecBuilder setMiniJobId(String userMiniJobId,
			int miniJobId);

		public CreateUserMiniJobsSpecBuilder setBaseDmgReceived(
			String userMiniJobId, int baseDmgReceived);

		public CreateUserMiniJobsSpecBuilder setDurationMinutes(
			String userMiniJobId, int durationMinutes);

		public CreateUserMiniJobsSpecBuilder setUserMonsterIds(
			String userMiniJobId, Set<String> userMonsterIds);

		public CreateUserMiniJobsSpecBuilder setTimeStarted(
			String userMiniJobId, Date timeStarted);

		public CreateUserMiniJobsSpecBuilder setTimeCompleted(
			String userMiniJobId, Date timeCompleted);
	}

	public interface AlternateCreateUserMiniJobsSpecBuilder {
		public AlternateCreateUserMiniJobsSpecBuilder addMiniJob(int miniJobId,
			int baseDmgRcvd, int durationMins, Set<String> userMonsterIds,
			Director<OptionalUserMiniJobsSpecBuilder> optionalDirector);
		
		public AlternateCreateUserMiniJobsSpecBuilder addMiniJob(int miniJobId,
			int baseDmgRcvd, int durationMins, Set<String> userMonsterIds);
	}

	public interface OptionalUserMiniJobsSpecBuilder {
		// Let the default be TimeUtils.createNow()
		public OptionalUserMiniJobsSpecBuilder timeStarted(Date timeStarted);

		// Let the default be null for not completed or, in the case of modify,
		// unchanged.
		public OptionalUserMiniJobsSpecBuilder timeCompleted(Date timeCompleted);
	}
}
