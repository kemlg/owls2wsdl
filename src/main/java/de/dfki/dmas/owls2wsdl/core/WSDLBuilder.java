/*
 * WSDLBuilder.java
 *
 * Created on 24. August 2006, 15:10
 *
 * http://forum.java.sun.com/thread.jspa?threadID=670586&messageID=3920409
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

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;

import org.jdom.output.DOMOutputter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import cgl.hpsearch.nb4ws.WSDL.XMLUtils;

import com.ibm.wsdl.extensions.schema.SchemaSerializer;

import de.dfki.dmas.owls2wsdl.config.OWLS2WSDLSettings;
//import cgl.hpsearch.nb4ws.WSDL.*;
//import cgl.hpsearch.common.objects.XObject;
//import cgl.hpsearch.nb4ws.WSDL.WSDLParser;
//import cgl.hpsearch.nb4ws.WSDL.XMLUtils;
//import cgl.hpsearch.nb4ws.WSDL.TypesParserModified;
//import org.exolab.castor.xml.schema.Schema;
//import org.exolab.castor.xml.schema.SimpleTypesFactory;

/**
 * 
 * @author Oliver Fourman
 */
public class WSDLBuilder {

	private WSDLBuilderHelper helper;

	// Singleton
	private static WSDLBuilder instance;

	/** Creates a new instance of WSDLBuilder */
	private WSDLBuilder() {
		helper = new WSDLBuilderHelper();
	}

	public static WSDLBuilder getInstance() {
		if (instance == null) {
			instance = new WSDLBuilder();
		}
		return instance;
	}

	public void readWSDL(String wsdlURI) throws WSDLException {
		// // Ex.: http://www.hpsearch.org/
		// //
		// http://www2.sys-con.com/ITSG/virtualcd/WebServices/archives/0310/behera/index.htm#s3
		// //
		// http://ws.apache.org/wsif/developers/how_to_wsdl_extensions.html#N1000C
		//
		// System.out.println("READING: "+wsdlURI);
		//
		// WSDLFactory wsdlFactory = WSDLFactory.newInstance();
		//
		// // Create the WSDL Reader object
		// WSDLReader reader = wsdlFactory.newWSDLReader();
		//
		//
		// // Read the WSDL and get the top-level Definition object
		// Definition def = reader.readWSDL(null, wsdlURI);
		//
		// org.w3c.dom.Element schemaElement = null;
		//
		// if (def.getTypes() != null) {
		// ExtensibilityElement schemaExtElem
		// = findExtensibilityElement(def.getTypes().getExtensibilityElements(),
		// "schema");
		//
		// if (schemaExtElem != null && schemaExtElem instanceof
		// UnknownExtensibilityElement) {
		// schemaElement = ((UnknownExtensibilityElement)
		// schemaExtElem).getElement();
		// }
		// }
		//
		// if (schemaElement == null) {
		// // No schema to read
		// System.err.println("Unable to find schema extensibility element in WSDL");
		// }
		//
		// // Convert from DOM to JDOM
		// DOMBuilder domBuilder = new DOMBuilder();
		// org.jdom.Element jdomSchemaElement = domBuilder.build(schemaElement);
		//
		// if (jdomSchemaElement == null) {
		// System.err.println("Unable to read schema defined in WSDL");
		// }
		//
		// // Add namespaces from the WSDL
		// Map namespaces = def.getNamespaces();
		//
		// if (namespaces != null && !namespaces.isEmpty()) {
		// Iterator nsIter = namespaces.keySet().iterator();
		//
		// while (nsIter.hasNext()) {
		// String nsPrefix = (String) nsIter.next();
		// String nsURI = (String) namespaces.get(nsPrefix);
		//
		// if (nsPrefix != null && nsPrefix.length() > 0) {
		// org.jdom.Namespace nsDecl = org.jdom.Namespace.getNamespace(nsPrefix,
		// nsURI);
		// jdomSchemaElement.addNamespaceDeclaration(nsDecl);
		// }
		// }
		// }
		//
		// // Make sure that the types element is not processed
		// jdomSchemaElement.detach();
		//
		// // Convert it into a Castor schema instance
		// org.exolab.castor.xml.schema.Schema wsdlTypes = null;
		//
		// try {
		// wsdlTypes = XMLUtils.convertElementToSchema(jdomSchemaElement);
		//
		//
		// }
		//
		// catch (Exception e) {
		// System.err.println(e.getMessage());
		// }
		//
		// TypesParser typesParser = new TypesParser();
		// if (wsdlTypes != null) typesParser.init(wsdlTypes);
		//
		// //Types types = def.createTypes();
		// //types.addExtensibilityElement();
		// //typesParser.types.get()
	}

