package swingjs;

import swingjs.api.DOMNode;
import jsjava.awt.AlphaComposite;
import jsjava.awt.Graphics2D;
import jsjava.awt.Image;
import jsjava.awt.Point;
import jsjava.awt.geom.AffineTransform;
import jsjava.awt.geom.Point2D;
import jsjava.awt.image.AffineTransformOp;
import jsjava.awt.image.BufferedImage;
import jsjava.awt.image.BufferedImageOp;
import jsjava.awt.image.ByteLookupTable;
import jsjava.awt.image.ConvolveOp;
import jsjava.awt.image.Kernel;
import jsjava.awt.image.LookupOp;
import jsjava.awt.image.LookupTable;
import jsjava.awt.image.Raster;
import jsjava.awt.image.RasterOp;
import jsjava.awt.image.RescaleOp;
import jsjava.awt.image.SampleModel;
import jsjava.awt.image.WritableRaster;
import jssun.awt.image.SunWritableRaster;

public class JSGraphicsCompositor {


	private static double[] mat6;

	JSGraphicsCompositor() {
		// for reflection
	}
	/**
	 * apply a source/destination rule to a canvas.context2d object
	 * 
	 * see https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/globalCompositeOperation
	 * 
	 * 
	 * @param g
	 * @param alphaRule
	 * @return
	 */
	public boolean setGraphicsCompositeAlpha(JSGraphics2D g, int alphaRule) {
		// source-over This is the default setting and draws new shapes on top of
		// the existing canvas content.
		// source-in The new shape is drawn only where both the new shape and the
		// destination canvas overlap. Everything else is made transparent.
		// source-out The new shape is drawn where it doesn't overlap the existing
		// canvas content.
		// source-atop The new shape is only drawn where it overlaps the existing
		// canvas content.
		// lighter Where both shapes overlap the color is determined by adding color
		// values.
		// xor Shapes are made transparent where both overlap and drawn normal
		// everywhere else.
		// destination-over New shapes are drawn behind the existing canvas content.
		// destination-in The existing canvas content is kept where both the new
		// shape and existing canvas content overlap. Everything else is made
		// transparent.
		// destination-out The existing content is kept where it doesn't overlap the
		// new shape.
		// destination-atop The existing canvas is only kept where it overlaps the
		// new shape. The new shape is drawn behind the canvas content.
		// darker Where both shapes overlap the color is determined by subtracting
		// color values.
	

		@SuppressWarnings("unused")
		String s = null;
		switch (alphaRule) {
		default:
		case AlphaComposite.SRC_OVER:
			s = "source-over";
			break;
		case AlphaComposite.SRC_IN:
			s = "source-in";
			break;
		case AlphaComposite.SRC_OUT:
			s = "source-out";
			break;
		case AlphaComposite.SRC_ATOP:
			s = "source-atop";
			break;
		case AlphaComposite.XOR:
			s = "xor";
			break;
		case AlphaComposite.DST_OVER:
			s = "destination-over";
			break;
		case AlphaComposite.DST_IN:
			s = "destination-in";
			break;
		case AlphaComposite.DST_OUT:
			s = "destination-out";
			break;
		case AlphaComposite.DST_ATOP:
			s = "destination-atop";
			break;
		}
		/**
		 * @j2sNative
		 * 
		 *            g.ctx.globalCompositeOperation = s;
		 */
		{
		}
		return true;
	}

