package com.edinaftc.opmodes.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.edinaftc.library.Stickygamepad;
import com.edinaftc.library.motion.roadrunner.mecanum.SampleMecanumDriveBase;
import com.edinaftc.library.motion.roadrunner.mecanum.SampleMecanumDriveREVOptimized;
import com.edinaftc.library.vision.VuforiaCamera;
import com.edinaftc.skystone.vision.SkyStoneDetector;
import com.edinaftc.skystone.vision.SkystoneLocation;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="God Red Alliance", group="Autonomous")
@Config
public class GodRedRR extends LinearOpMode {
    private static double LEFTBLOCKX = 20;
    private static double LEFTBLOCKY = 30;
    private static double RIGHTBLOCKX = 1.5;
    private static double RIGHTBLOCKY = 32;
    private static double MIDDLEBLOCKX = 11.5;
    private static double MIDDLEBLOCKY = 30;

    public static double LEFTFIRSTBLOCKDRIVEDISTANCE = 88;
    public static double LEFTBACKWARDTOSECONDBLOCK = 110;
    public static double LEFTSECONDBLOCKDRIVEDISTANCE = 102;
    public static double LEFTBACKBEFORETURN = 12;

    private static double RIGHTBLOCKDRIVEDISTANCE = 100;
    private static double BACKWARDTORIGHTBLOCK = 126;
    private static double FRONTTORIGHTBLOCK = 126;

    private static double MIDDLEBLOCKDRIVEDISTANCE = 92;
    private static double BACKWARDTOMIDDLEBLOCK = 120;
    private static double FRONTTOMIDDLEBLOCK = 120;

    public static double STRAFETOPLATEFORFIRSTBLOCK = 9;
    public static double STRAFETOPLATEFORSECONDBLOCK = 7;
    public static double STRAFETOPICKUPFORSECONDBLOCK = 5;

    private VuforiaCamera _camera;
    private SkyStoneDetector _skyStoneDetector;
    private Servo _arm;
    private Servo _flap;
    private Servo _left;
    private Servo _right;
    private SkystoneLocation _location = SkystoneLocation.left;
    private double motorPower = 1;
    private Stickygamepad _gamepad1;
    private SampleMecanumDriveBase _drive;
    private DistanceSensor _distance;

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

    private AutonomousStates MoveToFirstBlock() {
        _flap.setPosition(0);
        _arm.setPosition(.4);

        switch (_location) {
            case left:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeTo(new Vector2d(LEFTBLOCKX, LEFTBLOCKY)) // 22,33
                                .build());
                break;

            case middle:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeTo(new Vector2d(MIDDLEBLOCKX, MIDDLEBLOCKY)) // 13.5,33
                                .build());
                break;

