/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 2000, 2004, Oracle and/or its affiliates. All rights reserved.
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

package jssun.awt.datatransfer;

import java.io.IOException;

import jsjava.awt.datatransfer.DataFlavor;
import jsjava.awt.datatransfer.Transferable;
import jsjava.awt.datatransfer.UnsupportedFlavorException;


/**
 * Proxies for another Transferable so that Serializable objects are never
 * returned directly by DnD or the Clipboard. Instead, a new instance of the
 * object is returned.
 *
 * @author Lawrence P.G. Cable
 * @author David Mendenhall
 *
 * @since 1.4
 */
public class TransferableProxy implements Transferable {
    public TransferableProxy(Transferable t, boolean local) {
        transferable = t;
        isLocal = local;
    }
    @Override
		public DataFlavor[] getTransferDataFlavors() {
        return transferable.getTransferDataFlavors();
    }
    @Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
        return transferable.isDataFlavorSupported(flavor);
    }
    @Override
		public Object getTransferData(DataFlavor df)
        throws UnsupportedFlavorException, IOException
    {
        Object data = transferable.getTransferData(df);

//        // If the data is a Serializable object, then create a new instance
//        // before returning it. This insulates applications sharing DnD and
//        // Clipboard data from each other.
//        if (data != null && isLocal && df.isFlavorSerializedObjectType()) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            ClassLoaderObjectOutputStream oos =
//                new ClassLoaderObjectOutputStream(baos);
//            oos.writeObject(data);
//
//            ByteArrayInputStream bais =
//                new ByteArrayInputStream(baos.toByteArray());
//
//            try {
//                ClassLoaderObjectInputStream ois =
//                    new ClassLoaderObjectInputStream(bais,
//                                                     oos.getClassLoaderMap());
//                data = ois.readObject();
//            } catch (ClassNotFoundException cnfe) {
//                throw (IOException)new IOException().initCause(cnfe);
//            }
//        }

        return data;
    }

    protected final Transferable transferable;
    protected final boolean isLocal;
}

//class ClassLoaderObjectOutputStream extends ObjectOutputStream {
//    private final Map<Set<String>, ClassLoader> map =
//        new HashMap<Set<String>, ClassLoader>();
//
//    public ClassLoaderObjectOutputStream(OutputStream os) throws IOException {
//        super(os);
//    }
//
//    @Override
//		protected void annotateClass(final Class<?> cl) throws IOException {
//        ClassLoader classLoader =
//            (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
//                @Override
//								public Object run() {
//                    return cl.getClassLoader();
//                }
//            });
//
//        Set<String> s = new HashSet<String>(1);
//        s.add(cl.getName());
//
//        map.put(s, classLoader);
//    }
//    @Override
//		protected void annotateProxyClass(final Class<?> cl) throws IOException {
//        ClassLoader classLoader =
//            (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
//                @Override
//								public Object run() {
//                    return cl.getClassLoader();
//                }
//            });
//
//        Class[] interfaces = cl.getInterfaces();
//        Set<String> s = new HashSet<String>(interfaces.length);
//        for (int i = 0; i < interfaces.length; i++) {
//            s.add(interfaces[i].getName());
//        }
//
//        map.put(s, classLoader);
//    }
//
//    public Map<Set<String>, ClassLoader> getClassLoaderMap() {
//        return new HashMap(map);
//    }
//}

//class ClassLoaderObjectInputStream extends ObjectInputStream {
//    private final Map<Set<String>, ClassLoader> map;
//
//    public ClassLoaderObjectInputStream(InputStream is,
//                                        Map<Set<String>, ClassLoader> map)
//      throws IOException {
//        super(is);
//        if (map == null) {
//            throw new NullPointerException("Null map");
//        }
//        this.map = map;
//    }
//
//    protected Class<?> resolveClass(ObjectStreamClass classDesc)
//      throws IOException, ClassNotFoundException {
//        String className = classDesc.getName();
//
//        Set<String> s = new HashSet<String>(1);
//        s.add(className);
//
//        ClassLoader classLoader = map.get(s);
//
//        return Class.forName(className, false, classLoader);
//    }
//
//    protected Class<?> resolveProxyClass(String[] interfaces)
//      throws IOException, ClassNotFoundException {
//
//        Set<String> s = new HashSet<String>(interfaces.length);
//        for (int i = 0; i < interfaces.length; i++) {
//            s.add(interfaces[i]);
//        }
//
//        ClassLoader classLoader = map.get(s);
//
//        // The code below is mostly copied from the superclass.
//        ClassLoader nonPublicLoader = null;
//        boolean hasNonPublicInterface = false;
//
//        // define proxy in class loader of non-public interface(s), if any
//        Class[] classObjs = new Class[interfaces.length];
//        for (int i = 0; i < interfaces.length; i++) {
//            Class cl = Class.forName(interfaces[i], false, classLoader);
//            if ((cl.getModifiers() & Modifier.PUBLIC) == 0) {
//                if (hasNonPublicInterface) {
//                    if (nonPublicLoader != cl.getClassLoader()) {
//                        throw new IllegalAccessError(
//                            "conflicting non-public interface class loaders");
//                    }
//                } else {
//                    nonPublicLoader = cl.getClassLoader();
//                    hasNonPublicInterface = true;
//                }
//            }
//            classObjs[i] = cl;
//        }
//        try {
//            return Proxy.getProxyClass(hasNonPublicInterface ?
//                                       nonPublicLoader : classLoader,
//                                       classObjs);
//        } catch (IllegalArgumentException e) {
//            throw new ClassNotFoundException(null, e);
//        }
//    }
//}
