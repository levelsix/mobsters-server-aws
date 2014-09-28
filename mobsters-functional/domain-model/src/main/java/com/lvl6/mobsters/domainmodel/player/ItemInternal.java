/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.IItem;

import java.util.UUID;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Item Internal</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemUuid <em>
 * Item Uuid</em>}</li>
 * <li>{@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getPlayer <em>
 * Player</em>}</li>
 * <li>{@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemMeta <em>
 * Item Meta</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getItemInternal()
 * @model
 * @generated
 */
public interface ItemInternal extends Item {
	/**
	 * Returns the value of the '<em><b>Item Uuid</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Item Uuid</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Item Uuid</em>' attribute.
	 * @see #setItemUuid(UUID)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getItemInternal_ItemUuid()
	 * @model unique="false" id="true"
	 *        dataType="com.lvl6.mobsters.domainmodel.player.UUID"
	 * @generated
	 */
	UUID getItemUuid();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemUuid
	 * <em>Item Uuid</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Item Uuid</em>' attribute.
	 * @see #getItemUuid()
	 * @generated
	 */
	void setItemUuid(UUID value);

	/**
	 * Returns the value of the '<em><b>Player</b></em>' container reference. It
	 * is bidirectional and its opposite is '
	 * {@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getItems
	 * <em>Items</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Player</em>' container reference.
	 * @see #setPlayer(PlayerInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getItemInternal_Player()
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getItems
	 * @model opposite="items" transient="false"
	 * @generated
	 */
	PlayerInternal getPlayer();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getPlayer
	 * <em>Player</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Player</em>' container reference.
	 * @see #getPlayer()
	 * @generated
	 */
	void setPlayer(PlayerInternal value);

	/**
	 * Returns the value of the '<em><b>Item Meta</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Item Meta</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Item Meta</em>' attribute.
	 * @see #setItemMeta(IItem)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getItemInternal_ItemMeta()
	 * @model unique="false"
	 *        dataType="com.lvl6.mobsters.domainmodel.player.IItem"
	 * @generated
	 */
	IItem getItemMeta();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemMeta
	 * <em>Item Meta</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Item Meta</em>' attribute.
	 * @see #getItemMeta()
	 * @generated
	 */
	void setItemMeta(IItem value);

} // ItemInternal
