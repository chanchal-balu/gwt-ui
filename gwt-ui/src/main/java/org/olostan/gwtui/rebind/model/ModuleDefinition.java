package org.olostan.gwtui.rebind.model;

import org.olostan.gwtui.rebind.ModuleGenerator;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

public class ModuleDefinition {
	private Class<?> moduleClass;
	private JClassType markerInterface;
	private ModuleGenerator moduleInstance = null;
	
	private boolean shouldImplement = false;

	public ModuleDefinition(Class<?> moduleClass, JClassType markerInterface) {
		super();
		this.moduleClass = moduleClass;
		this.markerInterface  = markerInterface;
	}	

	public Class<?> getModuleClass() {
		return moduleClass;
	}

	public JClassType getMarkerInterface() {
		return markerInterface;
	}

	public boolean isShouldImplement() {
		return shouldImplement;
	}

	public void setShouldImplement(boolean shouldImplement) {
		this.shouldImplement = shouldImplement;
	}	
	public ModuleGenerator getModuleInstance() throws UnableToCompleteException {
		if (moduleInstance==null) {
			try {
				moduleInstance =  (ModuleGenerator) moduleClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new UnableToCompleteException();				
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new UnableToCompleteException();
			}			
		}
		return moduleInstance;
	}
	
	
}
