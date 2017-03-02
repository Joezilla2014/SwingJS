package swingjs.plaf;

//import jsjava.awt.FontMetrics;
import java.awt.Event;
import java.awt.event.KeyEvent;

import jsjava.awt.Dimension;
import jsjava.awt.event.ActionEvent;
import jsjavax.swing.Action;
import jsjavax.swing.JTextField;
import jsjavax.swing.SwingConstants;
import jsjavax.swing.text.JTextComponent;
import swingjs.api.DOMNode;

/**
 * A minimal implementation of a test field ui/peer
 * 
 * @author Bob Hanson
 * 
 */
public class JSTextFieldUI extends JSTextUI {

	protected String inputType = "text";
	private JTextField textField;

	@Override
	protected DOMNode updateDOMNode() {
		if (domNode == null) {
			allowBackground = false;
			// no textNode here, because in input does not have that.
			focusNode = enableNode = valueNode = domNode = DOMNode.setStyles(
					newDOMObject("input", id, "type", inputType), "padding", "0px 0px",
					"lineHeight", "0.8", "box-sizing", "border-box");
			DOMNode.setAttrs(focusNode, "ui", this);
			// not active; requires position:absolute; wrong for standard text box
			// vCenter(domNode, -10);
			setDataUI(domNode);
			setDataComponent(domNode);
			if (textField.isEditable()) {
				bindJSEvents(domNode, "keydown keypress keyup", Event.KEY_PRESS, false);
				addJQueryFocusCallbacks();
			}
		}
		textListener.checkDocument();
		setCssFont(setProp(domNode, "value", getComponentText()), c.getFont());
		// setTextAlignment();
		setEditable(editable);
		if (jc.isOpaque())
			setBackground(jc.getBackground());
		return domNode;
	}

	@Override
	protected Dimension getCSSAdjustment(boolean addingCSS) {
		return new Dimension(0, addingCSS ? 0 : -2);
	}

	@Override
	protected void installUIImpl() {
		textField = (JTextField) c;
		super.installUIImpl();
	}

	@Override
	boolean handleEnter(int eventType) {
		if (eventType == KeyEvent.KEY_PRESSED) {
			Action a = getActionMap().get(JTextField.notifyAction);
			if (a != null)
				a.actionPerformed(new ActionEvent(c, ActionEvent.ACTION_PERFORMED,
						JTextField.notifyAction, System.currentTimeMillis(), 0));
		}
		return true;
	}

	@Override
	protected String getPropertyPrefix() {
		return "TextField";
	}

}
