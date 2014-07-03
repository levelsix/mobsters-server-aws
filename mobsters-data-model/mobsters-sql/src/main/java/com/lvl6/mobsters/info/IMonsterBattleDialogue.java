package com.lvl6.mobsters.info;

public interface IMonsterBattleDialogue extends IBaseIntPersistentObject
{

	public IMonster getMonster();

	public void setMonster( IMonster monster );

	public String getDialogueType();

	public void setDialogueType( String dialogueType );

	public String getDialogue();

	public void setDialogue( String dialogue );

	public float getProbabilityUttered();

	public void setProbabilityUttered( float probabilityUttered );

}