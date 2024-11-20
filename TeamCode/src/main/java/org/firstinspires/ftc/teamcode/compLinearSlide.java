package org.firstinspires.ftc.teamcode;

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

<<<<<<< Updated upstream
    private static double Encoder_COUNTS_PER_INCH = 134.136947;  //NEW VALUE (according to Ram. Also, the new CountsPerMotorRev is now 537.7)
=======
    private static double Encoder_COUNTS_PER_INCH =537.7/(112/25.4);  //NEW VALUE (according to Ram. Also, the new CountsPerMotorRev is now 537.7)
>>>>>>> Stashed changes


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

<<<<<<< Updated upstream
    public void resetEncoders () {
=======
    public void resetEncoderCount() {
>>>>>>> Stashed changes
        LinearSlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void stopLinearSlides () {
        LinearSlideL.setPower(0);
        LinearSlideR.setPower(0);
    }

<<<<<<< Updated upstream
    public double getEncoderPositions (String Slide) {
        if (Slide == "Left" || Slide == "left") {
            return LinearSlideL.getCurrentPosition();
        } else if (Slide == "Right" || Slide == "right") {
            return LinearSlideR.getCurrentPosition();
        }
        return -1;
    }

    public void extendVertical (double vertPower) {
        LinearSlideL.setDirection(DcMotor.Direction.FORWARD);
        LinearSlideR.setDirection(DcMotor.Direction.FORWARD);
=======
    public void extendVertical (double vertPower) {

        LinearSlideL.setDirection(DcMotor.Direction.REVERSE);
        LinearSlideR.setDirection(DcMotor.Direction.REVERSE);

>>>>>>> Stashed changes
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

    public void open_close_outtake (double left, double right) {
        ServoSpecimanDeployL.setPosition(left);
        ServoSpecimanDeployR.setPosition(right);
    }

    public void extendVerticalUsingEncoder (double vertPower, double dblInches, String direction) {
        stopLinearSlides();

        LinearSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (direction == "up" || direction == "Up" || direction == "UP") {
            LinearSlideL.setDirection(DcMotor.Direction.REVERSE);
            LinearSlideR.setDirection(DcMotor.Direction.REVERSE);
        } else if (direction == "down" || direction == "Down" || direction == "DOWN") {
            LinearSlideL.setDirection(DcMotor.Direction.FORWARD);
            LinearSlideR.setDirection(DcMotor.Direction.FORWARD);
        }

        int encoderCountsToMove = (int) (dblInches * Encoder_COUNTS_PER_INCH);

        LinearSlideL.setTargetPosition(encoderCountsToMove);
        LinearSlideR.setTargetPosition(encoderCountsToMove);

        LinearSlideL.setPower(Math.abs(vertPower));
        LinearSlideR.setPower(Math.abs(vertPower));

        LinearSlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (LinearSlideL.isBusy() && LinearSlideR.isBusy()) {

        }
        stopLinearSlides(); //End Drive
    }

    public double getLinearSlidePositions(String Which_Motor) {
        if (Which_Motor == "Left" || Which_Motor == "Left Motor" || Which_Motor == "LinearSlideL" || Which_Motor == "LeftMotor" || Which_Motor == "left") {
            return LinearSlideL.getCurrentPosition();
        } else if (Which_Motor == "Right" || Which_Motor == "Right Motor" || Which_Motor == "LinearSlideR" || Which_Motor == "RightMotor" || Which_Motor == "right") {
            return LinearSlideR.getCurrentPosition();
        }
        return -1; //Can't set it to null, so I had to set it to a different value
        }
    }

