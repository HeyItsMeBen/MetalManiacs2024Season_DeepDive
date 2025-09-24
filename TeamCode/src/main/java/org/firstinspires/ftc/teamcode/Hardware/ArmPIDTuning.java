package org.firstinspires.ftc.teamcode.Hardware;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
@Autonomous(name = "ArmPIDTuning", group = "Autonomous")
public class ArmPIDTuning extends LinearOpMode {
    private PIDController controller;
    public static double p = 0, i = 0, d = 0;
    public static double f = 0;
    private final double ticks_in_degree = 1120 / 360;
    public static double givenTarget=0;
    private DcMotorEx arm_motor;

    public void runOpMode() {
        controller = new PIDController(p, i, d);


        arm_motor = hardwareMap.get(DcMotorEx.class, "arm");   //real name?
        arm_motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        arm_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm_motor.setDirection(DcMotor.Direction.FORWARD);
        arm_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while (opModeIsActive()){
            double target=givenTarget*-325;
            controller.setPID(p, i, d);
            int armPos = arm_motor.getCurrentPosition();
            double pid = controller.calculate(armPos, givenTarget);
            double ff = Math.cos(Math.toRadians(givenTarget / ticks_in_degree)) * f;

            double power = pid + ff;

            arm_motor.setPower(power);
            telemetry.addData("target", givenTarget);
            telemetry.addData("armPos", armPos);
            telemetry.update();
        }
    }
    public void stopMotor(){
        arm_motor.setPower(0);
    }
}

