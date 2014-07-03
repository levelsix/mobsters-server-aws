package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="Item")
@Table(name="item")
@Proxy(lazy=true, proxyClass=IItem.class)
public class Item extends BaseIntPersistentObject implements IItem{
	
	private static final long serialVersionUID = -3565410667892531712L;
	
	
	@Column(name = "name")
	private String name;
	@Column(name = "img_name")
	private String imgName;
	@Column(name = "border_img_name")
	private String borderImgName;
	@Column(name = "blue")
	private int blue; //colors for the border?
	@Column(name = "green")
	private int green;
	@Column(name = "red")
	private int red;	
	public Item(){}
	public Item(int id, String name, String imgName, String borderImgName,
			int blue, int green, int red) {
		super(id);
		this.name = name;
		this.imgName = imgName;
		this.borderImgName = borderImgName;
		this.blue = blue;
		this.green = green;
		this.red = red;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#getImgName()
	 */
	@Override
	public String getImgName() {
		return imgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#setImgName(java.lang.String)
	 */
	@Override
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#getBorderImgName()
	 */
	@Override
	public String getBorderImgName() {
		return borderImgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#setBorderImgName(java.lang.String)
	 */
	@Override
	public void setBorderImgName(String borderImgName) {
		this.borderImgName = borderImgName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#getBlue()
	 */
	@Override
	public int getBlue() {
		return blue;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#setBlue(int)
	 */
	@Override
	public void setBlue(int blue) {
		this.blue = blue;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#getGreen()
	 */
	@Override
	public int getGreen() {
		return green;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#setGreen(int)
	 */
	@Override
	public void setGreen(int green) {
		this.green = green;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#getRed()
	 */
	@Override
	public int getRed() {
		return red;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IItem#setRed(int)
	 */
	@Override
	public void setRed(int red) {
		this.red = red;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", imgName=" + imgName
				+ ", borderImgName=" + borderImgName + ", blue=" + blue
				+ ", green=" + green + ", red=" + red + "]";
	}
	
}
