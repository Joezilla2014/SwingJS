/*
 * Copyright (c) 1996, 2007, Oracle and/or its affiliates. All rights reserved.
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

package jsjava.awt.event;

import jsjava.awt.Component;
import jsjava.awt.IllegalComponentStateException;
import jsjava.awt.Point;
import jsjava.awt.Toolkit;

/**
 * An event which indicates that a mouse action occurred in a component.
 * A mouse action is considered to occur in a particular component if and only
 * if the mouse cursor is over the unobscured part of the component's bounds
 * when the action happens.
 * For lightweight components, such as Swing's components, mouse events
 * are only dispatched to the component if the mouse event type has been
 * enabled on the component. A mouse event type is enabled by adding the
 * appropriate mouse-based {@code EventListener} to the component
 * ({@link MouseListener} or {@link MouseMotionListener}), or by invoking
 * {@link Component#enableEvents(long)} with the appropriate mask parameter
 * ({@code AWTEvent.MOUSE_EVENT_MASK} or {@code AWTEvent.MOUSE_MOTION_EVENT_MASK}).
 * If the mouse event type has not been enabled on the component, the
 * corresponding mouse events are dispatched to the first ancestor that
 * has enabled the mouse event type.
 *<p>
 * For example, if a {@code MouseListener} has been added to a component, or
 * {@code enableEvents(AWTEvent.MOUSE_EVENT_MASK)} has been invoked, then all
 * the events defined by {@code MouseListener} are dispatched to the component.
 * On the other hand, if a {@code MouseMotionListener} has not been added and
 * {@code enableEvents} has not been invoked with
 * {@code AWTEvent.MOUSE_MOTION_EVENT_MASK}, then mouse motion events are not
 * dispatched to the component. Instead the mouse motion events are
 * dispatched to the first ancestors that has enabled mouse motion
 * events.
 * <P>
 * This low-level event is generated by a component object for:
 * <ul>
 * <li>Mouse Events
 *     <ul>
 *     <li>a mouse button is pressed
 *     <li>a mouse button is released
 *     <li>a mouse button is clicked (pressed and released)
 *     <li>the mouse cursor enters the unobscured part of component's geometry
 *     <li>the mouse cursor exits the unobscured part of component's geometry
 *     </ul>
 * <li> Mouse Motion Events
 *     <ul>
 *     <li>the mouse is moved
 *     <li>the mouse is dragged
 *     </ul>
 * </ul>
 * <P>
 * A <code>MouseEvent</code> object is passed to every
 * <code>MouseListener</code>
 * or <code>MouseAdapter</code> object which is registered to receive
 * the "interesting" mouse events using the component's
 * <code>addMouseListener</code> method.
 * (<code>MouseAdapter</code> objects implement the
 * <code>MouseListener</code> interface.) Each such listener object
 * gets a <code>MouseEvent</code> containing the mouse event.
 * <P>
 * A <code>MouseEvent</code> object is also passed to every
 * <code>MouseMotionListener</code> or
 * <code>MouseMotionAdapter</code> object which is registered to receive
 * mouse motion events using the component's
 * <code>addMouseMotionListener</code>
 * method. (<code>MouseMotionAdapter</code> objects implement the
 * <code>MouseMotionListener</code> interface.) Each such listener object
 * gets a <code>MouseEvent</code> containing the mouse motion event.
 * <P>
 * When a mouse button is clicked, events are generated and sent to the
 * registered <code>MouseListener</code>s.
 * The state of modal keys can be retrieved using {@link InputEvent#getModifiers}
 * and {@link InputEvent#getModifiersEx}.
 * The button mask returned by {@link InputEvent#getModifiers} reflects
 * only the button that changed state, not the current state of all buttons.
 * (Note: Due to overlap in the values of ALT_MASK/BUTTON2_MASK and
 * META_MASK/BUTTON3_MASK, this is not always true for mouse events involving
 * modifier keys).
 * To get the state of all buttons and modifier keys, use
 * {@link InputEvent#getModifiersEx}.
 * The button which has changed state is returned by {@link MouseEvent#getButton}
 * <P>
 * For example, if the first mouse button is pressed, events are sent in the
 * following order:
 * <PRE>
 *    <b   >id           </b   >   <b   >modifiers   </b   > <b   >button </b   >
 *    <code>MOUSE_PRESSED</code>:  <code>BUTTON1_MASK</code> <code>BUTTON1</code>
 *    <code>MOUSE_RELEASED</code>: <code>BUTTON1_MASK</code> <code>BUTTON1</code>
 *    <code>MOUSE_CLICKED</code>:  <code>BUTTON1_MASK</code> <code>BUTTON1</code>
 * </PRE>
 * When multiple mouse buttons are pressed, each press, release, and click
 * results in a separate event.
 * <P>
 * For example, if the user presses <b>button 1</b> followed by
 * <b>button 2</b>, and then releases them in the same order,
 * the following sequence of events is generated:
 * <PRE>
 *    <b   >id           </b   >   <b   >modifiers   </b   > <b   >button </b   >
 *    <code>MOUSE_PRESSED</code>:  <code>BUTTON1_MASK</code> <code>BUTTON1</code>
 *    <code>MOUSE_PRESSED</code>:  <code>BUTTON2_MASK</code> <code>BUTTON2</code>
 *    <code>MOUSE_RELEASED</code>: <code>BUTTON1_MASK</code> <code>BUTTON1</code>
 *    <code>MOUSE_CLICKED</code>:  <code>BUTTON1_MASK</code> <code>BUTTON1</code>
 *    <code>MOUSE_RELEASED</code>: <code>BUTTON2_MASK</code> <code>BUTTON2</code>
 *    <code>MOUSE_CLICKED</code>:  <code>BUTTON2_MASK</code> <code>BUTTON2</code>
 * </PRE>
 * If <b>button 2</b> is released first, the
 * <code>MOUSE_RELEASED</code>/<code>MOUSE_CLICKED</code> pair
 * for <code>BUTTON2_MASK</code> arrives first,
 * followed by the pair for <code>BUTTON1_MASK</code>.
 * <p>
 *
 * <code>MOUSE_DRAGGED</code> events are delivered to the <code>Component</code>
 * in which the mouse button was pressed until the mouse button is released
 * (regardless of whether the mouse position is within the bounds of the
 * <code>Component</code>).  Due to platform-dependent Drag&Drop implementations,
 * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
 * Drag&Drop operation.
 *
 * In a multi-screen environment mouse drag events are delivered to the
 * <code>Component</code> even if the mouse position is outside the bounds of the
 * <code>GraphicsConfiguration</code> associated with that
 * <code>Component</code>. However, the reported position for mouse drag events
 * in this case may differ from the actual mouse position:
 * <ul>
 * <li>In a multi-screen environment without a virtual device:
 * <br>
 * The reported coordinates for mouse drag events are clipped to fit within the
 * bounds of the <code>GraphicsConfiguration</code> associated with
 * the <code>Component</code>.
 * <li>In a multi-screen environment with a virtual device:
 * <br>
 * The reported coordinates for mouse drag events are clipped to fit within the
 * bounds of the virtual device associated with the <code>Component</code>.
 * </ul>
 *
 * @author Carl Quinn
 * @see MouseAdapter
 * @see MouseListener
 * @see MouseMotionAdapter
 * @see MouseMotionListener
 * @see MouseWheelListener
 * @see <a href="http://java.sun.com/docs/books/tutorial/post1.0/ui/mouselistener.html">Tutorial: Writing a Mouse Listener</a>
 * @see <a href="http://java.sun.com/docs/books/tutorial/post1.0/ui/mousemotionlistener.html">Tutorial: Writing a Mouse Motion Listener</a>
 *
 * @since 1.1
 */
