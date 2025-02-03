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
import org.firstinspires.ftc.teamcode.Hardware.outtakeArm_PIDF;


@Autonomous(name = "Sample Pathing", group = "Linear OpMode")
public final class AutoMainSamplePathing extends LinearOpMode {
    public double LeftStrafeCompensation=0;
    public double MeepMeepCompensation=1.0125;
    //Servo servo=hardwareMap.get(Servo.class, "servo");
    private PIDController armController;
    private PIDController slideController;
    DcMotor arm;
    DcMotor leftSlide;
    DcMotor rightSlide;
    public static double Arm_p = 0.0025, Arm_i = 0.05, Arm_d = 0.0001, Arm_f = 0;
    private static double Slides_p = 0.009, Slides_i = 0, Slides_d = 0.0005, Slides_f = 0;


    private final double ticks_in_degree = 537.7/360;




    @Override
    public void runOpMode() throws InterruptedException {


        arm = hardwareMap.get(DcMotor.class, "arm");
        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");


        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);


        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Pose2d beginPose = new Pose2d(-15, -60, Math.toRadians(0));
        Vector2d scoring_position = new Vector2d((-50) * MeepMeepCompensation, (-50) * MeepMeepCompensation);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Actions.runBlocking(drive.actionBuilder(beginPose)

                //go to scoring position and score initial sample
                .strafeToLinearHeading(scoring_position, Math.toRadians(45))
                .waitSeconds(3) //score initial sample

                //grab and score first sample
                .strafeToLinearHeading(new Vector2d(-48*MeepMeepCompensation, -43*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(2) //grab first sample
                .waitSeconds(2) //transfer to linear slides
                .strafeToLinearHeading(scoring_position, Math.toRadians(45))
                .waitSeconds(3) //score first sample

                //grab and score second sample
                .strafeToLinearHeading(new Vector2d(-57*MeepMeepCompensation, -43*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(2) //grab second sample
                .waitSeconds(2) //transfer to linear slides
                .strafeToLinearHeading(scoring_position, Math.toRadians(45))
                .waitSeconds(3) //score second sample

                //go to achieve first ascent
                .splineToLinearHeading(new Pose2d(-25*MeepMeepCompensation, (-5*MeepMeepCompensation), Math.toRadians(180)), Math.toRadians(0))
                .waitSeconds(1) //achieve first ascent

                .build());

    }


    public class grabSample implements Action {
        Servo intakeClaw;
        Servo highBarPivot;
        DcMotor arm;
        int armPos;
        double armPID;
        double armFF;
        double armpower;


        public grabSample(HardwareMap hMap) {


            intakeClaw=hMap.get(Servo.class, "intakeClawServo");
            highBarPivot = hardwareMap.get(Servo.class, "rightOuttake");
            arm = hMap.get(DcMotor.class, "arm");



        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeClaw.setPosition(0.35);   //open
            highBarPivot.setPosition(0.5);

            setArmTarget(-350, 0.75);   //arm down
            setArmTarget(-400, 1);      //arm down a bit more
            setIntakeClawPosition(0.035);   //close
            setArmTarget(-100, 1);      //arm up
            setArmTarget(0, 1);      //arm up abit more

            // do we need to keep running?
            return false;
        }
    }/*
    public class grabSample implements Action {
        Servo intakeClaw;
        Servo highBarPivot;
        DcMotor arm;
        ElapsedTime timer;
        int armPos;
        double armPID;
        double armFF;
        double armpower;


        public grabSample(HardwareMap hMap) {


            intakeClaw=hMap.get(Servo.class, "intakeClawServo");
            highBarPivot = hardwareMap.get(Servo.class, "rightOuttake");
            arm = hMap.get(DcMotor.class, "arm");


            arm.setDirection(DcMotorSimple.Direction.FORWARD);
            //arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            armController = new PIDController(Arm_p, Arm_i, Arm_d);


        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                intakeClaw.setPosition(0.35);   //open
                highBarPivot.setPosition(0.5);
            }
            if (timer.seconds() >= 2.25) {

                armController.setPID(Arm_p, Arm_i, Arm_d);
                armPos = arm.getCurrentPosition();
                armPID = armController.calculate(armPos, 0);
                armFF = Math.cos(Math.toRadians(0 / ticks_in_degree)) * Arm_f;
                armpower = armPID + armFF;
                arm.setPower(armpower);      //close

            }else if (timer.seconds() >= 1.75) { //pull arm back


                armController.setPID(Arm_p, Arm_i, Arm_d);
                armPos = arm.getCurrentPosition();
                armPID = armController.calculate(armPos, -100);
                armFF = Math.cos(Math.toRadians(-100 / ticks_in_degree)) * Arm_f;
                armpower = armPID + armFF;
                arm.setPower(armpower);


            } else if (timer.seconds() >= 1.25) {


                intakeClaw.setPosition(0.035); //close


            } else if (timer.seconds() >= 0.75){


                armController.setPID(Arm_p, Arm_i, Arm_d);
                armPos = arm.getCurrentPosition();
                armPID = armController.calculate(armPos, -400);
                armFF = Math.cos(Math.toRadians(-400 / ticks_in_degree)) * Arm_f;
                armpower = armPID + armFF;
                arm.setPower(armpower);      //close


            } else if (timer.seconds() >= 0.25) { // bring arm forward


                armController.setPID(Arm_p, Arm_i, Arm_d);
                armPos = arm.getCurrentPosition();
                armPID = armController.calculate(armPos, -350);
                armFF = Math.cos(Math.toRadians(-350 / ticks_in_degree)) * Arm_f;
                armpower = armPID + armFF;
                arm.setPower(armpower);


            }
            // do we need to keep running?
            if (timer.seconds() < 3){
                return true;
            } else{
                return false;
            }
        }
    }*/
    public class retrieveSample implements Action {
        DcMotor leftSlide = null;
        DcMotor rightSlide = null;
        Servo outtakeClawServo;
        Servo slideRightServo;
        Servo intakeClawServo;
        int slidePos;
        double slidePID;
        double slideFF;
        double slidePower ;
        DcMotor arm;
        int armPos;
        double armPID;
        double armFF;
        double armpower;


        ElapsedTime timer;


        public retrieveSample(HardwareMap hMap) {


            leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
            rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
            slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");
            intakeClawServo = hardwareMap.get(Servo.class, "intakeClawServo");
            outtakeClawServo = hardwareMap.get(Servo.class, "outtakeServo");
            arm = hardwareMap.get(DcMotor.class, "arm");
            armController = new PIDController(Arm_p, Arm_i, Arm_d);


            leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
            rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);


//            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
            } else if (timer.seconds() >= 2.5) {
                slideRightServo.setPosition(0); //prepare to drop sample
            } else if (timer.seconds() >= 1.5) {

                slideRightServo.setPosition(0.76); //clearance
                slideController.setPID(Slides_p, Slides_i, Slides_d);
                slidePos = rightSlide.getCurrentPosition();
                slidePID = slideController.calculate(slidePos, -2000);
                slideFF = Math.cos(Math.toRadians(-2000 / ticks_in_degree)) * Slides_f;
                slidePower = slidePID + slideFF;
                leftSlide.setPower(slidePower);
                rightSlide.setPower(slidePower);
//            } else if (timer.seconds() >= 1.0) {
//
//                armController.setPID(Arm_p, Arm_i, Arm_d);
//                armPos = arm.getCurrentPosition();
//                armPID = armController.calculate(armPos, -100);
//                armFF = Math.cos(Math.toRadians(-100 / ticks_in_degree)) * Arm_f;
//                armpower = armPID + armFF;
//                arm.setPower(armpower);
            } else if (timer.seconds() >= 0.5) {
                outtakeClawServo.setPosition(0.035); //close linear slides claw
                intakeClawServo.setPosition(0.35);   //open claw
                //slideRightServo.setPosition(0.76); //clearance
            } else {
                slideRightServo.setPosition(.99); //posed to transfer sample
            }

            // do we need to keep running?
            if (timer.seconds() < 3){
                return true;
            } else{
                return false;
            }


        }
    }
    public class achieveFirstAscent implements Action {

        Servo slideRightServo;
        ElapsedTime timer;
        public achieveFirstAscent(HardwareMap hMap) {


            slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");


        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                slideRightServo.setPosition(0.5); //touch the bar
            }
            // do we need to keep running?
            if (timer.seconds() < 3){
                return true;
            } else{
                return false;
            }
        }
    }

