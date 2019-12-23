package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.Stickygamepad;
import com.edinaftc.library.motion.Mecanum;
import com.edinaftc.library.vision.VuforiaCamera;
import com.edinaftc.skystone.vision.SkyStoneDetector;
import com.edinaftc.skystone.vision.SkystoneLocation;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="God Blue Alliance", group="Autonomous")
public class GodBlueRR extends LinearOpMode {
    private VuforiaCamera _camera;
    private SkyStoneDetector _skyStoneDetector;
    private Servo _arm;
    private Servo _flap;
    private Servo _left;
    private Servo _right;
    private SkystoneLocation _location = SkystoneLocation.left;
    private double motorPower = 1;
    private Stickygamepad _gamepad1;

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
        TURNED_LEFT_TOWARDS_WALL,
        BACKED_AND_GRABBED_PLATE,
        PULLED_AND_TURNED_LEFT_WITH_PLATE,
        DRIVEN_UNDER_BRIDGE
    }

    public void runOpMode() {
        GodBlueRR.AutonomousStates currentState = GodBlueRR.AutonomousStates.STARTED;
        long sleepTime = 0;

        _skyStoneDetector = new SkyStoneDetector();
        _camera = new VuforiaCamera();

        _gamepad1 = new Stickygamepad(gamepad1);
        _arm = hardwareMap.servo.get("rightArm");
        _flap = hardwareMap.servo.get("rightFlap");
        _left = hardwareMap.servo.get("blhook");
        _right = hardwareMap.servo.get("brhook");

        _camera.addTracker(_skyStoneDetector);
        _skyStoneDetector.cx0 = 190;
        _skyStoneDetector.cy0 = 210;
        _skyStoneDetector.cx1 = 520;
        _skyStoneDetector.cy1 = 210;
        _skyStoneDetector.cx2 = 810;
        _skyStoneDetector.cy2 = 210;

        _camera.initialize();

        _flap.setPosition(0);

        hardwareMap.servo.get("leftFlap").setPosition(1);

        while (!isStarted()) {
            synchronized (this) {
                try {
                    _location = _skyStoneDetector.getLocation();
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
                    telemetry.addData("location ", _location);
                    telemetry.update();
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        hardwareMap.servo.get("leftArm").setPosition(1);

        sleep(sleepTime);

        while (opModeIsActive() && (currentState != GodBlueRR.AutonomousStates.DRIVEN_UNDER_BRIDGE)) {
        }
    }
}