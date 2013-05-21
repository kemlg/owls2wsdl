/*
 * ServiceParser.java
 *
 * Created on 25. August 2006, 17:28
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

/*
 import org.mindswap.owl.OWLErrorHandler;
 import org.mindswap.owl.OWLIndividual;
 import org.mindswap.owl.OWLKnowledgeBase;
 import org.mindswap.owl.OWLFactory;
 import org.mindswap.owl.OWLReader;
 import org.mindswap.owl.OWLWriter;
 import org.mindswap.owls.validation.OWLSValidator;
 import org.mindswap.owl.OWLCache;
 import org.mindswap.owls.service.Service;
 import org.mindswap.owls.profile.Profile;
 import org.mindswap.owls.process.Process;
 import org.mindswap.owls.process.InputList;
 import org.mindswap.owls.process.Input;
 import org.mindswap.owls.process.OutputList;
 import org.mindswap.owls.process.Output;
 import org.mindswap.owls.process.ParameterList;
 import org.mindswap.owls.process.Parameter;
 import org.mindswap.owls.grounding.AtomicGrounding;
 import org.mindswap.owl.OWLOntology;
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Parser for OWL-S defined services. Anfangs �ber Mindswap OWL-S Model
 * durchgef�hrt. Aktuell rein �ber DOM.
 * 
 * @author Oliver Fourman
 */
public class ServiceParser {
	private Vector<URI> pathList;

	/** Creates a new instance of ServiceParser */
	public ServiceParser() {
		System.out.println("[C] ServiceParser");
		this.pathList = new Vector<URI>();
	}

	public ServiceParser(String path) {
		// this();
		//
		// try {
		// URI curPath = new URI(path);
		// if(curPath.getScheme().equals("http")) {
		// this.pathList.add(path);
		// }
		// else {
		// File file = new File(curPath);
		// if(file.isDirectory()) {
		// this.buildFileList(file);
		// }
		// else {
		// System.out.println("Add "+curPath+" to filelist.");
		// this.pathList.add(curPath);
		// }
		// }
		// System.out.println("PARAMETER: "+curPath.toString());
		// System.out.println("GESAMT: "+this.pathList.size());
		// }
		// catch(Exception e) {
		// e.printStackTrace();
		// }
	}

	private final void buildFileList(File file) throws URISyntaxException,
			FileNotFoundException, Exception {
		// System.out.println("Path to owl-s directory: "+file.toURL().toString());
		File[] entries = file.listFiles();
		for (int i = 0; i < entries.length; i++) {
			if (entries[i].isFile()) {
				// System.out.println("Add file://"+((File)entries[i]).toURI().getPath()+" to filelist.");
				if (((File) entries[i]).getName().toLowerCase()
						.endsWith(".owls")) {
					// pathList.add("file://"+((File)entries[i]).toURI().getPath());
					// System.out.println("+ "+((File)entries[i]).toURI());
					this.pathList.add(((File) entries[i]).toURI());
				}
			} else if (entries[i].isDirectory()) {
				this.buildFileList((File) entries[i]);
			} else {
				throw new Exception("No file and no directory.");
			}
		}
	}

	// public final String getFileList(Vector listOfFiles) {
	// String temp = "";
	// for(int i=0; i<listOfFiles.size(); i++) {
	// temp += listOfFiles.get(i).toString();
	// if(!listOfFiles.get(i).equals(listOfFiles.lastElement())) {
	// temp += ", ";
	// }
	// }
	// return temp;
	// }

