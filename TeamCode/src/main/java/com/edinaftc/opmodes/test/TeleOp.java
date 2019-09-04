package com.edinaftc.opmodes.test;

import com.edinaftc.library.Stickygamepad;
import com.edinaftc.library.Vector2d;
import com.edinaftc.skystone.Robot;
import com.edinaftc.library.subsystems.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Subsystem Mecanum Test", group = "teleop")
public class TeleOp extends OpMode {
    private Stickygamepad stickygamepad1, stickygamepad2;

    public void init() {
        robot = new Robot(this);
        robot.start();

        stickygamepad1 = new Stickygamepad(gamepad1);
        stickygamepad2 = new Stickygamepad(gamepad2);

    }

    private boolean slowMode, superSlowMode;


    private Robot robot;

    public void start() {
    }

    public void loop() {
        stickygamepad1.update();
        stickygamepad2.update();

        if (stickygamepad1.b) {
            slowMode = !slowMode;
            superSlowMode = false;
            if (slowMode) {
                robot.drive.setVelocityPIDCoefficients(MecanumDrive.SLOW_VELOCITY_PID);
            } else {
                robot.drive.setVelocityPIDCoefficients(MecanumDrive.NORMAL_VELOCITY_PID);
            }
        } else if (stickygamepad1.left_bumper) {
            superSlowMode = !superSlowMode;
            slowMode = false;
            if (superSlowMode) {
                robot.drive.setVelocityPIDCoefficients(MecanumDrive.SLOW_VELOCITY_PID);
            } else {
                robot.drive.setVelocityPIDCoefficients(MecanumDrive.NORMAL_VELOCITY_PID);
            }
        }

        double x, y = 0, omega;

        x = -gamepad1.left_stick_y;

        if (Math.abs(gamepad1.left_stick_x) > 0.5) {
            y = -gamepad1.left_stick_x;
        }

        omega = -gamepad1.right_stick_x;

        robot.drive.setVelocity(new Vector2d(x, y), omega);
    }
}
