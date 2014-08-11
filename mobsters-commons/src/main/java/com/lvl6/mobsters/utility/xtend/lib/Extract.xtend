/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.lvl6.mobsters.utility.xtend.lib

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.Visibility

/**
 * Extracts an interface for all locally declared public methods.
 */
@Target(ElementType.TYPE)
@Active(ExtractProcessor)
annotation Extract {
	val Class<?> baseInterface
}

class ExtractProcessor extends AbstractClassProcessor {
	
	override doRegisterGlobals(ClassDeclaration annotatedClass, RegisterGlobalsContext context) {
		context.registerInterface(annotatedClass.interfaceName)
	}

	def String getInterfaceName(ClassDeclaration annotatedClass) {
		return 
			annotatedClass.qualifiedName
				.replace(annotatedClass.simpleName, "I"+annotatedClass.simpleName)
				.replace("Impl", "")
	}
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		val interfaceType = findInterface(annotatedClass.interfaceName)
		val baseInterfaceTypeRef = 
			annotatedClass.findAnnotation(
				context.findTypeGlobally("com.lvl6.mobsters.utility.xtend.lib.Extract")
			).getClassValue("baseInterface")
			
		// add the interface to the list of implemented interfaces
		if (baseInterfaceTypeRef != null) {
			interfaceType.extendedInterfaces = #[baseInterfaceTypeRef]
		}
		annotatedClass.implementedInterfaces = #[interfaceType.newTypeReference]
		
		// add the public methods to the interface
		for (method : annotatedClass.declaredMethods) {
			if (method.visibility == Visibility.PUBLIC) {
				interfaceType.addMethod(method.simpleName) [
					docComment = method.docComment
					returnType = method.returnType
					for (p : method.parameters) {
						addParameter(p.simpleName, p.type)
					}
					exceptions = method.exceptions
				]
			}
		}
	}
}