package com.edinaftc.opmodes.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.heading.ConstantInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.edinaftc.library.Stickygamepad;
import com.edinaftc.library.motion.roadrunner.mecanum.DriveConstants;
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
import kotlin.Unit;

@Autonomous(name="God Red Alliance", group="Autonomous")
@Config
public class GodRedRR extends LinearOpMode {
    private VuforiaCamera camera;
    private SkyStoneDetector skyStoneDetector;
    private Servo arm;
    private Servo flap;
    private Servo left;
    private Servo right;
    private SkystoneLocation location = SkystoneLocation.left;
    private Stickygamepad _gamepad1;
    private SampleMecanumDriveBase drive;
    private DistanceSensor distance;

    public void runOpMode() {
        long sleepTime = 0;
        double firstBlockLocation = 0;
        double secondBlockLocation = 0;

        skyStoneDetector = new SkyStoneDetector();
        camera = new VuforiaCamera();
        distance = hardwareMap.get(DistanceSensor.class, "testdetector");

        _gamepad1 = new Stickygamepad(gamepad1);
        arm = hardwareMap.servo.get("leftArm");
        flap = hardwareMap.servo.get("leftFlap");
        left = hardwareMap.servo.get("blhook");
        right = hardwareMap.servo.get("brhook");

        camera.addTracker(skyStoneDetector);
        skyStoneDetector.cx0 = 330;
        skyStoneDetector.cy0 = 420;
        skyStoneDetector.cx1 = 640;
        skyStoneDetector.cy1 = 420;
        skyStoneDetector.cx2 = 950;
        skyStoneDetector.cy2 = 420;

        camera.initialize();

        flap.setPosition(1);

        hardwareMap.servo.get("rightArm").setPosition(0);
        hardwareMap.servo.get("rightFlap").setPosition(0);

        drive = new SampleMecanumDriveREVOptimized(hardwareMap);

        while (!isStarted()) {
            synchronized (this) {
                try {
                    location = skyStoneDetector.getLocation();
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

                    telemetry.addData("tickPerRev, Gearing, MaxRPM", "%f %f %f", DriveConstants.MOTOR_CONFIG.getTicksPerRev(), DriveConstants.MOTOR_CONFIG.getGearing(), DriveConstants.MOTOR_CONFIG.getMaxRPM());
                    telemetry.addData("use left/right bumper to adjust sleep time", "");
                    telemetry.addData("sleep time (ms)", sleepTime);
                    telemetry.addData("location ", location);
                    telemetry.addData("distance should be about 55", "%f", distance.getDistance(DistanceUnit.CM));
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

        switch (location) {
            case left:
                firstBlockLocation = -22;
                secondBlockLocation = -46;
                break;
            case right:
                firstBlockLocation = -30;
                secondBlockLocation = -54;
                break;
            case middle:
                firstBlockLocation = -38;
                secondBlockLocation = -62;
                break;
        }

        flap.setPosition(0);
        Trajectory driveToFirstBlock = drive.trajectoryBuilder()
                .strafeTo(new Vector2d(firstBlockLocation, -32.0)).build(); // pick up first block

        drive.followTrajectorySync(driveToFirstBlock);
        arm.setPosition(0);
        sleep(250);
        flap.setPosition(1);
        sleep(350);
        arm.setPosition(1);
        sleep(100);

        Trajectory dropOffFirstBlock = drive.trajectoryBuilder()
                .lineTo(new Vector2d(0.0, -36.0))
                .splineTo(new Pose2d(55.0, -30.0)) // drop off first block
                .build();

        drive.followTrajectorySync(dropOffFirstBlock);
        flap.setPosition(0);
        arm.setPosition(0);
        sleep(400);

        flap.setPosition(1);
        arm.setPosition(1);

        Trajectory driveToSecondBlock = drive.trajectoryBuilder()
                .reverse() // drive backwards
                .splineTo(new Pose2d(0.0, -36.0))
                .lineTo(new Vector2d(secondBlockLocation, -32.0)) // pick up second block
                .build();

        drive.followTrajectorySync(driveToSecondBlock);
        flap.setPosition(0);
        arm.setPosition(0);
        sleep(450);
        flap.setPosition(1);
        sleep(350);
        arm.setPosition(1);
        sleep(100);

        Trajectory dropOffSecondBlock = drive.trajectoryBuilder()
                .splineTo(new Pose2d(0.0, -36.0))
                .splineTo(new Pose2d(50.0, -30.0)) // drop off second block
                .build();

        drive.followTrajectorySync(dropOffSecondBlock);
        flap.setPosition(0);
        arm.setPosition(0);
        sleep(400);

        flap.setPosition(1);
        arm.setPosition(1);

        Trajectory backupAndPrepForTurn = drive.trajectoryBuilder()
                .reverse() // drive backwards
                .strafeTo(new Vector2d(42, -36.0)) // turn
                .lineTo(new Vector2d(42, -36), new ConstantInterpolator(Math.toRadians(-90)))
                .build();

        drive.followTrajectorySync(backupAndPrepForTurn);

        drive.turnSync(Math.toRadians(-90));

        Trajectory backupAndGrabPlate = drive.trajectoryBuilder()
                .reverse() // drive backwards
                .lineTo(new Vector2d(42, -30.0)) // backup
                .build();

        drive.followTrajectorySync(backupAndGrabPlate);

        left.setPosition(.3);
        right.setPosition(.6);
        sleep(600);

        Trajectory pullAndTurn = drive.trajectoryBuilder()
                .lineTo(new Vector2d(42.0, -53.0)) // drag forward and turn
                .build();

        drive.followTrajectorySync(pullAndTurn);

        drive.turnWithTimeoutSync(Math.toRadians(-90), 3);

        left.setPosition(.7);
        right.setPosition(.17);
        sleep(500);

        Trajectory driveToBridge = drive.trajectoryBuilder()
                .lineTo(new Vector2d(0.0, -34)) // drive to bridge
                .build();
        drive.followTrajectorySync(driveToBridge);
    }
}