// XMLFilterImpl.java - base SAX2 filter implementation.
// http://www.saxproject.org
// Written by David Megginson
// NO WARRANTY!  This class is in the Public Domain.
// $Id: XMLFilterImpl.java,v 1.9 2004/04/26 17:34:35 dmegginson Exp $

package jsjavax.xml.sax.helpers;

import java.io.IOException;

import jsjavax.xml.sax.XMLReader;
import jsjavax.xml.sax.XMLFilter;
import jsjavax.xml.sax.InputSource;
import jsjavax.xml.sax.Locator;
import jsjavax.xml.sax.Attributes;
import jsjavax.xml.sax.EntityResolver;
import jsjavax.xml.sax.DTDHandler;
import jsjavax.xml.sax.ContentHandler;
import jsjavax.xml.sax.ErrorHandler;
import jsjavax.xml.sax.SAXException;
import jsjavax.xml.sax.SAXParseException;
import jsjavax.xml.sax.SAXNotSupportedException;
import jsjavax.xml.sax.SAXNotRecognizedException;


/**
 * Base class for deriving an XML filter.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * See <a href='http://www.saxproject.org'>http://www.saxproject.org</a>
 * for further information.
 * </blockquote>
 *
 * <p>This class is designed to sit between an {@link jsjavax.xml.sax.XMLReader
 * XMLReader} and the client application's event handlers.  By default, it
 * does nothing but pass requests up to the reader and events
 * on to the handlers unmodified, but subclasses can override
 * specific methods to modify the event stream or the configuration
 * requests as they pass through.</p>
 *
 * @since SAX 2.0
 * @author David Megginson
 * @version 2.0.1 (sax2r2)
 * @see jsjavax.xml.sax.XMLFilter
 * @see jsjavax.xml.sax.XMLReader
 * @see jsjavax.xml.sax.EntityResolver
 * @see jsjavax.xml.sax.DTDHandler
 * @see jsjavax.xml.sax.ContentHandler
 * @see jsjavax.xml.sax.ErrorHandler
 */
