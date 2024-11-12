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
        // set direction for motors by default
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized" );
        telemetry.update();
        waitForStart();
        runtime.reset();

        double optimalArmLeftServoOpen = 0.5825;
        double optimalArmRightServoOpen = 0.7625;


        //double optimalArmLeftServoClose =0.568 ; //changed 0.562
        //double optimalArmRightServoClose =0.835; //changed 0.813
        double optimalArmLeftServoClose = 0.5415; //changed
        double optimalArmRightServoClose = 0.8135; //changed


        double optimalLinearSlideLeftServoOpen = 0.68;
        double optimalLinearSlideRightServoOpen = 0.63;

        double optimalLinearSlideLeftServoClose = 0.58;
        double optimalLinearSlideRightServoClose = 0.74;

        double ArmPowerDeploy = -0.55;
        double ArmPowerIntake = 0.7;

        double LinearSlidePower = 0.65;

        //Start Button Pushed
        while (opModeIsActive()) {

            //SET this 2 to use math theta, sine, cosine; otherwise SET to 0.


            // Drive Code
            double max; //variable to define maximum motor values never > 100%

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   =   gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  -gamepad1.left_stick_x;
            double yaw     =  -gamepad1.right_stick_x;

            double x = -gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            turn = -gamepad1.right_stick_x;
            theta = Math.atan2(y, x);
            power = Math.hypot(x, y);
            sine = Math.sin(theta - Math.PI/4);
            cosine = Math.cos(theta - Math.PI/4);

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


            // Send calculated power to wheels
            frontLeftDrive.setPower(leftFrontPower);
            frontRightDrive.setPower(rightFrontPower);
            backLeftDrive.setPower(leftBackPower);
            backRightDrive.setPower(rightBackPower);

            //Winch
            double winchPower = .25;
            winchServo.setPower(0);
            telemetry.addData("winch", "testing servo  no power");
            telemetry.update();
            if (gamepad2.dpad_up == true){
                winchServo.setPower(.25); //play around
                telemetry.addData("winch", "testing servo up");
                telemetry.update();
            }
            else if (gamepad2.dpad_up == false){
                winchServo.setPower(0); //no power
                telemetry.addData("winch", "testing servo STOP");
                telemetry.update();
            }

            if (gamepad2.dpad_down == true){
                winchServo.setPower(-.25); //unhook
                telemetry.addData("winch", "testing servo down");
                telemetry.update();
            }
            else if (gamepad2.dpad_down == false) {
                winchServo.setPower(0); //no power
                telemetry.addData("winch", "testing servo OFF down");
                telemetry.update();
            }

            //Intake code: Arm
            //To utilize, set the gamepad to start + 1
            //Activate by toggling the triggers
            if (gamepad1.left_trigger > 0){
                claw.moveArm(ArmPowerDeploy);
            }
            if (gamepad1.right_trigger > 0) {
                claw.moveArm(ArmPowerIntake);
            }
            claw.moveArm(0);

            //Intake code: Servos
            //To utilize, set the gamepad to start + a
            //Activate by toggling the triggers
            if (gamepad1.left_bumper){ //Open
                claw.open_close(optimalArmLeftServoOpen,optimalArmRightServoOpen);
            }
            if (gamepad1.right_bumper) { //Close
                claw.open_close(optimalArmLeftServoClose, optimalArmRightServoClose);
            }

            //Outtake code: Linear Slides
            //To utilize, set the gamepad to start + b
            //Activate by using the up/down right joystick
            if (gamepad2.right_stick_y > 0){
                linearSlide.extendVertical(LinearSlidePower);
            }
            //changed it to less then to move slides down :) EV
            if (gamepad2.right_stick_y < 0){
                linearSlide.extendVertical(-LinearSlidePower);
            }
            linearSlide.extendVertical(0);

            //Outtake code: Servos
            //To utilize, set the gamepad to start + b
            //Activate by pressing the bumpers
            if (gamepad2.left_bumper) { /*open*/
                linearSlide.open_close_outtake(optimalLinearSlideLeftServoOpen, optimalLinearSlideRightServoOpen);
                telemetry.addData("OpenOuttakeClaw", "testing servo OPEN");
                telemetry.update();
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