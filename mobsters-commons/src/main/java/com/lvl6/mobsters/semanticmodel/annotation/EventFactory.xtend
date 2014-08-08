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
annotation EventFactory {
    ListenerKind targetListeners = ListenerKind.UNSET	
}

enum ListenerKind {
	UNSET,
	CLIENT,
	SERVER,
	BOTH
}

class EventFactoryProcessor extends AbstractMethodProcessor {

	override doRegisterGlobals(MethodDeclaration annotatedMethod, RegisterGlobalsContext context) {
		val String eventName = annotatedMethod.eventName
		context.registerClass(eventName.eventClassName);
		context.registerInterface(eventName.eventInterfaceName);
		context.registerInterface(eventName.listenerInterfaceName);
	}
	
	def String getEventName(MethodDeclaration eventMethod) {
		return eventMethod.simpleName.replaceFirst("publish", "").toFirstUpper()
	}
	
	def String getEventClassName(String eventName) {
		return "com.lvl6.mobsters.semanticmodel.events.coarse.impl."+eventName+"Impl"
	}
	
	def String getEventInterfaceName(String eventName) {
		return "com.lvl6.mobsters.semanticmodel.events.coarse."+eventName
	}
	
	def String getListenerInterfaceName(String eventName) {
		return "com.lvl6.mobsters.semanticmodel.listener.coarse."+eventName+"Listener"
	}
			
	override doTransform(MutableMethodDeclaration annotatedMethod, extension TransformationContext context) {
		val String eventName = annotatedMethod.eventName
		val String eventClassName = eventName.eventClassName
		
		val MutableInterfaceDeclaration eventInterfaceType = 
			findInterface(eventName.eventInterfaceName)
		val MutableInterfaceDeclaration listenerInterfaceType = 
			findInterface(eventName.listenerInterfaceName)
		val MutableClassDeclaration classType = 
			findClass(eventClassName)

		// val Type baseListenerInterface = 
		// 	findTypeGlobally("com.lvl6.mobsters.semanticmodel.IEventListener")
		// val baseListernerType = findClass("com.lvl6.mobsters.semanticmodel.framework.AbstractEventListener")
				
		// add the interface to the list of implemented interfaces
		classType.implementedInterfaces = #[eventInterfaceType.newTypeReference]
		classType.extendedClass = 
			context
				.findTypeGlobally("com.lvl6.mobsters.domainmodel.gameclient.event.ClientGameEvent")
				.newTypeReference()
		
		// add the public methods to the interface
		for (param : annotatedMethod.parameters) {
			eventInterfaceType.addMethod("get" + param.simpleName.toFirstUpper()) [
				returnType = param.type
			]
		}
		for (param : annotatedMethod.parameters) {
			classType.addField(param.simpleName) [
				visibility = Visibility.PRIVATE
				type = param.type
				final = true
			]
		}
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
		
		// TODO: Distinguish between client and server events
		annotatedMethod.body = '''
			«eventClassName» newEvent = new «eventClassName»(
				«annotatedMethod.parameters.map[it.simpleName].join(', ')»
			);
			publish(newEvent);
			return;
		'''
		
		// listenerInterfaceType.extendedInterfaces = #[baseListenerInterface.newTypeReference]
		listenerInterfaceType.addMethod("on" + eventName) [
			visibility = Visibility.PUBLIC
			addParameter("event", eventInterfaceType.newTypeReference)
		]
	}
}