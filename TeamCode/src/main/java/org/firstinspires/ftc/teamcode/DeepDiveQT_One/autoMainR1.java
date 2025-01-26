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

@Autonomous(name = "Straight Line Test", group = "Linear OpMode")
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
    public double tileCompensation = 23.625/23.8125;
    public double tileLength=23.75*tileCompensation;
    public double fullCircle=12.0208513*3.14159265358979323*1.5*2; //51.8362788*1.16666667*2-->
    public double sidePlateCompensation= 0.56010417;
    double[] dblPower={0.35, 0.35, 0.35, 0.35};


    @Override
    //This runs when the program is activated
    public void runOpMode() {
        //creating objects
//        compDrive drive1 = new compDrive(hardwareMap);
//        compClaw claw = new compClaw(hardwareMap);
//        compLinearSlide slides = new compLinearSlide(hardwareMap);

        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
//
//        arm = hardwareMap.get(DcMotor.class, "arm");
//        leftClaw = hardwareMap.get(Servo.class, "armLeftServo");
//        rightClaw = hardwareMap.get(Servo.class, "armRightServo");
//
//        ServoSpecimanDeployL = hardwareMap.get(Servo.class, "leftOuttake");
//        ServoSpecimanDeployR = hardwareMap.get(Servo.class, "rightOuttake");

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
        while (opModeIsActive()){
            frontLeftDrive.setPower(0.5);
            frontRightDrive.setPower(0.5);
            backLeftDrive.setPower(0.5);
            backRightDrive.setPower(0.5);
            sleep(2000);
            frontLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            backRightDrive.setPower(0);
            sleep(1000);
            frontLeftDrive.setPower(-0.5);
            frontRightDrive.setPower(-0.5);
            backLeftDrive.setPower(-0.5);
            backRightDrive.setPower(-0.5);
            sleep(2000);
            frontLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            backRightDrive.setPower(0);
            sleep(1000);
        }
    }
}