public class MouseEvent extends InputEvent {

    /**
     * The first number in the range of ids used for mouse events.
     */
    public static final int MOUSE_FIRST         = 500;

    /**
     * The last number in the range of ids used for mouse events.
     */
    public static final int MOUSE_LAST          = 507;

    /**
     * The "mouse clicked" event. This <code>MouseEvent</code>
     * occurs when a mouse button is pressed and released.
     */
    public static final int MOUSE_CLICKED = MOUSE_FIRST;

    /**
     * The "mouse pressed" event. This <code>MouseEvent</code>
     * occurs when a mouse button is pushed down.
     */
    public static final int MOUSE_PRESSED = 1 + MOUSE_FIRST; //Event.MOUSE_DOWN

    /**
     * The "mouse released" event. This <code>MouseEvent</code>
     * occurs when a mouse button is let up.
     */
    public static final int MOUSE_RELEASED = 2 + MOUSE_FIRST; //Event.MOUSE_UP

    /**
     * The "mouse moved" event. This <code>MouseEvent</code>
     * occurs when the mouse position changes.
     */
    public static final int MOUSE_MOVED = 3 + MOUSE_FIRST; //Event.MOUSE_MOVE

    /**
     * The "mouse entered" event. This <code>MouseEvent</code>
     * occurs when the mouse cursor enters the unobscured part of component's
     * geometry.
     */
    public static final int MOUSE_ENTERED = 4 + MOUSE_FIRST; //Event.MOUSE_ENTER

