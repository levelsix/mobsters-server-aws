package com.lvl6.mobsters.semanticmodel.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import java.util.List
import org.eclipse.xtend.lib.macro.AbstractFieldProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration
import org.eclipse.xtend.lib.macro.declaration.Visibility

@Target(ElementType.FIELD)
@Active(SemanticPassthroughProcessor)
annotation SemanticPassthrough {
	String idProperty = "id"
	String[] clientProperties = #[]
	String[] serverProperties = #[]
	String[] bothProperties = #[]
	Class<?> clientInterface
	Class<?> serverInterface
}

class SemanticPassthroughProcessor extends AbstractFieldProcessor {
	public override doTransform(List<? extends MutableFieldDeclaration> annotatedFields, @Extension TransformationContext context) {
		/*val Iterable<AnnotationReference> iterOne = annotatedFields.map[
			it.annotations.findFirst[
				return it.annotationTypeDeclaration.simpleName == "SemanticPassthrough"
			]
		]
		iterOne.map[it.getClassValue("clientInterface")].toSet.forEach[
			context.findInterface(
				it.type.qualifiedName
			).addMethod("getDuid") [
				it.returnType = context.findClass("java.lang.String").newTypeReference()
				it.visibility = Visibility::PUBLIC
			]
		]
		iterOne.map[it.getClassValue("serverInterface")].toSet.forEach[
			context.findTypeGlobally(it.type.qualifiedName).addMethod("getDuid") [
				it.returnType = context.findClass("java.lang.String").newTypeReference()
				it.visibility = Visibility::PUBLIC
			]
		]*/
		annotatedFields.forEach[field|
			field.declaringType.addMethod("getUuid") [
				it.returnType = context.findTypeGlobally(String).newTypeReference()
				it.visibility = Visibility::PUBLIC
				it.body = '''return this.«field.simpleName».get«field.annotations.findFirst[
					return it.annotationTypeDeclaration.simpleName == "SemanticPassthrough"
				].getStringValue("idProperty").toFirstUpper()»();
				'''
			]
		]
	}
}