	// private Schema mergeSchemaDefinitions(Vector schemaList) {
	//
	// }

	public void printSchema(Definition def, OutputStream out)
			throws WSDLException {
		WSDLWriter writer = WSDLFactory.newInstance().newWSDLWriter();
		writer.writeWSDL(def, out);
	}

	/**
	 * http://de.wikipedia.org/wiki/XML-Schema#Einfache_Typen
	 */
	public boolean isPrimitiveType(String uri) {
		String[] primitiveTypes = { "http://www.w3.org/2001/XMLSchema#string",
				"http://www.w3.org/2001/XMLSchema#decimal",
				"http://www.w3.org/2001/XMLSchema#integer",
				"http://www.w3.org/2001/XMLSchema#float",
				"http://www.w3.org/2001/XMLSchema#date",
				"http://www.w3.org/2001/XMLSchema#time" };
		for (int i = 0; i < primitiveTypes.length; i++) {
			if (uri.equals(primitiveTypes[i].toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Validate Parameter. Checks if KB contains datatype or if datatype is
	 * primitive.
	 */
	public boolean validateServiceParameterTypes(AbstractService abstractService) {
		boolean CHECK = true;
		Vector<AbstractServiceParameter> parameter = new Vector<AbstractServiceParameter>();
		parameter.addAll(abstractService.getInputParameter());
		parameter.addAll(abstractService.getOutputParameter());

		for (Iterator<AbstractServiceParameter> it = parameter.iterator(); it
				.hasNext();) {
			AbstractServiceParameter param = it.next();
			if (AbstractDatatypeKB.getInstance().data.containsKey(param
					.getUri())) {
				System.out.println("[BUILD] Paramter [ok] " + param.getUri());
			} else {
				if (this.isPrimitiveType(param.getUri())) {
					System.out.println("[BUILD] Paramter [ok] Primitive Type "
							+ param.getUri());
					CHECK = true;
				} else {
					System.out.println("[BUILD] Paramter [er] "
							+ param.getUri());
					CHECK = false;
				}
			}
		}
		return CHECK;
	}

	/**
	 * Build the WSDL model.
	 * 
	 * @param abstractService
	 *            Collected Service information
	 * @param useHierarchyPattern
	 *            used by XSD generator. Activate hierarchy pattern (xfront).
	 * @param depth
	 *            used by XSD generator. Sets the inheritence level.
	 * @param xsdInheritance
	 *            used by XSD generator. Sets strategy for searching a xsd
	 *            primitive type.
	 * @param xsdDefaultType
	 *            used by XSD generator when no xsd type is set.
	 * @return Definition WSDL definition
	 */
	public Definition buildDefinition(AbstractService abstractService,
			boolean useHierarchyPattern, int depth, String xsdInheritance,
			String xsdDefaultType) throws WSDLException, java.lang.Exception {
		XsdSchemaGenerator xsdgen = new XsdSchemaGenerator("WSDLTYPE",
				useHierarchyPattern, depth, xsdInheritance, xsdDefaultType);
		return this.buildDefinition(abstractService, xsdgen);
	}

	/**
	 * Build the WSDL model.
	 * 
	 * @param abstractService
	 *            collected Service information
	 * @param prebuild
	 *            XSD generator for type section
	 * @return WSDL definition
	 */
	public Definition buildDefinition(AbstractService abstractService,
			XsdSchemaGenerator xsdgen) throws WSDLException,
			java.lang.Exception {
		String serviceName = helper
				.reformatOWLSSupportedByString(abstractService.getID());
		String serviceDescription = abstractService.getDescription();

		// sp�ter f�r jedem OutputParameter ein get oder bei mehreren Outputs
		// entweder zus�tzlicher Container oder mehrere Operationen.
		Vector<AbstractServiceParameter> inputParameter = abstractService
				.getInputParameter();
		Vector<AbstractServiceParameter> outputParameter = abstractService
				.getOutputParameter();

		System.out.println("[BUILD] Servicename: " + serviceName);
		System.out.println("[BUILD] description: " + serviceDescription);

		for (int i = 0; i < abstractService.getOutputParameter().size(); i++) {
			String operationName = "get"
					+ ((AbstractServiceParameter) abstractService
							.getOutputParameter().get(i)).getID();
			System.out.println("[BUILD] Operation  : " + operationName);
		}

		if (!this.validateServiceParameterTypes(abstractService)) {
			throw new Exception("Error in parameter list. Datatype not found.");
		}

		WSDLFactory wsdlFactory = WSDLFactory.newInstance();
		ExtensionRegistry extensionRegistry = wsdlFactory
				.newPopulatedExtensionRegistry();
		Definition def = wsdlFactory.newDefinition();

		SchemaSerializer schemaSer = new SchemaSerializer();
		extensionRegistry.setDefaultSerializer(schemaSer);

		//
		// NAMESPACE
		//
		// e.g. "http://dmas.dfki.de/axis/services/"

		int index = abstractService.getBase().lastIndexOf(".");

		String targetNS = abstractService.getBase().substring(0, index);

		if (OWLS2WSDLSettings.getInstance().getProperty("CHANGE_TNS")
				.equals("yes")) {
			String tns_basepath = OWLS2WSDLSettings.getInstance().getProperty(
					"TNS_BASEPATH");
			targetNS = tns_basepath + serviceName;
			def.setQName(new QName(tns_basepath, serviceName)); // +"Service"));
		} else {
			def.setQName(new QName(abstractService.getBasePath(), serviceName)); // abstractService.getID()));
		}

		System.out.println("abstractService.getBase    : "
				+ abstractService.getBase());
		System.out.println("abstractService.getBasePath: "
				+ abstractService.getBasePath());

		// def.setDocumentBaseURI("http://document/base");
		def.setTargetNamespace(targetNS);
		def.addNamespace("tns", targetNS);
		def.addNamespace("intf", targetNS);
		def.addNamespace("impl", targetNS + "-impl");
		def.addNamespace("", targetNS);

		def.addNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
		def.addNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/");
		def.addNamespace("wsdlsoap", "http://schemas.xmlsoap.org/wsdl/soap/");
		def.addNamespace("SOAP-ENC",
				"http://schemas.xmlsoap.org/soap/encoding/");
		def.addNamespace("apachesoap", "http://xml.apache.org/xml-soap");

		System.out.println("INFO: " + def.getQName().toString());
		System.out.println("tns : " + def.getNamespace("tns" + serviceName));

		// WA: xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
		// WA: xmlns:wsdlsoap="http://schemas.xmlsoap.org/wsdl/soap/"

		//
		// IMPORTS AND TYPES
		//
		/*
		 * Import importsec = def.createImport(); importsec.setDefinition(def);
		 * importsec.setLocationURI("locationURI");
		 * importsec.setNamespaceURI("nsURI"); def.addImport(importsec);
		 */

		// LESEN DES SCHEMAS AUS DATEISYSTEM
		// ===========================================
		// DOMParser domp = new DOMParser();
		// try {
		// //domp.parse("file:/D:/development/xsd/generated.xsd");
		// domp.parse("file:/D:/tmp/StEmilion.xsd");
		// }
		// catch(Exception e) { e.printStackTrace(); }
		//
		// Document doc = domp.getDocument();
		// Element element = doc.getDocumentElement();
		// =============================================================================

		Element element = null;

		try {
			// construct schema model for all parameter
			for (Iterator<AbstractServiceParameter> it = inputParameter
					.iterator(); it.hasNext();) {
				AbstractServiceParameter param = it.next();
				System.out.println("[BUILD] IN         :" + param.getUri());
				if (!this.isPrimitiveType(param.getUri())) {
					xsdgen.appendToSchema(AbstractDatatypeKB.getInstance()
							.getAbstractDatatypeKBData().get(param.getUri()));
					System.out.println("[BUILD] added to type section.");
				}
			}
			for (Iterator<AbstractServiceParameter> it = outputParameter
					.iterator(); it.hasNext();) {
				AbstractServiceParameter param = it.next();
				System.out.println("[BUILD] OUT        :" + param.getUri());
				if (!this.isPrimitiveType(param.getUri())) {
					xsdgen.appendToSchema(AbstractDatatypeKB.getInstance()
							.getAbstractDatatypeKBData().get(param.getUri()));
					System.out.println("[BUILD] added to type section.");
				}
			}

			xsdgen.deleteObsoleteTypesFromSchema();

			// org.jdom.Document jdoc =
			// XMLUtils.convertSchemaToElement(AbstractDatatypeKB.getInstance().getXmlSchemaElement("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#StEmilion",
			// false, -1)).getDocument();
			org.jdom.Document jdoc = XMLUtils.convertSchemaToElement(
					xsdgen.getSchema()).getDocument();

			DOMOutputter w3cOutputter = new DOMOutputter();
			Document doc = w3cOutputter.output(jdoc);
			element = doc.getDocumentElement();

			NamedNodeMap attList = element.getAttributes();
			for (int i = 0; i < attList.getLength(); i++) {
				Node n = element.getAttributes().item(i);
				if (n.getTextContent().equals(
						"http://www.w3.org/2001/XMLSchema")) {
					element.removeAttributeNode((Attr) n);
				}
			}

			element.setAttribute("targetNamespace", targetNS);
			element.setAttribute("xmlns", targetNS);
		} catch (org.jdom.JDOMException jdome) {
			jdome.printStackTrace();
		} catch (org.xml.sax.SAXException saxe) {
			saxe.printStackTrace();
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		UnknownExtensibilityElement extensibilityElement = new UnknownExtensibilityElement();
		extensibilityElement.setElement(element);
		extensibilityElement
				.setElementType(new QName(element.getNamespaceURI()));
		extensibilityElement.setRequired(Boolean.TRUE);
		// System.out.println("EXENSIBILITYELEMENT: "+extensibilityElement);

		Types types = def.getTypes();
		types = def.createTypes();
		types.addExtensibilityElement(extensibilityElement);
		def.setTypes(types);

		// Schema schema = new SchemaImpl();
		// DOMParser domp = new DOMParser();
		// try {
		// domp.parse("file:/D:/development/xsd/generated.xsd");
		// Document doc = domp.getDocument();
		// NodeList nodes = doc.getElementsByTagName("xsd:schema");
		// for(int i=0; i<nodes.getLength();i++) {
		// schema.setElement(doc.getDocumentElement());
		// schema.setElementType(new QName(nodes.item(i).getNamespaceURI()));
		// schema.setDocumentBaseURI(nodes.item(i).getNamespaceURI());
		// types.addExtensibilityElement(schema);
		// def.setTypes(types);
		// }
		// System.out.println("DOC1:"+doc.toString());
		// System.out.println("NODES:"+nodes.getLength());
		// }
		// catch(Exception e) {
		// e.printStackTrace();
		// }
		// //UnknownExtensibilityElement extensibilityElement = new
		// UnknownExtensibilityElement();

		// // org.jdom.Element jdelem = XMLUtils.convertSchemaToElement(schema);
		// // System.out.println("JDOM ELEMENT: "+jdelem.toString());

		// http://mail-archives.apache.org/mod_mbox/ws-axis-dev/200406.mbox/%3c3260.203.94.92.220.1087572286.squirrel@203.94.92.220%3e
		//
		// Schema schema = (Schema)
		// extensionRegistry.createExtension(javax.wsdl.Types, new
		// QName("http://www.w3.org/2001/XMLSchema", "schema"));
		// DocumentBuilderFactory factory =
		// DocumentBuilderFactory.newInstance();
		// DocumentBuilder builder = factory.newDocumentBuilder();
		// Document schemaDeclaration = builder.newDocument();
		// //... (populate schemaDeclaration with elements and types)
		// schema.setDeclaration(schemaDeclaration);
		// types.addExtensibilityElement(schema);

		// (get-) Operation name
		String operationName = "get";
		for (int i = 0; i < outputParameter.size(); i++) {
			AbstractServiceParameter param = (AbstractServiceParameter) outputParameter
					.get(i);
			operationName += param.getID();
		}

		//
		// MESSAGES, PARTS
		// =================================================================
		Message request = def.createMessage();
		request.setQName(new QName(targetNS, operationName + "Request"));

		boolean duplicateInputs = false;
		boolean duplicateOutputs = false;
		if (abstractService.hasDuplicateInputParameter()) {
			duplicateInputs = true;
		}
		if (abstractService.hasDuplicateOutputParameter()) {
			duplicateOutputs = true;
		}

		for (int ipi = 0; ipi < inputParameter.size(); ipi++) {
			AbstractServiceParameter param = (AbstractServiceParameter) inputParameter
					.get(ipi);
			Part part = def.createPart();
			if (duplicateInputs) {
				part.setName(param.getID() + String.valueOf(param.getPos()));
			} else {
				part.setName(param.getID());
			}
			System.out.println("SET TYPE OF PART: " + param.toString());
			if (param.isPrimitiveXsdType()) {
				part.setTypeName(new QName("http://www.w3.org/2001/XMLSchema",
						param.getTypeLocal()));
			} else {
				part.setTypeName(new QName(targetNS, param.getTypeRemote()));
			}
			request.addPart(part);

		}
		request.setUndefined(false);
		def.addMessage(request);

		Message response = def.createMessage();
		response.setQName(new QName(targetNS, operationName + "Response"));
		for (int opi = 0; opi < outputParameter.size(); opi++) {
			AbstractServiceParameter param = (AbstractServiceParameter) outputParameter
					.get(opi);
			Part part = def.createPart();
			if (duplicateOutputs) {
				part.setName(param.getID() + String.valueOf(param.getPos()));
			} else {
				part.setName(param.getID());
			}
			System.out.println("SET TYPE OF PART: " + param.toString());
			if (param.isPrimitiveXsdType()) {
				part.setTypeName(new QName("http://www.w3.org/2001/XMLSchema",
						param.getTypeLocal()));
			} else {
				part.setTypeName(new QName(targetNS, param.getTypeRemote()));
			}
			response.addPart(part);
		}
		response.setUndefined(false);
		def.addMessage(response);

		//
		// PORTTYPE, OPERATION
		//
		Input input = def.createInput();
		input.setMessage(request);
		Output output = def.createOutput();
		output.setMessage(response);

		// == build the wsdl operation + bindings for each owls output parameter

		Operation operation = def.createOperation();
		operation.setName(operationName);
		operation.setInput(input);
		operation.setOutput(output);
		operation.setUndefined(false);

		// == add PortType and Binding to WSDL defintion

		PortType portType = def.createPortType();
		portType.setQName(new QName(targetNS, serviceName + "Soap"));
		portType.addOperation(operation);
		portType.setUndefined(false);
		def.addPortType(portType);

		// == Binding section

		SOAPBinding soapBinding = (SOAPBinding) extensionRegistry
				.createExtension(Binding.class, new QName(
						"http://schemas.xmlsoap.org/wsdl/soap/", "binding"));
		soapBinding.setTransportURI("http://schemas.xmlsoap.org/soap/http");
		soapBinding.setStyle("rpc");

		SOAPBody body = (SOAPBody) extensionRegistry.createExtension(
				BindingInput.class, new QName(
						"http://schemas.xmlsoap.org/wsdl/soap/", "body"));
		body.setUse("literal");
		ArrayList<String> listOfStyles = new ArrayList<String>();
		listOfStyles.add("http://schemas.xmlsoap.org/soap/encoding/");
		body.setEncodingStyles(listOfStyles);
		body.setNamespaceURI(targetNS);

		Binding binding = def.createBinding();
		binding.setQName(new QName(targetNS, serviceName + "SoapBinding"));
		binding.addExtensibilityElement(soapBinding);

		BindingInput binding_input = def.createBindingInput();
		// binding_input.setName("BINDING IN");
		binding_input.addExtensibilityElement(body);
		BindingOutput binding_out = def.createBindingOutput();
		// binding_out.setName("BINDING OUT");
		binding_out.addExtensibilityElement(body);

		SOAPOperation soapOperation = (SOAPOperation) extensionRegistry
				.createExtension(BindingOperation.class, new QName(
						"http://schemas.xmlsoap.org/wsdl/soap/", "operation"));
		soapOperation.setSoapActionURI("");
		// soapOperation.setStyle("document");

		BindingOperation binding_op = def.createBindingOperation();
		binding_op.setName(operationName);
		binding_op.addExtensibilityElement(soapOperation);
		binding_op.setOperation(operation);
		binding_op.setBindingInput(binding_input);
		binding_op.setBindingOutput(binding_out);
		binding.addBindingOperation(binding_op);

		binding.setPortType(portType);

		binding.setUndefined(false);
		def.addBinding(binding);

		//
		// SERVICE
		//
		SOAPAddress soapAddress = (SOAPAddress) extensionRegistry
				.createExtension(Port.class, new QName(
						"http://schemas.xmlsoap.org/wsdl/soap/", "address"));
		soapAddress.setLocationURI(targetNS);

		Port port = def.createPort();
		port.setName(serviceName + "Soap");
		port.setBinding(binding);
		port.addExtensibilityElement(soapAddress);

		// description fehlt noch

		Service service = def.createService();
		// service.setDocumentationElement()
		service.setQName(new QName(targetNS, serviceName + "Service"));
		service.addPort(port);

		def.addService(service);

		return def;
	}

	public static void main(String[] args) {

		XsdSchemaGenerator xsdgen = new XsdSchemaGenerator("WSDLTYPE", false,
				2, AbstractDatatype.InheritanceByNone,
				"http://www.w3.org/2001/XMLSchema#string");

		File f = new File(
				"D:\\owls2wsdl\\owls2wsdl_projects_with_datatypes\\q01_economy-car_price_service_datatypes.xml");
		System.out
				.println("[i] STARTING ProjectLoadingThread: " + f.toString());
		ProjectLoadingThread t = new ProjectLoadingThread(f);
		t.setFile(f);
		t.run();

		Project p = de.dfki.dmas.owls2wsdl.gui.RuntimeModel.getInstance()
				.getProject();
		// p.getAbstractServiceCollection().printFullData();
		AbstractService aService = p.getAbstractServiceCollection()
				.getAbstractService("CAR_RECOMMENDEDPRICE_SERVICE");
		aService.printInfo();

		try {
			javax.wsdl.Definition wsdl = WSDLBuilder.getInstance()
					.buildDefinition(aService, xsdgen);
			WSDLBuilder.getInstance().printSchema(wsdl, System.out);
		} catch (WSDLException e) {
			System.out.println("[e] WSDLException: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("[e] Exception: " + e.getMessage());
			e.printStackTrace();
		}

		// // try {
		// // builder.readWSDL("file:/D:/tmp/EADSServiceMod2.wsdl");
		// // }
		// // catch(WSDLException e) {
		// // e.printStackTrace();
		// // }
		// // System.exit(1);
		//
		// // String ex_1 =
		// "file:/D:/development/netbeans-projects/XsdTypeExtraction/src/zipCode.wsdl";
		//
		//
		//
		//
		// // try {
		//
		// //WSDLBuilder.getInstance().buildEx();
		//
		// //Ex2
		// AbstractService abstractService = new AbstractService();
		//
		// Vector inputParameter = new Vector();
		// AbstractServiceParameter paramIn1 = new AbstractServiceParameter(
		// "InputPrice",
		// "http://www.mindswap.org/2004/owl-s/concepts.owl#Price", 0);
		// inputParameter.add(paramIn1);
		// //abstractService.set
		//
		// AbstractServiceParameter paramIn2 = new AbstractServiceParameter(
		// "OutputCurrency","http://www.daml.ecs.soton.ac.uk/ont/currency.owl#Currency",
		// 1);
		// inputParameter.add(paramIn2);
		//
		// Vector outputParameter = new Vector();
		// AbstractServiceParameter paramOut1 = new AbstractServiceParameter(
		// "OutputWine",
		// "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#StEmilion", 0);
		// //"OutputPrice",
		// "http://www.mindswap.org/2004/owl-s/concepts.owl#Price", 0);
		// outputParameter.add(paramOut1);
		//
		// // Definition def = WSDLBuilder.getInstance().buildDefinition(
		// // "CurrencyConverterService",
		// // "Converts the given price to another currency.",
		// // "getOutputPrice",
		// // inputParameter,
		// // outputParameter);
		// //
		// // WSDLBuilder.getInstance().printSchema(def, System.out);
		// // }
		// // catch(WSDLException e) {
		// // e.printStackTrace();
		// // }
	}

	// /**
	// * Returns the desired ExtensibilityElement if found in the List
	// *
	// * @param extensibilityElements
	// * The list of extensibility elements to search
	// * @param elementType
	// * The element type to find
	// *
	// * @return Returns the first matching element of type found in the list
	// */
	// private static ExtensibilityElement findExtensibilityElement(List
	// extensibilityElements,
	// String elementType) {
	// if (extensibilityElements != null) {
	// Iterator<ExtensibilityElement> iter = extensibilityElements.iterator();
	//
	// while (iter.hasNext()) {
	// ExtensibilityElement element = iter.next();
	//
	// if
	// (element.getElementType().getLocalPart().equalsIgnoreCase(elementType)) {
	// // Found it
	// return element;
	// }
	// }
	// }
	//
	// return null;
	// }
}
