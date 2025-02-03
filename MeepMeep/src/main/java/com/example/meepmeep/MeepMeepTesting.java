package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
//.
public class MeepMeepTesting {
    public static void main(String[] args) {

        double MeepMeepCompensation=1;

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity Finn = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        Vector2d scoring_position = new Vector2d((-50) * MeepMeepCompensation, (-50) * MeepMeepCompensation);
        Pose2d beginPose = new Pose2d(-15, -60, Math.toRadians(0));

        Finn.runAction(Finn.getDrive().actionBuilder(beginPose)

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

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(Finn)
                .start();
    }
}