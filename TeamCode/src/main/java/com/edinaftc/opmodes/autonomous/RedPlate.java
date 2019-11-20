package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.Stickygamepad;
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
    private Stickygamepad _gamepad1;

    @Override
    public void runOpMode() {
        long sleepTime = 0;

        _mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"),
                hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br"), telemetry);
        //_mecanum.StopResetEncodersAndRunToPosition();
        _left = hardwareMap.servo.get("blhook");
        _right = hardwareMap.servo.get("brhook");
        _gamepad1 = new Stickygamepad(gamepad1);

        hardwareMap.servo.get("rightArm").setPosition(0);
        hardwareMap.servo.get("rightFlap").setPosition(0);
        hardwareMap.servo.get("leftArm").setPosition(1);
        hardwareMap.servo.get("leftFlap").setPosition(1);

        while (!isStarted()) {
            synchronized (this) {
                try {
                    _gamepad1.update();
                    if (_gamepad1.left_bumper) {
                        if (sleepTime > 0) {
                            sleepTime -= 500;
                        }
                    } else if (_gamepad1.right_bumper) {
                        if (sleepTime < 9000) {
                            sleepTime += 500;
                        }
                    }

                    telemetry.addData("use left/right bumper to adjust sleep time", "");
                    telemetry.addData("sleep time (ms)", sleepTime);
                    telemetry.update();
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        sleep(sleepTime);

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
