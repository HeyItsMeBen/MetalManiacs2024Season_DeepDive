package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode;

import org.firstinspires.ftc.teamcode.Hardware.compDrive;
import org.firstinspires.ftc.teamcode.Hardware.compClaw;
import org.firstinspires.ftc.teamcode.Hardware.compLinearSlide;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Second QT Drive Code", group = "Linear OpMode")
public class DriveCode extends LinearOpMode {

    // Driver Code: Variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private double theta;
    private double power;
    private double sine;
    private double cosine;
    private double turn;

    @Override
    public void runOpMode() {

        //Declare using Linear Slide object and Claw (Intake Arm and claws) object
        compLinearSlide linearSlide = new compLinearSlide(hardwareMap);
        compClaw claw = new compClaw(hardwareMap);

        // Driver Code: Map the 4 motors based off of Driver Station
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        // set direction for motors by default
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();

        //Start Button Pushed
        while (opModeIsActive()) {

            // Drive Code
            double max; //variable to define maximum motor values never > 100%
            double x = -gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            turn = -gamepad1.right_stick_x;
            theta = Math.atan2(y, x);
            power = Math.hypot(x, y);
            sine = Math.sin(theta - Math.PI / 4);
            cosine = Math.cos(theta - Math.PI / 4);
            max = Math.max(Math.abs(sine),
                    Math.abs(cosine));
            double leftFrontPower = power * cosine / max + turn;
            double rightFrontPower = power * sine / max - turn;
            double leftBackPower = power * sine / max + turn;
            double rightBackPower = power * cosine / max - turn;
            //Makes sure motor does NOT exceed more than 100% or else it will have bad behaviors >:(
            if ((power + Math.abs(turn)) > 1) {
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

            //Intake code: Arm & servos
            if (gamepad2.dpad_up) {
                claw.moveArm(0.5);
            } else if (gamepad2.dpad_down) {
                claw.moveArm(-0.5);
            }
            claw.moveArm(0);

            //Outtake code: Linear Slide & servos

            idle();

        }
    }
}