package com.lvl6.properties;




public class Globals {
	
	
	protected String appleBundleId;
	protected String appStoreUrl;
	protected String reviewPageUrl;
	protected boolean kabamEnabled = true;
	protected boolean offerChartEnabled = false;

	protected boolean addAllFbFriends = false;
	protected boolean sandbox = true;
	protected boolean iddictionOn = true;
	protected float versionNumber = 1.0f;
	protected int healthCheckTimeoutSeconds = 6;
	protected int initialDiamonds = 20;
	
	public boolean isOfferChartEnabled() {
		return offerChartEnabled;
	}
	
	public void setOfferChartEnabled(boolean offerChartEnabled) {
		this.offerChartEnabled = offerChartEnabled;
	}
	
	
	public int getInitialDiamonds() {
		return initialDiamonds;
	}

	public void setInitialDiamonds(int initialDiamonds) {
		this.initialDiamonds = initialDiamonds;
	}

	public boolean isKabamEnabled() {
		return kabamEnabled;
	}

	public void setKabamEnabled(boolean kabamEnabled) {
		this.kabamEnabled = kabamEnabled;
	}

	public String getAppleBundleId() {
		return appleBundleId;
	}

	public void setAppleBundleId(String appleBundleId) {
		this.appleBundleId = appleBundleId;
	}

	public String getAppStoreUrl() {
		return appStoreUrl;
	}

	public void setAppStoreUrl(String appStoreUrl) {
		this.appStoreUrl = appStoreUrl;
	}

	public String getReviewPageUrl() {
		return reviewPageUrl;
	}

	public void setReviewPageUrl(String reviewPageUrl) {
		this.reviewPageUrl = reviewPageUrl;
	}

    public int getHealthCheckTimeoutSeconds() {
		return healthCheckTimeoutSeconds;
	}

	public void setHealthCheckTimeoutSeconds(int healthCheckTimeoutSeconds) {
		this.healthCheckTimeoutSeconds = healthCheckTimeoutSeconds;
	}

	public float getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(float versionNumber) {
		this.versionNumber = versionNumber;
	}

	public boolean isIddictionOn() {
		return iddictionOn;
	}

	public void setIddictionOn(boolean iddictionOn) {
		this.iddictionOn = iddictionOn;
	}

	public boolean getSandbox() {
		return sandbox;
	}

	public void setSandbox(boolean isSandbox) {
		this.sandbox = isSandbox;
	}

	public boolean isAddAllFbFriends() {
		return addAllFbFriends;
	}

	public void setAddAllFbFriends(boolean addAllFbFriends) {
		this.addAllFbFriends = addAllFbFriends;
	}


	/** size of ByteBuffer for reading/writing from channels */

    public static final int NET_BUFFER_SIZE=16384*64;

    /** maximum event size in bytes */
    public static final int MAX_EVENT_SIZE=16384*64;

    /** interval to sleep between attempts to write to a channel. */
    public static final long CHANNEL_WRITE_SLEEP = 10L;

    /** number of worker threads for EventWriter */
    public static final int EVENT_WRITER_WORKERS = 5;
    
    /** number of worker threads for APNSWriter */
    public static final int APNS_WRITER_WORKERS = 5;

    /** default number of workers for GameControllers */
    public static final int DEFAULT_CONTROLLER_WORKERS = 2;

    public static final String REVIEW_PAGE_CONFIRMATION_MESSAGE = "Awesome! Rate us 5 Stars in the App Store to keep the updates coming!";
    
    //public static final Level LOG_LEVEL = Level.INFO;
    
    public static final int NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING = 1;


}