    /**
     * The "mouse exited" event. This <code>MouseEvent</code>
     * occurs when the mouse cursor exits the unobscured part of component's
     * geometry.
     */
    public static final int MOUSE_EXITED = 5 + MOUSE_FIRST; //Event.MOUSE_EXIT

    /**
     * The "mouse dragged" event. This <code>MouseEvent</code>
     * occurs when the mouse position changes while a mouse button is pressed.
     */
    public static final int MOUSE_DRAGGED = 6 + MOUSE_FIRST; //Event.MOUSE_DRAG

    /**
     * The "mouse wheel" event.  This is the only <code>MouseWheelEvent</code>.
     * It occurs when a mouse equipped with a wheel has its wheel rotated.
     * @since 1.4
     */
    public static final int MOUSE_WHEEL = 7 + MOUSE_FIRST;

    /**
     * Indicates no mouse buttons; used by {@link #getButton}.
     * @since 1.4
     */
    public static final int NOBUTTON = 0;

    /**
     * Indicates mouse button #1; used by {@link #getButton}.
     * @since 1.4
     */
    public static final int BUTTON1 = 1;

    /**
     * Indicates mouse button #2; used by {@link #getButton}.
     * @since 1.4
     */
    public static final int BUTTON2 = 2;

    /**
     * Indicates mouse button #3; used by {@link #getButton}.
     * @since 1.4
     */
    public static final int BUTTON3 = 3;

    /**
     * The mouse event's x coordinate.
     * The x value is relative to the component that fired the event.
     *
     * @serial
     * @see #getX()
     */
    int x;

    /**
     * The mouse event's y coordinate.
     * The y value is relative to the component that fired the event.
     *
     * @serial
     * @see #getY()
     */
    int y;

    /**
     * The mouse event's x absolute coordinate.
     * In a virtual device multi-screen environment in which the
     * desktop area could span multiple physical screen devices,
     * this coordinate is relative to the virtual coordinate system.
     * Otherwise, this coordinate is relative to the coordinate system
     * associated with the Component's GraphicsConfiguration.
     *
     * @serial
   */
    private int xAbs;

    /**
     * The mouse event's y absolute coordinate.
     * In a virtual device multi-screen environment in which the
     * desktop area could span multiple physical screen devices,
     * this coordinate is relative to the virtual coordinate system.
     * Otherwise, this coordinate is relative to the coordinate system
     * associated with the Component's GraphicsConfiguration.
     *
     * @serial
     */
    private int yAbs;

    /**
     * Indicates the number of quick consecutive clicks of
     * a mouse button.
     * clickCount will be valid for only three mouse events :<BR>
     * <code>MOUSE_CLICKED</code>,
     * <code>MOUSE_PRESSED</code> and
     * <code>MOUSE_RELEASED</code>.
     * For the above, the <code>clickCount</code> will be at least 1.
     * For all other events the count will be 0.
     *
     * @serial
     * @see #getClickCount().
     */
    int clickCount;

