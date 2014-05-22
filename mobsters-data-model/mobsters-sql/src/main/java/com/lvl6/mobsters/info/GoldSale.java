package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class GoldSale extends BasePersistentObject{
	
	private static final long serialVersionUID = 7996839582751539192L;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	@Column(name = "package1_sale_identifier")
	private String package1SaleIdentifier;
	@Column(name = "package2_sale_identifier")
	private String package2SaleIdentifier;
	@Column(name = "package3_sale_identifier")
	private String package3SaleIdentifier;
	@Column(name = "package4_sale_identifier")
	private String package4SaleIdentifier;
	@Column(name = "package5_sale_identifier")
	private String package5SaleIdentifier;
	@Column(name = "package_s1_sale_identifier")
	private String packageS1SaleIdentifier;
	@Column(name = "package_s2_sale_identifier")
	private String packageS2SaleIdentifier;
	@Column(name = "package_s3_sale_identifier")
	private String packageS3SaleIdentifier;
	@Column(name = "package_s4_sale_identifier")
	private String packageS4SaleIdentifier;
	@Column(name = "package_s5_sale_identifier")
	private String packageS5SaleIdentifier;
	@Column(name = "gold_shoppe_image_name")
	private String goldShoppeImageName;
	@Column(name = "gold_bar_image_name")
	private String goldBarImageName;
	@Column(name = "is_beginner_sale")
	private boolean isBeginnerSale;  
	public GoldSale(){}
  public GoldSale(int id, Date startDate, Date endDate, String goldShoppeImageName,
      String goldBarImageName, String package1SaleIdentifier, 
      String package2SaleIdentifier, String package3SaleIdentifier, String package4SaleIdentifier,
      String package5SaleIdentifier, String packageS1SaleIdentifier, String packageS2SaleIdentifier,
      String packageS3SaleIdentifier, String packageS4SaleIdentifier, String packageS5SaleIdentifier,
      boolean isBeginnerSale) {
    super();
    this.startDate = startDate;
    this.endDate = endDate;
    this.package1SaleIdentifier = package1SaleIdentifier;
    this.package2SaleIdentifier = package2SaleIdentifier;
    this.package3SaleIdentifier = package3SaleIdentifier;
    this.package4SaleIdentifier = package4SaleIdentifier;
    this.package5SaleIdentifier = package5SaleIdentifier;
    this.packageS1SaleIdentifier = packageS1SaleIdentifier;
    this.packageS2SaleIdentifier = packageS2SaleIdentifier;
    this.packageS3SaleIdentifier = packageS3SaleIdentifier;
    this.packageS4SaleIdentifier = packageS4SaleIdentifier;
    this.packageS5SaleIdentifier = packageS5SaleIdentifier;
    this.goldShoppeImageName = goldShoppeImageName;
    this.goldBarImageName = goldBarImageName;
    this.isBeginnerSale = isBeginnerSale;
  }

  public boolean isBeginnerSale() {
    return isBeginnerSale;
  }

  public void setBeginnerSale(boolean isBeginnerSale) {
    this.isBeginnerSale = isBeginnerSale;
  }



  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getPackage1SaleIdentifier() {
    return package1SaleIdentifier;
  }

  public void setPackage1SaleIdentifier(String package1SaleIdentifier) {
    this.package1SaleIdentifier = package1SaleIdentifier;
  }

  public String getPackage2SaleIdentifier() {
    return package2SaleIdentifier;
  }

  public void setPackage2SaleIdentifier(String package2SaleIdentifier) {
    this.package2SaleIdentifier = package2SaleIdentifier;
  }

  public String getPackage3SaleIdentifier() {
    return package3SaleIdentifier;
  }

  public void setPackage3SaleIdentifier(String package3SaleIdentifier) {
    this.package3SaleIdentifier = package3SaleIdentifier;
  }

  public String getPackage4SaleIdentifier() {
    return package4SaleIdentifier;
  }

  public void setPackage4SaleIdentifier(String package4SaleIdentifier) {
    this.package4SaleIdentifier = package4SaleIdentifier;
  }

  public String getPackage5SaleIdentifier() {
    return package5SaleIdentifier;
  }

  public void setPackage5SaleIdentifier(String package5SaleIdentifier) {
    this.package5SaleIdentifier = package5SaleIdentifier;
  }

  public String getGoldShoppeImageName() {
    return goldShoppeImageName;
  }

  public void setGoldShoppeImageName(String goldShoppeImageName) {
    this.goldShoppeImageName = goldShoppeImageName;
  }

  public String getGoldBarImageName() {
    return goldBarImageName;
  }

  public void setGoldBarImageName(String goldBarImageName) {
    this.goldBarImageName = goldBarImageName;
  }

  public String getPackageS1SaleIdentifier() {
    return packageS1SaleIdentifier;
  }

  public void setPackageS1SaleIdentifier(String packageS1SaleIdentifier) {
    this.packageS1SaleIdentifier = packageS1SaleIdentifier;
  }

  public String getPackageS2SaleIdentifier() {
    return packageS2SaleIdentifier;
  }

  public void setPackageS2SaleIdentifier(String packageS2SaleIdentifier) {
    this.packageS2SaleIdentifier = packageS2SaleIdentifier;
  }

  public String getPackageS3SaleIdentifier() {
    return packageS3SaleIdentifier;
  }

  public void setPackageS3SaleIdentifier(String packageS3SaleIdentifier) {
    this.packageS3SaleIdentifier = packageS3SaleIdentifier;
  }

  public String getPackageS4SaleIdentifier() {
    return packageS4SaleIdentifier;
  }

  public void setPackageS4SaleIdentifier(String packageS4SaleIdentifier) {
    this.packageS4SaleIdentifier = packageS4SaleIdentifier;
  }

  public String getPackageS5SaleIdentifier() {
    return packageS5SaleIdentifier;
  }

  public void setPackageS5SaleIdentifier(String packageS5SaleIdentifier) {
    this.packageS5SaleIdentifier = packageS5SaleIdentifier;
  }

  @Override
  public String toString() {
    return "GoldSale [id=" + id + ", startDate=" + startDate + ", endDate="
        + endDate + ", package1SaleIdentifier=" + package1SaleIdentifier
        + ", package2SaleIdentifier=" + package2SaleIdentifier
        + ", package3SaleIdentifier=" + package3SaleIdentifier
        + ", package4SaleIdentifier=" + package4SaleIdentifier
        + ", package5SaleIdentifier=" + package5SaleIdentifier
        + ", packageS1SaleIdentifier=" + packageS1SaleIdentifier
        + ", packageS2SaleIdentifier=" + packageS2SaleIdentifier
        + ", packageS3SaleIdentifier=" + packageS3SaleIdentifier
        + ", packageS4SaleIdentifier=" + packageS4SaleIdentifier
        + ", packageS5SaleIdentifier=" + packageS5SaleIdentifier
        + ", goldShoppeImageName=" + goldShoppeImageName
        + ", goldBarImageName=" + goldBarImageName + "]";
  }
}
