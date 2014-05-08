package com.lvl6.info;

import java.util.Date;

//look at PvpBattleHistoryRetrieveUtil to see which columns are used
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PvpBattleHistory extends BasePersistentObject{	

	
	private static final long serialVersionUID = -2619239431555979083L;	

	@Column(name = "attacker_id")
	private int attackerId;
	@Column(name = "defender_id")
	private int defenderId;
	@Column(name = "battle_end_time")
	private Date battleEndTime;
	@Column(name = "battle_start_time")
	private Date battleStartTime;	

	@Column(name = "attacker_elo_change")
	private int attackerEloChange;
	@Column(name = "attacker_elo_before")
	private int attackerEloBefore;
	@Column(name = "attacker_elo_after")
	private int attackerEloAfter;	

	@Column(name = "defender_elo_change")
	private int defenderEloChange;
	@Column(name = "defender_elo_before")
	private int defenderEloBefore;
	@Column(name = "defender_elo_after")
	private int defenderEloAfter;	

	@Column(name = "attacker_prev_league")
	private int attackerPrevLeague;
	@Column(name = "attacker_cur_league")
	private int attackerCurLeague;
	@Column(name = "defender_prev_league")
	private int defenderPrevLeague;
	@Column(name = "defender_cur_league")
	private int defenderCurLeague;	

	@Column(name = "attacker_prev_rank")
	private int attackerPrevRank;
	@Column(name = "attacker_cur_rank")
	private int attackerCurRank;
	@Column(name = "defender_prev_rank")
	private int defenderPrevRank;
	@Column(name = "defender_cur_rank")
	private int defenderCurRank;	

	@Column(name = "attacker_cash_change")
	private int attackerCashChange;
	@Column(name = "defender_cash_change")
	private int defenderCashChange;
	@Column(name = "attacker_oil_change")
	private int attackerOilChange;
	@Column(name = "defender_oil_change")
	private int defenderOilChange;
	@Column(name = "attacker_won")
	private boolean attackerWon;
	@Column(name = "cancelled")
	private boolean cancelled;
	@Column(name = "exacted_revenge")
	private boolean exactedRevenge;	
	public PvpBattleHistory() {
		super();
	}

	public PvpBattleHistory(int attackerId, int defenderId, Date battleEndTime,
			Date battleStartTime, int attackerEloChange, int attackerEloBefore,
			int attackerEloAfter, int defenderEloChange, int defenderEloBefore,
			int defenderEloAfter, int attackerPrevLeague,
			int attackerCurLeague, int defenderPrevLeague,
			int defenderCurLeague, int attackerPrevRank, int attackerCurRank,
			int defenderPrevRank, int defenderCurRank, int attackerCashChange,
			int defenderCashChange, int attackerOilChange,
			int defenderOilChange, boolean attackerWon, boolean cancelled,
			boolean exactedRevenge) {
		super();
		this.attackerId = attackerId;
		this.defenderId = defenderId;
		this.battleEndTime = battleEndTime;
		this.battleStartTime = battleStartTime;
		this.attackerEloChange = attackerEloChange;
		this.attackerEloBefore = attackerEloBefore;
		this.attackerEloAfter = attackerEloAfter;
		this.defenderEloChange = defenderEloChange;
		this.defenderEloBefore = defenderEloBefore;
		this.defenderEloAfter = defenderEloAfter;
		this.attackerPrevLeague = attackerPrevLeague;
		this.attackerCurLeague = attackerCurLeague;
		this.defenderPrevLeague = defenderPrevLeague;
		this.defenderCurLeague = defenderCurLeague;
		this.attackerPrevRank = attackerPrevRank;
		this.attackerCurRank = attackerCurRank;
		this.defenderPrevRank = defenderPrevRank;
		this.defenderCurRank = defenderCurRank;
		this.attackerCashChange = attackerCashChange;
		this.defenderCashChange = defenderCashChange;
		this.attackerOilChange = attackerOilChange;
		this.defenderOilChange = defenderOilChange;
		this.attackerWon = attackerWon;
		this.cancelled = cancelled;
		this.exactedRevenge = exactedRevenge;
	}

	public int getAttackerId() {
		return attackerId;
	}

	public void setAttackerId(int attackerId) {
		this.attackerId = attackerId;
	}

	public int getDefenderId() {
		return defenderId;
	}

	public void setDefenderId(int defenderId) {
		this.defenderId = defenderId;
	}

	public Date getBattleEndTime() {
		return battleEndTime;
	}

	public void setBattleEndTime(Date battleEndTime) {
		this.battleEndTime = battleEndTime;
	}

	public Date getBattleStartTime() {
		return battleStartTime;
	}

	public void setBattleStartTime(Date battleStartTime) {
		this.battleStartTime = battleStartTime;
	}

	public int getAttackerEloChange() {
		return attackerEloChange;
	}

	public void setAttackerEloChange(int attackerEloChange) {
		this.attackerEloChange = attackerEloChange;
	}

	public int getAttackerEloBefore() {
		return attackerEloBefore;
	}

	public void setAttackerEloBefore(int attackerEloBefore) {
		this.attackerEloBefore = attackerEloBefore;
	}

	public int getAttackerEloAfter() {
		return attackerEloAfter;
	}

	public void setAttackerEloAfter(int attackerEloAfter) {
		this.attackerEloAfter = attackerEloAfter;
	}

	public int getDefenderEloChange() {
		return defenderEloChange;
	}

	public void setDefenderEloChange(int defenderEloChange) {
		this.defenderEloChange = defenderEloChange;
	}

	public int getDefenderEloBefore() {
		return defenderEloBefore;
	}

	public void setDefenderEloBefore(int defenderEloBefore) {
		this.defenderEloBefore = defenderEloBefore;
	}

	public int getDefenderEloAfter() {
		return defenderEloAfter;
	}

	public void setDefenderEloAfter(int defenderEloAfter) {
		this.defenderEloAfter = defenderEloAfter;
	}

	public int getAttackerPrevLeague() {
		return attackerPrevLeague;
	}

	public void setAttackerPrevLeague(int attackerPrevLeague) {
		this.attackerPrevLeague = attackerPrevLeague;
	}

	public int getAttackerCurLeague() {
		return attackerCurLeague;
	}

	public void setAttackerCurLeague(int attackerCurLeague) {
		this.attackerCurLeague = attackerCurLeague;
	}

	public int getDefenderPrevLeague() {
		return defenderPrevLeague;
	}

	public void setDefenderPrevLeague(int defenderPrevLeague) {
		this.defenderPrevLeague = defenderPrevLeague;
	}

	public int getDefenderCurLeague() {
		return defenderCurLeague;
	}

	public void setDefenderCurLeague(int defenderCurLeague) {
		this.defenderCurLeague = defenderCurLeague;
	}

	public int getAttackerPrevRank() {
		return attackerPrevRank;
	}

	public void setAttackerPrevRank(int attackerPrevRank) {
		this.attackerPrevRank = attackerPrevRank;
	}

	public int getAttackerCurRank() {
		return attackerCurRank;
	}

	public void setAttackerCurRank(int attackerCurRank) {
		this.attackerCurRank = attackerCurRank;
	}

	public int getDefenderPrevRank() {
		return defenderPrevRank;
	}

	public void setDefenderPrevRank(int defenderPrevRank) {
		this.defenderPrevRank = defenderPrevRank;
	}

	public int getDefenderCurRank() {
		return defenderCurRank;
	}

	public void setDefenderCurRank(int defenderCurRank) {
		this.defenderCurRank = defenderCurRank;
	}

	public int getAttackerCashChange() {
		return attackerCashChange;
	}

	public void setAttackerCashChange(int attackerCashChange) {
		this.attackerCashChange = attackerCashChange;
	}

	public int getDefenderCashChange() {
		return defenderCashChange;
	}

	public void setDefenderCashChange(int defenderCashChange) {
		this.defenderCashChange = defenderCashChange;
	}

	public int getAttackerOilChange() {
		return attackerOilChange;
	}

	public void setAttackerOilChange(int attackerOilChange) {
		this.attackerOilChange = attackerOilChange;
	}

	public int getDefenderOilChange() {
		return defenderOilChange;
	}

	public void setDefenderOilChange(int defenderOilChange) {
		this.defenderOilChange = defenderOilChange;
	}

	public boolean isAttackerWon() {
		return attackerWon;
	}

	public void setAttackerWon(boolean attackerWon) {
		this.attackerWon = attackerWon;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isExactedRevenge() {
		return exactedRevenge;
	}

	public void setExactedRevenge(boolean exactedRevenge) {
		this.exactedRevenge = exactedRevenge;
	}

	@Override
	public String toString() {
		return "PvpBattleHistory [attackerId=" + attackerId + ", defenderId="
				+ defenderId + ", battleEndTime=" + battleEndTime
				+ ", battleStartTime=" + battleStartTime
				+ ", attackerEloChange=" + attackerEloChange
				+ ", attackerEloBefore=" + attackerEloBefore
				+ ", attackerEloAfter=" + attackerEloAfter
				+ ", defenderEloChange=" + defenderEloChange
				+ ", defenderEloBefore=" + defenderEloBefore
				+ ", defenderEloAfter=" + defenderEloAfter
				+ ", attackerPrevLeague=" + attackerPrevLeague
				+ ", attackerCurLeague=" + attackerCurLeague
				+ ", defenderPrevLeague=" + defenderPrevLeague
				+ ", defenderCurLeague=" + defenderCurLeague
				+ ", attackerPrevRank=" + attackerPrevRank
				+ ", attackerCurRank=" + attackerCurRank
				+ ", defenderPrevRank=" + defenderPrevRank
				+ ", defenderCurRank=" + defenderCurRank
				+ ", attackerCashChange=" + attackerCashChange
				+ ", defenderCashChange=" + defenderCashChange
				+ ", attackerOilChange=" + attackerOilChange
				+ ", defenderOilChange=" + defenderOilChange + ", attackerWon="
				+ attackerWon + ", cancelled=" + cancelled
				+ ", exactedRevenge=" + exactedRevenge + "]";
	}
}
