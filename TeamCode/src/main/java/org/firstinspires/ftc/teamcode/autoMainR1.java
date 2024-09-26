package org.firstinspires.ftc.teamcode;

//basic imports like motors and opModes
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


//A lot of imports here. They include camera, compDrive, and array files
//Note: I'm not sure if the imports related to 'Camera' are neccessary anymore (exept for maybe in testing)
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.OpenCV.Camera_Exa;
import org.firstinspires.ftc.teamcode.OpenCV.compCam;
import org.firstinspires.ftc.teamcode.compDrive;
import org.firstinspires.ftc.teamcode.compClaw;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import java.util.ArrayList;

//hi
//sets mode to autonomous and makes the main class
@Autonomous(name = "autoMainR1", group = "Linear OpMode")
public class autoMainR1 extends LinearOpMode {
    //defining variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    OpenCvCamera camera;

    //creates new objects from imported/integrated files
    Camera_Exa tagID = new Camera_Exa(hardwareMap);
    compDrive drive1 = new compDrive(hardwareMap);
    compClaw claw = new compClaw(hardwareMap);

    @Override
    //This runs when the program is activated
    public void runOpMode() {


        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);


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




        //The actual auto code
        //this repeats the whole time while the program is running
        while (opModeIsActive()) {
            //This scans for April Tags
            tagID.scan();
            //'tagToId' gets the id of the april tag that it scanned earlier
            //'if tag is not found, tell the driver station'
            if (true){
                grab();
            }
            else if (tagID.tagToId() == 0){
                telemetry.addLine("Tag not found...");
                telemetry.update();
                sleep(500);
            }
            //otherwise, if the tag is equal to what we want, run this code
            else if (tagID.tagToId() == 4) {
                telemetry.addLine("Tag of interest is found!" + tagID.tagToId());
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
    public void grab(){
        claw.moveArm(-0.25, 200);
        claw.clawGrab();
        claw.moveArm(0.25, 200);
        claw.clawRelease();
    }
}
