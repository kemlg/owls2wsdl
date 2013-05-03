/*
 * TagTokenFactory.java
 *
 * Created on 15 September 2003, 23:45
 */

package org.tigris.lazer.xml;

import org.tigris.lazer.AbstractTokenFactory;
import org.tigris.lazer.Token;

/**
 *
 * @author Bob Tarling
 */
public class XmlTagTokenFactory extends AbstractTokenFactory {
    
    private static String braces[] = {"<", ">"};
    
    /** Creates a new instance of BlockCommentTokenFactory */
    public XmlTagTokenFactory() {
    }
    
    public boolean isApplicable(String text, int start) {
        return regionMatches(text, start, braces[0]);
        //return text.startsWith(braces[0]);
    }
    
    public Token createToken(String text, int start) {
        int end = indexOf(text, braces[1], start+1, 1);
        return createToken(text, start, end, XmlTagTokenType.getInstance());
    }
}
