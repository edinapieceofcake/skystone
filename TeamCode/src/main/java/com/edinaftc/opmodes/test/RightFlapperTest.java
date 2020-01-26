package com.edinaftc.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.edinaftc.library.Stickygamepad;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp()
//@Disabled
@Config
public class RightFlapperTest extends OpMode {
    Stickygamepad _gamepad1;
    Servo _rightArm;
    Servo _rightFlap;

    public static double XPOSITION = 0;
    public static double YPOSITION = 1;

    @Override
    public void init() {
        _gamepad1 = new Stickygamepad(gamepad1);
        _rightArm = hardwareMap.servo.get("rightArm");
        _rightFlap = hardwareMap.servo.get("rightFlap");
    }

    @Override
    public void loop() {
        _gamepad1.update();

        if (_gamepad1.x) {
            _rightFlap.setPosition(XPOSITION);
        }

        if (_gamepad1.y) {
            _rightFlap.setPosition(YPOSITION);
        }

        if (_gamepad1.b) {
            _rightArm.setPosition(0);
        }

        if (_gamepad1.a) {
            _rightArm.setPosition(1);
        }

        if (_gamepad1.dpad_right) {
            _rightFlap.setPosition(.5);
        }

        telemetry.addData("rarm", "%f", _rightArm.getPosition());
        telemetry.addData("rflap", "%f", _rightFlap.getPosition());

        telemetry.update();
    }
}
