package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode.DriveCode;

@Config
@TeleOp(name = "QTDriveCode", group = "Linear OpMode")

public class DriveCode extends LinearOpMode {

    // Driver Code
    public GamepadEx driver;
    public GamepadEx operator;

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    public DcMotor arm = null;

    private Servo armServo = null;
    private Servo armPivotServo = null;

    private PIDController armController;
    private static double p = 0.0005, i = 0.1, d = 0.00575;
    private static double f = 0.05;

    int armtarget;

    //Outtake subsystem
    private DcMotor leftSlide = null;
    private DcMotor rightSlide = null;

    private Servo outtakeClawServo = null;
    private Servo slideLeftServo = null;
    private Servo slideRightServo = null;

    private PIDController slideController;


    private static double Kp = 0.0005, Ki = 0.1, Kd = 0.006, Kf = 0;


    //Gobilda 202 19.2:1
    private final double ticks_in_degree = 537.7/360;

    int currpos = 2;

    //pick up arm servo pos
    double STATE_1[] = {0,1};

    //Stand-by arm servo pos
    double STATE_2[] = {.5,.7};

    //ready to score arm servo pos
    double STATE_3[] = {0.7,.3};

    //Scored arm servo pos
    double STATE_4[] = {0.95,0};

    // Note: pushing stick forward gives negative value

    @Override
    public void runOpMode() {

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);

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

        //arm Subsystem
        arm = hardwareMap.get(DcMotor.class, "arm");
        armServo = hardwareMap.get(Servo.class, "intakeClawServo");
        armPivotServo = hardwareMap.get(Servo.class, "intakePivotServo");

        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armController = new PIDController(p, i, d);

        //Outtake Subsystem
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

        //Set Servos to stand-by
        outakearmPosState2();
        //Set pivot to neutral
        setArmPivotServoBack();
        //claws to outside
        armServoOpen(0.35);

        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
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

            if (gamepad1.left_trigger > 0.01){
                arm.setPower(gamepad1.left_trigger);
            } else {
                arm.setPower(0);
            }
            if (gamepad1.right_trigger > 0.01){
                arm.setPower(-gamepad1.right_trigger);
            } else {
                arm.setPower(0);
            }
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
                armtarget = 1;
                armRetract();
            }
            // Moves slides up to basket
            if (operator.getButton(GamepadKeys.Button.DPAD_UP)){
                slidesMove(-27 * 122);
            }
            if (operator.getButton(GamepadKeys.Button.DPAD_LEFT)){
                slidesMove(-5 * 122);
            }
            //Moves Slides down
            if (operator.getButton(GamepadKeys.Button.DPAD_DOWN)) {
                slidesMove(0);
            }
            // slide arm claw open
            if (operator.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
                outtakeServoOpen();
            }
            // arm claw close

            if (operator.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                outtakeServoClose();
            }

            if (operator.getButton(GamepadKeys.Button.B)){
                outakearmPosState3();
            }
            if (operator.getButton(GamepadKeys.Button.X)){
                outakearmPosState2();
            }

            if (operator.getButton(GamepadKeys.Button.Y)){
                outakearmPosState4();
            }
            if (operator.getButton(GamepadKeys.Button.A)){
                outakearmPosState1();
            }

            //when OpMode is Active
        }
        //Run OpMode
    }
    public void armRetract() {
        outakearmPosState2();

        armController.setPID(p, i, d);

            int armPos = arm.getCurrentPosition();
            double armPID = armController.calculate(armPos, armtarget);
            double armFF = Math.cos(Math.toRadians(armtarget / ticks_in_degree)) * f;

            double armpower = armPID + armFF;

            arm.setPower(armpower);

            telemetry.addData("armPos", armPos);
            telemetry.addData("armTarget", armtarget);
            telemetry.update();

        outakearmPosState1();
        outtakeServoClose();
        armServoOpen(0.2);
        outakearmPosState2();
    }
    public void armServoOpen(double pos){
        armServo.setPosition(pos);
    }

    public void armServoClose(){
        armServo.setPosition(0.025);
    }

    public void setArmPivotServoOut(){
        armPivotServo.setPosition(0.65);
    }

    public void setArmPivotServoBack(){
        armPivotServo.setPosition(0.5);
    }

    public void slidesMove(int slidetarget) {

        slideController.setPID(Kp, Ki, Kd);

            int slidePos = leftSlide.getCurrentPosition();
            double slidePID = slideController.calculate(slidePos, slidetarget);
            double slideFF = Math.cos(Math.toRadians(slidetarget / ticks_in_degree)) * Kf;

            double slidePower = slidePID + slideFF;

            leftSlide.setPower(slidePower);
            rightSlide.setPower(slidePower);

            telemetry.addData("slidePos", slidePos);
            telemetry.addData("slideTarget", slidetarget);
            telemetry.update();
    }

    public void outtakeServoOpen(){
        outtakeClawServo.setPosition(0.4);
    }

    public void outtakeServoClose(){
        outtakeClawServo.setPosition(0.025);
    }

    public void outakearmPosState1(){
        slideRightServo.setPosition(STATE_1[1]);
    }

    public void outakearmPosState2(){
        slideRightServo.setPosition(STATE_2[1]);
    }

    public void outakearmPosState3(){
        slideRightServo.setPosition(STATE_3[1]);
    }

    public void outakearmPosState4(){
        slideRightServo.setPosition(STATE_4[1]);
    }

    //end
}