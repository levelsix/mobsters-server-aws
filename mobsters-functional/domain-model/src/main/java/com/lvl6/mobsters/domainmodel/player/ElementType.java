/**
 */
package com.lvl6.mobsters.domainmodel.player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Element Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc -->
 * 
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getElementType()
 * @model
 * @generated
 */
public enum ElementType implements Enumerator {
	/**
	 * The '<em><b>NO ELEMENT</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #NO_ELEMENT_VALUE
	 * @generated
	 * @ordered
	 */
	NO_ELEMENT(0, "NO_ELEMENT", "NO_ELEMENT"),

	/**
	 * The '<em><b>FIRE</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #FIRE_VALUE
	 * @generated
	 * @ordered
	 */
	FIRE(0, "FIRE", "FIRE"),

	/**
	 * The '<em><b>EARTH</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #EARTH_VALUE
	 * @generated
	 * @ordered
	 */
	EARTH(0, "EARTH", "EARTH"),

	/**
	 * The '<em><b>WATER</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #WATER_VALUE
	 * @generated
	 * @ordered
	 */
	WATER(0, "WATER", "WATER"),

	/**
	 * The '<em><b>ROCK</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #ROCK_VALUE
	 * @generated
	 * @ordered
	 */
	ROCK(0, "ROCK", "ROCK"),

	/**
	 * The '<em><b>LIGHT</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #LIGHT_VALUE
	 * @generated
	 * @ordered
	 */
	LIGHT(0, "LIGHT", "LIGHT"),

	/**
	 * The '<em><b>DARK</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #DARK_VALUE
	 * @generated
	 * @ordered
	 */
	DARK(0, "DARK", "DARK");

	/**
	 * The '<em><b>NO ELEMENT</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NO ELEMENT</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NO_ELEMENT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NO_ELEMENT_VALUE = 0;

	/**
	 * The '<em><b>FIRE</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FIRE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #FIRE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FIRE_VALUE = 0;

	/**
	 * The '<em><b>EARTH</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EARTH</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #EARTH
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EARTH_VALUE = 0;

	/**
	 * The '<em><b>WATER</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WATER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #WATER
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int WATER_VALUE = 0;

	/**
	 * The '<em><b>ROCK</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ROCK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ROCK
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ROCK_VALUE = 0;

	/**
	 * The '<em><b>LIGHT</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LIGHT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #LIGHT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LIGHT_VALUE = 0;

	/**
	 * The '<em><b>DARK</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DARK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #DARK
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DARK_VALUE = 0;

	/**
	 * An array of all the '<em><b>Element Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ElementType[] VALUES_ARRAY = new ElementType[] {
			NO_ELEMENT, FIRE, EARTH, WATER, ROCK, LIGHT, DARK, };

	/**
	 * A public read-only list of all the '<em><b>Element Type</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<ElementType> VALUES = Collections
			.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Element Type</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ElementType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ElementType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Element Type</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ElementType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ElementType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Element Type</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ElementType get(int value) {
		switch (value) {
		case NO_ELEMENT_VALUE:
			return NO_ELEMENT;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private ElementType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getLiteral() {
		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string
	 * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}

} // ElementType
