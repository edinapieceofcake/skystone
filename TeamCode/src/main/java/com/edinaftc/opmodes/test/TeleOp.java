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

        double x, y = 0, omega;

        x = -gamepad1.left_stick_y;

        if (Math.abs(gamepad1.left_stick_x) > 0.5) {
            y = -gamepad1.left_stick_x;
        }

        omega = -gamepad1.right_stick_x;

        robot.drive.setVelocity(new Vector2d(x, y), omega);

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

        if (gamepad2.right_stick_x > 0) {
            robot.liftandarm.setArmPower(1);
        } else if(gamepad2.right_stick_x < 0) {
            robot.liftandarm.setArmPower(-1);
        } else {
            robot.liftandarm.setArmPower(0);
        }

    }
}
