package org.firstinspires.ftc.teamcode;

//basic imports like motors and opModes
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;




//A lot of imports here
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.compDrive;
import org.firstinspires.ftc.teamcode.compClaw;
import org.firstinspires.ftc.teamcode.compLinearSlide;
/*import org.firstinspires.ftc.teamcode.OpenCV.compCam;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;*/

import java.util.ArrayList;

//sets mode to autonomous and makes the main class
@Autonomous(name = "autoMainR1", group = "Linear OpMode")
public class autoMainR1 extends LinearOpMode {
    //defining variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor arm = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;


    public int testMode;
    public double tileLength=23.75;
    public double fullCircle=51.8362788*1.16666667*2;
    double[] dblPower={0.25, 0.25, 0.25, 0.25};



    @Override
    //This runs when the program is activated
    public void runOpMode() {
        //creating objects
        compDrive drive1 = new compDrive(hardwareMap);
        compClaw claw = new compClaw(hardwareMap);
        compLinearSlide slides = new compLinearSlide(hardwareMap);
        //compCam camera1 = new compCam(hardwareMap);
        //OpenCvCamera camera;

        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        arm = hardwareMap.get(DcMotor.class, "arm");
        leftClaw = hardwareMap.get(Servo.class, "leftOuttake");
        rightClaw = hardwareMap.get(Servo.class, "rightOuttake");

        //int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

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

        //sets test mode
        testMode=2;

        if (testMode==2){
            //deliver preloaded specimen. It's currently commented
            //move to bar
            /*telemetry.addLine("Moving to basket...");
            telemetry.update();
            drive1.moveRight(tileLength*2, dblPower);
            sleep(1000);
            slides.extendVertical(-0.75);
            sleep(500);
            slides.extendVertical(0);
            drive1.moveForward(tileLength, dblPower);
            sleep(1000);
            drive1.moveClockwiseTurn(fullCircle*0.5, dblPower);   //0.5-->8*0.44444444-->0.5
            sleep(1000);

            //scores sample onto bar
            telemetry.addLine("Scoring specimen...");
            telemetry.update();
            slides.open_close_outtake(0.925, 0.75);            //closes
            sleep(1000);
            slides.extendVertical(0.75);
            sleep(500);
            slides.extendVertical(0);
            slides.open_close_outtake(1.0, 0.625);            //opens
            sleep(1000);


            //move back to 'critical point' (the start position for scoring each sample)
            telemetry.addLine("Moving to 'critical point'...");
            telemetry.update();
            drive1.moveCounterClockwiseTurn(fullCircle*0.5, dblPower);
            sleep(1000);
            drive1.moveBackward(tileLength*0.5, dblPower);
            sleep(1000);
            drive1.moveLeft(tileLength*3.33333-4, dblPower);
            sleep(1000);*/

            //uses camera as sensor to see if it is in the right position
            /*camera1.scan();
            if (camera1.getTelemetry('x')>0){
                drive1.moveLeft(camera1.getTelemetry('x'), dblPower);
            }
            else if (camera1.getTelemetry('x')<0){
                drive1.moveRight(Math.abs(camera1.getTelemetry('x')), dblPower);
            }
            if (camera1.getTelemetry('y')>0){
                drive1.moveLeft(camera1.getTelemetry('y'), dblPower);
            }
            else if (camera1.getTelemetry('y')<0){
                drive1.moveRight(Math.abs(camera1.getTelemetry('y')), dblPower);
            }*/

            //this  should loop thrice (scores to basket)
            for (int i=0; i<3; i++) {
                //moves to sample
                telemetry.addLine("Moving to sample...");
                telemetry.update();
                drive1.moveForward(tileLength*0.5-6, dblPower);
                sleep(1000);
                //this if statement determines which sample it is currently trying to score. There is another one like it a little later on
                if (i==0){
                    drive1.moveRight(tileLength*0.33333*1.33333, dblPower);
                    sleep(1000);
                }
                else if (i==2) {
                    drive1.moveLeft(tileLength*0.33333*1.33333, dblPower);
                    sleep(1000);
                }
                //grab sample and transfer it
                telemetry.addLine("Grabbing sample...");
                telemetry.update();
                claw.open_close(0.6,0.75);  //opens
                sleep(1000);
                claw.moveArm(-0.25);
                sleep(5000);
                claw.open_close(0.55, 0.8); //closes(grabs)
                sleep(1000);
                claw.moveArm(0.25);
                sleep(1000);
                claw.open_close(0.6, 0.75); //opens
                sleep(1000);
                claw.moveArm(0);

                //move to basket
                telemetry.addLine("Moving to basket...");
                telemetry.update();
                if (i==0){
                    drive1.moveLeft(tileLength*0.33333*1.33333, dblPower);
                    sleep(1000);
                }
                else if (i==2) {
                    drive1.moveRight(tileLength*0.33333*1.33333, dblPower);
                    sleep(1000);
                }
                sleep(1000);
                drive1.moveBackward(tileLength*0.5-6, dblPower);    //10.625-->tileLength*0.5-6
                sleep(1000);
                drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);
                sleep(1000);

                //score sample into basket
                telemetry.addLine("Scoring sample...");
                telemetry.update();
                slides.open_close_outtake(0.925, 0.75);
                sleep(1000);
                slides.extendVertical(-0.75);
                sleep(500);
                slides.extendVertical(0);
                slides.open_close_outtake(1.0, 0.625);
                sleep(1000);
                slides.extendVertical(0.75);
                sleep(500);
                slides.extendVertical(0);
                sleep(1000);

                //move back to critical point
                telemetry.addLine("Moving to start...");
                telemetry.update();
                drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);
                sleep(1000);
            }

            //park
            drive1.moveRight(tileLength*5*1.33333, dblPower);
            sleep(1000);
            drive1.moveBackward(tileLength*0.5, dblPower);
            sleep(1000);
        }

        //this repeats the whole time while the program is running
        while (opModeIsActive()) {
            telemetry.addLine("opMode is active");
            telemetry.update();
            sleep(1000);
            //the code testing april tags is commented out
            /*if (testMode==1) {
                //This scans for April Tags
                camera1.scan();
                //if tag is not found, tell the driver station'
                if (camera1.tagToId() == 0){ //'tagToId' gets the id of the april tag that it scanned earlier
                    telemetry.addLine("Tag not found...");
                    telemetry.update();
                    sleep(500);
                }
                //otherwise, if the tag is equal to what we want, run this code
                else if (camera1.tagToId() == 4) {
                    telemetry.addLine("Tag of interest is found! Tag ID: "+camera1.tagToId());
                    telemetry.addData("Op mode", "is active");
                    telemetry.update();
                    sleep(500);
                    telemetry.addLine("X: "+camera1.getTelemetry('x'));
                    telemetry.addLine("Y: "+camera1.getTelemetry('y'));
                    telemetry.addLine("Y: "+camera1.getTelemetry('z'));
                    telemetry.update();
                    sleep(1500);
                }
            }*/
        }
    }
}
