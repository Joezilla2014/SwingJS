/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1997, 2007, Oracle and/or its affiliates. All rights reserved.
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

package jssun.awt.image;

import swingjs.JSToolkit;
import jsjava.awt.geom.AffineTransform;
import jsjava.awt.image.AffineTransformOp;
import jsjava.awt.image.BufferedImage;
import jsjava.awt.image.BufferedImageOp;
import jsjava.awt.image.ByteLookupTable;
import jsjava.awt.image.ConvolveOp;
import jsjava.awt.image.Kernel;
import jsjava.awt.image.LookupOp;
import jsjava.awt.image.LookupTable;
import jsjava.awt.image.RasterOp;
import jsjava.awt.image.Raster;
import jsjava.awt.image.WritableRaster;
import jsjava.security.AccessController;
import jsjava.security.PrivilegedAction;

/**
 * This class provides a hook to access platform-specific
 * imaging code.
 *
 * If the implementing class cannot handle the op, tile format or
 * image format, the method will return null;
 * If there is an error when processing the
 * data, the implementing class may either return null
 * (in which case our java code will be executed) or may throw
 * an exception.
 */
public class ImagingLib {

//    static boolean useLib = true;
    static boolean verbose = false;

    private static final int NUM_NATIVE_OPS = 3;
    private static final int LOOKUP_OP   = 0;
    private static final int AFFINE_OP   = 1;
    private static final int CONVOLVE_OP = 2;

//    private static Class[] nativeOpClass = new Class[NUM_NATIVE_OPS];

//    /**
//     * Returned value indicates whether the library initailization was
//     * succeded.
//     *
//     * There could be number of reasons to failure:
//     * - failed to load library.
//     * - failed to get all required entry points.
//     */
//    private static native boolean init();
//
//    static public native int transformBI(BufferedImage src, BufferedImage dst,
//                                         double[] matrix, int interpType);
//    static public native int transformRaster(Raster src, Raster dst,
//                                             double[] matrix,
//                                             int interpType);
//    static public native int convolveBI(BufferedImage src, BufferedImage dst,
//                                        Kernel kernel, int edgeHint);
//    static public native int convolveRaster(Raster src, Raster dst,
//                                            Kernel kernel, int edgeHint);
//    static public native int lookupByteBI(BufferedImage src, BufferedImage dst,
//                                        byte[][] table);
//    static public native int lookupByteRaster(Raster src, Raster dst,
//                                              byte[][] table);
//
//    static {
//
//        PrivilegedAction<Boolean> doMlibInitialization =
//            new PrivilegedAction<Boolean>() {
//                public Boolean run() {
//                    String arch = System.getProperty("os.arch");
//
//                    if (arch == null || !arch.startsWith("sparc")) {
//                        try {
//                            System.loadLibrary("mlib_image");
//                        } catch (UnsatisfiedLinkError e) {
//                            return Boolean.FALSE;
//                        }
//
//                    }
//                    boolean success =  init();
//                    return Boolean.valueOf(success);
//                }
//            };
//
//        useLib = AccessController.doPrivileged(doMlibInitialization);
//
//        //
//        // Cache the class references of the operations we know about
//        // at the time this class is initially loaded.
//        //
//        try {
//            nativeOpClass[LOOKUP_OP] =
//                Class.forName("java.awt.image.LookupOp");
//        } catch (ClassNotFoundException e) {
//            System.err.println("Could not find class: "+e);
//        }
//        try {
//            nativeOpClass[AFFINE_OP] =
//                Class.forName("java.awt.image.AffineTransformOp");
//        } catch (ClassNotFoundException e) {
//            System.err.println("Could not find class: "+e);
//        }
//        try {
//            nativeOpClass[CONVOLVE_OP] =
//                Class.forName("java.awt.image.ConvolveOp");
//        } catch (ClassNotFoundException e) {
//            System.err.println("Could not find class: "+e);
//        }
//
//    }
//
//    private static int getNativeOpIndex(Class opClass) {
//        //
//        // Search for this class in cached list of
//        // classes supplying native acceleration
//        //
//        int opIndex = -1;
//        for (int i=0; i<NUM_NATIVE_OPS; i++) {
//            if (opClass == nativeOpClass[i]) {
//                opIndex = i;
//                break;
//            }
//        }
//        return opIndex;
//    }
//
//
    public static WritableRaster filter(RasterOp op, Raster src,
                                        WritableRaster dst) {
    // Create the destination tile
    if (dst == null) {
        dst = op.createCompatibleDestRaster(src);
    }
    	return JSToolkit.filterRaster(src, dst, op);
////        if (useLib == false) {
////            return null;
////        }
//
//        // Create the destination tile
//        if (dst == null) {
//            dst = op.createCompatibleDestRaster(src);
//        }
//        WritableRaster retRaster = null;
//        int type = 0;
//        /**
//         * @j2s
//         */
//        switch (getNativeOpIndex(op.getClass())) {
//
//          case LOOKUP_OP:
//            // REMIND: Fix this!
//            LookupTable table = ((LookupOp)op).getTable();
//            if (table.getOffset() != 0) {
//                // Right now the native code doesn't support offsets
//                return null;
//            }
//            if (table instanceof ByteLookupTable) {
//                ByteLookupTable bt = (ByteLookupTable) table;
//                if (lookupByteRaster(src, dst, bt.getTable()) > 0) {
//                    retRaster = dst;
//                }
//            }
//            break;
//
//          case AFFINE_OP:
//            AffineTransformOp bOp = (AffineTransformOp) op;
//            double[] matrix = new double[6];
//            bOp.getTransform().getMatrix(matrix);
//            if (transformRaster(src, dst, matrix,
//                                bOp.getInterpolationType()) > 0) {
//                retRaster =  dst;
//            }
//            break;
//
//          case CONVOLVE_OP:
//            ConvolveOp cOp = (ConvolveOp) op;
//            if (convolveRaster(src, dst,
//                               cOp.getKernel(), cOp.getEdgeCondition()) > 0) {
//                retRaster = dst;
//            }
//            break;
//
//          default:
//            break;
//        }
//
//        if (retRaster != null) {
//            SunWritableRaster.markDirty(retRaster);
//        }
//
//        return retRaster;
    }


