package swingjs.plaf;

import jsjava.awt.Component;
import jsjava.awt.Dimension;
import jsjava.awt.Graphics;
import jsjava.awt.Insets;
import jsjava.awt.Point;
import jsjava.awt.Rectangle;
import jsjava.awt.event.ActionEvent;
import jsjava.beans.PropertyChangeEvent;
import jsjava.beans.PropertyChangeListener;
import jsjavax.swing.BoundedRangeModel;
import jsjavax.swing.InputMap;
import jsjavax.swing.JComponent;
import jsjavax.swing.JScrollBar;
import jsjavax.swing.JScrollPane;
import jsjavax.swing.JViewport;
import jsjavax.swing.LookAndFeel;
import jsjavax.swing.Scrollable;
import jsjavax.swing.SwingConstants;
import jsjavax.swing.SwingUtilities;
import jsjavax.swing.UIManager;
import jsjavax.swing.border.Border;
import jsjavax.swing.event.ChangeEvent;
import jsjavax.swing.event.ChangeListener;
import jsjavax.swing.plaf.UIResource;
import jssun.swing.DefaultLookup;
import jssun.swing.UIAction;
import swingjs.api.DOMNode;

public class JSScrollPaneUI extends JSLightweightUI implements
		PropertyChangeListener, ChangeListener {

	/*
	 * This first implementation is an attempt to reproduce Java's JScrollPane
	 * with its associated separate ViewPort, View, vertical and horizontal
	 * JScrollBars.
	 * 
	 * It is only marginally successful and requires that the scrollbars be
	 * implemented as sliders, primarily because it is too difficult to do actual
	 * scrollbars. Or at least that is what I concluded based on looking at
	 * different browsers.
	 * 
	 * So now the way it works is that we allow the scrolled component ("view") to
	 * scroll itself. Somewhat less control, but more along the lines of letting
	 * the browser do its work when possible.
	 */

	private JComponent scrolledComponent;
	private JViewport viewport;

	private JSComponentUI scrolledUI;
	private JSScrollBarUI horizBarUI;
	private JSScrollBarUI vertBarUI;

	@Override
	protected DOMNode updateDOMNode() {
		isContainer = true;
		if (domNode == null) {
			domNode = newDOMObject("div", id);
		}
		// add code here for adjustments when changes in bounds or other properties
		// occur.
		return domNode;
	}

	// all are required:

	// children[0] viewport
	// children[1] vert scrollbar
	// children[2] horiz scrollbar

	boolean scrollBarUIDisabled = false;// set true for experimental only

	boolean jsSetViewPort() {
		// first time through -- could be a view, but not necessarily
		// need to find a listener for this
		JScrollBar hscrollbar = scrollpane.getHorizontalScrollBar();
		hscrollbar.addChangeListener(this);
		hscrollbar.addPropertyChangeListener(this);
		JScrollBar vscrollbar = scrollpane.getVerticalScrollBar();
		vscrollbar.addChangeListener(this);
		vscrollbar.addPropertyChangeListener(this);

		viewport = scrollpane.getViewport();
		JComponent sc = (JComponent) viewport.getView();
		// for whatever reason, the component being scrolled
		// in Java is referred to as the "view". This makes
		// no sense to me; I am calling it scrolledComponent.
		if (sc == null || sc.ui == null)
			return false;
		if (sc != scrolledComponent) {
			scrolledComponent = sc;
			// just experimenting  scrollBarUIDisabled = (sc.getUIClassID() == "TextAreaUI");
			scrolledUI = (JSComponentUI) sc.ui;
			scrolledUI.scrollPaneUI = this;
			scrollNode = scrolledUI.domNode; // why outer node?
			DOMNode.setSize(scrollNode, c.getWidth(), c.getHeight());
		}
		return true;
	}

	// @Override
	// protected void uninstallUIImpl() {
	// viewport.removeChangeListener(this);
	// viewport.removePropertyChangeListener(this);
	// JScrollBar hscrollbar = scrollpane.getHorizontalScrollBar();
	// if (hscrollbar != null)
	// hscrollbar.removePropertyChangeListener(this);
	// JScrollBar vscrollbar = scrollpane.getVerticalScrollBar();
	// if (vscrollbar != null)
	// vscrollbar.removePropertyChangeListener(this);
	// }

	@Override
	public void stateChanged(ChangeEvent e) {
		if (scrolledComponent == null && !jsSetViewPort())
			return;

		if (e.getSource() instanceof BoundedRangeModel && jsModelStateChanged(e)) {
			return;
		}


		getHandler().stateChanged(e);
	}

	private String getScrollBarPolicyCSS(int policy) {
		switch (policy % 10) {
		default:
		case 0: // as needed
			return "auto";
		case 1: // never
			return "none";
		case 2: // always
			return "scroll";
		}
	}

	@Override
	public Dimension getPreferredSize() {
		// System.out.println(id + " getpreferredSize");
		return null;
	}

	// //////////////////////////////////////// end SwingJS
	// ////////////////////////

	protected JScrollPane scrollpane;
	protected ChangeListener vsbChangeListener;
	protected ChangeListener hsbChangeListener;
	protected ChangeListener viewportChangeListener;
	protected PropertyChangeListener spPropertyChangeListener;
	// private MouseWheelListener mouseScrollListener;

	/**
	 * PropertyChangeListener installed on the vertical scrollbar.
	 */
	private PropertyChangeListener vsbPropertyChangeListener;

	/**
	 * PropertyChangeListener installed on the horizontal scrollbar.
	 */
	private PropertyChangeListener hsbPropertyChangeListener;

	private Handler handler;

	/**
	 * State flag that shows whether setValue() was called from a user program
	 * before the value of "extent" was set in right-to-left component
	 * orientation.
	 */
	boolean setValueCalled = false;

	// public static ComponentUI createUI(JComponent x) {
	// return new BasicScrollPaneUI();
	// }
	//
	
	static void loadActionMap(LazyActionMap map) {
		map.put(new Actions(Actions.SCROLL_UP));
		map.put(new Actions(Actions.SCROLL_DOWN));
		map.put(new Actions(Actions.SCROLL_HOME));
		map.put(new Actions(Actions.SCROLL_END));
		map.put(new Actions(Actions.UNIT_SCROLL_UP));
		map.put(new Actions(Actions.UNIT_SCROLL_DOWN));
		map.put(new Actions(Actions.SCROLL_LEFT));
		map.put(new Actions(Actions.SCROLL_RIGHT));
		map.put(new Actions(Actions.UNIT_SCROLL_RIGHT));
		map.put(new Actions(Actions.UNIT_SCROLL_LEFT));
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		checkTextAreaHeight();
		Border vpBorder = scrollpane.getViewportBorder();
		if (vpBorder != null) {
			Rectangle r = scrollpane.getViewportBorderBounds();
			vpBorder.paintBorder(scrollpane, g, r.x, r.y, r.width, r.height);
		}
	}

	private boolean haveTextArea = false;
	private Dimension textAreaSize;
	

	/**
	 * SwingJS: we need to make the scrollbar run the height of the text, not the size of the textarea itself 
	 */
	private void checkTextAreaHeight() {
		haveTextArea = false;
		JViewport vp = scrollpane.getViewport();
		if (vp == null)
			return;
		JScrollBar vsb = scrollpane.getVerticalScrollBar();
		JScrollBar hsb = scrollpane.getHorizontalScrollBar();
		if (vsb != null)
			((JSScrollBarUI) vsb.getUI()).setScrollPaneCSS();
		if (hsb != null)
			((JSScrollBarUI) hsb.getUI()).setScrollPaneCSS();
		JComponent sc = (JComponent) vp.getView();
		if (sc == null || sc.getUI() == null || sc.getUIClassID() != "TextAreaUI")
			return;
		haveTextArea = true;
		if (textAreaSize == null)
			textAreaSize = new Dimension();
		((JSTextAreaUI) sc.getUI()).getTextAreaTextSize(textAreaSize);
		
		boolean overHeight = textAreaSize.height > sc.getBounds().height; 
		boolean overWidth = textAreaSize.width > sc.getBounds().width; 
		if (vsb == null || !overHeight)
			textAreaSize.height = sc.getHeight();
		if (hsb == null || !overWidth)
			textAreaSize.width = sc.getWidth();
		sc.setSize(textAreaSize);
	}

	/**
	 * @return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)
	 */
	public Dimension getMaximumSize(JComponent c) {
		return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	}

	protected void installDefaults(JScrollPane scrollpane) {
		LookAndFeel.installBorder(scrollpane, "ScrollPane.border");
		LookAndFeel.installColorsAndFont(scrollpane, "ScrollPane.background",
				"ScrollPane.foreground", "ScrollPane.font");

		Border vpBorder = scrollpane.getViewportBorder();
		if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
			vpBorder = UIManager.getBorder("ScrollPane.viewportBorder");
			scrollpane.setViewportBorder(vpBorder);
		}
		LookAndFeel.installProperty(scrollpane, "opaque", Boolean.TRUE);
	}

	
	protected void installListeners(JScrollPane c) {
		vsbChangeListener = createVSBChangeListener();
		vsbPropertyChangeListener = createVSBPropertyChangeListener();
		hsbChangeListener = createHSBChangeListener();
		hsbPropertyChangeListener = createHSBPropertyChangeListener();
		viewportChangeListener = createViewportChangeListener();
		spPropertyChangeListener = createPropertyChangeListener();

		JViewport viewport = scrollpane.getViewport();
		JScrollBar vsb = scrollpane.getVerticalScrollBar();
		JScrollBar hsb = scrollpane.getHorizontalScrollBar();

		if (viewport != null) {
			viewport.addChangeListener(viewportChangeListener);
			viewport.addPropertyChangeListener(this); // SwingJS
		}
		
		horizBarUI = vertBarUI = null;
		
		if (vsb != null) {
			vsb.getModel().addChangeListener(vsbChangeListener);
			vsb.addPropertyChangeListener(vsbPropertyChangeListener);
		}
		if (hsb != null) {
			hsb.getModel().addChangeListener(hsbChangeListener);
			hsb.addPropertyChangeListener(hsbPropertyChangeListener);
		}

		scrollpane.addPropertyChangeListener(spPropertyChangeListener);

		// mouseScrollListener = createMouseWheelListener();
		// scrollpane.addMouseWheelListener(mouseScrollListener);

	}

	protected void installKeyboardActions(JScrollPane c) {
		InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		SwingUtilities.replaceUIInputMap(c,
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);

		LazyActionMap.installLazyActionMap(c, JSScrollPaneUI.class,
				"ScrollPane.actionMap");
	}

	InputMap getInputMap(int condition) {
		if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
			InputMap keyMap = (InputMap) DefaultLookup.get(scrollpane, this,
					"ScrollPane.ancestorInputMap");
			InputMap rtlKeyMap;

			if (scrollpane.getComponentOrientation().isLeftToRight()
					|| ((rtlKeyMap = (InputMap) DefaultLookup.get(scrollpane, this,
							"ScrollPane.ancestorInputMap.RightToLeft")) == null)) {
				return keyMap;
			} else {
				rtlKeyMap.setParent(keyMap);
				return rtlKeyMap;
			}
		}
		return null;
	}

	@Override
	public void installUIImpl() {
		scrollpane = (JScrollPane) jc;
		installDefaults(scrollpane);
		installListeners(scrollpane);
		installKeyboardActions(scrollpane);
	}

	protected void uninstallDefaults(JScrollPane c) {
		LookAndFeel.uninstallBorder(scrollpane);

		if (scrollpane.getViewportBorder() instanceof UIResource) {
			scrollpane.setViewportBorder(null);
		}
	}

	protected void uninstallListeners(JComponent c) {
		JViewport viewport = scrollpane.getViewport();
		JScrollBar vsb = scrollpane.getVerticalScrollBar();
		JScrollBar hsb = scrollpane.getHorizontalScrollBar();

		if (viewport != null) {
			viewport.removeChangeListener(viewportChangeListener);
		}
		if (vsb != null) {
			vsb.getModel().removeChangeListener(vsbChangeListener);
			vsb.removePropertyChangeListener(vsbPropertyChangeListener);
		}
		if (hsb != null) {
			hsb.getModel().removeChangeListener(hsbChangeListener);
			hsb.removePropertyChangeListener(hsbPropertyChangeListener);
		}

		scrollpane.removePropertyChangeListener(spPropertyChangeListener);

		// if (mouseScrollListener != null) {
		// scrollpane.removeMouseWheelListener(mouseScrollListener);
		// }

		vsbChangeListener = null;
		hsbChangeListener = null;
		viewportChangeListener = null;
		spPropertyChangeListener = null;
		// mouseScrollListener = null;
		handler = null;
	}

	protected void uninstallKeyboardActions(JScrollPane c) {
		SwingUtilities.replaceUIActionMap(c, null);
		SwingUtilities.replaceUIInputMap(c,
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);
	}

	@Override
	public void uninstallUIImpl() {
		uninstallDefaults(scrollpane);
		uninstallListeners(scrollpane);
		uninstallKeyboardActions(scrollpane);
		scrollpane = null;
	}

	Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}

	protected void syncScrollPaneWithViewport() {
		JViewport viewport = scrollpane.getViewport();
		JScrollBar vsb = scrollpane.getVerticalScrollBar();
		JScrollBar hsb = scrollpane.getHorizontalScrollBar();
		JViewport rowHead = scrollpane.getRowHeader();
		JViewport colHead = scrollpane.getColumnHeader();
		boolean ltr = scrollpane.getComponentOrientation().isLeftToRight();

		if (viewport != null) {
			Dimension extentSize = viewport.getExtentSize();
			Dimension viewSize = viewport.getViewSize();
			Point viewPosition = viewport.getViewPosition();

			if (vsb != null) {
				int extent = extentSize.height;
				int max = viewSize.height;
				int value = Math.max(0, Math.min(viewPosition.y, max - extent));
				vsb.setValues(value, extent, 0, max);
			}

			if (hsb != null) {
				int extent = extentSize.width;
				int max = viewSize.width;
				int value;

				if (ltr) {
					value = Math.max(0, Math.min(viewPosition.x, max - extent));
				} else {
					int currentValue = hsb.getValue();

					/*
					 * Use a particular formula to calculate "value" until effective x
					 * coordinate is calculated.
					 */
					if (setValueCalled && ((max - currentValue) == viewPosition.x)) {
						value = Math.max(0, Math.min(max - extent, currentValue));
						/*
						 * After "extent" is set, turn setValueCalled flag off.
						 */
						if (extent != 0) {
							setValueCalled = false;
						}
					} else {
						if (extent > max) {
							viewPosition.x = max - extent;
							viewport.setViewPosition(viewPosition);
							value = 0;
						} else {
							/*
							 * The following line can't handle a small value of viewPosition.x
							 * like Integer.MIN_VALUE correctly because (max - extent -
							 * viewPositoiin.x) causes an overflow. As a result, value becomes
							 * zero. (e.g. setViewPosition(Integer.MAX_VALUE, ...) in a user
							 * program causes a overflow. Its expected value is (max -
							 * extent).) However, this seems a trivial bug and adding a fix
							 * makes this often-called method slow, so I'll leave it until
							 * someone claims.
							 */
							value = Math.max(0,
									Math.min(max - extent, max - extent - viewPosition.x));
						}
					}
				}
				hsb.setValues(value, extent, 0, max);
			}

			if (rowHead != null) {
				Point p = rowHead.getViewPosition();
				p.y = viewport.getViewPosition().y;
				p.x = 0;
				rowHead.setViewPosition(p);
			}

			if (colHead != null) {
				Point p = colHead.getViewPosition();
				if (ltr) {
					p.x = viewport.getViewPosition().x;
				} else {
					p.x = Math.max(0, viewport.getViewPosition().x);
				}
				p.y = 0;
				colHead.setViewPosition(p);
			}
		}
	}

	/**
	 * Returns the baseline.
	 * 
	 * @throws NullPointerException
	 *           {@inheritDoc}
	 * @throws IllegalArgumentException
	 *           {@inheritDoc}
	 * @see javax.swing.JComponent#getBaseline(int, int)
	 * @since 1.6
	 */
	@Override
	public int getBaseline(JComponent c, int width, int height) {
		JViewport viewport = scrollpane.getViewport();
		Insets spInsets = scrollpane.getInsets();
		int y = spInsets.top;
		height = height - spInsets.top - spInsets.bottom;
		width = width - spInsets.left - spInsets.right;
		JViewport columnHeader = scrollpane.getColumnHeader();
		if (columnHeader != null && columnHeader.isVisible()) {
			Component header = columnHeader.getView();
			if (header != null && header.isVisible()) {
				// Header is always given it's preferred size.
				Dimension headerPref = header.getPreferredSize();
				int baseline = header.getBaseline(headerPref.width, headerPref.height);
				if (baseline >= 0) {
					return y + baseline;
				}
			}
			Dimension columnPref = columnHeader.getPreferredSize();
			height -= columnPref.height;
			y += columnPref.height;
		}
		Component view = (viewport == null) ? null : viewport.getView();
		if (view != null
				&& view.isVisible()
				&& view.getBaselineResizeBehavior() == Component.BaselineResizeBehavior.CONSTANT_ASCENT) {
			Border viewportBorder = scrollpane.getViewportBorder();
			if (viewportBorder != null) {
				Insets vpbInsets = viewportBorder.getBorderInsets(scrollpane);
				y += vpbInsets.top;
				height = height - vpbInsets.top - vpbInsets.bottom;
				width = width - vpbInsets.left - vpbInsets.right;
			}
			if (view.getWidth() > 0 && view.getHeight() > 0) {
				Dimension min = view.getMinimumSize();
				width = Math.max(min.width, view.getWidth());
				height = Math.max(min.height, view.getHeight());
			}
			if (width > 0 && height > 0) {
				int baseline = view.getBaseline(width, height);
				if (baseline > 0) {
					return y + baseline;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns an enum indicating how the baseline of the component changes as the
	 * size changes.
	 * 
	 * @throws NullPointerException
	 *           {@inheritDoc}
	 * @see javax.swing.JComponent#getBaseline(int, int)
	 * @since 1.6
	 */
	@Override
	public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
		super.getBaselineResizeBehavior(c);
		// Baseline is either from the header, in which case it's always
		// the same size and therefor can be created as CONSTANT_ASCENT.
		// If the header doesn't have a baseline than the baseline will only
		// be valid if it's BaselineResizeBehavior is
		// CONSTANT_ASCENT, so, return CONSTANT_ASCENT.
		return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
	}

	/**
	 * Listener for viewport events.
	 */
	public class ViewportChangeHandler implements ChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void stateChanged(ChangeEvent e) {
			getHandler().stateChanged(e);
		}
	}

	protected ChangeListener createViewportChangeListener() {
		return getHandler();
	}

	/**
	 * Horizontal scrollbar listener.
	 */
	public class HSBChangeListener implements ChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void stateChanged(ChangeEvent e) {
			getHandler().stateChanged(e);
		}
	}

	/**
	 * Returns a <code>PropertyChangeListener</code> that will be installed on the
	 * horizontal <code>JScrollBar</code>.
	 */
	private PropertyChangeListener createHSBPropertyChangeListener() {
		return getHandler();
	}

	protected ChangeListener createHSBChangeListener() {
		return getHandler();
	}

	/**
	 * Vertical scrollbar listener.
	 */
	public class VSBChangeListener implements ChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void stateChanged(ChangeEvent e) {
			getHandler().stateChanged(e);
		}
	}

	/**
	 * Returns a <code>PropertyChangeListener</code> that will be installed on the
	 * vertical <code>JScrollBar</code>.
	 */
	private PropertyChangeListener createVSBPropertyChangeListener() {
		return getHandler();
	}

	protected ChangeListener createVSBChangeListener() {
		return getHandler();
	}

	// /**
	// * MouseWheelHandler is an inner class which implements the
	// * MouseWheelListener interface. MouseWheelHandler responds to
	// * MouseWheelEvents by scrolling the JScrollPane appropriately.
	// * If the scroll pane's
	// * <code>isWheelScrollingEnabled</code>
	// * method returns false, no scrolling occurs.
	// *
	// * @see javax.swing.JScrollPane#isWheelScrollingEnabled
	// * @see #createMouseWheelListener
	// * @see java.awt.event.MouseWheelListener
	// * @see java.awt.event.MouseWheelEvent
	// * @since 1.4
	// */
	// protected class MouseWheelHandler implements MouseWheelListener {
	//
	// // NOTE: This class exists only for backward compatability. All
	// // its functionality has been moved into Handler. If you need to add
	// // new functionality add it to the Handler, but make sure this
	// // class calls into the Handler.
	//
	// /**
	// * Called when the mouse wheel is rotated while over a
	// * JScrollPane.
	// *
	// * @param e MouseWheelEvent to be handled
	// * @since 1.4
	// */
	// @Override
	// public void mouseWheelMoved(MouseWheelEvent e) {
	// getHandler().mouseWheelMoved(e);
	// }
	// }

	// /**
	// * Creates an instance of MouseWheelListener, which is added to the
	// * JScrollPane by installUI(). The returned MouseWheelListener is used
	// * to handle mouse wheel-driven scrolling.
	// *
	// * @return MouseWheelListener which implements wheel-driven scrolling
	// * @see #installUI
	// * @see MouseWheelHandler
	// * @since 1.4
	// */
	// protected MouseWheelListener createMouseWheelListener() {
	// return getHandler();
	// }

	protected void updateScrollBarDisplayPolicy(PropertyChangeEvent e) {
		scrollpane.revalidate();
		scrollpane.repaint();
	}

	protected void updateViewport(PropertyChangeEvent e) {
		JViewport oldViewport = (JViewport) (e.getOldValue());
		JViewport newViewport = (JViewport) (e.getNewValue());

		if (oldViewport != null) {
			oldViewport.removeChangeListener(viewportChangeListener);
		}

		if (newViewport != null) {
			Point p = newViewport.getViewPosition();
			if (scrollpane.getComponentOrientation().isLeftToRight()) {
				p.x = Math.max(p.x, 0);
			} else {
				int max = newViewport.getViewSize().width;
				int extent = newViewport.getExtentSize().width;
				if (extent > max) {
					p.x = max - extent;
				} else {
					p.x = Math.max(0, Math.min(max - extent, p.x));
				}
			}
			p.y = Math.max(p.y, 0);
			newViewport.setViewPosition(p);
			newViewport.addChangeListener(viewportChangeListener);
		}
	}

	protected void updateRowHeader(PropertyChangeEvent e) {
		JViewport newRowHead = (JViewport) (e.getNewValue());
		if (newRowHead != null) {
			JViewport viewport = scrollpane.getViewport();
			Point p = newRowHead.getViewPosition();
			p.y = (viewport != null) ? viewport.getViewPosition().y : 0;
			newRowHead.setViewPosition(p);
		}
	}

	protected void updateColumnHeader(PropertyChangeEvent e) {
		JViewport newColHead = (JViewport) (e.getNewValue());
		if (newColHead != null) {
			JViewport viewport = scrollpane.getViewport();
			Point p = newColHead.getViewPosition();
			if (viewport == null) {
				p.x = 0;
			} else {
				if (scrollpane.getComponentOrientation().isLeftToRight()) {
					p.x = viewport.getViewPosition().x;
				} else {
					p.x = Math.max(0, viewport.getViewPosition().x);
				}
			}
			newColHead.setViewPosition(p);
			scrollpane.add(newColHead, "COLUMN_HEADER");
		}
	}

	void updateHorizontalScrollBar(PropertyChangeEvent pce) {
		updateScrollBar(pce, hsbChangeListener, hsbPropertyChangeListener, false);
	}

	void updateVerticalScrollBar(PropertyChangeEvent pce) {
		updateScrollBar(pce, vsbChangeListener, vsbPropertyChangeListener, true);
	}

	private void updateScrollBar(PropertyChangeEvent pce, ChangeListener cl,
			PropertyChangeListener pcl, boolean isVertical) {
		JScrollBar sb = (JScrollBar) pce.getOldValue();
		if (sb != null) {
			if (cl != null) {
				sb.getModel().removeChangeListener(cl);
			}
			if (pcl != null) {
				sb.removePropertyChangeListener(pcl);
			}
		}
		sb = (JScrollBar) pce.getNewValue();
		if (sb != null) {
			if (isVertical) {
				vertBarUI = (JSScrollBarUI) sb.getUI();
				vertBarUI.isScrollPaneScrollBar = true;
				vertBarUI.myScrollPaneUI = this;
			} else {
				horizBarUI = (JSScrollBarUI) sb.getUI();
				horizBarUI.isScrollPaneScrollBar = true;
				horizBarUI.myScrollPaneUI = this;
			}

			if (cl != null) {
				sb.getModel().addChangeListener(cl);
			}
			if (pcl != null) {
				sb.addPropertyChangeListener(pcl);
			}
		}
	}

	public class PropertyChangeHandler implements PropertyChangeListener {

		// NOTE: This class exists only for backward compatability. All
		// its functionality has been moved into Handler. If you need to add
		// new functionality add it to the Handler, but make sure this
		// class calls into the Handler.

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			getHandler().propertyChange(e);
		}
	}

	/**
	 * Creates an instance of PropertyChangeListener that's added to the
	 * JScrollPane by installUI(). Subclasses can override this method to return a
	 * custom PropertyChangeListener, e.g.
	 * 
	 * <pre>
	 * class MyScrollPaneUI extends BasicScrollPaneUI {
	 *    protected PropertyChangeListener <b>createPropertyChangeListener</b>() {
	 *        return new MyPropertyChangeListener();
	 *    }
	 *    public class MyPropertyChangeListener extends PropertyChangeListener {
	 *        public void propertyChange(PropertyChangeEvent e) {
	 *            if (e.getPropertyName().equals("viewport")) {
	 *                // do some extra work when the viewport changes
	 *            }
	 *            super.propertyChange(e);
	 *        }
	 *    }
	 * }
	 * </pre>
	 * 
	 * @see java.beans.PropertyChangeListener
	 * @see #installUI
	 */
	protected PropertyChangeListener createPropertyChangeListener() {
		return getHandler();
	}

	private static class Actions extends UIAction {
		private static final String SCROLL_UP = "scrollUp";
		private static final String SCROLL_DOWN = "scrollDown";
		private static final String SCROLL_HOME = "scrollHome";
		private static final String SCROLL_END = "scrollEnd";
		private static final String UNIT_SCROLL_UP = "unitScrollUp";
		private static final String UNIT_SCROLL_DOWN = "unitScrollDown";
		private static final String SCROLL_LEFT = "scrollLeft";
		private static final String SCROLL_RIGHT = "scrollRight";
		private static final String UNIT_SCROLL_LEFT = "unitScrollLeft";
		private static final String UNIT_SCROLL_RIGHT = "unitScrollRight";

		Actions(String key) {
			super(key);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JScrollPane scrollPane = (JScrollPane) e.getSource();
			boolean ltr = scrollPane.getComponentOrientation().isLeftToRight();
			String key = getName();

			if (key == SCROLL_UP) {
				scroll(scrollPane, SwingConstants.VERTICAL, -1, true);
			} else if (key == SCROLL_DOWN) {
				scroll(scrollPane, SwingConstants.VERTICAL, 1, true);
			} else if (key == SCROLL_HOME) {
				scrollHome(scrollPane);
			} else if (key == SCROLL_END) {
				scrollEnd(scrollPane);
			} else if (key == UNIT_SCROLL_UP) {
				scroll(scrollPane, SwingConstants.VERTICAL, -1, false);
			} else if (key == UNIT_SCROLL_DOWN) {
				scroll(scrollPane, SwingConstants.VERTICAL, 1, false);
			} else if (key == SCROLL_LEFT) {
				scroll(scrollPane, SwingConstants.HORIZONTAL, ltr ? -1 : 1, true);
			} else if (key == SCROLL_RIGHT) {
				scroll(scrollPane, SwingConstants.HORIZONTAL, ltr ? 1 : -1, true);
			} else if (key == UNIT_SCROLL_LEFT) {
				scroll(scrollPane, SwingConstants.HORIZONTAL, ltr ? -1 : 1, false);
			} else if (key == UNIT_SCROLL_RIGHT) {
				scroll(scrollPane, SwingConstants.HORIZONTAL, ltr ? 1 : -1, false);
			}
		}

		private void scrollEnd(JScrollPane scrollpane) {
			JViewport vp = scrollpane.getViewport();
			Component view;
			if (vp != null && (view = vp.getView()) != null) {
				Rectangle visRect = vp.getViewRect();
				Rectangle bounds = view.getBounds();
				if (scrollpane.getComponentOrientation().isLeftToRight()) {
					vp.setViewPosition(new Point(bounds.width - visRect.width,
							bounds.height - visRect.height));
				} else {
					vp.setViewPosition(new Point(0, bounds.height - visRect.height));
				}
			}
		}

		private void scrollHome(JScrollPane scrollpane) {
			JViewport vp = scrollpane.getViewport();
			Component view;
			if (vp != null && (view = vp.getView()) != null) {
				if (scrollpane.getComponentOrientation().isLeftToRight()) {
					vp.setViewPosition(new Point(0, 0));
				} else {
					Rectangle visRect = vp.getViewRect();
					Rectangle bounds = view.getBounds();
					vp.setViewPosition(new Point(bounds.width - visRect.width, 0));
				}
			}
		}

		private void scroll(JScrollPane scrollpane, int orientation, int direction,
				boolean block) {
			JViewport vp = scrollpane.getViewport();
			Component view;
			if (vp != null && (view = vp.getView()) != null) {
				Rectangle visRect = vp.getViewRect();
				Dimension vSize = view.getSize();
				int amount;

				if (view instanceof Scrollable) {
					if (block) {
						amount = ((Scrollable) view).getScrollableBlockIncrement(visRect,
								orientation, direction);
					} else {
						amount = ((Scrollable) view).getScrollableUnitIncrement(visRect,
								orientation, direction);
					}
				} else {
					if (block) {
						if (orientation == SwingConstants.VERTICAL) {
							amount = visRect.height;
						} else {
							amount = visRect.width;
						}
					} else {
						amount = 10;
					}
				}
				if (orientation == SwingConstants.VERTICAL) {
					visRect.y += (amount * direction);
					if ((visRect.y + visRect.height) > vSize.height) {
						visRect.y = Math.max(0, vSize.height - visRect.height);
					} else if (visRect.y < 0) {
						visRect.y = 0;
					}
				} else {
					if (scrollpane.getComponentOrientation().isLeftToRight()) {
						visRect.x += (amount * direction);
						if ((visRect.x + visRect.width) > vSize.width) {
							visRect.x = Math.max(0, vSize.width - visRect.width);
						} else if (visRect.x < 0) {
							visRect.x = 0;
						}
					} else {
						visRect.x -= (amount * direction);
						if (visRect.width > vSize.width) {
							visRect.x = vSize.width - visRect.width;
						} else {
							visRect.x = Math.max(0,
									Math.min(vSize.width - visRect.width, visRect.x));
						}
					}
				}
				vp.setViewPosition(visRect.getLocation());
			}
		}
	}

	private boolean jsModelStateChanged(ChangeEvent e) {
		if (scrollBarUIDisabled) {
			jsScrollComponentUsingCSS(e.getSource() == viewport);
			return true;
		}
		return false;
	}

	/**
	 * not used
	 * 
	 * @param isViewportChange
	 */
	private void jsScrollComponentUsingCSS(boolean isViewportChange) {
		// if (scrolledComponent.uiClassID == "TextAreaUI") {
		// a problem will occur if the scrollers turn on; the content may be
		// shifted
		// there is discussion of this on StackOverflow. It is possible to
		// correct for it.
		Rectangle r1 = viewport.getBounds();
		Rectangle r2 = scrolledComponent.getBounds();
		if (!r1.equals(r2) && !isViewportChange) // infinite loop if
																											// resizing
			scrolledComponent.setBounds(r1);
		DOMNode.setStyles(scrolledUI.domNode, "overflow-x",
				getScrollBarPolicyCSS(scrollpane.getHorizontalScrollBarPolicy()),
				"overflow-y",
				getScrollBarPolicyCSS(scrollpane.getVerticalScrollBarPolicy()));
		if (horizBarUI != null)
			DOMNode.setStyles(horizBarUI.jqSlider, "display", "none");
		if (vertBarUI != null)
			DOMNode.setStyles(vertBarUI.jqSlider, "display", "none");
		// }
	}

	class Handler implements ChangeListener, PropertyChangeListener {// ,
																																		// MouseWheelListener
																																		// {
	// //
	// // MouseWheelListener
	// //
	// @Override
	// public void mouseWheelMoved(MouseWheelEvent e) {
	// if (scrollpane.isWheelScrollingEnabled() &&
	// e.getWheelRotation() != 0) {
	// JScrollBar toScroll = scrollpane.getVerticalScrollBar();
	// int direction = e.getWheelRotation() < 0 ? -1 : 1;
	// int orientation = SwingConstants.VERTICAL;
	//
	// // find which scrollbar to scroll, or return if none
	// if (toScroll == null || !toScroll.isVisible()) {
	// toScroll = scrollpane.getHorizontalScrollBar();
	// if (toScroll == null || !toScroll.isVisible()) {
	// return;
	// }
	// orientation = SwingConstants.HORIZONTAL;
	// }
	//
	// if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
	// JViewport vp = scrollpane.getViewport();
	// if (vp == null) { return; }
	// Component comp = vp.getView();
	// int units = Math.abs(e.getUnitsToScroll());
	//
	// // When the scrolling speed is set to maximum, it's possible
	// // for a single wheel click to scroll by more units than
	// // will fit in the visible area. This makes it
	// // hard/impossible to get to certain parts of the scrolling
	// // Component with the wheel. To make for more accurate
	// // low-speed scrolling, we limit scrolling to the block
	// // increment if the wheel was only rotated one click.
	// boolean limitScroll = Math.abs(e.getWheelRotation()) == 1;
	//
	// // Check if we should use the visibleRect trick
	// Object fastWheelScroll = toScroll.getClientProperty(
	// "JScrollBar.fastWheelScrolling");
	// if (Boolean.TRUE == fastWheelScroll &&
	// comp instanceof Scrollable) {
	// // 5078454: Under maximum acceleration, we may scroll
	// // by many 100s of units in ~1 second.
	// //
	// // BasicScrollBarUI.scrollByUnits() can bog down the EDT
	// // with repaints in this situation. However, the
	// // Scrollable interface allows us to pass in an
	// // arbitrary visibleRect. This allows us to accurately
	// // calculate the total scroll amount, and then update
	// // the GUI once. This technique provides much faster
	// // accelerated wheel scrolling.
	// Scrollable scrollComp = (Scrollable) comp;
	// Rectangle viewRect = vp.getViewRect();
	// int startingX = viewRect.x;
	// boolean leftToRight =
	// comp.getComponentOrientation().isLeftToRight();
	// int scrollMin = toScroll.getMinimum();
	// int scrollMax = toScroll.getMaximum() -
	// toScroll.getModel().getExtent();
	//
	// if (limitScroll) {
	// int blockIncr =
	// scrollComp.getScrollableBlockIncrement(viewRect,
	// orientation,
	// direction);
	// if (direction < 0) {
	// scrollMin = Math.max(scrollMin,
	// toScroll.getValue() - blockIncr);
	// }
	// else {
	// scrollMax = Math.min(scrollMax,
	// toScroll.getValue() + blockIncr);
	// }
	// }
	//
	// for (int i = 0; i < units; i++) {
	// int unitIncr =
	// scrollComp.getScrollableUnitIncrement(viewRect,
	// orientation, direction);
	// // Modify the visible rect for the next unit, and
	// // check to see if we're at the end already.
	// if (orientation == SwingConstants.VERTICAL) {
	// if (direction < 0) {
	// viewRect.y -= unitIncr;
	// if (viewRect.y <= scrollMin) {
	// viewRect.y = scrollMin;
	// break;
	// }
	// }
	// else { // (direction > 0
	// viewRect.y += unitIncr;
	// if (viewRect.y >= scrollMax) {
	// viewRect.y = scrollMax;
	// break;
	// }
	// }
	// }
	// else {
	// // Scroll left
	// if ((leftToRight && direction < 0) ||
	// (!leftToRight && direction > 0)) {
	// viewRect.x -= unitIncr;
	// if (leftToRight) {
	// if (viewRect.x < scrollMin) {
	// viewRect.x = scrollMin;
	// break;
	// }
	// }
	// }
	// // Scroll right
	// else if ((leftToRight && direction > 0) ||
	// (!leftToRight && direction < 0)) {
	// viewRect.x += unitIncr;
	// if (leftToRight) {
	// if (viewRect.x > scrollMax) {
	// viewRect.x = scrollMax;
	// break;
	// }
	// }
	// }
	// else {
	// assert false : "Non-sensical ComponentOrientation / scroll direction";
	// }
	// }
	// }
	// // Set the final view position on the ScrollBar
	// if (orientation == SwingConstants.VERTICAL) {
	// toScroll.setValue(viewRect.y);
	// }
	// else {
	// if (leftToRight) {
	// toScroll.setValue(viewRect.x);
	// }
	// else {
	// // rightToLeft scrollbars are oriented with
	// // minValue on the right and maxValue on the
	// // left.
	// int newPos = toScroll.getValue() -
	// (viewRect.x - startingX);
	// if (newPos < scrollMin) {
	// newPos = scrollMin;
	// }
	// else if (newPos > scrollMax) {
	// newPos = scrollMax;
	// }
	// toScroll.setValue(newPos);
	// }
	// }
	// }
	// else {
	// // Viewport's view is not a Scrollable, or fast wheel
	// // scrolling is not enabled.
	// JSSScrollBarUI.scrollByUnits(toScroll, direction,
	// units, limitScroll);
	// }
	// }
	// else if (e.getScrollType() ==
	// MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
	// JSScrollBarUI.scrollByBlock(toScroll, direction);
	// }
	// }
	// }

		//
		// ChangeListener: This is added to the vieport, and hsb/vsb models.
		//
		@Override
		public void stateChanged(ChangeEvent e) {
			JViewport viewport = scrollpane.getViewport();
			if (viewport != null) {
				if (e.getSource() == viewport) {
					viewportStateChanged(e);
				} else {
					JScrollBar hsb = scrollpane.getHorizontalScrollBar();
					if (hsb != null && e.getSource() == hsb.getModel()) {
						hsbStateChanged(viewport, e);
					} else {
						JScrollBar vsb = scrollpane.getVerticalScrollBar();
						if (vsb != null && e.getSource() == vsb.getModel()) {
							vsbStateChanged(viewport, e);
						}
					}
				}
			}
		}

		private void vsbStateChanged(JViewport viewport, ChangeEvent e) {
			BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
			Point p = viewport.getViewPosition();
			p.y = model.getValue();
			viewport.setViewPosition(p);
		}

		private void hsbStateChanged(JViewport viewport, ChangeEvent e) {
			BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
			Point p = viewport.getViewPosition();
			int value = model.getValue();
			if (scrollpane.getComponentOrientation().isLeftToRight()) {
				p.x = value;
			} else {
				int max = viewport.getViewSize().width;
				int extent = viewport.getExtentSize().width;
				int oldX = p.x;

				/*
				 * Set new X coordinate based on "value".
				 */
				p.x = max - extent - value;

				/*
				 * If setValue() was called before "extent" was fixed, turn
				 * setValueCalled flag on.
				 */
				if ((extent == 0) && (value != 0) && (oldX == max)) {
					setValueCalled = true;
				} else {
					/*
					 * When a pane without a horizontal scroll bar was reduced and the bar
					 * appeared, the viewport should show the right side of the view
					 */
					if ((extent != 0) && (oldX < 0) && (p.x == 0)) {
						p.x += value;
					}
				}
			}
			viewport.setViewPosition(p);
		}

		private void viewportStateChanged(ChangeEvent e) {
			syncScrollPaneWithViewport();
		}

		//
		// PropertyChangeListener: This is installed on both the JScrollPane
		// and the horizontal/vertical scrollbars.
		//

		// Listens for changes in the model property and reinstalls the
		// horizontal/vertical PropertyChangeListeners.
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (e.getSource() == scrollpane) {
				scrollPanePropertyChange(e);
			} else {
				sbPropertyChange(e);
			}
		}

		private void scrollPanePropertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();

			if (propertyName == "verticalScrollBarDisplayPolicy") {
				updateScrollBarDisplayPolicy(e);
			} else if (propertyName == "horizontalScrollBarDisplayPolicy") {
				updateScrollBarDisplayPolicy(e);
			} else if (propertyName == "viewport") {
				updateViewport(e);
			} else if (propertyName == "rowHeader") {
				updateRowHeader(e);
			} else if (propertyName == "columnHeader") {
				updateColumnHeader(e);
			} else if (propertyName == "verticalScrollBar") {
				updateVerticalScrollBar(e);
			} else if (propertyName == "horizontalScrollBar") {
				updateHorizontalScrollBar(e);
			} else if (propertyName == "componentOrientation") {
				scrollpane.revalidate();
				scrollpane.repaint();
			}
		}

		// PropertyChangeListener for the horizontal and vertical scrollbars.
		private void sbPropertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();
			Object source = e.getSource();

			if ("model" == propertyName) {
				JScrollBar sb = scrollpane.getVerticalScrollBar();
				BoundedRangeModel oldModel = (BoundedRangeModel) e.getOldValue();
				ChangeListener cl = null;

				if (source == sb) {
					cl = vsbChangeListener;
				} else if (source == scrollpane.getHorizontalScrollBar()) {
					sb = scrollpane.getHorizontalScrollBar();
					cl = hsbChangeListener;
				}
				if (cl != null) {
					if (oldModel != null) {
						oldModel.removeChangeListener(cl);
					}
					if (sb.getModel() != null) {
						sb.getModel().addChangeListener(cl);
					}
				}
			} else if ("componentOrientation" == propertyName) {
				if (source == scrollpane.getHorizontalScrollBar()) {
					JScrollBar hsb = scrollpane.getHorizontalScrollBar();
					JViewport viewport = scrollpane.getViewport();
					Point p = viewport.getViewPosition();
					if (scrollpane.getComponentOrientation().isLeftToRight()) {
						p.x = hsb.getValue();
					} else {
						p.x = viewport.getViewSize().width - viewport.getExtentSize().width
								- hsb.getValue();
					}
					viewport.setViewPosition(p);
				}
			}
		}
	}

}
