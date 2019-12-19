package com.edinaftc.opmodes.test;

import com.edinaftc.library.Stickygamepad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class ArmSpeedAndLocationTest extends LinearOpMode {
    private DcMotor dummyarm = null;
    private CRServo arm = null;
    private DistanceSensor distanceSensor = null;
    private double currentPower = 0.3;
    private int armConstant = 16324;
    private int armLocation = 16324;
    boolean moveArm = false;

    public void runOpMode() {
        Stickygamepad _gamepad1 = new Stickygamepad(gamepad1);
        dummyarm = hardwareMap.dcMotor.get("arm");
        dummyarm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm = hardwareMap.crservo.get("crarm");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "testdetector");

        waitForStart();

        while (opModeIsActive()) {
            _gamepad1.update();

            if (moveArm) {
                moveArm();
            } else {

                if (gamepad1.right_stick_y < 0) {
                    if (gamepad1.right_stick_button) {
                        currentPower = .8;
                    } else {
                        currentPower = .3;
                    }

                    arm.setPower((currentPower));
                } else if (gamepad1.right_stick_y > 0) {
                    if (gamepad1.right_stick_button) {
                        currentPower = .8;
                    } else {
                        currentPower = .3;
                    }

                    arm.setPower(-currentPower * gamepad1.right_stick_y);
                } else {
                    arm.setPower(0);
                }
            }

            armLocation = armConstant + ((int)(distanceSensor.getDistance(DistanceUnit.CM) - 3.0) * 735);
            if (armLocation < armConstant) {
                armLocation = armConstant;
            }

            if (_gamepad1.y && !moveArm){
                moveArm = true;
            } else if (_gamepad1.a) {
                moveArm = false;
            }

            telemetry.addData("arm power, location", "%f %d", currentPower, dummyarm.getCurrentPosition());
            telemetry.addData("distance, arm", "%f %d", distanceSensor.getDistance(DistanceUnit.CM), armLocation);
            telemetry.update();
        }
    }

    private void moveArm() {
        int pos = dummyarm.getCurrentPosition();
        if ((armLocation - pos) > 0) {
            if (Math.abs(armLocation - pos) < 150) {
                // close enough so stop
                moveArm = false;
                arm.setPower(0);
            } else if (Math.abs(armLocation - pos) < 1000) {
                arm.setPower(currentPower / 2);
            } else {
                arm.setPower(currentPower);
            }
        } else if ((armLocation - pos) < 0) {
            if (Math.abs(armLocation - pos) < 150) {
                // close enough so stop
                moveArm = false;
                arm.setPower(0);
            } else if (Math.abs(armLocation - pos) < 1000) {
                arm.setPower(-currentPower / 2);
            } else {
                // move down
                arm.setPower(-currentPower);
            }
        }
    }
}
