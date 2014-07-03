package com.lvl6.mobsters.info;

import java.util.List;

public interface IMonster extends IBaseIntPersistentObject
{

	public String getEvolutionGroup();

	public void setEvolutionGroup( String evolutionGroup );

	public String getMonsterGroup();

	public void setMonsterGroup( String monsterGroup );

	public String getQuality();

	public void setQuality( String quality );

	public int getEvolutionLevel();

	public void setEvolutionLevel( int evolutionLevel );

	public String getDisplayName();

	public void setDisplayName( String displayName );

	public String getElement();

	public void setElement( String element );

	public String getImagePrefix();

	public void setImagePrefix( String imagePrefix );

	public int getNumPuzzlePieces();

	public void setNumPuzzlePieces( int numPuzzlePieces );

	public int getMinutesToCombinePieces();

	public void setMinutesToCombinePieces( int minutesToCombinePieces );

	public int getMaxLevel();

	public void setMaxLevel( int maxLevel );

	public IMonster getEvolutionMonster();

	public void setEvolutionMonster( IMonster evolutionMonster );

	public IMonster getEvolutionCatalystMonster();

	public void setEvolutionCatalystMonster( IMonster evolutionCatalystMonster );

	public int getMinutesToEvolve();

	public void setMinutesToEvolve( int minutesToEvolve );

	public int getNumCatalystsRequired();

	public void setNumCatalystsRequired( int numCatalystsRequired );

	public String getCarrotRecruited();

	public void setCarrotRecruited( String carrotRecruited );

	public String getCarrotDefeated();

	public void setCarrotDefeated( String carrotDefeated );

	public String getCarrotEvolved();

	public void setCarrotEvolved( String carrotEvolved );

	public String getDescription();

	public void setDescription( String description );

	public int getEvolutionCost();

	public void setEvolutionCost( int evolutionCost );

	public String getAnimationType();

	public void setAnimationType( String animationType );

	public int getVerticalPixelOffset();

	public void setVerticalPixelOffset( int verticalPixelOffset );

	public String getAtkSoundFile();

	public void setAtkSoundFile( String atkSoundFile );

	public int getAtkSoundAnimationFrame();

	public void setAtkSoundAnimationFrame( int atkSoundAnimationFrame );

	public int getAtkAnimationRepeatedFramesStart();

	public void setAtkAnimationRepeatedFramesStart( int atkAnimationRepeatedFramesStart );

	public int getAtkAnimationRepeatedFramesEnd();

	public void setAtkAnimationRepeatedFramesEnd( int atkAnimationRepeatedFramesEnd );

	public String getShorterName();

	public void setShorterName( String shorterName );

	public List<IMonsterLevelInfo> getLvlInfo();

	public void setLvlInfo( List<IMonsterLevelInfo> lvlInfo );

	public List<IMonsterBattleDialogue> getBattleDialogue();

	public void setBattleDialogue( List<IMonsterBattleDialogue> battleDialogue );

}