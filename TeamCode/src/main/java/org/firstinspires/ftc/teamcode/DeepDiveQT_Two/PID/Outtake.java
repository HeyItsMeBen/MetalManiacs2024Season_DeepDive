package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp
public class Outtake extends OpMode {

    private DcMotor leftSlide = null;
    private DcMotor rightSlide = null;

    private Servo outtakeClawServo = null;
    private Servo slideLeftServo = null;
    private Servo slideRightServo = null;

    private PIDController slideController;

    public static double Kp = 0.009, Ki = 0, Kd = 0.0005;
    double Kf = 0 ;
    public static int target = 0;

    //Gobilda 202 19.2:1
    private final double ticks_in_degree = 537.7/360;

    public static double servopos = 0.5;

    public static double leftservopos = 0;
    public static double rightservopos = 1;

    int currpos = 1;

    //pick up arm servo pos
    double STATE_1[] = {1,0};

    //Stand-by arm servo pos
    double STATE_2[] = {0.85,.15};

    //ready to score arm servo pos
    double STATE_3[] = {0,1};

    //Scored arm servo pos
    double STATE_4[] = {0.15,0.85};

    public void init() {


        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        slideLeftServo = hardwareMap.get(Servo.class, "leftOuttake");
        slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");
        outtakeClawServo = hardwareMap.get(Servo.class, "outtakeServo");

        leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideController = new PIDController(Kp, Ki, Kd);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void loop() {

        slideController.setPID(Kp, Ki, Kd);

        int slidePos = leftSlide.getCurrentPosition();
        double slidePID = slideController.calculate(slidePos, target);
        double slideFF = Math.cos(Math.toRadians(target / ticks_in_degree)) * Kf;

        double slidePower = slidePID + slideFF;

        leftSlide.setPower(slidePower);
        rightSlide.setPower(slidePower);

        telemetry.addData("leftservopos", slideLeftServo.getPosition());
        telemetry.addData("rightservopos", slideRightServo.getPosition());
        telemetry.addData("target", target);
        telemetry.addData("leftslidepos", leftSlide.getCurrentPosition());
        telemetry.update();

    }

    public void outtakeServoOpen(){
        outtakeClawServo.setPosition(0.4);
    }

    public void outtakeServoClose(){
        outtakeClawServo.setPosition(0.025);
    }

    public void increaseOuttakeArmState(){
        currpos = currpos + 1;
        checkOuttakeArmState();
    }

    public void decreaseOuttakeArmState(){
        currpos = currpos - 1;
        checkOuttakeArmState();
    }

    public void checkOuttakeArmState(){
        switch (currpos){
            case 1:
                slideLeftServo.setPosition(STATE_1[0]);
                slideRightServo.setPosition(STATE_1[1]);
                break;
            case 2:
                slideLeftServo.setPosition(STATE_2[0]);
                slideRightServo.setPosition(STATE_2[1]);
                break;
            case 3:
                slideLeftServo.setPosition(STATE_3[0]);
                slideRightServo.setPosition(STATE_3[1]);
                break;
            case 4:
                slideLeftServo.setPosition(STATE_4[0]);
                slideRightServo.setPosition(STATE_4[1]);
                break;
        }
    }

    public void neutralOuttakeArmState(){
        currpos = 2;
        checkOuttakeArmState();
    }

    //end
}
