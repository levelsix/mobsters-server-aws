package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Referral extends BasePersistentObject{
	
	private static final long serialVersionUID = -3007773158955045515L;
	@Column(name = "referrer_id")
	private int referrerId;
	@Column(name = "newly_referred_id")
	private int newlyReferredId;
	@Column(name = "time_of_referral")
	private Date timeOfReferral;
	@Column(name = "coins_given_to_referrer")
	private int coinsGivenToReferrer;
	public Referral(){}
	public Referral(int referrerId, int newlyReferredId, Date timeOfReferral,
			int coinsGivenToReferrer) {
		this.referrerId = referrerId;
		this.newlyReferredId = newlyReferredId;
		this.timeOfReferral = timeOfReferral;
		this.coinsGivenToReferrer = coinsGivenToReferrer;
	}

	public int getReferrerId() {
		return referrerId;
	}

	public int getNewlyReferredId() {
		return newlyReferredId;
	}

	public Date getTimeOfReferral() {
		return timeOfReferral;
	}

	public int getCoinsGivenToReferrer() {
		return coinsGivenToReferrer;
	}

	@Override
	public String toString() {
		return "Referral [referrerId=" + referrerId + ", newlyReferredId="
				+ newlyReferredId + ", timeOfReferral=" + timeOfReferral
				+ ", coinsGivenToReferrer=" + coinsGivenToReferrer + "]";
	}
}
