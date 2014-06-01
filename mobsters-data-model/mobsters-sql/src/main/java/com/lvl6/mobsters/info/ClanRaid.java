package com.lvl6.mobsters.info;

//multiple clan raids can be available at the same time
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanRaid extends BasePersistentObject{

	
	private static final long serialVersionUID = 3611542670148402997L;
	@Column(name = "clan_raid_name")
	private String clanRaidName;
	@Column(name = "active_title_img_name")
	private String activeTitleImgName;
	@Column(name = "active_background_img_name")
	private String activeBackgroundImgName;
	@Column(name = "active_description")
	private String activeDescription;
	@Column(name = "inactive_monster_img_name")
	private String inactiveMonsterImgName;
	@Column(name = "inactive_description")
	private String inactiveDescription;
	@Column(name = "dialogue_text")
	private String dialogueText;
	@Column(name = "spotlight_monster_img_name")
	private String spotlightMonsterImgName;	
	public ClanRaid(){}
	public ClanRaid(String id, String clanRaidName, String activeTitleImgName,
			String activeBackgroundImgName, String activeDescription,
			String inactiveMonsterImgName, String inactiveDescription,
			String dialogueText, String spotlightMonsterImgName) {
		super(id);
		this.clanRaidName = clanRaidName;
		this.activeTitleImgName = activeTitleImgName;
		this.activeBackgroundImgName = activeBackgroundImgName;
		this.activeDescription = activeDescription;
		this.inactiveMonsterImgName = inactiveMonsterImgName;
		this.inactiveDescription = inactiveDescription;
		this.dialogueText = dialogueText;
		this.spotlightMonsterImgName = spotlightMonsterImgName;
	}



	public String getClanRaidName() {
		return clanRaidName;
	}

	public void setClanRaidName(String clanRaidName) {
		this.clanRaidName = clanRaidName;
	}

	public String getActiveTitleImgName() {
		return activeTitleImgName;
	}

	public void setActiveTitleImgName(String activeTitleImgName) {
		this.activeTitleImgName = activeTitleImgName;
	}

	public String getActiveBackgroundImgName() {
		return activeBackgroundImgName;
	}

	public void setActiveBackgroundImgName(String activeBackgroundImgName) {
		this.activeBackgroundImgName = activeBackgroundImgName;
	}

	public String getActiveDescription() {
		return activeDescription;
	}

	public void setActiveDescription(String activeDescription) {
		this.activeDescription = activeDescription;
	}

	public String getInactiveMonsterImgName() {
		return inactiveMonsterImgName;
	}

	public void setInactiveMonsterImgName(String inactiveMonsterImgName) {
		this.inactiveMonsterImgName = inactiveMonsterImgName;
	}

	public String getInactiveDescription() {
		return inactiveDescription;
	}

	public void setInactiveDescription(String inactiveDescription) {
		this.inactiveDescription = inactiveDescription;
	}

	public String getDialogueText() {
		return dialogueText;
	}

	public void setDialogueText(String dialogueText) {
		this.dialogueText = dialogueText;
	}

	public String getSpotlightMonsterImgName() {
		return spotlightMonsterImgName;
	}

	public void setSpotlightMonsterImgName(String spotlightMonsterImgName) {
		this.spotlightMonsterImgName = spotlightMonsterImgName;
	}

	@Override
	public String toString() {
		return "ClanRaid [id=" + id + ", clanRaidName=" + clanRaidName
				+ ", activeTitleImgName=" + activeTitleImgName
				+ ", activeBackgroundImgName=" + activeBackgroundImgName
				+ ", activeDescription=" + activeDescription
				+ ", inactiveMonsterImgName=" + inactiveMonsterImgName
				+ ", inactiveDescription=" + inactiveDescription + ", dialogueText="
				+ dialogueText + ", spotlightMonsterImgName=" + spotlightMonsterImgName
				+ "]";
	}
	
}
