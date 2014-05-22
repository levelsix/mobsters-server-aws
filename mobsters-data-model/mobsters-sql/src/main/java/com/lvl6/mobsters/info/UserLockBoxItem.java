package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserLockBoxItem extends BasePersistentObject{  

	@Column(name = "lock_box_item_id")
	private int lockBoxItemId;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "quantity")
	private int quantity;  
	public UserLockBoxItem(){}
  public UserLockBoxItem(int lockBoxItemId, int userId,
      int quantity) {
   
    this.lockBoxItemId = lockBoxItemId;
    this.userId = userId;
    this.quantity = quantity;
  }

  public int getLockBoxItemId() {
    return lockBoxItemId;
  }

  public void setLockBoxItemId(int lockBoxItemId) {
    this.lockBoxItemId = lockBoxItemId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "UserLockBoxItem [lockBoxItemId=" + lockBoxItemId + ", userId="
        + userId + ", quantity=" + quantity + "]";
  }

}
