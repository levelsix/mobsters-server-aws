package com.lvl6.mobsters.info;

public interface IPvpLeague extends IBaseIntPersistentObject
{

	public String getLeagueName();

	public void setLeagueName( String leagueName );

	public String getImgPrefix();

	public void setImgPrefix( String imgPrefix );

	public int getNumRanks();

	public void setNumRanks( int numRanks );

	public String getDescription();

	public void setDescription( String description );

	public int getMinElo();

	public void setMinElo( int minElo );

	public int getMaxElo();

	public void setMaxElo( int maxElo );

}