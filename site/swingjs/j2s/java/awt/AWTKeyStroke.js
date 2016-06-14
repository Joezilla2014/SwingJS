Clazz.declarePackage ("java.awt");
Clazz.load (null, ["java.awt.AWTKeyStroke", "$.VKCollection"], ["java.io.ObjectStreamException", "java.lang.IllegalArgumentException", "$.StringBuilder", "java.util.Collections", "$.HashMap", "$.StringTokenizer", "java.awt.event.KeyEvent", "jssun.awt.AppContext"], function () {
c$ = Clazz.decorateAsClass (function () {
this.keyChar = '\uffff';
this.keyCode = 0;
this.modifiers = 0;
this.onKeyRelease = false;
Clazz.instantialize (this, arguments);
}, java.awt, "AWTKeyStroke");
c$.getAWTKeyStrokeClass = Clazz.defineMethod (c$, "getAWTKeyStrokeClass", 
 function () {
var clazz = jssun.awt.AppContext.getAppContext ().get (java.awt.AWTKeyStroke);
if (clazz == null) {
clazz = java.awt.AWTKeyStroke;
jssun.awt.AppContext.getAppContext ().put (java.awt.AWTKeyStroke, java.awt.AWTKeyStroke);
}return clazz;
});
Clazz.makeConstructor (c$, 
function () {
});
Clazz.makeConstructor (c$, 
function (keyChar, keyCode, modifiers, onKeyRelease) {
this.keyChar = keyChar;
this.keyCode = keyCode;
this.modifiers = modifiers;
this.onKeyRelease = onKeyRelease;
}, "~S,~N,~N,~B");
c$.registerSubclass = Clazz.defineMethod (c$, "registerSubclass", 
function (subclass) {
}, "Class");
c$.getCachedStroke = Clazz.defineMethod (c$, "getCachedStroke", 
 function (keyChar, keyCode, modifiers, onKeyRelease) {
return null;
}, "~S,~N,~N,~B");
c$.getAWTKeyStroke = Clazz.defineMethod (c$, "getAWTKeyStroke", 
function (keyChar) {
return java.awt.AWTKeyStroke.getCachedStroke (keyChar, 0, 0, false);
}, "~S");
c$.getAWTKeyStroke = Clazz.defineMethod (c$, "getAWTKeyStroke", 
function (keyChar, modifiers) {
if (keyChar == null) {
throw  new IllegalArgumentException ("keyChar cannot be null");
}return java.awt.AWTKeyStroke.getCachedStroke (keyChar.charValue (), 0, modifiers, false);
}, "Character,~N");
c$.getAWTKeyStroke = Clazz.defineMethod (c$, "getAWTKeyStroke", 
function (keyCode, modifiers, onKeyRelease) {
return java.awt.AWTKeyStroke.getCachedStroke ('\uffff', keyCode, modifiers, onKeyRelease);
}, "~N,~N,~B");
c$.getAWTKeyStroke = Clazz.defineMethod (c$, "getAWTKeyStroke", 
function (keyCode, modifiers) {
return java.awt.AWTKeyStroke.getCachedStroke ('\uffff', keyCode, modifiers, false);
}, "~N,~N");
c$.getAWTKeyStrokeForEvent = Clazz.defineMethod (c$, "getAWTKeyStrokeForEvent", 
function (anEvent) {
var id = anEvent.getID ();
switch (id) {
case 401:
case 402:
return java.awt.AWTKeyStroke.getCachedStroke ('\uffff', anEvent.getKeyCode (), anEvent.getModifiers (), (id == 402));
case 400:
return java.awt.AWTKeyStroke.getCachedStroke (anEvent.getKeyChar (), 0, anEvent.getModifiers (), false);
default:
return null;
}
}, "java.awt.event.KeyEvent");
c$.getAWTKeyStroke = Clazz.defineMethod (c$, "getAWTKeyStroke", 
function (s) {
if (s == null) {
throw  new IllegalArgumentException ("String cannot be null");
}var errmsg = "String formatted incorrectly";
var st =  new java.util.StringTokenizer (s, " ");
var mask = 0;
var released = false;
var typed = false;
var pressed = false;
{
if (java.awt.AWTKeyStroke.modifierKeywords == null) {
var uninitializedMap =  new java.util.HashMap (8, 1.0);
uninitializedMap.put ("shift", Integer.$valueOf (65));
uninitializedMap.put ("control", Integer.$valueOf (130));
uninitializedMap.put ("ctrl", Integer.$valueOf (130));
uninitializedMap.put ("meta", Integer.$valueOf (260));
uninitializedMap.put ("alt", Integer.$valueOf (520));
uninitializedMap.put ("altGraph", Integer.$valueOf (8224));
uninitializedMap.put ("button1", Integer.$valueOf (1024));
uninitializedMap.put ("button2", Integer.$valueOf (2048));
uninitializedMap.put ("button3", Integer.$valueOf (4096));
java.awt.AWTKeyStroke.modifierKeywords = java.util.Collections.synchronizedMap (uninitializedMap);
}}var count = st.countTokens ();
for (var i = 1; i <= count; i++) {
var token = st.nextToken ();
if (typed) {
if (token.length != 1 || i != count) {
throw  new IllegalArgumentException ("String formatted incorrectly");
}return java.awt.AWTKeyStroke.getCachedStroke (token.charAt (0), 0, mask, false);
}if (pressed || released || i == count) {
if (i != count) {
throw  new IllegalArgumentException ("String formatted incorrectly");
}var keyCodeName = "VK_" + token;
var keyCode = java.awt.AWTKeyStroke.getVKValue (keyCodeName);
return java.awt.AWTKeyStroke.getCachedStroke ('\uffff', keyCode, mask, released);
}if (token.equals ("released")) {
released = true;
continue;
}if (token.equals ("pressed")) {
pressed = true;
continue;
}if (token.equals ("typed")) {
typed = true;
continue;
}var tokenMask = java.awt.AWTKeyStroke.modifierKeywords.get (token);
if (tokenMask != null) {
mask |= tokenMask.intValue ();
} else {
throw  new IllegalArgumentException ("String formatted incorrectly");
}}
throw  new IllegalArgumentException ("String formatted incorrectly");
}, "~S");
c$.getVKCollection = Clazz.defineMethod (c$, "getVKCollection", 
 function () {
if (java.awt.AWTKeyStroke.vks == null) {
java.awt.AWTKeyStroke.vks =  new java.awt.VKCollection ();
}return java.awt.AWTKeyStroke.vks;
});
c$.getVKValue = Clazz.defineMethod (c$, "getVKValue", 
 function (key) {
var vkCollect = java.awt.AWTKeyStroke.getVKCollection ();
var value = vkCollect.findCode (key);
if (value == null) {
var keyCode = 0;
var errmsg = "String formatted incorrectly";
try {
keyCode = java.awt.event.KeyEvent.getField (key).getInt (java.awt.event.KeyEvent);
} catch (e$$) {
if (Clazz.exceptionOf (e$$, NoSuchFieldException)) {
var nsfe = e$$;
{
throw  new IllegalArgumentException ("String formatted incorrectly");
}
} else if (Clazz.exceptionOf (e$$, IllegalAccessException)) {
var iae = e$$;
{
throw  new IllegalArgumentException ("String formatted incorrectly");
}
} else {
throw e$$;
}
}
value = Integer.$valueOf (keyCode);
vkCollect.put (key, value);
}return value.intValue ();
}, "~S");
Clazz.defineMethod (c$, "getKeyChar", 
function () {
return this.keyChar;
});
Clazz.defineMethod (c$, "getKeyCode", 
function () {
return this.keyCode;
});
Clazz.defineMethod (c$, "getModifiers", 
function () {
return this.modifiers;
});
Clazz.defineMethod (c$, "isOnKeyRelease", 
function () {
return this.onKeyRelease;
});
Clazz.defineMethod (c$, "getKeyEventType", 
function () {
if (this.keyCode == 0) {
return 400;
} else {
return (this.onKeyRelease) ? 402 : 401;
}});
Clazz.overrideMethod (c$, "hashCode", 
function () {
return (((this.keyChar).charCodeAt (0)) + 1) * (2 * (this.keyCode + 1)) * (this.modifiers + 1) + (this.onKeyRelease ? 1 : 2);
});
Clazz.defineMethod (c$, "equals", 
function (anObject) {
if (Clazz.instanceOf (anObject, java.awt.AWTKeyStroke)) {
var ks = anObject;
return (ks.keyChar == this.keyChar && ks.keyCode == this.keyCode && ks.onKeyRelease == this.onKeyRelease && ks.modifiers == this.modifiers);
}return false;
}, "~O");
Clazz.overrideMethod (c$, "toString", 
function () {
if (this.keyCode == 0) {
return java.awt.AWTKeyStroke.getModifiersText (this.modifiers) + "typed " + this.keyChar;
} else {
return java.awt.AWTKeyStroke.getModifiersText (this.modifiers) + (this.onKeyRelease ? "released" : "pressed") + " " + java.awt.AWTKeyStroke.getVKText (this.keyCode);
}});
c$.getModifiersText = Clazz.defineMethod (c$, "getModifiersText", 
function (modifiers) {
var buf =  new StringBuilder ();
if ((modifiers & 64) != 0) {
buf.append ("shift ");
}if ((modifiers & 128) != 0) {
buf.append ("ctrl ");
}if ((modifiers & 256) != 0) {
buf.append ("meta ");
}if ((modifiers & 512) != 0) {
buf.append ("alt ");
}if ((modifiers & 8192) != 0) {
buf.append ("altGraph ");
}if ((modifiers & 1024) != 0) {
buf.append ("button1 ");
}if ((modifiers & 2048) != 0) {
buf.append ("button2 ");
}if ((modifiers & 4096) != 0) {
buf.append ("button3 ");
}return buf.toString ();
}, "~N");
c$.getVKText = Clazz.defineMethod (c$, "getVKText", 
function (keyCode) {
return "UNKNOWN";
}, "~N");
Clazz.defineMethod (c$, "readResolve", 
function () {
{
var newClass = this.getClass ();
var awtKeyStrokeClass = java.awt.AWTKeyStroke.getAWTKeyStrokeClass ();
if (!newClass.equals (awtKeyStrokeClass)) {
java.awt.AWTKeyStroke.registerSubclass (newClass);
}return java.awt.AWTKeyStroke.getCachedStroke (this.keyChar, this.keyCode, this.modifiers, this.onKeyRelease);
}});
Clazz.defineStatics (c$,
"modifierKeywords", null,
"vks", null);
c$ = Clazz.decorateAsClass (function () {
this.code2name = null;
this.name2code = null;
Clazz.instantialize (this, arguments);
}, java.awt, "VKCollection");
Clazz.makeConstructor (c$, 
function () {
this.code2name =  new java.util.HashMap ();
this.name2code =  new java.util.HashMap ();
});
Clazz.defineMethod (c$, "put", 
function (name, code) {
this.code2name.put (code, name);
this.name2code.put (name, code);
}, "~S,Integer");
Clazz.defineMethod (c$, "findCode", 
function (name) {
return this.name2code.get (name);
}, "~S");
Clazz.defineMethod (c$, "findName", 
function (code) {
return this.code2name.get (code);
}, "Integer");
});
