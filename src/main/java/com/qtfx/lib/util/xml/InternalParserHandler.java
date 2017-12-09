/*
 * Copyright (C) 2015 Miquel Sas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.qtfx.lib.util.xml;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Internal SAX parser handler that is installed in the parser to handle events and dispatch them to the more convenient
 * <i>ParserHandler</i> class.
 * 
 * @author Miquel Sas
 */
public class InternalParserHandler extends DefaultHandler {

	/**
	 * The <i>ParserHandler</i> used to dispatch events.
	 */
	private ParserHandler handler;
	/**
	 * A stack to manage element names, allowing to know the current element being processed and also get a complete
	 * path to it.
	 */
	private Deque<String> elementStack = new ArrayDeque<>();

	/**
	 * Default constructor.
	 * 
	 * @param handler The handler.
	 */
	public InternalParserHandler(ParserHandler handler) {
		super();
		this.handler = handler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startDocument() throws SAXException {
		handler.documentStart();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endDocument() throws SAXException {
		handler.documentEnd();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		String element = qName;
		elementStack.push(element);
		handler.elementStart(getElementPrefix(element), getElementName(element), getCurrentPath(), attributes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String element = qName;
		handler.elementEnd(getElementPrefix(element), getElementName(element), getCurrentPath());
		elementStack.pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String element = elementStack.peek();
		handler.elementBody(
			getElementPrefix(element),
			getElementName(element),
			getCurrentPath(),
			getText(ch, start, length));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warning(SAXParseException e) throws SAXException {
		handler.warning(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(SAXParseException e) throws SAXException {
		handler.error(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		handler.fatalError(e);
	}

	/**
	 * Returns the prefix (namespace) of an element.
	 * 
	 * @param element The XML element.
	 * @return The namespace or prefix part.
	 */
	private String getElementPrefix(String element) {
		int index = element.indexOf(":");
		if (index < 0) {
			return "";
		}
		return element.substring(0, index);
	}

	/**
	 * Returns the element name.
	 * 
	 * @param element The XML element.
	 * @return The name part.
	 */
	private String getElementName(String element) {
		int index = element.indexOf(":");
		if (index < 0) {
			return element;
		}
		return element.substring(index + 1);
	}

	/**
	 * Returns the current path at a given moment in the parsing process.
	 * 
	 * @return The path.
	 */
	private String getCurrentPath() {
		StringBuilder path = new StringBuilder();
		boolean first = true;
		Iterator<String> i = elementStack.descendingIterator();
		while (i.hasNext()) {
			if (first) {
				first = false;
			} else {
				path.append("/");
			}
			String element = i.next();
			path.append(element);
		}
		return path.toString();
	}

	/**
	 * Returns the array part as a string.
	 * 
	 * @param ch The array of characters.
	 * @param start The start index.
	 * @param length The length of the string.
	 * @return The array part as a string.
	 */
	private String getText(char[] ch, int start, int length) {
		StringBuilder text = new StringBuilder();
		for (int i = start; i < start + length; i++) {
			text.append(ch[i]);
		}
		return text.toString();
	}

}
