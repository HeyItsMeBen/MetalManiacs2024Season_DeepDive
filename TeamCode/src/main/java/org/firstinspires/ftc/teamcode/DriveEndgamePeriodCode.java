package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Drive & Endgame Period Code", group = "Linear OpMode")
public class DriveEndgamePeriodCode extends LinearOpMode {

    // Driver Code
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private float POWER_REDUCTION = 2;
    private static final int LINEAR_ENCODER_COUNTS_PER_INCH = 43;
    private double theta    ;
    private double power;
    private double sine;
    private double cosine;
    private double max;
    private double turn;
    int linearencoderCountsToMove = (int) (48 * LINEAR_ENCODER_COUNTS_PER_INCH);
    @Override
    public void runOpMode() {

        compLinearSlide linearSlide = new compLinearSlide(hardwareMap);
        compClaw claw = new compClaw(hardwareMap);

        // Driver Code
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized" );
        telemetry.update();
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {

            //SET this 2 to use math theta, sine, cosine; otherwise SET to 0.
            int intTestMode = 2;

            // Drive Code
            double max;
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   =   gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  -gamepad1.left_stick_x;
            double yaw     =   -gamepad1.right_stick_x;

            if (intTestMode > 1) {

                double x = -gamepad1.left_stick_x;
                double y = gamepad1.left_stick_y;
                turn = -gamepad1.right_stick_x;
                theta = Math.atan2(y, x);
                power = Math.hypot(x, y);

                sine = Math.sin(theta - Math.PI/4);
                cosine = Math.cos(theta - Math.PI/4);

            }
            else
            {
                telemetry.addData("DriveEndgame Period", "TEST MODE 0");
                telemetry.update();
            }

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;


            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));
            max = POWER_REDUCTION*max; //Reduces power to slow down robot. This can be modified to increase or reduce robot speed by will.
            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            if (intTestMode > 1) {
                telemetry.addData("DriveEndgame Period", "testing strafe :) 0");
                telemetry.update();
                max = Math.max(Math.abs(sine),
                        Math.abs(cosine));

                leftFrontPower = power * cosine / max + turn;
                rightFrontPower = power * sine / max - turn;
                leftBackPower = power * sine / max + turn;
                rightBackPower = power * cosine / max - turn;

                //Makes sure motor does NOT exceed more than 100% or else it will have bad behaviors >:(
                if ((power + Math.abs(turn))>1) {
                    leftFrontPower /= power + turn;
                    rightFrontPower /= power + turn;
                    leftBackPower /= power + turn;
                    rightBackPower /= power + turn;
                }
            }

            // Send calculated power to wheels
            frontLeftDrive.setPower(leftFrontPower);
            frontRightDrive.setPower(rightFrontPower);
            backLeftDrive.setPower(leftBackPower);
            backRightDrive.setPower(rightBackPower);

            //Intake code: Arm
            //To utilize, set the gamepad to start + 1
            //Activate by toggling the triggers
            if (gamepad1.left_trigger > 0){
                claw.moveArm(-0.5);
            }
            if (gamepad1.right_trigger > 0) {
                claw.moveArm(0.5);
            }
            claw.moveArm(0);

            //Intake code: Servos
            //To utilize, set the gamepad to start + 2
            //Activate by toggling the triggers
            if (gamepad1.left_bumper){ //Open
                claw.open_close(0.6,0.75);
            }
            if (gamepad1.right_bumper) { //Close
                claw.open_close(0.55, 0.8);
            }

            //Outtake code: Linear Slides
            //To utilize, set the gamepad to start + 2
            //Activate by using the up/down right joystick
                 if (gamepad2.right_stick_y > 0){
                linearSlide.extendVertical(.75);
            }
                 //changed it to less then to move slides down :) ev
            if (gamepad2.right_stick_y < 0){
                linearSlide.extendVertical(-.75);
            }
            linearSlide.extendVertical(0);

            //Outtake code: Servos
            //To utilize, set the gamepad to start + 2
            //Activate by pressing the bumpers

           /*open*/ if (gamepad2.left_bumper) {
                linearSlide.open_close_outtake(1.0, 0.625);
                telemetry.addData("OpenOutakeClaw", "testing servo OPEN");
                telemetry.update();
            }
            /*close*/ if (gamepad2.right_bumper) {
                linearSlide.open_close_outtake(0.925, 0.75);
                telemetry.addData("CloawOutakeClaw", "testing servo CLOSE");
                telemetry.update();
            }

            idle();
        }

        // Signal done;

        telemetry.addData(">", "Done");
        telemetry.update();
    }
}