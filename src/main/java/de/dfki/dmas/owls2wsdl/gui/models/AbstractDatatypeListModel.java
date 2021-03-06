/*
 * AbstractDatatypeListModel.java
 * Created on 27. September 2006, 01:53
 *
 * Copyright (C) 2007
 * German Research Center for Artificial Intelligence (DFKI GmbH) Saarbruecken
 * Hochschule fuer Technik und Wirtschaft (HTW) des Saarlandes
 * Developed by Oliver Fourman, Ingo Zinnikus, Matthias Klusch
 *
 * The code is free for non-commercial use only.
 * You can redistribute it and/or modify it under the terms
 * of the Mozilla Public License version 1.1  as
 * published by the Mozilla Foundation at
 * http://www.mozilla.org/MPL/MPL-1.1.txt
 */

package de.dfki.dmas.owls2wsdl.gui.models;

import java.util.Collections;
import java.util.Vector;

import javax.swing.AbstractListModel;

import de.dfki.dmas.owls2wsdl.core.AbstractDatatype;
import de.dfki.dmas.owls2wsdl.core.AbstractDatatypeComparer;
import de.dfki.dmas.owls2wsdl.core.AbstractDatatypeKB;

/**
 * 
 * @author Oliver Fourman
 */
public class AbstractDatatypeListModel extends
		AbstractListModel<AbstractDatatype> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4739179210188712951L;
	private Vector<AbstractDatatype> registeredDatatypeList;

	/** Creates a new instance of AbstractDatatypeListModel */
	public AbstractDatatypeListModel() {
		this.registeredDatatypeList = new Vector<AbstractDatatype>();
	}

	public AbstractDatatype getElementAt(int i) {
		return registeredDatatypeList.get(i);
	}

	// public AbstractDatatype getAbstractDatatypeAt(int i) {
	// return ((AbstractDatatype)registeredDatatypeList.get(i));
	// }

	public int getIndexOfAbstractDatatype(AbstractDatatype atype) {
		return this.registeredDatatypeList.indexOf(atype);
	}

	public int getSize() {
		return this.registeredDatatypeList.size();
	}

	// public ListDataListener[] getListDataListeners() {
	// ListDataListener[] retValue;
	//
	// retValue = super.getListDataListeners();
	// return retValue;
	// }

	public void updateModel() {
		this.registeredDatatypeList.removeAllElements();
		this.registeredDatatypeList.addAll(AbstractDatatypeKB.getInstance()
				.getAbstractDatatypeKBData().getRegisteredDatatypes().values());
		System.out.println("[AbstractDatatypeListModel] size "
				+ this.registeredDatatypeList.size());
		Collections.sort(this.registeredDatatypeList,
				new AbstractDatatypeComparer());
	}

	// public void updateModel(String path) {
	// RuntimeModel.getInstance().project.importDatatypes(path);
	// this.registeredDatatypeList = new Vector(
	// AbstractDatatypeKB.getInstance().getAbstractDatatypeKBData().getRegisteredDatatypes().values()
	// );
	// Collections.sort(this.registeredDatatypeList, new
	// AbstractDatatypeComparer() );
	// }
}
