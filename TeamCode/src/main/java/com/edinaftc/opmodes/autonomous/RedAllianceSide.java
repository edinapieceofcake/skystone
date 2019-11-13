package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.motion.Mecanum;
import com.edinaftc.library.vision.VuforiaCamera;
import com.edinaftc.skystone.vision.SkyStoneDetector;
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

    public AutonomousStates DriveToFirstBlock() {
        _mecanum.SlideRightRunToPosition(.5, 1650, this);

        _flap.setPosition(1);

        switch (_skyStoneDetector.getLocation()) {
            case left:
                _mecanum.MoveBackwardsRunToPosition(0.5, 450, this);
                break;

            case middle:
                _mecanum.MoveForwardRunToPosition(0.5, 100, this);
                break;

            case right:
                _mecanum.MoveForwardRunToPosition(0.5, 450, this);
                break;
        }

        sleep(500); // need time for flap to open
        
        return AutonomousStates.DRIVEN_TO_FIRST_BLOCK;
    }

    public AutonomousStates DriveToSecondBlock() {

        _flap.setPosition(1);
        sleep(2000);
        return AutonomousStates.DRIVEN_TO_SECOND_BLOCK;
    }

    public AutonomousStates PickUpFirstBlock() {
        PickUpBlock();

        return AutonomousStates.PICKED_UP_FIRST_BLOCK;
    }

    public AutonomousStates PickUpSecondBlock() {
        PickUpBlock();

        return AutonomousStates.PICKED_UP_SECOND_BLOCK;
    }

    private void PickUpBlock() {
        _arm.setPosition(1);
        sleep(1000);
        _flap.setPosition(0);
        sleep(1000);

        _arm.setPosition(0);
        sleep(1000);
    }

    public AutonomousStates DriveToBridgeForFirstBlock() {
        //_mecanum.MoveForwardRunToPosition(0.5, 2000, this);
        return AutonomousStates.DRIVEN_TO_BRIDGE_FOR_FIRST_BLOCK;
    }

    public AutonomousStates DriveToBridgeForSecondBlock() {
        return AutonomousStates.DRIVEN_TO_BRIDGE_FOR_SECOND_BLOCK;
    }

    public AutonomousStates DriveUnderBridge() {
        //_mecanum.MoveBackwardsRunToPosition(1, 750, this);
        return AutonomousStates.DRIVEN_UNDER_BRIDGE;
    }

    private void DropOffBlock() {
        _arm.setPosition(1);
        sleep(1000);
        _flap.setPosition(1);
        sleep(1000);

        _arm.setPosition(0);
        sleep(1000);
        _flap.setPosition(0);
        sleep(1000);
    }

    public AutonomousStates DropOffFirstBlock() {
        DropOffBlock();

        return AutonomousStates.DROPPED_OFF_FIRST_BLOCK;
    }

    public AutonomousStates DropOffSecondBlock() {
        DropOffBlock();

        return AutonomousStates.DROPPED_OFF_SECOND_BLOCK;
    }

    public void runOpMode() {
        AutonomousStates currentState = AutonomousStates.STARTED;
        _skyStoneDetector = new SkyStoneDetector();
        _camera = new VuforiaCamera();

        _mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"),
                hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br"), telemetry);
        _arm = hardwareMap.servo.get("rightArm");
        _flap = hardwareMap.servo.get("rightFlap");

        _camera.addTracker(_skyStoneDetector);
        _skyStoneDetector.cx0 = 880;
        _skyStoneDetector.cy0 = 400;
        _skyStoneDetector.cx1 = 540;
        _skyStoneDetector.cy1 = 400;
        _skyStoneDetector.cx2 = 200;
        _skyStoneDetector.cy2 = 400;

        _camera.initialize();

        while (!isStarted()) {
            synchronized (this) {
                try {
                    telemetry.addData("location ", _skyStoneDetector.getLocation());
                    telemetry.update();
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        while (opModeIsActive() && (currentState != AutonomousStates.DRIVEN_UNDER_BRIDGE)) {
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