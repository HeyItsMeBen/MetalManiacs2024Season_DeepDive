package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepSpecimen {
    public static void main(String[] args) {

        double MeepMeepCompensation=1;

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        //Vector2d scoring_position = new Vector2d((-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation, (-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation);
        Pose2d beginPose = new Pose2d(-34.99500+23.33+23.6+2.0, -61.5, Math.toRadians(270));

        myBot.runAction(myBot.getDrive().actionBuilder(beginPose)

                .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-38) * MeepMeepCompensation))
                //.stopAndAdd(new scoreSpecimenPart1(hardwareMap))
                .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-38+1) * MeepMeepCompensation))    //shud be +4
                //.stopAndAdd(new scoreSpecimenPart2(hardwareMap))
                .strafeTo( new Vector2d((5) * MeepMeepCompensation, (-38-8) * MeepMeepCompensation))

                .waitSeconds(2)
                //.stopAndAdd(new setStandby(hardwareMap))

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

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}