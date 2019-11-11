package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.motion.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class RedPlate extends LinearOpMode {
    Mecanum _mecanum;
    private Servo _left;
    private Servo _right;

    @Override
    public void runOpMode() {
        _mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"),
                hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br"), telemetry);
        //_mecanum.StopResetEncodersAndRunToPosition();
        _left = hardwareMap.servo.get("blhook");
        _right = hardwareMap.servo.get("brhook");

        waitForStart();
        _mecanum.MoveBackwardsRunToPosition(0.5, 500, this);
        _mecanum.SlideLeftRunToPosition(0.5, 300, this);
        _mecanum.MoveBackwardsRunToPosition(0.5, 1400, this);
        DropHooks();
        sleep(500);
        _mecanum.MoveForwardRunToPosition(0.5, 1900, this);
        LiftHooks();
        sleep(250);
        _mecanum.SlideRightRunToPosition(0.5, 1800, this);
        _mecanum.MoveBackwardsRunToPosition(0.5, 1200, this);
        _mecanum.SlideLeftRunToPosition(0.5, 700, this);
        _mecanum.DiagonalRightAndUpRunToPosition(0.5, 1700, this);
        _mecanum.SlideRightRunToPosition(0.5, 800, this);
        _mecanum.MoveForwardRunToPosition(0.5, 300, this);

    }

    public void DropHooks() {
        _left.setPosition(.55);
        _right.setPosition(0.3);
    }

    public void LiftHooks() {
        _left.setPosition(0);
        _right.setPosition(0.9);
    }
}
