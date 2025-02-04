package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode;

import com.acmerobotics.dashboard.config.Config;
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

    //Gobilda 202 19.2:1
    private final double ticks_in_degree = 537.7/360;

    // Note: pushing stick forward gives negative value
    @Override
    public void runOpMode() {

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
        Outtake outtake = new Outtake();
        Intake intake = new Intake();

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


        //Outtake Subsystem init
        outtake.init();
        //Intake Subsystem init
        intake.init();
        //Set pivot to neutral
        intake.setArmPivotServoBack();
        //claws to outside
        intake.armServoOpen(0.35);

        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            outtake.slidesMove();
            intake.armRetract();
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
                intake.armServoOpen(0.35);
            }
            // arm claw close
            if (driver.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                intake.armServoClose();
            }
            //Makes intake pivot go out 0.15 so variable is now 0.65
            if (driver.getButton(GamepadKeys.Button.X)) {
                intake.setArmPivotServoOut();
            }
            //Brings pivot back to 0.5
            if (driver.getButton(GamepadKeys.Button.B)){
                intake.setArmPivotServoBack();
            }
            //brings arm back and allows for it to be picked up by outtake arm
            if (driver.getButton(GamepadKeys.Button.A)){
                Intake.armtarget = 0;
                intake.armRetract();
            }
            intake.armRetract();

            if (driver.getButton(GamepadKeys.Button.DPAD_UP)){
                outtake.outtakeServoOpen();
                outtake.outtakearmPosState4();
                sleep(1000);
                outtake.outtakeServoClose();
                sleep(100);
                intake.armServoOpen(0.35);
                sleep(100);
                outtake.outtakearmPosState1();
                sleep(1250);
                outtake.outtakeServoClosetight();
                sleep(100);
                outtake.outtakearmPosState2();
            }
            if (driver.getButton(GamepadKeys.Button.Y)){
                Intake.armtarget = -350;
                intake.armRetract();
            }

            if (driver.getButton(GamepadKeys.Button.DPAD_DOWN)){
                Intake.armtarget = -425;
                intake.armRetract();
            }
            intake.armRetract();
            // Moves slides up to basket
            if (operator.getButton(GamepadKeys.Button.DPAD_UP)){
                Outtake.slidetarget = -3300;
                outtake.slidesMove();
            }
            outtake.slidesMove();
            if (operator.getButton(GamepadKeys.Button.DPAD_LEFT)){
                Outtake.slidetarget = -500;
                outtake.slidesMove();
            }
            outtake.slidesMove();

            //Moves Slides down
            if (operator.getButton(GamepadKeys.Button.DPAD_DOWN)) {
                Outtake.slidetarget = 0;
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
                outtake.outtakearmPosState3();
            }
            if (operator.getButton(GamepadKeys.Button.X)){
                outtake.outtakearmPosState2();
            }

            if (operator.getButton(GamepadKeys.Button.Y)){
                outtake.outtakearmPosState4();
            }

            if (operator.getButton(GamepadKeys.Button.A)){
                outtake.outtakearmPosState1();
            }

            //when OpMode is Active
        }
        //Run OpMode
    }
    //end
}