	// public void validate() throws URISyntaxException, FileNotFoundException {
	// OWLKnowledgeBase kb = OWLFactory.createKB();
	// kb.setReasoner("Pellet");
	// kb.getReader().getCache().setLocalCacheDirectory("D:\\tmp\\owlscache");
	//
	// OWLReader aReader = OWLFactory.createReader();
	// kb.setReader(aReader);
	//
	// // read in the file specified by the URI using the newly created reader
	// OWLOntology aOntology = aReader.read(URI.create(this._filename));
	// // create a writer using the owl factory
	// OWLWriter aWriter = OWLFactory.createWriter();
	// // write this ontology out to the specified output stream
	// //aWriter.write(aOntology,System.out);
	//
	// // System.out.println("SIZE: "+aOntology.getIndividuals().size());
	// // Iterator curit = aOntology.getIndividuals().iterator();
	// // while(curit.hasNext()) {
	// // OWLIndividual cur_individual = (OWLIndividual)curit.next();
	// // System.out.println("TYPE      : "+cur_individual.toPrettyString());
	// // //System.out.println("LOCAL_NAME: "+cur_individual.getLocalName());
	// // //System.out.println("IND: "+cur_individual.toRDF());
	// // }
	//
	// // OWLReader reader = kb.getReader();
	// // MyOWLErrorHandler erhandler = new MyOWLErrorHandler();
	// // reader.setErrorHandler((OWLErrorHandler)erhandler);
	//
	// Service service = kb.readService(this._filename);
	// OWLSValidator validator = new OWLSValidator();
	// validator.validate(service.getOntology());
	// }

	public Vector<AbstractService> parse(URI path) throws Exception {
		Vector<AbstractService> collectedServices = new Vector<AbstractService>();
		if (path.getScheme().equals("http")) {
			this.pathList.add(path);
		} else {
			File file = new File(path);
			if (file.isDirectory()) {
				this.buildFileList(file);
			} else {
				System.out.println("Add " + path + " to filelist.");
				this.pathList.add(path);
			}
		}
		System.out.println("GESAMT: " + pathList.size());

		for (Iterator<URI> it = this.pathList.iterator(); it.hasNext();) {
			String curPathItem = it.next().toString();
			// System.out.println("PARSING: "+curPathItem.substring(curPathItem.lastIndexOf('/')+1,curPathItem.length()));
			// this.parseWithOWLSApi(curPath);
			AbstractService servs[] = parseWithDOM(curPathItem);
			for (int i = 0; i < servs.length; i++) {
				collectedServices.add(servs[i]);
			}
		}
		this.pathList.removeAllElements();
		return collectedServices;
	}

	public Vector<AbstractService> parse(String path)
			throws NullPointerException {
		Vector<AbstractService> collectedServices = new Vector<AbstractService>();
		URI curPath = null;
		try {
			curPath = new URI(path);
		} catch (URISyntaxException uriex) {
			// try to convert...
			File tempf = new File(path);
			curPath = tempf.toURI();
		}
		if (curPath.getScheme().equals("http")) {
			// this.pathList.add(path);
			this.pathList.add(curPath);
		} else {
			File file = new File(curPath);
			if (file.isDirectory()) {
				try {
					this.buildFileList(file);
				} catch (Exception e) {
					System.err.println("Error building FileList");
					e.printStackTrace();
				}
			} else {
				System.out.println("Add " + curPath + " to filelist.");
				this.pathList.add(curPath);
			}
		}
		System.out.println("PARAMETER: " + curPath.toString());
		System.out.println("GESAMT: " + pathList.size());

		for (Iterator<URI> it = this.pathList.iterator(); it.hasNext();) {
			String curPathItem = it.next().toString();
			// System.out.println("PARSING: "+curPathItem.substring(curPathItem.lastIndexOf('/')+1,curPathItem.length()));
			// this.parseWithOWLSApi(curPath);
			AbstractService servs[] = parseWithDOM(curPathItem);
			for (int i = 0; i < servs.length; i++) {
				collectedServices.add(servs[i]);
			}
		}
		this.pathList.removeAllElements();
		return collectedServices;
	}

	// /*
	// * Wrapper to print all services
	// */
	// public void printCollectedServices() {
	// System.out.println("=================================================");
	// this.collectedServices.printFullData();
	// System.out.println("=================================================");
	// }

