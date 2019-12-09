package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.motion.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class RedPlatePullTurnPark extends LinearOpMode {

    private Mecanum _mecanum;
    private Servo _left;
    private Servo _right;

    public void runOpMode() {
        _mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"),
                hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br"), telemetry);
        //_mecanum.StopResetEncodersAndRunToPosition();
        _left = hardwareMap.servo.get("blhook");
        _right = hardwareMap.servo.get("brhook");

        waitForStart();

        _mecanum.MoveBackwardsRunToPosition(0.5, 500, this);
        _mecanum.SlideLeftRunToPosition(0.5, 600, this);
        _mecanum.MoveBackwardsRunToPosition(0.5, 1500, this);
        DropHooks();
        sleep(1000);
        _mecanum.MoveForwardRunToPosition(0.5, 1000, this);
        _mecanum.TurnRightRunToPosition(0.5,900, this);
        _mecanum.MoveBackwardsRunToPosition(0.5,500,this);
        LiftHooks();
        sleep(1000);
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
