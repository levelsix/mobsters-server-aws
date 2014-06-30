//package com.lvl6.mobsters.info;
//
//import java.sql.Timestamp;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.ConstraintMode;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.ForeignKey;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//
///**
// * The persistent class for the monster_for_user database table.
// * 
// */
//@Entity
//@Table(name="monster_for_user")
//public class MonsterForUser extends BasePersistentObject {
//
//	private static final long serialVersionUID = 7211130161525778567L;
//
//	@Column(name="combine_start_time")
//	private Timestamp combineStartTime;
//
//	@Column(name="current_experience")
//	private int currentExperience;
//
//	@Column(name="current_health")
//	private int currentHealth;
//
//	@Column(name="current_level")
//	private byte currentLevel;
//
//	@Column(name="is_complete")
//	private byte isComplete;
//
//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name = "monster_id", foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
//	private IMonster monster;
//
//	@Column(name="num_pieces")
//	private byte numPieces;
//
//	@Column(name="source_of_pieces", length=1000)
//	private String sourceOfPieces;
//
//	@Column(name="team_slot_num")
//	private byte teamSlotNum;
//
//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name = "user_id", foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
//	private User user;
//
//	public MonsterForUser() { }
//
//	public MonsterForUser(String id, Timestamp combineStartTime,
//			int currentExperience, int currentHealth, byte currentLevel,
//			byte isComplete, IMonster monster, byte numPieces,
//			String sourceOfPieces, byte teamSlotNum, User user) {
//		super(id);
//		this.combineStartTime = combineStartTime;
//		this.currentExperience = currentExperience;
//		this.currentHealth = currentHealth;
//		this.currentLevel = currentLevel;
//		this.isComplete = isComplete;
//		this.monster = monster;
//		this.numPieces = numPieces;
//		this.sourceOfPieces = sourceOfPieces;
//		this.teamSlotNum = teamSlotNum;
//		this.user = user;
//	}
//
//	public Timestamp getCombineStartTime() {
//		return this.combineStartTime;
//	}
//
//	public void setCombineStartTime(Timestamp combineStartTime) {
//		this.combineStartTime = combineStartTime;
//	}
//
//	public int getCurrentExperience() {
//		return this.currentExperience;
//	}
//
//	public void setCurrentExperience(int currentExperience) {
//		this.currentExperience = currentExperience;
//	}
//
//	public int getCurrentHealth() {
//		return this.currentHealth;
//	}
//
//	public void setCurrentHealth(int currentHealth) {
//		this.currentHealth = currentHealth;
//	}
//
//	public byte getCurrentLevel() {
//		return this.currentLevel;
//	}
//
//	public void setCurrentLevel(byte currentLevel) {
//		this.currentLevel = currentLevel;
//	}
//
//	public byte getIsComplete() {
//		return this.isComplete;
//	}
//
//	public void setIsComplete(byte isComplete) {
//		this.isComplete = isComplete;
//	}
//
//	public IMonster getMonster() {
//		return this.monster;
//	}
//
//	public void setMonster(IMonster monster) {
//		this.monster = monster;
//	}
//
//	public byte getNumPieces() {
//		return this.numPieces;
//	}
//
//	public void setNumPieces(byte numPieces) {
//		this.numPieces = numPieces;
//	}
//
//	public String getSourceOfPieces() {
//		return this.sourceOfPieces;
//	}
//
//	public void setSourceOfPieces(String sourceOfPieces) {
//		this.sourceOfPieces = sourceOfPieces;
//	}
//
//	public byte getTeamSlotNum() {
//		return this.teamSlotNum;
//	}
//
//	public void setTeamSlotNum(byte teamSlotNum) {
//		this.teamSlotNum = teamSlotNum;
//	}
//
//	public User getUser() {
//		return this.user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//}