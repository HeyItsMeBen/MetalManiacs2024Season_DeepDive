package org.firstinspires.ftc.teamcode;
//motor testing
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.MotorAlertLevelParameters;

@SuppressWarnings("unused")

@TeleOp (name="MotorTesting", group="test")
public class MotorTesting extends LinearOpMode {

    private DcMotor Motor;
    private DcMotor Motor2;

    //@Override
    public void runOpMode() {

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
        }
    }

