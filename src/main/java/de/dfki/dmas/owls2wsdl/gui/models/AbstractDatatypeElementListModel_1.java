/*
 * AbstractDatatypeElementListModel.java
 *
 * Created on 28. November 2006, 15:45
 */

package de.dfki.dmas.owls2wsdl.gui.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractListModel;

import de.dfki.dmas.owls2wsdl.core.AbstractDatatype;
import de.dfki.dmas.owls2wsdl.core.AbstractDatatypeElement;

/**
 * 
 * @author Oliver Fourman
 */
public class AbstractDatatypeElementListModel_1 extends
		AbstractListModel<AbstractDatatypeElement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2091270552669148215L;
	private Vector<AbstractDatatypeElement> registeredDatatypeElements;
	private Vector<AbstractDatatypeElement> registeredDatatypeMetaElements;
	private boolean RETURNMETADATA = false;

	/** Creates a new instance of AbstractDatatypeElementListModel */
	public AbstractDatatypeElementListModel_1() {
		this.registeredDatatypeElements = new Vector<AbstractDatatypeElement>();
		this.registeredDatatypeMetaElements = new Vector<AbstractDatatypeElement>();
	}

	public void getMetaData(boolean val) {
		this.RETURNMETADATA = val;
	}

	public AbstractDatatypeElement getElementAt(int i) {
		if (RETURNMETADATA) {
			return registeredDatatypeMetaElements.get(i);
		} else {
			return registeredDatatypeElements.get(i);
		}
	}

	public int getSize() {
		if (RETURNMETADATA) {
			return this.registeredDatatypeMetaElements.size();
		} else {
			return this.registeredDatatypeElements.size();
		}
	}

	public void updateModel(AbstractDatatype datatype) {
		this.registeredDatatypeElements.removeAllElements();
		this.registeredDatatypeMetaElements.removeAllElements();

		Iterator<AbstractDatatypeElement> it = datatype.getProperties()
				.iterator();
		while (it.hasNext()) {
			AbstractDatatypeElement elem = it.next();
			if (elem.getOwlSource().equals("META")) {
				this.registeredDatatypeMetaElements.add(elem);
			} else {
				this.registeredDatatypeElements.add(elem);
			}
		}

		Collections.sort(this.registeredDatatypeElements,
				new AbstractDatatypeElementComparer_1());
		Collections.sort(this.registeredDatatypeMetaElements,
				new AbstractDatatypeElementComparer_1());

		System.out.println("Count      Elements: "
				+ this.registeredDatatypeElements.size());
		System.out.println("Count Meta-Elements: "
				+ this.registeredDatatypeMetaElements.size());
		System.out.println("       CURRENT MODE: " + this.RETURNMETADATA);
		this.fireContentsChanged(this, 0,
				this.registeredDatatypeElements.size());
	}
}

class AbstractDatatypeElementComparer_1 implements
		Comparator<AbstractDatatypeElement> {
	public int compare(AbstractDatatypeElement obj1,
			AbstractDatatypeElement obj2) {
		String s1 = obj1.getName();
		String s2 = obj2.getName();
		return s1.compareTo(s2);
	}
}
