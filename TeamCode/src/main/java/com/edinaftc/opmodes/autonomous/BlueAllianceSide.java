package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.motion.Mecanum;
import com.edinaftc.library.vision.VuforiaCamera;
import com.edinaftc.skystone.vision.SkyStoneDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Blue Alliance Block Side", group="Autonomous")
public class BlueAllianceSide extends LinearOpMode {
    private Mecanum _mecanum;
    private VuforiaCamera _camera;
    private SkyStoneDetector _skyStoneDetector;

    public enum AutonomousStates{
        STARTED,
        DRIVEN_TO_BLOCK,
        PICKED_UP_BLOCK,
        DRIVEN_TO_BRIDGE,
        DRIVEN_UNDER_BRIDGE
    }

    public AutonomousStates DriveToBlock() {
        _mecanum.SlideRightRunWithEncoders(.5, 3600, this);

        switch (_skyStoneDetector.getLocation())
        {
            case left:
                break;

            case middle:
                break;

            case right:
                break;
        }

        return AutonomousStates.DRIVEN_TO_BLOCK;
    }

    public AutonomousStates PickUpBlock() {
        Servo rightArm = hardwareMap.servo.get("rightArm");
        Servo rightPaddle = hardwareMap.servo.get("rightPaddle");

        rightArm.setPosition(1);
        sleep(1000);
        rightPaddle.setPosition(0);
        sleep(1000);

        rightArm.setPosition(0);
        sleep(1000);
        return AutonomousStates.PICKED_UP_BLOCK;
    }

    public AutonomousStates DriveToBridge() {
        _mecanum.SlideLeftRunWithEncoders(1, 1800, this);
        return AutonomousStates.DRIVEN_TO_BRIDGE;
    }

    public AutonomousStates DriveUnderBridge() {
        _mecanum.MoveForwardRunToPosition(1, 10800, this);
        return AutonomousStates.DRIVEN_UNDER_BRIDGE;
    }

    public void runOpMode() {
        AutonomousStates currentState = AutonomousStates.STARTED;
        _skyStoneDetector = new SkyStoneDetector();
        _camera = new VuforiaCamera();

        _mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"),
                hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br"), telemetry);

        _camera.addTracker(_skyStoneDetector);
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
                    currentState = DriveToBlock();
                    break;
                case DRIVEN_TO_BLOCK:
                    currentState = PickUpBlock();
                    break;
                case PICKED_UP_BLOCK:
                    currentState = DriveToBridge();
                    break;
                case DRIVEN_TO_BRIDGE:
                    currentState = DriveUnderBridge();
                    break;

            }
        }

        _camera.close();
    }
}
