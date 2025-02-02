package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.arcrobotics.ftclib.controller.PIDController;


import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.tuning.TuningOpModes;

import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.MecanumDrive;
import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.TankDrive;
import org.firstinspires.ftc.teamcode.Hardware.Arm_PIDF_UsableFromOtherClasses;
import org.firstinspires.ftc.teamcode.Hardware.compLinearSlide;
//import org.firstinspires.ftc.teamcode.Hardware.outtakeArm_PIDF;

@Autonomous(name = "SamplePathing", group = "Linear OpMode")
public final class AutoMainSamplePathing extends LinearOpMode {
    public double LeftStrafeCompensation=0;
    public double MeepMeepCompensation=1.0125;
    //Servo servo=hardwareMap.get(Servo.class, "servo");
    private PIDController armController;
    private PIDController slideController;
    public static double Arm_p = 0.0025, Arm_i = 0.05, Arm_d = 0.0001, Arm_f = 0;
    private static double Slides_p = 0.009, Slides_i = 0, Slides_d = 0.0005, Slides_f = 0;

    private final double ticks_in_degree = 537.7/360;


    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d beginPose = new Pose2d(-15, -60, Math.toRadians(0));
        Vector2d scoring_position = new Vector2d((-23.33 * 2.5 + 20 /2) * MeepMeepCompensation, (-23.33 * 2.5 + 20 / 2) * MeepMeepCompensation);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();
        Actions.runBlocking(drive.actionBuilder(beginPose)

                //go to scoring position
                .strafeTo(scoring_position)
                .turnTo(Math.toRadians(45))

                //drop preloaded sample
                .waitSeconds(1)//score

                //go to first sample
                .splineTo(new Vector2d((-37.5-(23.33/2))*MeepMeepCompensation, (-55+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)
                .waitSeconds(0.5)

                //grab sample
                .stopAndAdd(new grabSample(hardwareMap)) //cycles claw

                //return to scoring position
                .splineTo(scoring_position, Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)
                .waitSeconds(0.5)

                //drop first sample
                .stopAndAdd(new scoreSample(hardwareMap))

                //go to second sample
                .strafeTo(new Vector2d(-56, -56))
                .turnTo(Math.toRadians(90))
                .splineTo(new Vector2d(-56, -45), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)
                .waitSeconds(0.5)//reverse safety

                //grab second sample
                .stopAndAdd(new grabSample(hardwareMap))

                //return to scoring position
                .splineTo(scoring_position, Math.toRadians(0))
                .waitSeconds(1)//score
                .setReversed(false)
                .turnTo(Math.toRadians(45))
                .waitSeconds(0.5)

                //drop second sample
                .stopAndAdd(new scoreSample(hardwareMap))

                //go to white triangle area
                .turnTo(Math.toRadians(225))
                .setReversed(true)
                .splineTo(new Vector2d((-25)*MeepMeepCompensation, (-8)*MeepMeepCompensation), Math.toRadians(0))//135

                //bring linear slide arm to bar
                .stopAndAdd(new moveLinearSlideArm(hardwareMap))

                .build());

    }

    public class grabSample implements Action {
        Servo intakeClaw;
        DcMotor arm;
        ElapsedTime timer;
        int armPos;
        double armPID;
        double armFF;
        double armpower;

        public grabSample(HardwareMap hMap) {

            intakeClaw=hMap.get(Servo.class, "claw");
            arm = hMap.get(DcMotor.class, "arm");

            arm.setDirection(DcMotorSimple.Direction.FORWARD);
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            armController = new PIDController(Arm_p, Arm_i, Arm_d);

        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                intakeClaw.setPosition(0.35);   //open
            }
            if (timer.seconds() >= 2) {
                return true;

            } else if (timer.seconds() >= 1) { //pull arm back

                armController.setPID(Arm_p, Arm_i, Arm_d);
                armPos = arm.getCurrentPosition();
                armPID = armController.calculate(armPos, 0);
                armFF = Math.cos(Math.toRadians(0 / ticks_in_degree)) * Arm_f;
                armpower = armPID + armFF;
                arm.setPower(armpower);

            } else if (timer.seconds() >= 0.5){

                intakeClaw.setPosition(0.025); //close

            } else { // bring arm forward

                armController.setPID(Arm_p, Arm_i, Arm_d);
                armPos = arm.getCurrentPosition();
                armPID = armController.calculate(armPos, -390);
                armFF = Math.cos(Math.toRadians(-390 / ticks_in_degree)) * Arm_f;
                armpower = armPID + armFF;
                arm.setPower(armpower);      //close

            }
            // do we need to keep running?
            if (timer.seconds() < 5){
                return true;
            } else{
                return false;
            }
        }
    }
    public class scoreSample implements Action {
        DcMotor leftSlide = null;
        DcMotor rightSlide = null;
        Servo outtakeClawServo;
        Servo slideRightServo;
        Servo intakeClawServo;
        int slidePos;
        double slidePID;
        double slideFF;
        double slidePower ;

        ElapsedTime timer;

        public scoreSample(HardwareMap hMap) {

            leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
            rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
            slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");
            intakeClawServo = hardwareMap.get(Servo.class, "intakeClawServo");
            outtakeClawServo = hardwareMap.get(Servo.class, "outtakeServo");

            leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
            rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);

            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            slideController = new PIDController(Slides_p, Slides_i, Slides_d);

        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                outtakeClawServo.setPosition(0.2); //open
            }
            if (timer.seconds() >= 3){

                slideRightServo.setPosition(0.75); //transfer perpendicular linear slides
                slideController.setPID(Slides_p, Slides_i, Slides_d);
                slidePos = rightSlide.getCurrentPosition();
                slidePID = slideController.calculate(slidePos, 0);
                slideFF = Math.cos(Math.toRadians(0 / ticks_in_degree)) * Slides_f;
                slidePower = slidePID + slideFF;
                leftSlide.setPower(slidePower);
                rightSlide.setPower(slidePower);

            } else if (timer.seconds() >= 2.25){

                outtakeClawServo.setPosition(0.035); //open linear slide claw

            } else if (timer.seconds() >= 2){

                slideRightServo.setPosition(0.2);

            } else if (timer.seconds() >=1.5){

                slideController.setPID(Slides_p, Slides_i, Slides_d);
                slidePos = rightSlide.getCurrentPosition();
                slidePID = slideController.calculate(slidePos, -3300);
                slideFF = Math.cos(Math.toRadians(-3300 / ticks_in_degree)) * Slides_f;
                slidePower = slidePID + slideFF;
                leftSlide.setPower(slidePower);
                rightSlide.setPower(slidePower);

            } else if (timer.seconds() >= 0.5){

                intakeClawServo.setPosition(0.35);   //open claw

            } else {

                slideRightServo.setPosition(.87); //posed to transfer sample
                outtakeClawServo.setPosition(0.035); //close linear slides claw

            }

            // do we need to keep running?
            if (timer.seconds() < 10){
                return true;
            } else{
                return false;
            }
        }
    }
    public class moveLinearSlideArm implements Action {

        Servo slideRightServo;

        ElapsedTime timer;

        public moveLinearSlideArm(HardwareMap hMap) {

            slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");

        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                slideRightServo.setPosition(0.3); //touch the bar

            }
            // do we need to keep running?
            if (timer.seconds() < 0.5){
                return true;
            } else{
                return false;
            }
        }
    }

}
