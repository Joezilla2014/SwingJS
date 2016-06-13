/*
 * Copyright (c) 1997, 2010, Oracle and/or its affiliates. All rights reserved.
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
package jsjavax.swing;

import jsjava.awt.Component;
import jsjava.awt.Font;
import jsjava.awt.Color;
import jsjava.awt.Insets;
import jsjava.awt.Dimension;



import jsjavax.swing.plaf.ComponentUI;
import jsjavax.swing.border.Border;

//import jsjavax.swing.event.SwingPropertyChangeSupport;
import jsjava.beans.PropertyChangeListener;

import java.util.ArrayList;

import swingjs.JSToolkit;
import swingjs.plaf.HTML5LookAndFeel;
import jsjava.util.Locale;

//import jssun.swing.SwingUtilities2;

// SwingJS just sketching in the LookAndFeel idea here; assuming there is one -- "HTML5LookAndFeel"

/**
 * {@code UIManager} manages the current look and feel, the set of
 * available look and feels, {@code PropertyChangeListeners} that
 * are notified when the look and feel changes, look and feel defaults, and
 * convenience methods for obtaining various default values.
 *
 * <h3>Specifying the look and feel</h3>
 *
 * The look and feel can be specified in two distinct ways: by
 * specifying the fully qualified name of the class for the look and
 * feel, or by creating an instance of {@code LookAndFeel} and passing
 * it to {@code setLookAndFeel}. The following example illustrates
 * setting the look and feel to the system look and feel:
 * <pre>
 *   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
 * </pre>
 * The following example illustrates setting the look and feel based on
 * class name:
 * <pre>
 *   UIManager.setLookAndFeel("jsjavax.swing.plaf.metal.MetalLookAndFeel");
 * </pre>
 * Once the look and feel has been changed it is imperative to invoke
 * {@code updateUI} on all {@code JComponents}. The method {@link
 * SwingUtilities#updateComponentTreeUI} makes it easy to apply {@code
 * updateUI} to a containment hierarchy. Refer to it for
 * details. The exact behavior of not invoking {@code
 * updateUI} after changing the look and feel is
 * unspecified. It is very possible to receive unexpected exceptions,
 * painting problems, or worse.
 *
 * <h3>Default look and feel</h3>
 *
 * The class used for the default look and feel is chosen in the following
 * manner:
 * <ol>
 *   <li>If the system property <code>swing.defaultlaf</code> is
 *       {@code non-null}, use its value as the default look and feel class
 *       name.
 *   <li>If the {@link java.util.Properties} file <code>swing.properties</code>
 *       exists and contains the key <code>swing.defaultlaf</code>,
 *       use its value as the default look and feel class name. The location
 *       that is checked for <code>swing.properties</code> may vary depending
 *       upon the implementation of the Java platform. In Sun's implementation
 *       the location is <code>${java.home}/lib/swing.properties</code>.
 *       Refer to the release notes of the implementation being used for
 *       further details.
 *   <li>Otherwise use the cross platform look and feel.
 * </ol>
 *
 * <h3>Defaults</h3>
 *
 * {@code UIManager} manages three sets of {@code UIDefaults}. In order, they
 * are:
 * <ol>
 *   <li>Developer defaults. With few exceptions Swing does not
 *       alter the developer defaults; these are intended to be modified
 *       and used by the developer.
 *   <li>Look and feel defaults. The look and feel defaults are
 *       supplied by the look and feel at the time it is installed as the
 *       current look and feel ({@code setLookAndFeel()} is invoked). The
 *       look and feel defaults can be obtained using the {@code
 *       getLookAndFeelDefaults()} method.
 *   <li>Sytem defaults. The system defaults are provided by Swing.
 * </ol>
 * Invoking any of the various {@code get} methods
 * results in checking each of the defaults, in order, returning
 * the first {@code non-null} value. For example, invoking
 * {@code UIManager.getString("Table.foreground")} results in first
 * checking developer defaults. If the developer defaults contain
 * a value for {@code "Table.foreground"} it is returned, otherwise
 * the look and feel defaults are checked, followed by the system defaults.
 * <p>
 * It's important to note that {@code getDefaults} returns a custom
 * instance of {@code UIDefaults} with this resolution logic built into it.
 * For example, {@code UIManager.getDefaults().getString("Table.foreground")}
 * is equivalent to {@code UIManager.getString("Table.foreground")}. Both
 * resolve using the algorithm just described. In many places the
 * documentation uses the word defaults to refer to the custom instance
 * of {@code UIDefaults} with the resolution logic as previously described.
 * <p>
 * When the look and feel is changed, {@code UIManager} alters only the
 * look and feel defaults; the developer and system defaults are not
 * altered by the {@code UIManager} in any way.
 * <p>
 * The set of defaults a particular look and feel supports is defined
 * and documented by that look and feel. In addition, each look and
 * feel, or {@code ComponentUI} provided by a look and feel, may
 * access the defaults at different times in their life cycle. Some
 * look and feels may agressively look up defaults, so that changing a
 * default may not have an effect after installing the look and feel.
 * Other look and feels may lazily access defaults so that a change to
 * the defaults may effect an existing look and feel. Finally, other look
 * and feels might not configure themselves from the defaults table in
 * any way. None-the-less it is usually the case that a look and feel
 * expects certain defaults, so that in general
 * a {@code ComponentUI} provided by one look and feel will not
 * work with another look and feel.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>jsjava.beans</code> package.
 * Please see {@link jsjava.beans.XMLEncoder}.
 *
 * @author Thomas Ball
 * @author Hans Muller
 */
public class UIManager
{
//    /**
//     * This class defines the state managed by the <code>UIManager</code>.  For
//     * Swing applications the fields in this class could just as well
//     * be static members of <code>UIManager</code> however we give them
//     * "AppContext"
//     * scope instead so that applets (and potentially multiple lightweight
//     * applications running in a single VM) have their own state. For example,
//     * an applet can alter its look and feel, see <code>setLookAndFeel</code>.
//     * Doing so has no affect on other applets (or the browser).
//     */

