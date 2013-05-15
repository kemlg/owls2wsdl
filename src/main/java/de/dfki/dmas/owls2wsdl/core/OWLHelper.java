/*
 * OWLHelper.java
 *
 * Created on 26. Oktober 2006, 10:06
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

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.CardinalityRestriction;
import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.MaxCardinalityRestriction;
import com.hp.hpl.jena.ontology.MinCardinalityRestriction;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * 
 * @author Oliver Fourman
 */
public class OWLHelper {

	/** Creates a new instance of OWLHelper */
	public OWLHelper() {
	}

	/**
	 * Depending on the used jena model (rdfs information necessary!) we can
	 * determine the range also from SuperProperties
	 */
	public String getPropertyRange(OntProperty prop) {
		String range = "http://www.w3.org/2002/07/owl#Thing";
		if (prop.getRange() != null) {
			range = prop.getRange().getURI(); // prop.getRange().getNameSpace()+prop.getRange().getLocalName();
		} else {
			for (ExtendedIterator<? extends OntProperty> superPropIt = prop
					.listSuperProperties(true); superPropIt.hasNext();) {
				ObjectProperty super_prop = superPropIt.next()
						.asObjectProperty();
				if (super_prop.getRange() != null) {
					range = super_prop.getRange().getURI();
				}
			}
		}
		return range;
	}

	public boolean checkForPrimitiveSchemaType(String uri) {
		if (uri.contains("http://www.w3.org/2001/XMLSchema")) {
			return true;
		}
		return false;
	}

	public void printRestriction(Restriction curr) {
		OntProperty ontproperty = null;
		try {
			ontproperty = curr.getOnProperty();

			String owlsource = "";
			if (ontproperty.isDatatypeProperty()) {
				owlsource = "ON_DATATYPE";
				System.out.println("");
				System.out.println("     [Res] Domain: "
						+ ontproperty.getDomain().getURI());
				System.out.println("     [Res] Range: "
						+ ontproperty.getRange().getURI());
			} else if (ontproperty.isObjectProperty()) {
				owlsource = "ON_OBJECT";
			} else {
				owlsource = "ON_?";
			}

			System.out.println("     [Res] OntProperty: "
					+ ontproperty.getLocalName() + " (" + owlsource + ")");
		} catch (Exception e) {
			System.out.println("     [Res] OntProperty Type missing");
		}

		// at this point just informational
		if (curr.isHasValueRestriction()) {
			HasValueRestriction hvr = curr.asHasValueRestriction();
			if (hvr.getHasValue().isLiteral()) {
				System.out.println("     [Res] HasValue. isLiteral "
						+ hvr.getHasValue().asNode().getLiteralValue() + " ("
						+ hvr.getHasValue().asNode().getLiteralDatatypeURI()
						+ ")");
			} else {
				System.out.println("     [Res] HasValue: "
						+ hvr.getHasValue().toString());
			}
		} else if (curr.isAllValuesFromRestriction()) {
			AllValuesFromRestriction avfr = curr.asAllValuesFromRestriction();
			System.out.println("     [Res] AllValuesFrom: "
					+ avfr.getAllValuesFrom().getURI()); // asNode().toString());

			try {
				OntClass avfrvalue = (OntClass) avfr.getAllValuesFrom().as(
						OntClass.class);
				if (avfrvalue.isEnumeratedClass()) {
					// �hnlich: EnumeratedClass enumAvfrValue =
					// avfrvalue.convertToEnumeratedClass(....)
					for (ExtendedIterator<? extends OntResource> oneOfIt = avfrvalue
							.asEnumeratedClass().listOneOf(); oneOfIt.hasNext();) {
						// Individual oneOf = (Individual) oneOfIt.next();
						System.out.println("          [I,oneOf] "
								+ oneOfIt.next().toString());
					}
				}
			} catch (Exception e) {
				if (this.checkForPrimitiveSchemaType(avfr.getAllValuesFrom()
						.getURI())) {
					System.out
							.println("     [Res] [i] DATATYPE (primitive XSD type)");
				}
			}
		} else if (curr.isSomeValuesFromRestriction()) {
			SomeValuesFromRestriction svfr = curr.asSomeValuesFromRestriction();
			System.out.println("     [Res] SomeValuesFrom: "
					+ svfr.getSomeValuesFrom().asNode().toString());
			OntClass svfrvalue = (OntClass) svfr.getSomeValuesFrom().as(
					OntClass.class);
			if (svfrvalue.isEnumeratedClass()) {
				for (ExtendedIterator<? extends OntResource> oneOfIt = svfrvalue
						.asEnumeratedClass().listOneOf(); oneOfIt.hasNext();) {
					System.out.println("          [I,oneOf]"
							+ oneOfIt.next().toString());
				}
			}
		} else if (curr.isCardinalityRestriction()) {
			CardinalityRestriction cr = curr.asCardinalityRestriction();
			System.out
					.println("     [Res] Cardinality: " + cr.getCardinality());
			// this.setCardinality(curr.getOnProperty().getLocalName(),
			// curr.asCardinalityRestriction().getCardinality());
		}
		// else if( curr.hasProperty(OWL.minCardinality) ) {
		else if (curr.isMinCardinalityRestriction()) {
			MinCardinalityRestriction mincr = curr
					.asMinCardinalityRestriction();
			System.out.println("     [Res] MinCardinality: "
					+ mincr.getMinCardinality());
			// this.setMinCardinality(curr.getOnProperty().getLocalName(),
			// ((Literal)curr.getPropertyValue(OWL.minCardinality)).getValue().toString());
		}
		// else if( curr.hasProperty(OWL.maxCardinality) ) {
		else if (curr.isMaxCardinalityRestriction()) {
			MaxCardinalityRestriction maxcr = curr
					.asMaxCardinalityRestriction();
			System.out.println("     [Res] MaxCardinality: "
					+ maxcr.getMaxCardinality());
			// this.setMaxCardinality(curr.getOnProperty().getLocalName(),
			// ((Literal)curr.getPropertyValue(OWL.maxCardinality)).getValue().toString());
		} else {
			System.out.println("Restriction unkown: " + curr.toString());
		}
	}
}
