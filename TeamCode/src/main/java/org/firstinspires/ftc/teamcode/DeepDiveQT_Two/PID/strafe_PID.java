//FILE PURPOSE: Should make the robot strafe using PID. I made this file using from a yt vid abt PID, then modified it to include the whole drivetrain instead of just one motor
package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
@Disabled
@Config
@TeleOp//(name = "strafe_PID", group = "Linear OpMode")
public class strafe_PID extends LinearOpMode {

    //creates all four motors using the extended version of DcMotor
    DcMotorEx frontLeftDrive;
    DcMotorEx backLeftDrive;
    DcMotorEx frontRightDrive;
    DcMotorEx backRightDrive;

    //creates variables. Some of these should be modifiable through the dashboard
    public static double Kp=0.005;
    public static double Ki;
    public static double Kd;
    public static double target2 = 5;

    private static final double ENCODER_COUNTS_PER_INCH = 38.1971863;
    double integralSum = 0;
    double wheelPosition;
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


        waitForStart();
        //gets target angle, calculates power using PID, then sends it through the wheels
        while (opModeIsActive()) {
            wheelPosition = frontRightDrive.getCurrentPosition();
            double power = PIDControl(target2*ENCODER_COUNTS_PER_INCH, wheelPosition);
            frontLeftDrive.setPower(power);
            backLeftDrive.setPower(power);
            frontRightDrive.setPower(power);
            backRightDrive.setPower(power);
            telemetry.addData("pos2", wheelPosition);
            telemetry.addData("target2", target2);
            telemetry.update();
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
