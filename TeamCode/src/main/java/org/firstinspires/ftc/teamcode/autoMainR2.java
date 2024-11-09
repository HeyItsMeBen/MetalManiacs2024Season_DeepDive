package org.firstinspires.ftc.teamcode;

//basic imports like motors and opModes
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
    double[] dblPower={0.25, 0.25, 0.25, 0.25};

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

        //deliver preloaded specimen
        //move to bar
        telemetry.addLine("Moving to basket...");
        telemetry.update();
        slides.open_close_outtake(0.925, 0.725); //close
        drive1.moveForward(tileLength*0.5, dblPower);

        //This is the turning/extension part
        drive1.moveClockwiseTurn(halfCircle, dblPower);
        slides.extendVertical(-0.5);
        sleep(2500);
        slides.extendVertical(0);
        drive1.moveBackward(tileLength, dblPower);

        sleep(5000);

        //scores sample onto bar
        telemetry.addLine("Scoring specimen...");
        telemetry.update();
        slides.extendVertical(0.5);
        sleep(500);
        slides.open_close_outtake(1.0, 0.625);            //opens
        sleep(1000);

        //move back to 'critical point' (the start position for scoring each sample)
        telemetry.addLine("Moving to 'critical point'...");
        telemetry.update();
        drive1.moveCounterClockwiseTurn(halfCircle, dblPower);
        sleep(1000);
        drive1.moveBackward(tileLength*0.5, dblPower);
        sleep(1000);
        drive1.moveRight(tileLength*3.33333-4, dblPower);
        sleep(1000);

        //Grabs outermost sample
        telemetry.addLine("Grabbing sample...");
        telemetry.update();
        claw.open_close(0.6,0.75);  //opens
        sleep(500);
        claw.moveArm(-0.25);
        sleep(500);
        claw.open_close(0.55, 0.8); //close
        sleep(500);
        claw.moveArm(0.25);
        sleep(500);
        claw.open_close(0.6, 0.75); //opens
        sleep(1000);
        claw.moveArm(0);

        sleep(5000);


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
                drive1.moveClockwiseTurn(halfCircle * 0.125, dblPower);
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
                drive1.moveCounterClockwiseTurn(halfCircle * 0.125, dblPower);
                sleep(1000);
            }

            //park
            drive1.moveRight(tileLength*5*1.33333, dblPower);
            sleep(1000);
            drive1.moveBackward(tileLength*0.5, dblPower);
            sleep(1000);
        }
    }

