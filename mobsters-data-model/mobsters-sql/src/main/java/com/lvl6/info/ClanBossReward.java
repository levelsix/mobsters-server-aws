package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanBossReward extends BasePersistentObject{

	
	private static final long serialVersionUID = 4581148128205247466L;  

	@Column(name = "clan_boss_id")
	private int clanBossId;
	@Column(name = "equip_id")
	private int equipId;  
	public ClanBossReward(){}
  public ClanBossReward(int id, int clanBossId, int equipId) {
    super();
    this.clanBossId = clanBossId;
    this.equipId = equipId;
  }



  public int getClanBossId() {
    return clanBossId;
  }

  public void setClanBossId(int clanBossId) {
    this.clanBossId = clanBossId;
  }

  public int getEquipId() {
    return equipId;
  }

  public void setEquipId(int equipId) {
    this.equipId = equipId;
  }

  @Override
  public String toString() {
    return "ClanBossReward [id=" + id + ", clanBossId=" + clanBossId
        + ", equipId=" + equipId + "]";
  }
  
}
