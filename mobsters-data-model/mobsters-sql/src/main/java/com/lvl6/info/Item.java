package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Item extends BasePersistentObject{

	
	private static final long serialVersionUID = 4660675777685331403L;	

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
		super();
		this.name = name;
		this.imgName = imgName;
		this.borderImgName = borderImgName;
		this.blue = blue;
		this.green = green;
		this.red = red;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getBorderImgName() {
		return borderImgName;
	}

	public void setBorderImgName(String borderImgName) {
		this.borderImgName = borderImgName;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getRed() {
		return red;
	}

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
