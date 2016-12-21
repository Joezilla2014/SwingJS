/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1997, 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package swingjs.plaf;

import jsjava.awt.Dimension;
import jsjava.beans.PropertyChangeListener;
import jsjavax.swing.JViewport;
import jsjavax.swing.event.ChangeListener;
import swingjs.api.DOMNode;

public class JSViewportUI extends JSLightweightUI implements PropertyChangeListener,
ChangeListener {

	JViewport viewport;

	@Override
	protected DOMNode updateDOMNode() {
		if (domNode == null) {
			domNode = newDOMObject("div", id);
		}
		// add code here for adjustments when changes in bounds or other properties occur.
		return domNode;
	}

	@Override
	protected void installUIImpl() {
		viewport = (JViewport) c;
	}

	@Override
	protected void uninstallUIImpl() {
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (debugging) 
			System.out.println(id + " getPreferredSize");
  	return null;
  }


	@Override
	protected DOMNode setHTMLElement() {
		return DOMNode.setStyles(setHTMLElementCUI(), "overflow", "hidden");
	}

}
