package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MonsterBattleDialogue extends BaseIntPersistentObject{

	@ManyToOne
	@JoinColumn(
		name = "monster_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Monster monster;
	
	@Column(name = "dialogue_type")
	private String dialogueType;
	@Column(name = "dialogue")
	private String dialogue;
	@Column(name = "probability_uttered")
	private float probabilityUttered;	
	public MonsterBattleDialogue(){}
	public MonsterBattleDialogue(int id, Monster monster, String dialogueType,
			String dialogue, float probabilityUttered) {
		super(id);
		this.monster = monster;
		this.dialogueType = dialogueType;
		this.dialogue = dialogue;
		this.probabilityUttered = probabilityUttered;
	}
	
	public Monster getMonster()
	{
		return monster;
	}
	public void setMonster( Monster monster )
	{
		this.monster = monster;
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
		return "MonsterBattleDialogue [id=" + id + ", monsterId=" + monster
				+ ", dialogueType=" + dialogueType + ", dialogue=" + dialogue
				+ ", probabilityUttered=" + probabilityUttered + "]";
	}
	
}
