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
    private double theta;
    private double power;
    private double sine;
    private double cosine;
    private double turn;
<<<<<<< Updated upstream
    private double optimalArmLeftServoNarrowOpen = 0.5825;
    private double optimalArmRightServoNarrowOpen = 0.7625;
    private double optimalArmLeftServoWideOpen = 0.64;
    private double optimalArmRightServoWideOpen = 0.7;
    private double optimalArmLeftServoClose = 0.5415; //changed
    private double optimalArmRightServoClose = 0.8135; //changed
    private double optimalLinearSlideLeftServoOpen = 0.68;
    private double optimalLinearSlideRightServoOpen = 0.63;
    private double optimalLinearSlideLeftServoClose = 0.6;
    private double optimalLinearSlideRightServoClose = 0.71;
    private double ArmPowerDeploy = -0.55;
    private double ArmPowerIntake = 0.9;
    private double LinearSlidePower = 0.65;
    private boolean narrowOpen = true; //This is a new variable that serves the purpose to check if the arm servos are to open narrow or wide
    //If the arm has been moved upwards into the release area of the intake, it will open narrow. This is to prevent collision with the linear slides
    //If the arm has been moved downwards onto the ground, it will open wide. This way, there is more room to pick the sample up

=======
    double optimalArmLeftServoNarrowOpen = 0.5825;
    double optimalArmRightServoNarrowOpen = 0.7625;
    double optimalArmLeftServoWideOpen = 0.7;
    double optimalArmRightServoWideOpen = 0.65;
    double optimalArmLeftServoClose = 0.5415; //changed
    double optimalArmRightServoClose = 0.8135; //changed
    double optimalLinearSlideLeftServoOpen = 0.68;
    double optimalLinearSlideRightServoOpen = 0.63;
    double optimalLinearSlideLeftServoClose = 0.58;
    double optimalLinearSlideRightServoClose = 0.74;
    double ArmPowerDeploy = -0.55;
    double ArmPowerIntake = 0.7;
    double LinearSlidePower = 0.65;
    boolean narrowOpen = true; //This is a new variable that serves the purpose to check if the arm servos are to open narrow or wide
    double winchServoPower = .25;
    double winchMotorPower = 0.5;

    //If the arm has been moved upwards into the release area of the intake, it will open narrow. This is to prevent collision with the linear slides
    //If the arm has been moved downwards onto the ground, it will open wide. This way, there is more room to pick the sample up
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        winchMotor = hardwareMap.get(DcMotor.class, "winch"); //placeholder
=======
        winchMotor = hardwareMap.get(DcMotor.class, "winch");
>>>>>>> Stashed changes
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

            //SET this 2 to use math theta, sine, cosine; otherwise SET to 0.


            // Drive Code
            double max; //variable to define maximum motor values never > 100%

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = -gamepad1.left_stick_x;
            double yaw = -gamepad1.right_stick_x;

            double x = -gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            turn = -gamepad1.right_stick_x;
            theta = Math.atan2(y, x);
            power = Math.hypot(x, y);
            sine = Math.sin(theta - Math.PI / 4);
            cosine = Math.cos(theta - Math.PI / 4);

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;


            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));
            max = POWER_REDUCTION * max; //Reduces power to slow down robot. This can be modified to increase or reduce robot speed by will.
            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            max = Math.max(Math.abs(sine),
                    Math.abs(cosine));

            leftFrontPower = power * cosine / max + turn;
            rightFrontPower = power * sine / max - turn;
            leftBackPower = power * sine / max + turn;
            rightBackPower = power * cosine / max - turn;

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

            //Winch
            if (gamepad2.dpad_up) {
                winchServo.setPower(winchServoPower);
            } else if (gamepad2.dpad_down) {
                winchServo.setPower(-winchServoPower);
            }
            if (gamepad2.left_stick_y > 0) {
                winchMotor.setPower(winchMotorPower);
            } else if (gamepad2.left_stick_y < 0) {
                winchMotor.setPower(-winchMotorPower);
            }
            winchServo.setPower(0);

            //Intake code: Arm
            //Activate by toggling the triggers
            if (gamepad1.left_trigger > 0) {
                claw.moveArm(ArmPowerDeploy);
                narrowOpen = false;
            }
            if (gamepad1.right_trigger > 0) {
                claw.moveArm(ArmPowerIntake);
                narrowOpen = true;
            }
            claw.moveArm(0);

            //Intake code: Servos
            //Activate by toggling the triggers
            if (gamepad1.left_bumper) { //Open
                if (narrowOpen == true) {
                    claw.open_close(optimalArmLeftServoNarrowOpen, optimalArmRightServoNarrowOpen);
                } else {
                    claw.open_close(optimalArmLeftServoWideOpen, optimalArmRightServoWideOpen);
                }
            }
            if (gamepad1.right_bumper) { //Close
                claw.open_close(optimalArmLeftServoClose, optimalArmRightServoClose);
            }

<<<<<<< Updated upstream
            //Outtake code: Linear Slide & servos
            //To utilize, set the gamepad to start + b
            //Activate by pressing the bumpers
            if (gamepad2.left_bumper) { /*open*/
                linearSlide.open_close_outtake(optimalLinearSlideLeftServoOpen, optimalLinearSlideRightServoOpen);
            } else if (gamepad2.right_bumper) { /*close*/
                linearSlide.open_close_outtake(optimalLinearSlideLeftServoClose, optimalLinearSlideRightServoClose);
            }
            //Activate by using the up/down right joystick for fine tuning
            if (gamepad2.right_stick_y > 0) {
                linearSlide.extendVertical(LinearSlidePower/2);
            } else if (gamepad2.right_stick_y < 0) {
=======
            //Outtake code: Linear Slides
            //Activate by using the up/down right joystick
            if (gamepad2.right_stick_y > 0) {
                linearSlide.extendVertical(LinearSlidePower);
            }
            //changed it to less then to move slides down :) EV
            if (gamepad2.right_stick_y < 0) {
>>>>>>> Stashed changes
                linearSlide.extendVertical(-LinearSlidePower);
            }
            linearSlide.extendVertical(0);

<<<<<<< Updated upstream
            //Winch
            winchServo.setPower(0);
            if (gamepad2.dpad_up){
                winchServo.setPower(.25); //up
            } else if (gamepad2.dpad_down){
                winchServo.setPower(-.25); //down
=======
            //Outtake code: Servos
            //Activate by pressing the bumpers
            if (gamepad2.left_bumper) { /*open*/
                linearSlide.open_close_outtake(optimalLinearSlideLeftServoOpen, optimalLinearSlideRightServoOpen);
                telemetry.addData("OpenOuttakeClaw", "testing servo OPEN");
                telemetry.update();
>>>>>>> Stashed changes
            }
            if (gamepad2.right_bumper) { /*close*/
                linearSlide.open_close_outtake(optimalLinearSlideLeftServoClose, optimalLinearSlideRightServoClose);
                telemetry.addData("CloseOuttakeClaw", "testing servo CLOSE");
                telemetry.update();
            }

            idle();
        }

        // Signal done;

        telemetry.addData(">", "Done");
        telemetry.update();
    }
}