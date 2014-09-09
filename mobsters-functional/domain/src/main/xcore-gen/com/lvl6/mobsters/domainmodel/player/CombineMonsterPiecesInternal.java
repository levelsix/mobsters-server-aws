/**
 */
package com.lvl6.mobsters.domainmodel.player;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Combine Monster Pieces Internal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal#getNewMonster <em>New Monster</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getCombineMonsterPiecesInternal()
 * @model
 * @generated
 */
public interface CombineMonsterPiecesInternal extends PendingOperationInternal {
	/**
	 * Returns the value of the '<em><b>New Monster</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Monster</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Monster</em>' reference.
	 * @see #setNewMonster(MonsterInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getCombineMonsterPiecesInternal_NewMonster()
	 * @model
	 * @generated
	 */
	MonsterInternal getNewMonster();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal#getNewMonster <em>New Monster</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Monster</em>' reference.
	 * @see #getNewMonster()
	 * @generated
	 */
	void setNewMonster(MonsterInternal value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='<%com.lvl6.mobsters.domainmodel.player.PlayerInternal%> _player = this.getPlayer();\n<%org.eclipse.emf.common.util.EList%><<%com.lvl6.mobsters.domainmodel.player.MonsterInternal%>> _monsters = _player.getMonsters();\n<%com.lvl6.mobsters.domainmodel.player.MonsterInternal%> _newMonster = this.getNewMonster();\n_monsters.add(_newMonster);\nreturn;'"
	 * @generated
	 */
	void happen();

} // CombineMonsterPiecesInternal
