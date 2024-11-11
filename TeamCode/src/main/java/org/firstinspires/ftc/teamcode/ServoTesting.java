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
        telemetry.update();

        Servo = hardwareMap.get(Servo.class, "armLeftServo");
        Servo2 = hardwareMap.get(Servo.class, "armRightServo");
        Arm = hardwareMap.get(DcMotor.class, "arm");

        double servoPosition = 0.53;
        double servoPosition2 = 0.82;

        waitForStart();

        //executing
        while (opModeIsActive()) {

            Servo.setPosition(servoPosition);
            Servo2.setPosition(servoPosition2);

            if (gamepad1 != null) {
                telemetry.addData("Gamepad 1", " Set");
                telemetry.addData("Current left servo position is: ", servoPosition);
                telemetry.addData("To change servo position by increments of 0.01, press", "gamepad1.left_bumper (the button on top)");
                telemetry.addData("To change servo position by increments of -0.01, press", "gamepad1.right_bumper (the button on top)");
                telemetry.update();

            } else if (gamepad2 != null) {
                telemetry.addData("Gamepad 2", " Set");
                telemetry.addData("Current right servo position is: ", servoPosition2);
                telemetry.addData("To change servo position by increments of 0.01, press", "gamepad2.left_bumper (the button on top)");
                telemetry.addData("To change servo position by increments of -0.01, press", "gamepad2.right_bumper (the button on top)");
                telemetry.update();

            }

            if (gamepad1.left_bumper) {
                servoPosition += 0.01;
                Servo.setPosition(servoPosition);
                sleep(1000);
                telemetry.addData("Servo Position increased by 0.01", "Current Position: " + servoPosition);
                telemetry.update();

            } else if (gamepad1.right_bumper) {
                servoPosition -= 0.01;
                Servo.setPosition(servoPosition);
                sleep(1000);
                telemetry.addData("Servo Position decreased by 0.01", "Current Position: " + servoPosition);
                telemetry.update();

            } else if (gamepad2.left_bumper) {
                servoPosition2 += 0.01;
                Servo2.setPosition(servoPosition2);
                sleep(1000);
                telemetry.addData("Servo Position increased by 0.01", "Current Position: " + servoPosition2);
                telemetry.update();

            } else if (gamepad2.right_bumper) {
                servoPosition2 -= 0.01;
                Servo2.setPosition(servoPosition2);
                sleep(1000);
                telemetry.addData("Servo Position decreased by 0.01", "Current Position: " + servoPosition2);
                telemetry.update();

            } else if (gamepad1.right_stick_y > 0 || gamepad2.right_stick_y > 0){
                Arm.setPower(0.5);

            } else if (gamepad1.right_stick_y < 0 || gamepad2.right_stick_y < 0) {
                Arm.setPower(-0.5);
            }

            Arm.setPower(0);
        }
    }
}

