package com.edinaftc.library.subsystems;

import com.edinaftc.opmodes.teleop.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftandArm extends Subsystem{
    private double liftIndex = 1;
    private boolean autoLiftLocation = false;
    private int liftLocation;
    private DcMotor lift;
    private CRServo arm;
    private double liftPower, armPower;
    private boolean toggleArmPower = false;
    private double armPowerMulti = 1;
    private boolean autoRunToPositionSet = false;
// 16324

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm = map.crservo.get("crarm");
    }

    @Override
    public void update() {
        if (autoLiftLocation) {
            if (!autoRunToPositionSet) {
                autoRunToPositionSet = true;
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift.setTargetPosition(liftLocation);
                lift.setPower(1);
            }
        } else {
            if (lift.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (liftPower < 0) {
                if ((lift.getCurrentPosition() > -10) && (lift.getCurrentPosition() < 1000)) {
                    lift.setPower(liftPower * .5);
                } else if (lift.getCurrentPosition() >= 1000) {
                    lift.setPower(liftPower);
                } else {
                    lift.setPower(0);
                }
            } else {
                lift.setPower(liftPower);
            }
        }

        arm.setPower(armPower);
    }

    public void setLiftPower(double liftPower) {
        this.liftPower = -liftPower;
        if (liftPower != 0) {
            autoLiftLocation = false;
        }
    }

    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addData("lift position, power", "%d %f", lift.getCurrentPosition(), lift.getPower());
        telemetry.addData("arm power", "%f", arm.getPower());
        telemetry.addData("lift brake on", lift.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData("auto on, location", "%s %d", autoLiftLocation, liftLocation);
    }

    public void setArmPower(double armPower) {
        this.armPower = -.8 * armPower * armPowerMulti;
        if (armPower != 0) {
            autoLiftLocation = false;
        }
    }

    public void increaseHHeight() {
        liftIndex++;
        computeLocation();
    }

    public void decreaseHeight() {
        if (liftIndex > 0) {
            liftIndex--;
        }

        computeLocation();
    }

    private void computeLocation() {
        if (liftIndex == 0) {
            liftLocation = 0;
        } else {
            liftLocation = (int) ((1.1 * liftIndex - .6) * 1000);
        }

        autoLiftLocation = true;
        autoRunToPositionSet = false;
    }

    public void toggleArmPower() {
        if (armPowerMulti == 1) {
            armPowerMulti = .25;
        } else {
            armPowerMulti = 1;
        }
    }
}
