package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

//.
public class MeepMeepSpecimenWithCompensation {
    public static void main(String[] args) {
        double currentTileSize=70/3;//divide by 3 cuz 70 is the size for half a field
        double referenceTileSize=70/3;//(reference tile should be MeepMeep)
        double currentBotLength=17;
        double referenceBotLength=17;
        //Notes: 71.125(ourFieldSize), 70.5in(compFieldSize), 70in(MeepMeep)
        double MeepMeepTileCompensation=currentTileSize/referenceTileSize;   //multiply this by every movement of the wheels (besides rotation ofc)
        //double interactionCompensation=(currentTileSize-referenceTileSize)/2+(currentBotLength/2-referenceBotLength/2);    //applicable when bot interacts with something at it's side, rather than at it's center (eg: starting, scoring(usually), grabbing(usually). This happens cuz when field size changes and bot size stays constant, the field size to bot size ratio changes. Ask me for clarification if needed.
        double currentInteractionDistance=(currentTileSize/2)-(currentBotLength/2);
        double referenceInteractionDistance=(referenceTileSize/2)-(referenceBotLength/2);
        double interactionCompensation=currentInteractionDistance-referenceInteractionDistance;

        double botSizeWhenGrabFromWall=19-(2/16);
        double botSizeWhenScoreSpecimen=19.86;

        double grabFromWallCompensation=((currentTileSize/2)-((botSizeWhenGrabFromWall*(currentBotLength/referenceBotLength))-currentBotLength/2))-((referenceTileSize/2)-(botSizeWhenGrabFromWall-referenceBotLength/2));  //This is here because i wasn't allowed to put methods in MeepMeep. I used a method in the autonomous programs, and it's a lot smoother than these variables here
        double scoreSpecimenCompensation=((currentTileSize/2)-((botSizeWhenScoreSpecimen*(currentBotLength/referenceBotLength))-currentBotLength/2))-((referenceTileSize/2)-(botSizeWhenScoreSpecimen-referenceBotLength/2)); //This is here because i wasn't allowed to put methods in MeepMeep. I used a method in the autonomous programs, and it's a lot smoother than these variables here

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity Finn = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(currentBotLength, currentBotLength)
                .build();

        Pose2d beginPose = new Pose2d(-34.99500+23.33+23.6+2.0, -62+0.1875006-interactionCompensation, Math.toRadians(270));

        Finn.runAction(Finn.getDrive().actionBuilder(beginPose)

                //go to scoring position
                //.stopAndAdd(new setServos(hardwareMap, 0, 0, 0.99))
                .waitSeconds(3) //set timer if you want
                //hang preloaded sample
                .strafeTo(new Vector2d((5) * MeepMeepTileCompensation, (-39.5) * MeepMeepTileCompensation))

                //.stopAndAdd(new scoreSpecimenPart1(hardwareMap))
                .waitSeconds(4)
                .strafeTo( new Vector2d((5) * MeepMeepTileCompensation, (-32.75-0.1875006+scoreSpecimenCompensation) * MeepMeepTileCompensation))    //scores
                //.stopAndAdd(new scoreSpecimenPart2(hardwareMap))                                                //releases
                .waitSeconds(4)
                .strafeTo( new Vector2d((5) * MeepMeepTileCompensation, (-46.5) * MeepMeepTileCompensation))

                //.stopAndAdd(new scoreSpecimenPart3(hardwareMap))
                .waitSeconds(4)

                .splineTo(new Vector2d((20)* MeepMeepTileCompensation, (-53.5)* MeepMeepTileCompensation), Math.toRadians(Math.PI/2))
                //.splineTo(new Vector2d((35+2)*MeepMeepCompensation, (-20)*MeepMeepCompensation), Math.toRadians(95))
                .splineTo(new Vector2d((35)* MeepMeepTileCompensation, (-12.5)* MeepMeepTileCompensation), Math.toRadians(90))

                //.waitSeconds(0.5)

                //push first sample
                .strafeTo(new Vector2d((45) * MeepMeepTileCompensation, (-12.5) * MeepMeepTileCompensation))
                .strafeTo(new Vector2d((45) * MeepMeepTileCompensation, (-50.5) * MeepMeepTileCompensation))

                //push second sample
                /*.strafeTo(new Vector2d((45) * MeepMeepCompensation, (-12) * MeepMeepCompensation))
                .strafeTo(new Vector2d((55) * MeepMeepCompensation, (-12) * MeepMeepCompensation))
                .strafeTo(new Vector2d((55) * MeepMeepCompensation, (-50) * MeepMeepCompensation))*/

                //score specimen
                //grab specimen from wall 1
                .strafeToLinearHeading(new Vector2d((45) * MeepMeepTileCompensation, (-45) * MeepMeepTileCompensation), Math.toRadians(90))  //get out of observation zone    //-47
                .waitSeconds(1) //Human get ready!
                .strafeTo(new Vector2d((45) * MeepMeepTileCompensation, (-55.25+0.1875006-grabFromWallCompensation) * MeepMeepTileCompensation))  //-70+11.5-->-50-->
                //.stopAndAdd(new grabSpecimenFromWallPart2(hardwareMap))
                .waitSeconds(4) //(temporary until i get arm working)
                .strafeTo(new Vector2d((45) * MeepMeepTileCompensation, (-49.5) * MeepMeepTileCompensation))  //-70+11.5-->-50-->
                //.waitSeconds(1)//grabFromWall

                //move to bar and score
                .turnTo(Math.toRadians(0))//180-->0
                .setTangent(Math.toRadians(180))
                .splineTo( new Vector2d((5-4) * MeepMeepTileCompensation, (-39.5) * MeepMeepTileCompensation), Math.toRadians(90))
                //.stopAndAdd(new scoreSpecimenPart1(hardwareMap))
                .waitSeconds(4)
                //.stopAndAdd(new setServos(hardwareMap, 0, 0.035, 0.597222222))
                .waitSeconds(1)
                //.stopAndAdd(new setServos(hardwareMap, 0, 0, outtakeArm.prepSpecimen))
                .strafeTo( new Vector2d((5-4) * MeepMeepTileCompensation, (-31.25-0.1875006+scoreSpecimenCompensation) * MeepMeepTileCompensation))     //scores //-4-->0
                //.stopAndAdd(new scoreSpecimenPart2(hardwareMap))                                                //releases
                .waitSeconds(4)
                .strafeTo( new Vector2d((5-4) * MeepMeepTileCompensation, (-46.5) * MeepMeepTileCompensation))

                //.stopAndAdd(new scoreSpecimenPart3(hardwareMap))
                .waitSeconds(4)

                //grab specimen from wall 2
                .strafeToLinearHeading(new Vector2d((45) * MeepMeepTileCompensation, (-45) * MeepMeepTileCompensation), Math.toRadians(90))  //get out of observation zone    //-47
                .waitSeconds(1) //Human get ready!
                .strafeTo(new Vector2d((45) * MeepMeepTileCompensation, (-55.25+0.1875006-grabFromWallCompensation) * MeepMeepTileCompensation))  //-70+11.5-->-50-->
                //.stopAndAdd(new grabSpecimenFromWallPart2(hardwareMap))
                .waitSeconds(4) //(temporary until i get arm working)
                .strafeTo(new Vector2d((45) * MeepMeepTileCompensation, (-49.5) * MeepMeepTileCompensation))  //-70+11.5-->-50-->
                //.waitSeconds(1)//grabFromWall

                //move to bar and score
                .turnTo(Math.toRadians(0))//180-->0
                .setTangent(Math.toRadians(180))
                .splineTo( new Vector2d((5-8) * MeepMeepTileCompensation, (-39.5) * MeepMeepTileCompensation), Math.toRadians(90))
                //.stopAndAdd(new scoreSpecimenPart1(hardwareMap))
                .waitSeconds(4)
                //.stopAndAdd(new setServos(hardwareMap, 0, 0.035, 0.597222222))
                .waitSeconds(1)
                //.stopAndAdd(new setServos(hardwareMap, 0, 0, outtakeArm.prepSpecimen))
                .strafeTo( new Vector2d((5-8) * MeepMeepTileCompensation, (-31.25-0.1875006+scoreSpecimenCompensation) * MeepMeepTileCompensation))     //scores //-4-->0
                //.stopAndAdd(new scoreSpecimenPart2(hardwareMap))                                                //releases
                .waitSeconds(4)
                .strafeTo( new Vector2d((5-8) * MeepMeepTileCompensation, (-46.5) * MeepMeepTileCompensation))

                //.stopAndAdd(new scoreSpecimenPart3(hardwareMap))
                //.waitSeconds(4)
                //end
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(Finn)
                .start();
    }
}