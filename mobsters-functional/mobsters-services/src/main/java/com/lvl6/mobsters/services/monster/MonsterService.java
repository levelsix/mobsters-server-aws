package com.lvl6.mobsters.services.monster;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Table;
import com.lvl6.mobsters.common.utils.BiFunction;
import com.lvl6.mobsters.dynamo.MonsterForUser;

public interface MonsterService {
	// public void updateUserMonsterHealth(String userId, Map<String,Integer> monsterIdToHealthMap);

	/**
	 * Apply an arbitary number of property changes to an arbitrary number of monsters all owned by a
	 * single user.  In the details table, a row corresponds to the identifier for a specific user monster,
	 * a column corresponds to a specific type of property change operation, and a value is the argument
	 * required to perform the column-specified operation.
	 * 
	 * @param details
	 * @see MonsterForUserOp
	 */
	public abstract void modifyMonstersForUser(String userId, Table<String,MonsterForUserOp,Object> details);
	
	public enum MonsterForUserOp {
		SET_HEALTH_RELATIVE(new BiFunction<MonsterForUser, Integer>() {
			@Override
			public void apply(MonsterForUser m, Integer h) {
				m.setCurrentHealth(
					m.getCurrentHealth() + h
				);
			}
		}),
		SET_HEALTH_ABSOLUTE(new BiFunction<MonsterForUser, Integer>() {
			@Override
			public void apply(MonsterForUser m, Integer h) {
				m.setCurrentHealth(h);
			}
		}),
		SET_EXPERIENCE_RELATIVE(new BiFunction<MonsterForUser, String>() {
			@Override
			public void apply(MonsterForUser m, String h) {
				
			}
		}),
		SET_EXPERIENCE_ABSOLUTE(new BiFunction<MonsterForUser, String>() {
			@Override
			public void apply(MonsterForUser m, String h) {
				
			}
		});

		private BiFunction<MonsterForUser, ?> lambda;

		/**
		 * Enum is parameterized because every operator is binary, with the first value being a String
		 * identifier for the object to target and the second being of a operator-specific type.  Some
		 * properties are Strings, others are Dates or integers.  The type of the property is usually
		 * what determines the type of the value needed to modify it.  Table<R,C,V> can only account
		 * for this variability in V by binding it as "Object", so the more specific type needed is
		 * retained by this operator enumeration that is used for C (Column identifier).
		 * 
		 * @param clazz
		 * @see MonsterService#modifyMonstersForUser(Table)
		 */
		<T> MonsterForUserOp(BiFunction<MonsterForUser,T> lambda) {
			this.lambda = lambda;
		}

		@SuppressWarnings("unchecked")
        public <T> void apply(MonsterForUser nextMonster, T value) {
			((BiFunction<MonsterForUser,T>) this.lambda).apply(nextMonster, value);
		}
	}
}
