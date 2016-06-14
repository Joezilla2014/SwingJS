Clazz.declarePackage ("java.awt");
Clazz.load (null, "java.awt.Cursor", ["java.lang.IllegalArgumentException"], function () {
c$ = Clazz.decorateAsClass (function () {
this.type = 0;
this.name = null;
Clazz.instantialize (this, arguments);
}, java.awt, "Cursor");
c$.getPredefinedCursor = Clazz.defineMethod (c$, "getPredefinedCursor", 
function (type) {
if (type < 0 || type > 13) {
throw  new IllegalArgumentException ("illegal cursor type");
}var c = java.awt.Cursor.predefinedPrivate[type];
if (c == null) {
java.awt.Cursor.predefinedPrivate[type] = c =  new java.awt.Cursor (type);
}if (java.awt.Cursor.predefined[type] == null) {
java.awt.Cursor.predefined[type] = c;
}return c;
}, "~N");
c$.getSystemCustomCursor = Clazz.defineMethod (c$, "getSystemCustomCursor", 
function (name) {
return null;
}, "~S");
c$.getDefaultCursor = Clazz.defineMethod (c$, "getDefaultCursor", 
function () {
return java.awt.Cursor.getPredefinedCursor (0);
});
Clazz.makeConstructor (c$, 
function (type) {
if (type < 0 || type > 13) {
throw  new IllegalArgumentException ("illegal cursor type");
}this.type = type;
this.name = "TODO_CURSOR";
}, "~N");
Clazz.makeConstructor (c$, 
function (name) {
this.type = -1;
this.name = name;
}, "~S");
Clazz.defineMethod (c$, "getType", 
function () {
return this.type;
});
Clazz.defineMethod (c$, "getName", 
function () {
return this.name;
});
Clazz.overrideMethod (c$, "toString", 
function () {
return this.getClass ().getName () + "[" + this.getName () + "]";
});
Clazz.defineStatics (c$,
"DEFAULT_CURSOR", 0,
"CROSSHAIR_CURSOR", 1,
"TEXT_CURSOR", 2,
"WAIT_CURSOR", 3,
"SW_RESIZE_CURSOR", 4,
"SE_RESIZE_CURSOR", 5,
"NW_RESIZE_CURSOR", 6,
"NE_RESIZE_CURSOR", 7,
"N_RESIZE_CURSOR", 8,
"S_RESIZE_CURSOR", 9,
"W_RESIZE_CURSOR", 10,
"E_RESIZE_CURSOR", 11,
"HAND_CURSOR", 12,
"MOVE_CURSOR", 13);
c$.predefined = c$.prototype.predefined =  new Array (14);
c$.predefinedPrivate = c$.prototype.predefinedPrivate =  new Array (14);
Clazz.defineStatics (c$,
"cursorProperties", [["AWT.DefaultCursor", "Default Cursor"], ["AWT.CrosshairCursor", "Crosshair Cursor"], ["AWT.TextCursor", "Text Cursor"], ["AWT.WaitCursor", "Wait Cursor"], ["AWT.SWResizeCursor", "Southwest Resize Cursor"], ["AWT.SEResizeCursor", "Southeast Resize Cursor"], ["AWT.NWResizeCursor", "Northwest Resize Cursor"], ["AWT.NEResizeCursor", "Northeast Resize Cursor"], ["AWT.NResizeCursor", "North Resize Cursor"], ["AWT.SResizeCursor", "South Resize Cursor"], ["AWT.WResizeCursor", "West Resize Cursor"], ["AWT.EResizeCursor", "East Resize Cursor"], ["AWT.HandCursor", "Hand Cursor"], ["AWT.MoveCursor", "Move Cursor"]],
"CUSTOM_CURSOR", -1);
});
