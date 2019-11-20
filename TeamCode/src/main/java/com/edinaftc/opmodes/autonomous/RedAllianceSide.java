package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.motion.Mecanum;
import com.edinaftc.library.vision.VuforiaCamera;
import com.edinaftc.skystone.vision.SkyStoneDetector;
import com.edinaftc.skystone.vision.SkystoneLocation;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Red Alliance Block Side", group="Autonomous")
public class RedAllianceSide extends LinearOpMode {
    private Mecanum _mecanum;
    private VuforiaCamera _camera;
    private SkyStoneDetector _skyStoneDetector;
    private Servo _arm;
    private Servo _flap;
    private SkystoneLocation _location = SkystoneLocation.left;
    private double motorPower = 0.5;

    public enum AutonomousStates{
        STARTED,
        DRIVEN_TO_FIRST_BLOCK,
        PICKED_UP_FIRST_BLOCK,
        DRIVEN_TO_BRIDGE_FOR_FIRST_BLOCK,
        DROPPED_OFF_FIRST_BLOCK,
        DRIVEN_TO_SECOND_BLOCK,
        PICKED_UP_SECOND_BLOCK,
        DRIVEN_TO_BRIDGE_FOR_SECOND_BLOCK,
        DROPPED_OFF_SECOND_BLOCK,
        DRIVEN_UNDER_BRIDGE
    }

    public BlueAllianceSide.AutonomousStates DriveToFirstBlock() {
        _mecanum.SlideLeftRunToPosition(.5, 1725, this);

        _flap.setPosition(0);

        switch (_location) {
            case left:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 1125, this);
                break;

            case middle:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 575, this);
                break;

            case right:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 100, this);
                break;
        }

        sleep(500); // need time for flap to open

        return BlueAllianceSide.AutonomousStates.DRIVEN_TO_FIRST_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates DriveToSecondBlock() {
        switch (_location) {
            case left:
                _mecanum.MoveBackwardsRunWithEncoders(motorPower, 4575, this);
                break;

            case middle:
                _mecanum.MoveBackwardsRunWithEncoders(motorPower, 5075, this);
                break;

            case right:
                _mecanum.MoveBackwardsRunWithEncoders(motorPower, 5550, this);
                break;
        }

        _flap.setPosition(0);
        sleep(500); // need time for flap to open
        return BlueAllianceSide.AutonomousStates.DRIVEN_TO_SECOND_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates PickUpFirstBlock() {
        PickUpBlock();

        return BlueAllianceSide.AutonomousStates.PICKED_UP_FIRST_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates PickUpSecondBlock() {
        _arm.setPosition(.35);
        sleep(350);
        _mecanum.SlideLeftRunWithEncoders(.5, 300, this);
        _arm.setPosition(0);
        sleep(150);
        _flap.setPosition(1);
        sleep(500);
        _arm.setPosition(1);
        sleep(500);

        _mecanum.SlideRightRunWithEncoders(.5, 300, this);
        return BlueAllianceSide.AutonomousStates.PICKED_UP_SECOND_BLOCK;
    }

    private void PickUpBlock() {
        _arm.setPosition(0);
        sleep(500);
        _flap.setPosition(1);
        sleep(500);

        _arm.setPosition(1);
        sleep(500);
    }

    public BlueAllianceSide.AutonomousStates DriveToBridgeForFirstBlock() {
        switch (_location) {
            case left:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 3000, this);
                break;

            case middle:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 3500, this);
                break;

            case right:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 3950, this);
                break;
        }

        return BlueAllianceSide.AutonomousStates.DRIVEN_TO_BRIDGE_FOR_FIRST_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates DriveToBridgeForSecondBlock() {
        switch (_location) {
            case left:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 4575, this);
                break;

            case middle:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 5075, this);
                break;

            case right:
                _mecanum.MoveForwardRunWithEncoders(motorPower, 5550, this);
                break;
        }

        return BlueAllianceSide.AutonomousStates.DRIVEN_TO_BRIDGE_FOR_SECOND_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates DriveUnderBridge() {
        _mecanum.MoveBackwardsRunWithEncoders(motorPower, 1450, this);
        return BlueAllianceSide.AutonomousStates.DRIVEN_UNDER_BRIDGE;
    }

    private void DropOffBlock() {
        _flap.setPosition(0);
        sleep(500);

        _flap.setPosition(1);
        sleep(500);
    }

    public BlueAllianceSide.AutonomousStates DropOffFirstBlock() {
        DropOffBlock();

        return BlueAllianceSide.AutonomousStates.DROPPED_OFF_FIRST_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates DropOffSecondBlock() {
        DropOffBlock();

        return BlueAllianceSide.AutonomousStates.DROPPED_OFF_SECOND_BLOCK;
    }

    public void runOpMode() {
        BlueAllianceSide.AutonomousStates currentState = BlueAllianceSide.AutonomousStates.STARTED;
        _skyStoneDetector = new SkyStoneDetector();
        _camera = new VuforiaCamera();

        _mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"),
                hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br"), telemetry);
        _arm = hardwareMap.servo.get("leftArm");
        _flap = hardwareMap.servo.get("leftFlap");

        _camera.addTracker(_skyStoneDetector);
        _skyStoneDetector.cx0 = 330;
        _skyStoneDetector.cy0 = 420;
        _skyStoneDetector.cx1 = 640;
        _skyStoneDetector.cy1 = 420;
        _skyStoneDetector.cx2 = 950;
        _skyStoneDetector.cy2 = 420;

        _camera.initialize();

        _arm.setPosition(1);
        _flap.setPosition(1);

        hardwareMap.servo.get("rightArm").setPosition(0);
        hardwareMap.servo.get("rightFlap").setPosition(0);

        while (!isStarted()) {
            synchronized (this) {
                try {
                    _location = _skyStoneDetector.getLocation();
                    telemetry.addData("location ", _location);
                    telemetry.update();
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        while (opModeIsActive() && (currentState != BlueAllianceSide.AutonomousStates.DRIVEN_UNDER_BRIDGE)) {
            switch (currentState) {
                case STARTED:
                    currentState = DriveToFirstBlock();
                    break;
                case DRIVEN_TO_FIRST_BLOCK:
                    currentState = PickUpFirstBlock();
                    break;
                case PICKED_UP_FIRST_BLOCK:
                    currentState = DriveToBridgeForFirstBlock();
                    break;
                case DRIVEN_TO_BRIDGE_FOR_FIRST_BLOCK:
                    currentState = DropOffFirstBlock();
                    break;
                case DROPPED_OFF_FIRST_BLOCK:
                    currentState = DriveToSecondBlock();
                    break;
                case DRIVEN_TO_SECOND_BLOCK:
                    currentState = PickUpSecondBlock();
                    break;
                case PICKED_UP_SECOND_BLOCK:
                    currentState = DriveToBridgeForSecondBlock();
                    break;
                case DRIVEN_TO_BRIDGE_FOR_SECOND_BLOCK:
                    currentState = DropOffSecondBlock();
                    break;
                case DROPPED_OFF_SECOND_BLOCK:
                    currentState = DriveUnderBridge();
                    break;
            }
        }

        _camera.close();
    }

}