	public static UIDefaults getLookAndFeelDefaults() { return uid; }//tables[0]; }
////        void setLookAndFeelDefaults(UIDefaults x) { tables[0] = x; }
////
////        UIDefaults getSystemDefaults() { return tables[1]; }
////        void setSystemDefaults(UIDefaults x) { tables[1] = x; }
//
//        /**
//         * Returns the SwingPropertyChangeSupport for the current
//         * AppContext.  If <code>create</code> is a true, a non-null
//         * <code>SwingPropertyChangeSupport</code> will be returned, if
//         * <code>create</code> is false and this has not been invoked
//         * with true, null will be returned.
//         */
//        public synchronized SwingPropertyChangeSupport
//                                 getPropertyChangeSupport(boolean create) {
//            if (create && changeSupport == null) {
//                changeSupport = new SwingPropertyChangeSupport(
//                                         UIManager.class);
//            }
//            return changeSupport;
//        }
//    }
//



    /* Lock object used in place of class object for synchronization. (4187686)
     */
//    private static final Object classLock = new Object();

//    /**
//     * Return the <code>LAFState</code> object, lazily create one if necessary.
//     * All access to the <code>LAFState</code> fields is done via this method,
//     * for example:
//     * <pre>
//     *     getLAFState().initialized = true;
//     * </pre>
//     */
//    private static LAFState getLAFState() {
//        LAFState rv = (LAFState)SwingUtilities.appContextGet(
//                SwingUtilities2.LAF_STATE_KEY);
//        if (rv == null) {
//            synchronized (classLock) {
//                rv = (LAFState)SwingUtilities.appContextGet(
//                        SwingUtilities2.LAF_STATE_KEY);
//                if (rv == null) {
//                    SwingUtilities.appContextPut(
//                            SwingUtilities2.LAF_STATE_KEY,
//                            (rv = new LAFState()));
//                }
//            }
//        }
//        return rv;
//    }


//    /* Keys used for the properties file in <java.home>/lib/swing.properties.
//     * See loadUserProperties(), initialize().
//     */
//
//    private static final String defaultLAFKey = "swing.defaultlaf";
//    private static final String auxiliaryLAFsKey = "swing.auxiliarylaf";
//    private static final String multiplexingLAFKey = "swing.plaf.multiplexinglaf";
//    private static final String installedLAFsKey = "swing.installedlafs";
//    private static final String disableMnemonicKey = "swing.disablenavaids";
//
//    /**
//     * Return a swing.properties file key for the attribute of specified
//     * look and feel.  The attr is either "name" or "class", a typical
//     * key would be: "swing.installedlaf.windows.name"
//     */
//    private static String makeInstalledLAFKey(String laf, String attr) {
//        return "swing.installedlaf." + laf + "." + attr;
//    }
//
//    /**
//     * The filename for swing.properties is a path like this (Unix version):
//     * <java.home>/lib/swing.properties.  This method returns a bogus
//     * filename if java.home isn't defined.
//     */
//    private static String makeSwingPropertiesFilename() {
//        String sep = File.separator;
//        // No need to wrap this in a doPrivileged as it's called from
//        // a doPrivileged.
//        String javaHome = System.getProperty("java.home");
//        if (javaHome == null) {
//            javaHome = "<java.home undefined>";
//        }
//        return javaHome + sep + "lib" + sep + "swing.properties";
//    }


    /**
     * Provides a little information about an installed
     * <code>LookAndFeel</code> for the sake of configuring a menu or
     * for initial application set up.
     *
     * @see UIManager#getInstalledLookAndFeels
     * @see LookAndFeel
     */
    public static class LookAndFeelInfo {
        private String name;
        private String className;

        /**
         * Constructs a <code>UIManager</code>s
         * <code>LookAndFeelInfo</code> object.
         *
         * @param name      a <code>String</code> specifying the name of
         *                      the look and feel
         * @param className a <code>String</code> specifiying the name of
         *                      the class that implements the look and feel
         */
        public LookAndFeelInfo(String name, String className) {
            this.name = name;
            this.className = className;
        }

        /**
         * Returns the name of the look and feel in a form suitable
         * for a menu or other presentation
         * @return a <code>String</code> containing the name
         * @see LookAndFeel#getName
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the name of the class that implements this look and feel.
         * @return the name of the class that implements this
         *              <code>LookAndFeel</code>
         * @see LookAndFeel
         */
        public String getClassName() {
            return className;
        }

        /**
         * Returns a string that displays and identifies this
         * object's properties.
         *
         * @return a <code>String</code> representation of this object
         */
        @Override
				public String toString() {
            return getClass().getName() + "[" + getName() + " " + getClassName() + "]";
        }
    }


    /**
     * The default value of <code>installedLAFS</code> is used when no
     * swing.properties
     * file is available or if the file doesn't contain a "swing.installedlafs"
     * property.
     *
     * @see #initializeInstalledLAFs
     */
    private static LookAndFeelInfo[] installedLAFs;

    static {
        ArrayList iLAFs = new ArrayList(4);
        iLAFs.add(new LookAndFeelInfo("HTML5", "swingjs.plaf.HTML5LookAndFeel"));
// SwingJS         iLAFs.add(new LookAndFeelInfo(
//                      "Metal", "jsjavax.swing.plaf.metal.MetalLookAndFeel"));
//        iLAFs.add(new LookAndFeelInfo(
//                      "Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"));
//        iLAFs.add(new LookAndFeelInfo("CDE/Motif",
//                  "com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
//
//        // Only include windows on Windows boxs.
//        OSInfo.OSType osType = AccessController.doPrivileged(OSInfo.getOSTypeAction());
//        if (osType == OSInfo.OSType.WINDOWS) {
//            iLAFs.add(new LookAndFeelInfo("Windows",
//                        "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
//            if (Toolkit.getDefaultToolkit().getDesktopProperty(
//                    "win.xpstyle.themeActive") != null) {
//                iLAFs.add(new LookAndFeelInfo("Windows Classic",
//                 "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"));
//            }
//        }
//        else {
//            // GTK is not shipped on Windows.
//            iLAFs.add(new LookAndFeelInfo("GTK+",
//                  "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"));
//        }
        installedLAFs = (LookAndFeelInfo[])iLAFs.toArray(
                        new LookAndFeelInfo[iLAFs.size()]);
    }


