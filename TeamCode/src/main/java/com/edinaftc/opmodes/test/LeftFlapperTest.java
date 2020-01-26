package com.edinaftc.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.edinaftc.library.Stickygamepad;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp()
//@Disabled
@Config
public class LeftFlapperTest extends OpMode {
    Stickygamepad _gamepad1;
    Servo _leftArm;
    Servo _leftFlap;

    public static double XPOSITION = 0;
    public static double YPOSITION = 1;

    @Override
    public void init() {
        _gamepad1 = new Stickygamepad(gamepad1);
        _leftArm = hardwareMap.servo.get("leftArm");
        _leftFlap = hardwareMap.servo.get("leftFlap");
    }

    @Override
    public void loop() {
        _gamepad1.update();

        if (_gamepad1.x) {
            _leftFlap.setPosition(XPOSITION);
        }

        if (_gamepad1.y) {
            _leftFlap.setPosition(YPOSITION);
        }

        if (_gamepad1.b) {
            _leftArm.setPosition(0);
        }

        if (_gamepad1.a) {
            _leftArm.setPosition(1);
        }

        if (_gamepad1.dpad_right) {
            _leftFlap.setPosition(.55);
        }

        telemetry.addData("larm", "%f", _leftArm.getPosition());
        telemetry.addData("lflap", "%f", _leftFlap.getPosition());

        telemetry.update();
    }
}