    /**
     * Indicates which, if any, of the mouse buttons has changed state.
     *
     * The only legal values are the following constants:
     * <code>NOBUTTON</code>,
     * <code>BUTTON1</code>,
     * <code>BUTTON2</code> or
     * <code>BUTTON3</code>.
     * @serial
     * @see #getButton().
     */
    int button;

    /**
     * A property used to indicate whether a Popup Menu
     * should appear  with a certain gestures.
     * If <code>popupTrigger</code> = <code>false</code>,
     * no popup menu should appear.  If it is <code>true</code>
     * then a popup menu should appear.
     *
     * @serial
     * @see jsjava.awt.PopupMenu
     * @see #isPopupTrigger()
     */
    boolean popupTrigger = false;

    /*
     * JDK 1.1 serialVersionUID
     */
    //private static final long serialVersionUID = -991214153494842848L;

//    static {
//        /* ensure that the necessary native libraries are loaded */
//        NativeLibLoader.loadLibraries();
//        if (!GraphicsEnvironment.isHeadless()) {
//            initIDs();
//        }
//    }

    /**
     * Initialize JNI field and method IDs for fields that may be
       accessed from C.
     */
    //private static native void initIDs();

    /**
     * Returns the absolute x, y position of the event.
     * In a virtual device multi-screen environment in which the
     * desktop area could span multiple physical screen devices,
     * these coordinates are relative to the virtual coordinate system.
     * Otherwise, these coordinates are relative to the coordinate system
     * associated with the Component's GraphicsConfiguration.
     *
     * @return a <code>Point</code> object containing the absolute  x
     *  and y coordinates.
     *
     * @see jsjava.awt.GraphicsConfiguration
     * @since 1.6
     */
    public Point getLocationOnScreen(){
      return new Point(xAbs, yAbs);
    }

    /**
     * Returns the absolute horizontal x position of the event.
     * In a virtual device multi-screen environment in which the
     * desktop area could span multiple physical screen devices,
     * this coordinate is relative to the virtual coordinate system.
     * Otherwise, this coordinate is relative to the coordinate system
     * associated with the Component's GraphicsConfiguration.
     *
     * @return x  an integer indicating absolute horizontal position.
     *
     * @see jsjava.awt.GraphicsConfiguration
     * @since 1.6
     */
    public int getXOnScreen() {
        return xAbs;
    }

    /**
     * Returns the absolute vertical y position of the event.
     * In a virtual device multi-screen environment in which the
     * desktop area could span multiple physical screen devices,
     * this coordinate is relative to the virtual coordinate system.
     * Otherwise, this coordinate is relative to the coordinate system
     * associated with the Component's GraphicsConfiguration.
     *
     * @return y  an integer indicating absolute vertical position.
     *
     * @see jsjava.awt.GraphicsConfiguration
     * @since 1.6
     */
    public int getYOnScreen() {
        return yAbs;
    }

