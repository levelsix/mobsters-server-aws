package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BoosterPack extends BasePersistentObject{	

	
	private static final long serialVersionUID = -5200606492974418991L;
	@Column(name = "name")
	private String name;
	@Column(name = "gem_price")
	private int gemPrice;
	@Column(name = "list_background_img_name")
	private String listBackgroundImgName;
	@Column(name = "list_description")
	private String listDescription;
	@Column(name = "nav_bar_img_name")
	private String navBarImgName;
	@Column(name = "nav_title_img_name")
	private String navTitleImgName;
	@Column(name = "machine_img_name")
	private String machineImgName;  
	public BoosterPack(){}
	public BoosterPack(int id, String name, int gemPrice,
			String listBackgroundImgName, String listDescription,
			String navBarImgName, String navTitleImgName, String machineImgName) {
		super();
		this.name = name;
		this.gemPrice = gemPrice;
		this.listBackgroundImgName = listBackgroundImgName;
		this.listDescription = listDescription;
		this.navBarImgName = navBarImgName;
		this.navTitleImgName = navTitleImgName;
		this.machineImgName = machineImgName;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGemPrice() {
		return gemPrice;
	}

	public void setGemPrice(int gemPrice) {
		this.gemPrice = gemPrice;
	}

	public String getListBackgroundImgName() {
		return listBackgroundImgName;
	}

	public void setListBackgroundImgName(String listBackgroundImgName) {
		this.listBackgroundImgName = listBackgroundImgName;
	}

	public String getListDescription() {
		return listDescription;
	}

	public void setListDescription(String listDescription) {
		this.listDescription = listDescription;
	}

	public String getNavBarImgName() {
		return navBarImgName;
	}

	public void setNavBarImgName(String navBarImgName) {
		this.navBarImgName = navBarImgName;
	}

	public String getNavTitleImgName() {
		return navTitleImgName;
	}

	public void setNavTitleImgName(String navTitleImgName) {
		this.navTitleImgName = navTitleImgName;
	}

	public String getMachineImgName() {
		return machineImgName;
	}

	public void setMachineImgName(String machineImgName) {
		this.machineImgName = machineImgName;
	}

	@Override
	public String toString() {
		return "BoosterPack [id=" + id + ", name=" + name + ", gemPrice="
				+ gemPrice + ", listBackgroundImgName=" + listBackgroundImgName
				+ ", listDescription=" + listDescription + ", navBarImgName="
				+ navBarImgName + ", navTitleImgName=" + navTitleImgName
				+ ", machineImgName=" + machineImgName + "]";
	}
	
}
