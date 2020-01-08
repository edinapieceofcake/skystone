package com.edinaftc.opmodes.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.heading.LinearInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.edinaftc.library.Stickygamepad;
import com.edinaftc.library.motion.roadrunner.mecanum.DriveConstants_435_35;
import com.edinaftc.library.motion.roadrunner.mecanum.MecanumDriveBase_435_35;
import com.edinaftc.library.motion.roadrunner.mecanum.MecanumDriveREVOptimized_435_35;
import com.edinaftc.library.vision.VuforiaCamera;
import com.edinaftc.skystone.vision.SkyStoneDetector;
import com.edinaftc.skystone.vision.SkystoneLocation;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="RedPullPlateAndPark", group="Autonomous")
@Config
public class RedPullPlateAndPark extends LinearOpMode {
    private Servo arm;
    private Servo flap;
    private Servo left;
    private Servo right;
    private Stickygamepad _gamepad1;
    private MecanumDriveBase_435_35 drive;
    private DistanceSensor distance;

    public void runOpMode() {
        long sleepTime = 0;

        distance = hardwareMap.get(DistanceSensor.class, "reardetector");

        _gamepad1 = new Stickygamepad(gamepad1);
        arm = hardwareMap.servo.get("leftArm");
        flap = hardwareMap.servo.get("leftFlap");
        left = hardwareMap.servo.get("blhook");
        right = hardwareMap.servo.get("brhook");

        flap.setPosition(1);

        hardwareMap.servo.get("rightArm").setPosition(0);
        hardwareMap.servo.get("rightFlap").setPosition(0);

        drive = new MecanumDriveREVOptimized_435_35(hardwareMap);

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

                    telemetry.addData("tickPerRev, Gearing, MaxRPM", "%f %f %f", DriveConstants_435_35.MOTOR_CONFIG.getTicksPerRev(), DriveConstants_435_35.MOTOR_CONFIG.getGearing(), DriveConstants_435_35.MOTOR_CONFIG.getMaxRPM());
                    telemetry.addData("use left/right bumper to adjust sleep time", "");
                    telemetry.addData("sleep time (ms)", sleepTime);
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

        drive.setPoseEstimate(new Pose2d(40.0, -63.0, Math.toRadians(0.0)));

        Trajectory backupAndGrabPlate = drive.trajectoryBuilder()
                .reverse()
                .splineTo(new Pose2d(44.0, -30.0, Math.toRadians(0.0)))
                .build();

        drive.followTrajectorySync(backupAndGrabPlate);

        left.setPosition(.3);
        right.setPosition(.6);
        sleep(600);

        Trajectory pullAndTurn = drive.trajectoryBuilder()
                .lineTo(new Vector2d(42.0, -53.0)) // drag forward and turn
//                .lineTo(new Vector2d(38.0, -53.0), new LinearInterpolator(Math.toRadians(-90), Math.toRadians(-90)))
                .build();

        drive.followTrajectorySync(pullAndTurn);

        drive.turnWithTimeoutSync(Math.toRadians(-90), 3);

        left.setPosition(.7);
        right.setPosition(.17);
        sleep(500);

        Trajectory driveToBridge = drive.trajectoryBuilder()
//                .splineTo(new Pose2d(8, -34))
                .lineTo(new Vector2d(8.0, -63)) // drive to bridge
                .build();
        drive.followTrajectorySync(driveToBridge);
    }
}