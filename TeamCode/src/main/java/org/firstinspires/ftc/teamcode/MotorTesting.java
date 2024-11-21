package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.MotorAlertLevelParameters;


@TeleOp (name="Limit Testing", group="test")
public class MotorTesting extends LinearOpMode {

    // Driver Code: Variables

    //If the arm has been moved upwards into the release area of the intake, it will open narrow. This is to prevent collision with the linear slides
    //If the arm has been moved downwards onto the ground, it will open wide. This way, there is more room to pick the sample up
    @Override
    public void runOpMode() {

        compLinearSlide linearslide = new compLinearSlide(hardwareMap);

        //notify the driver to tell them that the code is ready to be ran
        telemetry.addData("Motor Testing - Test Code", "Testing: Intake");
        telemetry.update();

        waitForStart();

        //executing
        while (opModeIsActive()) {
            if (gamepad1.left_stick_y > 0) {
                telemetry.addData("Left Slide Position: ", linearslide.getLinearSlidePositions("left"));
                telemetry.addData("Right Slide Position: ", linearslide.getLinearSlidePositions("right"));
                telemetry.update();
                if ((linearslide.getLinearSlidePositions("left") > 50 || linearslide.getLinearSlidePositions("right") > 50)) {

                    linearslide.stopLinearSlides();
                    linearslide.extendVerticalUsingEncoder(0.5, 0, "up");

                    telemetry.addData("Too low", "!");
                    telemetry.addData("Left Slide Position: ", linearslide.getLinearSlidePositions("left"));
                    telemetry.addData("Right Slide Position: ", linearslide.getLinearSlidePositions("right"));
                    telemetry.update();
                }
                linearslide.extendVertical(0.5);
            } else if (gamepad1.left_stick_y < 0) {
                linearslide.extendVertical(-0.5);
                telemetry.addData("Left Slide Position: ", linearslide.getLinearSlidePositions("left"));
                telemetry.addData("Right Slide Position: ", linearslide.getLinearSlidePositions("right"));
                telemetry.update();

            }

            if (gamepad1.a) {
                telemetry.addData("Left Motor: ", linearslide.getLinearSlidePositions("Left"));
                telemetry.addData("Right Motor: ", linearslide.getLinearSlidePositions("Right"));
                telemetry.update();
            } else if (gamepad1.b) {
                linearslide.extendVerticalUsingEncoder(0.5, -1, "down");
            } else if (gamepad1.x) {
                linearslide.resetEncoderCount();
            } else if (gamepad1.y) {
                linearslide.extendVerticalUsingEncoder(0.4, 18, "up");
//                    double LeftPosition = linearslide.getEncoderPositions("left");
//                    double RightPosition = linearslide.getEncoderPositions("right");
//                    telemetry.addData("LeftPosition: ", LeftPosition);
//                    telemetry.addData("RightPosition: ", RightPosition);
//                    telemetry.update();
            }
            linearslide.stopLinearSlides();

            idle();
        }
    }
}