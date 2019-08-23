package com.edinaftc.subsystems;


import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class MecanumDrive extends Subsystem{

    private double[] powers;
    private Vector2d targetVel = new Vector2d(0, 0);
    private double targetOmega = 0;

    public static int TRACKING_ENCODER_TICKS_PER_REV = 2000;

    public static final PIDCoefficients NORMAL_VELOCITY_PID = new PIDCoefficients(20, 8, 12);
    public static final PIDCoefficients SLOW_VELOCITY_PID = new PIDCoefficients(10, 3, 1);

    public static PIDFCoefficients HEADING_PIDF = new PIDFCoefficients(-0.5, 0, 0, 0.230, 0);
    public static PIDFCoefficients AXIAL_PIDF = new PIDFCoefficients(-0.05, 0, 0, 0.0177, 0);
    public static PIDFCoefficients LATERAL_PIDF = new PIDFCoefficients(-0.05, 0, 0, 0.0179, 0);

    // units in cm
    public static PIDCoefficients COLUMN_ALIGN_PID = new PIDCoefficients(-0.06, 0, -0.01);
    public static double COLUMN_ALIGN_SETPOINT = 7;
    public static double COLUMN_ALIGN_ALLOWED_ERROR = 0.5;

    public static double PROXIMITY_SMOOTHING_COEFF = 0.5;
    public static double PROXIMITY_SWIVEL_EXTEND = 0;
    public static double PROXIMITY_SWIVEL_RETRACT = 0.64;

    public static double ULTRASONIC_SWIVEL_EXTEND = 0.2;
    public static double ULTRASONIC_SWIVEL_RETRACT = 0.75;

    public static PIDCoefficients MAINTAIN_HEADING_PID = new PIDCoefficients(-2, 0, -0.01);

    private void resetTrackingEncoders() {
        int[] positions = internalGetTrackingEncoderPositions();
        for(int i = 0; i < 2; i++) {
            trackingEncoderOffsets[i] = -positions[i];
        }
    }

    public double[] getTrackingEncoderRotations() {
        double[] motorRotations = new double[2];
        int[] encoderPositions = getTrackingEncoderPositions();
        for (int i = 0; i < 2; i++) {
            motorRotations[i] = trackingEncoderTicksToRadians(encoderPositions[i]);
        }
        return motorRotations;
    }

}

    private void updatePowers() {
        powers[0] = targetVel.x() - targetVel.y() - targetOmega;
        powers[1] = targetVel.x() + targetVel.y() - targetOmega;
        powers[2] = targetVel.x() - targetVel.y() + targetOmega;
        powers[3] = targetVel.x() + targetVel.y() + targetOmega;

        double max = Collections.max(Arrays.asList(1.0, Math.abs(powers[0]),
                Math.abs(powers[1]), Math.abs(powers[2]), Math.abs(powers[3])));

        for (int i = 0; i < 4; i++) {
            powers[i] /= max;
        }
    }

