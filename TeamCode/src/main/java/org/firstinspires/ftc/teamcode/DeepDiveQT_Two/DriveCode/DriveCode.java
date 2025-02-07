package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    Intake intake;
    int armTarget;

    Outtake outtake;
    int slideTarget;
    int manualSlides = 0; //0 = false, 1 = true

    // Note: pushing stick forward gives negative value
    @Override
    public void runOpMode() {

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);

        ElapsedTime runtime = new ElapsedTime();

        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);

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


        //close outtake claw
        outtake.outtakeServoClosetight();
        //outtake arm pos 4
        outtake.outtakearmPosState4();
        //Set pivot to neutral
        intake.setArmPivotServoBack();
        //claws to outside
        intake.armServoClose();
        //intake.armServoOpen(0.35);
        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            outtake.slidesMove(slideTarget);
            intake.armRetract(armTarget);
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
                armTarget = -5;
                intake.setArmPivotServoBack();
                intake.armRetract(armTarget);
            }
            intake.armRetract(armTarget);

            if (driver.getButton(GamepadKeys.Button.DPAD_UP)){

                outtake.outtakeServoOpen();
                outtake.outtakearmPosState4();
                runtime.reset();
                sleep(1000);
                outtake.outtakeServoClosetight();
                sleep(100);
                intake.armServoOpen(0.35);
                sleep(100);
                outtake.outtakearmPosState2();
            }
            if (driver.getButton(GamepadKeys.Button.Y)){
                if(armTarget == 0) {
                    armTarget = -350;
                }
                if (armTarget == -350){
                    armTarget = -425;
                }
                if (armTarget == -425){
                    armTarget = -350;
                }
                intake.armRetract(armTarget);
            }
            intake.armRetract(armTarget);

            // Moves slides up to basket
            if (operator.getButton(GamepadKeys.Button.DPAD_UP)){
                slideTarget = 3300;
                outtake.slidesMove(slideTarget);
                manualSlides = 0;
            }
            outtake.slidesMove(slideTarget);

            if (operator.getButton(GamepadKeys.Button.DPAD_LEFT)){
                slideTarget = 825;
                manualSlides = 1;
                outtake.slidesMove(slideTarget);
            }
            outtake.slidesMove(slideTarget);

            if (manualSlides == 1 && gamepad2.right_stick_y >= 0.01){
                double slidePower = gamepad2.right_stick_y;
                outtake.manualSlidesMove(slidePower);
            }

            //Moves Slides down
            if (operator.getButton(GamepadKeys.Button.DPAD_DOWN)) {
                slideTarget = 0;
                manualSlides = 0;
                outtake.slidesMove(slideTarget);

            }
            outtake.slidesMove(slideTarget);
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