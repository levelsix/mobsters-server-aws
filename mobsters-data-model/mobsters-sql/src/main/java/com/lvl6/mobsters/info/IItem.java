package com.lvl6.mobsters.info;

public interface IItem extends IBaseIntPersistentObject
{

	public String getName();

	public void setName( String name );

	public String getImgName();

	public void setImgName( String imgName );

	public String getBorderImgName();

	public void setBorderImgName( String borderImgName );

	public int getBlue();

	public void setBlue( int blue );

	public int getGreen();

	public void setGreen( int green );

	public int getRed();

	public void setRed( int red );

}