	// /*
	// * // OWL-S parsing
	// =======================================================
	// */
	// public void parseWithOWLSApi() throws URISyntaxException,
	// FileNotFoundException
	// {
	// Service service;
	// Profile profile;
	// Process process;
	// OWLKnowledgeBase kb = OWLFactory.createKB();
	//
	// // kb.setReasoner("Pellet");
	// // OWLCache cache = kb.getReader().getCache();
	// // cache.setLocalCacheDirectory("D\\:/tmp/owlscache");
	// // cache.setForced(true);
	//
	// service = kb.readService(this._filename);
	// System.out.println("RDF:\n"+service.toRDF());
	//
	// AbstractService curServ =
	// new AbstractService(this._filename,
	// service.getQName(),
	// service.getProfile().getServiceName(),
	// service.getProfile().getTextDescription());
	//
	// // Beinhaltet auch alle importierten Ontologien, daher werden die imports
	// �ber DOM eingelesen...
	// // Set ontset = kb.getOntologies();
	// // Iterator ontit = ontset.iterator();
	// // while(ontit.hasNext()) {
	// //
	// System.out.println("ONT: "+((org.mindswap.owl.OWLOntology)ontit.next()).toString());
	// // }
	//
	// // GENERAL ===
	// // System.out.println("QNAME: "+service.getQName());
	// // System.out.println("PROFILE: "+service.getProfile().toRDF());
	// // System.out.println("ServiceName: "+service.getName());
	// //
	// System.out.println("ServiceName: "+service.getProfile().getServiceName());
	// //
	// System.out.println("ServiceDescription: "+service.getProfile().getTextDescription());
	//
	// // INPUTS ===
	// InputList inputs = service.getProfile().getInputs();
	// for(int i=0; i<inputs.size();i++) {
	// org.mindswap.owls.process.Parameter cur =
	// (org.mindswap.owls.process.Parameter)inputs.inputAt(i);
	// curServ.addInputParameter(cur.getLocalName(),
	// cur.getParamType().getURI().toString());
	// // System.out.println("PARAM: "+cur.getLocalName()); //toPrettyString());
	// // System.out.println("TYPE : "+cur.getParamType().getURI());
	// }
	//
	// // OUTPUTS ===
	// OutputList outputs = service.getProfile().getOutputs();
	// for(int i=0; i<outputs.size();i++) {
	// org.mindswap.owls.process.Parameter cur =
	// (org.mindswap.owls.process.Parameter)outputs.outputAt(i);
	// curServ.addOutputParameter(cur.getLocalName(),
	// cur.getParamType().getURI().toString());
	// // System.out.println("PARAM: "+cur.getLocalName()); //toPrettyString());
	// // System.out.println("TYPE : "+cur.getParamType().getURI());
	// }
	//
	// curServ.printInfo();
	// }

