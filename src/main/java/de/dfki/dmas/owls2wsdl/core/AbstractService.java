/*
 * AbstractService.java
 *
 * Created on 28. August 2006, 14:11
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

package de.dfki.dmas.owls2wsdl.core;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * @author Oliver
 */
public class AbstractService implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4538347806184103324L;
	private String _filename = null;
	private String _version = null;
	private String _qname = null;
	private String _id = null;
	private String _name = null;
	private String _description = null;

	private HashMap<String, String> namespaceEntries;
	private Vector<String> importedOWLFiles;
	private Vector<AtomicProcess> processes;

	// BESSER:
	// http://www.castor.org/api/org/exolab/castor/util/OrderedHashMap.html
	// private Vector inputOrder;
	// private Vector outputOrder;

	/** Creates a new instance of ProcessContainer */
	public AbstractService() {
		this.namespaceEntries = new HashMap<String, String>();
		this.importedOWLFiles = new Vector<String>();
		this.processes = new Vector<AtomicProcess>();
	}

	/** Creates a new instance of ProcessContainer */
	public AbstractService(String filename, String qname, String name,
			String description) {
		this();
		/*
		 * check begin of strings for CR if("\b\t\n\f\r\"\\".indexOf
		 * (name.charAt(0)) >= 0) { name = name.substring(1); } // check end of
		 * strings for CR if("\b\t\n\f\r\"\\".indexOf
		 * (name.charAt(name.length()-1)) >= 0) { name =
		 * name.substring(0,name.length()-1); }
		 */

		this._filename = filename;
		this._qname = qname;
		this._name = name.trim();
		this._description = description.trim();
	}

	public String getFilename() {
		return this._filename;
	}

	public String getVersion() {
		return this._version;
	}

	public String getQname() {
		return this._qname;
	}

	public String getID() {
		return this._id;
	}

	public String getName() {
		return this._name;
	}

	public String getDescription() {
		return this._description;
	}

	public HashMap<String, String> getNamespaceEntries() {
		return this.namespaceEntries;
	}

	public Vector<String> getImportedOWLFiles() {
		return this.importedOWLFiles;
	}

	public void setFilename(String filename) {
		this._filename = filename;
	}

	public void setVersion(String version) {
		this._version = version;
	}

	public void setQname(String qname) {
		this._qname = qname;
	}

	public void setID(String id) {
		this._id = id;
	}

	public void setName(String name) {
		this._name = name.trim();
	}

	public void setDescription(String description) {
		this._description = description.trim();
	}

	public void addNamespaceEntry(String key, String val) {
		this.namespaceEntries.put(key, val);
	}

	public void addImportedOWLFile(String path) {
		this.importedOWLFiles.add(path);
	}

	public String getBase() {
		return this.namespaceEntries.get("xml:base").toString();
	}

	public String getBasePath() {
		String basepath = this.getBase();
		int index = basepath.lastIndexOf("/");
		return basepath.substring(0, index);
	}

	public void printInfo() {
		System.out.println("FILE    : " + this._filename);
		System.out.println("VERSION : " + this._version);
		System.out.println("QNAME   : " + this._qname);
		System.out.println("ID      : " + this._id);
		System.out.println("PROFILE : ServiceName: (" + this._name
				+ "), Description: (" + this._description + ")");

		// Iterator it = this.namespaceEntries.keySet().iterator();
		// while(it.hasNext()) {

		for (Iterator<String> it = this.namespaceEntries.keySet().iterator(); it
				.hasNext();) {
			String key = it.next();
			System.out.println("NS ENTRY: " + key + " = "
					+ this.namespaceEntries.get(key));
		}
		for (int i = 0; i < this.importedOWLFiles.size(); i++) {
			System.out.println("IMPORT  : " + this.importedOWLFiles.get(i));
		}
		Iterator<AtomicProcess>	itAp = this.getProcesses().iterator();
		while(itAp.hasNext()) {
			itAp.next().printInfo();
		}
	}

	Vector<AtomicProcess> getProcesses() {
		return processes;
	}

	public String toString() {
		// return
		// this._id+" Name: "+this._name+" ("+this._version+") "+this._filename;
		return this._name;
	}

	/**
	 * Checks all AbstractServiceParameter (input/output) for validation
	 * attribute attention: attributes are not persistent
	 * 
	 * @return status if service is translatable
	 */
	public boolean istranslatable() {
		boolean returnVal = true;
		Iterator<AtomicProcess> it = this.processes.iterator();
		while(it.hasNext()) {
			returnVal = returnVal && it.next().istranslatable();
		}

		return returnVal;
	}

	public Vector<String> getImportedOWLFiles(boolean filterOWLSImports) {
		Vector<String> filteredFileList = new Vector<String>();
		for (Iterator<String> it = this.importedOWLFiles.iterator(); it
				.hasNext();) {
			String path = it.next().toString();
			if (path.endsWith("Service.owl") || path.endsWith("Process.owl")
					|| path.endsWith("Profile.owl")
					|| path.endsWith("Grounding.owl")) {
				// Filter !
			} else {
				filteredFileList.add(path);
			}
		}
		return filteredFileList;
	}

	public String getLocalFilename() {
		String parsedSep = "/";
		if (this._filename.contains("\\")) {
			System.out.println("[i] filename DOS/WINDOWS formated");
			parsedSep = "\\";
		}
		int index = this._filename.lastIndexOf(parsedSep);
		return this._filename.substring(index + 1);
	}

	public String getReformatedServiceId4Translator() {
		String temp = this._id.toLowerCase();
		if (temp.endsWith("service")) {
			int pos = this._id.length() - 7;
			return this._id.substring(0, pos);
		}
		return this._id;
	}

	public String getReformatedServicename4Gen() {
		String temp = this._name.toLowerCase();
		temp = temp.replaceAll(" ", "");
		temp = temp.replaceAll("_", "");
		if (temp.endsWith("service")) {
			return temp.substring(0, temp.length() - 7);
		}
		return temp;
	}

	/**
	 * WSDL4J
	 */
	public boolean marshallToWSDL(OutputStream out, XsdSchemaGenerator xsdgen) {
		if (WSDLBuilder.getInstance().validateServiceParameterTypes(this)) {
			try {
				System.out.println("[marshallAbstractServiceAsWSDL] "
						+ this.toString());
				javax.wsdl.Definition def = WSDLBuilder.getInstance()
						.buildDefinition(this, xsdgen);
				WSDLBuilder.getInstance().printSchema(def, out);
			} catch (javax.wsdl.WSDLException e) {
				e.printStackTrace();
			} catch (java.lang.Exception e) {
				System.err.println("Error: " + e.toString());
			}
			return true;
		} else {
			System.err
					.println("[er] one or more parameter types not registered.");
			return false;
		}
	}

	public void addProcess(AtomicProcess ap) {
		this.processes.add(ap);
	}

	public String getParameterType(String localName) {
		Iterator<AtomicProcess> itAp = this.processes.iterator();
		while(itAp.hasNext()) {
			String retValue = itAp.next().getParameterType(localName);
			if(retValue != null) {
				return retValue;
			}
		}
		return null;
	}

	public Vector<AbstractServiceParameter> getAllOutputParameter() {
		Iterator<AtomicProcess> itAp = this.processes.iterator();
		Vector<AbstractServiceParameter> v = new Vector<AbstractServiceParameter>();
		while(itAp.hasNext()) {
			v.addAll(itAp.next().getOutputParameter());
		}
		return v;
	}

	public Vector<AbstractServiceParameter> getAllInputParameter() {
		Iterator<AtomicProcess> itAp = this.processes.iterator();
		Vector<AbstractServiceParameter> v = new Vector<AbstractServiceParameter>();
		while(itAp.hasNext()) {
			v.addAll(itAp.next().getInputParameter());
		}
		return v;
	}
}