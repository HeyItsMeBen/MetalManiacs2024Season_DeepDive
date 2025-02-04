package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode;

import  androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.tuning.TuningOpModes;

import org.firstinspires.ftc.teamcode.Hardware.Slides_PID;
import org.firstinspires.ftc.teamcode.Hardware.outtakeArm;

@Autonomous(name = "SpecimenPathing", group = "Linear OpMode")
public final class AutoMainSpecimenPathing extends LinearOpMode {
    public double LeftStrafeCompensation=0;
    public double MeepMeepCompensation=71.125/70;//70.5(compFieldSize)/70(MeepMeep)
    Servo intakeClaw;
    Servo outtakeClaw;
    Slides_PID slides;
    org.firstinspires.ftc.teamcode.Hardware.outtakeArm outtakeArm;
    //Servo servo=hardwareMap.get(Servo.class, "servo");
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(-34.99500+23.33+23.6+2.0, -61.5, Math.toRadians(270));
        intakeClaw= hardwareMap.get(Servo.class, "intakeClawServo");
        outtakeClaw= hardwareMap.get(Servo.class, "outtakeServo");
        slides = new Slides_PID(hardwareMap);
        outtakeArm = new outtakeArm(hardwareMap);
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

            waitForStart();
            Actions.runBlocking(
                    drive.actionBuilder(beginPose)
                            //go to scoring position
                            .stopAndAdd(new tightenClaws(hardwareMap))
                            .waitSeconds(3) //set timer if you want
                            //hang preloaded sample
                            .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-39) * MeepMeepCompensation))
                            .stopAndAdd(new scoreSpecimenPart1(hardwareMap))
                            //.waitSeconds(4)
                            .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-39+6.75) * MeepMeepCompensation))    //shud be +4    //scores
                            .stopAndAdd(new scoreSpecimenPart2(hardwareMap))                                                //releases
                            .waitSeconds(4)
                            .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-38-8) * MeepMeepCompensation))

                            //.stopAndAdd(new scoreSpecimenPart3(hardwareMap))
                            .waitSeconds(4)

                            .splineTo(new Vector2d((20)*MeepMeepCompensation, (-53)*MeepMeepCompensation), Math.toRadians(Math.PI/2))
                            .splineTo(new Vector2d((35)*MeepMeepCompensation, (-12)*MeepMeepCompensation), Math.toRadians(90))

                            .waitSeconds(0.5)

                            //push first sample
                            .strafeTo(new Vector2d((45) * MeepMeepCompensation, (-12) * MeepMeepCompensation))
                            .strafeTo(new Vector2d((45) * MeepMeepCompensation, (-50) * MeepMeepCompensation))

                            //push second sample
                            .strafeTo(new Vector2d((45) * MeepMeepCompensation, (-12) * MeepMeepCompensation))
                            .strafeTo(new Vector2d((55) * MeepMeepCompensation, (-12) * MeepMeepCompensation))
                            .strafeTo(new Vector2d((55) * MeepMeepCompensation, (-50) * MeepMeepCompensation))

                            .waitSeconds(3)

                            //move forward and park
                            .strafeTo(new Vector2d((55) * MeepMeepCompensation, (-55) * MeepMeepCompensation))

                            .build());
            /*

             */

        } else {
            throw new RuntimeException();
        }
    }
    public class tightenClaws implements Action {
        public tightenClaws(HardwareMap hMap) {

        }
        //@Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            setOuttakeClawPosition(0);         //tighten specimen
            return false;
        }
    }
    public class scoreSpecimenPart1 implements Action {
        public scoreSpecimenPart1(HardwareMap hMap) {

        }
        //@Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            setOuttakeArmPosition(outtakeArm.prepSpecimen);
            setSlidesTarget(-875, 2);
            return false;
        }
    }
    public class scoreSpecimenPart2 implements Action {
        public scoreSpecimenPart2(HardwareMap hMap) {}
        //@Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            setOuttakeClawPosition(0.2);         //release specimen
            return false;
        }
    }
    public class scoreSpecimenPart3 implements Action {
        public scoreSpecimenPart3(HardwareMap hMap) {}
//waitList=[2,2]
        //@Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            setSlidesTarget(0, 2);   //retract slides downward.
            setOuttakeArmPosition(0.99);

            return false;
        }
    }
    /*if (timer.seconds() >= checkValue(3)){
        slides.setSlidesTarget(0);   //retract slides downward.
    }*/
    public class setStandby implements Action {

        public setStandby(HardwareMap hMap) {
            outtakeArm = new outtakeArm(hMap);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            outtakeArm.setArmTarget(outtakeArm.standby);
            return false;
        }
    }
    //WARNING commented method is not finished and is not ready for use. DO NOT USE
    /*public class grabSpecimenFromWall implements Action {
        Servo intakeClaw;
        Servo outtakeClaw;
        outtakeArm_PIDF outtakeArm;
        compLinearSlide slides;
        double position;
        ElapsedTime timer;
        //outtakeArmDown, outtakeClawClose, intakeClawOpen, slidesUp, outtakeArmUp, outtakeClawOpen
        double[] waitList={2,.5,.5,2,2,2};
        //2.5, 3, 5, 7
        double waitListSize=6;
        double var=0;

        public grabSpecimenFromWall(HardwareMap hMap) {
            intakeClaw=hMap.get(Servo.class, "intakeClawServo");
            outtakeClaw=hMap.get(Servo.class, "outtakeServo");   //Real Name? //"outtakeClaw"
            outtakeArm = new outtakeArm_PIDF(hMap);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                outtakeClaw.setPosition(0.2);   //open
            }
            //list of commands. The instructions said to use Elapsed time like this, so i didn't use a sleep function. These commands run from bottom to top (meaning the bottom command runs first). The parameter inputted in the checkValue function is simply which action it's on (Eg, 4th action)

            //empty...

            // do we need to keep running?
            if (timer.seconds() < checkValue(waitListSize+1)){
                return true;
            } else{
                return false;
            }
        }
        double checkValue(double num){
            var=0;
            num-=1;
            for (int i=0; i<num; i++){
                var+=waitList[i];
            }
            return var;
        }
    }*/

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
        double estimatedTime=Math.abs((outtakeArm.getArmPosition()-position))*0.8;//intake claw took 0.35 seconds (roughly) to open all the way (by all the way i mean from 0 to 0.5). So, i rounded up to 0.4 and divided by half becuase we are not using full range of motion
        outtakeArm.setArmTarget(position);
        while (timer.seconds()<estimatedTime){
            //empty loop. Keeps running until actual position reaches target position
        }
    }
    public void setOuttakeClawPosition(double position){
        ElapsedTime timer;
        timer=new ElapsedTime();
        outtakeClaw.setPosition(0.35);
        double estimatedTime=Math.abs((outtakeClaw.getPosition()-position))*0.8;//intake claw took 0.35 seconds (roughly) to open all the way (by all the way i mean from 0 to 0.5). So, i rounded up to 0.4 and divided by half becuase we are not using full range of motion
        outtakeClaw.setPosition(position);
        while (timer.seconds()<estimatedTime){
            //empty loop. Keeps running until actual position reaches target position
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
}
