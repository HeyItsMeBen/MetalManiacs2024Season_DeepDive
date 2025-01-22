package org.firstinspires.ftc.teamcode.DeepDiveQT_One;

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
import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.MotorAlertLevelParameters;

@Disabled
@TeleOp (name="Encoder Directional Determination", group="test")
public class MotorTesting extends LinearOpMode {

    // Driver Code: Variables

    //If the arm has been moved upwards into the release area of the intake, it will open narrow. This is to prevent collision with the linear slides
    //If the arm has been moved downwards onto the ground, it will open wide. This way, there is more room to pick the sample up
    @Override
    public void runOpMode() {

        compLinearSlide linearslide = new compLinearSlide(hardwareMap);

        telemetry.addData("Left Slide Position (Encoder): ", linearslide.getLinearSlidePositions("left", "encoder"));
        telemetry.addData("Right Slide Position (Encoder): ", linearslide.getLinearSlidePositions("right", "encoder"));
        telemetry.addData(" ", " ");
        telemetry.addData("Left Slide Position (Inches): ", linearslide.getLinearSlidePositions("left", "inches"));
        telemetry.addData("Right Slide Position (Inches): ", linearslide.getLinearSlidePositions("right", "inches"));
        telemetry.update();

        waitForStart();

        //executing
        while (opModeIsActive()) {
            if (gamepad1.left_stick_y > 0) {
                telemetry.addData("Down", "");
                telemetry.update();
                if ((linearslide.getLinearSlidePositions("left", "encoder") < 50 || linearslide.getLinearSlidePositions("right", "encoder") < 50)) {

                    linearslide.stopLinearSlides();
                    linearslide.extendVerticalUsingEncoder(0.5, 0, "up");

                    telemetry.addData("Too low", "!");
                    telemetry.addData("Left Slide Position (Encoder): ", linearslide.getLinearSlidePositions("left", "encoder"));
                    telemetry.addData("Right Slide Position (Encoder): ", linearslide.getLinearSlidePositions("right", "encoder"));
                    telemetry.update();
                }
                linearslide.extendVertical(-0.5);
            } else if (gamepad1.left_stick_y < 0) {
                telemetry.addData("Up", "");
                telemetry.update();
                if ((linearslide.getLinearSlidePositions("left", "inches") > 38 || linearslide.getLinearSlidePositions("right", "inches") > 38)) {

                    linearslide.stopLinearSlides();
                    linearslide.extendVerticalUsingEncoder(0.5, 38, "down");

                    telemetry.addData("Too high", "!");
                    telemetry.addData("Left Slide Position (Encoder): ", linearslide.getLinearSlidePositions("left", "encoder"));
                    telemetry.addData("Right Slide Position (Encoder): ", linearslide.getLinearSlidePositions("right", "encoder"));
                    telemetry.update();

                }
                linearslide.extendVertical(0.5);
            }

            if (gamepad1.a) {
                telemetry.addData("Left Slide Position (Encoder): ", linearslide.getLinearSlidePositions("left", "encoder"));
                telemetry.addData("Right Slide Position (Encoder): ", linearslide.getLinearSlidePositions("right", "encoder"));
                telemetry.addData(" ", " ");
                telemetry.addData("Left Slide Position (Inches): ", linearslide.getLinearSlidePositions("left", "inches"));
                telemetry.addData("Right Slide Position (Inches): ", linearslide.getLinearSlidePositions("right", "inches"));
                telemetry.update();
            } else if (gamepad1.b) {
                linearslide.extendVerticalUsingEncoder(0.4, 0, "down");
            } else if (gamepad1.x) {
                linearslide.resetEncoderCount();
//                    double LeftPosition = linearslide.getEncoderPositions("left");
//                    double RightPosition = linearslide.getEncoderPositions("right");
//                    telemetry.addData("LeftPosition: ", LeftPosition);
//                    telemetry.addData("RightPosition: ", RightPosition);
//                    telemetry.update(
            } else if (gamepad1.dpad_up) {
                linearslide.extendVerticalUsingEncoder(0.4, 5, "up");
            } else if (gamepad1.dpad_down) {
                linearslide.extendVerticalUsingEncoder(0.1, 5, "down");
            }
            linearslide.stopLinearSlides();

            idle();
        }
    }
}
