package org.firstinspires.ftc.teamcode;

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
@TeleOp (name="MotorTesting", group="test")
public class MotorTesting extends LinearOpMode {

    private DcMotor Motor;

    //@Override
    public void runOpMode() {

        //notify the driver to tell them that the code is ready to be ran
        telemetry.addData("Motor Testing - Test Code", "Testing: Intake");
        telemetry.update();

        Motor = hardwareMap.get(DcMotor.class, "Intake");
        Motor.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        //executing
        while (opModeIsActive()) {
            while (gamepad1.left_stick_y > 0) {
                Motor.setPower(10);
            }
            while (gamepad1.left_stick_y < 0) {
                Motor.setPower(-10);
            }
            Motor.setPower(0);
        }
    }
}
