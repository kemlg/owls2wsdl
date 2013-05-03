/*
 * AbstractDatatypeElementComparer.java
 *
 * Created on 16. M�rz 2007, 18:33
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

import java.util.Comparator;

/**
 *
 * @author Oliver Fourman
 */
public class AbstractDatatypeElementComparer implements Comparator 
{    
        public int compare(Object obj1, Object obj2)
        {
                String s1 = ((AbstractDatatypeElement)obj1).getName();
                String s2 = ((AbstractDatatypeElement)obj2).getName();
                return s1.compareTo(s2);
        }
}