    /**
     * Returns an array of {@code LookAndFeelInfo}s representing the
     * {@code LookAndFeel} implementations currently available. The
     * <code>LookAndFeelInfo</code> objects can be used by an
     * application to construct a menu of look and feel options for
     * the user, or to determine which look and feel to set at startup
     * time. To avoid the penalty of creating numerous {@code
     * LookAndFeel} objects, {@code LookAndFeelInfo} maintains the
     * class name of the {@code LookAndFeel} class, not the actual
     * {@code LookAndFeel} instance.
     * <p>
     * The following example illustrates setting the current look and feel
     * from an instance of {@code LookAndFeelInfo}:
     * <pre>
     *   UIManager.setLookAndFeel(info.getClassName());
     * </pre>
     *
     * @return an array of <code>LookAndFeelInfo</code> objects
     * @see #setLookAndFeel
     */
    public static LookAndFeelInfo[] getInstalledLookAndFeels() {
//        maybeInitialize();
//        LookAndFeelInfo[] ilafs = getLAFState().installedLAFs;
//        if (ilafs == null) {
//            ilafs = installedLAFs;
//        }
//        LookAndFeelInfo[] rv = new LookAndFeelInfo[ilafs.length];
//        System.arraycopy(ilafs, 0, rv, 0, ilafs.length);
//        return rv;
    	return installedLAFs;
    }


    /**
     * Sets the set of available look and feels. While this method does
     * not check to ensure all of the {@code LookAndFeelInfos} are
     * {@code non-null}, it is strongly recommended that only {@code non-null}
     * values are supplied in the {@code infos} array.
     *
     * @param infos set of <code>LookAndFeelInfo</code> objects specifying
     *        the available look and feels
     *
     * @see #getInstalledLookAndFeels
     * @throws NullPointerException if {@code infos} is {@code null}
     */
    public static void setInstalledLookAndFeels(LookAndFeelInfo[] infos)
        throws SecurityException
    {
    	JSToolkit.notImplemented(null);
//        maybeInitialize();
//        LookAndFeelInfo[] newInfos = new LookAndFeelInfo[infos.length];
//        System.arraycopy(infos, 0, newInfos, 0, infos.length);
//        getLAFState().installedLAFs = newInfos;
    }


    /**
     * Adds the specified look and feel to the set of available look
     * and feels. While this method allows a {@code null} {@code info},
     * it is strongly recommended that a {@code non-null} value be used.
     *
     * @param info a <code>LookAndFeelInfo</code> object that names the
     *          look and feel and identifies the class that implements it
     * @see #setInstalledLookAndFeels
     */
    public static void installLookAndFeel(LookAndFeelInfo info) {
    	JSToolkit.notImplemented(null);
//        LookAndFeelInfo[] infos = getInstalledLookAndFeels();
//        LookAndFeelInfo[] newInfos = new LookAndFeelInfo[infos.length + 1];
//        System.arraycopy(infos, 0, newInfos, 0, infos.length);
//        newInfos[infos.length] = info;
//        setInstalledLookAndFeels(newInfos);
    }


    /**
     * Adds the specified look and feel to the set of available look
     * and feels. While this method does not check the
     * arguments in any way, it is strongly recommended that {@code
     * non-null} values be supplied.
     *
     * @param name descriptive name of the look and feel
     * @param className name of the class that implements the look and feel
     * @see #setInstalledLookAndFeels
     */
    public static void installLookAndFeel(String name, String className) {
    	JSToolkit.notImplemented(null);
//        installLookAndFeel(new LookAndFeelInfo(name, className));
    }

	
	private static LookAndFeel laf;
	
    /**
     * Returns the current (and only) look and feel.
     *
     * @return current look and feel, or <code>null</code>
     * @see #setLookAndFeel
     */
    public static LookAndFeel getLookAndFeel() {
    	return (laf == null ? (laf = (HTML5LookAndFeel) JSToolkit.getInstance("swingjs.plaf.HTML5LookAndFeel")) : laf);
    }


    /**
     * Sets the current look and feel to {@code newLookAndFeel}.
     * If the current look and feel is {@code non-null} {@code
     * uninitialize} is invoked on it. If {@code newLookAndFeel} is
     * {@code non-null}, {@code initialize} is invoked on it followed
     * by {@code getDefaults}. The defaults returned from {@code
     * newLookAndFeel.getDefaults()} replace those of the defaults
     * from the previous look and feel. If the {@code newLookAndFeel} is
     * {@code null}, the look and feel defaults are set to {@code null}.
     * <p>
     * A value of {@code null} can be used to set the look and feel
     * to {@code null}. As the {@code LookAndFeel} is required for
     * most of Swing to function, setting the {@code LookAndFeel} to
     * {@code null} is strongly discouraged.
     * <p>
     * This is a JavaBeans bound property.
     *
     * @param newLookAndFeel {@code LookAndFeel} to install
     * @throws UnsupportedLookAndFeelException if
     *          {@code newLookAndFeel} is {@code non-null} and
     *          {@code newLookAndFeel.isSupportedLookAndFeel()} returns
     *          {@code false}
     * @see #getLookAndFeel
     */
    public static void setLookAndFeel(LookAndFeel newLookAndFeel)
        throws UnsupportedLookAndFeelException
    {
//        if ((newLookAndFeel != null) && !newLookAndFeel.isSupportedLookAndFeel()) {
//            String s = newLookAndFeel.toString() + " not supported on this platform";
//            throw new UnsupportedLookAndFeelException(s);
//        }
//
//        LAFState lafState = getLAFState();
//        LookAndFeel oldLookAndFeel = lafState.lookAndFeel;
//        if (oldLookAndFeel != null) {
//            oldLookAndFeel.uninitialize();
//        }
//
//        lafState.lookAndFeel = newLookAndFeel;
//        if (newLookAndFeel != null) {
//            jssun.swing.DefaultLookup.setDefaultLookup(null);
//            newLookAndFeel.initialize();
//            lafState.setLookAndFeelDefaults(newLookAndFeel.getDefaults());
//        }
//        else {
//            lafState.setLookAndFeelDefaults(null);
//        }
//
//        SwingPropertyChangeSupport changeSupport = lafState.
//                                         getPropertyChangeSupport(false);
//        if (changeSupport != null) {
//            changeSupport.firePropertyChange("lookAndFeel", oldLookAndFeel,
//                                             newLookAndFeel);
//        }
    }


