package com.edinaftc.library.subsystems;


import com.edinaftc.library.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.Collections;

public class MecanumDrive2 extends Subsystem{

    private DcMotorEx[] motors;
    public static final String[] MOTOR_NAMES = {"fl", "bl", "br", "fr"};
    private double[] powers;
    private double leftStickX;
    private double leftStickY;
    private double rightStickY;

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
        //setVelocityPIDCoefficients(NORMAL_VELOCITY_PID);
    }

    public DcMotorEx[] getMotors() { return motors; }

    public double[] getPowers() { return powers; }

    public void setVelocity(double leftStickX, double leftStickY, double rightStickY) {
        internalSetVelocity(leftStickX, leftStickY, rightStickY);
    }

    private void internalSetVelocity(double leftStickX, double leftStickY, double rightStickY) {
        this.leftStickX = leftStickX;
        this.leftStickY = leftStickY;
        this.rightStickY = rightStickY;
    }

    public double leftX() { return this.leftStickX; }

    public double leftY() { return this.leftStickY; }

    public double rightY() {
        return this.rightStickY;
    }

    private void updatePowers() {
        final double x = Math.pow(-leftStickX, 3.0);
        final double y = Math.pow(leftStickY, 3.0);

        final double rotation = Math.pow(-rightStickY, 3.0);
        final double direction = Math.atan2(x, y);
        final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));

        powers[0] = speed * Math.sin(direction + Math.PI / 4.0) + rotation;
        powers[3] = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
        powers[1] = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
        powers[2] = speed * Math.sin(direction + Math.PI / 4.0) - rotation;
    }

    public void update() {
        updatePowers();
        for (int i = 0; i < 4; i++) {
            motors[i].setPower(powers[i]);
        }
    }

    public void displayTelemetry(Telemetry telemetry) {
        for (int i = 0; i < 4; i++) {
            telemetry.addData(String.format("%s: position %d power %f", MOTOR_NAMES[i], motors[i].getCurrentPosition(), motors[i].getPower()), "");
        }
    }

    public void setVelocityPIDCoefficients(PIDCoefficients pidCoefficients) {
        for (int i = 0; i < 4; i++) {
            motors[i].setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidCoefficients);
        }
    }
}




