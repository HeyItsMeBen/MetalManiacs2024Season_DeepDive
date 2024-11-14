package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class LinearSlideEncoderTesting extends LinearOpMode {

    private DcMotor LinearSlideOne = null;
    private DcMotor LinearSlideTwo = null;

    private static double Encoder_COUNTS_PER_INCH = 3.17;


    public void runOpMode(){
        LinearSlideOne = hardwareMap.get(DcMotor.class, "leftSlide");
        LinearSlideTwo = hardwareMap.get(DcMotor.class, "rightSlide");

    }

}
