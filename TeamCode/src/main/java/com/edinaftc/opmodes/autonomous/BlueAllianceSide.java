package com.edinaftc.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.DRIVED_TO_BLOCK;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.DRIVED_TO_BRIDGE;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.DRIVE_TO_BLOCK;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.PICKED_UP_BLOCK;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.PICK_UP_BLOCK;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.START;
import static com.edinaftc.opmodes.autonomous.BlueAllianceSide.AutonomousStates.STARTED;

public class BlueAllianceSide extends LinearOpMode {

    public enum AutonomousStates{
        STARTED,
        DRIVED_TO_BLOCK,
        PICKED_UP_BLOCK,
        DRIVED_TO_BRIDGE
    }

    public BlueAllianceSide DriveToBlock() {

    }

    public BlueAllianceSide PickUpBlock() {
        sleep(5000)
    }

    public BlueAllianceSide DriveToBridge() {

    }

    public void runOpMode() {
        waitForStart();

        while (opModeIsActive() && (currentState != DRIVED_TO_BRIDGE)) {
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


            }
        }

    }

}
