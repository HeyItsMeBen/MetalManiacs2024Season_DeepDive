package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepExample {
    public static void main(String[] args) {

        double MeepMeepCompensation=1;

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        Vector2d scoring_position = new Vector2d((-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation, (-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation);
        Pose2d beginPose = new Pose2d(-36.99500+23.33, -60, Math.toRadians(0));

        myBot.runAction(myBot.getDrive().actionBuilder(beginPose)
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

                .splineTo(scoring_position, Math.toRadians(180))
                .turnTo(Math.toRadians(45))
                .waitSeconds(1)//score
                .setReversed(false)
                .waitSeconds(1)//reverse safety

                //Drop third sample
                .waitSeconds(2)

                .strafeTo(new Vector2d((-57) * MeepMeepCompensation, (-57) * MeepMeepCompensation)) //park

                .build());
//                .splineTo(new Vector2d(-50, -40), Math.PI/2)
//                .waitSeconds(1)
//                .turn(Math.toRadians(135))
//                .strafeTo(new Vector2d(-55, -55))
//                .waitSeconds(1)
//                .strafeTo(new Vector2d(-47, -47))
//                .splineTo(new Vector2d(-55, -40), -Math.PI/2)
                //.waitSeconds(2)
                //.turn(Math.toRadians(180))
                //.splineTo(new Vector2d(0, 60), Math.PI)
                //.build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}