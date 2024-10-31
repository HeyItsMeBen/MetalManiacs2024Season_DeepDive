package org.firstinspires.ftc.teamcode.FinalDriveCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "DriveCode", group = "Linear OpMode")

public class DriveCode extends LinearOpMode {

    // Driver Code
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor arm = null;
    private DcMotor leftSlide = null;
    private DcMotor rightSlide = null;

    private Servo armLeftServo = null;
    private Servo armRightServo = null;
    private Servo slideLeftServo = null;
    private Servo slideRightServo = null;

    private double POWER_REDUCTION = 0.8;

    static final double COUNTS_PER_MOTOR_REVFIFTY = 1224;
    static final double COUNTS_PER_MOTOR_REVSIXTY = 1440;
    static final double COUNTS_PER_MOTOR_REVTWENTY = 480;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double SPOOL_CIRCUMFRENCE_INCHES = (112 / 25.4);
    static final double COUNTS_PER_INCH_SPOOL = (COUNTS_PER_MOTOR_REVTWENTY * DRIVE_GEAR_REDUCTION) /
            (SPOOL_CIRCUMFRENCE_INCHES);

    @Override
    public void runOpMode() {

        // Driver Code
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        arm = hardwareMap.get(DcMotor.class, "arm");
        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        armLeftServo = hardwareMap.get(Servo.class, "armLeftServo");
        armRightServo = hardwareMap.get(Servo.class, "armRightServo");
        slideLeftServo = hardwareMap.get(Servo.class, "slideLeftServo");
        slideRightServo = hardwareMap.get(Servo.class, "slideRightServo");

        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            // Drive Code
            double x = gamepad1.left_stick_x;  // Note: pushing stick forward gives negative value
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
            frontLeftDrive.setPower(leftFrontPower);
            frontRightDrive.setPower(rightFrontPower);
            backLeftDrive.setPower(leftBackPower);
            backRightDrive.setPower(rightBackPower);

            while (gamepad1.y) {
                arm.setPower(0.75);
            }
            arm.setPower(0);

            while (gamepad1.a) {
                arm.setPower(-0.75);
            }
            arm.setPower(0);

            if (gamepad1.left_bumper) {
                armLeftServo.setPosition(0.8);
                armRightServo.setPosition(0.6);
            }
            if (gamepad1.right_bumper) {
                armLeftServo.setPosition(0.55);
                armRightServo.setPosition(0.55);
            }
            // Slide move up for Rung Scoring
            if (gamepad2.x) {
                slideMoveUpRung(0.75, 3);
            }
            if (gamepad2.b) {
                slideMoveDownRung(0.75, 3);
            }

            if (gamepad2.a) {
                basketScore(0.75, 5);
            }
            if (gamepad2.left_bumper) {
                slideLeftServo.setPosition(0.0);
                slideRightServo.setPosition(1.0);
            }
            if (gamepad2.right_bumper) {
                slideLeftServo.setPosition(-0.5);
                slideRightServo.setPosition(-0.5);
            }
        }

    }

    public void slideMoveUpRung ( double speed2, double timeout2){
        int newLeftSlideTarget;
        int newRightSlideTarget;

        if (opModeIsActive()) {
            newLeftSlideTarget = leftSlide.getCurrentPosition() + (int) (15 * COUNTS_PER_INCH_SPOOL);
            newRightSlideTarget = leftSlide.getCurrentPosition() + (int) (15 * COUNTS_PER_INCH_SPOOL);

            leftSlide.setTargetPosition(newLeftSlideTarget);
            rightSlide.setTargetPosition(newRightSlideTarget);

            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            leftSlide.setPower(Math.abs(speed2));
            rightSlide.setPower(Math.abs(speed2));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeout2) &&
                    (leftSlide.isBusy() && rightSlide.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to", " %7d :%7d", newLeftSlideTarget, newRightSlideTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        leftSlide.getCurrentPosition(), rightSlide.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftSlide.setPower(0);
            rightSlide.setPower(0);

            // Turn off RUN_TO_POSITION
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);
        }
    }

    public void slideMoveDownRung ( double speed2, double timeout2){
        int newLeftSlideTarget;
        int newRightSlideTarget;


        if (opModeIsActive()) {
            newLeftSlideTarget = leftSlide.getCurrentPosition() + (int) (-2.5 * COUNTS_PER_INCH_SPOOL);
            newRightSlideTarget = rightSlide.getCurrentPosition() + (int) (-2.5 * COUNTS_PER_INCH_SPOOL);

            leftSlide.setTargetPosition(newLeftSlideTarget);
            rightSlide.setTargetPosition(newRightSlideTarget);

            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            leftSlide.setPower(Math.abs(speed2));
            rightSlide.setPower(Math.abs(speed2));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeout2) &&
                    (leftSlide.isBusy() && rightSlide.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to", " %7d :%7d", newLeftSlideTarget, newRightSlideTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        frontLeftDrive.getCurrentPosition(), frontRightDrive.getCurrentPosition(), backLeftDrive.getCurrentPosition(), backRightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftSlide.setPower(0);
            rightSlide.setPower(0);

            // Turn off RUN_TO_POSITION
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);

            slideLeftServo.setPosition(0.0);
            slideRightServo.setPosition(1.0);

            sleep(250);

            newLeftSlideTarget = leftSlide.getCurrentPosition() + (int) (-11 * COUNTS_PER_INCH_SPOOL);
            newRightSlideTarget = rightSlide.getCurrentPosition() + (int) (-11 * COUNTS_PER_INCH_SPOOL);

            leftSlide.setTargetPosition(newLeftSlideTarget);
            rightSlide.setTargetPosition(newRightSlideTarget);

            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            leftSlide.setPower(Math.abs(speed2));
            rightSlide.setPower(Math.abs(speed2));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeout2) &&
                    (leftSlide.isBusy() && rightSlide.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to", " %7d :%7d", newLeftSlideTarget, newRightSlideTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        leftSlide.getCurrentPosition(), rightSlide.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftSlide.setPower(0);
            rightSlide.setPower(0);

            // Turn off RUN_TO_POSITION
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(250);
        }
    }
    public void basketScore ( double speed2, double timeout2){
        int newLeftSlideTarget;
        int newRightSlideTarget;

        if (opModeIsActive()) {
            newLeftSlideTarget = leftSlide.getCurrentPosition() + (int) (15 * COUNTS_PER_INCH_SPOOL);
            newRightSlideTarget = leftSlide.getCurrentPosition() + (int) (15 * COUNTS_PER_INCH_SPOOL);

            leftSlide.setTargetPosition(newLeftSlideTarget);
            rightSlide.setTargetPosition(newRightSlideTarget);

            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            leftSlide.setPower(Math.abs(speed2));
            rightSlide.setPower(Math.abs(speed2));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeout2) &&
                    (leftSlide.isBusy() && rightSlide.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to", " %7d :%7d", newLeftSlideTarget, newRightSlideTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        leftSlide.getCurrentPosition(), rightSlide.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftSlide.setPower(0);
            rightSlide.setPower(0);

            // Turn off RUN_TO_POSITION
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);

            slideLeftServo.setPosition(0.0);
            slideRightServo.setPosition(1.0);

            sleep(250);

            newLeftSlideTarget = leftSlide.getCurrentPosition() + (int) (-13 * COUNTS_PER_INCH_SPOOL);
            newRightSlideTarget = leftSlide.getCurrentPosition() + (int) (-13 * COUNTS_PER_INCH_SPOOL);

            leftSlide.setTargetPosition(newLeftSlideTarget);
            rightSlide.setTargetPosition(newRightSlideTarget);

            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            leftSlide.setPower(Math.abs(speed2));
            rightSlide.setPower(Math.abs(speed2));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeout2) &&
                    (leftSlide.isBusy() && rightSlide.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to", " %7d :%7d", newLeftSlideTarget, newRightSlideTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        leftSlide.getCurrentPosition(), rightSlide.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftSlide.setPower(0);
            rightSlide.setPower(0);

            // Turn off RUN_TO_POSITION
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);
        }
    }
}
// Signal done;