public class XMLFilterImpl
    implements XMLFilter, EntityResolver, DTDHandler, ContentHandler, ErrorHandler
{


    ////////////////////////////////////////////////////////////////////
    // Constructors.
    ////////////////////////////////////////////////////////////////////


    /**
     * Construct an empty XML filter, with no parent.
     *
     * <p>This filter will have no parent: you must assign a parent
     * before you start a parse or do any configuration with
     * setFeature or setProperty, unless you use this as a pure event
     * consumer rather than as an {@link XMLReader}.</p>
     *
     * @see jsjavax.xml.sax.XMLReader#setFeature
     * @see jsjavax.xml.sax.XMLReader#setProperty
     * @see #setParent
     */
    public XMLFilterImpl ()
    {
	super();
    }


    /**
     * Construct an XML filter with the specified parent.
     *
     * @see #setParent
     * @see #getParent
     */
    public XMLFilterImpl (XMLReader parent)
    {
        super();
	setParent(parent);
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of jsjavax.xml.sax.XMLFilter.
    ////////////////////////////////////////////////////////////////////


    /**
     * Set the parent reader.
     *
     * <p>This is the {@link jsjavax.xml.sax.XMLReader XMLReader} from which 
     * this filter will obtain its events and to which it will pass its 
     * configuration requests.  The parent may itself be another filter.</p>
     *
     * <p>If there is no parent reader set, any attempt to parse
     * or to set or get a feature or property will fail.</p>
     *
     * @param parent The parent XML reader.
     * @see #getParent
     */
    @Override
		public void setParent (XMLReader parent)
    {
	this.parent = parent;
    }


    /**
     * Get the parent reader.
     *
     * @return The parent XML reader, or null if none is set.
     * @see #setParent
     */
    @Override
		public XMLReader getParent ()
    {
	return parent;
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of jsjavax.xml.sax.XMLReader.
    ////////////////////////////////////////////////////////////////////


    /**
     * Set the value of a feature.
     *
     * <p>This will always fail if the parent is null.</p>
     *
     * @param name The feature name.
     * @param value The requested feature value.
     * @exception jsjavax.xml.sax.SAXNotRecognizedException If the feature
     *            value can't be assigned or retrieved from the parent.
     * @exception jsjavax.xml.sax.SAXNotSupportedException When the
     *            parent recognizes the feature name but 
     *            cannot set the requested value.
     */
    @Override
		public void setFeature (String name, boolean value)
	throws SAXNotRecognizedException, SAXNotSupportedException
    {
	if (parent != null) {
	    parent.setFeature(name, value);
	} else {
	    throw new SAXNotRecognizedException("Feature: " + name);
	}
    }


    /**
     * Look up the value of a feature.
     *
     * <p>This will always fail if the parent is null.</p>
     *
     * @param name The feature name.
     * @return The current value of the feature.
     * @exception jsjavax.xml.sax.SAXNotRecognizedException If the feature
     *            value can't be assigned or retrieved from the parent.
     * @exception jsjavax.xml.sax.SAXNotSupportedException When the
     *            parent recognizes the feature name but 
     *            cannot determine its value at this time.
     */
    @Override
		public boolean getFeature (String name)
	throws SAXNotRecognizedException, SAXNotSupportedException
    {
	if (parent != null) {
	    return parent.getFeature(name);
	} else {
	    throw new SAXNotRecognizedException("Feature: " + name);
	}
    }


    /**
     * Set the value of a property.
     *
     * <p>This will always fail if the parent is null.</p>
     *
     * @param name The property name.
     * @param value The requested property value.
     * @exception jsjavax.xml.sax.SAXNotRecognizedException If the property
     *            value can't be assigned or retrieved from the parent.
     * @exception jsjavax.xml.sax.SAXNotSupportedException When the
     *            parent recognizes the property name but 
     *            cannot set the requested value.
     */
    @Override
		public void setProperty (String name, Object value)
	throws SAXNotRecognizedException, SAXNotSupportedException
    {
	if (parent != null) {
	    parent.setProperty(name, value);
	} else {
	    throw new SAXNotRecognizedException("Property: " + name);
	}
    }


    /**
     * Look up the value of a property.
     *
     * @param name The property name.
     * @return The current value of the property.
     * @exception jsjavax.xml.sax.SAXNotRecognizedException If the property
     *            value can't be assigned or retrieved from the parent.
     * @exception jsjavax.xml.sax.SAXNotSupportedException When the
     *            parent recognizes the property name but 
     *            cannot determine its value at this time.
     */
    @Override
		public Object getProperty (String name)
	throws SAXNotRecognizedException, SAXNotSupportedException
    {
	if (parent != null) {
	    return parent.getProperty(name);
	} else {
	    throw new SAXNotRecognizedException("Property: " + name);
	}
    }


    /**
     * Set the entity resolver.
     *
     * @param resolver The new entity resolver.
     */
    @Override
		public void setEntityResolver (EntityResolver resolver)
    {
	entityResolver = resolver;
    }


    /**
     * Get the current entity resolver.
     *
     * @return The current entity resolver, or null if none was set.
     */
    @Override
		public EntityResolver getEntityResolver ()
    {
	return entityResolver;
    }


    /**
     * Set the DTD event handler.
     *
     * @param handler the new DTD handler
     */
    @Override
		public void setDTDHandler (DTDHandler handler)
    {
	dtdHandler = handler;
    }


    /**
     * Get the current DTD event handler.
     *
     * @return The current DTD handler, or null if none was set.
     */
    @Override
		public DTDHandler getDTDHandler ()
    {
	return dtdHandler;
    }


    /**
     * Set the content event handler.
     *
     * @param handler the new content handler
     */
    @Override
		public void setContentHandler (ContentHandler handler)
    {
	contentHandler = handler;
    }


    /**
     * Get the content event handler.
     *
     * @return The current content handler, or null if none was set.
     */
    @Override
		public ContentHandler getContentHandler ()
    {
	return contentHandler;
    }


    /**
     * Set the error event handler.
     *
     * @param handler the new error handler
     */
    @Override
		public void setErrorHandler (ErrorHandler handler)
    {
	errorHandler = handler;
    }


    /**
     * Get the current error event handler.
     *
     * @return The current error handler, or null if none was set.
     */
    @Override
		public ErrorHandler getErrorHandler ()
    {
	return errorHandler;
    }


    /**
     * Parse a document.
     *
     * @param input The input source for the document entity.
     * @exception jsjavax.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     */
    @Override
		public void parse (InputSource input)
	throws SAXException, IOException
    {
	setupParse();
	parent.parse(input);
    }


    /**
     * Parse a document.
     *
     * @param systemId The system identifier as a fully-qualified URI.
     * @exception jsjavax.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     */
    @Override
		public void parse (String systemId)
	throws SAXException, IOException
    {
	parse(new InputSource(systemId));
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of jsjavax.xml.sax.EntityResolver.
    ////////////////////////////////////////////////////////////////////


    /**
     * Filter an external entity resolution.
     *
     * @param publicId The entity's public identifier, or null.
     * @param systemId The entity's system identifier.
     * @return A new InputSource or null for the default.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     * @exception java.io.IOException The client may throw an
     *            I/O-related exception while obtaining the
     *            new InputSource.
     */
    @Override
		public InputSource resolveEntity (String publicId, String systemId)
	throws SAXException, IOException
    {
	if (entityResolver != null) {
	    return entityResolver.resolveEntity(publicId, systemId);
	} else {
	    return null;
	}
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of jsjavax.xml.sax.DTDHandler.
    ////////////////////////////////////////////////////////////////////

    
    /**
     * Filter a notation declaration event.
     *
     * @param name The notation name.
     * @param publicId The notation's public identifier, or null.
     * @param systemId The notation's system identifier, or null.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void notationDecl (String name, String publicId, String systemId)
	throws SAXException
    {
	if (dtdHandler != null) {
	    dtdHandler.notationDecl(name, publicId, systemId);
	}
    }

    
    /**
     * Filter an unparsed entity declaration event.
     *
     * @param name The entity name.
     * @param publicId The entity's public identifier, or null.
     * @param systemId The entity's system identifier, or null.
     * @param notationName The name of the associated notation.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void unparsedEntityDecl (String name, String publicId,
				    String systemId, String notationName)
	throws SAXException
    {
	if (dtdHandler != null) {
	    dtdHandler.unparsedEntityDecl(name, publicId, systemId,
					  notationName);
	}
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of jsjavax.xml.sax.ContentHandler.
    ////////////////////////////////////////////////////////////////////


    /**
     * Filter a new document locator event.
     *
     * @param locator The document locator.
     */
    @Override
		public void setDocumentLocator (Locator locator)
    {
	this.locator = locator;
	if (contentHandler != null) {
	    contentHandler.setDocumentLocator(locator);
	}
    }


    /**
     * Filter a start document event.
     *
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void startDocument ()
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.startDocument();
	}
    }


    /**
     * Filter an end document event.
     *
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void endDocument ()
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.endDocument();
	}
    }


    /**
     * Filter a start Namespace prefix mapping event.
     *
     * @param prefix The Namespace prefix.
     * @param uri The Namespace URI.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void startPrefixMapping (String prefix, String uri)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.startPrefixMapping(prefix, uri);
	}
    }


    /**
     * Filter an end Namespace prefix mapping event.
     *
     * @param prefix The Namespace prefix.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void endPrefixMapping (String prefix)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.endPrefixMapping(prefix);
	}
    }


    /**
     * Filter a start element event.
     *
     * @param uri The element's Namespace URI, or the empty string.
     * @param localName The element's local name, or the empty string.
     * @param qName The element's qualified (prefixed) name, or the empty
     *        string.
     * @param atts The element's attributes.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void startElement (String uri, String localName, String qName,
			      Attributes atts)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.startElement(uri, localName, qName, atts);
	}
    }


    /**
     * Filter an end element event.
     *
     * @param uri The element's Namespace URI, or the empty string.
     * @param localName The element's local name, or the empty string.
     * @param qName The element's qualified (prefixed) name, or the empty
     *        string.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.endElement(uri, localName, qName);
	}
    }


    /**
     * Filter a character data event.
     *
     * @param ch An array of characters.
     * @param start The starting position in the array.
     * @param length The number of characters to use from the array.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void characters (char ch[], int start, int length)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.characters(ch, start, length);
	}
    }


    /**
     * Filter an ignorable whitespace event.
     *
     * @param ch An array of characters.
     * @param start The starting position in the array.
     * @param length The number of characters to use from the array.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void ignorableWhitespace (char ch[], int start, int length)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.ignorableWhitespace(ch, start, length);
	}
    }


    /**
     * Filter a processing instruction event.
     *
     * @param target The processing instruction target.
     * @param data The text following the target.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void processingInstruction (String target, String data)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.processingInstruction(target, data);
	}
    }


    /**
     * Filter a skipped entity event.
     *
     * @param name The name of the skipped entity.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void skippedEntity (String name)
	throws SAXException
    {
	if (contentHandler != null) {
	    contentHandler.skippedEntity(name);
	}
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of jsjavax.xml.sax.ErrorHandler.
    ////////////////////////////////////////////////////////////////////


    /**
     * Filter a warning event.
     *
     * @param e The warning as an exception.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void warning (SAXParseException e)
	throws SAXException
    {
	if (errorHandler != null) {
	    errorHandler.warning(e);
	}
    }


    /**
     * Filter an error event.
     *
     * @param e The error as an exception.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void error (SAXParseException e)
	throws SAXException
    {
	if (errorHandler != null) {
	    errorHandler.error(e);
	}
    }


    /**
     * Filter a fatal error event.
     *
     * @param e The error as an exception.
     * @exception jsjavax.xml.sax.SAXException The client may throw
     *            an exception during processing.
     */
    @Override
		public void fatalError (SAXParseException e)
	throws SAXException
    {
	if (errorHandler != null) {
	    errorHandler.fatalError(e);
	}
    }



    ////////////////////////////////////////////////////////////////////
    // Internal methods.
    ////////////////////////////////////////////////////////////////////


    /**
     * Set up before a parse.
     *
     * <p>Before every parse, check whether the parent is
     * non-null, and re-register the filter for all of the 
     * events.</p>
     */
    private void setupParse ()
    {
	if (parent == null) {
	    throw new NullPointerException("No parent for filter");
	}
	parent.setEntityResolver(this);
	parent.setDTDHandler(this);
	parent.setContentHandler(this);
	parent.setErrorHandler(this);
    }



    ////////////////////////////////////////////////////////////////////
    // Internal state.
    ////////////////////////////////////////////////////////////////////

    private XMLReader parent = null;
    private Locator locator = null;
    private EntityResolver entityResolver = null;
    private DTDHandler dtdHandler = null;
    private ContentHandler contentHandler = null;
    private ErrorHandler errorHandler = null;

}

// end of XMLFilterImpl.java
