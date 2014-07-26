package com.lvl6.mobsters.info.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.BoosterPack;
public interface BoosterPackRepository extends JpaRepository<BoosterPack, Integer>{
    BoosterPack findById(int boosterPackId);
}
