package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;




import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


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


    private static double Encoder_COUNTS_PER_INCH = 31.7 * 2;


    public compLinearSlide(HardwareMap hMap) {

        //LinearSlide
        LinearSlideL = hMap.get(DcMotor.class, "leftSlide"); //added 7/24/24
        LinearSlideR = hMap.get(DcMotor.class, "rightSlide"); // change display name after we design


        //Deploy the bucket for the servos
        ServoSpecimanDeployL = hMap.get(Servo.class, "leftOuttake");
        ServoSpecimanDeployR = hMap.get(Servo.class, "rightOuttake");

        LinearSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void resetEncoderCount() {
        LinearSlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void stopLinearSlides () {
        LinearSlideL.setTargetPosition(0);
        LinearSlideR.setTargetPosition(0);

        LinearSlideL.setPower(0);
        LinearSlideR.setPower(0);

        resetEncoderCount();
    }

    public void extendVertical (double vertPower) {
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
            LinearSlideL.setDirection(DcMotorSimple.Direction.REVERSE);
            LinearSlideR.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if (direction == "down" || direction == "Down" || direction == "DOWN") {
            LinearSlideL.setDirection(DcMotorSimple.Direction.FORWARD);
            LinearSlideR.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        int encoderCountsToMove = (int) (dblInches * Encoder_COUNTS_PER_INCH);

        LinearSlideL.setTargetPosition(encoderCountsToMove);
        LinearSlideR.setTargetPosition(encoderCountsToMove);

        LinearSlideL.setPower(vertPower);
        LinearSlideR.setPower(vertPower);

        LinearSlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (LinearSlideL.isBusy() && LinearSlideR.isBusy()) {

        }
        // Stop the motors and reset the power to 0 and also reset encoder count
        stopLinearSlides(); //End Drive Reset Encoders
    }
}