    public static BufferedImage filter(BufferedImageOp op, BufferedImage src,
                                       BufferedImage dst)
    {
    	
		// Called by java.awt.image.FooOp
    	
        if (verbose) {
            System.out.println("in filter and op is "+op
                               + "bufimage is "+src+" and "+dst);
        }

//        if (useLib == false) {
//            return null;
//        }

        // Create the destination image
        if (dst == null) {
            dst = op.createCompatibleDestImage(src, null);
        }
        
        return JSToolkit.filterImage(src, dst, op);

//        BufferedImage retBI = null;
//        switch (getNativeOpIndex(op.getClass())) {
//
//          case LOOKUP_OP:
//            // REMIND: Fix this!
//            LookupTable table = ((LookupOp)op).getTable();
//            if (table.getOffset() != 0) {
//                // Right now the native code doesn't support offsets
//                return null;
//            }
//            if (table instanceof ByteLookupTable) {
//                ByteLookupTable bt = (ByteLookupTable) table;
//                if (lookupByteBI(src, dst, bt.getTable()) > 0) {
//                    retBI = dst;
//                }
//            }
//            break;
//
//          case AFFINE_OP:
//            AffineTransformOp bOp = (AffineTransformOp) op;
//            double[] matrix = new double[6];
//            AffineTransform xform = bOp.getTransform();
//            bOp.getTransform().getMatrix(matrix);
//
//            if (transformBI(src, dst, matrix,
//                            bOp.getInterpolationType())>0) {
//                retBI = dst;
//            }
//            break;
//
//          case CONVOLVE_OP:
//            ConvolveOp cOp = (ConvolveOp) op;
//            if (convolveBI(src, dst, cOp.getKernel(),
//                           cOp.getEdgeCondition()) > 0) {
//                retBI = dst;
//            }
//            break;
//
//          default:
//            break;
//        }
//
//        if (retBI != null) {
//            SunWritableRaster.markDirty(retBI);
//        }
//
//        return retBI;
    }
}
