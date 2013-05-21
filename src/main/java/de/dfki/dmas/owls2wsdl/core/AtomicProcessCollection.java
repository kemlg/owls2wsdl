package de.dfki.dmas.owls2wsdl.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class AtomicProcessCollection {

	private Vector<AtomicProcess> _processCollection;

	public AtomicProcessCollection(Collection<AtomicProcess> aps) {
		this._processCollection.addAll(aps);
	}
	
	public AtomicProcessCollection() {
		this._processCollection = new Vector<AtomicProcess>();
	}

	public Vector<String> getParameterTypes() {
		Vector<String> datatypeList = new Vector<String>();
		for (Iterator<AtomicProcess> it = this._processCollection.iterator(); it
				.hasNext();) {
			AtomicProcess aService = it.next();
			for (Iterator<AbstractServiceParameter> paramIt = aService
					.getInputParameter().iterator(); paramIt.hasNext();) {
				AbstractServiceParameter param = paramIt.next();
				if (!datatypeList.contains(param.getUri())) {
					datatypeList.add(param.getUri());
				}
			}
			for (Iterator<AbstractServiceParameter> paramIt = aService
					.getOutputParameter().iterator(); paramIt.hasNext();) {
				AbstractServiceParameter param = paramIt.next();
				if (!datatypeList.contains(param.getUri())) {
					datatypeList.add(param.getUri());
				}
			}
		}
		return datatypeList;
	}

}
