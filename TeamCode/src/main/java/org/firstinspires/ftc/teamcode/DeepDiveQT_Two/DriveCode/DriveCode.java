package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hw.Intake;
import org.firstinspires.ftc.teamcode.Hw.Outtake;

import java.util.concurrent.CountDownLatch;

@Config
@TeleOp(name = "QT Drive Code", group = "Linear OpMode")

public class DriveCode extends LinearOpMode {

    // Driver Code
    public GamepadEx driver;
    public GamepadEx operator;

    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;


    private DcMotor leftSlide = null;
    private DcMotor rightSlide = null;

    private Servo outtakeClawServo = null;
    private Servo leftOuttakeArm = null;
    private Servo rightOuttakeArm = null;

    private PIDController slideController;

    public static double Kp = 0.009, Ki = 0, Kd = 0.0005;
    double Kf = 0 ;
    public static int slidetarget = 0;

    double[] STATE_1 = {1,0};

    //Stand-by arm servo pos
    double[] STATE_2 = {0.7,.3};

    //ready to score arm servo pos
    double[] STATE_3 = {.3,.7};

    //Scored arm servo pos
    double[] STATE_4 = {0.135,0.865};


    public DcMotor arm = null;
    private Servo armServo = null;
    private Servo armPivotServo = null;
    private PIDController armController;
    public static double p = 0.005, i = 0., d = 0.00075;
    public static double f = 0;

    public static int armtarget = 0;

    public double pivotTarget = 0.5;
    // Note: pushing stick forward gives negative value
    @Override
    public void runOpMode() {

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);

        slideController = new PIDController(Kp, Ki, Kd);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        ElapsedTime runtime = new ElapsedTime();

        armController = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        // Driver Code
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        //arm and slides needs to run with encoder
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        leftOuttakeArm = hardwareMap.get(Servo.class, "leftOuttake");
        rightOuttakeArm = hardwareMap.get(Servo.class, "rightOuttake");
        outtakeClawServo = hardwareMap.get(Servo.class, "outtakeServo");
        //Outtake Subsystem init

        leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //Intake Subsystem init
        //arm Subsystem
        arm = hardwareMap.get(DcMotor.class, "arm");
        armServo = hardwareMap.get(Servo.class, "intakeClawServo");
        armPivotServo = hardwareMap.get(Servo.class, "intakePivotServo");

        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //close outtake claw
        outtakeServoClosetight();
        //outtake arm pos 4
        outtakearmPosState4();
        //Set pivot to neutral
        setArmPivotServoBack();
        //claws to outside
        armServoClose();
        //intake.armServoOpen(0.35);
        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            slidesMove();
            armRetract();
            // Drive Code
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            double theta = Math.atan2(y, x);
            double power = Math.hypot(x, y);
            double sin = Math.sin(theta - Math.PI/4);
            double cos = Math.cos(theta - Math.PI/4);
            double max = Math.max(Math.abs(sin), Math.abs(cos));

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = power * cos/max + turn;
            double rightFrontPower = power * sin/max - turn;
            double leftBackPower = power * sin/max + turn;
            double rightBackPower = power * cos/max - turn;

            if ((power + Math.abs(turn)) > 1){
                leftFrontPower /= power + turn;
                rightFrontPower /= power + turn;
                leftBackPower /= power + turn;
                rightBackPower /= power + turn;
            }

            // Send calculated power to wheels
            frontLeftDrive.setPower(leftFrontPower * 0.8);
            frontRightDrive.setPower(rightFrontPower * 0.8);
            backLeftDrive.setPower(leftBackPower * 0.8);
            backRightDrive.setPower(rightBackPower * 0.8);

            // arm claw open
            if (driver.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
                armServoOpen(0.35);
            }
            // arm claw close
            if (driver.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                armServoClose();
            }
            //Makes intake pivot go out 0.15 so variable is now 0.65
            if (driver.getButton(GamepadKeys.Button.X)) {
                setArmPivotServoOut();
            }
            //Brings pivot back to 0.5
            if (driver.getButton(GamepadKeys.Button.B)){
                setArmPivotServoBack();
            }
            //brings arm back and allows for it to be picked up by outtake arm
            if (driver.getButton(GamepadKeys.Button.A)){
                Intake.armtarget = 0;
                setArmPivotServoBack();
                armRetract();
            }
            armRetract();

