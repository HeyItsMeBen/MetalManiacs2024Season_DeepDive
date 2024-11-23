package org.firstinspires.ftc.teamcode;

/*
Notes:
 voltage was 13.48V when robot worked well(while running) and 13.52 when stopped,
 */
//0.56010417

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
    private Servo ServoSpecimanDeployL;
    private Servo ServoSpecimanDeployR;


    public int testMode;
    public double tileLength=23.75;
    public double fullCircle=12.0208513*3.14159265358979323*1.5*2; //51.8362788*1.16666667*2-->
    public double sidePlateCompensation= 0.56010417;
    public double encoderCompensation = 1.12020834;
    public double tileCompensation = 23.8125/23.8125;
    //public double DiagonalToSide=1/(Math.sqrt(2));    //This is sorta like a conversion unit. If I want to decrease my diagonal(used by wheels before scoring into basket) by 3 inches for example, then I would subtract a sideways movement and a downwards movement by 3*DiagonalToSide. This is just for my testing. Will prob fuse with the other values later.
    public double DiagonalToSide=0;
    double[] dblPower={0.35, 0.35, 0.35, 0.35};



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
        testMode=2;

        if (testMode==3){
            drive1.moveForward(2, dblPower);
            drive1.moveRight(24*tileCompensation, dblPower);
        }
        if (testMode==2) {
            //this part moves the robot to critical point. The specimen code above also does this, so the two lines below are a temporary replacement of the specimen code
            slides.resetEncoderCount();
            claw.resetEncoderCount();

            claw.open_close(0.6, 0.75); //opens(releases)
            drive1.moveForward(5*tileCompensation-sidePlateCompensation, dblPower);
            drive1.moveLeft((tileLength - 11*tileCompensation) * 1.33333333, dblPower);
            //this  should loop thrice (scores to basket)



            //sample 1
            //moves to sample
            telemetry.addLine("Moving to sample...");
            telemetry.update();

            claw.moveArmUsingEncoder(0.454444444,0.3, "down"); //0.5-->0.3
            claw.open_close(0.7, 0.65);  //opens. Prepares to "plow"
            //sleep(3000);

            drive1.moveForward(tileLength * 0.5+3.5*tileCompensation, dblPower); //-6-->-1

            //grab sample and transfer it
            telemetry.addLine("Grabbing sample...");
            telemetry.update();
            claw.open_close(0.5405, 0.8145); //closes(grabs) //to open left one: increase //to open Right one: decrease //0.5415 && 0.8135 --> +0.0005
            sleep(500);
            claw.moveArmUsingEncoder(0.305555556,0.35, "up");   //0.138888888
            claw.moveArmUsingEncoder(0.148888888,0.1,"up");
            //sleep(400);    //+300-->400
            claw.open_close(0.6, 0.75); //opens(releases)
            //sleep(2000);

            //move to basket
            telemetry.addLine("Moving to basket...");
            telemetry.update();
            drive1.moveLeft((8-2.486-DiagonalToSide)*tileCompensation * 1.33333, dblPower); //10.5* 1.33333-->8 * 1.33333-->(added sqrt(2) stuff)
            //sleep(1000);

            slides.open_close_outtake(0.58, 0.74);  //closes
            //sleep(1000);
            slides.extendVerticalUsingEncoder(0.6, 18.5, "up");
            //sleep(1000);

            drive1.moveBackward(tileLength * 0.5 +(1-2.486-DiagonalToSide)*tileCompensation, dblPower);    //10.625-->tileLength*0.5+2.5-->tileLength * 0.5 -2-->(added sqrt(2) stuff)
            //sleep(1000);
            drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT
            drive1.moveBackward(3.9*Math.sqrt(2)*tileCompensation, dblPower);    //-0.5

            //score sample into basket
            telemetry.addLine("Scoring sample...");
            telemetry.update();
            slides.open_close_outtake(0.68, 0.63);  //opens
            sleep(500);

            //move back to critical point
            telemetry.addLine("Moving to start...");
            telemetry.update();
            drive1.moveForward(3.9*Math.sqrt(2)*tileCompensation, dblPower);
            drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT



            //sample 2
            //moves to sample
            telemetry.addLine("Moving to sample...");
            telemetry.update();
            drive1.moveLeft((1.75+2.486+DiagonalToSide-0.75)*tileCompensation*1.33333, dblPower);      //-->(added sqrt(2) stuff)    2.5-->2 //-0.75
            //sleep(1000);
            claw.moveArmUsingEncoder(0.464444444,0.3, "down"); //+
            claw.open_close(0.7, 0.65);  //opens. Prepares to "plow"
            //sleep(3000);

            drive1.moveForward(tileLength * 0.5 +(1 -2.486-DiagonalToSide)*tileCompensation, dblPower); //-6-->-1-->+2.5-->-1-->(added sqrt(2) stuff)
            //sleep(1000);

            slides.extendVerticalUsingEncoder(0.5, 0, "down"); //1-->0
            //slides.extendVerticalUsingEncoder(0.2, 0, "down");
            //sleep(1000);

            //grab sample and transfer it
            telemetry.addLine("Grabbing sample...");
            telemetry.update();
            claw.open_close(0.5405, 0.8145);    //closes(grabs)
            sleep(500);
            claw.moveArmUsingEncoder(0.305555556,0.35, "up");
            claw.moveArmUsingEncoder(0.138888888,0.1,"up");
            sleep(200);
            claw.open_close(0.6, 0.75); //opens(releases)
            //sleep(2000);

            //prepares slides to score (grabs sample and brings it up)
            slides.open_close_outtake(0.58, 0.74);  //closes
            //sleep(1000);
            slides.extendVerticalUsingEncoder(0.6, 18.5, "up");     //MISUS 3 for going down
            //sleep(1000);

            //move to basket
            telemetry.addLine("Moving to basket...");
            telemetry.update();
            drive1.moveBackward(tileLength * 0.5 +(1-2.486-DiagonalToSide)*tileCompensation, dblPower);    //10.625-->tileLength*0.5+2.5-->tileLength * 0.5 -2-->(added sqrt(2) stuff)
            drive1.moveRight((2+2.486+DiagonalToSide)*tileCompensation*1.33333, dblPower); //-->(added sqrt(2) stuff). 2.5-->2

            drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT
            drive1.moveBackward((3.9*Math.sqrt(2)+0.5)*tileCompensation, dblPower);

            //score sample into basket
            telemetry.addLine("Scoring sample...");
            telemetry.update();
            slides.open_close_outtake(0.68, 0.63);
            sleep(1000);

            //move back to critical point
            telemetry.addLine("Moving to start...");
            telemetry.update();
            drive1.moveForward((3.9*Math.sqrt(2)+0.5)*tileCompensation, dblPower);
            drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);     //CRIT POINT

            //park
            /*drive1.moveRight(tileLength * 5 * 1.33333, dblPower);
            sleep(1000);
            drive1.moveBackward(tileLength * 0.5, dblPower);
            sleep(1000);*/
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
