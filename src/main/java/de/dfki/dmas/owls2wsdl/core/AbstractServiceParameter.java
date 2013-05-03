/*
 * AbstractServiceParameter.java
 *
 * Created on 18. September 2006, 11:28
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

import java.net.URI;

/**
 *
 * @author Oliver
 */
public class AbstractServiceParameter implements java.io.Serializable {
    
    private String  _id;
    private String  _uri;
    private int     _pos;
    
    
    /** Creates a new instance of AbstractServiceParameter */
    public AbstractServiceParameter() {
    }
    
    public AbstractServiceParameter(String id, String uri, int pos) {
        this();
        this._id  = id;
        this._uri = uri;
        this._pos = pos;
    }
    
    public void setID(String id)   { this._id = id; }
    public void setUri(String uri) { this._uri = uri; }
    public void setPos(int pos)    { this._pos = pos; }
    
    public String getID()  { return this._id; }
    public String getUri() { return this._uri; }
    public int    getPos() { return this._pos; }
    
    /**
     * Used for Schema generation
     */    
    public String getTypeLocal() 
    {
        int index = this._uri.lastIndexOf("#");   
        return this._uri.substring(index+1)+"Type";
    }
    
    public String getTypeNamespace()
    {
        return this._uri.split("#")[0].toString();
    }
    
    /**
     * Checks if type is primitive XSD datatype.
     * http://www.edition-w3c.de/TR/2001/REC-xmlschema-2-20010502/
     */
    public boolean isPrimitiveXsdType() {
        return this.getTypeNamespace().equals("http://www.w3.org/2001/XMLSchema");
    }
    
    /**
     * Checks AbstractDatabase Knowledgebase for type.
     */
    public boolean isInKB() {
        return (AbstractDatatypeKB.getInstance().data.containsKey(this._uri));
    }
    
    /**
     * Checks if Name is a valid NCName.
     * See comment for OntClassContainer.getOntClass(..)
     */
    public boolean isValidNCName() {
        int idx = 0;
        if(this._uri.contains("#")) {
            idx=this._uri.indexOf("#")+1;
        }
        char first = this._uri.charAt(idx);
        char[] checklist = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_' };        
        
        for(int i=0; i<checklist.length; i++) {
            if(first == checklist[i]) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isValid() {        
        try {       
            if(this.isInKB()) {
                return this.isValidNCName();
            }
        }
        catch(Exception e) {
            System.out.println("Param: "+this.getUri());
            System.out.println("Exception testing for valid NCName");
        }
        return false;
    }
    
    public String toString() {
        return "("+this._pos+")"+this._id+"("+this._uri+")";
    }    
}
