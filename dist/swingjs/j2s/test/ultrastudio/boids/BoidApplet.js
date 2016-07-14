Clazz.declarePackage ("test.ultrastudio.boids");
Clazz.load (["java.awt.event.ActionListener", "javax.swing.JApplet", "$.Timer"], "test.ultrastudio.boids.BoidApplet", ["java.awt.event.MouseAdapter", "test.ultrastudio.boids.Boid", "$.BoidCanvas", "$.Constants", "$.Flock"], function () {
c$ = Clazz.decorateAsClass (function () {
this.canvas = null;
this.flock = null;
this.step = 250;
this.timer = null;
this.prev = 0;
Clazz.instantialize (this, arguments);
}, test.ultrastudio.boids, "BoidApplet", javax.swing.JApplet, java.awt.event.ActionListener);
Clazz.prepareFields (c$, function () {
this.timer =  new javax.swing.Timer (this.step, this);
});
Clazz.overrideMethod (c$, "init", 
function () {
this.canvas =  new test.ultrastudio.boids.BoidCanvas ();
this.flock =  new test.ultrastudio.boids.Flock ();
this.canvas.flock = this.flock;
for (var n = 0; n < test.ultrastudio.boids.Constants.count; n++) {
var boid =  new test.ultrastudio.boids.Boid ();
boid.flock = this.flock;
this.flock.add (boid);
}
this.add (this.canvas, "Center");
this.timer.setRepeats (true);
this.timer.setCoalesce (true);
this.timer.start ();
this.canvas.addMouseListener (((Clazz.isClassDefined ("test.ultrastudio.boids.BoidApplet$1") ? 0 : test.ultrastudio.boids.BoidApplet.$BoidApplet$1$ ()), Clazz.innerTypeInstance (test.ultrastudio.boids.BoidApplet$1, this, null)));
});
Clazz.overrideMethod (c$, "actionPerformed", 
function (e) {
this.flock.$$size.height = this.getHeight ();
this.flock.$$size.width = this.getWidth ();
this.flock.iteration (this.step);
this.canvas.repaint (Clazz.doubleToInt (this.step / 2));
var now = System.currentTimeMillis ();
if (now - this.prev - this.step < Clazz.doubleToInt (this.step / 5)) {
if (this.step > 5) this.step = Clazz.doubleToInt (this.step / 2);
this.timer.setDelay (this.step);
}this.prev = now;
}, "java.awt.event.ActionEvent");
c$.$BoidApplet$1$ = function () {
Clazz.pu$h(self.c$);
c$ = Clazz.declareAnonymous (test.ultrastudio.boids, "BoidApplet$1", java.awt.event.MouseAdapter);
Clazz.overrideMethod (c$, "mousePressed", 
function (e) {
if (e.getButton () == 3) this.b$["test.ultrastudio.boids.BoidApplet"].flock.setCooperative (false);
 else this.b$["test.ultrastudio.boids.BoidApplet"].flock.setScared (true);
}, "java.awt.event.MouseEvent");
Clazz.overrideMethod (c$, "mouseReleased", 
function (e) {
this.b$["test.ultrastudio.boids.BoidApplet"].flock.setScared (false);
this.b$["test.ultrastudio.boids.BoidApplet"].flock.setCooperative (true);
}, "java.awt.event.MouseEvent");
c$ = Clazz.p0p ();
};
});
