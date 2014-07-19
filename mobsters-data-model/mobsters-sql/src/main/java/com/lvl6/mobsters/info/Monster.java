package com.lvl6.mobsters.info;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="Monster")
@Table(name="monster")
@Cacheable(true)
@Proxy(lazy=true, proxyClass=IMonster.class)
public class Monster extends BaseIntPersistentObject implements IMonster
{	
	/**
	 */
	private static final long serialVersionUID = 3306136127069310310L;

	@Column(name = "evolution_group")
	private String evolutionGroup;
	@Column(name = "monster_group")
	private String monsterGroup;
	@Column(name = "quality")
	private String quality;
	@Column(name = "evolution_level")
	private int evolutionLevel;
	@Column(name = "display_name")
	private String displayName;
	@Column(name = "element")
	private String element;
	@Column(name = "image_prefix")
	private String imagePrefix;
	@Column(name = "num_puzzle_pieces")
	private int numPuzzlePieces;
	@Column(name = "minutes_to_combine_pieces")
	private int minutesToCombinePieces;
	@Column(name = "max_level")
	private int maxLevel; //aka max enhancing level
	
	@OneToOne(fetch=FetchType.LAZY, targetEntity=Monster.class)
	@JoinColumn(
		name = "evolution_monster_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster evolutionMonster;
	
	@OneToOne(fetch=FetchType.LAZY, targetEntity=Monster.class)
	@JoinColumn(
		name = "evolution_catalyst_monster_id",
			nullable = true,
			foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster evolutionCatalystMonster;
	
	@Column(name = "minutes_to_evolve")
	private int minutesToEvolve;
	@Column(name = "num_catalysts_required")
	private int numCatalystsRequired; //will most likely be 1
	@Column(name = "carrot_recruited")
	private String carrotRecruited;
	@Column(name = "carrot_defeated")
	private String carrotDefeated;
	@Column(name = "carrot_evolved")
	private String carrotEvolved;
	@Column(name = "description")
	private String description;
	@Column(name = "evolution_cost")
	private int evolutionCost; //oil not cash
	@Column(name = "animation_type")
	private String animationType;
	@Column(name = "vertical_pixel_offset")
	private int verticalPixelOffset;
	@Column(name = "atk_sound_file")
	private String atkSoundFile;
	@Column(name = "atk_sound_animation_frame")
	private int atkSoundAnimationFrame;
	@Column(name = "atk_animation_repeated_frames_start")
	private int atkAnimationRepeatedFramesStart;
	@Column(name = "atk_animation_repeated_frames_end")
	private int atkAnimationRepeatedFramesEnd;
	@Column(name = "short_name")
	private String shortName;	
	
	@OneToMany(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="monster", 
		orphanRemoval=true,
		targetEntity=MonsterLevelInfo.class)
//	@JoinColumns( {
//		@JoinColumn(name="monster_id", referencedColumnName="monster_id"),
//		@JoinColumn(name="level", referencedColumnName="level")
//	} )
	private List<IMonsterLevelInfo> lvlInfo;
	
	@OneToMany(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="monster", 
		orphanRemoval=true,
		targetEntity=MonsterBattleDialogue.class)
	private List<IMonsterBattleDialogue> battleDialogue;

	@Column(name = "shadow_scale_factor")
	private float shadowScaleFactor; //TODO: Use this column
	
	public Monster() { }
	public Monster(int id, String name, String monsterGroup, String quality,
			int evolutionLevel, String displayName, String element,
			String imagePrefix, int numPuzzlePieces,
			int minutesToCombinePieces, int maxLevel, IMonster evolutionMonster,
			IMonster evolutionCatalystMonster, int minutesToEvolve,
			int numCatalystsRequired, String carrotRecruited,
			String carrotDefeated, String carrotEvolved, String description,
			int evolutionCost, String animationType, int verticalPixelOffset,
			String atkSoundFile, int atkSoundAnimationFrame,
			int atkAnimationRepeatedFramesStart,
			int atkAnimationRepeatedFramesEnd, String shortName,
			List<IMonsterLevelInfo> lvlInfo,
			List<IMonsterBattleDialogue> battleDialogue,
			float shadowScaleFactor) {
		super(id);
		this.evolutionGroup = name;
		this.monsterGroup = monsterGroup;
		this.quality = quality;
		this.evolutionLevel = evolutionLevel;
		this.displayName = displayName;
		this.element = element;
		this.imagePrefix = imagePrefix;
		this.numPuzzlePieces = numPuzzlePieces;
		this.minutesToCombinePieces = minutesToCombinePieces;
		this.maxLevel = maxLevel;
		this.evolutionMonster = evolutionMonster;
		this.evolutionCatalystMonster = evolutionCatalystMonster;
		this.minutesToEvolve = minutesToEvolve;
		this.numCatalystsRequired = numCatalystsRequired;
		this.carrotRecruited = carrotRecruited;
		this.carrotDefeated = carrotDefeated;
		this.carrotEvolved = carrotEvolved;
		this.description = description;
		this.evolutionCost = evolutionCost;
		this.animationType = animationType;
		this.verticalPixelOffset = verticalPixelOffset;
		this.atkSoundFile = atkSoundFile;
		this.atkSoundAnimationFrame = atkSoundAnimationFrame;
		this.atkAnimationRepeatedFramesStart = atkAnimationRepeatedFramesStart;
		this.atkAnimationRepeatedFramesEnd = atkAnimationRepeatedFramesEnd;
		this.shortName = shortName;
		this.lvlInfo = lvlInfo;
		this.battleDialogue = battleDialogue;
		this.shadowScaleFactor = shadowScaleFactor;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getName()
	 */
	@Override
	public String getEvolutionGroup() {
		return evolutionGroup;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setName(java.lang.String)
	 */
	@Override
	public void setEvolutionGroup(String evolutionGroup) {
		this.evolutionGroup = evolutionGroup;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getMonsterGroup()
	 */
	@Override
	public String getMonsterGroup() {
		return monsterGroup;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setMonsterGroup(java.lang.String)
	 */
	@Override
	public void setMonsterGroup(String monsterGroup) {
		this.monsterGroup = monsterGroup;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getQuality()
	 */
	@Override
	public String getQuality() {
		return quality;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setQuality(java.lang.String)
	 */
	@Override
	public void setQuality(String quality) {
		this.quality = quality;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getEvolutionLevel()
	 */
	@Override
	public int getEvolutionLevel() {
		return evolutionLevel;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setEvolutionLevel(int)
	 */
	@Override
	public void setEvolutionLevel(int evolutionLevel) {
		this.evolutionLevel = evolutionLevel;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setDisplayName(java.lang.String)
	 */
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getElement()
	 */
	@Override
	public String getElement() {
		return element;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setElement(java.lang.String)
	 */
	@Override
	public void setElement(String element) {
		this.element = element;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getImagePrefix()
	 */
	@Override
	public String getImagePrefix() {
		return imagePrefix;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setImagePrefix(java.lang.String)
	 */
	@Override
	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getNumPuzzlePieces()
	 */
	@Override
	public int getNumPuzzlePieces() {
		return numPuzzlePieces;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setNumPuzzlePieces(int)
	 */
	@Override
	public void setNumPuzzlePieces(int numPuzzlePieces) {
		this.numPuzzlePieces = numPuzzlePieces;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getMinutesToCombinePieces()
	 */
	@Override
	public int getMinutesToCombinePieces() {
		return minutesToCombinePieces;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setMinutesToCombinePieces(int)
	 */
	@Override
	public void setMinutesToCombinePieces(int minutesToCombinePieces) {
		this.minutesToCombinePieces = minutesToCombinePieces;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getMaxLevel()
	 */
	@Override
	public int getMaxLevel() {
		return maxLevel;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setMaxLevel(int)
	 */
	@Override
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getEvolutionMonster()
	 */
	@Override
	public IMonster getEvolutionMonster()
	{
		return evolutionMonster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setEvolutionMonster(com.lvl6.mobsters.info.IMonster)
	 */
	@Override
	public void setEvolutionMonster( IMonster evolutionMonster )
	{
		this.evolutionMonster = evolutionMonster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getEvolutionCatalystMonster()
	 */
	@Override
	public IMonster getEvolutionCatalystMonster()
	{
		return evolutionCatalystMonster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setEvolutionCatalystMonster(com.lvl6.mobsters.info.IMonster)
	 */
	@Override
	public void setEvolutionCatalystMonster( IMonster evolutionCatalystMonster )
	{
		this.evolutionCatalystMonster = evolutionCatalystMonster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getMinutesToEvolve()
	 */
	@Override
	public int getMinutesToEvolve() {
		return minutesToEvolve;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setMinutesToEvolve(int)
	 */
	@Override
	public void setMinutesToEvolve(int minutesToEvolve) {
		this.minutesToEvolve = minutesToEvolve;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getNumCatalystsRequired()
	 */
	@Override
	public int getNumCatalystsRequired() {
		return numCatalystsRequired;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setNumCatalystsRequired(int)
	 */
	@Override
	public void setNumCatalystsRequired(int numCatalystsRequired) {
		this.numCatalystsRequired = numCatalystsRequired;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getCarrotRecruited()
	 */
	@Override
	public String getCarrotRecruited() {
		return carrotRecruited;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setCarrotRecruited(java.lang.String)
	 */
	@Override
	public void setCarrotRecruited(String carrotRecruited) {
		this.carrotRecruited = carrotRecruited;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getCarrotDefeated()
	 */
	@Override
	public String getCarrotDefeated() {
		return carrotDefeated;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setCarrotDefeated(java.lang.String)
	 */
	@Override
	public void setCarrotDefeated(String carrotDefeated) {
		this.carrotDefeated = carrotDefeated;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getCarrotEvolved()
	 */
	@Override
	public String getCarrotEvolved() {
		return carrotEvolved;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setCarrotEvolved(java.lang.String)
	 */
	@Override
	public void setCarrotEvolved(String carrotEvolved) {
		this.carrotEvolved = carrotEvolved;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getEvolutionCost()
	 */
	@Override
	public int getEvolutionCost() {
		return evolutionCost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setEvolutionCost(int)
	 */
	@Override
	public void setEvolutionCost(int evolutionCost) {
		this.evolutionCost = evolutionCost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getAnimationType()
	 */
	@Override
	public String getAnimationType() {
		return animationType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setAnimationType(java.lang.String)
	 */
	@Override
	public void setAnimationType(String animationType) {
		this.animationType = animationType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getVerticalPixelOffset()
	 */
	@Override
	public int getVerticalPixelOffset() {
		return verticalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setVerticalPixelOffset(int)
	 */
	@Override
	public void setVerticalPixelOffset(int verticalPixelOffset) {
		this.verticalPixelOffset = verticalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getAtkSoundFile()
	 */
	@Override
	public String getAtkSoundFile() {
		return atkSoundFile;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setAtkSoundFile(java.lang.String)
	 */
	@Override
	public void setAtkSoundFile(String atkSoundFile) {
		this.atkSoundFile = atkSoundFile;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getAtkSoundAnimationFrame()
	 */
	@Override
	public int getAtkSoundAnimationFrame() {
		return atkSoundAnimationFrame;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setAtkSoundAnimationFrame(int)
	 */
	@Override
	public void setAtkSoundAnimationFrame(int atkSoundAnimationFrame) {
		this.atkSoundAnimationFrame = atkSoundAnimationFrame;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getAtkAnimationRepeatedFramesStart()
	 */
	@Override
	public int getAtkAnimationRepeatedFramesStart() {
		return atkAnimationRepeatedFramesStart;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setAtkAnimationRepeatedFramesStart(int)
	 */
	@Override
	public void setAtkAnimationRepeatedFramesStart(
			int atkAnimationRepeatedFramesStart) {
		this.atkAnimationRepeatedFramesStart = atkAnimationRepeatedFramesStart;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getAtkAnimationRepeatedFramesEnd()
	 */
	@Override
	public int getAtkAnimationRepeatedFramesEnd() {
		return atkAnimationRepeatedFramesEnd;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setAtkAnimationRepeatedFramesEnd(int)
	 */
	@Override
	public void setAtkAnimationRepeatedFramesEnd(int atkAnimationRepeatedFramesEnd) {
		this.atkAnimationRepeatedFramesEnd = atkAnimationRepeatedFramesEnd;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getShortName()
	 */
	@Override
	public String getShortName() {
		return shortName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setShortName(java.lang.String)
	 */
	@Override
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getLvlInfo()
	 */
	@Override
	public List<IMonsterLevelInfo> getLvlInfo()
	{
		if( lvlInfo == null ) {
			lvlInfo = new ArrayList<IMonsterLevelInfo>(4);
		}
		return lvlInfo;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setLvlInfo(java.util.List)
	@Override
	public void setLvlInfo( List<IMonsterLevelInfo> lvlInfo )
	{
		this.lvlInfo = lvlInfo;
	}
	 */
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#getBattleDialogue()
	 */
	@Override
	public List<IMonsterBattleDialogue> getBattleDialogue()
	{
		if( battleDialogue == null ) {
			battleDialogue = new ArrayList<IMonsterBattleDialogue>(4);
		}
		return battleDialogue;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonster#setBattleDialogue(java.util.List)
	@Override
	public void setBattleDialogue( List<IMonsterBattleDialogue> battleDialogue )
	{
		this.battleDialogue = battleDialogue;
	}
	 */

	@Override
	public float getShadowScaleFactor()
	{
		return shadowScaleFactor;
	}
	@Override
	public void setShadowScaleFactor( float shadowScaleFactor )
	{
		this.shadowScaleFactor = shadowScaleFactor;
	}
	
	@Override
	public String toString()
	{
		return "Monster [id="
			+ id
			+ "evolutionGroup="
			+ evolutionGroup
			+ ", monsterGroup="
			+ monsterGroup
			+ ", quality="
			+ quality
			+ ", evolutionLevel="
			+ evolutionLevel
			+ ", displayName="
			+ displayName
			+ ", element="
			+ element
			+ ", imagePrefix="
			+ imagePrefix
			+ ", numPuzzlePieces="
			+ numPuzzlePieces
			+ ", minutesToCombinePieces="
			+ minutesToCombinePieces
			+ ", maxLevel="
			+ maxLevel
			+ ", evolutionMonster=Monster[id="
			+ evolutionMonster.getId()
			+ "], evolutionCatalystMonster=Monster[id="
			+ evolutionCatalystMonster.getId()
			+ "], minutesToEvolve="
			+ minutesToEvolve
			+ ", numCatalystsRequired="
			+ numCatalystsRequired
			+ ", carrotRecruited="
			+ carrotRecruited
			+ ", carrotDefeated="
			+ carrotDefeated
			+ ", carrotEvolved="
			+ carrotEvolved
			+ ", description="
			+ description
			+ ", evolutionCost="
			+ evolutionCost
			+ ", animationType="
			+ animationType
			+ ", verticalPixelOffset="
			+ verticalPixelOffset
			+ ", atkSoundFile="
			+ atkSoundFile
			+ ", atkSoundAnimationFrame="
			+ atkSoundAnimationFrame
			+ ", atkAnimationRepeatedFramesStart="
			+ atkAnimationRepeatedFramesStart
			+ ", atkAnimationRepeatedFramesEnd="
			+ atkAnimationRepeatedFramesEnd
			+ ", shortName="
			+ shortName
			+ ", lvlInfo="
			+ lvlInfo.toString()
			+ ", battleDialogue="
			+ battleDialogue.toString()
			+ ", shadowScaleFactor="
			+ shadowScaleFactor
			+ "]";
	}
}
