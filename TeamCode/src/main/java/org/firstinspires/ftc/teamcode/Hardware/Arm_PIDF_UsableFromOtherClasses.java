package org.firstinspires.ftc.teamcode.Hardware;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Arm_PIDF_UsableFromOtherClasses {
    private PIDController controller;
    public static double p = 0.00575, i = 0.1, d = 0.0005;
    public static double f = 0.05;
    private final double ticks_in_degree = 700 / 180.0;
    private DcMotorEx arm_motor;

    public Arm_PIDF_UsableFromOtherClasses(HardwareMap hMap) {
        controller = new PIDController(p, i, d);


        arm_motor = hMap.get(DcMotorEx.class, "arm");
        arm_motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        arm_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }


    public void setArmTarget(double givenTarget) {
        double target=givenTarget*400;
        controller.setPID(p, i, d);
        int armPos = arm_motor.getCurrentPosition();
        double pid = controller.calculate(armPos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power = pid + ff;

        arm_motor.setPower(power);
    }
    public void stopMotor(){
        arm_motor.setPower(0);
    }
}
