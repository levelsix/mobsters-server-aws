package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BlacksmithAttemptHistory extends BasePersistentObject{

	@Column(name = "blacksmith_attempt")
	private BlacksmithAttempt blacksmithAttempt;
	@Column(name = "success")
	private boolean success;  
	public BlacksmithAttemptHistory(){}
  public BlacksmithAttemptHistory(BlacksmithAttempt blacksmithAttempt,
      boolean success) {
    this.blacksmithAttempt = blacksmithAttempt;
    this.success = success;
  }

  public BlacksmithAttempt getBlacksmithAttempt() {
    return blacksmithAttempt;
  }

  public boolean isSuccess() {
    return success;
  }

  @Override
  public String toString() {
    return "BlacksmithAttemptHistory [blacksmithAttempt=" + blacksmithAttempt
        + ", success=" + success + "]";
  }
  
}