            if (driver.getButton(GamepadKeys.Button.DPAD_UP)){

                outtakeServoOpen();
                outtakearmPosState4();
                runtime.reset();
                sleep(1000);
                outtakeServoClosetight();
                sleep(100);
                armServoOpen(0.35);
                sleep(100);
                outtakearmPosState2();
            }
            if (driver.getButton(GamepadKeys.Button.Y)){
                Intake.armtarget = -350;
                armRetract();
            }

            if (driver.getButton(GamepadKeys.Button.DPAD_DOWN)){
                Intake.armtarget = -425;
                armRetract();
            }
            armRetract();
            // Moves slides up to basket
            if (operator.getButton(GamepadKeys.Button.DPAD_UP)){
                Outtake.slidetarget = 3300;
                slidesMove();
            }
            slidesMove();
            if (operator.getButton(GamepadKeys.Button.DPAD_LEFT)){
                Outtake.slidetarget = 600;
                slidesMove();
            }
            slidesMove();

            //Moves Slides down
            if (operator.getButton(GamepadKeys.Button.DPAD_DOWN)) {
                Outtake.slidetarget = 0;
                slidesMove();
            }
            slidesMove();
            // slide arm claw open
            if (operator.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
                outtakeServoOpen();
            }
            // arm claw close

            if (operator.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                outtakeServoClose();
            }

            if (operator.getButton(GamepadKeys.Button.DPAD_RIGHT)){
                outtakeServoClosetight();
            }
            if (operator.getButton(GamepadKeys.Button.B)){
                outtakearmPosState3();
            }
            if (operator.getButton(GamepadKeys.Button.X)){
                outtakearmPosState2();
            }

            if (operator.getButton(GamepadKeys.Button.Y)){
                outtakearmPosState4();
            }

            if (operator.getButton(GamepadKeys.Button.A)){
                outtakearmPosState1();
            }

            //when OpMode is Active
        }
        //Run OpMode
    }
    public void outtakeServoOpen(){
        outtakeClawServo.setPosition(0.2);
        if (slidetarget == 3300){
                outtakearmPosState3();
        }
    }

    public void outtakeServoClose(){outtakeClawServo.setPosition(0.035);}

    public void outtakeServoClosetight(){outtakeClawServo.setPosition(0);}

    public void outtakearmPosState1(){
        leftOuttakeArm.setPosition(STATE_1[0]);
        rightOuttakeArm.setPosition(STATE_1[1]);

    }

    public void outtakearmPosState2(){
        leftOuttakeArm.setPosition(STATE_2[0]);
        rightOuttakeArm.setPosition(STATE_2[1]);
    }

    public void outtakearmPosState3(){
        leftOuttakeArm.setPosition(STATE_3[0]);
        rightOuttakeArm.setPosition(STATE_3[1]);
    }

    public void outtakearmPosState4(){
        leftOuttakeArm.setPosition(STATE_4[0]);
        rightOuttakeArm.setPosition(STATE_4[1]);
    }
    public void slidesMove() {

        slideController.setPID(Kp, Ki, Kd);
        double ticks_in_degree = 537.7 / 360;
        int slidePos = rightSlide.getCurrentPosition();
        double slidePID = slideController.calculate(slidePos, slidetarget);
        //Gobilda 202 19.2:1

        double slideFF = Math.cos(Math.toRadians(slidetarget / ticks_in_degree)) * Kf;

        double slidePower = slidePID + slideFF;

        if (slidetarget == 0) {
            leftSlide.setPower(slidePower * 0.8);
            rightSlide.setPower(slidePower * 0.8);
        } else {
            leftSlide.setPower(slidePower);
            rightSlide.setPower(slidePower);
        }

        telemetry.addData("slidePos", slidePos);
        telemetry.addData("slideTarget", slidetarget);
        telemetry.update();
    }
    public void armRetract() {
        armController.setPID(p, i, d);
        double ticks_in_degree = 537.7 / 360;
        int armPos = arm.getCurrentPosition();
        double armPID = armController.calculate(armPos, armtarget);
        double armFF = Math.cos(Math.toRadians(armtarget / ticks_in_degree)) * f;

        double armpower = armPID + armFF;

        arm.setPower(armpower * 0.75);

        telemetry.addData("armPos", armPos);
        telemetry.addData("armTarget", armtarget);
        telemetry.update();
    }

    public void armServoOpen(double pos){
        armServo.setPosition(pos);
    }

    public void armServoClose(){
        armServo.setPosition(0.0425);
    }

    public void setArmPivotServoOut(){
        pivotTarget += 0.15;
        armPivotServo.setPosition(pivotTarget);
    }

    public void setArmPivotServoBack(){
        armPivotServo.setPosition(0.5);
    }
    //end
}