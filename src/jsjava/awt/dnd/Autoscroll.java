/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright 1998-1999 Sun Microsystems, Inc.  All Rights Reserved.
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

package jsjava.awt.dnd;

import jsjava.awt.Insets;
import jsjava.awt.Point;

/**
 * During DnD operations it is possible that a user may wish to drop the
 * subject of the operation on a region of a scrollable GUI control that is
 * not currently visible to the user.
 * <p>
 * In such situations it is desirable that the GUI control detect this
 * and institute a scroll operation in order to make obscured region(s)
 * visible to the user. This feature is known as autoscrolling.
 * <p>
 * If a GUI control is both an active <code>DropTarget</code>
 * and is also scrollable, it
 * can receive notifications of autoscrolling gestures by the user from
 * the DnD system by implementing this interface.
 * <p>
 * An autoscrolling gesture is initiated by the user by keeping the drag
 * cursor motionless with a border region of the <code>Component</code>,
 * referred to as
 * the "autoscrolling region", for a predefined period of time, this will
 * result in repeated scroll requests to the <code>Component</code>
 * until the drag <code>Cursor</code> resumes its motion.
 *
 * @since 1.2
 */

public interface Autoscroll {

    /**
     * This method returns the <code>Insets</code> describing
     * the autoscrolling region or border relative
     * to the geometry of the implementing Component.
     * <P>
     * This value is read once by the <code>DropTarget</code>
     * upon entry of the drag <code>Cursor</code>
     * into the associated <code>Component</code>.
     * <P>
     * @return the Insets
     */

    public Insets getAutoscrollInsets();

    /**
     * notify the <code>Component</code> to autoscroll
     * <P>
     * @param cursorLocn A <code>Point</code> indicating the
     * location of the cursor that triggered this operation.
     */

    public void autoscroll(Point cursorLocn);

}
