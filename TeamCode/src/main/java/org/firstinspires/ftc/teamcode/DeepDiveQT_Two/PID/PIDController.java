package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;


import android.annotation.TargetApi;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

public class PIDController {
    private double kP, kI, kD;
    private double errorSum = 0;
    private double lastError = 0;
    private double maxOutput = 1.0;  // Max motor power (1.0 = 100%)
    private double minOutput = -1.0; // Min motor power

    public static double p, i, d = 0;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void setOutputLimits(double min, double max) {
        this.minOutput = min;
        this.maxOutput = max;
    }

    public double calculate(double target, double current) {
        double error = target - current; // gets amount of error by subtracting traget position from current one

        // Proportional term
        p = kP * error; // calculates PID

        // Integral term
        errorSum += error;
        i = kI * errorSum; // calculates integral

        // Derivative term
        double derivative = error - lastError;
        d = kD * derivative; // calculates derivative

        lastError = error;

        // Calculate output and clamp to limits
        double output = p + i + d;
        output = Math.max(minOutput, Math.min(maxOutput, output)); // Clamp output
        return output;
    }

    public double getProportional () {
        return p;
    }

    public double getIntegral () {
        return i;
    }

    public double getDerivative () {
        return d;
    }

    public void errorValueReset() {
        errorSum = 0;
        lastError = 0;
    }
}
