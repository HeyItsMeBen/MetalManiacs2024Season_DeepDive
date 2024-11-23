package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "autoMainR2", group = "Linear OpMode")
public class autoMainR2 extends LinearOpMode {
    //defining variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor arm = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;

    public double tileLength=23.75;
    public double halfCircle=12.0208513*3.141592653589798293*1.5;
    double[] dblPower = {0, 0, 0, 0};
    double armleftServoNarrowOpen = 0.6;
    double armrightServoNarrowOpen = 0.75;
    double LinearSlideLeftServoOpen = 0.68;
    double LinearSlideRightServoOpen = 0.63;
    double LinearSlideLeftServoClose = 0.6;
    double LinearSlideRightServoClose = 0.71;
    double LinearSlidePower = 0.5;

    @Override
    //This runs when the program is activated
    public void runOpMode() {
        //creating objects
        compDrive drive1 = new compDrive(hardwareMap);
        compClaw claw = new compClaw(hardwareMap);
        compLinearSlide slides = new compLinearSlide(hardwareMap);

        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        arm = hardwareMap.get(DcMotor.class, "arm");

        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized" );
        telemetry.update();
        waitForStart();
        runtime.reset();

        //start the robot turned 180 degrees

        //deliver preloaded specimen
        claw.open_close(armleftServoNarrowOpen, armrightServoNarrowOpen);
        slides.open_close_outtake(LinearSlideLeftServoClose, LinearSlideRightServoClose); //close

        telemetry.addData("Running Linear Slides now", "");
        telemetry.update();

        slides.resetEncoderCount();

        //This is the extension part
        //slides.extendVerticalUsingEncoder(LinearSlidePower, 25, "up"); //uses encoders

        dblPower[0] = 0.25; //set motor powers to 0.25 for wheels
        dblPower[1] = 0.25;
        dblPower[2] = 0.25;
        dblPower[3] = 0.25;

        //move to bar
        drive1.moveForward(tileLength, dblPower);
        //drive1.moveRight(tileLength*0.5, dblPower);

        //slides.extendVerticalUsingEncoder(0.25, 23, "up"); //test

        //dblPower[0] = 0.125; //slow robot down to approach bar
        //dblPower[1] = 0.125;
        //dblPower[2] = 0.125;
        //dblPower[3] = 0.125;

        //drive1.moveBackward(tileLength*0.35, dblPower);


        //scores sample onto bar
        //slides.extendVerticalUsingEncoder(LinearSlidePower, 21, "down"); //Descend

        //slides.open_close_outtake(LinearSlideLeftServoOpen, LinearSlideRightServoOpen);            //opens
        //slides.extendVerticalUsingEncoder(LinearSlidePower, 0, "down"); //Return to original position

        sleep(250);

        dblPower[0] = 0.3; //robot can go a little faster now
        dblPower[1] = 0.3;
        dblPower[2] = 0.3;
        dblPower[3] = 0.3;

        //prepares to move forward and push samples
        //drive1.moveCounterClockwiseTurn(halfCircle, dblPower); //turns
        drive1.moveRight(tileLength*1.3, dblPower); //moves right

        sleep(100);

        //moves into position behind samples to push them
        drive1.moveForward(tileLength*1.5, dblPower);
        drive1.moveRight(tileLength*0.5, dblPower);
        drive1.moveBackward(tileLength*2.25, dblPower); //pushes samples into observatory

        sleep(250);

        //cycles again
        drive1.moveForward(tileLength*2.35, dblPower);
        drive1.moveRight(tileLength*0.6, dblPower);
        drive1.moveBackward(tileLength*2.45, dblPower);

        dblPower[0] = 0.25; //slows the robot down a bit
        dblPower[1] = 0.25;
        dblPower[2] = 0.25;
        dblPower[3] = 0.25;

        //give human player some time to hook specimen on
        drive1.moveForward(tileLength*0.5, dblPower);

        sleep(2500);

        //turn around and park
        drive1.moveClockwiseTurn(halfCircle, dblPower);
        drive1.moveForward(tileLength*0.35, dblPower);


        }
    }

