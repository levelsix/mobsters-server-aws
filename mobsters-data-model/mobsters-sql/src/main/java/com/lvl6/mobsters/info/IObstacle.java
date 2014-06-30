package com.lvl6.mobsters.info;

public interface IObstacle extends IBaseIntPersistentObject
{

	public String getName();

	public void setName( String name );

	public String getRemovalCostType();

	public void setRemovalCostType( String removalCostType );

	public int getCost();

	public void setCost( int cost );

	public int getSecondsToRemove();

	public void setSecondsToRemove( int secondsToRemove );

	public int getWidth();

	public void setWidth( int width );

	public int getHeight();

	public void setHeight( int height );

	public String getImgName();

	public void setImgName( String imgName );

	public float getImgVerticalPixelOffset();

	public void setImgVerticalPixelOffset( float imgVerticalPixelOffset );

	public String getDescription();

	public void setDescription( String description );

	public float getChanceToAppear();

	public void setChanceToAppear( float chanceToAppear );

	public String getShadowImgName();

	public void setShadowImgName( String shadowImgName );

	public float getShadowVerticalOffset();

	public void setShadowVerticalOffset( float shadowVerticalOffset );

	public float getShadowHorizontalOffset();

	public void setShadowHorizontalOffset( float shadowHorizontalOffset );

}