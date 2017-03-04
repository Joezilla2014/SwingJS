package swingjs.api;

import jsjava.awt.Image;
import jsjava.awt.Rectangle;
import swingjs.JSToolkit;
import swingjs.plaf.JSComponentUI;

public abstract class DOMNode {

	public abstract void appendChild(DOMNode node);

	public abstract boolean hasFocus();

	public abstract boolean play();

	public abstract DOMNode removeChild(DOMNode nocde);

	public abstract DOMNode removeAttribute(String attr);
	
	public abstract void setSelectionRange(int pt0, int pt1);

	public abstract Rectangle getBoundingClientRect();


	public static DOMNode createElement(String key, String id, Object... attrs) {
		DOMNode node = null;
		/**
		 * adding __CLASS_NAME__ allows a node to be used as a parameter in an overloaded method
		 * 
		 * @j2sNative
		 * 					node = document.createElement(key);
		 * 					node.__CLASS_NAME__ = "swingjs.api.DOMNode";
		 * 					if(id)node.id = id;
		 */
		{
		}
		return setAttrs(node, attrs);
	}

	public static DOMNode createTextNode(String text) {
		DOMNode node = null;
		/**
		 * 
		 * @j2sNative
		 * 					node = document.createTextNode(text); 
		 */
		{
		}
		return node;
	}


	public static DOMNode getParent(DOMNode node) {
		/**
		 * @j2sNative
		 * 
		 *            return node.parentNode;
		 * 
		 */
		{
			return null;
		}
	}

	/**
	 * remove this node and return its parent
	 * @param node
	 * @return parent or null
	 */
	public static DOMNode remove(DOMNode node) {
		if (node == null)
			return null;
		/**
		 * @j2sNative
		 * 
		 * try {
		 *   var p = node.parentElement;
		 *   p.removeChild(node);
		 *   $(body).remove(node);
		 * } catch(e) {
		 * };
		 * return p;
		 */
		{
			return null;
		}
	}
	
	/**
	 * note: this works with 'checked' as well
	 * 
	 * @param node
	 * @param attr
	 * @return
	 */
	public static Object getAttr(DOMNode node, String attr) {
		/**
		 * @j2sNative
		 * 
		 *       if (node)return node[attr];
		 * 
		 */
		{
			return null;
		}
	}

	public static String getStyle(DOMNode node, String style) {
		/**
		 * @j2sNative
		 * 
		 *       if (node)return node.style[style];
		 * 
		 */
		{
			return null;
		}
	}

	public static void getRectangle(DOMNode node, Rectangle r) {
		/**
		 * @j2sNative
		 * 
		 *       r.x = parseInt(node.style.left.split("p")[0]);
		 *       r.y = parseInt(node.style.top.split("p")[0]);
		 *       r.width = parseInt(node.style.width.split("p")[0]);
		 *       r.height = parseInt(node.style.height.split("p")[0]);
		 * 
		 */
		{
		}
	}

	
	public static DOMNode setAttr(DOMNode node, String attr, Object val) {
		if (val == null) {
			node.removeAttribute("readOnly");
		} else {
			/**
			 * @j2sNative
			 * 
			 *            node[attr] = (val == "TRUE" ? true : val);
			 * 
			 */
			{
			}
		}
		return node;
	}

	public static DOMNode setAttrs(DOMNode node, Object... attr) {
		/**
		 * @j2sNative
		 * 
		 *            for (var i = 0; i < attr.length;) { 
		 *            node[attr[i++]] = attr[i++]; }
		 * 
		 */
		{
		}
		return node;
	}

	public static DOMNode setStyles(DOMNode node, String... attr) {
		if (node != null)
		/**
		 * @j2sNative
		 * 
		 *            for (var i = 0; i < attr.length;) {
		 *             node.style[attr[i++]] = attr[i++]; }
		 * 
		 */
		{
		}
		return node;
	}

	public static DOMNode setSize(DOMNode node, int width, int height) {
		return setStyles(node, "width", width + "px", "height", height + "px");
	}

	public static DOMNode setPositionAbsolute(DOMNode node, int top, int left) {
		if (top != Integer.MIN_VALUE) {
			DOMNode.setStyles(node, "top", top + "px");
			DOMNode.setStyles(node, "left", left + "px");
		}
		return DOMNode.setStyles(node, "position", "absolute");
	}

	public static DOMNode firstChild(DOMNode node) {
		/**
		 * @j2sNative
		 * 
		 * return node.firstChild;
		 * 
		 */
		{
			return null;
		}
	}

	public static DOMNode lastChild(DOMNode node) {
		/**
		 * @j2sNative
		 * 
		 * return node.lastChild;
		 * 
		 */
		{
			return null;
		}
	}

	public static void addJqueryHandledEvent(JSComponentUI me, DOMNode node, String event) {
		Object f = null;
	  /**
		 * @j2sNative
		 * 
		 *            f = function(ev) {me.handleJSEvent(node, -1, ev)};
		 */
		{}
		JSToolkit.getJQuery().$(node).on(event, f);
	}

	public static DOMNode setZ(DOMNode node, int z) {
		return setStyles(node, "z-index", "" + z);
	}

	public static void playWav(String filePath) {
		DOMNode.setAttrs(DOMNode.createElement("audio", null), 
				"controls", "true", "src", filePath).play();
	}

	public static void setCursor(String c) {
		/**
		 * @j2sNative
		 * 
		 * document.body.style.cursor = c;
		 * 
		 */
		{}
	}

	public static DOMNode getImageNode(Image img) {
		
		/**
		 * note that canvas takes precedence over imgNode, because
		 * imgNode is a placeholder for the original image, but canvas
		 * will be an op-filtered image
		 * 
		 * @j2sNative
		 * 
		 *            return (img._canvas || img._imgNode);
		 */
		{
			return null;
		}
	}

	public static void addHorizontalGap(DOMNode domNode, int gap) {
		DOMNode label = DOMNode.setStyles(DOMNode.createElement("label", null), 
				"letter-spacing", gap + "px", "font-size", "0pt");
		label.appendChild(DOMNode.createTextNode("."));
		domNode.appendChild(label);
	}

}
