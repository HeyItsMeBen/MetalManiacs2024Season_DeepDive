package org.firstinspires.ftc.teamcode;

//basic imports like motors and opModes
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



//other imports
import org.firstinspires.ftc.teamcode.compDrive;
import org.firstinspires.ftc.teamcode.compClaw;
import org.firstinspires.ftc.teamcode.compLinearSlide;

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
    public double tileCompensation = 23.8125/23.8125;
    double[] dblPower={0.35, 0.35, 0.35, 0.35};



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
        leftClaw = hardwareMap.get(Servo.class, "armLeftServo");
        rightClaw = hardwareMap.get(Servo.class, "armRightServo");

        ServoSpecimanDeployL = hardwareMap.get(Servo.class, "leftOuttake");
        ServoSpecimanDeployR = hardwareMap.get(Servo.class, "rightOuttake");


        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE); //deletable?
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized" );
        telemetry.update();
        waitForStart();
        runtime.reset();

        //sets test mode
        testMode=0;

        if (testMode==0) {
            //resets encoder counts for later use
            slides.resetEncoderCount();
            claw.resetEncoderCount();

            //Moves the robot to critical point. Sets the robot up to run the rest of the code.
            claw.open_close(0.6, 0.75); //opens(releases)
            drive1.moveForward(5*tileCompensation-sidePlateCompensation, dblPower);
            drive1.moveLeft((tileLength - 11*tileCompensation) * 1.33333333, dblPower);


            //sample 1
            //moves to sample
            telemetry.addLine("Moving to sample...");
            telemetry.update();

            claw.moveArmUsingEncoder(0.454444444,0.3, "down");
            claw.open_close(0.7, 0.65);  //opens. Prepares to "plow"

            drive1.moveForward(tileLength * 0.5+3.5*tileCompensation, dblPower);

            //grab sample and transfer it
            telemetry.addLine("Grabbing sample...");
            telemetry.update();
            claw.open_close(0.5405, 0.8145); //closes(grabs)
            sleep(500);
            claw.moveArmUsingEncoder(0.305555556,0.35, "up");
            claw.moveArmUsingEncoder(0.148888888,0.1,"up");
            claw.open_close(0.6, 0.75); //opens(releases)

            //starts moving towards to basket
            telemetry.addLine("Moving to basket...");
            telemetry.update();
            drive1.moveLeft((8-2.486)*tileCompensation * 1.33333, dblPower);

            //prepares slides to score (grabs sample and brings it up)
            slides.open_close_outtake(0.58, 0.74);  //closes
            slides.extendVerticalUsingEncoder(0.6, 18.5, "up");

            drive1.moveBackward(tileLength * 0.5 +(1-2.486)*tileCompensation, dblPower);

            //Critical point. Each time this point is reached, the robot rotates and moves backwards (to approach baskets perpendicularly)
            drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);
            drive1.moveBackward(3.9*Math.sqrt(2)*tileCompensation, dblPower);

            //score sample into basket
            telemetry.addLine("Scoring sample...");
            telemetry.update();
            slides.open_close_outtake(0.68, 0.63);  //opens
            sleep(500);

            //move back to critical point. Sets things up for sample 2
            telemetry.addLine("Moving to start...");
            telemetry.update();
            drive1.moveForward(3.9*Math.sqrt(2)*tileCompensation, dblPower);
            drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);     //Critical point



            //sample 2
            //moves to sample
            telemetry.addLine("Moving to sample...");
            telemetry.update();
            drive1.moveLeft((1.75+2.486-0.75)*tileCompensation*1.33333, dblPower); //sets the robot up so it can plow

            claw.moveArmUsingEncoder(0.464444444,0.3, "down");
            claw.open_close(0.7, 0.65);  //opens. Prepares to "plow"

            drive1.moveForward(tileLength * 0.5 +(1 -2.486)*tileCompensation, dblPower);

            slides.extendVerticalUsingEncoder(0.5, 0, "down");  //brings slides down for later use


            //grab sample and transfer it
            telemetry.addLine("Grabbing sample...");
            telemetry.update();
            claw.open_close(0.5405, 0.8145);    //closes(grabs)
            sleep(500);
            claw.moveArmUsingEncoder(0.305555556,0.35, "up");
            claw.moveArmUsingEncoder(0.138888888,0.1,"up");
            claw.open_close(0.6, 0.75); //opens(releases)

            //prepares slides to score (grabs sample and brings it up)
            slides.open_close_outtake(0.58, 0.74);  //closes
            slides.extendVerticalUsingEncoder(0.6, 18.5, "up");

            //move to basket
            telemetry.addLine("Moving to basket...");
            telemetry.update();
            drive1.moveBackward(tileLength * 0.5 +(1-2.486)*tileCompensation, dblPower);
            drive1.moveRight((2+2.486)*tileCompensation*1.33333, dblPower);

            drive1.moveClockwiseTurn(fullCircle * 0.125, dblPower);     //critical point
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
            drive1.moveCounterClockwiseTurn(fullCircle * 0.125, dblPower);     //critical point
        }

        //this repeats the whole time while the program is running
        while (opModeIsActive()) {
            telemetry.addLine("opMode is active");
            telemetry.update();
            sleep(1000);
        }
    }
}