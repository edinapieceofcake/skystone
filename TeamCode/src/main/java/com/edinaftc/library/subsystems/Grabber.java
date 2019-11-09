package com.edinaftc.library.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Grabber extends Subsystem {

    private enum Location{
        zerodegrees,
        ninetydegrees,
        oneeightydegrees
    }

    private boolean frontGrabberOpen = false;
    private boolean backGrabberOpen = false;
    private Location location = Location.zerodegrees;
    private Servo front;
    private Servo back;
    private Servo rotate;

    public Grabber(HardwareMap map) {
        front = map.servo.get("fg");
        back = map.servo.get("bg");
        rotate = map.servo.get("rg");
    }

    public void update() {
        if(frontGrabberOpen) {
            front.setPosition(1);
        } else {
            front.setPosition(0);
        }

        if(backGrabberOpen) {
            back.setPosition(1);
        } else {
            back.setPosition(0);
        }

        if(location == Location.zerodegrees) {
            rotate.setPosition(0);
        } else if(location == Location.ninetydegrees) {
            rotate.setPosition(.5);
        } else {
            rotate.setPosition(1);
        }
    }

    public void openBothGrabbers() {
        frontGrabberOpen = true;
        backGrabberOpen = true;
    }

    public void toggleFrontGrabber() {
        if(frontGrabberOpen = false) {
            frontGrabberOpen = true;
        } else {
            frontGrabberOpen = false;
        }
    }

    public void toggleBackGrabber() {
        if(backGrabberOpen = false) {
            backGrabberOpen = true;
        } else {
            backGrabberOpen = false;
        }
    }

    public void closeBothGrabbers() {
        frontGrabberOpen = false;
        backGrabberOpen = false;
    }

    public void rotateLeft() {
        if(location == Location.zerodegrees) {
            location = Location.ninetydegrees;
        } else if(location == Location.ninetydegrees) {
            location = Location.oneeightydegrees;
        }
    }

    public void rotateRight() {
        if(location == Location.oneeightydegrees) {
            location = Location.ninetydegrees;
        } else if(location == Location.ninetydegrees) {
            location = Location.zerodegrees;
        }
    }

}