    /**
     * Loads the {@code LookAndFeel} specified by the given class
     * name, using the current thread's context class loader, and
     * passes it to {@code setLookAndFeel(LookAndFeel)}.
     *
     * @param className  a string specifying the name of the class that implements
     *        the look and feel
     * @exception ClassNotFoundException if the <code>LookAndFeel</code>
     *           class could not be found
     * @exception InstantiationException if a new instance of the class
     *          couldn't be created
     * @exception IllegalAccessException if the class or initializer isn't accessible
     * @exception UnsupportedLookAndFeelException if
     *          <code>lnf.isSupportedLookAndFeel()</code> is false
     * @throws ClassCastException if {@code className} does not identify
     *         a class that extends {@code LookAndFeel}
     */
    public static void setLookAndFeel(String className)
        throws ClassNotFoundException,
               InstantiationException,
               IllegalAccessException,
               UnsupportedLookAndFeelException
    {
//        if ("jsjavax.swing.plaf.metal.MetalLookAndFeel".equals(className)) {
//            // Avoid reflection for the common case of metal.
//            setLookAndFeel(new jsjavax.swing.plaf.metal.MetalLookAndFeel());
//        }
//        else {
            Class lnfClass = SwingUtilities.loadSystemClass(className);
            setLookAndFeel((LookAndFeel)(lnfClass.newInstance()));
//        }
    }

    /**
     * Returns the name of the <code>LookAndFeel</code> class that implements
     * the native system look and feel if there is one, otherwise
     * the name of the default cross platform <code>LookAndFeel</code>
     * class. This value can be overriden by setting the
     * <code>swing.systemlaf</code> system property.
     *
     * @return the <code>String</code> of the <code>LookAndFeel</code>
     *          class
     *
     * @see #setLookAndFeel
     * @see #getCrossPlatformLookAndFeelClassName
     */
    public static String getSystemLookAndFeelClassName() {
//        String systemLAF = AccessController.doPrivileged(
//                             new GetPropertyAction("swing.systemlaf"));
//        if (systemLAF != null) {
//            return systemLAF;
//        }
// SwingJS  ??         OSInfo.OSType osType = AccessController.doPrivileged(OSInfo.getOSTypeAction());
//        if (osType == OSInfo.OSType.WINDOWS) {
//            return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
//        } else {
//            String desktop = AccessController.doPrivileged(new GetPropertyAction("sun.desktop"));
//            Toolkit toolkit = Toolkit.getDefaultToolkit();
//            if ("gnome".equals(desktop) &&
//                    toolkit instanceof SunToolkit &&
//                    ((SunToolkit) toolkit).isNativeGTKAvailable()) {
//                // May be set on Linux and Solaris boxs.
//                return "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
//            }
//            if (osType == OSInfo.OSType.SOLARIS) {
//                return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
//            }
//        }
        return getCrossPlatformLookAndFeelClassName();
    }


    /**
     * Returns the name of the <code>LookAndFeel</code> class that implements
     * the default cross platform look and feel -- the Java
     * Look and Feel (JLF).  This value can be overriden by setting the
     * <code>swing.crossplatformlaf</code> system property.
     *
     * @return  a string with the JLF implementation-class
     * @see #setLookAndFeel
     * @see #getSystemLookAndFeelClassName
     */
    public static String getCrossPlatformLookAndFeelClassName() {
    	return  "swingjs.plaf.HTML5LookAndFeel";
//      return "jsjavax.swing.plaf.basic.BasicLookAndFeel";
//        String laf = (String)AccessController.doPrivileged(
//                             new GetPropertyAction("swing.crossplatformlaf"));
//        if (laf != null) {
//            return laf;
//        }
//        return "jsjavax.swing.plaf.metal.MetalLookAndFeel";
    }


    /**
     * Returns the defaults. The returned defaults resolve using the
     * logic specified in the class documentation.
     *
     * @return a <code>UIDefaults</code> object containing the default values
     */
    public static UIDefaults getDefaults() {
        maybeInitialize();
        return uid;//getLAFState().multiUIDefaults;
    }

