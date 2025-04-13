package com.epic;

public class Controller {

    public double x, y, z, rotation, xa, za, rotationa;
    public static boolean turnRight = false;
    public static boolean turnLeft = false;
    public static boolean walk = false;
    public static boolean crouch;
    public static boolean run;
    public static boolean forward;

    public void tick(boolean forward, boolean backward, boolean right, boolean left, boolean jump, boolean crouch,
            boolean run) {
        Controller.crouch = crouch;
        Controller.run = run;
        Controller.forward = forward;

        double rotationSpeed = 0.025;
        double jumpHeight = 0.5;
        double crouchHeight = 0.35;
        double walkSpeed = 0.75;
        double zMove = 0;
        double xMove = 0;

        if (forward) {
            zMove += walkSpeed;
            walk = true;
        }

        if (backward) {
            zMove -= walkSpeed;
            walk = true;
        }

        if (right) {
            xMove += walkSpeed;
            walk = true;
        }

        if (left) {
            xMove -= walkSpeed;
            walk = true;
        }

        if (turnRight) {
            rotationa += rotationSpeed;
            walk = true;
        }

        if (turnLeft) {
            rotationa -= rotationSpeed;
            walk = true;
        }

        if (jump) {
            y += jumpHeight;
        }

        if (crouch) {
            walkSpeed = walkSpeed / 2;
            y -= crouchHeight;
        }

        if (run && !crouch && forward) {
            walkSpeed *= 2;
        }

        if (!forward && !backward && !right && !left && !turnRight && !turnLeft) {
            walk = false;
            Controller.crouch = false;
            Controller.run = false;
        }

        xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
        za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

        x += xa;
        y *= 0.9;
        z += za;
        xa *= 0.1;
        za *= 0.1;
        rotation += rotationa;
        rotationa *= 0.5;
    }
}
