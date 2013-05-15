/*
 * AbstractServiceCollection.java
 *
 * Created on 11. September 2006, 13:55
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

import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import java.io.OutputStream;
import java.io.File;

/**
 *
 * @author Oliver Fourman
 */
public class AbstractServiceCollection implements java.io.Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6035758993387299717L;
	
	private Vector<AbstractService> _serviceCollection;
    private int    					_count10;
    private int    					_count11;
        
    /** Creates a new instance of AbstractServiceCollection */
    public AbstractServiceCollection() {
        this._serviceCollection = new Vector<AbstractService>();
    }
    
    public Vector<AbstractService> getServiceCollection() {
        return this._serviceCollection;        
    }
    public int getCount10() { return this._count10; }
    public int getCount11() { return this._count11; }    
    
    public void setServiceCollection(Vector<AbstractService> collection) {
        this._serviceCollection = collection;
    }
    public void setCount10(int count) { this._count10 = count; }
    public void setCount11(int count) { this._count11 = count; }
    
    public void addAbstractService(AbstractService service) {
//        boolean NEW_SERVICE = true;
//        for(Iterator<AbstractService> it=this._serviceCollection.iterator(); it.hasNext(); ) {
//        	AbstractService oldService = it.next();
//        	System.out.println("Old service base: " + oldService.getBase());
//        	System.out.println("New service base: " + service.getBase());
//        	
//            if(service.getBase().equals( oldService.getBase() )) {
//                NEW_SERVICE = false;
//            }
//        }
//        if(NEW_SERVICE) {
            if(service.getVersion().equals("1.0"))
                this._count10++;
            else if(service.getVersion().equals("1.1"))
                this._count11++;
            this._serviceCollection.add(service);
//        }
    }
    
    public AbstractService getAbstractService(int pos) {
        if(pos>=0 && pos < this._serviceCollection.size())
            return (AbstractService)this._serviceCollection.get(pos);
        return null;
    }
    
    public AbstractService getAbstractService(String id) {
        for(int i=0; i<this._serviceCollection.size(); i++) {
            if( ((AbstractService)this._serviceCollection.get(i)).getID().equals(id))
                return (AbstractService)this._serviceCollection.get(i);
        }
        return null;
    }
       
    public void addAbstractServiceList(Vector<AbstractService> listofServices) {
        for(Iterator<AbstractService> it=listofServices.iterator(); it.hasNext(); ) {
        	AbstractService aService = it.next();
        	System.out.println("Adding: " + aService.getName());
            this.addAbstractService(aService);
        }
    }
    
    public int getSize() {
        return this._serviceCollection.size();
    }
    
    public void sortData() {        
        Collections.sort(this._serviceCollection, new AbstractServiceComparer() );        
    }
    
    public void removeAllElements() {
        this._serviceCollection.removeAllElements();
        this._count10 = 0;
        this._count11 = 0;
    }
    
    public boolean removeAbstractService(AbstractService service) {
        int idx = this._serviceCollection.indexOf(service);        
        System.out.println("[i] removeAbstractService ("+idx+"): "+this._serviceCollection.get(idx)+" ("+System.identityHashCode(this._serviceCollection.get(idx))+")");
        if(service.getVersion().equals("1.0")) {
            this._count10--;
        }
        else if(service.getVersion().equals("1.1")) {
            this._count11--;
        }
        return this._serviceCollection.removeElement(service);
    }
    
    public void mergeAbstractServiceCollection(AbstractServiceCollection serviceCollection) {
        this._serviceCollection.addAll(serviceCollection.getServiceCollection());
        this._count10 += serviceCollection.getCount10();
        this._count11 += serviceCollection.getCount11();
    }
    
    public Vector<AbstractService> getParameterTypes() {
        Vector datatypeList = new Vector();
        for(Iterator<AbstractService> it = this._serviceCollection.iterator(); it.hasNext(); ) {
            AbstractService aService = (AbstractService)it.next();
            for(Iterator paramIt = aService.getInputParameter().iterator(); paramIt.hasNext(); ) {
                AbstractServiceParameter param = (AbstractServiceParameter)paramIt.next();
                if(!datatypeList.contains(param.getUri())) {
                    datatypeList.add(param.getUri());
                }
            }
            for(Iterator paramIt = aService.getOutputParameter().iterator(); paramIt.hasNext(); ) {
                AbstractServiceParameter param = (AbstractServiceParameter)paramIt.next();
                if(!datatypeList.contains(param.getUri())) {
                    datatypeList.add(param.getUri());
                }
            }
        }
        return datatypeList;
    }
    
    public void printData() {
        for(Iterator<AbstractService> it = this._serviceCollection.iterator(); it.hasNext(); ) {
            System.out.println( it.next().toString() );
        }        
    }
    
    public void printFullData() {
        for(Iterator<AbstractService> it = this._serviceCollection.iterator(); it.hasNext(); ) {
            it.next().printInfo();
        }
    }
    
    /**
     * Counts the number of translateable services in collection.
     * This number is dynamic because each change of the AbstractDatatype 
     * knowledgebase could change the ability to translate a service.
     * @return array with both values 1.0 services and 1.1 services
     */
    public int[] getTranslateableCount() {
        int[] c = {0,0};
        for(Iterator<AbstractService> it=this._serviceCollection.iterator(); it.hasNext(); ) {
            AbstractService aService = it.next();
            
            if( aService.getVersion().equals("1.0") && aService.istranslatable() ) {
                c[0]++;
            }
            else if( aService.getVersion().equals("1.1") && aService.istranslatable() ) {
                c[1]++;
            }            
        }
        return c;
    }
    
    /**
     * Uses the AbstractServiceUtils to find the common root directory of parsed services.
     * Used to generate a relative directory structure when generating WSDL/OWL-S.
     */
    public String getCommonRoot() {        
        return de.dfki.dmas.owls2wsdl.utils.TranslationUtils.findCommonRoot(this._serviceCollection);
    }
    
    /*
     * Wrapper to marshall as XML (Castor)
     */
    public void marshallCollectedServicesAsXml(OutputStream out, boolean prettyprint) {
        //
        // CASTOR XML-MAPPING
        //
        
        //this.collectedServices.getAbstractService(2).printInfo();
        //AbstractServiceMapper.getInstance().mapAbstractService( this.collectedServices.getAbstractService(2));
        AbstractServiceMapper.getInstance().mapAbstractServiceCollection(this, out, prettyprint);        
    }
    
    public void saveCollectedServicesAsXml(String path) {
        AbstractServiceMapper.getInstance().saveAbstractServiceCollection(this, path);
    }
    
    
    public void importServiceCollection(File f) {
        this.mergeAbstractServiceCollection(AbstractServiceMapper.getInstance().loadAbstractServiceCollection(f));
    }
}

class AbstractServiceComparer implements Comparator<AbstractService> {
        public int compare(AbstractService obj1, AbstractService obj2)
        {
                String s1 = obj1.getName();
                String s2 = obj2.getName();

                return s1.compareTo(s2);
        }
}
