package org.olostan.gwtui.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This class contains all necessary data needed to generate
 * code by modules 
 */
public class UIGeneratorContext {
	private TreeLogger logger;
	private GeneratorContext context;
	private UIConfiguration configuration;
	private SourceWriter writer;
	public UIGeneratorContext(TreeLogger logger, GeneratorContext context,
			UIConfiguration configuration, SourceWriter writer) {
		super();
		this.logger = logger;
		this.context = context;
		this.configuration = configuration;
		this.writer = writer;
	}
	public TreeLogger getLogger() {
		return logger;
	}
	public GeneratorContext getContext() {
		return context;
	}
	public UIConfiguration getConfiguration() {
		return configuration;
	}
	public SourceWriter getWriter() {
		return writer;
	}
	
	

}
