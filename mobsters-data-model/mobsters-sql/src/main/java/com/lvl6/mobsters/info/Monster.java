package com.lvl6.mobsters.info;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="monster")
public class Monster extends BaseIntPersistentObject{	

	private static final long serialVersionUID = -3176206778781138581L;

	@Column(name = "name")
	private String name;
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
	@Column(name = "evolution_monster_id")
	private int evolutionMonsterId;
	@Column(name = "evolution_catalyst_monster_id")
	private int evolutionCatalystMonsterId;
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
	@Column(name = "shorter_name")
	private String shorterName;	
	
	@OneToOne(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="monster", 
		orphanRemoval=true)
	private List<MonsterLevelInfo> lvlInfo;
	
	@OneToMany(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="monster", 
		orphanRemoval=true)
	private List<MonsterBattleDialogue> battleDialogue;
	
	public Monster() { }
	public Monster(int id, String name, String monsterGroup, String quality,
			int evolutionLevel, String displayName, String element,
			String imagePrefix, int numPuzzlePieces,
			int minutesToCombinePieces, int maxLevel, int evolutionMonsterId,
			int evolutionCatalystMonsterId, int minutesToEvolve,
			int numCatalystsRequired, String carrotRecruited,
			String carrotDefeated, String carrotEvolved, String description,
			int evolutionCost, String animationType, int verticalPixelOffset,
			String atkSoundFile, int atkSoundAnimationFrame,
			int atkAnimationRepeatedFramesStart,
			int atkAnimationRepeatedFramesEnd, String shorterName,
			List<MonsterLevelInfo> lvlInfo,
			List<MonsterBattleDialogue> battleDialogue) {
		super(id);
		this.name = name;
		this.monsterGroup = monsterGroup;
		this.quality = quality;
		this.evolutionLevel = evolutionLevel;
		this.displayName = displayName;
		this.element = element;
		this.imagePrefix = imagePrefix;
		this.numPuzzlePieces = numPuzzlePieces;
		this.minutesToCombinePieces = minutesToCombinePieces;
		this.maxLevel = maxLevel;
		this.evolutionMonsterId = evolutionMonsterId;
		this.evolutionCatalystMonsterId = evolutionCatalystMonsterId;
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
		this.shorterName = shorterName;
		this.lvlInfo = lvlInfo;
		this.battleDialogue = battleDialogue;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMonsterGroup() {
		return monsterGroup;
	}

	public void setMonsterGroup(String monsterGroup) {
		this.monsterGroup = monsterGroup;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public int getEvolutionLevel() {
		return evolutionLevel;
	}

	public void setEvolutionLevel(int evolutionLevel) {
		this.evolutionLevel = evolutionLevel;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getImagePrefix() {
		return imagePrefix;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public int getNumPuzzlePieces() {
		return numPuzzlePieces;
	}

	public void setNumPuzzlePieces(int numPuzzlePieces) {
		this.numPuzzlePieces = numPuzzlePieces;
	}

	public int getMinutesToCombinePieces() {
		return minutesToCombinePieces;
	}

	public void setMinutesToCombinePieces(int minutesToCombinePieces) {
		this.minutesToCombinePieces = minutesToCombinePieces;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getEvolutionMonsterId() {
		return evolutionMonsterId;
	}

	public void setEvolutionMonsterId(int evolutionMonsterId) {
		this.evolutionMonsterId = evolutionMonsterId;
	}

	public int getEvolutionCatalystMonsterId() {
		return evolutionCatalystMonsterId;
	}

	public void setEvolutionCatalystMonsterId(int evolutionCatalystMonsterId) {
		this.evolutionCatalystMonsterId = evolutionCatalystMonsterId;
	}

	public int getMinutesToEvolve() {
		return minutesToEvolve;
	}

	public void setMinutesToEvolve(int minutesToEvolve) {
		this.minutesToEvolve = minutesToEvolve;
	}

	public int getNumCatalystsRequired() {
		return numCatalystsRequired;
	}

	public void setNumCatalystsRequired(int numCatalystsRequired) {
		this.numCatalystsRequired = numCatalystsRequired;
	}

	public String getCarrotRecruited() {
		return carrotRecruited;
	}

	public void setCarrotRecruited(String carrotRecruited) {
		this.carrotRecruited = carrotRecruited;
	}

	public String getCarrotDefeated() {
		return carrotDefeated;
	}

	public void setCarrotDefeated(String carrotDefeated) {
		this.carrotDefeated = carrotDefeated;
	}

	public String getCarrotEvolved() {
		return carrotEvolved;
	}

	public void setCarrotEvolved(String carrotEvolved) {
		this.carrotEvolved = carrotEvolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEvolutionCost() {
		return evolutionCost;
	}

	public void setEvolutionCost(int evolutionCost) {
		this.evolutionCost = evolutionCost;
	}

	public String getAnimationType() {
		return animationType;
	}

	public void setAnimationType(String animationType) {
		this.animationType = animationType;
	}

	public int getVerticalPixelOffset() {
		return verticalPixelOffset;
	}

	public void setVerticalPixelOffset(int verticalPixelOffset) {
		this.verticalPixelOffset = verticalPixelOffset;
	}

	public String getAtkSoundFile() {
		return atkSoundFile;
	}

	public void setAtkSoundFile(String atkSoundFile) {
		this.atkSoundFile = atkSoundFile;
	}

	public int getAtkSoundAnimationFrame() {
		return atkSoundAnimationFrame;
	}

	public void setAtkSoundAnimationFrame(int atkSoundAnimationFrame) {
		this.atkSoundAnimationFrame = atkSoundAnimationFrame;
	}

	public int getAtkAnimationRepeatedFramesStart() {
		return atkAnimationRepeatedFramesStart;
	}

	public void setAtkAnimationRepeatedFramesStart(
			int atkAnimationRepeatedFramesStart) {
		this.atkAnimationRepeatedFramesStart = atkAnimationRepeatedFramesStart;
	}

	public int getAtkAnimationRepeatedFramesEnd() {
		return atkAnimationRepeatedFramesEnd;
	}

	public void setAtkAnimationRepeatedFramesEnd(int atkAnimationRepeatedFramesEnd) {
		this.atkAnimationRepeatedFramesEnd = atkAnimationRepeatedFramesEnd;
	}

	public String getShorterName() {
		return shorterName;
	}

	public void setShorterName(String shorterName) {
		this.shorterName = shorterName;
	}

	public List<MonsterLevelInfo> getLvlInfo()
	{
		return lvlInfo;
	}
	public void setLvlInfo( List<MonsterLevelInfo> lvlInfo )
	{
		this.lvlInfo = lvlInfo;
	}
	public List<MonsterBattleDialogue> getBattleDialogue()
	{
		return battleDialogue;
	}
	public void setBattleDialogue( List<MonsterBattleDialogue> battleDialogue )
	{
		this.battleDialogue = battleDialogue;
	}
	
	@Override
	public String toString() {
		return "Monster [id=" + id + ", name=" + name + ", monsterGroup="
				+ monsterGroup + ", quality=" + quality + ", evolutionLevel="
				+ evolutionLevel + ", displayName=" + displayName
				+ ", element=" + element + ", imagePrefix=" + imagePrefix
				+ ", numPuzzlePieces=" + numPuzzlePieces
				+ ", minutesToCombinePieces=" + minutesToCombinePieces
				+ ", maxLevel=" + maxLevel + ", evolutionMonsterId="
				+ evolutionMonsterId + ", evolutionCatalystMonsterId="
				+ evolutionCatalystMonsterId + ", minutesToEvolve="
				+ minutesToEvolve + ", numCatalystsRequired="
				+ numCatalystsRequired + ", carrotRecruited=" + carrotRecruited
				+ ", carrotDefeated=" + carrotDefeated + ", carrotEvolved="
				+ carrotEvolved + ", description=" + description
				+ ", evolutionCost=" + evolutionCost + ", animationType="
				+ animationType + ", verticalPixelOffset="
				+ verticalPixelOffset + ", atkSoundFile=" + atkSoundFile
				+ ", atkSoundAnimationFrame=" + atkSoundAnimationFrame
				+ ", atkAnimationRepeatedFramesStart="
				+ atkAnimationRepeatedFramesStart
				+ ", atkAnimationRepeatedFramesEnd="
				+ atkAnimationRepeatedFramesEnd + ", shorterName="
				+ shorterName + "]";
	}
	
}
