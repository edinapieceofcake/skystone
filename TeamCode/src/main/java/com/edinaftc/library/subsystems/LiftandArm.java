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
    private boolean autoArmLocation = false;
    private int liftLocation;
    private int armLocation;
    private DcMotor lift;
    private DcMotor dummyarm;
    private CRServo arm;
    private double liftPower, armPower;
    private boolean toggleArmPower = false;
    private double armPowerMulti = 1;
// 16324

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        dummyarm = map.dcMotor.get("arm");
        dummyarm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        arm = map.crservo.get("arm");
    }

    @Override
    public void update() {
        if (autoLiftLocation) {
            int pos = lift.getCurrentPosition();
            if ((liftLocation - pos) > 0) {
                if (Math.abs(liftLocation - pos) < 10) {
                    // close enough so stop
                    lift.setPower(0);
                    autoLiftLocation = false;
                } else if (Math.abs(liftLocation - pos) < 50) {
                    lift.setPower(.25);
                } else {
                    lift.setPower(1);
                }
            } else if ((liftLocation - pos) < 0) {
                if (Math.abs(liftLocation - pos) < 10) {
                    // close enough so stop
                    lift.setPower(0);
                    autoLiftLocation = false;
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

        if (autoArmLocation) {
            int pos = dummyarm.getCurrentPosition();
            if ((armLocation - pos) > 0) {
                if (Math.abs(armLocation - pos) < 10) {
                    // close enough so stop
                    arm.setPower(0);
                    autoArmLocation = false;
                } else if (Math.abs(armLocation - pos) < 50) {
                    arm.setPower(.25);
                } else {
                    arm.setPower(.5);
                }
            } else if ((armLocation - pos) < 0) {
                if (Math.abs(armLocation - pos) < 10) {
                    // close enough so stop
                    arm.setPower(0);
                    autoArmLocation = false;
                } else if (Math.abs(armLocation - pos) < 50) {
                    arm.setPower(-.25);
                } else {
                    // move down
                    arm.setPower(-.5);
                }
            }
        } else {
            arm.setPower(armPower);
        }
    }

    public void setLiftPower(double liftPower) {
        this.liftPower = -liftPower;
        if (liftPower != 0) {
            autoLiftLocation = false;
            autoArmLocation = false;
        }
    }

    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addData("lift position, power", "%d %f", lift.getCurrentPosition(), lift.getPower());
        telemetry.addData("arm power, location", "%f %d", arm.getPower(), dummyarm.getCurrentPosition());
        telemetry.addData("lift brake on", lift.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData("auto on, location", "%s %d", autoLiftLocation, liftLocation);
    }

    public void setArmPower(double armPower) {
        this.armPower = -.8 * armPower * armPowerMulti;
        if (armPower != 0) {
            autoLiftLocation = false;
            autoArmLocation = false;
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
    }

    public void toggleArmPower() {
        if (armPowerMulti == 1) {
            armPowerMulti = .25;
        } else {
            armPowerMulti = 1;
        }
    }

    public void sendArmOut() {
        armLocation = 16324;
        autoArmLocation = true;
    }

    public void sendArmIn() {
        armLocation = 0;
        autoArmLocation = true;
    }
}
