package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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
import org.firstinspires.ftc.teamcode.Hw.Outtake;

@Config
@TeleOp(name = "QT Drive Code", group = "Linear OpMode")

public class DriveCode extends LinearOpMode {

    // Driver Code
    public GamepadEx driver;
    public GamepadEx operator;

    private ElapsedTime runtime = new ElapsedTime();

    int slidetarget;

    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    public DcMotor arm = null;

    private Servo armServo = null;
    private Servo armPivotServo = null;

    private PIDController armController;
    public static double p = 0.0025, i = 0.05, d = 0.0001;
    public static double f = 0;

    public static int armtarget = 0;

    //Outtake subsystem
    private DcMotor leftSlide = null;
    private DcMotor rightSlide = null;

    private Servo outtakeClawServo = null;
    private Servo slideLeftServo = null;
    private Servo slideRightServo = null;

    private PIDController slideController;

    private static double Kp = 0.009, Ki = 0, Kd = 0.0005, Kf = 0;

    //Gobilda 202 19.2:1
    private final double ticks_in_degree = 537.7/360;

    // Note: pushing stick forward gives negative value
    @Override
    public void runOpMode() {

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
        Outtake outtake = new Outtake();

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
        //arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armController = new PIDController(p, i, d);

        //Outtake Subsystem
        outtake.init();

        //Set pivot to neutral
        setArmPivotServoBack();
        //claws to outside
        armServoOpen(0.35);

        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            outtake.slidesMove();
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
                armtarget = 0;
                armRetract();
            }
            armRetract();

            if (driver.getButton(GamepadKeys.Button.DPAD_UP)){
                outtake.outtakeServoOpen();
                outtake.outakearmPosState4();
                sleep(1000);
                outtake.outtakeServoClose();
                sleep(100);
                armServoOpen(0.35);
                sleep(100);
                outtake.outakearmPosState1();
                sleep(1250);
                outtake.outtakeServoClosetight();
                sleep(100);
                outtake.outakearmPosState2();
            }
            if (driver.getButton(GamepadKeys.Button.Y)){
                armtarget = -350;
                armRetract();
            }

            if (driver.getButton(GamepadKeys.Button.DPAD_DOWN)){
                armtarget = -425;
                armRetract();
            }
            armRetract();
            // Moves slides up to basket
            if (operator.getButton(GamepadKeys.Button.DPAD_UP)){
                outtake.slidetarget = -3300;
                outtake.slidesMove();
            }
            outtake.slidesMove();
            if (operator.getButton(GamepadKeys.Button.DPAD_LEFT)){
                outtake.slidetarget = -500;
                outtake.slidesMove();
            }
            outtake.slidesMove();

            //Moves Slides down
            if (operator.getButton(GamepadKeys.Button.DPAD_DOWN)) {
                outtake.slidetarget = 0;
                outtake.slidesMove();
            }
            outtake.slidesMove();
            // slide arm claw open
            if (operator.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
                outtake.outtakeServoOpen();
            }
            // arm claw close

            if (operator.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                outtake.outtakeServoClose();
            }

            if (operator.getButton(GamepadKeys.Button.DPAD_RIGHT)){
                outtake.outtakeServoClosetight();
            }
            if (operator.getButton(GamepadKeys.Button.B)){
                outtake.outakearmPosState3();
            }
            if (operator.getButton(GamepadKeys.Button.X)){
                outtake.outakearmPosState2();
            }

            if (operator.getButton(GamepadKeys.Button.Y)){
                outtake.outakearmPosState4();
            }

            if (operator.getButton(GamepadKeys.Button.A)){
                outtake.outakearmPosState1();
            }

            //when OpMode is Active
        }
        //Run OpMode
    }
    public void armRetract() {
        armController.setPID(p, i, d);

            int armPos = arm.getCurrentPosition();
            double armPID = armController.calculate(armPos, armtarget);
            double armFF = Math.cos(Math.toRadians(armtarget / ticks_in_degree)) * f;

            double armpower = armPID + armFF;

            arm.setPower(armpower);

            telemetry.addData("armPos", armPos);
            telemetry.addData("armTarget", armtarget);
            telemetry.update();
    }

    public void armServoOpen(double pos){
        armServo.setPosition(pos);
    }

    public void armServoClose(){
        armServo.setPosition(0.035);
    }

    public void setArmPivotServoOut(){
        armPivotServo.setPosition(0.35);
    }

    public void setArmPivotServoBack(){
        armPivotServo.setPosition(0.49);
    }
    //end
}