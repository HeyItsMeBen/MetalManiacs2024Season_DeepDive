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

import org.firstinspires.ftc.teamcode.Hardware.Arm_PIDF_UsableFromOtherClasses;
import org.firstinspires.ftc.teamcode.Hardware.Slides_PID;
import org.firstinspires.ftc.teamcode.Hardware.outtakeArm;


@Autonomous(name = "Sample Pathing", group = "Linear OpMode")
public final class AutoMainSamplePathing extends LinearOpMode {
    double currentTileSize=71.125/3;//divide by 3 cuz 70 is the size for half a field
    double referenceTileSize=70/3;//(reference tile should be MeepMeep)
    double currentBotLength=17;
    double referenceBotLength=17;
    //Notes: 71.125(ourFieldSize), 70.5in(compFieldSize), 70in(MeepMeep)
    double MeepMeepTileCompensation =currentTileSize/referenceTileSize;   //multiply this by every movement of the wheels (besides rotation ofc)
    //double interactionCompensation=(currentTileSize-referenceTileSize)/2+(currentBotLength/2-referenceBotLength/2);    //applicable when bot interacts with something at it's side, rather than at it's center (eg: starting, scoring(usually), grabbing(usually). This happens cuz when field size changes and bot size stays constant, the field size to bot size ratio changes. Ask me for clarification if needed.
    double currentInteractionDistance=(currentTileSize/2)-(currentBotLength/2);
    double referenceInteractionDistance=(referenceTileSize/2)-(referenceBotLength/2);
    double interactionCompensation=currentInteractionDistance-referenceInteractionDistance;

    //double botSizeWhenGrabFromWall=19-(2/16);
    //double botSizeWhenScoreSpecimen=19.86; //placeholder value. Likely NOT ACCURATE
    double botSizeWhenGrabFromWall=17;//19
    double botSizeWhenScoreSpecimen=17; //placeholder value. Likely NOT ACCURATE


    private PIDController armController;
    private PIDController slideController;
    Arm_PIDF_UsableFromOtherClasses arm;
    outtakeArm outtakeArmServos;
    Slides_PID slides;
    DcMotor leftSlide;
    DcMotor rightSlide;
    Servo intakeClaw;
    Servo outtakeClaw;
    public static double Arm_p = 0.005, Arm_i = 0, Arm_d = 0.00075, Arm_f = 0;
    private static double Slides_p = 0.009, Slides_i = 0, Slides_d = 0.0005, Slides_f = 0;

    private final double ticks_in_degree = 537.7/360;

