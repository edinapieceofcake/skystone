package com.edinaftc.library.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Grabber extends Subsystem {
    private boolean frontGrabberOpen = false;
    private boolean backGrabberOpen = false;
    private Servo front;
    private Servo back;
    private boolean _canUpdate = false;

    public Grabber(HardwareMap map) {
        front = map.servo.get("fg");
        back = map.servo.get("bg");
    }

    public void update() {
        if (_canUpdate) {
            if (frontGrabberOpen) {
                front.setPosition(1);
            } else {
                front.setPosition(0);
            }

            if (backGrabberOpen) {
                back.setPosition(1);
            } else {
                back.setPosition(0);
            }
        }
    }

    public void turnOnUpdate() {
        _canUpdate = true;
    }

    public void toggleBothGrabbers() {
        if(frontGrabberOpen == false && backGrabberOpen == false) {
            frontGrabberOpen = true;
            backGrabberOpen = true;
        } else {
            frontGrabberOpen = false;
            backGrabberOpen = false;
        }
    }

    public void loadBlock() {
        frontGrabberOpen = false;
        backGrabberOpen = true;
    }
}
