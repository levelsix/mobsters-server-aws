package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PvpLeague extends BasePersistentObject{

	
	private static final long serialVersionUID = 4071892938955324752L;	

	@Column(name = "league_name")
	private String leagueName;
	@Column(name = "img_prefix")
	private String imgPrefix;
	@Column(name = "num_ranks")
	private int numRanks;
	@Column(name = "description")
	private String description;
	@Column(name = "min_elo")
	private int minElo;
	@Column(name = "max_elo")
	private int maxElo;	
	public PvpLeague(){}
	public PvpLeague(String id, String leagueName, String imgPrefix, int numRanks,
			String description, int minElo, int maxElo) {
		super(id);
		this.leagueName = leagueName;
		this.imgPrefix = imgPrefix;
		this.numRanks = numRanks;
		this.description = description;
		this.minElo = minElo;
		this.maxElo = maxElo;
	}



	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getImgPrefix() {
		return imgPrefix;
	}

	public void setImgPrefix(String imgPrefix) {
		this.imgPrefix = imgPrefix;
	}

	public int getNumRanks() {
		return numRanks;
	}

	public void setNumRanks(int numRanks) {
		this.numRanks = numRanks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinElo() {
		return minElo;
	}

	public void setMinElo(int minElo) {
		this.minElo = minElo;
	}

	public int getMaxElo() {
		return maxElo;
	}

	public void setMaxElo(int maxElo) {
		this.maxElo = maxElo;
	}

	@Override
	public String toString() {
		return "PvpLeague [id=" + id + ", leagueName=" + leagueName
				+ ", imgPrefix=" + imgPrefix + ", numRanks=" + numRanks
				+ ", description=" + description + ", minElo=" + minElo
				+ ", maxElo=" + maxElo + "]";
	}
	
}
