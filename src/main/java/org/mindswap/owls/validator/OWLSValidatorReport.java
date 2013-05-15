package org.mindswap.owls.validator;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mindswap.owls.service.Service;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Mindswap (htp://www.mindswap.org)
 * </p>
 * 
 * @author Michael Grove
 * @version 1.0
 */

/*
 * want uri of the file this report is for??
 */
public class OWLSValidatorReport {
	private Map<Service, Set<OWLSValidatorMessage>> mMessageMap;

	public OWLSValidatorReport(Map<Service, Set<OWLSValidatorMessage>> theMsgs) {
		mMessageMap = theMsgs;
	}

	public void print(PrintStream theOut) {
		theOut.println("Validation Report");
		if (mMessageMap.isEmpty()) {
			theOut.println("Valid:\ttrue");
			return;
		}

		Iterator<Service> kIter = mMessageMap.keySet().iterator();
		while (kIter.hasNext()) {
			Service key = kIter.next();
			Set<OWLSValidatorMessage> msgSet = mMessageMap.get(key);
			Service aService = key;
			boolean valid = msgSet.isEmpty();

			theOut.println("Service:\t" + aService);
			theOut.println("Valid:\t\t" + valid);
			if (!valid) {
				theOut.println("Validation messages: ");
				Iterator<OWLSValidatorMessage> mIter = msgSet.iterator();
				while (mIter.hasNext()) {
					OWLSValidatorMessage msg = mIter.next();
					theOut.println(msg);
				}
			}
		}
	}

	public Map<Service, Set<OWLSValidatorMessage>> getMessages() {
		return mMessageMap;
	}

}
