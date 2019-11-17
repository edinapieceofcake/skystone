package com.edinaftc.library.subsystems;

import com.edinaftc.opmodes.teleop.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftandArm extends Subsystem{

    private DcMotor arm, lift;
    private double liftPower, armPower;

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm = map.dcMotor.get("arm");
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void update() {
        lift.setPower(liftPower);
        arm.setPower(armPower);
    }

    public void setLiftPower(double liftPower) {
        if (liftPower > 0) {
            this.liftPower = liftPower / 3;
        } else if (liftPower < 0) {
            this.liftPower = .02;
        } else {
            this.liftPower = 0;
        }
    }

    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addData("position, power", "%d %f", lift.getCurrentPosition(), lift.getPower());
    }

    public void setArmPower(double armPower) {
        this.armPower = armPower;
    }
}
