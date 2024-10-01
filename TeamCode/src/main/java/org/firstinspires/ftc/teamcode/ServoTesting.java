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
@Disabled
@TeleOp (name="ServoTesting", group="test")
public class ServoTesting extends LinearOpMode {

    private Servo Servo;
    double increment = 1;

    //@Override
    public void runOpMode() {

        //notify the driver to tell them that the code is ready to be ran
        telemetry.addData("Servo Testing", "Start+A");
        telemetry.update();

        Servo = hardwareMap.get(Servo.class, "Servo");

        waitForStart();

        //executing
        while (opModeIsActive()) {
            telemetry.addData("To push servo forward:","press 'gamepad2.a'");
            while (gamepad2.a) {
                Servo.setPosition(increment);
            }
            telemetry.addData("To pull servo backward:","press 'gamepad2.x");
            while (gamepad2.x) {
                Servo.setPosition(-increment);
            }
            telemetry.update();
        }
    }
}