    /**
     * Constructs a <code>MouseEvent</code> object with the
     * specified source component,
     * type, modifiers, coordinates, and click count.
     * <p>
     * Note that passing in an invalid <code>id</code> results in
     * unspecified behavior.  Creating an invalid event (such
     * as by using more than one of the old _MASKs, or modifier/button
     * values which don't match) results in unspecified behavior.
     * An invocation of the form
     * <tt>MouseEvent(source, id, when, modifiers, x, y, clickCount, popupTrigger, button)</tt>
     * behaves in exactly the same way as the invocation
     * <tt> {@link #MouseEvent(Component, int, long, int, int, int,
     * int, int, int, boolean, int) MouseEvent}(source, id, when, modifiers,
     * x, y, xAbs, yAbs, clickCount, popupTrigger, button)</tt>
     * where xAbs and yAbs defines as source's location on screen plus
     * relative coordinates x and y.
     * xAbs and yAbs are set to zero if the source is not showing.
     * This method throws an
     * <code>IllegalArgumentException</code> if <code>source</code>
     * is <code>null</code>.
     *
     * @param source       the <code>Component</code> that originated the event
     * @param id           the integer that identifies the event
     * @param when         a long int that gives the time the event occurred
     * @param modifiers    the modifier keys down during event (e.g. shift, ctrl,
     *                     alt, meta)
     *                     Either extended _DOWN_MASK or old _MASK modifiers
     *                     should be used, but both models should not be mixed
     *                     in one event. Use of the extended modifiers is
     *                     preferred.
     * @param x            the horizontal x coordinate for the mouse location
     * @param y            the vertical y coordinate for the mouse location
     * @param clickCount   the number of mouse clicks associated with event
     * @param popupTrigger a boolean, true if this event is a trigger for a
     *                     popup menu
     * @param button       which of the mouse buttons has changed state.
     *                      <code>NOBUTTON</code>,
     *                      <code>BUTTON1</code>,
     *                      <code>BUTTON2</code> or
     *                      <code>BUTTON3</code>.
     * @throws IllegalArgumentException if an invalid <code>button</code>
     *            value is passed in
     * @throws IllegalArgumentException if <code>source</code> is null
     * @since 1.4
     */
    public MouseEvent(Component source, int id, long when, int modifiers,
                      int x, int y, int clickCount, boolean popupTrigger,
                      int button)
    {
        this(source, id, when, modifiers, x, y, 0, 0, clickCount, popupTrigger, button);
        Point eventLocationOnScreen = new Point(0, 0);
        try {
          eventLocationOnScreen = source.getLocationOnScreen();
          this.xAbs = eventLocationOnScreen.x + x;
          this.yAbs = eventLocationOnScreen.y + y;
        } catch (IllegalComponentStateException e){
          this.xAbs = 0;
          this.yAbs = 0;
        }
    }

    /**
     * Constructs a <code>MouseEvent</code> object with the
     * specified source component,
     * type, modifiers, coordinates, and click count.
     * <p>Note that passing in an invalid <code>id</code> results in
     * unspecified behavior.
     * An invocation of the form
     * <tt>MouseEvent(source, id, when, modifiers, x, y, clickCount, popupTrigger)</tt>
     * behaves in exactly the same way as the invocation
     * <tt> {@link #MouseEvent(Component, int, long, int, int, int,
     * int, int, int, boolean, int) MouseEvent}(source, id, when, modifiers,
     * x, y, xAbs, yAbs, clickCount, popupTrigger, MouseEvent.NOBUTTON)</tt>
     * where xAbs and yAbs defines as source's location on screen plus
     * relative coordinates x and y.
     * xAbs and yAbs are set to zero if the source is not showing.
     * This method throws an <code>IllegalArgumentException</code>
     * if <code>source</code> is <code>null</code>.
     *
     * @param source       the <code>Component</code> that originated the event
     * @param id           the integer that identifies the event
     * @param when         a long int that gives the time the event occurred
     * @param modifiers    the modifier keys down during event (e.g. shift, ctrl,
     *                     alt, meta)
     *                     Either extended _DOWN_MASK or old _MASK modifiers
     *                     should be used, but both models should not be mixed
     *                     in one event. Use of the extended modifiers is
     *                     preferred.
     * @param x            the horizontal x coordinate for the mouse location
     * @param y            the vertical y coordinate for the mouse location
     * @param clickCount   the number of mouse clicks associated with event
     * @param popupTrigger a boolean, true if this event is a trigger for a
     *                     popup menu
     * @throws IllegalArgumentException if <code>source</code> is null
     */
     public MouseEvent(Component source, int id, long when, int modifiers,
                      int x, int y, int clickCount, boolean popupTrigger) {
        this(source, id, when, modifiers, x, y, clickCount, popupTrigger, NOBUTTON);
     }


