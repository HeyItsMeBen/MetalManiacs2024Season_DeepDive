package org.firstinspires.ftc.teamcode;
//servo testing
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

@SuppressWarnings("unused")

@TeleOp (name="ServoTesting", group="test")
public class ServoTesting extends LinearOpMode {

    private Servo Servo;

    //@Override
    public void runOpMode() {

        //notify the driver to tell them that the code is ready to be ran
        telemetry.addData("Servo Testing", "");
        telemetry.addData("Start+A ", "If running test 1 (non-manual servo position testing)");
        telemetry.addData("Start+B ", "If running test 2 (experimental feature testing directly running servos instead of setting positions)");
        telemetry.update();

        Servo = hardwareMap.get(Servo.class, "armRightServo");

        double servoPosition = 1;

        waitForStart();

        //executing
        while (opModeIsActive()) {
            if (gamepad1 != null) {

                telemetry.addData("Gamepad 1", " Set");
                telemetry.addData("Current servo position is: ", servoPosition);
                telemetry.addData("To move servo to position, press", "gamepad1.a");
                telemetry.addData("To change servo position by increments of 0.1, press", "gamepad2.left_bumper (the button on top)");
                telemetry.addData("To change servo position by increments of -0.1, press", "gamepad2.right_bumper (the button on top)");

                if (gamepad1.a) {
                    Servo.setPosition(servoPosition);
                    telemetry.addData("Servo Position set to: ", servoPosition);
                } else if (gamepad1.left_bumper) {
                    servoPosition += 0.05;
                    sleep(1000);
                    telemetry.addData("Servo Position increased by 0.1", "Current Position: " + servoPosition);
                } else if (gamepad1.right_bumper) {
                    servoPosition -= 0.05;
                    sleep(1000);
                    telemetry.addData("Servo Position decreased by 0.1", "Current Position: " + servoPosition);
                }

                telemetry.update();
            }

            else if (gamepad2 != null) {

                telemetry.addData("This is a new experimental feature that will attempt to adjust the servo positions using a toggleable button", "");
                telemetry.addData("To try it, press down on the left_trigger on gamepad1 to change positive, and press down on the right_trigger to change negative", "");
                telemetry.update();


                if (gamepad2.left_trigger > 0) {
                    Servo.setPosition(servoPosition);
                    servoPosition += 0.01;
                    sleep(10);
                }

                if (gamepad2.right_trigger > 0) {
                    Servo.setPosition(servoPosition);
                    servoPosition -= 0.01;
                    sleep(10);
                }
            }
        }
    }
}
