package com.lvl6.mobsters.utility.xtend.lib

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableInterfaceDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.Visibility

@Target(ElementType.TYPE)
@Active(EventFactoryProcessor)
annotation EventFactory 
{ }

class EventFactoryProcessor extends AbstractClassProcessor 
{
	override doRegisterGlobals(ClassDeclaration annotatedClass, extension RegisterGlobalsContext context) {
		val String interfaceName = annotatedClass.factoryInterfaceName
		registerInterface(interfaceName)
		
		annotatedClass.eventMethods.forEach[m |
			val String eventClassName = m.eventClassName
			val String eventInterfaceName = m.eventInterfaceName
			
			registerClass(eventClassName)
			registerInterface(eventInterfaceName)
		]
	}
	
	def String getFactoryInterfaceName(ClassDeclaration factoryClass) {
		return 
			factoryClass.qualifiedName
				.replace(factoryClass.simpleName, "I" + factoryClass.simpleName)
				.replace("Impl", "")
	}
	
	def String getEventSimpleName(MethodDeclaration eventMethod) {
		return eventMethod.simpleName
			.replaceFirst("publish", "")
			.replace("Impl", "")
			.toFirstUpper
	}
		
	def String getEventClassName(MethodDeclaration eventMethod) {		
		return "com.lvl6.mobsters.domain.game.event." + eventMethod.eventSimpleName
	}
	
	def String getEventInterfaceName(MethodDeclaration eventMethod) {
		return "com.lvl6.mobsters.domain.game.event.I" + eventMethod.eventSimpleName
	}
	
	def Iterable<MethodDeclaration> getEventMethods(ClassDeclaration annotatedClass)
	{
		return
			annotatedClass.declaredMethods.filter(MethodDeclaration).filter[
				MethodDeclaration m | return m.simpleName.startsWith("publish")
			]
	}
	
	def Iterable<MutableMethodDeclaration> getMutableEventMethods(MutableClassDeclaration annotatedClass)
	{
		return
			annotatedClass.declaredMethods.filter(MutableMethodDeclaration).filter[
				MutableMethodDeclaration m | return m.simpleName.startsWith("publish")	
			]
	}
			
	override doTransform(
		MutableClassDeclaration annotatedClass, extension TransformationContext context
	) {
		val MutableInterfaceDeclaration factoryInterfaceType = 
			findInterface(annotatedClass.factoryInterfaceName)
		annotatedClass.mutableEventMethods.forEach[m |
			val eventClassName = m.eventClassName
			val eventInterfaceName = m.eventInterfaceName
			
			factoryInterfaceType.addMethod(m.simpleName) [newMethod |
				m.parameters.forEach[p |
					newMethod.addParameter(
						p.simpleName, p.type
					)
				]
				newMethod.returnType = m.returnType
				newMethod.visibility = Visibility.PUBLIC
			]
			
			m.visibility = Visibility.PUBLIC				
			m.body = '''
				«eventInterfaceName» newEvent = new «eventClassName»(
					«m.parameters.map[it.simpleName].join(', ')»
				);
				
				_eventPublisher.publish(newEvent);
				return;
			'''
			
			m.generateEventClassAndInterface(context)
		]		
	}
			
	def void generateEventClassAndInterface(
		MutableMethodDeclaration annotatedMethod, extension TransformationContext context) 
	{
		val String eventClassName = 
			annotatedMethod.eventClassName
		val MutableInterfaceDeclaration eventInterfaceType = 
			findInterface(annotatedMethod.eventInterfaceName)
		val MutableClassDeclaration eventClassType = 
			findClass(eventClassName)

		// add the event interface to the event class's otherwise empty list of implemented interfaces
		eventClassType.implementedInterfaces =
			#[eventInterfaceType.newTypeReference]
		eventInterfaceType.extendedInterfaces = 
			#[context.findTypeGlobally("com.lvl6.mobsters.domain.game.event.IGameEvent")
				.newTypeReference()]
		
		eventInterfaceType.visibility = Visibility.PUBLIC
		eventClassType.visibility = Visibility.DEFAULT
		eventClassType.final = true
		
		// Add getter methods on the event interface for each of its fields.
		for (param : annotatedMethod.parameters) {
			eventInterfaceType.addMethod("get" + param.simpleName.toFirstUpper()) [
				it.returnType = param.type
				it.visibility = Visibility.PUBLIC
			]
		}
		
		// Add field declarations on the event class for each of its fields.
		for (param : annotatedMethod.parameters) {
			eventClassType.addField(param.simpleName) [
				visibility = Visibility.PRIVATE
				type = param.type
				final = true
			]
		}
		
		// Add a constructor method that takes all arguments in one go.
		eventClassType.addConstructor[
			for (param : annotatedMethod.parameters) {
				addParameter(param.simpleName, param.type)
			}
			visibility = Visibility.DEFAULT
			body = '''
				«FOR param : annotatedMethod.parameters»
				this.«param.simpleName» = «param.simpleName»;
				«ENDFOR»
				
			'''
		]
		
		// Add getter methods on the Event class for each of its fields.
		for (param : annotatedMethod.parameters) {
			eventClassType.addMethod("get" + param.simpleName.toFirstUpper()) 
			[		
				visibility = Visibility.PUBLIC
				returnType = param.type
				body = '''
					return this.«param.simpleName»;
				'''
			]
		}
	}
	
//	def ConstructorDeclaration findConstructor(MutableFieldDeclaration fld) 
//	{
//		return fld.type.type.compilationUnit.sourceTypeDeclarations.map[
//			it.declaredConstructors.filter[
//				it.declaringType.qualifiedName == fld.type.type.qualifiedName
//			]
//		].flatten.reduce[best, next |
//			var retVal = best
//			if (next.parameters.size > best.parameters.size) {
//				retVal = next
//			}
//			return retVal
//		]
//	}
//	
//	def TypeReference findInterface(MutableFieldDeclaration fld) {
//		return
//			fld.type.type.compilationUnit.sourceTypeDeclarations
//			.map[src | src.declaredClasses
//				.filter[cls | cls.qualifiedName == fld.type.type.qualifiedName]
//				.map[cls | cls.implementedInterfaces]
//				.get(0)]
//			.flatten
//			.get(0)
//	}
}