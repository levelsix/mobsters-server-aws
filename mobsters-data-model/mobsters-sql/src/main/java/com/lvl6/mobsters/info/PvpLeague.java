package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="PvpLeague")
@Table(name="pvp_league")
@Proxy(lazy=true, proxyClass=IPvpLeague.class)
public class PvpLeague extends BaseIntPersistentObject implements IPvpLeague{

	private static final long serialVersionUID = -5973475549260002542L;
	
	
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
	public PvpLeague(int id, String leagueName, String imgPrefix, int numRanks,
			String description, int minElo, int maxElo) {
		super(id);
		this.leagueName = leagueName;
		this.imgPrefix = imgPrefix;
		this.numRanks = numRanks;
		this.description = description;
		this.minElo = minElo;
		this.maxElo = maxElo;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#getLeagueName()
	 */
	@Override
	public String getLeagueName() {
		return leagueName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#setLeagueName(java.lang.String)
	 */
	@Override
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#getImgPrefix()
	 */
	@Override
	public String getImgPrefix() {
		return imgPrefix;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#setImgPrefix(java.lang.String)
	 */
	@Override
	public void setImgPrefix(String imgPrefix) {
		this.imgPrefix = imgPrefix;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#getNumRanks()
	 */
	@Override
	public int getNumRanks() {
		return numRanks;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#setNumRanks(int)
	 */
	@Override
	public void setNumRanks(int numRanks) {
		this.numRanks = numRanks;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#getMinElo()
	 */
	@Override
	public int getMinElo() {
		return minElo;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#setMinElo(int)
	 */
	@Override
	public void setMinElo(int minElo) {
		this.minElo = minElo;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#getMaxElo()
	 */
	@Override
	public int getMaxElo() {
		return maxElo;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IPvpLeague#setMaxElo(int)
	 */
	@Override
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