    public class retractSlide implements Action {

        DcMotor leftSlide = null;
        DcMotor rightSlide = null;
        Servo outtakeClawServo;
        Servo slideRightServo;
        Servo intakeClawServo;
        int slidePos;
        double slidePID;
        double slideFF;
        double slidePower ;
        DcMotor arm;
        int armPos;
        double armPID;
        double armFF;
        double armpower;
        ElapsedTime timer;

        public retractSlide(HardwareMap hMap) {


            leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
            rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
            slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");
            intakeClawServo = hardwareMap.get(Servo.class, "intakeClawServo");
            outtakeClawServo = hardwareMap.get(Servo.class, "outtakeServo");
            arm = hardwareMap.get(DcMotor.class, "arm");
            armController = new PIDController(Arm_p, Arm_i, Arm_d);


            leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
            rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);


//            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            slideController = new PIDController(Slides_p, Slides_i, Slides_d);


        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                slideRightServo.setPosition(0); //prepare to drop sample
            } else if (timer.seconds() >= 1.5) {
                slideController.setPID(Slides_p, Slides_i, Slides_d);
                slidePos = rightSlide.getCurrentPosition();
                slidePID = slideController.calculate(slidePos, 0);
                slideFF = Math.cos(Math.toRadians(0 / ticks_in_degree)) * Slides_f;
                slidePower = slidePID + slideFF;
                leftSlide.setPower(slidePower);
                rightSlide.setPower(slidePower);
            } else if (timer.seconds() >= 1) {
                slideRightServo.setPosition(0.5); //pivot over linear slides
            } else {
                outtakeClawServo.setPosition(0.2); //open linear slide claw
            }
            // do we need to keep running?
            if (timer.seconds() < 2.5){
                return true;
            } else{
                return false;
            }
        }
    }

    public void setArmTarget(double target, double seconds){
        DcMotor arm;
        int armPos;
        double armPID;
        double armFF;
        double armpower;
        ElapsedTime timer;
        timer=new ElapsedTime();

        arm = hardwareMap.get(DcMotor.class, "arm");
        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        //arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armController = new PIDController(Arm_p, Arm_i, Arm_d);

        while (timer.seconds()<seconds) {
            armController.setPID(Arm_p, Arm_i, Arm_d);
            armPos = arm.getCurrentPosition();
            armPID = armController.calculate(armPos, target);
            armFF = Math.cos(Math.toRadians(target / ticks_in_degree)) * Arm_f;
            armpower = armPID + armFF;
            arm.setPower(armpower);
        }
    }
    public void setIntakeClawPosition(double position){
        Servo intakeClaw;

        intakeClaw=hardwareMap.get(Servo.class, "intakeClawServo");

        intakeClaw.setPosition(position);

        while (intakeClaw.getPosition()!=position){
            //empty loop. Keeps running until actual position reaches target position
        }
    }
}







