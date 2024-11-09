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
    private Servo Servo2;
    private DcMotor Arm;

    //@Override
    public void runOpMode() {

        //notify the driver to tell them that the code is ready to be ran
        telemetry.addData("Servo Testing", "");
        telemetry.addData("Start+A ", "If running test 1 (non-manual servo position testing)");
        telemetry.addData("Start+B ", "If running test 2 (experimental feature testing directly running servos instead of setting positions)");
        telemetry.update();

        Servo = hardwareMap.get(Servo.class, "armLeftServo");
        Servo2 = hardwareMap.get(Servo.class, "armRightServo");
        Arm = hardwareMap.get(DcMotor.class, "arm");

        double servoPosition = 0.53;
        double servoPosition2 = 0.82;

        waitForStart();

        //executing
        while (opModeIsActive()) {

                telemetry.addData("Gamepad 1", " Set");
                telemetry.addData("Current servo position is: ", servoPosition);
                telemetry.addData("To change servo position by increments of 0.05, press", "gamepad1.left_bumper (the button on top)");
                telemetry.addData("To change servo position by increments of -0.05, press", "gamepad1.right_bumper (the button on top)");
                if (gamepad1.left_bumper) {
                    servoPosition += 0.01;
                    Servo.setPosition(servoPosition);
                    sleep(1000);
                    telemetry.addData("Servo Position increased by 0.01", "Current Position: " + servoPosition);
                } else if (gamepad1.right_bumper) {
                    servoPosition -= 0.01;
                    Servo.setPosition(servoPosition);
                    sleep(1000);
                    telemetry.addData("Servo Position decreased by 0.01", "Current Position: " + servoPosition);
                } else if (gamepad2.left_bumper) {
                    servoPosition2 += 0.01;
                    Servo2.setPosition(servoPosition2);
                    sleep(1000);
                    telemetry.addData("Servo Position increased by 0.01", "Current Position: " + servoPosition2);
                } else if (gamepad2.right_bumper) {
                    servoPosition2 -= 0.01;
                    Servo2.setPosition(servoPosition2);
                    sleep(1000);
                    telemetry.addData("Servo Position decreased by 0.01", "Current Position: " + servoPosition2);
                } else if (gamepad1.right_stick_y > 0 || gamepad2.right_stick_y > 0){
                    Arm.setPower(0.5);
                } else if (gamepad1.right_stick_y < 0 || gamepad2.right_stick_y < 0) {
                    Arm.setPower(-0.5);
                }
                Arm.setPower(0);
                telemetry.update();
            }
        }
    }

