/**
 * XMLBeanReader.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.persistance.io.reader;

import java.beans.IntrospectionException;
import java.io.Reader;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.io.BeanReader;
import org.xml.sax.SAXException;

/**
 * General Reader for mapping xml to java beans.<br>
 * <br>
 * This class can not be inherited, so if you need to write an especialized
 * reader make a wrapper, or implement a new reader.
 */

public class XMLBeanReader {
    private BindingConfiguration bconf;

    private XMLBeanReader (){
        bconf = new BindingConfiguration();
        bconf.setMapIDs(false);
    }
    
    /**
     * return an instance of <code>XMLBeanReader</code>
     *
     * @return An instance of <code>XMLBeanReader</code>
     */
    public static XMLBeanReader
    getInstance (){
        return new XMLBeanReader();
    }
    
    /**
     * Read xml file and map to an bean
     * 
     * @param source URI to the xml data to be parsed.
     * @param beanClass Bean class representing the xml to be parsed.
     *
     * @return An instance of the Bean that represents the xml.
     * @throws IOException If an input or output exception occured.
     * @throws IntrospectionException If an exception happens during Introspection.
     * @throws SAXException If an exception occures while parsing xml.
     */
    public Object
    read (String source, Class beanClass)
        throws IntrospectionException,
               IOException,SAXException{
        BeanReader beanReader = prepareBeanReader(beanClass);
        return beanReader.parse(source);
    }
    
    /**
     * Read xml file and map to an bean
     * 
     * @param source xml file that encapsulates the data to be parsed.
     * @param beanClass Bean class representing the xml to be parsed.
     *
     * @return An instance of the Bean that represents the xml.
     * @throws IOException If an input or output exception occured.
     * @throws IntrospectionException If an exception happens during Introspection.
     * @throws SAXException If an exception occures while parsing xml.
     */
    public Object
    read (File source, Class beanClass)
        throws IntrospectionException,
               IOException,SAXException{
        BeanReader beanReader = prepareBeanReader(beanClass);
        return beanReader.parse(source);
    }
    
    /**
     * Read xml file and map to an bean
     * 
     * @param source reader that points the data to be parsed.
     * @param beanClass Bean class representing the xml to be parsed.
     *
     * @return An instance of the Bean that represents the xml.
     * @throws IOException If an input or output exception occured.
     * @throws IntrospectionException If an exception happens during Introspection.
     * @throws SAXException If an exception occures while parsing xml.
     */
    public Object
    read (Reader source, Class beanClass)
        throws IntrospectionException,
               IOException,SAXException{
        BeanReader beanReader = prepareBeanReader(beanClass);
        return beanReader.parse(source);
    }
    
    /**
     * Read xml file and map to an bean
     * 
     * @param source input stream that points the data to be parsed.
     * @param beanClass Bean class representing the xml to be parsed.
     *
     * @return An instance of the Bean that represents the xml.
     * @throws IOException If an input or output exception occured.
     * @throws IntrospectionException If an exception happens during Introspection.
     * @throws SAXException If an exception occures while parsing xml.
     */
    public Object
    read (InputStream source, Class beanClass)
        throws IntrospectionException,
               IOException,SAXException{
        BeanReader beanReader = prepareBeanReader(beanClass);
        return beanReader.parse(source);
    }

    /**
     * Prepare the bean reader to be used ind the parsing.
     * 
     * @param beanClass Bean class representing the xml to be parsed.
     *
     * @return The bean reader ready to be used.
     * @throws IntrospectionException If an exception happens during Introspection.
     */
    private BeanReader
    prepareBeanReader (Class beanClass)
        throws IntrospectionException{
        BeanReader beanReader = new BeanReader();
        beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        beanReader.registerBeanClass(beanClass);
        return beanReader;
    }
}
