package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
//import org.firstinspires.ftc.teamcode.robot.compClaw;
//import org.firstinspires.ftc.teamcode.robot.compDrive;
//import org.firstinspires.ftc.teamcode.robot.compLinearSlide;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Encoder Test", group = "Linear OpMode")
public class RawEncoderTesting extends LinearOpMode{
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    public void runOpMode() {

        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //opposite to move forward
        backRightDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move forward

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftDrive.setTargetPosition(0);
        backLeftDrive.setTargetPosition(0);
        frontRightDrive.setTargetPosition(0);
        backRightDrive.setTargetPosition(0);

        // Wait for the start button
        telemetry.addData("Starting", "...");
        telemetry.update();
        waitForStart();

        // set movement for motors
        frontLeftDrive.setTargetPosition(1000);
        backLeftDrive.setTargetPosition(1000);
        frontRightDrive.setTargetPosition(1000);
        backRightDrive.setTargetPosition(1000);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeftDrive.setPower(0.2);
        backLeftDrive.setPower(0.2);
        frontRightDrive.setPower(0.2);
        backRightDrive.setPower(0.2);

        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() && frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            telemetry.addData("Moving Forward", "Distance: %d ticks", 1000);
            telemetry.update();
        }
//        // Stop the motors and reset the power to 0 and also reset encoder count
        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);

        telemetry.addData("Stopping", "...");
        telemetry.update();

    }
}
