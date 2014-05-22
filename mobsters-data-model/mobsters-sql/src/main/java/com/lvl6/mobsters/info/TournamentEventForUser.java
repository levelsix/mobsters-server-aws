package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TournamentEventForUser extends BasePersistentObject{
	
	private static final long serialVersionUID = -4146319195339195482L;
	@Column(name = "tournament_event_id")
	private int tournamentEventId;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "battles_won")
	private int battlesWon;
	@Column(name = "battles_lost")
	private int battlesLost;
	@Column(name = "battles_fled")
	private int battlesFled;  
	public TournamentEventForUser(){}
  public TournamentEventForUser(int tournamentEventId, int userId, int battlesWon,
      int battlesLost, int battlesFled) {
    super();
    this.tournamentEventId = tournamentEventId;
    this.userId = userId;
    this.battlesWon = battlesWon;
    this.battlesLost = battlesLost;
    this.battlesFled = battlesFled;
  }
  public int getTournamentEventId() {
    return tournamentEventId;
  }
  public void setTournamentEventId(int tournamentEventId) {
    this.tournamentEventId = tournamentEventId;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public int getBattlesWon() {
    return battlesWon;
  }
  public void setBattlesWon(int battlesWon) {
    this.battlesWon = battlesWon;
  }
  public int getBattlesLost() {
    return battlesLost;
  }
  public void setBattlesLost(int battlesLost) {
    this.battlesLost = battlesLost;
  }
  public int getBattlesFled() {
    return battlesFled;
  }
  public void setBattlesFled(int battlesFled) {
    this.battlesFled = battlesFled;
  }
  @Override
  public String toString() {
    return "BossEvent [tournamentEventId=" + tournamentEventId + ", userId="
        + userId + ", battlesWon=" + battlesWon + ", battlesLost=" + battlesLost
        + ", battlesFled=" + battlesFled + "]";
  }
}
