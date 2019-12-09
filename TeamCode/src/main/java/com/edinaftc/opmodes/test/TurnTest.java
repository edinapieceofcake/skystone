package com.edinaftc.opmodes.test;

import com.edinaftc.library.Stickygamepad;
import com.edinaftc.library.motion.Mecanum;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp
public class TurnTest extends LinearOpMode {
    BNO055IMU gyro;
    public IMU imu;
    private Mecanum driveTrain;
    private Stickygamepad _gamepad1;
    private double _speedModifier = -25;

    @Override
    public void runOpMode() {
        double angles[];

        gyro = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new IMU(gyro);
        imu.initialize();
//         driveTrain = new DriveTrain(lf, rf, lr, rr);
        driveTrain = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br"), telemetry);
        driveTrain.StopResetEncodersRunWithEncoderAndBrakekOn();
        _gamepad1 = new Stickygamepad(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            _gamepad1.update();
            if (_gamepad1.left_bumper) {
                _speedModifier--;
            } else if (_gamepad1.right_bumper) {
                _speedModifier++;
            }

            if (_gamepad1.x) {
                turn_to_heading(270, _speedModifier);
            } else if (_gamepad1.y) {
                turn_to_heading(0, _speedModifier);
            } else if (_gamepad1.b) {
                turn_to_heading(90, _speedModifier);
            } else if (_gamepad1.a) {
                turn_to_heading(180, _speedModifier);
            } else if (_gamepad1.dpad_left) {
                driveTrain.TurnLeftRunToPosition(1, 1415, this);
            }

            angles = imu.printAngles();

            telemetry.addData("first, second, third angles", "%f, %f, %f,", angles[0], angles[1], angles[2]);
            telemetry.addData("speed modifier", "%f", _speedModifier);
            telemetry.update();
        }
    }

    public void turn_to_heading(double target_heading, double speedModifier) {
        boolean goRight;
        double currentHeading;
        double degreesToTurn;
        double wheelPower;
        double prevHeading = 0;
        ElapsedTime timeoutTimer = new ElapsedTime();

        double wheel_encoder_ticks = 2400;
        double wheel_diameter = 2.3622;  // size of wheels
        double ticks_per_inch = wheel_encoder_ticks / (wheel_diameter * Math.PI);

        currentHeading = imu.readCurrentHeading();
        degreesToTurn = Math.abs(target_heading - currentHeading);

        goRight = target_heading > currentHeading;

        if (degreesToTurn > 180) {
            goRight = !goRight;
            degreesToTurn = 360 - degreesToTurn;
        }

        timeoutTimer.reset();
        prevHeading = currentHeading;
        while (degreesToTurn > .5 && timeoutTimer.seconds() < 2) {  // 11/21 changed from .5 to .3

            if (speedModifier < 0) {
                wheelPower = (Math.pow((degreesToTurn + 25) / -speedModifier, 3) + 15) / 100;
            } else {
                if (speedModifier != 0) {
                    wheelPower = (Math.pow((degreesToTurn) / speedModifier, 4) + 35) / 100;
                } else {
                    wheelPower = (Math.pow((degreesToTurn) / 30, 4) + 15) / 100;
                }
            }

            if (goRight) {
                wheelPower = -wheelPower;
            }

            telemetry.addData("degrees, timeout, power", "%f %f %f", degreesToTurn, timeoutTimer.milliseconds(), wheelPower);
            telemetry.update();

            driveTrain.Move(-wheelPower, wheelPower, -wheelPower, wheelPower);

            currentHeading = imu.readCurrentHeading();

            degreesToTurn = Math.abs(target_heading - currentHeading);       // Calculate how far is remaining to turn

            goRight = target_heading > currentHeading;

            if (degreesToTurn > 180) {
                goRight = !goRight;
                degreesToTurn = 360 - degreesToTurn;
            }

            if (Math.abs(currentHeading - prevHeading) > 1) {  // if it has turned at least one degree
                timeoutTimer.reset();
                prevHeading = currentHeading;
            }

        }

        driveTrain.Move(0, 0, 0, 0);

        telemetry.addData("Heading: ", currentHeading);
        telemetry.update();

    } // end of turn_to_heading

    public class IMU {

        BNO055IMU imu;
        Orientation angles;

        public IMU(BNO055IMU imu) {
            this.imu = imu;
        }

        public void initialize(){
            BNO055IMU.Parameters IMUParameters = new BNO055IMU.Parameters();
            IMUParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            IMUParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            IMUParameters.calibrationDataFile = "BNO055IMUCalibration.json";

            imu.initialize(IMUParameters);
        }

        public double readCurrentHeading() {
            double currentHeading;
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentHeading = angles.firstAngle;
            if (currentHeading < 0) {
                currentHeading = -currentHeading;
            } else {
                currentHeading = 360 - currentHeading;
            }
            return currentHeading;
        }

        public double[] printAngles(){
            double[] values;

            values = new double[3];
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            values[0] = angles.firstAngle;
            values[1] = angles.secondAngle;
            values[2] = angles.thirdAngle;

            return values;
        }

        public double headingAdjustment(double targetHeading){
            double adjustment;
            double currentHeading;
            double degreesOff;
            boolean goRight;

            currentHeading = readCurrentHeading();

            goRight = targetHeading > currentHeading;
            degreesOff = Math.abs(targetHeading - currentHeading);

            if (degreesOff > 180) {
                goRight = !goRight;
                degreesOff = 360 - degreesOff;
            }

            if (degreesOff < .3) {
                adjustment = 0;
            } else {
                adjustment = (Math.pow((degreesOff + 2) / 5, 2) + 2) / 100;
            }

            if (goRight) {
                adjustment = -adjustment;
            }
            return adjustment;
        }
    }

}

