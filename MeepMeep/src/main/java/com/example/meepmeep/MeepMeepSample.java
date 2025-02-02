package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
//.
public class MeepMeepSample {
    public static void main(String[] args) {
        double MeepMeepCompensation=1;

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        Vector2d scoring_position = new Vector2d((-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation, (-23.33 * 2.5 + 17 / 2) * MeepMeepCompensation);
        Pose2d beginPose = new Pose2d(-15, -60, Math.toRadians(0));

        myBot.runAction(myBot.getDrive().actionBuilder(beginPose)

                //go to scoring position
                .strafeTo(scoring_position)
                .turnTo(Math.toRadians(45))

                //drop preloaded sample
                .waitSeconds(1)//score

                //go to first sample
                .splineTo(new Vector2d((-34.99500-(23.33/2)-1.5)*MeepMeepCompensation, (-53+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)

                //grab sample
                .waitSeconds(2)

                //return to scoring position
                .splineTo(scoring_position, Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)

                //drop first sample
                .waitSeconds(2)

                //go to second sample
                .turnTo(Math.toRadians(135))
                .splineTo(new Vector2d((-34.99500-(23.33/2)-1.5-9)*MeepMeepCompensation, (-53+(23.33/2))*MeepMeepCompensation), Math.toRadians(90))
                .waitSeconds(1)//grab
                .setReversed(true)

                //grab second sample
                .waitSeconds(2)//reverse safety

                //return to scoring position
                .splineTo(scoring_position, Math.toRadians(0))
                .waitSeconds(1)//score
                .setReversed(false)
                .turnTo(Math.toRadians(45))

                //drop second sample
                .waitSeconds(2)

                //go to third sample
                .turnTo(Math.toRadians(90))
                .splineTo(new Vector2d((-55)*MeepMeepCompensation, (-37)*MeepMeepCompensation), Math.toRadians(90))//135
                .waitSeconds(1)//grab
                .setReversed(true)
                .waitSeconds(1)//reverse safety

                //grab third sample
                .turnTo(Math.toRadians(135))
                .setReversed(true)
                .splineTo(scoring_position, Math.toRadians(225))
                .waitSeconds(1)//score
                .setReversed(false)

                //score final sample
                .waitSeconds(2)//reverse safety

                //park
                .strafeTo(new Vector2d((-60)*MeepMeepCompensation, (-60)*MeepMeepCompensation))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}