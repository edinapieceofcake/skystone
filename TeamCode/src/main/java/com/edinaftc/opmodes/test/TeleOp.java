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

        robot.drive.setVelocity(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetry.addData("left x", "%f", robot.drive.leftX());
        telemetry.addData("left y", "%f", robot.drive.leftY());
        telemetry.addData("right y", "%f", robot.drive.rightY());
        telemetry.addData("power 0", "%f", robot.drive.getPowers()[0]);
        telemetry.addData("power 1", "%f", robot.drive.getPowers()[1]);
        telemetry.addData("power 2", "%f", robot.drive.getPowers()[2]);
        telemetry.addData("power 3", "%f", robot.drive.getPowers()[3]);
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

        if (gamepad2.left_stick_y > 0) {
            robot.liftandarm.setLiftPower(1);
        } else if (gamepad2.left_stick_y < 0) {
            robot.liftandarm.setLiftPower(-1);
        } else {
            robot.liftandarm.setLiftPower(0);
        }
 */
    }
}
