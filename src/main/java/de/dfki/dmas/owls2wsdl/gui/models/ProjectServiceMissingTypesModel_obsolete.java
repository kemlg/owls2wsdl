/*
 * ProjectServiceMissingTypesModel.java
 *
 * Created on 20. M�rz 2007, 10:20
 */

package de.dfki.dmas.owls2wsdl.gui.models;

import javax.swing.AbstractListModel;

import de.dfki.dmas.owls2wsdl.gui.RuntimeModel;

/**
 * 
 * @author Oliver
 */
public class ProjectServiceMissingTypesModel_obsolete extends
		AbstractListModel<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7891181122102813228L;

	public String getElementAt(int index) {
		return RuntimeModel.getInstance().getProject().getServiceMissingTypes()
				.get(index);
	}

	public int getSize() {
		return RuntimeModel.getInstance().getProject().getServiceMissingTypes()
				.size();
	}
}
