package com.edinaftc.opmodes.teleop;

import com.edinaftc.library.Stickygamepad;
import com.edinaftc.skystone.Robot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Teleop", group = "teleop")
public class TeleOp extends OpMode {
    private Robot robot;
    private Stickygamepad _gamepad1;

    public void init() {
        _gamepad1 = new Stickygamepad(gamepad1);
        robot = new Robot(this, telemetry);
        robot.start();
    }

    @Override
    public void start() {
        robot.hook.TurnOnUpdate();
    }

    public void loop() {

        _gamepad1.update();

        robot.drive.setVelocity(gamepad1.left_stick_x, gamepad1.left_stick_y,
                gamepad1.right_stick_x);

        if (_gamepad1.left_bumper) {
            robot.intake.toggleIntake();
        } else if (_gamepad1.right_bumper) {
            robot.intake.toggleExpel();
        }

        robot.liftandarm.setLiftPower(-gamepad2.left_stick_y);
        robot.liftandarm.setArmPower(gamepad2.right_stick_y);

        if (gamepad1.left_trigger > 0) {
            robot.hook.LiftHooks();
        }

        if (gamepad1.right_trigger > 0) {
            robot.hook.DropHooks();
        }

        if(gamepad2.a) {
            robot.grabber.openBothGrabbers();
        }

        if(gamepad2.y) {
            robot.grabber.closeBothGrabbers();
        }

        if(gamepad2.x) {
            robot.grabber.toggleBackGrabber();
        }

        if(gamepad2.b) {
            robot.grabber.toggleFrontGrabber();
        }

        if(gamepad2.right_bumper) {
            robot.grabber.rotateRight();
        }

        if (gamepad2.left_bumper) {
            robot.grabber.rotateLeft();
        }

        robot.drive.displayTelemetry(telemetry);
        robot.liftandarm.displayTelemetry(telemetry);

        telemetry.update();
    }

    @Override
    public  void stop() {
        robot.stop();
    }
}
