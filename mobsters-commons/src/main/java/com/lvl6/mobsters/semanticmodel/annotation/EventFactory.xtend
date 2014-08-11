package com.lvl6.mobsters.semanticmodel.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.AbstractMethodProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableInterfaceDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.Visibility

@Target(ElementType.METHOD)
@Active(EventFactoryProcessor)
annotation EventFactory 
{ }

class EventFactoryProcessor extends AbstractMethodProcessor 
{
	override doRegisterGlobals(MethodDeclaration annotatedMethod, RegisterGlobalsContext context) {
		val String eventName = annotatedMethod.eventName
		context.registerInterface(eventName.eventInterfaceName);
		context.registerClass(eventName.eventInterfaceName + "Impl");
	}
	
	def String getEventName(MethodDeclaration eventMethod) {
		return eventMethod.simpleName.replaceFirst("publish", "").toFirstUpper()
	}
	
	def String getEventClassName(String eventName) {
		return "com.lvl6.mobsters.domain.game.events." + eventName + "Impl"
	}
	
	def String getEventInterfaceName(String eventName) {
		return "com.lvl6.mobsters.domain.game.events." + eventName
	}
			
	override doTransform(MutableMethodDeclaration annotatedMethod, extension TransformationContext context) {
		val String eventName = annotatedMethod.eventName
		val String eventClassName = eventName.eventClassName
		
		val MutableInterfaceDeclaration eventInterfaceType = 
			findInterface(eventName.eventInterfaceName)
		val MutableClassDeclaration classType = 
			findClass(eventClassName)

		// add the event interface to the event class's otherwise empty list of implemented interfaces
		classType.implementedInterfaces = #[eventInterfaceType.newTypeReference]
		classType.extendedClass = 
			context
				.findTypeGlobally("com.lvl6.mobsters.domain.game.event.AbstractGameEvent")
				.newTypeReference()
		
		// Add getter methods on the event interface for each of its fields.
		for (param : annotatedMethod.parameters) {
			eventInterfaceType.addMethod("get" + param.simpleName.toFirstUpper()) [
				returnType = param.type
			]
		}
		
		// Add field declarations on the event class for each of its fields.
		for (param : annotatedMethod.parameters) {
			classType.addField(param.simpleName) [
				visibility = Visibility.PRIVATE
				type = param.type
				final = true
			]
		}
		
		// Add a constructor method that takes all arguments in one go.
		classType.addConstructor[
			for (param : annotatedMethod.parameters) {
				addParameter(param.simpleName, param.type)
			}
			body = '''
				«FOR param : annotatedMethod.parameters»
				this.«param.simpleName» = «param.simpleName»;
				«ENDFOR»
				
			'''
			visibility = Visibility.PUBLIC
		]
		
		// Add getter methods on the Event class for each of its fields.
		for (param : annotatedMethod.parameters) {
			classType.addMethod("get" + param.simpleName.toFirstUpper()) 
			[		
				visibility = Visibility.PUBLIC
				returnType = param.type
				body = '''
					return this.«param.simpleName»;
				'''
			]
		}
		
		// Publish event body--construct an instance and call publish() from AbstractSemanticObject
		annotatedMethod.body = '''
			«eventClassName» newEvent = new «eventClassName»(
				«annotatedMethod.parameters.map[it.simpleName].join(', ')»
			);
			publish(newEvent);
			return;
		'''
	}
}