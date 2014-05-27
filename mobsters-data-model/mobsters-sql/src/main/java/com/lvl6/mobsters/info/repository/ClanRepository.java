package com.lvl6.mobsters.info.repository;
import org.springframework.data.repository.CrudRepository;

import com.lvl6.mobsters.info.Clan;
public interface ClanRepository extends CrudRepository<Clan, Integer>{
	Clan findByName(String name);
}