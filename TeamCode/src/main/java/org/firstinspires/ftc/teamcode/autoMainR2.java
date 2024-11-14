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
    double[] dblPower={0.4, 0.4, 0.4, 0.4};

    double maxHeight = 0;
    double hookheight = 0;
    double armleftServoWideOpen = 0.65;
    double armrightServoWideOpen = 0.725;

    double armleftServoNarrowOpen = 0.6;
    double armrightServoNarrowOpen = 0.75;

    double armleftServoClose = 0.5415;
    double armrightServoClose = 0.8135;

    double LinearSlideLeftServoOpen = 0.68;
    double LinearSlideRightServoOpen = 0.63;

    double LinearSlideLeftServoClose = 0.58;
    double LinearSlideRightServoClose = 0.74;

    double LinearSlidePower = 0;
    double armPower = -0.5;

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

        slides.open_close_outtake(LinearSlideLeftServoClose, LinearSlideRightServoClose); //close

        telemetry.addData("Running Linear Slides now", "");
        telemetry.update();

        //This is the turning/extension part
        slides.extendVerticalUsingEncoder(LinearSlidePower, maxHeight, "up"); //uses encoders

        telemetry.addData("Running Linear Slides now", "");
        telemetry.update();

        //move to bar
        drive1.moveBackward(tileLength*1.6, dblPower);

        //scores sample onto bar
        slides.extendVerticalUsingEncoder(LinearSlidePower, maxHeight-hookheight, "down"); //Descend
        sleep(500);
        slides.open_close_outtake(LinearSlideLeftServoOpen, LinearSlideRightServoOpen);            //opens
        sleep(500);
        slides.extendVerticalUsingEncoder(LinearSlidePower, hookheight, "down"); //Return to original position

        sleep(500);

        //move back to 'critical point' (the start position for scoring each sample)
        drive1.moveForward(tileLength, dblPower);
        drive1.moveCounterClockwiseTurn(halfCircle, dblPower);
        drive1.moveRight(tileLength*2 + tileLength*0.55, dblPower);

        //moves in to grab outermost sample
        claw.open_close(armleftServoClose, armrightServoClose);  //close to get it past the linear slides
        claw.moveArm(armPower); //deploy arm out
        sleep(1000);
        claw.moveArm(0);
        claw.open_close(armleftServoWideOpen, armrightServoWideOpen); //opens wider to grab sample
        sleep(250);
        drive1.moveForward(tileLength*0.3, dblPower);
        claw.open_close(armleftServoClose, armrightServoClose); //closes on the sample
        sleep(250);
        claw.moveArm(-armPower); //intake arm
        sleep(100);
        claw.moveArm(0);

        //Now the robot will spin around and drop the sample in the observation station to convert it to a sample
        drive1.moveBackward(tileLength*0.15, dblPower);
        drive1.moveCounterClockwiseTurn(halfCircle, dblPower);
        claw.moveArm(armPower); //deploy claw
        sleep(500);
        claw.moveArm(0);
        claw.open_close(armleftServoNarrowOpen, armrightServoNarrowOpen); //open claw to release sample
        claw.moveArm(-armPower); //move arm back in place
        sleep(500);
        claw.moveArm(0);

        //Turn robot back around
        drive1.moveCounterClockwiseTurn(halfCircle, dblPower);

        //grab second sample
        drive1.moveBackward(tileLength*0.5, dblPower);
        drive1.moveRight(tileLength*0.6, dblPower);

        claw.moveArm(armPower); //deploy claw
        sleep(300);
        claw.moveArm(0);
        claw.open_close(armleftServoWideOpen, armrightServoWideOpen);
        drive1.moveForward(tileLength*0.6, dblPower);
        claw.open_close(armleftServoClose, armrightServoClose);
        claw.moveArm(-armPower);
        sleep(250);
        claw.moveArm(0);
        drive1.moveCounterClockwiseTurn(halfCircle, dblPower);
        drive1.moveForward(tileLength*0.2, dblPower);
        claw.open_close(armleftServoNarrowOpen, armleftServoNarrowOpen);
        claw.moveArm(-armPower);
        sleep(500);
        drive1.moveForward(tileLength*0.25, dblPower);

        }
    }

