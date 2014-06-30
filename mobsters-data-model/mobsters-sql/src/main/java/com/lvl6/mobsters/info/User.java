//package com.lvl6.mobsters.info;
//
//import javax.persistence.Column;
//import javax.persistence.ConstraintMode;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.ForeignKey;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.NamedAttributeNode;
//import javax.persistence.NamedEntityGraph;
//import javax.persistence.NamedEntityGraphs;
//import javax.persistence.NamedQuery;
//import javax.persistence.NamedSubgraph;
//import javax.persistence.Table;
//
//
///**
// * The persistent class for the user database table.
// * 
// */
//@Entity
//@Table(name="user")
//@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
//@NamedEntityGraphs({
//	@NamedEntityGraph(name="User.withClan", attributeNodes={
//		@NamedAttributeNode(value="clan", subgraph="Clan.withIcon")
//	}, subgraphs={
//		@NamedSubgraph(name="Clan.withIcon", attributeNodes={
//			@NamedAttributeNode(value="clanIcon")
//		})
//	})
//})
//public class User extends BasePersistentObject {
//	private static final long serialVersionUID = -5816575918761871837L;
//
//	private int cash;
//
//	private int experience;
//
//	private int gems;
//
//	@Column(name="is_admin")
//	private byte isAdmin;
//
//	private int level;
//
//	private String name;
//
//	private int oil;
//
//	// unidirectional many-to-one association to Clan
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name = "clan_id", foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
//	private Clan clan;
//
//	public User(final String id, final int cash, final int experience, final int gems, final byte isAdmin,
//			final int level, final String name, final int oil, final Clan clan) {
//		super(id);
//		this.cash = cash;
//		this.experience = experience;
//		this.gems = gems;
//		this.isAdmin = isAdmin;
//		this.level = level;
//		this.name = name;
//		this.oil = oil;
//		this.clan = clan;
//	}
//
//	public User() {
//	}
//
//	public int getCash() {
//		return this.cash;
//	}
//
//	public void setCash(int cash) {
//		this.cash = cash;
//	}
//
//	public int getExperience() {
//		return this.experience;
//	}
//
//	public void setExperience(int experience) {
//		this.experience = experience;
//	}
//
//	public int getGems() {
//		return this.gems;
//	}
//
//	public void setGems(int gems) {
//		this.gems = gems;
//	}
//
//	public byte getIsAdmin() {
//		return this.isAdmin;
//	}
//
//	public void setIsAdmin(byte isAdmin) {
//		this.isAdmin = isAdmin;
//	}
//
//	public int getLevel() {
//		return this.level;
//	}
//
//	public void setLevel(int level) {
//		this.level = level;
//	}
//
//	public String getName() {
//		return this.name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public int getOil() {
//		return this.oil;
//	}
//
//	public void setOil(int oil) {
//		this.oil = oil;
//	}
//
//	public Clan getClan() {
//		return this.clan;
//	}
//
//	public void setClan(Clan clan) {
//		this.clan = clan;
//	}
//}