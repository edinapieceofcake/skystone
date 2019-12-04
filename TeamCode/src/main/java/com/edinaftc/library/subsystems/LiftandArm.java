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

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm = map.crservo.get("arm");
    }

    @Override
    public void update() {
        lift.setPower(liftPower);
        arm.setPower(armPower);
    }

    public void setLiftPower(double liftPower) {
        this.liftPower = liftPower;
    }

    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addData("lift position, power", "%d %f", lift.getCurrentPosition(), lift.getPower());
        telemetry.addData("arm power", "%f", arm.getPower());
        telemetry.addData("lift brake on", lift.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setArmPower(double armPower) {
        this.armPower = - armPower * .8;
    }
}
