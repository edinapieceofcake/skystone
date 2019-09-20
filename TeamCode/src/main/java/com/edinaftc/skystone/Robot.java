package com.edinaftc.skystone;

import com.edinaftc.library.subsystems.Intake;
import com.edinaftc.library.subsystems.MecanumDrive;
import com.edinaftc.library.subsystems.Subsystem;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Robot {
    private ExecutorService subsystemUpdateExecutor;
    private boolean started;

    public MecanumDrive drive;

    public Intake intake;

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

        try {
            intake = new Intake(opMode.hardwareMap);
            subsystems.add(intake);
        } catch (IllegalArgumentException e) {

        }


        subsystemUpdateExecutor = ThreadPool.newSingleThreadExecutor("subsystem update");
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
