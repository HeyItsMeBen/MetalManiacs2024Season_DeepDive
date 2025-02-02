package org.firstinspires.ftc.teamcode.Hardware;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides_PID {
    private static double Kp = 0.009, Ki = 0, Kd = 0.0005, Kf = 0;
    private final double ticks_in_degree = 537.7/360;
    PIDController slideController= new PIDController(Kp, Ki, Kd);
    DcMotor rightSlide;
    DcMotor leftSlide;
    public Slides_PID (HardwareMap hMap) {
        leftSlide = hMap.get(DcMotor.class, "leftSlide");
        rightSlide = hMap.get(DcMotor.class, "rightSlide");
    }
    public void setSlidesTarget(double target){
        slideController.setPID(Kp, Ki, Kd);

        int slidePos = rightSlide.getCurrentPosition();
        double slidePID = slideController.calculate(slidePos, target);
        double slideFF = Math.cos(Math.toRadians(target / ticks_in_degree)) * Kf;

        double slidePower = slidePID + slideFF;

        leftSlide.setPower(slidePower);
        rightSlide.setPower(slidePower);
    }
}