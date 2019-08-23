package com.edinaftc.subsystems;


import com.edinaftc.library.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.Hardware;

import java.util.Arrays;
import java.util.Collections;

public class MecanumDrive extends Subsystem{

    private DcMotorEx[] motors;
    public static final String[] MOTOR_NAMES = {"frontLeft", "rearLeft", "rearRight", "frontRight"};
    private double[] powers;
    private Vector2d targetVel = new Vector2d(0, 0);
    private double targetOmega = 0;

    public static final PIDCoefficients NORMAL_VELOCITY_PID = new PIDCoefficients(20, 8, 12);
    public static final PIDCoefficients SLOW_VELOCITY_PID = new PIDCoefficients(10, 3, 1);

    public MecanumDrive(HardwareMap map) {
        motors = new DcMotorEx[4];
        for (int i = 0; i < 4; i ++) {
            DcMotorEx dcMotorEx = map.get(DcMotorEx.class, MOTOR_NAMES[i]);
            motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        motors[2].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[3].setDirection(DcMotorSimple.Direction.REVERSE);
        setVelocityPIDCoefficients(NORMAL_VELOCITY_PID);

    }

    private void updatePowers() {
        powers[0] = targetVel.x() - targetVel.y() - targetOmega;
        powers[1] = targetVel.x() + targetVel.y() - targetOmega;
        powers[2] = targetVel.x() - targetVel.y() + targetOmega;
        powers[3] = targetVel.x() + targetVel.y() + targetOmega;

        double max = Collections.max(Arrays.asList(1.0, Math.abs(powers[0]),
                Math.abs(powers[1]), Math.abs(powers[2]), Math.abs(powers[3])));

        for (int i = 0; i < 4; i++) {
            powers[i] /= max;
        }
    }

    public void update() {

        updatePowers();

    }

    public void setVelocityPIDCoefficients(PIDCoefficients pidCoefficients) {
        for (int i = 0; i < 4; i++) {
            motors[i].setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidCoefficients);
        }
    }
}