    /**
     * Constructs a <code>MouseEvent</code> object with the
     * specified source component,
     * type, modifiers, coordinates, absolute coordinates, and click count.
     * <p>
     * Note that passing in an invalid <code>id</code> results in
     * unspecified behavior.  Creating an invalid event (such
     * as by using more than one of the old _MASKs, or modifier/button
     * values which don't match) results in unspecified behavior.
     * Even if inconsistent values for relative and absolute coordinates are
     * passed to the constructor, the mouse event instance is still
     * created and no exception is thrown.
     * This method throws an
     * <code>IllegalArgumentException</code> if <code>source</code>
     * is <code>null</code>.
     *
     * @param source       the <code>Component</code> that originated the event
     * @param id           the integer that identifies the event
     * @param when         a long int that gives the time the event occurred
     * @param modifiers    the modifier keys down during event (e.g. shift, ctrl,
     *                     alt, meta)
     *                     Either extended _DOWN_MASK or old _MASK modifiers
     *                     should be used, but both models should not be mixed
     *                     in one event. Use of the extended modifiers is
     *                     preferred.
     * @param x            the horizontal x coordinate for the mouse location
     * @param y            the vertical y coordinate for the mouse location
     * @param xAbs         the absolute horizontal x coordinate for the mouse location
     * @param yAbs         the absolute vertical y coordinate for the mouse location
     * @param clickCount   the number of mouse clicks associated with event
     * @param popupTrigger a boolean, true if this event is a trigger for a
     *                     popup menu
     * @param button       which of the mouse buttons has changed state.
     *                      <code>NOBUTTON</code>,
     *                      <code>BUTTON1</code>,
     *                      <code>BUTTON2</code> or
     *                      <code>BUTTON3</code>.
     * @throws IllegalArgumentException if an invalid <code>button</code>
     *            value is passed in
     * @throws IllegalArgumentException if <code>source</code> is null
     * @since 1.6
     */
    public MouseEvent(Component source, int id, long when, int modifiers,
                      int x, int y, int xAbs, int yAbs,
                      int clickCount, boolean popupTrigger, int button)
    {
        super(source, id, when, modifiers);
        this.x = x;
        this.y = y;
        this.xAbs = xAbs;
        this.yAbs = yAbs;
        this.clickCount = clickCount;
        this.popupTrigger = popupTrigger;
        if (button < NOBUTTON || button >BUTTON3) {
            throw new IllegalArgumentException("Invalid button value");
        }
        this.button = button;
        if ((getModifiers() != 0) && (getModifiersEx() == 0)) {
            setNewModifiers();
        } else if ((getModifiers() == 0) &&
                   (getModifiersEx() != 0 || button != NOBUTTON))
        {
            setOldModifiers();
        }
    }

    /**
     * Returns the horizontal x position of the event relative to the
     * source component.
     *
     * @return x  an integer indicating horizontal position relative to
     *            the component
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the vertical y position of the event relative to the
     * source component.
     *
     * @return y  an integer indicating vertical position relative to
     *            the component
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the x,y position of the event relative to the source component.
     *
     * @return a <code>Point</code> object containing the x and y coordinates
     *         relative to the source component
     *
     */
    public Point getPoint() {
        int x;
        int y;
        synchronized (this) {
            x = this.x;
            y = this.y;
        }
        return new Point(x, y);
    }

