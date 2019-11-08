package com.edinaftc.library.subsystems;

import com.edinaftc.opmodes.teleop.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftandArm extends Subsystem{

    private DcMotor arm, lift;
    private double liftPower, armPower;
    private long basePosition = 100;
    private long blockHeight = 400;
    private long currentIndex = 0;
    private long threshold = 15;
    private boolean autoLift = false;

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm = map.dcMotor.get("arm");
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void update() {
        if (autoLift) {
            long computedPosition = basePosition + (blockHeight * currentIndex);
            long lowerThreshold = lift.getCurrentPosition() - threshold;
            long upperThreshold = lift.getCurrentPosition() + threshold;

            if (lowerThreshold < computedPosition && computedPosition < upperThreshold) {
                lift.setPower(.15);
            } else if (computedPosition < lift.getCurrentPosition()) {
                lift.setPower(0);
            } else if (computedPosition > lift.getCurrentPosition()) {
                lift.setPower(.5);
            }
        } else {
            lift.setPower(liftPower);
            arm.setPower(armPower);
        }
    }

    public void setLiftPower(double liftPower) {
        autoLift = false;
        this.liftPower = liftPower;
    }

    public void moveLiftUpOneSpot() {
        currentIndex++;
        autoLift = true;
    }

    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addData("position, power", "%d %f", lift.getCurrentPosition(), lift.getPower());
    }

    public void setArmPower(double armPower) {
        this.armPower = armPower;
    }
}
