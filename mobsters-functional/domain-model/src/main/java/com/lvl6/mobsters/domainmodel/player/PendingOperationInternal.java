/**
 */
package com.lvl6.mobsters.domainmodel.player;

import java.util.Date;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Pending Operation Internal</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getPlayer
 * <em>Player</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpStartTimer
 * <em>Op Start Timer</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpEndTimer
 * <em>Op End Timer</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPendingOperationInternal()
 * @model abstract="true"
 * @generated
 */
public interface PendingOperationInternal extends PendingOperation {
	/**
	 * Returns the value of the '<em><b>Player</b></em>' container reference. It
	 * is bidirectional and its opposite is '
	 * {@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getPendingOperations
	 * <em>Pending Operations</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Player</em>' container reference.
	 * @see #setPlayer(PlayerInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPendingOperationInternal_Player()
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getPendingOperations
	 * @model opposite="pendingOperations" transient="false"
	 * @generated
	 */
	PlayerInternal getPlayer();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getPlayer
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
	 * Returns the value of the '<em><b>Op Start Timer</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Op Start Timer</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Op Start Timer</em>' attribute.
	 * @see #setOpStartTimer(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPendingOperationInternal_OpStartTimer()
	 * @model unique="false"
	 * @generated
	 */
	Date getOpStartTimer();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpStartTimer
	 * <em>Op Start Timer</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Op Start Timer</em>' attribute.
	 * @see #getOpStartTimer()
	 * @generated
	 */
	void setOpStartTimer(Date value);

	/**
	 * Returns the value of the '<em><b>Op End Timer</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Op End Timer</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Op End Timer</em>' attribute.
	 * @see #setOpEndTimer(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPendingOperationInternal_OpEndTimer()
	 * @model unique="false"
	 * @generated
	 */
	Date getOpEndTimer();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpEndTimer
	 * <em>Op End Timer</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Op End Timer</em>' attribute.
	 * @see #getOpEndTimer()
	 * @generated
	 */
	void setOpEndTimer(Date value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model
	 * @generated
	 */
	void happen();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model unique="false" annotation=
	 *        "http://www.eclipse.org/emf/2002/GenModel body='<%java.util.Date%> now = <%com.lvl6.mobsters.utility.common.TimeUtils%>.createNow();\nboolean retVal = false;\n<%java.util.Date%> _opEndTimer = this.getOpEndTimer();\nboolean _lessThan = (_opEndTimer.compareTo(now) < 0);\nif (_lessThan)\n{\n\tthis.happen();\n\tretVal = true;\n}\nreturn retVal;'"
	 * @generated
	 */
	@Override
	boolean checkTimer();

} // PendingOperationInternal
