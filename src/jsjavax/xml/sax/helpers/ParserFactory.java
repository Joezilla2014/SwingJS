// SAX parser factory.
// http://www.saxproject.org
// No warranty; no copyright -- use this as you will.
// $Id: ParserFactory.java,v 1.7 2002/01/30 20:52:36 dbrownell Exp $

package jsjavax.xml.sax.helpers;

import jsjavax.xml.sax.Parser;


/**
 * Java-specific class for dynamically loading SAX parsers.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * See <a href='http://www.saxproject.org'>http://www.saxproject.org</a>
 * for further information.
 * </blockquote>
 *
 * <p><strong>Note:</strong> This class is designed to work with the now-deprecated
 * SAX1 {@link jsjavax.xml.sax.Parser Parser} class.  SAX2 applications should use
 * {@link jsjavax.xml.sax.helpers.XMLReaderFactory XMLReaderFactory} instead.</p>
 *
 * <p>ParserFactory is not part of the platform-independent definition
 * of SAX; it is an additional convenience class designed
 * specifically for Java XML application writers.  SAX applications
 * can use the static methods in this class to allocate a SAX parser
 * dynamically at run-time based either on the value of the
 * `jsjavax.xml.sax.parser' system property or on a string containing the class
 * name.</p>
 *
 * <p>Note that the application still requires an XML parser that
 * implements SAX1.</p>
 *
 * @deprecated This class works with the deprecated
 *             {@link jsjavax.xml.sax.Parser Parser}
 *             interface.
 * @since SAX 1.0
 * @author David Megginson
 * @version 2.0.1 (sax2r2)
 */
public class ParserFactory {
    
    
    /**
     * Private null constructor.
     */
    private ParserFactory ()
    {
    }
    
    
    /**
     * Create a new SAX parser using the `jsjavax.xml.sax.parser' system property.
     *
     * <p>The named class must exist and must implement the
     * {@link jsjavax.xml.sax.Parser Parser} interface.</p>
     *
     * @exception java.lang.NullPointerException There is no value
     *            for the `jsjavax.xml.sax.parser' system property.
     * @exception java.lang.ClassNotFoundException The SAX parser
     *            class was not found (check your CLASSPATH).
     * @exception IllegalAccessException The SAX parser class was
     *            found, but you do not have permission to load
     *            it.
     * @exception InstantiationException The SAX parser class was
     *            found but could not be instantiated.
     * @exception java.lang.ClassCastException The SAX parser class
     *            was found and instantiated, but does not implement
     *            jsjavax.xml.sax.Parser.
     * @see #makeParser(java.lang.String)
     * @see jsjavax.xml.sax.Parser
     */
    public static Parser makeParser ()
	throws ClassNotFoundException,
	IllegalAccessException, 
	InstantiationException,
	NullPointerException,
	ClassCastException
    {
	String className = System.getProperty("jsjavax.xml.sax.parser");
	if (className == null) {
	    throw new NullPointerException("No value for sax.parser property");
	} else {
	    return makeParser(className);
	}
    }
    
    
    /**
     * Create a new SAX parser object using the class name provided.
     *
     * <p>The named class must exist and must implement the
     * {@link jsjavax.xml.sax.Parser Parser} interface.</p>
     *
     * @param className A string containing the name of the
     *                  SAX parser class.
     * @exception java.lang.ClassNotFoundException The SAX parser
     *            class was not found (check your CLASSPATH).
     * @exception IllegalAccessException The SAX parser class was
     *            found, but you do not have permission to load
     *            it.
     * @exception InstantiationException The SAX parser class was
     *            found but could not be instantiated.
     * @exception java.lang.ClassCastException The SAX parser class
     *            was found and instantiated, but does not implement
     *            jsjavax.xml.sax.Parser.
     * @see #makeParser()
     * @see jsjavax.xml.sax.Parser
     */
    public static Parser makeParser (String className)
	throws ClassNotFoundException,
	IllegalAccessException, 
	InstantiationException,
	ClassCastException
    {
	return (Parser) NewInstance.newInstance (
		NewInstance.getClassLoader (), className);
    }
    
}

