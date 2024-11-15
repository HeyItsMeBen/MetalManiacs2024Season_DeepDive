package org.firstinspires.ftc.teamcode;
/*To Do (Nov 5)
1. make sure individual parts work (specifically slides)
    1. CompDrive: accurate?
    2. Claw/arm: working?
    3. Slides/servos: working/functional?
    4. Check hardware AND software for errors
2. Combine em in a path
    1. grab sample
    2. transfer sample
    3. outtake sample
3. Soft
    1. Convos
        1. occasional off-topic stuff. Eg: how u doing? hw?
        2. Mostly focus and be productive/efficient
    2. 'Start Caring'
 */
/*
Goals:
1. add tape behind fuse box to mark specific start position
2. y not accurate? battery? encoders? natural error? fixable by sensors?

 */
/*Notes from Nov 13
For second sample, claw didint' make it all the way to the ground
Fro second sample, claw didn't release after delivering sample to transfer
Second sample: Left wheel 6.5 inches behind line, right wheel 4.75 inches behind
 */

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
import org.firstinspires.ftc.teamcode.OpenCV.compCam;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

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
    private Servo ServoSpecimanDeployL;
    private Servo ServoSpecimanDeployR;


    public int testMode;
    public double tileLength=23.75;
    public double fullCircle=12.0208513*3.14159265358979323*1.5*2; //51.8362788*1.16666667*2-->
    public double DiagonalToSide=1/(Math.sqrt(2));    //This is sorta like a conversion unit. If I want to decrease my diagonal(used by wheels before scoring into basket) by 3 inches for example, then I would subtract a sideways movement and a downwards movement by 3*DiagonalToSide. This is just for my testing. Will prob fuse with the other values later.
    //public double DiagonalToSide=0;
    double[] dblPower={0.25, 0.25, 0.25, 0.25};



    @Override
    //This runs when the program is activated
    public void runOpMode() {
        //creating objects
        compDrive drive1 = new compDrive(hardwareMap);
        compClaw claw = new compClaw(hardwareMap);
        compLinearSlide slides = new compLinearSlide(hardwareMap);
        compCam camera1 = new compCam(hardwareMap);
        OpenCvCamera camera;

        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        arm = hardwareMap.get(DcMotor.class, "arm");
        leftClaw = hardwareMap.get(Servo.class, "armLeftServo");
        rightClaw = hardwareMap.get(Servo.class, "armRightServo");

        ServoSpecimanDeployL = hardwareMap.get(Servo.class, "leftOuttake");
        ServoSpecimanDeployR = hardwareMap.get(Servo.class, "rightOuttake");

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
        testMode=1;

        if (testMode==2) {
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
            //this part moves the robot to critical point. The specimen code above also does this, so the two lines below are a temporary replacement of the specimen code
            drive1.moveForward(5, dblPower);
            //sleep(1000);
            drive1.moveLeft((tileLength - 11) * 1.33333333, dblPower);
            //sleep(1000);
            //this  should loop thrice (scores to basket)



            //sample 1
            //moves to sample
            telemetry.addLine("Moving to sample...");
            telemetry.update();

            claw.moveArm(-0.25);
            sleep(1000);
            claw.moveArm(0);
            claw.open_close(0.7, 0.65);  //opens. Prepares to "plow"
            sleep(3000);

            drive1.moveForward(tileLength * 0.5+3.5, dblPower); //-6-->-1
            //sleep(1000);

            //grab sample and transfer it
            telemetry.addLine("Grabbing sample...");
            telemetry.update();
            claw.open_close(0.5415, 0.8135); //closes(grabs) //to open left one: increase //to open Right one: decrease
            sleep(1000);
            claw.moveArm(0.4);
            sleep(1000);
            claw.moveArm(0);
            sleep(200);
            claw.open_close(0.6, 0.75); //opens(releases)
            sleep(1000);

            //move to basket
            telemetry.addLine("Moving to basket...");
            telemetry.update();
            drive1.moveLeft((8-2.486-DiagonalToSide) * 1.33333, dblPower); //10.5* 1.33333-->8 * 1.33333-->(added sqrt(2) stuff)
            //sleep(1000);

            slides.open_close_outtake(0.58, 0.74);  //closes
            sleep(1000);
            /*slides.extendVertical(-0.75);
            sleep(1500);    //500-->1800
            slides.extendVertical(0);*/
            //slides.extendVerticalUsingEncoder(0.6, 12, "UP"); //19-->12(to be safe. Can change after testing)
            sleep(1000);

            drive1.moveBackward(tileLength * 0.5 +1-2.486-DiagonalToSide, dblPower);    //10.625-->tileLength*0.5+2.5-->tileLength * 0.5 -2-->(added sqrt(2) stuff)
            //sleep(1000);
            drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT
            drive1.moveBackward(3.9*Math.sqrt(2), dblPower);

            //score sample into basket
            telemetry.addLine("Scoring sample...");
            telemetry.update();
            slides.open_close_outtake(0.68, 0.63);  //opens
            sleep(1000);

            //move back to critical point
            telemetry.addLine("Moving to start...");
            telemetry.update();
            drive1.moveForward(3.9*Math.sqrt(2), dblPower);
            drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT



            //sample 2
            //moves to sample
            telemetry.addLine("Moving to sample...");
            telemetry.update();
            drive1.moveLeft((2+2.486+DiagonalToSide)*1.33333, dblPower);      //-->(added sqrt(2) stuff)    2.5-->2
            claw.moveArm(-0.25);
            sleep(1000);
            claw.moveArm(0);
            claw.open_close(0.7, 0.65);  //opens
            sleep(5000);
            drive1.moveForward(tileLength * 0.5 +1 -2.486-DiagonalToSide, dblPower); //-6-->-1-->+2.5-->-1-->(added sqrt(2) stuff)
            //sleep(1000);

            /*slides.extendVertical(0.75);
            sleep(1000);
            slides.extendVertical(0);*/
            //slides.extendVerticalUsingEncoder(0.6, 12, "DOWN");   //19-->12(to be safe. Can change after testing)
            //slides.extendVerticalUsingEncoder(0.4, 34, "DOWN");
            sleep(1000);

            //grab sample and transfer it
            telemetry.addLine("Grabbing sample...");
            telemetry.update();
            claw.open_close(0.55, 0.8); //closes(grabs)
            sleep(1000);
            claw.moveArm(0.4);
            sleep(1000);
            claw.moveArm(0);
            claw.open_close(0.5415, 0.8135); //opens
            sleep(1000);

            //move to basket
            telemetry.addLine("Moving to basket...");
            telemetry.update();
            slides.open_close_outtake(0.58, 0.74);
            sleep(1000);
            //slides.extendVerticalUsingEncoder(0.6, 12, "UP");   //19-->12(to be safe. Can change after testing)
            drive1.moveBackward(tileLength * 0.5 +1-2.486-DiagonalToSide, dblPower);    //10.625-->tileLength*0.5+2.5-->tileLength * 0.5 -2-->(added sqrt(2) stuff)
            drive1.moveRight((2+2.486+DiagonalToSide)*1.33333, dblPower); //-->(added sqrt(2) stuff). 2.5-->2
            drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT
            drive1.moveBackward(3.9*Math.sqrt(2), dblPower);

            //score sample into basket
            telemetry.addLine("Scoring sample...");
            telemetry.update();
            slides.open_close_outtake(0.68, 0.63);
            sleep(1000);

            //move back to critical point
            telemetry.addLine("Moving to start...");
            telemetry.update();
            drive1.moveForward(3.9*Math.sqrt(2), dblPower);
            drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT



            //sample 3
            //moves to sample
            telemetry.addLine("Moving to sample...");
            telemetry.update();
            sleep(10000);
            drive1.moveForward(tileLength * 0.5 - 1, dblPower); //-6-->-1
            sleep(1000);
            //this if statement determines which sample it is currently trying to score. There is another one like it a little later on
            drive1.moveLeft(10, dblPower);
            sleep(1000);

            //grab sample and transfer it
            telemetry.addLine("Grabbing sample...");
            telemetry.update();
            claw.open_close(0.6, 0.75);  //opens
            sleep(1000);
            claw.moveArm(-0.25);
            sleep(5000);
            claw.open_close(0.55, 0.8); //closes(grabs)
            sleep(1000);
            claw.moveArm(0.25);
            sleep(5000);
            claw.open_close(0.6, 0.75); //opens
            sleep(1000);
            claw.moveArm(0);

            //move to basket
            telemetry.addLine("Moving to basket...");
            telemetry.update();
            drive1.moveRight(10, dblPower);
            sleep(1000);
            drive1.moveBackward(tileLength * 0.5 - 1, dblPower);    //10.625-->tileLength*0.5-6
            sleep(1000);
            drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);
            sleep(1000);

            //score sample into basket
            telemetry.addLine("Scoring sample...");
            telemetry.update();
            slides.open_close_outtake(0.925, 0.75);
            sleep(1000);
            /*slides.extendVertical(-0.75);
            sleep(500);
            slides.extendVertical(0);
            */
            slides.open_close_outtake(1.0, 0.625);
            sleep(1000);
            /*
            slides.extendVertical(0.75);
            sleep(500);
            slides.extendVertical(0);
            sleep(1000);*/

            //move back to critical point
            telemetry.addLine("Moving to start...");
            telemetry.update();
            drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);
            sleep(1000);

            //park
            drive1.moveRight(tileLength * 5 * 1.33333, dblPower);
            sleep(1000);
            drive1.moveBackward(tileLength * 0.5, dblPower);
            sleep(1000);
        }

        //this repeats the whole time while the program is running
        while (opModeIsActive()) {
            telemetry.addLine("opMode is active");
            telemetry.update();
            sleep(1000);
            //the code testing april tags is commented out
            if (testMode==1) {
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
            }
        }
    }
}
