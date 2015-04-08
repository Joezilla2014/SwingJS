package swingjs;

import java.net.URL;


import jsjava.awt.Dimension;
import jsjava.awt.EventQueue;
import jsjava.awt.Font;
import jsjava.awt.FontMetrics;
import jsjava.awt.Image;
import jsjava.awt.Dialog.ModalExclusionType;
import jsjava.awt.Dialog.ModalityType;
import jsjava.awt.Window;
import jsjava.awt.image.ColorModel;
import jsjava.awt.image.ImageObserver;
import jsjava.awt.image.ImageProducer;
import jssun.awt.SunToolkit;


public class JSToolkit extends SunToolkit {

	/*
	 * NOTE: This class is called from jsjava.awt.Toolkit
	 * within in j2sNative block.
	 * 
	 */
	
	public JSToolkit(){
		System.out.println("JSToolkit initialized");		
	}

	//////// jsjava.awt.Toolkit /////////

	@Override
	public Dimension getScreenSize() {
		Dimension d = new Dimension(0, 0);
		/**
		 * @j2sNative
		 * 
		 * d.setSize(Jmol.$(window).width(), Jmol.$(window).height());
		 * return d;
		 */
		{
			return d;
		}
	}


	@Override
	public int getScreenResolution() {
		// n/a
		return 0;
	}


	@Override
	public ColorModel getColorModel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	@Deprecated
	public String[] getFontList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	@Deprecated
	public FontMetrics getFontMetrics(Font font) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void sync() {
		// n/a?
		
	}


	@Override
	public Image getImage(String filename) {
		// TODO Auto-generated method stub
		return getImageObj(filename);
	}


	@Override
	public Image getImage(URL url) {
		return getImageObj(url);
	}

  private Image getImageObj(Object nameOrURL) {
		// send off to get this
		return null;
	}

	@Override
	public Image createImage(String filename) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Image createImage(URL url) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean prepareImage(Image image, int width, int height,
			ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int checkImage(Image image, int width, int height,
			ImageObserver observer) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Image createImage(ImageProducer producer) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Image createImage(byte[] imagedata, int imageoffset, int imagelength) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected EventQueue getSystemEventQueueImpl() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isModalityTypeSupported(ModalityType modalityType) {
		// TODO Auto-generated method stub
		return false;
	}

	//////// sun.awt.SunToolkit /////////

	@Override
	public boolean isModalExclusionTypeSupported(
			ModalExclusionType modalExclusionType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTraySupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getScreenWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getScreenHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean syncNativeQueue(long timeout) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void grab(Window w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ungrab(Window w) {
		// TODO Auto-generated method stub
		
	}

	public static Object getPropertyObject(Object t, String key, Object def) {
		/**
		 * @j2sNative
		 * switch (key) {
		 * case "graphics":
		 *   return SwingJS.getGraphics(t)
		 *   break;
		 * }
		 * 
		 */
		{}
		return def;
	}
	
	
	
	
	
}
