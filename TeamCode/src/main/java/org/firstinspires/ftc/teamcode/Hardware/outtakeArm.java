package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class outtakeArm {
    private Servo outtakeArmRightServo;
    private Servo outtakeArmLeftServo;
    //sample positions
    public double grabSample=0.9; //0.99-->0.865
    public double scoreSample=0.3;

    //specimen positions
    public double prepSpecimen=0.13;
    //public double scoreSpecimen=0.125-0.0078125;    //i got 0.0078125 by dividing 0.25(90º) by 2, four times
    public double grabFromWall=0;   //0

    //specimen/sample positions
    public double standby=0.7;//0.885-->.7

    public outtakeArm(HardwareMap hMap) {
        outtakeArmRightServo = hMap.get(Servo.class, "rightOuttake"); //real name? //"outtakeArm"
        outtakeArmLeftServo = hMap.get(Servo.class, "leftOuttake"); //real name? //"outtakeArm"
    }

    public void setArmTarget(double givenTarget) {
        outtakeArmRightServo.setPosition(givenTarget);
        outtakeArmLeftServo.setPosition(1-givenTarget);
    }
    public double getArmPosition(){return outtakeArmRightServo.getPosition();}
}