//package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode;
//
//import androidx.annotation.NonNull;
//
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.Vector2d;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.arcrobotics.ftclib.controller.PIDController;
//
//
//import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.tuning.TuningOpModes;
//
//import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.MecanumDrive;
//import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.TankDrive;
//import org.firstinspires.ftc.teamcode.Hardware.Arm_PIDF_UsableFromOtherClasses;
//import org.firstinspires.ftc.teamcode.Hardware.compLinearSlide;
////import org.firstinspires.ftc.teamcode.Hardware.outtakeArm_PIDF;
//
//@Autonomous(name = "Sample Pathing", group = "Linear OpMode")
//public final class AutoMainSamplePathing extends LinearOpMode {
//    public double LeftStrafeCompensation=0;
//    public double MeepMeepCompensation=1.0125;
//    //Servo servo=hardwareMap.get(Servo.class, "servo");
//    private PIDController armController;
//    private PIDController slideController;
//    DcMotor arm;
//    DcMotor leftSlide;
//    DcMotor rightSlide;
//    public static double Arm_p = 0.0025, Arm_i = 0.05, Arm_d = 0.0001, Arm_f = 0;
//    private static double Slides_p = 0.009, Slides_i = 0, Slides_d = 0.0005, Slides_f = 0;
//
//    private final double ticks_in_degree = 537.7/360;
//
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//        arm = hardwareMap.get(DcMotor.class, "arm");
//        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
//        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
//
//        arm.setDirection(DcMotorSimple.Direction.FORWARD);
//        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
//        rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);
//
//        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//
//        Pose2d beginPose = new Pose2d(-15, -60, Math.toRadians(0));
//        Vector2d scoring_position = new Vector2d((-23.33 * 2.5 + 16 /2) * MeepMeepCompensation, (-23.33 * 2.5 + 16 / 2) * MeepMeepCompensation);
//
//        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);
//
//        waitForStart();
//        Actions.runBlocking(drive.actionBuilder(beginPose)
//
//                //go to scoring position
//                .strafeTo(scoring_position)
//                .turnTo(Math.toRadians(45))
//
//                //drop preloaded sample
//                .stopAndAdd(new scoreSample(hardwareMap))
//
//                //REMOVE THIS IF NOT WORKING v
//
//                //go to first sample
//                .turnTo(Math.toRadians(90))
//                .strafeTo(new Vector2d((-36-(23.33/2))*MeepMeepCompensation, (-55+(23.33/2))*MeepMeepCompensation))
//                .waitSeconds(0.5)//grab
//                .setReversed(true)
//
//                //grab sample
//                .stopAndAdd(new grabSample(hardwareMap)) //cycles claw
//
//                //return to scoring position
//                .splineTo(scoring_position, Math.toRadians(225))
//                .waitSeconds(0.5)//score
//                .setReversed(false)
//                //.waitSeconds(0.5)
//
//                //drop first sample
//                .stopAndAdd(new scoreSample(hardwareMap))
//
//                //go to second sample
//                .strafeTo(new Vector2d(-56, -56))
//                .turnTo(Math.toRadians(90))
//                .splineTo(new Vector2d(-59, -45), Math.toRadians(90))
//                .waitSeconds(0.5)
//                .setReversed(true)
//
//                //grab second sample
//                .stopAndAdd(new grabSample(hardwareMap))
//
//                //return to scoring position
//                .strafeTo((scoring_position))
//                .waitSeconds(0.5)
//                .setReversed(false)
//                .turnTo(Math.toRadians(45))
//
//                //drop second sample
//                .stopAndAdd(new scoreSample(hardwareMap))
//
//                //REMOVE THIS IF NOT WORKING ^
//
//
//                //go to white triangle area
//                .turnTo(Math.toRadians(225))
//                .setReversed(true)
//                .splineTo(new Vector2d((-25)*MeepMeepCompensation, (-8)*MeepMeepCompensation), Math.toRadians(0))//135
//
//                //bring linear slide arm to bar
//                .stopAndAdd(new moveLinearSlideArm(hardwareMap))
//
//                .build());
//
//    }
//
//    public class grabSample implements Action {
//        Servo intakeClaw;
//        DcMotor arm;
//        ElapsedTime timer;
//        int armPos;
//        double armPID;
//        double armFF;
//        double armpower;
//        double var=0;
//
//        //armDown (towards floor) part1, armDown part2, intakeClaw close, armUp (to transfer)
//        //double[] waitList={0.75, 0.75, 0.5, 2};   //old values
//        double[] waitList={0.75, 0.75, 0.5, 2};     //safer values
//        double waitListSize=4;
//
//        public grabSample(HardwareMap hMap) {
//
//            intakeClaw=hMap.get(Servo.class, "intakeClawServo");
//            arm = hMap.get(DcMotor.class, "arm");
//
//            arm.setDirection(DcMotorSimple.Direction.FORWARD);
//            //arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//            armController = new PIDController(Arm_p, Arm_i, Arm_d);
//
//        }
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            if (timer == null) {
//                timer = new ElapsedTime();
//                intakeClaw.setPosition(0.35);   //open
//            }
//            if (timer.seconds() >= checkValue(4)) { //pull arm back //2
//
//                armController.setPID(Arm_p, Arm_i, Arm_d);
//                armPos = arm.getCurrentPosition();
//                armPID = armController.calculate(armPos, 0);
//                armFF = Math.cos(Math.toRadians(0 / ticks_in_degree)) * Arm_f;
//                armpower = armPID + armFF;
//                arm.setPower(armpower);
//
//            } else if (timer.seconds() >= checkValue(3)) {//1.5
//
//                intakeClaw.setPosition(0.035); //close
//
//            } else if (timer.seconds() >= waitList[0]){    //0.75
//
//                armController.setPID(Arm_p, Arm_i, Arm_d);
//                armPos = arm.getCurrentPosition();
//                armPID = armController.calculate(armPos, -400);
//                armFF = Math.cos(Math.toRadians(-400 / ticks_in_degree)) * Arm_f;
//                armpower = armPID + armFF;
//                arm.setPower(armpower);      //close
//
//            } else { // bring arm forward
//
//                armController.setPID(Arm_p, Arm_i, Arm_d);
//                armPos = arm.getCurrentPosition();
//                armPID = armController.calculate(armPos, -350);
//                armFF = Math.cos(Math.toRadians(-350 / ticks_in_degree)) * Arm_f;
//                armpower = armPID + armFF;
//                arm.setPower(armpower);
//
//            }
//            // do we need to keep running?
//            if (timer.seconds() < checkValue(waitListSize+1)){
//                return true;
//            } else{
//                return false;
//            }
//        }
//        double checkValue(double num){
//            var=0;
//            num-=1;
//            for (int i=0; i<num; i++){
//                var+=waitList[i];
//            }
//            return var;
//        }
//    }
//    public class scoreSample implements Action {
//        DcMotor leftSlide = null;
//        DcMotor rightSlide = null;
//        Servo outtakeClawServo;
//        Servo slideRightServo;
//        Servo intakeClawServo;
//        int slidePos;
//        double slidePID;
//        double slideFF;
//        double slidePower ;
//        DcMotor arm;
//        int armPos;
//        double armPID;
//        double armFF;
//        double armpower;
//        ElapsedTime timer;
//        double var=0;
//
//        /*
//        outtakeArm_transferPosition     1
//        prepForSlides                   2
//        raiseSlides                     3
//        outtakeArm toBasket             4
//        score                           5
//        outtakeArm toVertical           6
//        retract slides                  7
//         */
//        //double[] waitList={0.5, 1, 1, 0.5, 0.5, 0.5, 1};  //old values
//        double[] waitList={1, 2, 1, 1, 0.5, 1, 1};    //safer values
//        double waitListSize=7;
//
//        public scoreSample(HardwareMap hMap) {
//
//            leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
//            rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
//            slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");
//            intakeClawServo = hardwareMap.get(Servo.class, "intakeClawServo");
//            outtakeClawServo = hardwareMap.get(Servo.class, "outtakeServo");
//            arm = hardwareMap.get(DcMotor.class, "arm");
//            armController = new PIDController(Arm_p, Arm_i, Arm_d);
//
//            leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
//            rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);
//
////            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
////            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//            slideController = new PIDController(Slides_p, Slides_i, Slides_d);
//
//        }
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            if (timer == null) {
//                timer = new ElapsedTime();
//                //outtakeClawServo.setPosition(0.2); //open
//            }
//            if (timer.seconds() >= checkValue(7)) {  //4
//
//                slideController.setPID(Slides_p, Slides_i, Slides_d);
//                slidePos = rightSlide.getCurrentPosition();
//                slidePID = slideController.calculate(slidePos, 0);
//                slideFF = Math.cos(Math.toRadians(0 / ticks_in_degree)) * Slides_f;
//                slidePower = slidePID + slideFF;
//                leftSlide.setPower(slidePower);
//                rightSlide.setPower(slidePower);
//
//            } else if (timer.seconds() >= checkValue(6)) {  //3.5
//
//                slideRightServo.setPosition(0.5); //pivot over linear slides
//
//            } else if (timer.seconds() >= checkValue(5)) {  //3
//
//                outtakeClawServo.setPosition(0.2); //open linear slide claw
//
//            } else if (timer.seconds() >= checkValue(4)) {//2.5
//
//                slideRightServo.setPosition(0); //prepare to drop sample
//
//            } else if (timer.seconds() >=checkValue(3)) { //1.5
//
//                slideController.setPID(Slides_p, Slides_i, Slides_d);
//                slidePos = rightSlide.getCurrentPosition();
//                slidePID = slideController.calculate(slidePos, -2000);
//                slideFF = Math.cos(Math.toRadians(-2000 / ticks_in_degree)) * Slides_f;
//                slidePower = slidePID + slideFF;
//                leftSlide.setPower(slidePower);
//                rightSlide.setPower(slidePower);
//
////            } else if (timer.seconds() >= 1.0) {
////
////                armController.setPID(Arm_p, Arm_i, Arm_d);
////                armPos = arm.getCurrentPosition();
////                armPID = armController.calculate(armPos, -100);
////                armFF = Math.cos(Math.toRadians(-100 / ticks_in_degree)) * Arm_f;
////                armpower = armPID + armFF;
////                arm.setPower(armpower);
//
//            } else if (timer.seconds() >= waitList[0]) { //0.5
//
//                outtakeClawServo.setPosition(0.035); //close linear slides claw
//                intakeClawServo.setPosition(0.35);   //open claw
//                slideRightServo.setPosition(0.76); //clearance
//
//            } else {
//
//                slideRightServo.setPosition(.89); //posed to transfer sample
//
//            }
//
//            // do we need to keep running?
//            if (timer.seconds() < checkValue(waitListSize+1)){
//                return true;
//            } else{
//                return false;
//            }
//
//        }
//        double checkValue(double num){
//            var=0;
//            num-=1;
//            for (int i=0; i<num; i++){
//                var+=waitList[i];
//            }
//            return var;
//        }
//    }
//    public class moveLinearSlideArm implements Action {
//        Servo slideRightServo;
//        ElapsedTime timer;
//
//        public moveLinearSlideArm(HardwareMap hMap) {
//
//            slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");
//
//        }
//
//        @Override
//        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            if (timer == null) {
//                timer = new ElapsedTime();
//                slideRightServo.setPosition(0.4); //touch the bar
//            }
//            // do we need to keep running?
//            if (timer.seconds() < 3){
//                return true;
//            } else{
//                return false;
//            }
//        }
//    }
//}