    /**
     * Returns a font from the defaults. If the value for {@code key} is
     * not a {@code Font}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the font
     * @return the <code>Font</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static Font getFont(Object key) {
        return getDefaults().getFont(key);
    }

    /**
     * Returns a font from the defaults that is appropriate
     * for the given locale. If the value for {@code key} is
     * not a {@code Font}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the font
     * @param l the <code>Locale</code> for which the font is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>Font</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static Font getFont(Object key, Locale l) {
        return getDefaults().getFont(key,l);
    }

    /**
     * Returns a color from the defaults. If the value for {@code key} is
     * not a {@code Color}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the color
     * @return the <code>Color</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static Color getColor(Object key) {
        return getDefaults().getColor(key);
    }

    /**
     * Returns a color from the defaults that is appropriate
     * for the given locale. If the value for {@code key} is
     * not a {@code Color}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the color
     * @param l the <code>Locale</code> for which the color is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>Color</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static Color getColor(Object key, Locale l) {
        return getDefaults().getColor(key,l);
    }

    /**
     * Returns an <code>Icon</code> from the defaults. If the value for
     * {@code key} is not an {@code Icon}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the icon
     * @return the <code>Icon</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static Icon getIcon(Object key) {
        return getDefaults().getIcon(key);
    }

    /**
     * Returns an <code>Icon</code> from the defaults that is appropriate
     * for the given locale. If the value for
     * {@code key} is not an {@code Icon}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the icon
     * @param l the <code>Locale</code> for which the icon is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>Icon</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static Icon getIcon(Object key, Locale l) {
        return getDefaults().getIcon(key,l);
    }

    /**
     * Returns a border from the defaults. If the value for
     * {@code key} is not a {@code Border}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the border
     * @return the <code>Border</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static Border getBorder(Object key) {
        return getDefaults().getBorder(key);
    }

    /**
     * Returns a border from the defaults that is appropriate
     * for the given locale.  If the value for
     * {@code key} is not a {@code Border}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the border
     * @param l the <code>Locale</code> for which the border is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>Border</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static Border getBorder(Object key, Locale l) {
        return getDefaults().getBorder(key,l);
    }

    /**
     * Returns a string from the defaults. If the value for
     * {@code key} is not a {@code String}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the string
     * @return the <code>String</code>
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static String getString(Object key) {
        return getDefaults().getString(key);
    }

    /**
     * Returns a string from the defaults that is appropriate for the
     * given locale.  If the value for
     * {@code key} is not a {@code String}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the string
     * @param l the <code>Locale</code> for which the string is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>String</code>
     * @since 1.4
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static String getString(Object key, Locale l) {
        return getDefaults().getString(key,l);
    }

    /**
     * Returns a string from the defaults that is appropriate for the
     * given locale.  If the value for
     * {@code key} is not a {@code String}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the string
     * @param c {@code Component} used to determine the locale;
     *          {@code null} implies the default locale as
     *          returned by {@code Locale.getDefault()}
     * @return the <code>String</code>
     * @throws NullPointerException if {@code key} is {@code null}
     */
    static String getString(Object key, Component c) {
        Locale l = (c == null) ? Locale.getDefault() : c.getLocale();
        return getString(key, l);
    }

    /**
     * Returns an integer from the defaults. If the value for
     * {@code key} is not an {@code Integer}, or does not exist,
     * {@code 0} is returned.
     *
     * @param key  an <code>Object</code> specifying the int
     * @return the int
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static int getInt(Object key) {
        return getDefaults().getInt(key);
    }

    /**
     * Returns an integer from the defaults that is appropriate
     * for the given locale. If the value for
     * {@code key} is not an {@code Integer}, or does not exist,
     * {@code 0} is returned.
     *
     * @param key  an <code>Object</code> specifying the int
     * @param l the <code>Locale</code> for which the int is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the int
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static int getInt(Object key, Locale l) {
        return getDefaults().getInt(key,l);
    }

    /**
     * Returns a boolean from the defaults which is associated with
     * the key value. If the key is not found or the key doesn't represent
     * a boolean value then {@code false} is returned.
     *
     * @param key  an <code>Object</code> specifying the key for the desired boolean value
     * @return the boolean value corresponding to the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static boolean getBoolean(Object key) {
        return getDefaults().getBoolean(key);
    }

    /**
     * Returns a boolean from the defaults which is associated with
     * the key value and the given <code>Locale</code>. If the key is not
     * found or the key doesn't represent
     * a boolean value then {@code false} will be returned.
     *
     * @param key  an <code>Object</code> specifying the key for the desired
     *             boolean value
     * @param l the <code>Locale</code> for which the boolean is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the boolean value corresponding to the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static boolean getBoolean(Object key, Locale l) {
        return getDefaults().getBoolean(key,l);
    }

    /**
     * Returns an <code>Insets</code> object from the defaults. If the value
     * for {@code key} is not an {@code Insets}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the <code>Insets</code> object
     * @return the <code>Insets</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static Insets getInsets(Object key) {
        return getDefaults().getInsets(key);
    }

    /**
     * Returns an <code>Insets</code> object from the defaults that is
     * appropriate for the given locale. If the value
     * for {@code key} is not an {@code Insets}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the <code>Insets</code> object
     * @param l the <code>Locale</code> for which the object is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>Insets</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static Insets getInsets(Object key, Locale l) {
        return getDefaults().getInsets(key,l);
    }

    /**
     * Returns a dimension from the defaults. If the value
     * for {@code key} is not a {@code Dimension}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the dimension object
     * @return the <code>Dimension</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static Dimension getDimension(Object key) {
        return getDefaults().getDimension(key);
    }

    /**
     * Returns a dimension from the defaults that is appropriate
     * for the given locale. If the value
     * for {@code key} is not a {@code Dimension}, {@code null} is returned.
     *
     * @param key  an <code>Object</code> specifying the dimension object
     * @param l the <code>Locale</code> for which the object is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>Dimension</code> object
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static Dimension getDimension(Object key, Locale l) {
        return getDefaults().getDimension(key,l);
    }

    /**
     * Returns an object from the defaults.
     *
     * @param key  an <code>Object</code> specifying the desired object
     * @return the <code>Object</code>
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public static Object get(Object key) {
        return getDefaults().get(key);
    }

    /**
     * Returns an object from the defaults that is appropriate for
     * the given locale.
     *
     * @param key  an <code>Object</code> specifying the desired object
     * @param l the <code>Locale</code> for which the object is desired; refer
     *        to {@code UIDefaults} for details on how a {@code null}
     *        {@code Locale} is handled
     * @return the <code>Object</code>
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public static Object get(Object key, Locale l) {
        return getDefaults().get(key,l);
    }

    /**
     * Stores an object in the developer defaults. This is a cover method
     * for {@code getDefaults().put(key, value)}. This only effects the
     * developer defaults, not the system or look and feel defaults.
     *
     * @param key    an <code>Object</code> specifying the retrieval key
     * @param value  the <code>Object</code> to store; refer to
     *               {@code UIDefaults} for details on how {@code null} is
     *               handled
     * @return the <code>Object</code> returned by {@link UIDefaults#put}
     * @throws NullPointerException if {@code key} is {@code null}
     * @see UIDefaults#put
     */
    public static Object put(Object key, Object value) {
        return getDefaults().put(key, value);
    }

