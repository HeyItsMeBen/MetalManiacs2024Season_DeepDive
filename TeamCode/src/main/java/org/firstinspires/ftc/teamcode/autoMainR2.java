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

    double maxHeight = 30;
    double armleftServoWideOpen = 0.65;
    double armrightServoWideOpen = 0.725;

    double armleftServoNarrowOpen = 0.6;
    double armrightServoNarrowOpen = 0.75;

    double armleftServoClose = 0.565;
    double armrightServoClose = 0.815;

    double LinearSlidePower = 0.5;
    double armPower = -0.25;

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
        slides.open_close_outtake(0.925, 0.725); //close
        drive1.moveForward(tileLength, dblPower);
        drive1.moveLeft(tileLength*0.5, dblPower);

        //This is the turning/extension part
        drive1.moveClockwiseTurn(halfCircle, dblPower);
        slides.extendVerticalUsingEncoder(LinearSlidePower, maxHeight, "up");
        drive1.moveBackward(tileLength*0.355, dblPower);

        sleep(500);

        //scores sample onto bar
        slides.extendVerticalUsingEncoder(LinearSlidePower, 5, "down"); //Descend
        slides.open_close_outtake(1.0, 0.625);            //opens
        slides.extendVerticalUsingEncoder(LinearSlidePower, 25, "down"); //Return to original position

        sleep(500);

        //move back to 'critical point' (the start position for scoring each sample)
        drive1.moveCounterClockwiseTurn(halfCircle, dblPower);
        drive1.moveBackward(tileLength*0.7, dblPower);
        drive1.moveRight(tileLength*2 + tileLength*0.5, dblPower);

        sleep(500);

        //moves in to grab outermost sample
        claw.open_close(armleftServoClose, armrightServoClose);  //close to get it past the linear slides
        claw.moveArm(armPower); //deploy arm out
        sleep(1000);
        claw.moveArm(0);
        claw.open_close(armleftServoWideOpen, armrightServoWideOpen); //opens wider to grab sample
        sleep(500);
        drive1.moveForward(tileLength*0.3, dblPower);
        claw.open_close(armleftServoClose, armrightServoClose); //closes on the sample
        claw.moveArm(-armPower); //intake arm
        sleep(1000);
        claw.moveArm(0);

        sleep(500);

        //Now the robot will spin around and drop the sample in the observation station to convert it to a sample
        drive1.moveBackward(tileLength*0.3, dblPower);
        drive1.moveClockwiseTurn(halfCircle, dblPower);
        claw.moveArm(armPower); //deploy claw
        sleep(500);
        claw.moveArm(0);
        claw.open_close(armleftServoNarrowOpen, armrightServoNarrowOpen); //open claw to release sample
        claw.moveArm(-armPower); //move arm back in place
        sleep(800);
        claw.moveArm(0);
        sleep(500);

        //Turn robot back around
        drive1.moveCounterClockwiseTurn(halfCircle*0.8, dblPower);


        }
    }

