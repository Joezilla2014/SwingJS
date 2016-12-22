package swingjs.plaf;


import jsjava.awt.Dimension;
import jsjavax.swing.JPopupMenu;
import jsjavax.swing.LookAndFeel;
import swingjs.J2SRequireImport;
import swingjs.JSToolkit;
import swingjs.api.DOMNode;
import swingjs.api.JSSwingMenu;

@J2SRequireImport(swingjs.jquery.JQueryUI.class)
public class JSPopupMenuUI extends JSPanelUI {
	
	// a frameless independent window
	
	static JSSwingMenu j2sSwingMenu;
	private JPopupMenu menu;


	public JSPopupMenuUI() {
		
		if (j2sSwingMenu == null) {
			JSToolkit.loadStaticResource("swingjs/jquery/j2sMenu.js");
			j2sSwingMenu = JSToolkit.J2S._getSwing();
		}
		isContainer = true;	
		isMenuItem = true;
		setDoc();
	}
	
	@Override
	protected DOMNode updateDOMNode() {
		// j2sMenu.js will wrap this in a div with the appropriate
		if (domNode == null) {
//			popupMenu = (JPopupMenu) jc;
//			isTopLevel = (!(popupMenu.getInvoker() instanceof JMenu) 
//					|| ((JMenu) popupMenu.getInvoker()).isTopLevelMenu());
			domNode = containerNode = newDOMObject("ul", id);
		}
		return domNode;
	}

	@Override
	protected void installUIImpl() {
    LookAndFeel.installColorsAndFont(jc,
        "PopupMenu.background",
        "PopupMenu.foreground",
        "PopupMenu.font");
	}

	@Override
	protected void uninstallUIImpl() {
		// TODO Auto-generated method stub
		
	}

	public Object getPopup() {
		// TODO: this causes an uncaught error. 
		return null;		
	}


	/**
	 * j2s bug in this particular method makes this Clazz.overrideMethod, 
	 * but it cannot override if it has a super call,
	 * so I avoid the super call by duplicating the code from JSComponentUI
	 * 
	 */
	@Override
	public void setVisible(boolean b) {
		if (menu == null) {
			// important to do this here, not earlier?
			menu = (JPopupMenu) c;
			j2sSwingMenu.setMenu(menu);
		}
		if (b) {
			getOuterNode();
			int x = 0, y = 0;
			
			/**
			 * have to cheat here, because we want screen coordinates
			 * 
			 * @j2sNative
			 * 
			 * x = this.menu.desiredLocationX;
			 * y = this.menu.desiredLocationY;
			 * 
			 */
			{}
			j2sSwingMenu.showMenu(menu, x, y);
		} else {
			j2sSwingMenu.hideMenu(menu);
		}
	}
	
	@Override
	public void dispose() {
    DOMNode.remove(domNode);
    DOMNode.remove(outerNode);
    j2sSwingMenu.disposeMenu(menu);
	}

	@Override
	public Dimension getPreferredSize() {
		// unnecessary  -- will never be subject to a layout manager
		return null;
	}


}
