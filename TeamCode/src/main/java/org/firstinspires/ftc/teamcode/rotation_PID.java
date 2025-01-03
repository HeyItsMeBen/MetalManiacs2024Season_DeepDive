package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "rotation_PID", group = "Linear OpMode")
public class rotation_PID extends LinearOpMode {


    DcMotorEx FrontLeft;
    DcMotorEx FrontRight;
    DcMotorEx BackLeft;
    DcMotorEx BackRight;


    private BNO055IMU imu;  //IMP for debugging and functionality. Check actual model. (I think this uses a gyroscope)


    double integralSum = 0;
    double Kp = 0;
    double Ki = 0;
    double Kd = 0;
    //double Kf = 10;


    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        FrontLeft = hardwareMap.get(DcMotorEx.class, "motor");
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        FrontRight = hardwareMap.get(DcMotorEx.class, "FrontRight");
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        BackLeft = hardwareMap.get(DcMotorEx.class, "BackLeft");
        BackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        BackRight = hardwareMap.get(DcMotorEx.class, "BackRight");
        BackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        //BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);


        waitForStart();


        while (opModeIsActive()) {
            // Main loop code
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
