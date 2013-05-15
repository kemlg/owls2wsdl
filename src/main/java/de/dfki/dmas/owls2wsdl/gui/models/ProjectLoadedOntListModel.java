/*
 * ProjectLoadedOntListModel.java
 *
 * Created on 20. M�rz 2007, 09:57
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

import javax.swing.AbstractListModel;

import de.dfki.dmas.owls2wsdl.core.AbstractDatatypeKB;

//import java.util.Vector;

/**
 * 
 * @author Oliver
 */
public class ProjectLoadedOntListModel extends AbstractListModel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3056053586434968886L;

	public String getElementAt(int index) {
		return AbstractDatatypeKB.getInstance().getAbstractDatatypeKBData()
				.getOntologyURIs().get(index);
	}

	public int getSize() {
		return AbstractDatatypeKB.getInstance().getAbstractDatatypeKBData()
				.getOntologyURIs().size();
	}
}