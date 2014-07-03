package com.lvl6.mobsters.info;

import java.util.List;

public interface IBoosterPack extends IBaseIntPersistentObject
{
	public void setName( String name );

	public String getName();

	public void setPackItems( List<IBoosterItem> packItems );

	public List<IBoosterItem> getPackItems();

	public void setMachineImgName( String machineImgName );

	public String getMachineImgName();

	public void setNavTitleImgName( String navTitleImgName );

	public String getNavTitleImgName();

	public void setNavBarImgName( String navBarImgName );

	public String getNavBarImgName();

	public void setListDescription( String listDescription );

	public String getListDescription();

	public void setListBackgroundImgName( String listBackgroundImgName );

	public String getListBackgroundImgName();

	public void setGemPrice( int gemPrice );

	public int getGemPrice();


	public void setPackDisplayItems( List<IBoosterDisplayItem> packDisplayItems );

	public List<IBoosterDisplayItem> getPackDisplayItems();

}