            case right:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeTo(new Vector2d(RIGHTBLOCKX, RIGHTBLOCKY)) // 5,33
                                .build());
                break;
        }

        _arm.setPosition(0);
        sleep(250);
        _drive.followTrajectorySync(
                _drive.trajectoryBuilder()
                        .strafeLeft(4)
                        .build());

        _flap.setPosition(1);
        sleep(350);
        _arm.setPosition(1);
        sleep(100);

        return AutonomousStates.PICKED_UP_FIRST_BLOCK;
    }

    public AutonomousStates DropOffFirstBlock() {
        switch (_location) {
            case left:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(6)
                                .forward(LEFTFIRSTBLOCKDRIVEDISTANCE)
                                .build());
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPLATEFORFIRSTBLOCK)
                                .build());
                break;

            case middle:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(6)
                                .forward(MIDDLEBLOCKDRIVEDISTANCE)
                                .build());
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPLATEFORFIRSTBLOCK)
                                .build());
                break;

            case right:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(6)
                                .forward(RIGHTBLOCKDRIVEDISTANCE)
                                .build());
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPLATEFORFIRSTBLOCK)
                                .build());
                break;
        }

        _flap.setPosition(0);
        _arm.setPosition(0);
        sleep(500);

        _flap.setPosition(1);
        _arm.setPosition(1);

        return AutonomousStates.DROPPED_OFF_FIRST_BLOCK;
    }

    private AutonomousStates MoveToSecondBlock() {
        switch (_location) {
            case left:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(2)
                                .back(LEFTBACKWARDTOSECONDBLOCK)
                                .build());
                _flap.setPosition(0);
                _arm.setPosition(.4);
                sleep(250);
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPICKUPFORSECONDBLOCK)
                                .build());
                break;

            case middle:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(2)
                                .back(BACKWARDTOMIDDLEBLOCK)
                                .build());
                _flap.setPosition(0);
                _arm.setPosition(.4);
                sleep(250);
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPICKUPFORSECONDBLOCK)
                                .build());
                break;

            case right:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(2)
                                .back(BACKWARDTORIGHTBLOCK)
                                .build());
                _flap.setPosition(0);
                _arm.setPosition(.4);
                sleep(250);
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPICKUPFORSECONDBLOCK)
                                .build());
                break;
        }

        _arm.setPosition(.3);
        sleep(250);
        _flap.setPosition(1);
        sleep(350);
        _arm.setPosition(1);
        sleep(250);

        return AutonomousStates.PICKED_UP_SECOND_BLOCK;
    }

    public AutonomousStates DropOffSecondBlock() {
        switch (_location) {
            case left:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(2)
                                .forward(LEFTSECONDBLOCKDRIVEDISTANCE)
                                .build());
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPLATEFORSECONDBLOCK)
                                .build());
                break;

            case middle:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(2)
                                .forward(FRONTTOMIDDLEBLOCK)
                                .build());
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPLATEFORSECONDBLOCK)
                                .build());
                break;

            case right:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeRight(2)
                                .forward(FRONTTORIGHTBLOCK)
                                .build());
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .strafeLeft(STRAFETOPLATEFORSECONDBLOCK)
                                .build());
                break;
        }

        _flap.setPosition(0);
        _arm.setPosition(0);
        sleep(500);

        _flap.setPosition(1);
        _arm.setPosition(1);

        _drive.followTrajectorySync(
                _drive.trajectoryBuilder()
                        .strafeRight(3)
                        .build());

        switch (_location) {
            case left:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .back(LEFTBACKBEFORETURN)
                                .build());
                break;

            case middle:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .back(16)
                                .build());
                break;

            case right:
                _drive.followTrajectorySync(
                        _drive.trajectoryBuilder()
                                .back(16)
                                .build());
                break;
        }


        _drive.turnSync(Math.toRadians(-90));

        _drive.followTrajectorySync(
                _drive.trajectoryBuilder()
                        .back(7)
                        .build());

        dropHooks();
        sleep(1000);

        _drive.followTrajectorySync(
                _drive.trajectoryBuilder()
                        .forward(26)
                        .build());

        _drive.turnWithTimeoutSync(Math.toRadians(-90), 4.0);

        liftHooks();

        _drive.followTrajectorySync(
                _drive.trajectoryBuilder()
                        .strafeRight(6)
                        .forward(30)
                        .build());

        return AutonomousStates.DRIVEN_UNDER_BRIDGE;
    }

    private void dropHooks() {
        _left.setPosition(.3);
        _right.setPosition(.6);
    }


    private void liftHooks() {
        _left.setPosition(.7);
        _right.setPosition(.17);
    }

    public void runOpMode() {
        GodRedRR.AutonomousStates currentState = GodRedRR.AutonomousStates.STARTED;
        long sleepTime = 0;

        _skyStoneDetector = new SkyStoneDetector();
        _camera = new VuforiaCamera();
        _distance = hardwareMap.get(DistanceSensor.class, "testdetector");

        _gamepad1 = new Stickygamepad(gamepad1);
        _arm = hardwareMap.servo.get("leftArm");
        _flap = hardwareMap.servo.get("leftFlap");
        _left = hardwareMap.servo.get("blhook");
        _right = hardwareMap.servo.get("brhook");

        _camera.addTracker(_skyStoneDetector);
        _skyStoneDetector.cx0 = 330;
        _skyStoneDetector.cy0 = 420;
        _skyStoneDetector.cx1 = 640;
        _skyStoneDetector.cy1 = 420;
        _skyStoneDetector.cx2 = 950;
        _skyStoneDetector.cy2 = 420;

        _camera.initialize();

        _flap.setPosition(1);

        hardwareMap.servo.get("rightArm").setPosition(0);
        hardwareMap.servo.get("rightFlap").setPosition(0);

        _drive = new SampleMecanumDriveREVOptimized(hardwareMap);

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
                    telemetry.addData("distance should be about 55", "%f", _distance.getDistance(DistanceUnit.CM));
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

        while (opModeIsActive() && (currentState != GodRedRR.AutonomousStates.DRIVEN_UNDER_BRIDGE)) {
            switch (currentState) {
                case STARTED:
                    currentState = MoveToFirstBlock();
                    break;
                case PICKED_UP_FIRST_BLOCK:
                    currentState = DropOffFirstBlock();
                    break;
                case DROPPED_OFF_FIRST_BLOCK:
                    currentState = MoveToSecondBlock();
                    break;
                case PICKED_UP_SECOND_BLOCK:
                    currentState = DropOffSecondBlock();
                    break;
            }
        }
    }
}