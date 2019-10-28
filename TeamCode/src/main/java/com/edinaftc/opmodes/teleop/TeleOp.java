package com.edinaftc.opmodes.teleop;

import com.edinaftc.library.Stickygamepad;
import com.edinaftc.library.Vector2d;
import com.edinaftc.skystone.Robot;
import com.edinaftc.library.subsystems.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Teleop", group = "teleop")
public class TeleOp extends OpMode {
    private Robot robot;

    public void init() {
        robot = new Robot(this, telemetry);
        robot.start();
    }

    public void start() {
    }

    public void loop() {

        robot.drive.setVelocity(gamepad1.left_stick_x, gamepad1.left_stick_y,
                -gamepad1.right_stick_x);

        if (gamepad1.left_trigger != 0) {
            robot.intake.setIntakePower(gamepad1.left_trigger);
        } else if (gamepad1.right_trigger != 0) {
            robot.intake.setIntakePower(-gamepad1.right_trigger);
        } else {
            robot.intake.setIntakePower(0);

        }
/*
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
