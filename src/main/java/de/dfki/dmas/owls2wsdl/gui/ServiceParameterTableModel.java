/*
 * ServiceParameterTableModel.java
 *
 * Created on 11. Dezember 2006, 17:32
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

package de.dfki.dmas.owls2wsdl.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import de.dfki.dmas.owls2wsdl.core.AbstractServiceParameter;
// to check KB for parameter
//import java.util.Observable;
//import java.util.Observer;

/**
 * 
 * @author Oliver Fourman
 */
public class ServiceParameterTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499915610223030825L;
	private Vector<AbstractServiceParameter> parameterList;
	private int inputCount;

	/** Creates a new instance of ServiceParameterTableModel */
	public ServiceParameterTableModel() {
		super();
		this.parameterList = new Vector<AbstractServiceParameter>();
		// RuntimeModel.getInstance().addObserver(this);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (rowIndex < inputCount) {
				return "INPUT";
			} else {
				return "OUTPUT";
			}
		case 1:
			return new Integer(
					((AbstractServiceParameter) this.parameterList
							.get(rowIndex)).getPos());
		case 2:
			return ((AbstractServiceParameter) this.parameterList.get(rowIndex))
					.getID();
		case 3:
			return ((AbstractServiceParameter) this.parameterList.get(rowIndex))
					.getUri();
		case 4:
			if (((AbstractServiceParameter) this.parameterList.get(rowIndex))
					.isInKB()) {
				return "yes";
			} else {
				return "no";
			}
		case 5: // Check
			if (((AbstractServiceParameter) this.parameterList.get(rowIndex))
					.isValidNCName()) {
				return "yes";
			} else {
				return "no";
			}
		default:
			break;
		}
		return "blub";
	}

	public int getRowCount() {
		return parameterList.size();
	}

	public int getColumnCount() {
		return 5;
	}

	public AbstractServiceParameter getAbstractServiceParameter(int row) {
		return (AbstractServiceParameter) this.parameterList.get(row);
	}

	public void removeAllParameter() {
		this.parameterList.removeAllElements();
		this.inputCount = 0;
	}

	public void reinitParameter(
			Vector<AbstractServiceParameter> inputParameter,
			Vector<AbstractServiceParameter> outputParameter) {
		this.removeAllParameter();
		this.parameterList.addAll(inputParameter);
		this.inputCount = this.parameterList.size();
		this.parameterList.addAll(outputParameter);
	}
}
