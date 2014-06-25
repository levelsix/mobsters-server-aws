package com.lvl6.mobsters.info;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name="booster_pack")
@Proxy(lazy=false, proxyClass=IBoosterPack.class)
public class BoosterPack extends BaseIntPersistentObject implements IBoosterPack{	

	private static final long serialVersionUID = -7927101098350853116L;
	
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
	
	@OneToMany(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="boosterPack", //the exact property name (verbatim) in BoosterItem.java 
		orphanRemoval=true,
		targetEntity=BoosterItem.class)
	private List<IBoosterItem> packItems;  
	

	@OneToMany(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="boosterPack", //the exact property name (verbatim) in BoosterDisplayItem.java 
		orphanRemoval=true,
		targetEntity=BoosterDisplayItem.class)
	private List<IBoosterDisplayItem> packDisplayItems;
	
	public BoosterPack(){}
	public BoosterPack(int id, String name, int gemPrice,
			String listBackgroundImgName, String listDescription,
			String navBarImgName, String navTitleImgName, String machineImgName,
			List<BoosterItem> packItems, List<BoosterDisplayItem> packDisplayItems) {
		super(id);
		this.name = name;
		this.gemPrice = gemPrice;
		this.listBackgroundImgName = listBackgroundImgName;
		this.listDescription = listDescription;
		this.navBarImgName = navBarImgName;
		this.navTitleImgName = navTitleImgName;
		this.machineImgName = machineImgName;
		this.setPackItems(new ArrayList<IBoosterItem>(packItems));
		this.setPackDisplayItems(new ArrayList<IBoosterDisplayItem>(packDisplayItems));
	}



	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getGemPrice() {
		return gemPrice;
	}

	@Override
	public void setGemPrice(int gemPrice) {
		this.gemPrice = gemPrice;
	}

	@Override
	public String getListBackgroundImgName() {
		return listBackgroundImgName;
	}

	@Override
	public void setListBackgroundImgName(String listBackgroundImgName) {
		this.listBackgroundImgName = listBackgroundImgName;
	}

	@Override
	public String getListDescription() {
		return listDescription;
	}

	@Override
	public void setListDescription(String listDescription) {
		this.listDescription = listDescription;
	}

	@Override
	public String getNavBarImgName() {
		return navBarImgName;
	}

	@Override
	public void setNavBarImgName(String navBarImgName) {
		this.navBarImgName = navBarImgName;
	}

	@Override
	public String getNavTitleImgName() {
		return navTitleImgName;
	}

	@Override
	public void setNavTitleImgName(String navTitleImgName) {
		this.navTitleImgName = navTitleImgName;
	}

	@Override
	public String getMachineImgName() {
		return machineImgName;
	}

	@Override
	public void setMachineImgName(String machineImgName) {
		this.machineImgName = machineImgName;
	}

	@Override
	public List<IBoosterItem> getPackItems()
	{
		return packItems;
	}
	@Override
	public void setPackItems( List<IBoosterItem> packItems )
	{
		this.packItems = packItems;
	}
	
	@Override
	public List<IBoosterDisplayItem> getPackDisplayItems()
	{
		return packDisplayItems;
	}
	@Override
	public void setPackDisplayItems( List<IBoosterDisplayItem> packDisplayItems )
	{
		this.packDisplayItems = packDisplayItems;
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