    /**
     * Translates the event's coordinates to a new position
     * by adding specified <code>x</code> (horizontal) and <code>y</code>
     * (vertical) offsets.
     *
     * @param x the horizontal x value to add to the current x
     *          coordinate position
     * @param y the vertical y value to add to the current y
                coordinate position
     */
    public synchronized void translatePoint(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Returns the number of mouse clicks associated with this event.
     *
     * @return integer value for the number of clicks
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * Returns which, if any, of the mouse buttons has changed state.
     *
     * @return one of the following constants:
     * <code>NOBUTTON</code>,
     * <code>BUTTON1</code>,
     * <code>BUTTON2</code> or
     * <code>BUTTON3</code>.
     * @since 1.4
     */
    public int getButton() {
        return button;
    }

    /**
     * Returns whether or not this mouse event is the popup menu
     * trigger event for the platform.
     * <p><b>Note</b>: Popup menus are triggered differently
     * on different systems. Therefore, <code>isPopupTrigger</code>
     * should be checked in both <code>mousePressed</code>
     * and <code>mouseReleased</code>
     * for proper cross-platform functionality.
     *
     * @return boolean, true if this event is the popup menu trigger
     *         for this platform
     */
    public boolean isPopupTrigger() {
        return popupTrigger;
    }

    /**
     * Returns a <code>String</code> describing the modifier keys and
     * mouse buttons that were down during the event, such as "Shift",
     * or "Ctrl+Shift". These strings can be localized by changing
     * the <code>awt.properties</code> file.
     * <p>
     * Note that <code>InputEvent.ALT_MASK</code> and
     * <code>InputEvent.BUTTON2_MASK</code> have the same value,
     * so the string "Alt" is returned for both modifiers.  Likewise,
     * <code>InputEvent.META_MASK</code> and
     * <code>InputEvent.BUTTON3_MASK</code> have the same value,
     * so the string "Meta" is returned for both modifiers.
     *
     * @param modifiers a modifier mask describing the modifier keys and
     *                  mouse buttons that were down during the event
     * @return string   a text description of the combination of modifier
     *                  keys and mouse buttons that were down during the event
     * @see InputEvent#getModifiersExText(int)
     * @since 1.4
     */
    public static String getMouseModifiersText(int modifiers) {
        StringBuilder buf = new StringBuilder();
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.alt", "Alt"));
            buf.append("+");
        }
        if ((modifiers & InputEvent.META_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.meta", "Meta"));
            buf.append("+");
        }
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.control", "Ctrl"));
            buf.append("+");
        }
        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.shift", "Shift"));
            buf.append("+");
        }
        if ((modifiers & InputEvent.ALT_GRAPH_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.altGraph", "Alt Graph"));
            buf.append("+");
        }
        if ((modifiers & InputEvent.BUTTON1_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.button1", "Button1"));
            buf.append("+");
        }
        if ((modifiers & InputEvent.BUTTON2_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.button2", "Button2"));
            buf.append("+");
        }
        if ((modifiers & InputEvent.BUTTON3_MASK) != 0) {
            buf.append(Toolkit.getProperty("AWT.button3", "Button3"));
            buf.append("+");
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length()-1); // remove trailing '+'
        }
        return buf.toString();
    }

    /**
     * Returns a parameter string identifying this event.
     * This method is useful for event-logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        StringBuilder str = new StringBuilder(80);
        str.append(getIdString(id));
        // (x,y) coordinates
        str.append(",(").append("" + x).append(",").append("" + y).append(")");
        str.append(",absolute(").append("" + xAbs).append(",").append("" + yAbs).append(")");

        str.append(",button=").append("" + getButton());

        if (getModifiers() != 0) {
            str.append(",modifiers=").append(getMouseModifiersText(modifiers));
        }

        if (getModifiersEx() != 0) {
            str.append(",extModifiers=").append(getModifiersExText(modifiers));
        }

        str.append(",clickCount=").append("" + clickCount);

        return str.toString();
    }

	/**
	 * SwingJS convenience for debugging
	 * 
	 * @param id
	 * @return
	 */
	public static String getIdString(int id) {
		switch (id) {
		case MOUSE_PRESSED:
			return "MOUSE_PRESSED";

		case MOUSE_RELEASED:
			return "MOUSE_RELEASED";

		case MOUSE_CLICKED:
			return "MOUSE_CLICKED";

		case MOUSE_ENTERED:
			return "MOUSE_ENTERED";

		case MOUSE_EXITED:
			return "MOUSE_EXITED";

		case MOUSE_MOVED:
			return "MOUSE_MOVED";

		case MOUSE_DRAGGED:
			return "MOUSE_DRAGGED";

		case MOUSE_WHEEL:
			return "MOUSE_WHEEL";

		default:
			return "unknown type";
		}

	}

		/**
     * Sets new modifiers by the old ones.
     * Also sets button.
     */
    private void setNewModifiers() {
        if ((modifiers & BUTTON1_MASK) != 0) {
            modifiers |= BUTTON1_DOWN_MASK;
        }
        if ((modifiers & BUTTON2_MASK) != 0) {
            modifiers |= BUTTON2_DOWN_MASK;
        }
        if ((modifiers & BUTTON3_MASK) != 0) {
            modifiers |= BUTTON3_DOWN_MASK;
        }
        if (id == MOUSE_PRESSED
            || id == MOUSE_RELEASED
            || id == MOUSE_CLICKED)
        {
            if ((modifiers & BUTTON1_MASK) != 0) {
                button = BUTTON1;
                modifiers &= ~BUTTON2_MASK & ~BUTTON3_MASK;
                if (id != MOUSE_PRESSED) {
                    modifiers &= ~BUTTON1_DOWN_MASK;
                }
            } else if ((modifiers & BUTTON2_MASK) != 0) {
                button = BUTTON2;
                modifiers &= ~BUTTON1_MASK & ~BUTTON3_MASK;
                if (id != MOUSE_PRESSED) {
                    modifiers &= ~BUTTON2_DOWN_MASK;
                }
            } else if ((modifiers & BUTTON3_MASK) != 0) {
                button = BUTTON3;
                modifiers &= ~BUTTON1_MASK & ~BUTTON2_MASK;
                if (id != MOUSE_PRESSED) {
                    modifiers &= ~BUTTON3_DOWN_MASK;
                }
            }
        }
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            modifiers |= InputEvent.ALT_DOWN_MASK;
        }
        if ((modifiers & InputEvent.META_MASK) != 0) {
            modifiers |= InputEvent.META_DOWN_MASK;
        }
        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            modifiers |= InputEvent.SHIFT_DOWN_MASK;
        }
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            modifiers |= InputEvent.CTRL_DOWN_MASK;
        }
        if ((modifiers & InputEvent.ALT_GRAPH_MASK) != 0) {
            modifiers |= InputEvent.ALT_GRAPH_DOWN_MASK;
        }
    }

    /**
     * Sets old modifiers by the new ones.
     */
    private void setOldModifiers() {
        if (id == MOUSE_PRESSED
            || id == MOUSE_RELEASED
            || id == MOUSE_CLICKED)
        {
            switch(button) {
            case BUTTON1:
                modifiers |= BUTTON1_MASK;
                break;
            case BUTTON2:
                modifiers |= BUTTON2_MASK;
                break;
            case BUTTON3:
                modifiers |= BUTTON3_MASK;
                break;
            }
        } else {
            if ((modifiers & BUTTON1_DOWN_MASK) != 0) {
                modifiers |= BUTTON1_MASK;
            }
            if ((modifiers & BUTTON2_DOWN_MASK) != 0) {
                modifiers |= BUTTON2_MASK;
            }
            if ((modifiers & BUTTON3_DOWN_MASK) != 0) {
                modifiers |= BUTTON3_MASK;
            }
        }
        if ((modifiers & ALT_DOWN_MASK) != 0) {
            modifiers |= ALT_MASK;
        }
        if ((modifiers & META_DOWN_MASK) != 0) {
            modifiers |= META_MASK;
        }
        if ((modifiers & SHIFT_DOWN_MASK) != 0) {
            modifiers |= SHIFT_MASK;
        }
        if ((modifiers & CTRL_DOWN_MASK) != 0) {
            modifiers |= CTRL_MASK;
        }
        if ((modifiers & ALT_GRAPH_DOWN_MASK) != 0) {
            modifiers |= ALT_GRAPH_MASK;
        }
    }

//    /**
//     * Sets new modifiers by the old ones.
//     * @serial
//     */
//    private void readObject(ObjectInputStream s)
//      throws IOException, ClassNotFoundException {
//        s.defaultReadObject();
//        if (getModifiers() != 0 && getModifiersEx() == 0) {
//            setNewModifiers();
//        }
//    }
}
