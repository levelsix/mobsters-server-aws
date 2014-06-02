package com.lvl6.mobsters.tests.fixture;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PartialDataSet {

	public class ObjectMeta<T> {
		private final String name;
		private final T inst;

		public ObjectMeta(String name, T inst) {
			this.name = name;
			this.inst = inst;
		}

		String getName() {
			return name;
		}

		T getInst() {
			return inst;
		}

	}

	void repopulate();

	public List<ObjectMeta<?>> getObjects();
	
	public Set<JpaRepository<?, ?>> getUsedRepositories();
}
