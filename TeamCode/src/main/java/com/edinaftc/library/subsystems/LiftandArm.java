package com.edinaftc.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LiftandArm extends Subsystem{

    private DcMotor arm, lift;
    private double liftPower, armPower;

    public LiftandArm(HardwareMap map) {
        lift = map.dcMotor.get("lift");
        arm = map.dcMotor.get("arm");
    }

    @Override
    public void update() {
        lift.setPower(liftPower);
        arm.setPower(armPower);
    }


    public void setLiftPower(double liftPower) {
        this.liftPower = liftPower;
    }

    public void setArmPower(double armPower) {
        this.armPower = armPower;
    }
}
