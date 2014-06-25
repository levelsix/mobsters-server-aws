package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="MonsterBattleDialogue")
@Table(name="monster_battle_dialogue")
@Proxy(lazy=true, proxyClass=IMonsterBattleDialogue.class)
public class MonsterBattleDialogue extends BaseIntPersistentObject implements IMonsterBattleDialogue{

	private static final long serialVersionUID = 712145467719179722L;
	

	@ManyToOne
	@JoinColumn(
		name = "monster_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster monster;
	
	@Column(name = "dialogue_type")
	private String dialogueType;
	@Column(name = "dialogue")
	private String dialogue;
	@Column(name = "probability_uttered")
	private float probabilityUttered;	
	public MonsterBattleDialogue(){}
	public MonsterBattleDialogue(int id, IMonster monster, String dialogueType,
			String dialogue, float probabilityUttered) {
		super(id);
		this.monster = monster;
		this.dialogueType = dialogueType;
		this.dialogue = dialogue;
		this.probabilityUttered = probabilityUttered;
	}
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#getMonster()
	 */
	@Override
	public IMonster getMonster()
	{
		return monster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#setMonster(com.lvl6.mobsters.info.Monster)
	 */
	@Override
	public void setMonster( IMonster monster )
	{
		this.monster = monster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#getDialogueType()
	 */
	@Override
	public String getDialogueType() {
		return dialogueType;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#setDialogueType(java.lang.String)
	 */
	@Override
	public void setDialogueType(String dialogueType) {
		this.dialogueType = dialogueType;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#getDialogue()
	 */
	@Override
	public String getDialogue() {
		return dialogue;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#setDialogue(java.lang.String)
	 */
	@Override
	public void setDialogue(String dialogue) {
		this.dialogue = dialogue;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#getProbabilityUttered()
	 */
	@Override
	public float getProbabilityUttered() {
		return probabilityUttered;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterBattleDialogue#setProbabilityUttered(float)
	 */
	@Override
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
