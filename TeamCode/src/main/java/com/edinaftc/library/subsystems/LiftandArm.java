package com.edinaftc.library.subsystems;

import com.edinaftc.opmodes.teleop.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftandArm extends Subsystem{

    private DcMotor arm, lift;
    private double liftPower, armPower;
    private boolean zeroPowerChanged = false;
    private boolean zeroPowerOn = true;
    private boolean lockLiftOn = false;

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm = map.dcMotor.get("arm");
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void update() {
        if (lockLiftOn) {
            lift.setPower(0.02);
        } else {
            lift.setPower(liftPower);
        }

        arm.setPower(armPower);

        if (zeroPowerChanged) {
            zeroPowerChanged = false;
            if (zeroPowerOn) {
                lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            } else {
                lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
        }
    }

    public void setLiftPower(double liftPower) {
        if (liftPower > 0) {
            this.liftPower = liftPower / 3;
        } else if (liftPower < 0) {
            this.liftPower = -.1;
        } else {
            this.liftPower = 0;
        }
    }

    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addData("lift position, power", "%d %f", lift.getCurrentPosition(), lift.getPower());
        telemetry.addData("lift brake on", lift.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData("lock lift on", lockLiftOn);
    }

    public void setArmPower(double armPower) {
        this.armPower = armPower;
    }

    public void setZeroPowerToBrake() {
        if (!zeroPowerChanged) {
            zeroPowerChanged = true;
            zeroPowerOn = true;
        }
    }

    public void setZeroPowerToFloat() {
        if (!zeroPowerChanged) {
            zeroPowerChanged = true;
            zeroPowerOn = false;
        }
    }

    public void lockLift(boolean lockSetting) {
        lockLiftOn = lockSetting;
    }
}
