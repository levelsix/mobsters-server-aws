package com.lvl6.mobsters.domain.initemf

import org.eclipse.emf.common.util.URI
// import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl

class MyCodeGenerator {

  def static void main(String[] args) {
    new MyCodeGenerator().generate("mymodel.xmi")
  }

  def generate(String file) {
    doEMFSetup
    val resourceSet = new ResourceSetImpl
    val resource = resourceSet.getResource(URI::createURI(file), true)
    for (content : resource.contents) {
      // generateCode(content)
    }
  }

//  def dispatch generateCode(Player it) '''
//    public class «name» {
//      «FOR task : completedTasks»
//          «task.generateCode()»
//      «ENDFOR»
//    }
//  '''
//
//  def dispatch generateCode(CompletedTask it) '''
//    private «taskId» «taskId»;
//  '''

  def doEMFSetup() {
    // EPackage.Registry::INSTANCE.put(MobsPlayerPackage::eINSTANCE.nsURI, MobsPlayerPackage::eINSTANCE)
    Resource.Factory.Registry::INSTANCE.extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl);
    Resource.Factory.Registry::INSTANCE.extensionToFactoryMap.put("mobster", new XMIResourceFactoryImpl);
  }
}