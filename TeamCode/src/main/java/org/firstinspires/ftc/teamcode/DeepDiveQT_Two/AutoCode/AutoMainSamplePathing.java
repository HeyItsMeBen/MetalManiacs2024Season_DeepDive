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
import org.firstinspires.ftc.teamcode.Hardware.compLinearSlide;
//import org.firstinspires.ftc.teamcode.Hardware.outtakeArm_PIDF;

@Autonomous(name = "SamplePathing", group = "Linear OpMode")
public final class AutoMainSamplePathing extends LinearOpMode {
    public double LeftStrafeCompensation=0;
    public double MeepMeepCompensation=1.0125;
    //Servo servo=hardwareMap.get(Servo.class, "servo");
    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d beginPose = new Pose2d(-36.99500+23.33, -60, Math.toRadians(0));

        TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class);
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        Vector2d scoring_position = new Vector2d((-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation, (-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation);

        waitForStart();

        Actions.runBlocking(drive.actionBuilder(beginPose)

                .waitSeconds(3) //set timer if you want

                .strafeTo(scoring_position)
                .turnTo(Math.toRadians(45))
                //.waitSeconds(1)//score

                //Drop first sample
                .waitSeconds(2)

                //grab first sample
                .splineTo(new Vector2d((-34.99500-(23.33/2)-1.5)*MeepMeepCompensation, (-53+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)

                .splineTo(scoring_position, Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)

                //Drop first sample
                .waitSeconds(2)

                //Grab second sample
                .turnTo(Math.toRadians(135))
                .splineTo(new Vector2d((-34.99500-(23.33/2)-1.5-9)*MeepMeepCompensation, (-53+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)

                .splineTo(scoring_position, 0)
                .turnTo(Math.toRadians(45))
                .waitSeconds(1)//score
                .setReversed(false)

                //Drop second sample
                .waitSeconds(2)

                //grab third sample
                .splineTo(new Vector2d((-49)*MeepMeepCompensation, (-25)*MeepMeepCompensation), Math.toRadians(90))//135
                .turnTo(Math.toRadians(180))
                .waitSeconds(1)//grab
                .setReversed(true)

                .splineTo(scoring_position, Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)
                .waitSeconds(1)//reverse safety

                //Drop third sample
                .waitSeconds(2)

                .strafeTo(new Vector2d((-57) * MeepMeepCompensation, (-57) * MeepMeepCompensation)) //park

                .build());

    }

    public class grabSample implements Action {
        Servo intakeClaw;
        Arm_PIDF_UsableFromOtherClasses arm;
        double position;
        ElapsedTime timer;

        public grabSample(HardwareMap hMap) {
            intakeClaw=hMap.get(Servo.class, "claw");
            arm = new Arm_PIDF_UsableFromOtherClasses(hMap);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                intakeClaw.setPosition(1);   //open
            }
            if (timer.seconds() >= 8){
                return true;
            } else if (timer.seconds() >= 6){
                arm.setArmTarget(1);
            } else if (timer.seconds() >= 3){
                intakeClaw.setPosition(0);   //close
            } else {
                arm.setArmTarget(0);        //arm down
            }

            // do we need to keep running?
            if (timer.seconds() < 10){
                return true;
            } else{
                return false;
            }
        }
    }
    public class scoreSample implements Action {
        Servo intakeClaw;
        Servo outtakeClaw;
        //outtakeArm_PIDF outtakeArm;    //WARNING. outtakeArm_PIDF code is IDENTICAL to Arm_PIDF_UsableFromOtherClasses, and is currently not tuned.
        compLinearSlide slides;
        double position;
        ElapsedTime timer;
        //outtakeArmDown, outtakeClawClose, intakeClawOpen, slidesUp, outtakeArmUp, outtakeClawOpen
        double[] waitList={2,.5,.5,2,2,2};
        //2.5, 3, 5, 7
        double waitListSize=6;
        double var=0;

        public scoreSample(HardwareMap hMap) {
            intakeClaw=hMap.get(Servo.class, "claw");
            outtakeClaw=hMap.get(Servo.class, "outtakeClaw");
            //outtakeArm = new outtakeArm_PIDF(hMap);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (timer == null) {
                timer = new ElapsedTime();
                outtakeClaw.setPosition(1);   //open
            }
            //list of commands. The instructions said to use Elapsed time like this, so i didn't use a sleep function. These commands run from bottom to top (meaning the bottom command runs first). The parameter inputted in the checkValue function is simply which action it's on (Eg, 4th action)

            //if (timer.seconds() >= checkValue(8)){
            //    outtakeArm.setArmTarget(0);        //arm down
            //}else
            if (timer.seconds() >= checkValue(7)){
                slides.extendVerticalUsingEncoder(0.25, 30, "DOWN");   //retracts slides downward
            }
            //finished scoring
            else if (timer.seconds() >= checkValue(6)){
                outtakeClaw.setPosition(1);   //open
            } else if (timer.seconds() >= checkValue(5)){
                //outtakeArm.setArmTarget(0.5);       //arm up
            } else if (timer.seconds() >= checkValue(4)){
                slides.extendVerticalUsingEncoder(0.25, 30, "UP");   //extends slides upward.
            } else if (timer.seconds() >=checkValue(3)){
                intakeClaw.setPosition(1);   //open
            } else if (timer.seconds() >= waitList[0]){
                outtakeClaw.setPosition(0);   //close
            } else {
                //outtakeArm.setArmTarget(0);        //arm down
            }

            // do we need to keep running?
            if (timer.seconds() < checkValue(waitListSize)){
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
