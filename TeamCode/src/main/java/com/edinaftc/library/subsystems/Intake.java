package com.edinaftc.library.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends Subsystem {

    private DcMotor leftIntake, rightIntake;
    private CRServo leftIntakeServo, rightIntakeServo;
    private double intakePower;

    public Intake(HardwareMap map) {
        leftIntake = map.dcMotor.get("il");
        rightIntake = map.dcMotor.get("ir");
        leftIntakeServo = map.crservo.get("isl");
        rightIntakeServo = map.crservo.get("isr");
        rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void update() {
        leftIntake.setPower(intakePower);
        rightIntake.setPower(-intakePower);
        leftIntakeServo.setPower(-intakePower);
        rightIntakeServo.setPower(intakePower);
    }


    public void setIntakePower(double intakePower) {
        this.intakePower = intakePower;
    }
}
