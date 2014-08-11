/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.lvl6.mobsters.semanticmodel.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableInterfaceDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeReference
import org.eclipse.xtend.lib.macro.declaration.Visibility
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration

/**
 * Extracts an interface for all locally declared public methods with a
 * matching tag annotation.
 */
@Target(ElementType.TYPE)
@Active(ExtractInterfaceProcessor)
annotation ExtractInterfaces {
	String api
	String internal
}

@Target(ElementType.TYPE)
annotation ExposeThroughTo {
	boolean api = false
	boolean internal = true
	String fromField
	//Class<?> markerAnnotation
}

@Target(ElementType.METHOD)
annotation SplitReturn {
	String api
	String internal
}

@Target(ElementType.METHOD)
annotation ExposeTo {
	boolean api
	boolean internal
}

class ExtractInterfaceProcessor extends AbstractClassProcessor {
	
	override doRegisterGlobals(ClassDeclaration annotatedClass, RegisterGlobalsContext context) {
		val AnnotationReference annotRef = annotatedClass.annotations.findFirst[
			return it.annotationTypeDeclaration.simpleName == "ExtractInterfaces"
		]
		
		System.out.println(annotRef);
			
		context.registerInterface(
			annotRef.getStringValue("api")
		)
			
		context.registerInterface(
			annotRef.getStringValue("internal")
		)
	}
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		val AnnotationReference annotRef = annotatedClass.annotations.findFirst[
			return it.annotationTypeDeclaration.simpleName == "ExtractInterfaces"
		]
		val String apiIfName =
			annotRef.getStringValue("api")
		val String internalIfName = 
			annotRef.getStringValue("internal")
		
		val apiInterface = findInterface(apiIfName)
		val internalInterface = findInterface(internalIfName)
		
		// add the interface to the list of implemented interfaces
		annotatedClass.implementedInterfaces =
			#[ apiInterface.newTypeReference(), internalInterface.newTypeReference() ]
		
		// add the public methods to the interface
		for (method : annotatedClass.declaredMethods) {
			if (
				(method.visibility == Visibility.PUBLIC) && 
				(method.abstract == false)
			) {
				val splitReturn = 
					method.annotations.findFirst[	
						it.annotationTypeDeclaration.simpleName == "SplitReturn"
					]
				val exposeTo =
					method.annotations.findFirst[	
						it.annotationTypeDeclaration.simpleName == "ExposeTo"
					]
					
				if (splitReturn != null) {
					addInterfaceMethod(
						splitReturn.getClassValue("api"), apiInterface, method)
					addInterfaceMethod(
						splitReturn.getClassValue("internal"), internalInterface, method)
				} else if( exposeTo != null) {
					if (exposeTo.getBooleanValue("api")) {
						addInterfaceMethod(
							method.returnType, apiInterface, method)
					}
					if (exposeTo.getBooleanValue("internal")) {
						addInterfaceMethod(
							method.returnType, internalInterface, method)
					}
				} else {
					addInterfaceMethod(
						method.returnType, apiInterface, method)
					addInterfaceMethod(
						method.returnType, internalInterface, method)
				}
			}
		}		
		
		annotatedClass.declaredFields
			.filter[it.annotations.findAnnotationByName("ExposeThroughTo") != null]
			.forEach[
			// Locate the field to de-reference from
			val FieldDeclaration fromField = annotatedClass.
				findDeclaredField(expoPThru.getStringValue("fromField"))

			// Use a marker annotation to identify which fields from the fromField to
			// delegate through the interface being generated.		
			val TypeReference markerAnnotation = expoPThru.getClassValue(
				"markerAnnotation")
			context.findClass(fromField.type.toString()).declaredMethods.forEach [dm|
				if (
					(dm.annotations.findFirst [
						it.annotationTypeDeclaration == markerAnnotation
					] != null) &&
					(dm.visibility == Visibility::PUBLIC) &&
					(dm.abstract == false))
				{
					if (expoPThru.getBooleanValue("api")) {
						addInterfaceMethod(
							dm.returnType, apiInterface, dm)
					}
					if (expoPThru.getBooleanValue("internal")) {
						addInterfaceMethod(
							dm.returnType, internalInterface, dm)
					}	
					annotatedClass.addMethod(dm.simpleName) [
						for (param : dm.parameters) {
							addParameter(param.simpleName, param.type)
						}
						it.returnType = dm.returnType
						it.visibility = dm.visibility
						it.abstract = dm.abstract
						it.exceptions = dm.exceptions
						it.body = '''
				return this.«fromField.simpleName».«dm.simpleName»(
					«dm.parameters.map[it.simpleName].join(", ")»);
						'''
					]
				}
			]
		}
	}
	
	def AnnotationReference findAnnotationByName(
		Iterable<? extends AnnotationReference> iterSrc, String annotName) 
	{
		return iterSrc.findFirst[return it.annotationTypeDeclaration.simpleName == annotName]
	}
				
	def void addInterfaceMethod(
		TypeReference returnType, 
		MutableInterfaceDeclaration interfaceDest, 
		MutableMethodDeclaration sourceMethod
	) {
		interfaceDest.addMethod(sourceMethod.simpleName) [
			it.returnType = returnType
			for (p : sourceMethod.parameters) {
				it.addParameter(p.simpleName, p.type)
			}
			it.exceptions = sourceMethod.exceptions
		]
	}
}
