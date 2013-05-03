/*
 * HandbookFrame.java
 *
 * Created on 4. Oktober 2007, 19:50
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

package de.dfki.dmas.owls2wsdl.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import java.io.*;

import de.dfki.dmas.owls2wsdl.utils.HandbookEnvBuilder;

/**
 *
 * @author Oliver Fourman
 */
public class HandbookFrame extends javax.swing.JFrame {
    
    /** Creates new form HandbookFrame */
    public HandbookFrame(String handbookUrlString, String path2Icon) {        
        URL imageUrl = OWLS2WSDLGui.class.getClassLoader().getResource(path2Icon);
        ImageIcon owls2wsdlIcon = new ImageIcon(imageUrl);        
        this.setIconImage(owls2wsdlIcon.getImage());
        this.setVisible(false);
        initComponents();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int w=550;
        int h=600;
        this.setSize(w,h);
        this.setLocation((d.width - w)/2,(d.height-h)/2);
        
        try {
            URL url = new URL(handbookUrlString);
            jTextPane1.setPage(url);
        }
        catch(MalformedURLException murle) {
            System.err.println("[HandbookFrame] "+murle.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //this.setAlwaysOnTop(true);
        //this.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Handbook for OWLS2WSDL translation Tool");
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setForeground(java.awt.Color.white);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setViewportView(jTextPane1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HandbookEnvBuilder heb = new HandbookEnvBuilder(
                            "http://owls2wsdl.googlecode.com/svn/trunk/handbook/",
                            "owls2wsdl.gif,owls2wsdl.jpg,owls2wsdl-logo.jpg,owl-mini.jpg");
                    new HandbookFrame(heb.getHandbookIndexURL().toString(), "images/owls2wsdl_icon.gif").setVisible(true);
                }
                catch(MalformedURLException murle) {
                    System.err.println("MalformedURLException: "+murle.getMessage());
                }
                catch(IOException ioe) {
                    System.err.println("IOException: "+ioe.getMessage());
                }
                catch(Exception e) {
                    e.printStackTrace();
                }     
            }//file:///D:/development/OWLS2WSDL/handbook/index.html
        });
    }
    
    private void downloadPages() throws MalformedURLException, IOException {
        URL hb_index = new URL("http://owls2wsdl.googlecode.com/svn/trunk/handbook/index.html");
        BufferedReader in = new BufferedReader( new InputStreamReader(hb_index.openStream()));

	String inputLine;

	while ((inputLine = in.readLine()) != null)
	    System.out.println(inputLine);

	in.close();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
    
}
