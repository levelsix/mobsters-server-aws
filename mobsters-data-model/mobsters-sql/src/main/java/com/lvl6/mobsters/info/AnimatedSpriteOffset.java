package com.lvl6.mobsters.info;

public class AnimatedSpriteOffset
{
	private String imgName;

	private CoordinatePair offSet;

	public AnimatedSpriteOffset()
	{}

	public AnimatedSpriteOffset( final String imgName, final CoordinatePair offSet )
	{
		this.imgName = imgName;
		this.offSet = offSet;
	}

	// public String getId()
	// {
	// return id;
	// }
	//
	// public void setId( final String id )
	// {
	// this.id = id;
	// }

	public String getImgName()
	{
		return imgName;
	}

	public CoordinatePair getOffSet()
	{
		return offSet;
	}
}