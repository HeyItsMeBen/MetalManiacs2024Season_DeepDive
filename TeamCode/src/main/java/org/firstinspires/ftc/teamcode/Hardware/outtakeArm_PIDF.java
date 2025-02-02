package org.firstinspires.ftc.teamcode.Hardware;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class outtakeArm_PIDF {
    private Servo outtakeArm;
    //sample positions
    public double grabSample=0.99; //0-->0.99
    public double scoreSample=0.3;//0.5;

    //specimen positions
    public double prepSpecimen=0.125;
    public double scoreSpecimen=0.125-0.0078125;    //i got 0.0078125 by dividing 0.25(90º) by 2, four times
    public double grabFromWall=0;   //1-->0

    //specimen/sample positions
    public double standby=0.886;//0.25;

    public outtakeArm_PIDF(HardwareMap hMap) {
        outtakeArm = hMap.get(Servo.class, "arm");
    }

    public void setArmTarget(double givenTarget) {
        outtakeArm.setPosition(givenTarget);
    }
}