//FILE PURPOSE: Should make the robot strafe using PID. I made this file by modifying a copy of rotation_PID
package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;

import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@Disabled
//I'm commenting this (the part on thr line bellow) only because the Arm_PID doesn't have it, and the Arm_PID works. If commenting this code causes problems, u can uncomment it
@TeleOp//(name = "strafe_PID3_fromRotation", group = "Linear OpMode")
public class strafe_PID3_fromRotation extends LinearOpMode {

    //creates all four motors using the extended version of DcMotor
    DcMotorEx frontLeftDrive;
    DcMotorEx frontRightDrive;
    DcMotorEx backLeftDrive;
    DcMotorEx backRightDrive;

    //creates variables. Some of these should be modifiable through the dashboard
    public static double Kp = 0;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double targetPos=10;

    double integralSum = 0;
    private static final double ENCODER_COUNTS_PER_INCH = 38.1971863;
    private double lastError = 0;

    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        //hardware-maps all four motors, then sets them to run without encoder. The video said to do that cuz the ticks can still be counted, but this allows it to be faster (or something like that)
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //sets directions for the motors
        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);


        double wheelPosition;

        waitForStart();

        double referencePosition;
        //gets target angle, calculates power using PID, then sends it through the wheels
        while (opModeIsActive()) {
            referencePosition = targetPos*ENCODER_COUNTS_PER_INCH;
            wheelPosition = frontRightDrive.getCurrentPosition();
            double power = PIDControl(referencePosition, wheelPosition);
            frontLeftDrive.setPower(power);
            backLeftDrive.setPower(power);
            frontRightDrive.setPower(power);
            backRightDrive.setPower(power);
        }
    }

    //this method does the actual calculating for PID control
    public double PIDControl(double reference, double state) {
        double error = reference - state;
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;


        timer.reset();


        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki); //+ (reference * Kf);
        return output;
    }
}
