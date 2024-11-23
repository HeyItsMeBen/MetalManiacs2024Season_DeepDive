package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// IMPORTANT: As of 11/10 compLinearSlide has now been modified to also run on encoders.
//
// Even if your code does not use them, it shouldn't have any effect
//
// The method used to call the encoders is [extendVerticalUsingEncoder]. All others remain the same or are private
//
// Here is the proper syntax:
//
// [Linear slide variable name]
//          .extendVerticalUsingEncoder( [Input power] , [number of inches to travel] , "[direction up/down]")

public class compLinearSlide {

    private DcMotor LinearSlideL;
    private DcMotor LinearSlideR;
    private Servo ServoSpecimanDeployL;
    private Servo ServoSpecimanDeployR;
    private static double Encoder_COUNTS_PER_INCH = 134.136947;  //NEW VALUE (according to Ram. Also, the new CountsPerMotorRev is now 537.7)

    public compLinearSlide(HardwareMap hMap) {

        //LinearSlide
        LinearSlideL = hMap.get(DcMotor.class, "leftSlide"); //added 7/24/24
        LinearSlideR = hMap.get(DcMotor.class, "rightSlide"); // change display name after we design

        //Deploy the bucket for the servos
        ServoSpecimanDeployL = hMap.get(Servo.class, "leftOuttake");
        ServoSpecimanDeployR = hMap.get(Servo.class, "rightOuttake");

        LinearSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //LinearSlideL.setDirection(DcMotor.Direction.REVERSE);
        //LinearSlideR.setDirection(DcMotor.Direction.REVERSE);

    }

    public void resetEncoderCount() {
        LinearSlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void stopLinearSlides() {
        LinearSlideL.setPower(0);
        LinearSlideR.setPower(0);
    }

    public void extendVertical(double vertPower) {

        LinearSlideL.setDirection(DcMotor.Direction.FORWARD);
        LinearSlideR.setDirection(DcMotor.Direction.FORWARD);

//        int currentSlidePositionL = LinearSlideL.getCurrentPosition();
//        int currentSlidePositionR = LinearSlideR.getCurrentPosition();
//
//        if (minHeight > currentSlidePositionL && vertPower < 0 || currentSlidePositionL > maxHeight && vertPower > 0) {
//            LinearSlideL.setPower(0);
//        } else if (minHeight > currentSlidePositionR && vertPower < 0 || currentSlidePositionR > maxHeight && vertPower > 0) {
//            LinearSlideR.setPower(0);
//        }

        LinearSlideL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        LinearSlideL.setPower(vertPower);
        LinearSlideR.setPower(vertPower);

    }

    public void open_close_outtake(double left, double right) {
        ServoSpecimanDeployL.setPosition(left);
        ServoSpecimanDeployR.setPosition(right);
    }

    public void extendVerticalUsingEncoder(double vertPower, double dblInches, String direction) {
        stopLinearSlides();

        LinearSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (direction == "up" || direction == "Up" || direction == "UP") {
            LinearSlideL.setDirection(DcMotor.Direction.FORWARD);
            LinearSlideR.setDirection(DcMotor.Direction.FORWARD);
        } else if (direction == "down" || direction == "Down" || direction == "DOWN") {
            LinearSlideL.setDirection(DcMotor.Direction.REVERSE);
            LinearSlideR.setDirection(DcMotor.Direction.REVERSE);
        }

        if (dblInches > 23) {
            dblInches = 23;
        }

        int encoderCountsToMove = (int) (dblInches * Encoder_COUNTS_PER_INCH);

        LinearSlideL.setTargetPosition(Math.abs(encoderCountsToMove)); //This way, does not go below position 0
        LinearSlideR.setTargetPosition(Math.abs(encoderCountsToMove)); //This way, does not go below position 0

        LinearSlideL.setPower(Math.abs(vertPower));
        LinearSlideR.setPower(Math.abs(vertPower));

        LinearSlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (LinearSlideL.isBusy() && LinearSlideR.isBusy()) {


        }
        stopLinearSlides();
    }

    public double getLinearSlidePositions(String Which_Motor_ASCIIVALUE63, String format) {
        if (format == "encoder counts" || format == "encoder" || format == "Encoder counts" || format == "Encoders" || format == "Encoders count") {
            if (Which_Motor_ASCIIVALUE63 == "Left" || Which_Motor_ASCIIVALUE63 == "Left Motor" || Which_Motor_ASCIIVALUE63 == "LinearSlideL" || Which_Motor_ASCIIVALUE63 == "LeftMotor" || Which_Motor_ASCIIVALUE63 == "left") {
                return LinearSlideL.getCurrentPosition();
            } else if (Which_Motor_ASCIIVALUE63 == "Right" || Which_Motor_ASCIIVALUE63 == "Right Motor" || Which_Motor_ASCIIVALUE63 == "LinearSlideR" || Which_Motor_ASCIIVALUE63 == "RightMotor" || Which_Motor_ASCIIVALUE63 == "right") {
                return LinearSlideR.getCurrentPosition();
            }
        } else if (format == "Inches" || format == "inch" || format == "inch counts" || format == "Inch" || format == "inches") {
            if (Which_Motor_ASCIIVALUE63 == "Left" || Which_Motor_ASCIIVALUE63 == "Left Motor" || Which_Motor_ASCIIVALUE63 == "LinearSlideL" || Which_Motor_ASCIIVALUE63 == "LeftMotor" || Which_Motor_ASCIIVALUE63 == "left") {
                return (LinearSlideL.getCurrentPosition() / 134.136947);
            } else if (Which_Motor_ASCIIVALUE63 == "Right" || Which_Motor_ASCIIVALUE63 == "Right Motor" || Which_Motor_ASCIIVALUE63 == "LinearSlideR" || Which_Motor_ASCIIVALUE63 == "RightMotor" || Which_Motor_ASCIIVALUE63 == "right") {
                return (LinearSlideR.getCurrentPosition() / 134.136947);
            }
        }
        return 100000000.000000001; //Couldn't set it to null, so figured this would be easiest way to display return value
    }

}

