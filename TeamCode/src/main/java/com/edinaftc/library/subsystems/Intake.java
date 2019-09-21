Intake.javapackage com.edinaftc.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends Subsystem{

    private DcMotor leftIntake, rightIntake;
    private double intakePower;

    public Intake(HardwareMap map) {
        leftIntake = map.dcMotor.get("leftIntake");
        rightIntake = map.dcMotor.get("rightIntake");
        rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void update() {
        leftIntake.setPower(intakePower);
        rightIntake.setPower(intakePower);
    }


    public void setIntakePower(double intakePower) {
        this.intakePower = intakePower;
    }
}
