package com.lvl6.mobsters.info;

//this class is analogous to a task
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanRaidStage extends BasePersistentObject{

	
	private static final long serialVersionUID = 4353447583250770652L;
	@Column(name = "clan_raid_id")
	private int clanRaidId;
	@Column(name = "duration_minutes")
	private int durationMinutes;
	@Column(name = "stage_num")
	private int stageNum;
	@Column(name = "name")
	private String name;	
	//sum of all monster healths for this stage
	//not actually a column in the table

	@Column(name = "stage_health")
	private int stageHealth;	
	public ClanRaidStage(){}
	public ClanRaidStage(String id, int clanRaidId, int durationMinutes,
			int stageNum, String name) {
		super(id);
		this.clanRaidId = clanRaidId;
		this.durationMinutes = durationMinutes;
		this.stageNum = stageNum;
		this.name = name;
	}



	public int getClanRaidId() {
		return clanRaidId;
	}

	public void setClanRaidId(int clanRaidId) {
		this.clanRaidId = clanRaidId;
	}

	public int getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public int getStageNum() {
		return stageNum;
	}

	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getStageHealth() {
		return stageHealth;
	}

	public void setStageHealth(int stageHealth) {
		this.stageHealth = stageHealth;
	}

	@Override
	public String toString() {
		return "ClanRaidStage [id=" + id + ", clanRaidId=" + clanRaidId
				+ ", durationMinutes=" + durationMinutes + ", stageNum=" + stageNum
				+ ", name=" + name + ", stageHealth=" + stageHealth + "]";
	}

}
