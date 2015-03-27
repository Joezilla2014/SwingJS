Clazz.declarePackage ("jsjava.awt.geom");
Clazz.load (["jsjava.awt.Shape"], "jsjava.awt.geom.Line2D", ["java.lang.InternalError", "jsjava.awt.geom.LineIterator", "$.Point2D", "$.Rectangle2D"], function () {
c$ = Clazz.declareType (jsjava.awt.geom, "Line2D", null, [jsjava.awt.Shape, Cloneable]);
Clazz.makeConstructor (c$, 
function () {
});
Clazz.defineMethod (c$, "setLine", 
function (p1, p2) {
this.setLine (p1.getX (), p1.getY (), p2.getX (), p2.getY ());
}, "jsjava.awt.geom.Point2D,jsjava.awt.geom.Point2D");
Clazz.defineMethod (c$, "setLine", 
function (l) {
this.setLine (l.getX1 (), l.getY1 (), l.getX2 (), l.getY2 ());
}, "jsjava.awt.geom.Line2D");
c$.relativeCCW = Clazz.defineMethod (c$, "relativeCCW", 
function (x1, y1, x2, y2, px, py) {
x2 -= x1;
y2 -= y1;
px -= x1;
py -= y1;
var ccw = px * y2 - py * x2;
if (ccw == 0.0) {
ccw = px * x2 + py * y2;
if (ccw > 0.0) {
px -= x2;
py -= y2;
ccw = px * x2 + py * y2;
if (ccw < 0.0) {
ccw = 0.0;
}}}return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
}, "~N,~N,~N,~N,~N,~N");
Clazz.defineMethod (c$, "relativeCCW", 
function (px, py) {
return jsjava.awt.geom.Line2D.relativeCCW (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), px, py);
}, "~N,~N");
Clazz.defineMethod (c$, "relativeCCW", 
function (p) {
return jsjava.awt.geom.Line2D.relativeCCW (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), p.getX (), p.getY ());
}, "jsjava.awt.geom.Point2D");
c$.linesIntersect = Clazz.defineMethod (c$, "linesIntersect", 
function (x1, y1, x2, y2, x3, y3, x4, y4) {
return ((jsjava.awt.geom.Line2D.relativeCCW (x1, y1, x2, y2, x3, y3) * jsjava.awt.geom.Line2D.relativeCCW (x1, y1, x2, y2, x4, y4) <= 0) && (jsjava.awt.geom.Line2D.relativeCCW (x3, y3, x4, y4, x1, y1) * jsjava.awt.geom.Line2D.relativeCCW (x3, y3, x4, y4, x2, y2) <= 0));
}, "~N,~N,~N,~N,~N,~N,~N,~N");
Clazz.defineMethod (c$, "intersectsLine", 
function (x1, y1, x2, y2) {
return jsjava.awt.geom.Line2D.linesIntersect (x1, y1, x2, y2, this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 ());
}, "~N,~N,~N,~N");
Clazz.defineMethod (c$, "intersectsLine", 
function (l) {
return jsjava.awt.geom.Line2D.linesIntersect (l.getX1 (), l.getY1 (), l.getX2 (), l.getY2 (), this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 ());
}, "jsjava.awt.geom.Line2D");
c$.ptSegDistSq = Clazz.defineMethod (c$, "ptSegDistSq", 
function (x1, y1, x2, y2, px, py) {
x2 -= x1;
y2 -= y1;
px -= x1;
py -= y1;
var dotprod = px * x2 + py * y2;
var projlenSq;
if (dotprod <= 0.0) {
projlenSq = 0.0;
} else {
px = x2 - px;
py = y2 - py;
dotprod = px * x2 + py * y2;
if (dotprod <= 0.0) {
projlenSq = 0.0;
} else {
projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
}}var lenSq = px * px + py * py - projlenSq;
if (lenSq < 0) {
lenSq = 0;
}return lenSq;
}, "~N,~N,~N,~N,~N,~N");
c$.ptSegDist = Clazz.defineMethod (c$, "ptSegDist", 
function (x1, y1, x2, y2, px, py) {
return Math.sqrt (jsjava.awt.geom.Line2D.ptSegDistSq (x1, y1, x2, y2, px, py));
}, "~N,~N,~N,~N,~N,~N");
Clazz.defineMethod (c$, "ptSegDistSq", 
function (px, py) {
return jsjava.awt.geom.Line2D.ptSegDistSq (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), px, py);
}, "~N,~N");
Clazz.defineMethod (c$, "ptSegDistSq", 
function (pt) {
return jsjava.awt.geom.Line2D.ptSegDistSq (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), pt.getX (), pt.getY ());
}, "jsjava.awt.geom.Point2D");
Clazz.defineMethod (c$, "ptSegDist", 
function (px, py) {
return jsjava.awt.geom.Line2D.ptSegDist (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), px, py);
}, "~N,~N");
Clazz.defineMethod (c$, "ptSegDist", 
function (pt) {
return jsjava.awt.geom.Line2D.ptSegDist (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), pt.getX (), pt.getY ());
}, "jsjava.awt.geom.Point2D");
c$.ptLineDistSq = Clazz.defineMethod (c$, "ptLineDistSq", 
function (x1, y1, x2, y2, px, py) {
x2 -= x1;
y2 -= y1;
px -= x1;
py -= y1;
var dotprod = px * x2 + py * y2;
var projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
var lenSq = px * px + py * py - projlenSq;
if (lenSq < 0) {
lenSq = 0;
}return lenSq;
}, "~N,~N,~N,~N,~N,~N");
c$.ptLineDist = Clazz.defineMethod (c$, "ptLineDist", 
function (x1, y1, x2, y2, px, py) {
return Math.sqrt (jsjava.awt.geom.Line2D.ptLineDistSq (x1, y1, x2, y2, px, py));
}, "~N,~N,~N,~N,~N,~N");
Clazz.defineMethod (c$, "ptLineDistSq", 
function (px, py) {
return jsjava.awt.geom.Line2D.ptLineDistSq (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), px, py);
}, "~N,~N");
Clazz.defineMethod (c$, "ptLineDistSq", 
function (pt) {
return jsjava.awt.geom.Line2D.ptLineDistSq (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), pt.getX (), pt.getY ());
}, "jsjava.awt.geom.Point2D");
Clazz.defineMethod (c$, "ptLineDist", 
function (px, py) {
return jsjava.awt.geom.Line2D.ptLineDist (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), px, py);
}, "~N,~N");
Clazz.defineMethod (c$, "ptLineDist", 
function (pt) {
return jsjava.awt.geom.Line2D.ptLineDist (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 (), pt.getX (), pt.getY ());
}, "jsjava.awt.geom.Point2D");
Clazz.defineMethod (c$, "contains", 
function (x, y) {
return false;
}, "~N,~N");
Clazz.defineMethod (c$, "contains", 
function (p) {
return false;
}, "jsjava.awt.geom.Point2D");
Clazz.defineMethod (c$, "intersects", 
function (x, y, w, h) {
return this.intersects ( new jsjava.awt.geom.Rectangle2D.Double (x, y, w, h));
}, "~N,~N,~N,~N");
Clazz.defineMethod (c$, "intersects", 
function (r) {
return r.intersectsLine (this.getX1 (), this.getY1 (), this.getX2 (), this.getY2 ());
}, "jsjava.awt.geom.Rectangle2D");
Clazz.defineMethod (c$, "contains", 
function (x, y, w, h) {
return false;
}, "~N,~N,~N,~N");
Clazz.defineMethod (c$, "contains", 
function (r) {
return false;
}, "jsjava.awt.geom.Rectangle2D");
Clazz.overrideMethod (c$, "getBounds", 
function () {
return this.getBounds2D ().getBounds ();
});
Clazz.defineMethod (c$, "getPathIterator", 
function (at) {
return  new jsjava.awt.geom.LineIterator (this, at);
}, "jsjava.awt.geom.AffineTransform");
Clazz.defineMethod (c$, "getPathIterator", 
function (at, flatness) {
return  new jsjava.awt.geom.LineIterator (this, at);
}, "jsjava.awt.geom.AffineTransform,~N");
Clazz.defineMethod (c$, "clone", 
function () {
try {
return Clazz.superCall (this, jsjava.awt.geom.Line2D, "clone", []);
} catch (e) {
if (Clazz.exceptionOf (e, CloneNotSupportedException)) {
throw  new InternalError ();
} else {
throw e;
}
}
});
Clazz.pu$h ();
c$ = Clazz.decorateAsClass (function () {
this.x1 = 0;
this.y1 = 0;
this.x2 = 0;
this.y2 = 0;
Clazz.instantialize (this, arguments);
}, jsjava.awt.geom.Line2D, "Float", jsjava.awt.geom.Line2D);
Clazz.makeConstructor (c$, 
function () {
Clazz.superConstructor (this, jsjava.awt.geom.Line2D.Float, []);
});
Clazz.makeConstructor (c$, 
function (a, b, c, d) {
Clazz.superConstructor (this, jsjava.awt.geom.Line2D.Float, []);
this.setLine (a, b, c, d);
}, "~N,~N,~N,~N");
Clazz.makeConstructor (c$, 
function (a, b) {
Clazz.superConstructor (this, jsjava.awt.geom.Line2D.Float, []);
this.setLine (a, b);
}, "jsjava.awt.geom.Point2D,jsjava.awt.geom.Point2D");
Clazz.overrideMethod (c$, "getX1", 
function () {
return this.x1;
});
Clazz.overrideMethod (c$, "getY1", 
function () {
return this.y1;
});
Clazz.overrideMethod (c$, "getP1", 
function () {
return  new jsjava.awt.geom.Point2D.Float (this.x1, this.y1);
});
Clazz.overrideMethod (c$, "getX2", 
function () {
return this.x2;
});
Clazz.overrideMethod (c$, "getY2", 
function () {
return this.y2;
});
Clazz.overrideMethod (c$, "getP2", 
function () {
return  new jsjava.awt.geom.Point2D.Float (this.x2, this.y2);
});
Clazz.defineMethod (c$, "setLine", 
function (a, b, c, d) {
this.x1 = a;
this.y1 = b;
this.x2 = c;
this.y2 = d;
}, "~N,~N,~N,~N");
Clazz.defineMethod (c$, "setLine", 
function (a, b, c, d) {
this.x1 = a;
this.y1 = b;
this.x2 = c;
this.y2 = d;
}, "~N,~N,~N,~N");
Clazz.overrideMethod (c$, "getBounds2D", 
function () {
var a;
var b;
var c;
var d;
if (this.x1 < this.x2) {
a = this.x1;
c = this.x2 - this.x1;
} else {
a = this.x2;
c = this.x1 - this.x2;
}if (this.y1 < this.y2) {
b = this.y1;
d = this.y2 - this.y1;
} else {
b = this.y2;
d = this.y1 - this.y2;
}return  new jsjava.awt.geom.Rectangle2D.Float (a, b, c, d);
});
c$ = Clazz.p0p ();
Clazz.pu$h ();
c$ = Clazz.decorateAsClass (function () {
this.x1 = 0;
this.y1 = 0;
this.x2 = 0;
this.y2 = 0;
Clazz.instantialize (this, arguments);
}, jsjava.awt.geom.Line2D, "Double", jsjava.awt.geom.Line2D);
Clazz.makeConstructor (c$, 
function () {
Clazz.superConstructor (this, jsjava.awt.geom.Line2D.Double, []);
});
Clazz.makeConstructor (c$, 
function (a, b, c, d) {
Clazz.superConstructor (this, jsjava.awt.geom.Line2D.Double, []);
this.setLine (a, b, c, d);
}, "~N,~N,~N,~N");
Clazz.makeConstructor (c$, 
function (a, b) {
Clazz.superConstructor (this, jsjava.awt.geom.Line2D.Double, []);
this.setLine (a, b);
}, "jsjava.awt.geom.Point2D,jsjava.awt.geom.Point2D");
Clazz.overrideMethod (c$, "getX1", 
function () {
return this.x1;
});
Clazz.overrideMethod (c$, "getY1", 
function () {
return this.y1;
});
Clazz.overrideMethod (c$, "getP1", 
function () {
return  new jsjava.awt.geom.Point2D.Double (this.x1, this.y1);
});
Clazz.overrideMethod (c$, "getX2", 
function () {
return this.x2;
});
Clazz.overrideMethod (c$, "getY2", 
function () {
return this.y2;
});
Clazz.overrideMethod (c$, "getP2", 
function () {
return  new jsjava.awt.geom.Point2D.Double (this.x2, this.y2);
});
Clazz.defineMethod (c$, "setLine", 
function (a, b, c, d) {
this.x1 = a;
this.y1 = b;
this.x2 = c;
this.y2 = d;
}, "~N,~N,~N,~N");
Clazz.overrideMethod (c$, "getBounds2D", 
function () {
var a;
var b;
var c;
var d;
if (this.x1 < this.x2) {
a = this.x1;
c = this.x2 - this.x1;
} else {
a = this.x2;
c = this.x1 - this.x2;
}if (this.y1 < this.y2) {
b = this.y1;
d = this.y2 - this.y1;
} else {
b = this.y2;
d = this.y1 - this.y2;
}return  new jsjava.awt.geom.Rectangle2D.Double (a, b, c, d);
});
c$ = Clazz.p0p ();
});
