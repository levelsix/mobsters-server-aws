package com.lvl6.mobsters.info.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Monster;
public interface MonsterRepository extends JpaRepository<Monster, String>{

}