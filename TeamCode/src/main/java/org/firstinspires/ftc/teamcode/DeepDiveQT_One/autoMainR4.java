package org.firstinspires.ftc.teamcode.DeepDiveQT_One;

//basic imports like motors and opModes
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//other imports
import org.firstinspires.ftc.teamcode.Hardware.compDrive;
import org.firstinspires.ftc.teamcode.Hardware.compClaw;
import org.firstinspires.ftc.teamcode.Hardware.compLinearSlide;

import java.util.ArrayList;


//sets mode to autonomous and makes the main class

@Autonomous(name = "Distance Test", group = "Linear OpMode")
public class autoMainR4 extends LinearOpMode {
    //defining variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    int distance_in_inches = 20; //inches
    double inch_traveled_per_tick = 0.022876103;


    @Override
    //This runs when the program is activated
    public void runOpMode() {
        //creating objects
//        compDrive drive1 = new compDrive(hardwareMap);
//        compClaw claw = new compClaw(hardwareMap);
//        compLinearSlide slides = new compLinearSlide(hardwareMap);

        telemetry.addData("Current Target Distance: ", distance_in_inches);

        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

//        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftDrive.setTargetPosition( 874);
        frontRightDrive.setTargetPosition(874);
        backLeftDrive.setTargetPosition(874);
        backRightDrive.setTargetPosition(874);

//        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized" );
        telemetry.update();
        waitForStart();
        runtime.reset();


        //sets test mode
        while (opModeIsActive()){

            frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            frontLeftDrive.setPower(0.5);
            frontRightDrive.setPower(0.5);
            backLeftDrive.setPower(0.5);
            backRightDrive.setPower(0.5);

            sleep(2000);
        }
    }
}