	/**
	 * Returns the appropriate {@code ComponentUI} implementation for
	 * {@code target}. Typically, this is a cover for
	 * {@code getDefaults().getUI(target)}. However, if an auxiliary look and feel
	 * has been installed, this first invokes {@code getUI(target)} on the
	 * multiplexing look and feel's defaults, and returns that value if it is
	 * {@code non-null}.
	 * 
	 * @param target
	 *          the <code>JComponent</code> to return the {@code ComponentUI} for
	 * @return the <code>ComponentUI</code> object for {@code target}
	 * @throws NullPointerException
	 *           if {@code target} is {@code null}
	 * @see UIDefaults#getUI
	 */
	public static ComponentUI getUI(Component target) {
		maybeInitialize();
		// note that we use Component here instead of JComponent, because JWindow, JFrame,
		// and JDialog UI are all returned in JavaScript with this method despite the fact 
		// that the method would not work in Java.
		
		ComponentUI ui = getDefaults().getUI((JComponent) target);
		if (ui == null)
			System.out.println("SwingJS interface has not been implemented for "
					+ target.getClass().getName());
		return ui;
	}


//    /**
//     * Returns the {@code UIDefaults} from the current look and feel,
//     * that were obtained at the time the look and feel was installed.
//     * <p>
//     * In general, developers should use the {@code UIDefaults} returned from
//     * {@code getDefaults()}. As the current look and feel may expect
//     * certain values to exist, altering the {@code UIDefaults} returned
//     * from this method could have unexpected results.
//     *
//     * @return <code>UIDefaults</code> from the current look and feel
//     * @see #getDefaults
//     * @see #setLookAndFeel(LookAndFeel)
//     * @see LookAndFeel#getDefaults
//     */
//    public static UIDefaults getLookAndFeelDefaults() {
//        maybeInitialize();
//        return getLAFState().getLookAndFeelDefaults();
//    }
//
//    /**
//     * Finds the Multiplexing <code>LookAndFeel</code>.
//     */
//    private static LookAndFeel getMultiLookAndFeel() {
//        LookAndFeel multiLookAndFeel = getLAFState().multiLookAndFeel;
//        if (multiLookAndFeel == null) {
//            String defaultName = "jsjavax.swing.plaf.multi.MultiLookAndFeel";
//            String className = getLAFState().swingProps.getProperty(multiplexingLAFKey, defaultName);
//            try {
//                Class lnfClass = SwingUtilities.loadSystemClass(className);
//                multiLookAndFeel = (LookAndFeel)lnfClass.newInstance();
//            } catch (Exception exc) {
//                System.err.println("UIManager: failed loading " + className);
//            }
//        }
//        return multiLookAndFeel;
//    }
//
//    /**
//     * Adds a <code>LookAndFeel</code> to the list of auxiliary look and feels.
//     * The auxiliary look and feels tell the multiplexing look and feel what
//     * other <code>LookAndFeel</code> classes for a component instance are to be used
//     * in addition to the default <code>LookAndFeel</code> class when creating a
//     * multiplexing UI.  The change will only take effect when a new
//     * UI class is created or when the default look and feel is changed
//     * on a component instance.
//     * <p>Note these are not the same as the installed look and feels.
//     *
//     * @param laf the <code>LookAndFeel</code> object
//     * @see #removeAuxiliaryLookAndFeel
//     * @see #setLookAndFeel
//     * @see #getAuxiliaryLookAndFeels
//     * @see #getInstalledLookAndFeels
//     */
//    static public void addAuxiliaryLookAndFeel(LookAndFeel laf) {
//        maybeInitialize();
//
//        if (!laf.isSupportedLookAndFeel()) {
//            // Ideally we would throw an exception here, but it's too late
//            // for that.
//            return;
//        }
//        Vector v = getLAFState().auxLookAndFeels;
//        if (v == null) {
//            v = new Vector();
//        }
//
//        if (!v.contains(laf)) {
//            v.addElement(laf);
//            laf.initialize();
//            getLAFState().auxLookAndFeels = v;
//
//            if (getLAFState().multiLookAndFeel == null) {
//                getLAFState().multiLookAndFeel = getMultiLookAndFeel();
//            }
//        }
//    }
//
//    /**
//     * Removes a <code>LookAndFeel</code> from the list of auxiliary look and feels.
//     * The auxiliary look and feels tell the multiplexing look and feel what
//     * other <code>LookAndFeel</code> classes for a component instance are to be used
//     * in addition to the default <code>LookAndFeel</code> class when creating a
//     * multiplexing UI.  The change will only take effect when a new
//     * UI class is created or when the default look and feel is changed
//     * on a component instance.
//     * <p>Note these are not the same as the installed look and feels.
//     * @return true if the <code>LookAndFeel</code> was removed from the list
//     * @see #removeAuxiliaryLookAndFeel
//     * @see #getAuxiliaryLookAndFeels
//     * @see #setLookAndFeel
//     * @see #getInstalledLookAndFeels
//     */
//    static public boolean removeAuxiliaryLookAndFeel(LookAndFeel laf) {
//        maybeInitialize();
//
//        boolean result;
//
//        Vector v = getLAFState().auxLookAndFeels;
//        if ((v == null) || (v.size() == 0)) {
//            return false;
//        }
//
//        result = v.removeElement(laf);
//        if (result) {
//            if (v.size() == 0) {
//                getLAFState().auxLookAndFeels = null;
//                getLAFState().multiLookAndFeel = null;
//            } else {
//                getLAFState().auxLookAndFeels = v;
//            }
//        }
//        laf.uninitialize();
//
//        return result;
//    }
//
//    /**
//     * Returns the list of auxiliary look and feels (can be <code>null</code>).
//     * The auxiliary look and feels tell the multiplexing look and feel what
//     * other <code>LookAndFeel</code> classes for a component instance are
//     * to be used in addition to the default LookAndFeel class when creating a
//     * multiplexing UI.
//     * <p>Note these are not the same as the installed look and feels.
//     *
//     * @return list of auxiliary <code>LookAndFeel</code>s or <code>null</code>
//     * @see #addAuxiliaryLookAndFeel
//     * @see #removeAuxiliaryLookAndFeel
//     * @see #setLookAndFeel
//     * @see #getInstalledLookAndFeels
//     */
//    static public LookAndFeel[] getAuxiliaryLookAndFeels() {
//        maybeInitialize();
//
//        Vector v = getLAFState().auxLookAndFeels;
//        if ((v == null) || (v.size() == 0)) {
//            return null;
//        }
//        else {
//            LookAndFeel[] rv = new LookAndFeel[v.size()];
//            for (int i = 0; i < rv.length; i++) {
//                rv[i] = (LookAndFeel)v.elementAt(i);
//            }
//            return rv;
//        }
//    }


