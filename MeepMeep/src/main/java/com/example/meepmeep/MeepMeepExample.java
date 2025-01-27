package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepExample {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-15, -55, Math.toRadians(90)))
                //.lineToX(30)
                //.turn(Math.toRadians(90))
                //.lineToY(30)
                //.turn(Math.toRadians(90))
                //.lineToX(0)
                //.turn(Math.toRadians(90))
                //.lineToY(0)
                //.turn(Math.toRadians(90))
                .splineTo(new Vector2d(-50, -40), Math.PI/2)
                .waitSeconds(1)
                .turn(Math.toRadians(135))
                .strafeTo(new Vector2d(-55, -55))
                .waitSeconds(1)
                .strafeTo(new Vector2d(-47, -47))
                .splineTo(new Vector2d(-55, -40), -Math.PI/2)
                //.waitSeconds(2)
                //.turn(Math.toRadians(180))
                //.splineTo(new Vector2d(0, 60), Math.PI)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}