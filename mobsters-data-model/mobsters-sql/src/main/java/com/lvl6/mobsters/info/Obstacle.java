package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="Obstacle")
@Table(name="obstacle")
@Proxy(lazy=true, proxyClass=IObstacle.class)
public class Obstacle extends BaseIntPersistentObject implements IObstacle{

	private static final long serialVersionUID = -2903890974110126843L;
	
	
	@Column(name = "name")
	private String name;
	@Column(name = "removal_cost_type")
	private String removalCostType;
	@Column(name = "cost")
	private int cost;
	@Column(name = "seconds_to_remove")
	private int secondsToRemove;
	@Column(name = "width")
	private int width; //how many tiles it covers in the map
	@Column(name = "height")
	private int height; //how many tiles it covers in the map
	@Column(name = "img_name")
	private String imgName;
	@Column(name = "img_vertical_pixel_offset")
	private float imgVerticalPixelOffset;
	@Column(name = "description")
	private String description;
	@Column(name = "chance_to_appear")
	private float chanceToAppear;
	
	@Column(name = "shadow_img_name")
	private String shadowImgName;
	@Column(name = "shadow_vertical_offset")
	private float shadowVerticalOffset;
	@Column(name = "shadow_horizontal_offset")
	private float shadowHorizontalOffset;
  
	public Obstacle(){}
	public Obstacle(int id, String name, String removalCostType, int cost,
			int secondsToRemove, int width, int height, String imgName,
			float imgVerticalPixelOffset, String description, float chanceToAppear,
			String shadowImgName, float shadowVerticalOffset,
			float shadowHorizontalOffset) {
		super(id);
		this.name = name;
		this.removalCostType = removalCostType;
		this.cost = cost;
		this.secondsToRemove = secondsToRemove;
		this.width = width;
		this.height = height;
		this.imgName = imgName;
		this.imgVerticalPixelOffset = imgVerticalPixelOffset;
		this.description = description;
		this.chanceToAppear = chanceToAppear;
		this.shadowImgName = shadowImgName;
		this.shadowVerticalOffset = shadowVerticalOffset;
		this.shadowHorizontalOffset = shadowHorizontalOffset;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getRemovalCostType()
	 */
	@Override
	public String getRemovalCostType() {
		return removalCostType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setRemovalCostType(java.lang.String)
	 */
	@Override
	public void setRemovalCostType(String removalCostType) {
		this.removalCostType = removalCostType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getCost()
	 */
	@Override
	public int getCost() {
		return cost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setCost(int)
	 */
	@Override
	public void setCost(int cost) {
		this.cost = cost;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getSecondsToRemove()
	 */
	@Override
	public int getSecondsToRemove() {
		return secondsToRemove;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setSecondsToRemove(int)
	 */
	@Override
	public void setSecondsToRemove(int secondsToRemove) {
		this.secondsToRemove = secondsToRemove;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getImgName()
	 */
	@Override
	public String getImgName() {
		return imgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setImgName(java.lang.String)
	 */
	@Override
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getImgVerticalPixelOffset()
	 */
	@Override
	public float getImgVerticalPixelOffset() {
		return imgVerticalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setImgVerticalPixelOffset(float)
	 */
	@Override
	public void setImgVerticalPixelOffset(float imgVerticalPixelOffset) {
		this.imgVerticalPixelOffset = imgVerticalPixelOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getChanceToAppear()
	 */
	@Override
	public float getChanceToAppear() {
		return chanceToAppear;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setChanceToAppear(float)
	 */
	@Override
	public void setChanceToAppear(float chanceToAppear) {
		this.chanceToAppear = chanceToAppear;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getShadowImgName()
	 */
	@Override
	public String getShadowImgName() {
		return shadowImgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setShadowImgName(java.lang.String)
	 */
	@Override
	public void setShadowImgName(String shadowImgName) {
		this.shadowImgName = shadowImgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getShadowVerticalOffset()
	 */
	@Override
	public float getShadowVerticalOffset() {
		return shadowVerticalOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setShadowVerticalOffset(float)
	 */
	@Override
	public void setShadowVerticalOffset(float shadowVerticalOffset) {
		this.shadowVerticalOffset = shadowVerticalOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#getShadowHorizontalOffset()
	 */
	@Override
	public float getShadowHorizontalOffset() {
		return shadowHorizontalOffset;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IObstacle#setShadowHorizontalOffset(float)
	 */
	@Override
	public void setShadowHorizontalOffset(float shadowHorizontalOffset) {
		this.shadowHorizontalOffset = shadowHorizontalOffset;
	}

	@Override
	public String toString() {
		return "Obstacle [id=" + id + ", name=" + name + ", removalCostType="
				+ removalCostType + ", cost=" + cost + ", secondsToRemove="
				+ secondsToRemove + ", width=" + width + ", height=" + height
				+ ", imgName=" + imgName + ", imgVerticalPixelOffset="
				+ imgVerticalPixelOffset + ", description=" + description
				+ ", chanceToAppear=" + chanceToAppear + ", shadowImgName="
				+ shadowImgName + ", shadowVerticalOffset=" + shadowVerticalOffset
				+ ", shadowHorizontalOffset=" + shadowHorizontalOffset + "]";
	}
	
}
