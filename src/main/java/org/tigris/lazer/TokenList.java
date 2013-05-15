/*
 * TokenList.java
 *
 * Created on 25 August 2003, 15:53
 */

package org.tigris.lazer;

import java.util.ArrayList;

/**
 * 
 * @author Bob Tarling
 */
public class TokenList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6961353681447771550L;

	public void removeRange(int start, int end) {
		super.removeRange(start, end);
	}
}
