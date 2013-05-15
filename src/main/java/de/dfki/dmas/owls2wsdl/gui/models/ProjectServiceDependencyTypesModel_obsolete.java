/*
 * ProjectServiceDependencyTypesModel.java
 *
 */

package de.dfki.dmas.owls2wsdl.gui.models;

import javax.swing.AbstractListModel;

import de.dfki.dmas.owls2wsdl.gui.RuntimeModel;

/**
 * 
 * @author cosmic
 */
public class ProjectServiceDependencyTypesModel_obsolete extends
		AbstractListModel<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3713747651437289121L;

	public String getElementAt(int index) {
		return RuntimeModel.getInstance().getProject()
				.getServiceDependencyTypes().get(index);
	}

	public int getSize() {
		return RuntimeModel.getInstance().getProject()
				.getServiceDependencyTypes().size();
	}
}
