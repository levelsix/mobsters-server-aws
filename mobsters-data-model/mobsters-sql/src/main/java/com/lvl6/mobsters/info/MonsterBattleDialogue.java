package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MonsterBattleDialogue extends BasePersistentObject{

	
	private static final long serialVersionUID = -5528835530547990738L;
	@Column(name = "monster_id")
	private int monsterId;
	@Column(name = "dialogue_type")
	private String dialogueType;
	@Column(name = "dialogue")
	private String dialogue;
	@Column(name = "probability_uttered")
	private float probabilityUttered;	
	public MonsterBattleDialogue(){}
	public MonsterBattleDialogue(String id, int monsterId, String dialogueType,
			String dialogue, float probabilityUttered) {
		super(id);
		this.monsterId = monsterId;
		this.dialogueType = dialogueType;
		this.dialogue = dialogue;
		this.probabilityUttered = probabilityUttered;
	}
	
	public int getMonsterId() {
		return monsterId;
	}
	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}
	public String getDialogueType() {
		return dialogueType;
	}
	public void setDialogueType(String dialogueType) {
		this.dialogueType = dialogueType;
	}
	public String getDialogue() {
		return dialogue;
	}
	public void setDialogue(String dialogue) {
		this.dialogue = dialogue;
	}
	public float getProbabilityUttered() {
		return probabilityUttered;
	}
	public void setProbabilityUttered(float probabilityUttered) {
		this.probabilityUttered = probabilityUttered;
	}

	@Override
	public String toString() {
		return "MonsterBattleDialogue [id=" + id + ", monsterId=" + monsterId
				+ ", dialogueType=" + dialogueType + ", dialogue=" + dialogue
				+ ", probabilityUttered=" + probabilityUttered + "]";
	}
	
}