    @Override
    public void runOpMode() throws InterruptedException {

        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        intakeClaw=hardwareMap.get(Servo.class, "intakeClawServo");
        outtakeClaw=hardwareMap.get(Servo.class, "outtakeServo");
        slides = new Slides_PID(hardwareMap);
        outtakeArmServos= new outtakeArm(hardwareMap);
        arm = new Arm_PIDF_UsableFromOtherClasses(hardwareMap);

        /*arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/

        leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);


        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Pose2d beginPose = new Pose2d(-15, -60, Math.toRadians(0));
        Vector2d scoring_position = new Vector2d((-50) * MeepMeepTileCompensation, (-50) * MeepMeepTileCompensation);
        Vector2d slides_up_position = new Vector2d((-48)*MeepMeepTileCompensation, (-48)*MeepMeepTileCompensation);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Actions.runBlocking(drive.actionBuilder(beginPose)

                //score initial (first) sample
                    //.stopAndAdd(new setServos(hardwareMap, 0, 0, outtakeArmServos.grabSample)) //close claws
                .strafeTo(new Vector2d(-20 * MeepMeepTileCompensation, -50 * MeepMeepTileCompensation)) //move out. This way, not hit wall when spin
                .strafeToLinearHeading(slides_up_position, Math.toRadians(45)) //spin and move to open slides
                    .waitSeconds(2) //placeholder for slide action
                .strafeTo(scoring_position)
                    .waitSeconds(3) //score sample

                //grab second sample
                .strafeToLinearHeading(new Vector2d(-46*MeepMeepTileCompensation, -43*MeepMeepTileCompensation), Math.toRadians(90))
                    .waitSeconds(2) //bring linear slides down
                    //.stopAndAdd(new grabSample(hardwareMap))
                    .waitSeconds(2) //grab sample with arm and bring it to linear slides

                //score second sample
                .strafeToLinearHeading(slides_up_position, Math.toRadians(45))
                    .waitSeconds(3) //bring slides up
                .strafeTo(scoring_position)
                    .waitSeconds(3) //score second sample

                //grab and third second sample
                .strafeToLinearHeading(new Vector2d(-57* MeepMeepTileCompensation, -43* MeepMeepTileCompensation), Math.toRadians(90))
                    .waitSeconds(2) //bring linear slides down
                    //.stopAndAdd(new grabSample(hardwareMap))
                    .waitSeconds(2) //grab sample with arm and bring it to linear slides

                //score third sample
                .strafeToLinearHeading(slides_up_position, Math.toRadians(45))
                    .waitSeconds(3) //bring slides up
                .strafeTo(scoring_position)
                    .waitSeconds(3) //score second sample

                //go to achieve first ascent
                .splineToLinearHeading(new Pose2d(-25* MeepMeepTileCompensation, (-5* MeepMeepTileCompensation), Math.toRadians(180)), Math.toRadians(0))
                    .waitSeconds(1) //touch outtake arm to the low bar

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

            setArmTarget(-350-50, 0.75);   //arm down
            setArmTarget(-400-50, 1);      //arm down a bit more
            setIntakeClawPosition(0.035);   //close //set to 0 if it can't grab
            setArmTarget(-100, 1);      //arm up
            setArmTarget(0, 1);      //arm up abit more

            // do we need to keep running?
            return false;
        }
    }

    public class retrieveSample implements Action {
        public retrieveSample(HardwareMap hMap) {

        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            setOuttakeClawPosition(0.35);
            setOuttakeArmPosition(outtakeArmServos.grabSample);
            setOuttakeClawPosition(0.035);
            setIntakeClawPosition(0.35);
            setOuttakeArmPosition(outtakeArmServos.standby);
            setSlidesTarget(-2000, 2);
            setOuttakeArmPosition(outtakeArmServos.scoreSample);
            //y no drop?

            return false;
        }
    }
    public class achieveFirstAscent implements Action { //touch the bar in submersible
        public achieveFirstAscent(HardwareMap hMap) {

        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            setOuttakeArmPosition(0.7);//0.5-->0.7(to be safe. Re-assign new value later)
            return false;
        }
    }

    public class retractSlide implements Action {
        public retractSlide(HardwareMap hMap) {

        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            setOuttakeArmPosition(outtakeArmServos.grabFromWall);//actual value was 0
            setOuttakeClawPosition(0.2);
            setOuttakeArmPosition(outtakeArmServos.standby);
            setSlidesTarget(0, 2);

            return false;
        }
    }
    public class setServos implements Action {
        double intakeClawPos;
        double outtakeClawPos;
        double outtakeArmPos;
        public setServos(HardwareMap hMap, double intakeClawPosition, double outtakeClawPosition, double outtakeArmPosition) {
            intakeClawPos=intakeClawPosition;
            outtakeClawPos=outtakeClawPosition;
            outtakeArmPos=outtakeArmPosition;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeClaw.setPosition(intakeClawPos);
            outtakeClaw.setPosition(outtakeClawPos);
            outtakeArmServos.setArmTarget(outtakeArmPos);
            return false;
        }
    }

    public void setSlidesTarget(double target, double seconds){
        ElapsedTime timer;
        timer=new ElapsedTime();

        while (timer.seconds()<seconds) {
            slides.setSlidesTarget(target);
        }
    }
    public void setOuttakeArmPosition(double position){
        ElapsedTime timer;
        timer=new ElapsedTime();
        double estimatedTime=Math.abs((outtakeArmServos.getArmPosition()-position))*0.8;//intake claw took 0.35 seconds (roughly) to open all the way (by all the way i mean from 0 to 0.5). So, i rounded up to 0.4 and divided by half becuase we are not using full range of motion
        outtakeArmServos.setArmTarget(position);
        while (timer.seconds()<estimatedTime){
            //empty loop. Keeps running until actual position reaches target position
        }
    }
    public void setOuttakeClawPosition(double position){
        ElapsedTime timer;
        timer=new ElapsedTime();
        double estimatedTime=Math.abs((outtakeClaw.getPosition()-position))*0.8;//intake claw took 0.35 seconds (roughly) to open all the way (by all the way i mean from 0 to 0.5). So, i rounded up to 0.4 and multiplied by 2 becuase we are not using full range of motion
        outtakeClaw.setPosition(position);
        while (timer.seconds()<estimatedTime){
            //empty loop. Keeps running until actual position reaches target position
        }
    }
    public void setArmTarget(double target, double seconds){
        ElapsedTime timer;
        timer=new ElapsedTime();

        while (timer.seconds()<seconds) {
            arm.setArmTarget(target);
        }
    }
    public void setIntakeClawPosition(double position){
        ElapsedTime timer;
        timer=new ElapsedTime();
        double estimatedTime=Math.abs((intakeClaw.getPosition()-position))*0.8;//intake claw took 0.35 seconds (roughly) to open all the way (by all the way i mean from 0 to 0.5). So, i rounded up to 0.4 and divided by half becuase we are not using full range of motion
        intakeClaw.setPosition(position);
        while (timer.seconds()<estimatedTime){
            //empty loop. Keeps running until actual position reaches target position
        }
    }
    double getNewInteractionvalue(double newRefBotLength){
        double newCurrentBotLength=newRefBotLength*(currentBotLength/referenceBotLength);
        double newCurrentInteractionDistance=(currentTileSize/2)-(newCurrentBotLength-currentBotLength/2); //calculates distance from the center of the bot to the edge of the claw(or whatever “interact
        double newReferenceInteractionDistance=(referenceTileSize/2)-(newRefBotLength-referenceBotLength/2);
        double newInteractionCompensation=newCurrentInteractionDistance-newReferenceInteractionDistance;
        return newInteractionCompensation;
    }
}

