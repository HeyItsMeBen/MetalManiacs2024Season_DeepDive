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

    // Driver Code: Variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private CRServo winchServo;
    private DcMotor winchMotor;
    private float POWER_REDUCTION = 2;
    private static double REDUCE_SPEED = 0.8;
    private double theta;
    private double power;
    private double sine;
    private double cosine;
    private double turn;
    private double optimalArmLeftServoNarrowOpen = 0.5825;
    private double optimalArmRightServoNarrowOpen = 0.7625;
    private double optimalArmLeftServoWideOpen = 0.64;
    private double optimalArmRightServoWideOpen = 0.7;
    private double optimalArmLeftServoClose = 0.541; //changed from 0.5415
    private double optimalArmRightServoClose = 0.814; //changed from 0.8135
    private double optimalLinearSlideLeftServoOpen = 0.68;
    private double optimalLinearSlideRightServoOpen = 0.63;
    private double optimalLinearSlideLeftServoClose = 0.6;
    private double optimalLinearSlideRightServoClose = 0.71;
    private double ArmPowerDeploy = -0.55;
    private double ArmPowerIntake = 0.9;
    private double LinearSlidePower = 1;
    private boolean narrowOpen = true; //This is a new variable that serves the purpose to check if the arm servos are to open narrow or wide
    //If the arm has been moved upwards into the release area of the intake, it will open narrow. This is to prevent collision with the linear slides
    //If the arm has been moved downwards onto the ground, it will open wide. This way, there is more room to pick the sample up
    double winchServoPower = .25;
    double winchMotorPower = 1;

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
        //Winch
        winchServo = hardwareMap.get(CRServo.class, "winchServo"); // change display name after we design
        winchMotor = hardwareMap.get(DcMotor.class, "winch"); //placeholder

        // set direction for motors by default
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        winchServo.setDirection(DcMotor.Direction.FORWARD);
        winchMotor.setDirection(DcMotor.Direction.REVERSE);

        winchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();

        //Start Button Pushed
        while (opModeIsActive()) {

            //SET this 2 to use math theta, sine, cosine; otherwise SET to 0.

            // Drive Code
            double max; //variable to define maximum motor values never > 100%

            double x = -gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            turn = -gamepad1.right_stick_x;
            theta = Math.atan2(y, x);
            power = Math.hypot(x, y);
            sine = Math.sin(theta - Math.PI / 4);
            cosine = Math.cos(theta - Math.PI / 4);

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = x + y + turn;
            double rightFrontPower = x - y - turn;
            double leftBackPower = x - y + turn;
            double rightBackPower = x + y - turn;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));
            max = POWER_REDUCTION * max;

            max = Math.max(Math.abs(sine),
                    Math.abs(cosine));

            leftFrontPower = power * cosine / max + turn;
            rightFrontPower = power * sine / max - turn;
            leftBackPower = power * sine / max + turn;
            rightBackPower = power * cosine / max - turn;

            if (gamepad1.dpad_up) {
                REDUCE_SPEED = 0.8;
            } else if (gamepad1.dpad_down) {
                REDUCE_SPEED = 0.4;
            }
            // Send calculated power to wheels
            frontLeftDrive.setPower(leftFrontPower * REDUCE_SPEED);
            frontRightDrive.setPower(rightFrontPower * REDUCE_SPEED);
            backLeftDrive.setPower(leftBackPower * REDUCE_SPEED);
            backRightDrive.setPower(rightBackPower * REDUCE_SPEED);

            //Intake code: Arm & servos
            //Activate by toggling the triggers
            if (gamepad1.left_bumper) { //Open
                if (narrowOpen == true) {
                    claw.open_close(optimalArmLeftServoNarrowOpen, optimalArmRightServoNarrowOpen);
                } else {
                    claw.open_close(optimalArmLeftServoWideOpen, optimalArmRightServoWideOpen);
                }
            } else if (gamepad1.right_bumper) { //Close
                claw.open_close(optimalArmLeftServoClose, optimalArmRightServoClose);
            }
            if (gamepad1.left_trigger > 0) {
                claw.moveArm(ArmPowerDeploy);
                narrowOpen = false;
            } else if (gamepad1.right_trigger > 0) {
                claw.moveArm(ArmPowerIntake);
                narrowOpen = true;
            }
            claw.moveArm(0);

            //Outtake code: Linear Slide & servos
            //To utilize, set the gamepad to start + b
            //Activate by pressing the bumpers
            if (gamepad2.left_bumper) { /*open*/
                linearSlide.open_close_outtake(optimalLinearSlideLeftServoOpen, optimalLinearSlideRightServoOpen);
            } else if (gamepad2.right_bumper) { /*close*/
                linearSlide.open_close_outtake(optimalLinearSlideLeftServoClose, optimalLinearSlideRightServoClose);
            }
            if (gamepad2.right_stick_y > 0) { //Down
                if ((linearSlide.getLinearSlidePositions("left", "encoder") < 50 || linearSlide.getLinearSlidePositions("right", "encoder") < 50)) {

                    linearSlide.stopLinearSlides();
                    linearSlide.extendVerticalUsingEncoder(0.5, 0, "up");

                    telemetry.addData("Too low", "!");
                    telemetry.addData("Left Slide Position (Encoder): ", linearSlide.getLinearSlidePositions("left", "encoder"));
                    telemetry.addData("Right Slide Position (Encoder): ", linearSlide.getLinearSlidePositions("right", "encoder"));
                    telemetry.update();

                }
                linearSlide.extendVertical(-LinearSlidePower*0.6);
            } else if (gamepad2.right_stick_y < 0) { //Up
                linearSlide.extendVertical(LinearSlidePower);
            }
            linearSlide.extendVertical(0);
            // Cycle code for linear slides
            if (gamepad2.dpad_up) {
                linearSlide.extendVerticalUsingEncoder(0.6, 18, "up"); //basket and bar height
            } else if (gamepad2.dpad_down) {
                linearSlide.extendVerticalUsingEncoder(0.3, 0, "down"); // return to original position
            }

            //Winch
            if (gamepad2.y) {
                winchServo.setPower(winchServoPower);
                winchMotor.setPower(winchMotorPower);
            } else if (gamepad2.a) {
                winchServo.setPower(-winchServoPower);
                winchMotor.setPower(-winchMotorPower);
            } else if (gamepad2.x) {
                winchServo.setPower(winchServoPower);
            } else if (gamepad2.b) {
                winchServo.setPower(-winchServoPower);
            }
            if (gamepad2.left_stick_y > 0) {
                winchMotor.setPower(winchMotorPower);
            } else if (gamepad2.left_stick_y < 0) {
                winchMotor.setPower(-winchMotorPower);
            }
            winchServo.setPower(0);
            winchMotor.setPower(0);

            idle();

        }
    }
}
