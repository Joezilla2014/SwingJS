package swingjs.plaf;

import swingjs.api.DOMNode;

/**
 * CheckboxUI implementation for BasicCheckboxUI
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 * 
 * @author Jeff Dinkins
 */
public class JSCheckBoxMenuItemUI extends JSCheckBoxUI {

	@Override
	public DOMNode getDOMObject() {
		return getButtonObject("checkBox");
	}

	@Override
	protected String getPropertyPrefix() {
		return "CheckBoxMenuItem.";
	}

}