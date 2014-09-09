/**
 */
package com.lvl6.mobsters.domainmodel.metadata.impl;

import com.lvl6.mobsters.domainmodel.metadata.MobstersMetaFactory;
import com.lvl6.mobsters.domainmodel.metadata.MobstersMetaPackage;

import java.io.IOException;

import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MobstersMetaPackageImpl extends EPackageImpl implements
		MobstersMetaPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected String packageFilename = "metadata.ecore";

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iMonsterEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iItemEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iTaskEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iTaskStageEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iTaskStageMonsterEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType questMetaEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType questJobMetaEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.lvl6.mobsters.domainmodel.metadata.MobstersMetaPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MobstersMetaPackageImpl() {
		super(eNS_URI, MobstersMetaFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MobstersMetaPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @generated
	 */
	public static MobstersMetaPackage init() {
		if (isInited)
			return (MobstersMetaPackage) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI);

		// Obtain or create and register package
		MobstersMetaPackageImpl theMobstersMetaPackage = (MobstersMetaPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof MobstersMetaPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI) : new MobstersMetaPackageImpl());

		isInited = true;

		// Load packages
		theMobstersMetaPackage.loadPackage();

		// Fix loaded packages
		theMobstersMetaPackage.fixPackageContents();

		// Mark meta-data to indicate it can't be changed
		theMobstersMetaPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MobstersMetaPackage.eNS_URI,
				theMobstersMetaPackage);
		return theMobstersMetaPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIMonster() {
		if (iMonsterEDataType == null) {
			iMonsterEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI).getEClassifiers()
					.get(0);
		}
		return iMonsterEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIItem() {
		if (iItemEDataType == null) {
			iItemEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI).getEClassifiers()
					.get(1);
		}
		return iItemEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getITask() {
		if (iTaskEDataType == null) {
			iTaskEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI).getEClassifiers()
					.get(2);
		}
		return iTaskEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getITaskStage() {
		if (iTaskStageEDataType == null) {
			iTaskStageEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI).getEClassifiers()
					.get(3);
		}
		return iTaskStageEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getITaskStageMonster() {
		if (iTaskStageMonsterEDataType == null) {
			iTaskStageMonsterEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI).getEClassifiers()
					.get(4);
		}
		return iTaskStageMonsterEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getQuestMeta() {
		if (questMetaEDataType == null) {
			questMetaEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI).getEClassifiers()
					.get(5);
		}
		return questMetaEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getQuestJobMeta() {
		if (questJobMetaEDataType == null) {
			questJobMetaEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersMetaPackage.eNS_URI).getEClassifiers()
					.get(6);
		}
		return questJobMetaEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MobstersMetaFactory getMobstersMetaFactory() {
		return (MobstersMetaFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isLoaded = false;

	/**
	 * Laods the package and any sub-packages from their serialized form.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void loadPackage() {
		if (isLoaded)
			return;
		isLoaded = true;

		URL url = getClass().getResource(packageFilename);
		if (url == null) {
			throw new RuntimeException("Missing serialized package: "
					+ packageFilename);
		}
		URI uri = URI.createURI(url.toString());
		Resource resource = new EcoreResourceFactoryImpl().createResource(uri);
		try {
			resource.load(null);
		} catch (IOException exception) {
			throw new WrappedException(exception);
		}
		initializeFromLoadedEPackage(this, (EPackage) resource.getContents()
				.get(0));
		createResource(eNS_URI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isFixed = false;

	/**
	 * Fixes up the loaded package, to make it appear as if it had been programmatically built.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fixPackageContents() {
		if (isFixed)
			return;
		isFixed = true;
		fixEClassifiers();
	}

	/**
	 * Sets the instance class on the given classifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void fixInstanceClass(EClassifier eClassifier) {
		if (eClassifier.getInstanceClassName() == null) {
			eClassifier
					.setInstanceClassName("com.lvl6.mobsters.domainmodel.metadata."
							+ eClassifier.getName());
			setGeneratedClassName(eClassifier);
		}
	}

} //MobstersMetaPackageImpl
