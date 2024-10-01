package org.firstinspires.ftc.teamcode;




//basic imports like motors and opModes
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;








//A lot of imports here. They include camera, compDrive, and array files


//imports related to camera have been commented. They are not needed for this season
/*import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.OpenCV.compCam;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;*/


import org.firstinspires.ftc.teamcode.compDrive;
import org.firstinspires.ftc.teamcode.compClaw;
import org.firstinspires.ftc.teamcode.compLinearSlide;




import java.util.ArrayList;


//sets mode to autonomous and makes the main class
//Note to self: the number is 38.1971863 if u ever lose it again
@Autonomous(name = "autoMainR1", group = "Linear OpMode")
public class autoMainR1 extends LinearOpMode {
    //defining variables
    private ElapsedTime runtime = new ElapsedTime();            //Note to self: why in the world id this here and not in opMode
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor arm = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;
    public int testMode;
    double[] dblPower={0.25, 0.25, 0.25, 0.25};
    //OpenCvCamera camera;              //commented




    //creates new objects from imported/integrated files
    //compCam tagID = new compCam(hardwareMap);             //commented




    @Override
    //This runs when the program is activated
    public void runOpMode() {
        compDrive drive1 = new compDrive(hardwareMap);          //Note to self: maybe this has to be here because it includes hardware maps, which likely have to be in runOpMode()
        compClaw claw = new compClaw(hardwareMap);
        compLinearSlide slides = new compLinearSlide(hardwareMap);
        //createObjects();




        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");




      /*int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
      camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);*/      //commented




        arm = hardwareMap.get(DcMotor.class, "arm"); //ADD this to hardware map IMP
        leftClaw = hardwareMap.get(Servo.class, "leftOuttake");
        rightClaw = hardwareMap.get(Servo.class, "rightOuttake");








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
        //camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);     //delete? may be useful for streaming later








        testMode=1;




        //The actual auto code
        //this repeats the whole time while the program is running
        if (testMode==1){
            telemetry.addLine("testMode1 activated");
            telemetry.update();
            drive1.moveForward(19, dblPower);
            sleep(1000);
            drive1.moveLeft(30, dblPower);
            sleep(1000);
            claw.moveArm(-0.25);
            sleep(2000);
            claw.open_close(1,1);   //open
            sleep(1000);
            claw.moveArm(0.25);
            sleep(3000);
            claw.open_close(0.5,0.5);   //close
            sleep(1000);
              /*drive1.moveForward(122.2222222, dblPower);
              sleep(2000);
              drive1.moveLeft(75, dblPower);
              sleep(2000);*/
            //scoreSample(drive1, claw, slides);
            telemetry.addLine("drive completed!!! integration success!");
            telemetry.update();
            sleep(2000);
            //claw.open_close(1, 1);
        }
        while (opModeIsActive()) {
            telemetry.addLine("so far so good (opMode is active)");
            telemetry.update();
            sleep(1000);
            //This scans for April Tags
            //tagID.scan();
            //'tagToId' gets the id of the april tag that it scanned earlier
            //'if tag is not found, tell the driver station'
            if (testMode==0){
                telemetry.addLine("testMode1 activated");
                telemetry.update();
                drive1.moveForward(15.2*38.197, dblPower);
                sleep(2000);
                drive1.moveLeft(15*38.197, dblPower);
                sleep(2000);
                claw.moveArm(-0.25);
                claw.open_close(0,0);
                sleep(1000);
                claw.moveArm(0.25);
                sleep(1000);
                claw.open_close(1,1);
                sleep(1000);
              /*drive1.moveForward(122.2222222, dblPower);
              sleep(2000);
              drive1.moveLeft(75, dblPower);
              sleep(2000);*/
                //scoreSample(drive1, claw, slides);
                telemetry.addLine("drive completed!!! integration success!");
                telemetry.update();
                sleep(2000);
                //claw.open_close(1, 1);
            }
            else if (testMode==2) {
                if (false) {//tagID.tagToId() == 0){
                    telemetry.addLine("Tag not found...");
                    telemetry.update();
                    sleep(500);
                }
                //otherwise, if the tag is equal to what we want, run this code
                else if (false) {//tagID.tagToId() == 4) {
                    //if (DcMotor.Direction.FORWARD=DcMotor.Direction.FORWARD){}
                    telemetry.addLine("Tag of interest is found!");//+ tagID.tagToId());
                    telemetry.addData("Op mode", "is active");
                    telemetry.update();
                    sleep(500);




                    //sets direction
                    //strafe left
                    frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); //should go inward (REVERSE)
                    frontRightDrive.setDirection(DcMotor.Direction.FORWARD); //go outward (FORWARD)
                    backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //should go inward (FORWARD)
                    backRightDrive.setDirection(DcMotor.Direction.REVERSE); //go outward (REVERSE)








                    //sets how far we want to drive
                    frontLeftDrive.setTargetPosition(50);
                    backLeftDrive.setTargetPosition(50);
                    frontRightDrive.setTargetPosition(50);
                    backRightDrive.setTargetPosition(50);








                    //drives to the set position
                    frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);








                    //sets power and decides how fast we travel towards the set position
                    frontLeftDrive.setPower(0.25);
                    frontRightDrive.setPower(0.25);
                    backLeftDrive.setPower(0.25);
                    backRightDrive.setPower(0.25);








                    //waits a couple seconds before powering the wheels off
                    sleep(200);








                    frontLeftDrive.setPower(0);
                    frontRightDrive.setPower(0);
                    backLeftDrive.setPower(0);
                    backRightDrive.setPower(0);








                }
            }
        }
    }
    /*public void harwareMap1(){ //maybe not void
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
    }
    public void setUp1(){
        frontLeftDrive.setToRunWithEncoder();
        frontLeftDrive.setDirection;
        frontLeftDrive.setPosition;




    }*/
    public void scoreSample(compDrive drive1, compClaw claw, compLinearSlide slides){     //actual do stuff
        //intake
        claw.moveArm(-0.25);
        sleep(200);
        claw.open_close(0, 0);
        claw.moveArm(0.25);
        sleep(200);
        claw.open_close(1, 1);




        //output
        slides.extendVertical(-0.25);
        slides.open_close(0,0);
        slides.extendVertical(0.25);
        slides.open_close(1,1);
        drive1.moveRight(12, dblPower);
    }
}