    /**
     * Adds a <code>PropertyChangeListener</code> to the listener list.
     * The listener is registered for all properties.
     *
     * @param listener  the <code>PropertyChangeListener</code> to be added
     * @see jsjava.beans.PropertyChangeSupport
     */
    public static void addPropertyChangeListener(PropertyChangeListener listener)
    {
//        synchronized (classLock) {
//            getLAFState().getPropertyChangeSupport(true).
//                             addPropertyChangeListener(listener);
//        }
    }


    /**
     * Removes a <code>PropertyChangeListener</code> from the listener list.
     * This removes a <code>PropertyChangeListener</code> that was registered
     * for all properties.
     *
     * @param listener  the <code>PropertyChangeListener</code> to be removed
     * @see jsjava.beans.PropertyChangeSupport
     */
    public static void removePropertyChangeListener(PropertyChangeListener listener)
    {
//        synchronized (classLock) {
//            getLAFState().getPropertyChangeSupport(true).
//                          removePropertyChangeListener(listener);
//        }
    }


    /**
     * Returns an array of all the <code>PropertyChangeListener</code>s added
     * to this UIManager with addPropertyChangeListener().
     *
     * @return all of the <code>PropertyChangeListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public static PropertyChangeListener[] getPropertyChangeListeners() {
    	return new PropertyChangeListener[0];
//        synchronized(classLock) {
//            return getLAFState().getPropertyChangeSupport(true).
//                      getPropertyChangeListeners();
//        }
    }

//    private static Properties loadSwingProperties()
//    {
//        /* Don't bother checking for Swing properties if untrusted, as
//         * there's no way to look them up without triggering SecurityExceptions.
//         */
//        if (UIManager.class.getClassLoader() != null) {
//            return new Properties();
//        }
//        else {
//            final Properties props = new Properties();
//
//            jsjava.security.AccessController.doPrivileged(
//                new jsjava.security.PrivilegedAction() {
//                public Object run() {
//                    try {
//                        File file = new File(makeSwingPropertiesFilename());
//
//                        if (file.exists()) {
//                            // InputStream has been buffered in Properties
//                            // class
//                            FileInputStream ins = new FileInputStream(file);
//                            props.load(ins);
//                            ins.close();
//                        }
//                    }
//                    catch (Exception e) {
//                        // No such file, or file is otherwise non-readable.
//                    }
//
//                    // Check whether any properties were overridden at the
//                    // command line.
//                    checkProperty(props, defaultLAFKey);
//                    checkProperty(props, auxiliaryLAFsKey);
//                    checkProperty(props, multiplexingLAFKey);
//                    checkProperty(props, installedLAFsKey);
//                    checkProperty(props, disableMnemonicKey);
//                    // Don't care about return value.
//                    return null;
//                }
//            });
//            return props;
//        }
//    }

//    private static void checkProperty(Properties props, String key) {
//        // No need to do catch the SecurityException here, this runs
//        // in a doPrivileged.
//        String value = System.getProperty(key);
//        if (value != null) {
//            props.put(key, value);
//        }
//    }


//    /**
//     * If a swing.properties file exist and it has a swing.installedlafs property
//     * then initialize the <code>installedLAFs</code> field.
//     *
//     * @see #getInstalledLookAndFeels
//     */
//    private static void initializeInstalledLAFs(Properties swingProps)
//    {
//        String ilafsString = swingProps.getProperty(installedLAFsKey);
//        if (ilafsString == null) {
//            return;
//        }
//
//        /* Create a vector that contains the value of the swing.installedlafs
//         * property.  For example given "swing.installedlafs=motif,windows"
//         * lafs = {"motif", "windows"}.
//         */
//        Vector lafs = new Vector();
//        StringTokenizer st = new StringTokenizer(ilafsString, ",", false);
//        while (st.hasMoreTokens()) {
//            lafs.addElement(st.nextToken());
//        }
//
//        /* Look up the name and class for each name in the "swing.installedlafs"
//         * list.  If they both exist then add a LookAndFeelInfo to
//         * the installedLafs array.
//         */
//        Vector ilafs = new Vector(lafs.size());
//        for(int i = 0; i < lafs.size(); i++) {
//            String laf = (String)lafs.elementAt(i);
//            String name = swingProps.getProperty(makeInstalledLAFKey(laf, "name"), laf);
//            String cls = swingProps.getProperty(makeInstalledLAFKey(laf, "class"));
//            if (cls != null) {
//                ilafs.addElement(new LookAndFeelInfo(name, cls));
//            }
//        }
//
//        LookAndFeelInfo[] installedLAFs = new LookAndFeelInfo[ilafs.size()];
//        for(int i = 0; i < ilafs.size(); i++) {
//            installedLAFs[i] = (LookAndFeelInfo)(ilafs.elementAt(i));
//        }
//        getLAFState().installedLAFs = installedLAFs;
//    }
//
//
//    /**
//     * If the user has specified a default look and feel, use that.
//     * Otherwise use the look and feel that's native to this platform.
//     * If this code is called after the application has explicitly
//     * set it's look and feel, do nothing.
//     *
//     * @see #maybeInitialize
//     */
//    private static void initializeDefaultLAF(Properties swingProps)
//    {
//        if (getLAFState().lookAndFeel != null) {
//            return;
//        }
//
//        String metalLnf = getCrossPlatformLookAndFeelClassName();
//        String lnfDefault = metalLnf;
//
//        String lnfName = "<undefined>" ;
//        try {
//            lnfName = swingProps.getProperty(defaultLAFKey, lnfDefault);
//            setLookAndFeel(lnfName);
//        } catch (Exception e) {
//            try {
//                lnfName = swingProps.getProperty(defaultLAFKey, metalLnf);
//                setLookAndFeel(lnfName);
//            } catch (Exception e2) {
//                throw new Error("can't load " + lnfName);
//            }
//        }
//    }
//
//
//    private static void initializeAuxiliaryLAFs(Properties swingProps)
//    {
//        String auxLookAndFeelNames = swingProps.getProperty(auxiliaryLAFsKey);
//        if (auxLookAndFeelNames == null) {
//            return;
//        }
//
//        Vector auxLookAndFeels = new Vector();
//
//        StringTokenizer p = new StringTokenizer(auxLookAndFeelNames,",");
//        String factoryName;
//
//        /* Try to load each LookAndFeel subclass in the list.
//         */
//
//        while (p.hasMoreTokens()) {
//            String className = p.nextToken();
//            try {
//                Class lnfClass = SwingUtilities.loadSystemClass(className);
//                LookAndFeel newLAF = (LookAndFeel)lnfClass.newInstance();
//                newLAF.initialize();
//                auxLookAndFeels.addElement(newLAF);
//            }
//            catch (Exception e) {
//                System.err.println("UIManager: failed loading auxiliary look and feel " + className);
//            }
//        }
//
//        /* If there were problems and no auxiliary look and feels were
//         * loaded, make sure we reset auxLookAndFeels to null.
//         * Otherwise, we are going to use the MultiLookAndFeel to get
//         * all component UI's, so we need to load it now.
//         */
//        if (auxLookAndFeels.size() == 0) {
//            auxLookAndFeels = null;
//        }
//        else {
//            getLAFState().multiLookAndFeel = getMultiLookAndFeel();
//            if (getLAFState().multiLookAndFeel == null) {
//                auxLookAndFeels = null;
//            }
//        }
//
//        getLAFState().auxLookAndFeels = auxLookAndFeels;
//    }
//
//
//    private static void initializeSystemDefaults(Properties swingProps) {
//        getLAFState().swingProps = swingProps;
//    }
//
//

    
private static UIDefaults uid;

