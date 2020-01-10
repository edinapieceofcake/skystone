package com.edinaftc.opmodes.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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

@Autonomous(name="RedPark", group="Autonomous")
@Config
public class RedPark extends LinearOpMode {
    private Stickygamepad _gamepad1;
    private MecanumDriveBase_435_35 drive;

    public void runOpMode() {
        long sleepTime = 0;

        _gamepad1 = new Stickygamepad(gamepad1);

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
                    telemetry.update();
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        sleep(sleepTime);

        Trajectory driveFOrward = drive.trajectoryBuilder()
                .forward(10.0)
                .build();


        drive.followTrajectorySync(driveFOrward);
    }
}