package com.edinaftc.library.subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDrive2 extends Subsystem{

    private DcMotorEx[] motors;
    public static final String[] MOTOR_NAMES = {"fl", "bl", "br", "fr"};
    private double[] powers;
    private double leftStickX;
    private double leftStickY;
    private double rightStickY;

    private double currentPower = 1.4;

    public MecanumDrive2(HardwareMap map) {
        powers = new double[4];
        motors = new DcMotorEx[4];

        for (int i = 0; i < 4; i ++) {
            DcMotorEx dcMotor = map.get(DcMotorEx.class, MOTOR_NAMES[i]);
            motors[i] = dcMotor;
            motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        motors[2].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[3].setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setVelocity(double leftStickX, double leftStickY, double rightStickY) {
        this.leftStickX = leftStickX;
        this.leftStickY = leftStickY;
        this.rightStickY = rightStickY;
    }

    public void update() {
        final double x = Math.pow(-leftStickX, 3.0);
        final double y = Math.pow(leftStickY, 3.0);

        final double rotation = Math.pow(-rightStickY, 3.0);
        final double direction = Math.atan2(x, y);
        final double speed = Math.min(1.0, Math.sqrt(x * x + y * y)) * currentPower;

        powers[0] = speed * Math.sin(direction + Math.PI / 4.0) + rotation;
        powers[3] = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
        powers[1] = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
        powers[2] = speed * Math.sin(direction + Math.PI / 4.0) - rotation;

        for (int i = 0; i < 4; i++) {
            motors[i].setPower(powers[i]);
        }
    }

    public void displayTelemetry(Telemetry telemetry) {
        for (int i = 0; i < 4; i++) {
            telemetry.addData(String.format("%s: position %d power %f", MOTOR_NAMES[i], motors[i].getCurrentPosition(), motors[i].getPower()), "");
        }
    }
}
