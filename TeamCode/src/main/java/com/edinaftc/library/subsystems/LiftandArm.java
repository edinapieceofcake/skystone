package com.edinaftc.library.subsystems;

import com.edinaftc.opmodes.teleop.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftandArm extends Subsystem{

    private DcMotor lift;
    private CRServo arm;
    private double liftPower, armPower;
    private boolean zeroPowerChanged = false;
    private boolean zeroPowerOn = true;
    private boolean lockLiftOn = false;

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm = map.crservo.get("arm");
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
            this.liftPower = liftPower;
        } else if (liftPower < 0) {
            this.liftPower = Range.clip(liftPower, -.5, 0);
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
        if(armPower == 0) {
            this.armPower = .5;
        } else if(armPower > 0) {
            this.armPower = armPower * .25;
        } else if(armPower < 0) {
            this.armPower = -armPower * .25 + .5;
        }
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
