/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.ItemInternal;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;

import com.lvl6.mobsters.info.IItem;

import java.util.UUID;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Item Internal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.ItemInternalImpl#getItemUuid
 * <em>Item Uuid</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.ItemInternalImpl#getPlayer
 * <em>Player</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.ItemInternalImpl#getItemMeta
 * <em>Item Meta</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ItemInternalImpl extends MinimalEObjectImpl.Container implements
		ItemInternal {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ItemInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.ITEM_INTERNAL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public UUID getItemUuid() {
		return (UUID) eGet(
				MobstersPlayerPackage.Literals.ITEM_INTERNAL__ITEM_UUID, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setItemUuid(UUID newItemUuid) {
		eSet(MobstersPlayerPackage.Literals.ITEM_INTERNAL__ITEM_UUID,
				newItemUuid);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PlayerInternal getPlayer() {
		return (PlayerInternal) eGet(
				MobstersPlayerPackage.Literals.ITEM_INTERNAL__PLAYER, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setPlayer(PlayerInternal newPlayer) {
		eSet(MobstersPlayerPackage.Literals.ITEM_INTERNAL__PLAYER, newPlayer);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IItem getItemMeta() {
		return (IItem) eGet(
				MobstersPlayerPackage.Literals.ITEM_INTERNAL__ITEM_META, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setItemMeta(IItem newItemMeta) {
		eSet(MobstersPlayerPackage.Literals.ITEM_INTERNAL__ITEM_META,
				newItemMeta);
	}

} // ItemInternalImpl