    /*
     * This method is called before any code that depends on the
     * <code>AppContext</code> specific LAFState object runs.  When the AppContext
     * corresponds to a set of applets it's possible for this method
     * to be re-entered, which is why we grab a lock before calling
     * initialize().
     */
    private static void maybeInitialize() {
    	if (uid == null) {
    		uid = JSToolkit.getLookAndFeelDefaults();
    		initialize();
    	}
//        synchronized (classLock) {
//            if (!getLAFState().initialized) {
//                getLAFState().initialized = true;
//                initialize();
//            }
//        }
    }


    /*
     * Only called by maybeInitialize().
     */
    private static void initialize() {
// SwingJS 				Properties swingProps = loadSwingProperties();
//				initializeSystemDefaults(swingProps);
//				initializeDefaultLAF(swingProps);
//				initializeAuxiliaryLAFs(swingProps);
//				initializeInstalledLAFs(swingProps);
//
//        // Enable the Swing default LayoutManager.
//        String toolkitName = Toolkit.getDefaultToolkit().getClass().getName();
//        // don't set default policy if this is XAWT.
//        if (!"jssun.awt.X11.XToolkit".equals(toolkitName)) {
//            if (FocusManager.isFocusManagerEnabled()) {
//                KeyboardFocusManager.getCurrentKeyboardFocusManager().
//                    setDefaultFocusTraversalPolicy(
//                        new LayoutFocusTraversalPolicy());
//            }
//        }

        // Install Swing's PaintEventDispatcher
        if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
        	// SwingJS : We set this flag true
            jssun.awt.PaintEventDispatcher.setPaintEventDispatcher(
                                        new SwingPaintEventDispatcher());
        }
        // Install a hook that will be invoked if no one consumes the
        // KeyEvent.  If the source isn't a JComponent this will process
        // key bindings, if the source is a JComponent it implies that
        // processKeyEvent was already invoked and thus no need to process
        // the bindings again, unless the Component is disabled, in which
        // case KeyEvents will no longer be dispatched to it so that we
        // handle it here.
// SwingJS         KeyboardFocusManager.getCurrentKeyboardFocusManager().
//                addKeyEventPostProcessor(new KeyEventPostProcessor() {
//                    public boolean postProcessKeyEvent(KeyEvent e) {
//                        Component c = e.getComponent();
//
//                        if ((!(c instanceof JComponent) ||
//                             (c != null && !((JComponent)c).isEnabled())) &&
//                                JComponent.KeyboardState.shouldProcess(e) &&
//                                SwingUtilities.processKeyBindings(e)) {
//                            e.consume();
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//        try {
//            Method setRequestFocusControllerM = jsjava.security.AccessController.doPrivileged(
//                    new jsjava.security.PrivilegedExceptionAction<Method>() {
//                        public Method run() throws Exception {
//                            Method method =
//                            Component.class.getDeclaredMethod("setRequestFocusController",
//                                                              jssun.awt.RequestFocusController.class);
//                            method.setAccessible(true);
//                            return method;
//                        }
//                    });
//            setRequestFocusControllerM.invoke(null, JComponent.focusController);
//        } catch (Exception e) {
//            // perhaps we should log this
//            assert false;
//        }
    }
}
