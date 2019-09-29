package com.edinaftc.opmodes.autonomous;

import com.edinaftc.library.motion.Mecanum;
import com.edinaftc.skystone.Robot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Grab Platform", group = "teleop")
public class GrabPlatform extends OpMode {
    private Mecanum mecanum;

    public void init() {
        mecanum = new Mecanum(hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("bl"),
                hardwareMap.dcMotor.get("br"), true, telemetry);
    }

    public void start() {
    }

    public void loop() {
        // drive forward
        // turn servos
        // drive backwards

    }
}
