/**
 */
package com.lvl6.mobsters.domainmodel.metadata;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.lvl6.mobsters.domainmodel.metadata.MobstersMetaPackage
 * @generated
 */
public interface MobstersMetaFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MobstersMetaFactory eINSTANCE = com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaFactoryImpl
			.init();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MobstersMetaPackage getMobstersMetaPackage();

} //MobstersMetaFactory