	/*
	 * // DOM parsing =========================================================
	 */
	public AbstractService[] parseWithDOM(String curPath) {
		AbstractService servs[];
		AbstractService curServ;
		NodeList nodes, nodesService, nodesGlobal;
		int i, nService;
		Document docService;
		Map<String,AtomicProcess> mapProcesses = new HashMap<String,AtomicProcess>();
		Map<String,String> mapInputs = new HashMap<String,String>();
		Map<String,String> mapOutputs = new HashMap<String,String>();
		Map<String,Boolean> mapIsLabel = new HashMap<String,Boolean>();
		
		DOMParser domp = new DOMParser();

		try {
			// domp.setFeature ("http://xml.org/dom/features/validation", true);
			System.out.println("parseWithDOM:" + curPath);
			domp.parse(curPath);
			Document doc = domp.getDocument();

			NodeList nodesHasInput = doc.getElementsByTagName("process:hasInput");
			for(int l=0;l<nodesHasInput.getLength();l++) {
				Node nodeHasInput = nodesHasInput.item(l);
				Node value = nodeHasInput.getAttributes().getNamedItem("rdf:resource");
				if(value == null) {
					NodeList nodesProcess = doc.getElementsByTagName("process:Input");
					// System.out.println("process:Input: "+ nodes.getLength() +
					// " elements.");
					for (int k = 0; k < nodesProcess.getLength(); k++) {
						// System.out.println("rdfID       : "+nodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue());
						NodeList inputlist = nodesProcess.item(k).getChildNodes();
						String inputID = nodesProcess.item(k).getAttributes()
								.getNamedItem("rdf:ID").getNodeValue();
						String inputParam = "";
						String inputLabel = "";
						for (int j = 0; j < inputlist.getLength(); j++) {
							if (inputlist.item(j).getNodeName()
									.equals("process:parameterType")) {
//								if (curServ.getVersion().equals("1.0")) {
//									inputParam = inputlist.item(j).getAttributes()
//											.getNamedItem("rdf:resource")
//											.getNodeValue();
//								} else if (curServ.getVersion().equals("1.1")) {
//									inputParam = inputlist.item(j).getTextContent();
//								} else if (curServ.getVersion().equals("1.2")) {
									inputParam = inputlist.item(j).getTextContent();
//								} else {
//									throw new Exception("OWL-S Version not known.");
//								}
								mapInputs.put(inputID, inputParam);
								mapIsLabel.put(inputID, false);
							}
							if (inputlist.item(j).getNodeName()
									.equals("rdfs:label")) {
								// System.out.println("rdfs:label  : "+inputlist.item(j).getTextContent());
								inputLabel = inputlist.item(j).getTextContent();
								mapInputs.put(inputID, inputLabel);
								mapIsLabel.put(inputID, true);
							}
						}
					}
				}
			}

			nodesHasInput = doc.getElementsByTagName("profile:hasInput");
			for(int l=0;l<nodesHasInput.getLength();l++) {
				Node nodeHasInput = nodesHasInput.item(l);
				Node value = nodeHasInput.getAttributes().getNamedItem("rdf:resource");
				if(value == null) {
					NodeList nodesProcess = doc.getElementsByTagName("process:Input");
					// System.out.println("process:Input: "+ nodes.getLength() +
					// " elements.");
					for (int k = 0; k < nodesProcess.getLength(); k++) {
						// System.out.println("rdfID       : "+nodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue());
						NodeList inputlist = nodesProcess.item(k).getChildNodes();
						String inputID = nodesProcess.item(k).getAttributes()
								.getNamedItem("rdf:ID").getNodeValue();
						String inputParam = "";
						String inputLabel = "";
						for (int j = 0; j < inputlist.getLength(); j++) {
							if (inputlist.item(j).getNodeName()
									.equals("process:parameterType")) {
//								if (curServ.getVersion().equals("1.0")) {
//									inputParam = inputlist.item(j).getAttributes()
//											.getNamedItem("rdf:resource")
//											.getNodeValue();
//								} else if (curServ.getVersion().equals("1.1")) {
//									inputParam = inputlist.item(j).getTextContent();
//								} else if (curServ.getVersion().equals("1.2")) {
									inputParam = inputlist.item(j).getTextContent();
//								} else {
//									throw new Exception("OWL-S Version not known.");
//								}
								mapInputs.put(inputID, inputParam);
								mapIsLabel.put(inputID, false);
							}
							if (inputlist.item(j).getNodeName()
									.equals("rdfs:label")) {
								// System.out.println("rdfs:label  : "+inputlist.item(j).getTextContent());
								inputLabel = inputlist.item(j).getTextContent();
								mapInputs.put(inputID, inputLabel);
								mapIsLabel.put(inputID, true);
							}
						}
					}
				}
			}


			NodeList nodesHasOutput = doc.getElementsByTagName("process:hasOutput");
			for(int l=0;l<nodesHasOutput.getLength();l++) {
				Node nodeHasOutput = nodesHasOutput.item(l);
				Node value = nodeHasOutput.getAttributes().getNamedItem("rdf:resource");
				if(value == null) {
					NodeList nodesProcess = doc.getElementsByTagName("process:Output");
					// System.out.println("process:Output: "+ nodes.getLength() +
					// " elements.");
					for (int k = 0; k < nodesProcess.getLength(); k++) {
						// System.out.println("rdfID       : "+nodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue());
						NodeList outputlist = nodesProcess.item(k).getChildNodes();
						String outputID = nodesProcess.item(k).getAttributes()
								.getNamedItem("rdf:ID").getNodeValue();
						String outputParam = "";
						String outputLabel = "";
						for (int j = 0; j < outputlist.getLength(); j++) {
							if (outputlist.item(j).getNodeName()
									.equals("process:parameterType")) {
//								if (curServ.getVersion().equals("1.0")) {
//									outputParam = outputlist.item(j).getAttributes()
//											.getNamedItem("rdf:resource")
//											.getNodeValue();
//								} else if (curServ.getVersion().equals("1.1")) {
//									outputParam = outputlist.item(j).getTextContent();
//								} else if (curServ.getVersion().equals("1.2")) {
									outputParam = outputlist.item(j).getTextContent();
//								} else {
//									throw new Exception("OWL-S Version not known.");
//								}
								mapOutputs.put(outputID, outputParam);
								mapIsLabel.put(outputID, false);
							}
							if (outputlist.item(j).getNodeName()
									.equals("rdfs:label")) {
								// System.out.println("rdfs:label  : "+outputlist.item(j).getTextContent());
								outputLabel = outputlist.item(j).getTextContent();
								mapOutputs.put(outputID, outputLabel);
								mapIsLabel.put(outputID, true);
							}
						}
					}
				}
			}
			
			nodesHasOutput = doc.getElementsByTagName("profile:hasOutput");
			for(int l=0;l<nodesHasOutput.getLength();l++) {
				Node nodeHasOutput = nodesHasOutput.item(l);
				Node value = nodeHasOutput.getAttributes().getNamedItem("rdf:resource");
				if(value == null) {
					NodeList nodesProcess = doc.getElementsByTagName("process:Output");
					// System.out.println("process:Output: "+ nodes.getLength() +
					// " elements.");
					for (int k = 0; k < nodesProcess.getLength(); k++) {
						// System.out.println("rdfID       : "+nodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue());
						NodeList outputlist = nodesProcess.item(k).getChildNodes();
						String outputID = nodesProcess.item(k).getAttributes()
								.getNamedItem("rdf:ID").getNodeValue();
						String outputParam = "";
						String outputLabel = "";
						for (int j = 0; j < outputlist.getLength(); j++) {
							if (outputlist.item(j).getNodeName()
									.equals("process:parameterType")) {
//								if (curServ.getVersion().equals("1.0")) {
//									outputParam = outputlist.item(j).getAttributes()
//											.getNamedItem("rdf:resource")
//											.getNodeValue();
//								} else if (curServ.getVersion().equals("1.1")) {
//									outputParam = outputlist.item(j).getTextContent();
//								} else if (curServ.getVersion().equals("1.2")) {
									outputParam = outputlist.item(j).getTextContent();
//								} else {
//									throw new Exception("OWL-S Version not known.");
//								}
								mapOutputs.put(outputID, outputParam);
								mapIsLabel.put(outputID, false);
							}
							if (outputlist.item(j).getNodeName()
									.equals("rdfs:label")) {
								// System.out.println("rdfs:label  : "+outputlist.item(j).getTextContent());
								outputLabel = outputlist.item(j).getTextContent();
								mapOutputs.put(outputID, outputLabel);
								mapIsLabel.put(outputID, true);
							}
						}
					}
				}
			}
			
			nodesService = doc.getElementsByTagName("process:AtomicProcess");
			System.out.println("process:AtomicProcess: " + nodesService.getLength()
					+ " elements.");
			for(i = 0;i<nodesService.getLength();i++) {
				Node n = nodesService.item(i);
				AtomicProcess ap = new AtomicProcess(n.getAttributes().getNamedItem("rdf:ID").getNodeValue());
				
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				docService = builder.newDocument();
				Node importedNode = docService.importNode(nodesService.item(i),
						true);
				docService.appendChild(importedNode);

				nodesHasInput = docService.getElementsByTagName("process:hasInput");
				for(int l=0;l<nodesHasInput.getLength();l++) {
					Node nodeHasInput = nodesHasInput.item(l);
					Node value = nodeHasInput.getAttributes().getNamedItem("rdf:resource");
					if(value == null) {
						NodeList nodesProcess = docService.getElementsByTagName("process:Input");
						for (int k = 0; k < nodesProcess.getLength(); k++) {
							// System.out.println("rdfID       : "+nodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue());
							String id = nodesProcess.item(k).getAttributes()
									.getNamedItem("rdf:ID").getNodeValue();
							boolean isLabel = mapIsLabel.get(id);
							if(isLabel) {
								ap.addInputLabel(id, mapInputs.get(id));
							} else {
								ap.addInputParameter(id, mapInputs.get(id));
							}
						}
					} else {
						String id = value.getNodeValue().substring(1);
						System.out.println("Value of parameter " + id + " is " + mapInputs.get(id));
						boolean isLabel = mapIsLabel.get(id);
						if(isLabel) {
							ap.addInputLabel(id, mapInputs.get(id));
						} else {
							ap.addInputParameter(id, mapInputs.get(id));
						}
						System.out.println(ap.getInputParameter());
					}
				}


				nodesHasOutput = docService.getElementsByTagName("process:hasOutput");
				for(int l=0;l<nodesHasOutput.getLength();l++) {
					Node nodeHasOutput = nodesHasOutput.item(l);
					Node value = nodeHasOutput.getAttributes().getNamedItem("rdf:resource");
					if(value == null) {
						NodeList nodesProcess = docService.getElementsByTagName("process:Output");
						// System.out.println("process:Output: "+ nodes.getLength() +
						// " elements.");
						for (int k = 0; k < nodesProcess.getLength(); k++) {
							// System.out.println("rdfID       : "+nodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue());
							String id = nodesProcess.item(k).getAttributes()
									.getNamedItem("rdf:ID").getNodeValue();
							boolean isLabel = mapIsLabel.get(id);
							if(isLabel) {
								ap.addOuputLabel(id, mapOutputs.get(id));
							} else {
								ap.addOutputParameter(id, mapOutputs.get(id));
							}
						}
					} else {
						String id = value.getNodeValue().substring(1);
						boolean isLabel = mapIsLabel.get(id);
						if(isLabel) {
							ap.addOuputLabel(id, mapOutputs.get(id));
						} else {
							ap.addOutputParameter(id, mapOutputs.get(id));
						}
					}
				}
				
				System.out.println("Inserting AtomicProcess " + ap.getName() + " with parameters: " +
						ap.getInputParameter() + ", " + ap.getOutputParameter());
				mapProcesses.put(ap.getName(), ap);
			}
				
			nodes = doc.getElementsByTagName("service:Service");
			System.out.println("service:Service: " + nodes.getLength()
					+ " elements.");
			servs = new AbstractService[nodes.getLength()];
			for (nService = 0; nService < nodes.getLength(); nService++) {
				curServ = new AbstractService();
				System.out.println("DATA:"
						+ nodes.item(nService).getAttributes()
								.getNamedItem("rdf:ID").getNodeValue());
				curServ.setID(nodes.item(nService).getAttributes()
						.getNamedItem("rdf:ID").getNodeValue());
				curServ.setFilename(curPath);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				docService = builder.newDocument();
				Node importedNode = docService.importNode(nodes.item(nService),
						true);
				docService.appendChild(importedNode);

				nodesGlobal = doc.getElementsByTagName("rdf:RDF");
				// System.out.println("rdf:RDF Namespace (attributes): "+nodes.getLength()
				// + " elements.");
				for (i = 0; i < nodesGlobal.getLength(); i++) {
					NamedNodeMap curAttrMap = nodesGlobal.item(i)
							.getAttributes();
					for (int j = 0; j < curAttrMap.getLength(); j++) {
						// System.out.println("INFO: "+curAttrMap.item(j).getNodeName()+" = "+curAttrMap.item(j).getNodeValue());
						curServ.addNamespaceEntry(curAttrMap.item(j)
								.getNodeName(), curAttrMap.item(j)
								.getNodeValue());
						if (curAttrMap
								.item(j)
								.getNodeValue()
								.equals("http://www.daml.org/services/owl-s/1.0/Service.owl#")) {
							curServ.setVersion("1.0");
						} else if (curAttrMap
								.item(j)
								.getNodeValue()
								.equals("http://www.daml.org/services/owl-s/1.1/Service.owl#")) {
							curServ.setVersion("1.1");
						} else if (curAttrMap
								.item(j)
								.getNodeValue()
								.equals("http://www.daml.org/services/owl-s/1.2/Service.owl#")) {
							curServ.setVersion("1.2");
						}
					}
				}

				nodesGlobal = doc.getElementsByTagName("owl:imports");
				// System.out.println("owl:Ontology (imports): "+
				// nodes.getLength() + " elements.");
				for (i = 0; i < nodesGlobal.getLength(); i++) {
					// System.out.println("DATA:"+nodes.item(i).getAttributes().getNamedItem("rdf:resource").getNodeValue());
					curServ.addImportedOWLFile(nodesGlobal.item(i)
							.getAttributes().getNamedItem("rdf:resource")
							.getNodeValue());
				}

				nodesService = docService
						.getElementsByTagName("profile:serviceName");
				// System.out.println("profile:serviceName: "+ nodes.getLength()
				// + " elements.");
				for (i = 0; i < nodesService.getLength(); i++) {
					// System.out.println("DATA:"+nodes.item(i).getTextContent());
					curServ.setName(nodesService.item(i).getTextContent());
				}

				nodesService = docService
						.getElementsByTagName("profile:textDescription");
				// System.out.println("profile:textDescription: "+
				// nodes.getLength() + " elements.");
				for (i = 0; i < nodesService.getLength(); i++) {
					// System.out.println("DATA:"+nodes.item(i).getTextContent());
					curServ.setDescription(nodesService.item(i)
							.getTextContent());
				}

				nodesService = docService.getElementsByTagName("grounding:WsdlAtomicProcessGrounding");
				System.out.println("grounding:WsdlAtomicProcessGrounding: " + nodesService.getLength()
						+ " elements.");
				for(i = 0;i<nodesService.getLength();i++) {
					Node n = nodesService.item(i);
					
					DocumentBuilderFactory factoryGrounding = DocumentBuilderFactory
							.newInstance();
					factoryGrounding.setNamespaceAware(true);
					DocumentBuilder builderGrounding = factoryGrounding.newDocumentBuilder();
					Document docGrounding = builderGrounding.newDocument();
					Node importedNodeGrounding = docGrounding.importNode(n,
							true);
					docGrounding.appendChild(importedNodeGrounding);

					Node process = docGrounding.getElementsByTagName("grounding:owlsProcess").item(0);
					Node value = process.getAttributes().getNamedItem("rdf:resource");
					
					DocumentBuilderFactory factoryOperation = DocumentBuilderFactory
							.newInstance();
					factoryOperation.setNamespaceAware(true);
					DocumentBuilder builderOperation = factoryOperation.newDocumentBuilder();
					Document docOperation = builderOperation.newDocument();
					Node importedNodeOperation = docOperation.importNode(n,
							true);
					docOperation.appendChild(importedNodeOperation);
					
					Node operation = docOperation.getElementsByTagName("grounding:operation").item(0);
					
					AtomicProcess ap;
					String id = null;
					if(value == null) {
						Node nAp = docGrounding.getElementsByTagName("process:AtomicProcess").item(0);
						id = nAp.getAttributes().getNamedItem("rdf:ID").getNodeValue();
					}
					else {
						id = value.getNodeValue().substring(1);
					}
					
					ap = mapProcesses.get(id);
					
					StringTokenizer stk = new StringTokenizer(operation.getTextContent(), "#");
					String token = "dummyOperation";
					while(stk.hasMoreTokens()) {
						token = stk.nextToken();
					}
					ap.setOperationName(token);
					System.out.println("found AtomicProcess: " + ap.getName() + " : " + ap.getOperationName());
					curServ.addProcess(ap);
				}
				
				servs[nService] = curServ;
			}
		} catch (Exception ex) {
			servs = new AbstractService[0];
			System.out.println(ex);
			ex.printStackTrace();
		}

		return servs;
	}
}
