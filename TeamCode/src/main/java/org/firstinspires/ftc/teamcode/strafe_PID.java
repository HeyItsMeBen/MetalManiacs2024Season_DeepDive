package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Config
@Autonomous(name = "strafe_PID", group = "Autonomous")
public class strafe_PID extends LinearOpMode {


    DcMotorEx frontLeftWheel;
    DcMotorEx backLeftWheel;
    DcMotorEx frontRightWheel;
    DcMotorEx backRightWheel;


    private static final double ENCODER_COUNTS_PER_INCH = 38.1971863;
    double integralSum = 0;
    public static double Kp=0.005;
    public static double Ki;
    public static double Kd;
    double wheelPosition;
    public static double target2 = 5;
    //double Kf = 2;


    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        frontLeftWheel = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        frontLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        backLeftWheel = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        backLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        frontRightWheel = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        frontRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        backRightWheel = hardwareMap.get(DcMotorEx.class, "backRightDrive");
        backRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        frontLeftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightWheel.setDirection(DcMotorSimple.Direction.FORWARD);


        waitForStart();
        while (opModeIsActive()) {
            wheelPosition = frontRightWheel.getCurrentPosition();
            double power = PIDControl(target2*ENCODER_COUNTS_PER_INCH, wheelPosition);
            frontLeftWheel.setPower(power);
            backLeftWheel.setPower(power);
            frontRightWheel.setPower(power);
            backRightWheel.setPower(power);
            telemetry.addData("pos2", wheelPosition);
            telemetry.addData("target2", target2);
            telemetry.update();
        }
    }


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
