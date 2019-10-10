package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.motion.Mecanum;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.DRIVED_TO_BLOCK;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.DRIVED_TO_BRIDGE;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.DRIVEN_UNDER_BRIDGE;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.PICKED_UP_BLOCK;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.STARTED;

@Autonomous(name="Blue Alliance Block Side", group="Autonomous")
public class BlueAllianceSide extends LinearOpMode {

    private Mecanum _mecanum;

    public enum AutonomousStates{
        STARTED,
        DRIVED_TO_BLOCK,
        PICKED_UP_BLOCK,
        DRIVED_TO_BRIDGE,
        DRIVEN_UNDER_BRIDGE
    }

    public AutonomousStates DriveToBlock() {
        _mecanum.SlideRightRunWithEncoders(1, 3600, this);
        return DRIVED_TO_BLOCK;
    }

    public AutonomousStates PickUpBlock() {
        sleep(5000);
        return PICKED_UP_BLOCK;
    }

    public AutonomousStates DriveToBridge() {
        _mecanum.SlideLeftRunWithEncoders(1, 1800, this);
        return DRIVED_TO_BRIDGE;
    }

    public AutonomousStates DriveUnderBridge() {
        _mecanum.MoveForwardRunToPosition(1, 10800, this);
        return AutonomousStates.DRIVEN_UNDER_BRIDGE;
    }

    public void runOpMode() {
        AutonomousStates currentState = STARTED;

        _mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"),
                hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br"), telemetry);

        waitForStart();

        while (opModeIsActive() && (currentState != DRIVEN_UNDER_BRIDGE)) {
            switch (currentState) {
                case STARTED:
                    currentState = DriveToBlock();
                    break;
                case DRIVED_TO_BLOCK:
                    currentState = PickUpBlock();
                    break;
                case PICKED_UP_BLOCK:
                    currentState = DriveToBridge();
                    break;
                case DRIVED_TO_BRIDGE:
                    currentState = DriveUnderBridge();
                    break;

            }
        }

    }

}
