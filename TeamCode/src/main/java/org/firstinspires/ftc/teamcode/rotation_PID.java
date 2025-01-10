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
@TeleOp(name = "rotation_PID", group = "Linear OpMode")
public class rotation_PID extends LinearOpMode {


    DcMotorEx FrontLeft;
    DcMotorEx FrontRight;
    DcMotorEx BackLeft;
    DcMotorEx BackRight;


    private BHI260IMU imu;  //IMP for debugging and functionality. Check actual model. (I think this uses a gyroscope)


    double integralSum = 0;
    public static double Kp = 0;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double targetAngle=90;
    //double Kf = 10;


    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        FrontLeft = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        FrontRight = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        BackLeft = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        BackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        BackRight = hardwareMap.get(DcMotorEx.class, "backRightDrive");
        BackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        //BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        /*imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);*/
        IMU.Parameters myIMUparameters;
        myIMUparameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(myIMUparameters);

        YawPitchRollAngles robotOrientation;

        waitForStart();

        double referenceAngle;
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


    public double PIDControl(double reference, double state) {
        double error = angleWrap(reference - state);
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;


        timer.reset();


        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki); //+ (reference * Kf);
        return output;
    }


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
