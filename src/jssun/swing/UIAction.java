/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package jssun.swing;

import jsjava.beans.PropertyChangeListener;

import jsjavax.swing.Action;

/**
 * UIAction is the basis of all of basic's action classes that are used in
 * an ActionMap. Subclasses need to override <code>actionPerformed</code>.
 * <p>
 * A typical subclass will look like:
 * <pre>
 *    private static class Actions extends UIAction {
 *        Actions(String name) {
 *            super(name);
 *        }
 *
 *        public void actionPerformed(ActionEvent ae) {
 *            if (getName() == "selectAll") {
 *                selectAll();
 *            }
 *            else if (getName() == "cancelEditing") {
 *                cancelEditing();
 *            }
 *        }
 *    }
 * </pre>
 * <p>
 * Subclasses that wish to conditionalize the enabled state should override
 * <code>isEnabled(Component)</code>, and be aware that the passed in
 * <code>Component</code> may be null.
 *
 * @see com.sun.java.swing.ExtendedAction
 * @see jsjavax.swing.Action
 * @author Scott Violet
 */
public abstract class UIAction implements Action {
    private String name;

    public UIAction(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    @Override
		public Object getValue(String key) {
        if (key == NAME) {
            return name;
        }
        return null;
    }

    // UIAction is not mutable, this does nothing.
    @Override
		public void putValue(String key, Object value) {
    }

    // UIAction is not mutable, this does nothing.
    @Override
		public void setEnabled(boolean b) {
    }

    /**
     * Cover method for <code>isEnabled(null)</code>.
     */
    @Override
		public final boolean isEnabled() {
        return isEnabled(null);
    }

    /**
     * Subclasses that need to conditionalize the enabled state should
     * override this. Be aware that <code>sender</code> may be null.
     *
     * @param sender Widget enabled state is being asked for, may be null.
     */
    public boolean isEnabled(Object sender) {
        return true;
    }

    // UIAction is not mutable, this does nothing.
    @Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    // UIAction is not mutable, this does nothing.
    @Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
    }
}
