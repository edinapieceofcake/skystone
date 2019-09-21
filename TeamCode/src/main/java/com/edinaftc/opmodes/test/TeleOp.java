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
        robot = new Robot(this, telemetry);
        robot.start();

        stickygamepad1 = new Stickygamepad(gamepad1);
        stickygamepad2 = new Stickygamepad(gamepad2);

    }

    private boolean slowMode, superSlowMode;


    private Robot robot;

    public void start() {
    }

    public void loop() {
        /*
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
*/
/*
        double x, y = 0, omega;

        x = -gamepad1.left_stick_y;

        if (Math.abs(gamepad1.left_stick_x) > 0.5) {
            y = -gamepad1.left_stick_x;
        }

        omega = -gamepad1.right_stick_x;

        robot.drive.setVelocity(new Vector2d(x, y), omega);
*/

        robot.drive.setVelocity(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetry.addData("motor 0", "%f", robot.drive.getMotors()[0].getPower());
        telemetry.addData("motor 1", "%f", robot.drive.getMotors()[1].getPower());
        telemetry.addData("motor 2", "%f", robot.drive.getMotors()[2].getPower());
        telemetry.addData("motor 3", "%f", robot.drive.getMotors()[3].getPower());
        telemetry.update();
/*
        if (gamepad1.left_trigger != 0) {
            robot.intake.setIntakePower(gamepad1.left_trigger);
        } else if (gamepad1.right_trigger != 0) {
            robot.intake.setIntakePower(gamepad1.right_trigger);
        } else {
            robot.intake.setIntakePower(0);
        }
 */
    }
}
