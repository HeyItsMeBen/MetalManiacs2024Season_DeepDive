package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

<<<<<<< Updated upstream
import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.MotorAlertLevelParameters;

@SuppressWarnings("unused")

@TeleOp (name="MotorTesting", group="test")
public class MotorTesting extends LinearOpMode {

    private DcMotor Motor;
    private DcMotor Motor2;
=======
@TeleOp(name = "Linear Slide Test", group = "Linear OpMode")
public class MotorTesting extends LinearOpMode {

    // Driver Code: Variables
>>>>>>> Stashed changes

    //If the arm has been moved upwards into the release area of the intake, it will open narrow. This is to prevent collision with the linear slides
    //If the arm has been moved downwards onto the ground, it will open wide. This way, there is more room to pick the sample up
    @Override
    public void runOpMode() {

<<<<<<< Updated upstream
        compLinearSlide linearslide = new compLinearSlide(hardwareMap);

        //notify the driver to tell them that the code is ready to be ran
        telemetry.addData("Motor Testing - Test Code", "Testing: Intake");
        telemetry.update();

        Motor = hardwareMap.get(DcMotor.class, "leftSlide");
        Motor2 = hardwareMap.get(DcMotor.class, "rightSlide");
        Motor.setDirection(DcMotor.Direction.FORWARD);
        Motor2.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        linearslide.resetEncoders();

        //executing
        while (opModeIsActive()) {
            if (gamepad1.left_stick_y > 0) {
                Motor.setPower(10);
                Motor2.setPower(10);
            } else if (gamepad1.left_stick_y < 0) {
                Motor.setPower(-10);
                Motor2.setPower(-10);
                if (Motor.getCurrentPosition() < 0 || Motor2.getCurrentPosition() < 0) {
                    Motor.setPower(0);
                    Motor2.setPower(0);
                }
            }

            if (gamepad1.a) {
                linearslide.extendVerticalUsingEncoder(10, 10, "up");
            } else if (gamepad1.b) {
                linearslide.extendVerticalUsingEncoder(10, 0, "down");
            } else if (gamepad1.x) {
                linearslide.resetEncoders();
            } else if (gamepad1.y) {
                linearslide.extendVerticalUsingEncoder(10, 20, "up");

//                    double LeftPosition = linearslide.getEncoderPositions("left");
//                    double RightPosition = linearslide.getEncoderPositions("right");
//                    telemetry.addData("LeftPosition: ", LeftPosition);
//                    telemetry.addData("RightPosition: ", RightPosition);
//                    telemetry.update();
            }

                Motor.setPower(0);
                Motor2.setPower(0);
            }
=======
        //Declare using Linear Slide object and Claw (Intake Arm and claws) object
        compLinearSlide linearSlide = new compLinearSlide(hardwareMap);

        // Driver Code: Map the 4 motors based off of Driver Station
        DcMotor LeftLinearSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        DcMotor RightLinearSlide = hardwareMap.get(DcMotor.class, "rightSlide"); //One with spring

        // set direction for motors by default
        LeftLinearSlide.setDirection(DcMotor.Direction.REVERSE);
        RightLinearSlide.setDirection(DcMotor.Direction.REVERSE);
        
        LeftLinearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LeftLinearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();

        //Start Button Pushed
        while (opModeIsActive()) {

            if (gamepad1.right_stick_y > 0) {
                RightLinearSlide.setPower(0.75);
            } else if (gamepad1.right_stick_y < 0) {
                RightLinearSlide.setPower(-0.25);
            }

            if (gamepad1.left_stick_y > 0) {
                LeftLinearSlide.setPower(0.75);
            } else if (gamepad1.left_stick_y < 0) {
                LeftLinearSlide.setPower(-0.25);
            }

            //Outtake code: Linear Slides
            //Activate by using the up/down right joystick
            if (gamepad2.right_stick_y > 0) {
                linearSlide.extendVertical(0.75);
            }
            //changed it to less then to move slides down :) EV
            if (gamepad2.right_stick_y < 0) {
                linearSlide.extendVertical(-0.25);
            }
            linearSlide.extendVertical(0);

            idle();
>>>>>>> Stashed changes
        }

        // Signal done;

        telemetry.addData(">", "Done");
        telemetry.update();
    }
<<<<<<< Updated upstream

=======
}
>>>>>>> Stashed changes
