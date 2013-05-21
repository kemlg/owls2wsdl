package de.dfki.dmas.owls2wsdl.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class AtomicProcess {

	private String name;
	private Vector<AbstractServiceParameter> inputParameter;
	private Vector<AbstractServiceParameter> outputParameter;

	private HashMap<String, String> inputLabel;
	private HashMap<String, String> outputLabel;

	private int inputCount;
	private int outputCount;
	private String operationName;


	public AtomicProcess(String name) {
		this.name = name;
		this.inputParameter = new Vector<AbstractServiceParameter>();
		this.outputParameter = new Vector<AbstractServiceParameter>();

		this.inputLabel = new HashMap<String, String>();
		this.outputLabel = new HashMap<String, String>();
		// this.inputOrder = new Vector();
		// this.outputOrder = new Vector();
		this.inputCount = 0;
		this.outputCount = 0;
	}

	public Vector<AbstractServiceParameter> getInputParameter() {
		return this.inputParameter;
	}

	public Vector<AbstractServiceParameter> getOutputParameter() {
		return this.outputParameter;
	}

	public String toString() {
		return this.name;
	}
	
	/**
	 * Avoids duplicate ids in WSDL message. (WSDL validation)
	 */
	public boolean hasDuplicateInputParameter() {
		if (this.inputParameter.size() > 1) {
			AbstractServiceParameterComparator comp = new AbstractServiceParameterComparator();
			for (int i = 1; i < this.inputParameter.size(); i++) {
				if (comp.compare(this.inputParameter.get(0),
						this.inputParameter.get(i)) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Avoids duplicate ids in WSDL message. (WSDL validation)
	 */
	public boolean hasDuplicateOutputParameter() {
		if (this.outputParameter.size() > 1) {
			AbstractServiceParameterComparator comp = new AbstractServiceParameterComparator();
			for (int i = 1; i < this.outputParameter.size(); i++) {
				if (comp.compare(this.outputParameter.get(0),
						this.outputParameter.get(i)) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public void addInputParameter(String name, String paramType) {
		this.inputCount++;
		AbstractServiceParameter param = new AbstractServiceParameter(name,
				paramType, this.inputCount);
		// System.out.println("PARAM-I: "+param.toString());
		// this.inputParameter.put(name, param);
		this.inputParameter.add(param);
	}

	public void addOutputParameter(String name, String paramType) {
		this.outputCount++;
		AbstractServiceParameter param = new AbstractServiceParameter(name,
				paramType, this.outputCount);
		// System.out.println("PARAM-O: "+param.toString());
		// this.outputParameter.put(name, param);
		this.outputParameter.add(param);
	}

	public void addInputLabel(String name, String label) {
		this.inputLabel.put(name, label);
	}

	public void addOuputLabel(String name, String label) {
		this.outputLabel.put(name, label);
	}

	public void printInfo() {
		System.out.println("PROFILE : ProcessName: (" + this.name
				+ ")");

		for (Iterator<AbstractServiceParameter> it = this.inputParameter
				.iterator(); it.hasNext();) {
			System.out.println("INPUT   : " + it.next().toString());
		}
		for (Iterator<AbstractServiceParameter> it = this.outputParameter
				.iterator(); it.hasNext();) {
			System.out.println("OUTPUT  : " + it.next().toString());
		}
	}

	/**
	 * Checks all AbstractServiceParameter (input/output) for validation
	 * attribute attention: attributes are not persistent
	 * 
	 * @return status if service is translatable
	 */
	public boolean istranslatable() {
		System.out.println("is " + this.name + " translatable?");
		boolean returnVal = true;
		for (Iterator<AbstractServiceParameter> it = this.getInputParameter()
				.iterator(); it.hasNext();) {
			AbstractServiceParameter param = it.next();
			System.out.println("inspecting " + param.getID());
			if (!param.isValid() && !param.isPrimitiveXsdType()) {
				returnVal = false;
			}
		}
		for (Iterator<AbstractServiceParameter> it = this.getOutputParameter()
				.iterator(); it.hasNext();) {
			AbstractServiceParameter param = it.next();
			System.out.println("inspecting " + param.getID());
			if (!param.isValid() && !param.isPrimitiveXsdType()) {
				returnVal = false;
			}
		}
		
		System.out.println("Result: false");

		return returnVal;
	}
	
	public Object[] checkInput4InvalidNCNames() {
		Vector<String> nameList = new Vector<String>();
		for (Iterator<AbstractServiceParameter> it = this.getInputParameter()
				.iterator(); it.hasNext();) {
			AbstractServiceParameter param = it.next();
			if (param.isValidNCName()) {
				nameList.add(param.getUri());
			}
		}
		return nameList.toArray();
	}

	public Object[] checkOutput4InvalidNCNames() {
		Vector<String> nameList = new Vector<String>();
		for (Iterator<AbstractServiceParameter> it = this.getOutputParameter()
				.iterator(); it.hasNext();) {
			AbstractServiceParameter param = it.next();
			if (param.isValidNCName()) {
				nameList.add(param.getUri());
			}
		}
		return nameList.toArray();
	}

	public String getParameterType(String localName) {
		for (Iterator<AbstractServiceParameter> it = this.inputParameter
				.iterator(); it.hasNext();) {
			AbstractServiceParameter aParm = it.next();
			if (aParm.getID().equals(localName)) {
				return aParm.getUri();
			}
		}
		for (Iterator<AbstractServiceParameter> it = this.outputParameter
				.iterator(); it.hasNext();) {
			AbstractServiceParameter aParm = it.next();
			if (aParm.getID().equals(localName)) {
				return aParm.getUri();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationName() {
		return operationName;
	}
}
