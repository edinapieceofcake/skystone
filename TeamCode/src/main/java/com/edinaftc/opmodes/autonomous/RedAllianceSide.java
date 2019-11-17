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
                _mecanum.MoveForwardRunToPosition(0.5, 1100, this);
                break;

            case middle:
                _mecanum.MoveForwardRunToPosition(0.5, 600, this);
                break;

            case right:
                _mecanum.MoveForwardRunToPosition(0.5, 150, this);
                break;
        }

        sleep(500); // need time for flap to open

        return BlueAllianceSide.AutonomousStates.DRIVEN_TO_FIRST_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates DriveToSecondBlock() {
        switch (_location) {
            case left:
                _mecanum.MoveBackwardsRunToPosition(0.5, 4550, this);
                break;

            case middle:
                _mecanum.MoveBackwardsRunToPosition(0.5, 4950, this);
                break;

            case right:
                _mecanum.MoveBackwardsRunToPosition(0.5, 5300, this);
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
        PickUpBlock();

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
                _mecanum.MoveForwardRunToPosition(0.5, 3000, this);
                break;

            case middle:
                _mecanum.MoveForwardRunToPosition(0.5, 3500, this);
                break;

            case right:
                _mecanum.MoveForwardRunToPosition(0.5, 3850, this);
                break;
        }

        return BlueAllianceSide.AutonomousStates.DRIVEN_TO_BRIDGE_FOR_FIRST_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates DriveToBridgeForSecondBlock() {
        switch (_location) {
            case left:
                _mecanum.MoveForwardRunToPosition(0.5, 3550, this);
                break;

            case middle:
                _mecanum.MoveForwardRunToPosition(0.5, 3950, this);
                break;

            case right:
                _mecanum.MoveForwardRunToPosition(0.5, 4500, this);
                break;
        }

        return BlueAllianceSide.AutonomousStates.DRIVEN_TO_BRIDGE_FOR_SECOND_BLOCK;
    }

    public BlueAllianceSide.AutonomousStates DriveUnderBridge() {
        _mecanum.MoveBackwardsRunToPosition(0.5, 650, this);
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