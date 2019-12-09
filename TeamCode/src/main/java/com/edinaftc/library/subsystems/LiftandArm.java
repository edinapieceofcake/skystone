package com.edinaftc.library.subsystems;

import com.edinaftc.opmodes.teleop.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftandArm extends Subsystem{
    private double liftIndex = 1;
    private boolean autoLocation = false;
    private int liftLocation;
    private DcMotor lift;
    private CRServo arm;
    private double liftPower, armPower;

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm = map.crservo.get("arm");
    }

    @Override
    public void update() {
        if (autoLocation) {
            int pos = lift.getCurrentPosition();
            if ((liftLocation - pos) > 0) {
                if (Math.abs(liftLocation - pos) < 10) {
                    // close enough so stop
                    lift.setPower(0);
                    autoLocation = false;
                } else if (Math.abs(liftLocation - pos) < 50) {
                    lift.setPower(.25);
                } else {
                    lift.setPower(1);
                }
            } else if ((liftLocation - pos) < 0) {
                if (Math.abs(liftLocation - pos) < 10) {
                    // close enough so stop
                    lift.setPower(0);
                    autoLocation = false;
                } else if (Math.abs(liftLocation - pos) < 50) {
                    lift.setPower(-.25);
                } else {
                    // move down
                    lift.setPower(-.5);
                }
            }
        } else {
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
            autoLocation = false;
        }
    }

    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addData("lift position, power", "%d %f", lift.getCurrentPosition(), lift.getPower());
        telemetry.addData("arm power", "%f", arm.getPower());
        telemetry.addData("lift brake on", lift.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData("auto on, location", "%s %d", autoLocation, liftLocation);
    }

    public void setArmPower(double armPower) {
        this.armPower = - armPower * .8;
        if (armPower != 0) {
            autoLocation = false;
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

        autoLocation = true;
    }
}
