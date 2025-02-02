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
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.tuning.TuningOpModes;

import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.MecanumDrive;
import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.AutoCode.TankDrive;
import org.firstinspires.ftc.teamcode.Hardware.Arm_PIDF_UsableFromOtherClasses;
import org.firstinspires.ftc.teamcode.Hardware.Slides_PID;
import org.firstinspires.ftc.teamcode.Hardware.compLinearSlide;
import org.firstinspires.ftc.teamcode.Hardware.outtakeArm_PIDF;

@Autonomous(name = "SpecimenPathing", group = "Linear OpMode")
public final class AutoMainSpecimenPathing extends LinearOpMode {
    public double LeftStrafeCompensation=0;
    public double MeepMeepCompensation=1.0125;
    //Servo servo=hardwareMap.get(Servo.class, "servo");
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(-34.99500+23.33, -61.5, Math.toRadians(0));
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

            waitForStart();
            Actions.runBlocking(
                    drive.actionBuilder(beginPose)
                            //go to scoring position
                            .waitSeconds(3) //set timer if you want

                            //hang preloaded sample
                            .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-38) * MeepMeepCompensation))
                            .stopAndAdd(new scoreSpecimenPart1(hardwareMap))
                            .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-38+1) * MeepMeepCompensation))
                            .stopAndAdd(new scoreSpecimenPart2(hardwareMap))

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
    public class scoreSpecimenPart1 implements Action {
        Servo intakeClaw;
        Servo outtakeClaw;
        outtakeArm_PIDF outtakeArm;
        Slides_PID slides;
        double position;
        ElapsedTime timer;
        //setOuttakeArm, raise slides
        double[] waitList={2, 2};   //{2, 1} if u can
        //2.5, 3, 5, 7
        double waitListSize=2;
        double var=0;

        public scoreSpecimenPart1(HardwareMap hMap) {
            intakeClaw=hMap.get(Servo.class, "intakeClawServo");
            outtakeClaw=hMap.get(Servo.class, "outtakeServo");
            outtakeArm = new outtakeArm_PIDF(hMap);
            slides = new Slides_PID(hMap);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                outtakeClaw.setPosition(0.2);   //open
            }
            //list of commands. The instructions said to use Elapsed time like this, so i didn't use a sleep function. These commands run from bottom to top (meaning the bottom command runs first). The parameter inputted in the checkValue function is simply which action it's on (Eg, 4th action)

            if (timer.seconds() >= waitList[0]){
                slides.setSlidesTarget(-500);
            } else {
                outtakeArm.setArmTarget(outtakeArm.prepSpecimen);   //sets arm to parallel to the ground, and pointing out the back (preps arm to score)
            }

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
    }
    public class scoreSpecimenPart2 implements Action {
        Servo intakeClaw;
        Servo outtakeClaw;
        outtakeArm_PIDF outtakeArm;
        Slides_PID slides;
        double position;
        ElapsedTime timer;
        //moveArmDown(score), release specimen, retract slides.
        double[] waitList={2, 2, 2};    //{1, 1, 1} if possible
        //2.5, 3, 5, 7
        double waitListSize=3;
        double var=0;

        public scoreSpecimenPart2(HardwareMap hMap) {
            intakeClaw=hMap.get(Servo.class, "intakeClawServo");
            outtakeClaw=hMap.get(Servo.class, "outtakeServo");
            outtakeArm = new outtakeArm_PIDF(hMap);
            slides = new Slides_PID(hMap);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                outtakeClaw.setPosition(0.2);   //open
            }
            //list of commands. The instructions said to use Elapsed time like this, so i didn't use a sleep function. These commands run from bottom to top (meaning the bottom command runs first). The parameter inputted in the checkValue function is simply which action it's on (Eg, 4th action)

            if (timer.seconds() >= checkValue(3)){
                slides.setSlidesTarget(0);   //retract slides downward.
            } else if (timer.seconds() >= waitList[0]){
                outtakeClaw.setPosition(0.2);         //release specimen
            } else {
                outtakeArm.setArmTarget(outtakeArm.scoreSpecimen);   //push arm down abit, scoring specimen
            }

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
    }
    public class setStandby implements Action {
        outtakeArm_PIDF outtakeArm;
        /*ElapsedTime timer;
        //setStandby
        double[] waitList={0};    //{1, 1, 1} if possible
        double waitListSize=0;
        double var=0;*/

        public setStandby(HardwareMap hMap) {
            outtakeArm = new outtakeArm_PIDF(hMap);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            outtakeArm.setArmTarget(outtakeArm.standby);
            // do we need to keep running?
            /*if (timer.seconds() < checkValue(waitListSize+1)){
                return true;
            } else{
                return false;
            }*/
            return false;
        }
        /*double checkValue(double num){
            var=0;
            num-=1;
            for (int i=0; i<num; i++){
                var+=waitList[i];
            }
            return var;
        }*/
    }
    public class grabSpecimenFromWall implements Action {
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
            outtakeClaw=hMap.get(Servo.class, "outtakeServo");
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
    }





    //ActionWithSleep is a template and should not be used in the code
    public class ActionWithSleep implements Action {
        Servo servo;
        double position;
        ElapsedTime timer;

        public ActionWithSleep(Servo s, double p) {
            this.servo = s;
            this.position = p;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
            }

            servo.setPosition(position);

            // do we need to keep running?
            return timer.seconds() < 3;
        }
    }
    //QuickAction is a template and should not be used in the code
    public class QuickAction implements Action {
        Servo servo;
        double position;

        public QuickAction(Servo s, double p) {
            this.servo = s;
            this.position = p;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            servo.setPosition(position);
            return false;
        }
    }

}