	/**
	 * apply a BufferedImageOp to an image enroute to
	 * 
	 * @param g
	 * @param img
	 * @param op
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean drawImageOp(JSGraphics2D g, BufferedImage img,
			BufferedImageOp op, int x, int y) {
		int type = 0;
		/**
		 * @j2sNative
		 * 
		 *            type = op.swingJStype;
		 * 
		 */
		{
		}
		switch (type) {

		case 'R':
			// HTML5 can only do simple alpha rescaling
			RescaleOp rop = (RescaleOp) op;
			float[] offsets = rop.offsets;
			float[] scaleFactors = rop.scaleFactors;
			boolean canDo = (offsets.length == 4 && offsets[3] == 0);
			if (canDo)
				for (int i = 0; i < 3; i++)
					if (offsets[i] != 0 || scaleFactors[i] != 1) {
						canDo = false;
						break;
					}
			if (canDo) {
				g.setAlpha(scaleFactors[3]);
				g.drawImagePriv(img, x, y, null);
				g.setAlpha(1);
				return true;
			}
			break;
		case 'L':
			// SwingJS TODO
			break;
		case 'A':
			// SwingJS TODO
			break;
		case 'C':
			// SwingJS TODO
			break;
		}
		return false;
	}

	public WritableRaster filterRaster(Raster src, WritableRaster dst, RasterOp op) {
		// Create the destination tile
		if (dst == null) {
			dst = op.createCompatibleDestRaster(src);
		}
		WritableRaster retRaster = null;
		int type = 0;
		/**
		 * @j2sNative
		 * 
		 *            type = op.swingJStype;
		 * 
		 */
		{
		}
		switch (type) {

		case 'L':
			LookupTable table = ((LookupOp) op).getTable();
			if (table instanceof ByteLookupTable) {
				ByteLookupTable bt = (ByteLookupTable) table;
				if (lookupByteRaster(src, dst, bt.getTable()) > 0) {
					retRaster = dst;
				}
			}
			break;

		case 'A':
			AffineTransformOp bOp = (AffineTransformOp) op;
			double[] matrix = (mat6 == null ? mat6 = new double[6] : mat6);
			bOp.getTransform().getMatrix(matrix);
			if (transformRaster(src, dst, matrix, bOp.getInterpolationType()) > 0) {
				retRaster = dst;
			}
			break;

		case 'C':
			ConvolveOp cOp = (ConvolveOp) op;
			if (convolveRaster(src, dst, cOp.getKernel(), cOp.getEdgeCondition()) > 0) {
				retRaster = dst;
			}
			break;

		default:
			break;
		}

		if (retRaster != null) {
			SunWritableRaster.markDirty(retRaster);
		}

		return retRaster;
	}

	private int convolveRaster(Raster src, WritableRaster dst, Kernel kernel,
			int edgeCondition) {
		// SwingJS TODO
		return 0;
	}

	private int transformRaster(Raster src, WritableRaster dst, double[] matrix,
			int interpolationType) {
		// SwingJS TODO
		return 0;
	}

	private int lookupByteRaster(Raster src, WritableRaster dst, byte[][] table) {
		// SwingJS TODO
		return 0;
	}

	public BufferedImage filterImage(BufferedImage src, BufferedImage dst,
			BufferedImageOp op) {
		BufferedImage retBI = null;
		int type = 0;
		/**
		 * @j2sNative
		 * 
		 *            type = op.swingJStype;
		 * 
		 */
		{
		}
		switch (type) {
		default:
			retBI = op.filter(src, dst);
			break;
		case 'A':
			((JSGraphics2D) dst.getGraphics()).drawImage(src, ((AffineTransformOp) op).getTransform(),
					null);
			retBI = dst;
			break;
		case 'L':
			// not implemented
			LookupTable table = ((LookupOp) op).getTable();
			if (table.getOffset() != 0) {
				// Right now the native code doesn't support offsets
				return null;
			}
			if (table instanceof ByteLookupTable) {
				ByteLookupTable bt = (ByteLookupTable) table;
				if (lookupByteBI(src, dst, bt.getTable()) > 0) {
					retBI = dst;
				}
			}
			break;
		case 'C':
			// not implemented
			ConvolveOp cOp = (ConvolveOp) op;
			if (convolveBI(src, dst, cOp.getKernel(), cOp.getEdgeCondition()) > 0) {
				retBI = dst;
			}
			break;
		}

		// if (retBI != null) {
		// SunWritableRaster.markDirty(retBI);
		// }
		//
		return retBI;
	}

	private int convolveBI(BufferedImage src, BufferedImage dst, Kernel kernel,
			int edgeCondition) {
		// SwingJS TODO
		return 0;
	}

	private int lookupByteBI(BufferedImage src, BufferedImage dst, byte[][] table) {
		// SwingJS TODO
		return 0;
	}
	
	/**
	 * Get or create a DOM image node, as needed.
	 * Images could be from:
	 * 
	 * a) java.awt.Toolkit.createImage()
	 * b) javax.imageio.ImageIO.read()
	 * c) new java.awt.BufferedImage()
	 * 
	 * 
	 *  
	 * @param img
	 * @return
	 */
	public DOMNode createImageNode(Image img) {
	  DOMNode imgNode = null;
	  /**
	   * @j2sNative
	   * 
	   * imgNode = img._imgNode;
	   * 
	   */
	  {}
		if (imgNode == null && img instanceof BufferedImage) {
		  /**
		   * @j2sNative
		   * 
		   * var canvas = img._canvas;
		   * if (canvas == null) {
		   *   img.getGraphics();
		   *   canvas = img._canvas;
		   * }
		   * imgNode = canvas;
		   * imgNode.style.width = img.getWidth() + "px";
		   * imgNode.style.height = img.getHeight()	 	+ "px";
		   * 
		   */
		  {}
		  // note: images created some other way are presumed to have int[] pix defined and possibly byte[pix]
		}
		return imgNode;
	}
}
