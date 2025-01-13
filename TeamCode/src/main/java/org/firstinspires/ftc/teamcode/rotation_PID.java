package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit; //just added this one
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles; //just added this one

@Config

//I'm commenting this (the part on thr line bellow) only because the Arm_PID doesn't have it, and the Arm_PID works. If commenting this code causes problems, u can uncomment it
@TeleOp//(name = "rotation_PID", group = "Linear OpMode")
public class rotation_PID extends LinearOpMode {

    //creates all four motors using the extended version of DcMotor
    DcMotorEx FrontLeft;
    DcMotorEx FrontRight;
    DcMotorEx BackLeft;
    DcMotorEx BackRight;

    //creates imu (used to track rotation)
    private BHI260IMU imu;


    //creates variables. Some of these should be modifiable through the dashboard
    double integralSum = 0;
    public static double Kp = 0.001;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double targetAngle=90;
    private double lastError = 0;


    ElapsedTime timer = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        //hardware-maps all four motors, then sets them to run without encoder. The video said to do that cuz the ticks can still be counted, but this allows it to be faster (or something like that)
        FrontLeft = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FrontRight = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        BackLeft = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        BackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        BackRight = hardwareMap.get(DcMotorEx.class, "backRightDrive");
        BackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        //sets directions for the motors
        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        FrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        BackRight.setDirection(DcMotorSimple.Direction.FORWARD);

        //initializes IMU
        IMU.Parameters myIMUparameters;
        myIMUparameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(myIMUparameters);

        YawPitchRollAngles robotOrientation;

        waitForStart();

        double referenceAngle;
        //gets target angle, calculates power using PID, then sends it through the wheels
        while (opModeIsActive()) {
            referenceAngle = Math.toRadians(targetAngle);
            robotOrientation = imu.getRobotYawPitchRollAngles();
            double power = PIDControl(referenceAngle, robotOrientation.getYaw(AngleUnit.RADIANS));
            FrontLeft.setPower(power);
            BackLeft.setPower(power);
            FrontRight.setPower(-power);
            BackRight.setPower(-power);
        }
    }


    //this method does the actual calculating for PID control
    public double PIDControl(double reference, double state) {
        double error = angleWrap(reference - state);
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;


        timer.reset();


        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki); //+ (reference * Kf);
        return output;
    }

    //this method just makes the robot turn the shortest possible distance to get to the angle it needs. It's usually not necessary, it just makes it faster.
    public double angleWrap(double radians) {
        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }
        return radians;
    }
}
