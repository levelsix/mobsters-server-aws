package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NotificationTitleColor extends BasePersistentObject{
	
	private static final long serialVersionUID = 491813211027909557L;
	@Column(name = "red")
	private double red;
	@Column(name = "green")
	private double green;
	@Column(name = "blue")
	private double blue;  
	public NotificationTitleColor(){}
  public NotificationTitleColor(double red, double green, double blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public double getRed() {
    return red;
  }

  public double getGreen() {
    return green;
  }

  public double getBlue() {
    return blue;
  }

  @Override
  public String toString() {
    return "UnhandledBlacksmithAttempt [red=" + red + ", green=" + green
        + ", blue=" + blue + "]";
  }
  
}
