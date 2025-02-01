package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepSample {
    public static void main(String[] args) {
        double MeepMeepCompensation=1;

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        //Vector2d scoring_position = new Vector2d((-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation, (-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation);
        Pose2d beginPose = new Pose2d(10, -60, Math.toRadians(-90));

        myBot.runAction(myBot.getDrive().actionBuilder(beginPose)

                .strafeTo(new Vector2d((-23.33*2.5+17/2)*MeepMeepCompensation,(-23.33*2.5+17/2)*MeepMeepCompensation))
                .turnTo(Math.toRadians(45))
                .waitSeconds(1)//score
                //.stopAndAdd(new ActionWithSleep(servo, 0.5))
                .splineTo(new Vector2d((-34.99500-(23.33/2)-1.5)*MeepMeepCompensation, (-53+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)
                .waitSeconds(1)//reverse safety
                //.splineTo(new Vector2d(-23.33*2.5,-61.5), Math.toRadians(225))
                .splineTo(new Vector2d((-23.33*2.5+17/2)*MeepMeepCompensation,(-23.33*2.5+17/2)*MeepMeepCompensation), Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)
                .waitSeconds(1)//reverse safety
                .splineTo(new Vector2d((-34.99500-(23.33/2)-1.5-9)*MeepMeepCompensation, (-53+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)
                .waitSeconds(1)//reverse safety
                .splineTo(new Vector2d((-23.33*2.5+17/2)*MeepMeepCompensation,(-23.33*2.5+17/2)*MeepMeepCompensation), Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)
                .waitSeconds(1)//reverse safety
                .splineTo(new Vector2d((-34.99500-(23.33/2)-1.5-18)*MeepMeepCompensation, (-53+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))//135
                .waitSeconds(1)//grab
                .setReversed(true)
                .waitSeconds(1)//reverse safety
                .splineTo(new Vector2d((-23.33*2.5+17/2)*MeepMeepCompensation,(-23.33*2.5+17/2)*MeepMeepCompensation), Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)
                .waitSeconds(1)//reverse safety
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}