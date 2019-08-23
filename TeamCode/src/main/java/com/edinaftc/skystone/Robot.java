package com.edinaftc.skystone;

import android.util.Log;

import com.edinaftc.subsystems.MecanumDrive;
import com.edinaftc.subsystems.Subsystem;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Robot {
    private ExecutorService subsystemUpdateExecutor;
    private boolean started;

    public MecanumDrive drive;

    private List<Subsystem> subsystems;

    private Runnable subsystemUpdateRunnable = () -> {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (Subsystem subsystem : subsystems) {
                    if (subsystem == null) continue;
                    try {
                        subsystem.update();
                    } catch (Throwable t) {
                    }
                }
            } catch (Throwable t) {

            }
        }
    };

    public Robot(OpMode opMode) {

        subsystems = new ArrayList<>();

        try {
            drive = new MecanumDrive(opMode.hardwareMap);
            subsystems.add(drive);
        } catch (IllegalArgumentException e) {

        }

    }

    public void start() {
        if (!started) {
            subsystemUpdateExecutor.submit(subsystemUpdateRunnable);
            started = true;
        }
    }

    private void stop() {
        if (subsystemUpdateExecutor != null) {
            subsystemUpdateExecutor.shutdownNow();
            subsystemUpdateExecutor = null;
        }

    }

}
