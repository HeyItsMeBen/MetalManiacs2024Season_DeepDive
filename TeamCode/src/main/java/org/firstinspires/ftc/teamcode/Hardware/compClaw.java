package org.firstinspires.ftc.teamcode.Hardware;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class compClaw {
    private DcMotor arm;
    private Servo pivot;
    private Servo claw;
    private static double Encoder_COUNTS_PER_INCH = 44.5633841;

    public compClaw(HardwareMap hMap) {

        //LinearSlide
        arm = hMap.get(DcMotor.class, "arm"); //added 7/24/24
        pivot = hMap.get(Servo.class, "intakePivotServo"); // change display name after we design
        claw = hMap.get(Servo.class, "intakeClawServo");

        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        //arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setPower(0);

        pivot.setDirection(Servo.Direction.FORWARD);
        pivot.setDirection(Servo.Direction.FORWARD);

    }

    public void open_close (double value, double redundant) { //The redundant value has no use and is only present to ensure older files have no error
        claw.setPosition(value);
    }

    public void spin (double value) {
        pivot.setPosition(value);
    }

    public void moveArm(double power) {
        arm.setPower(power);
    }

    public void resetEncoderCount() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    private void setMotorPower(double dblPower){
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dblPower = Math.abs(dblPower);
        arm.setPower(dblPower);
    }
    public void moveArmUsingEncoder(double dblInches, double dblPower, String direction){
        dblInches=dblInches*37.6991118;
        int encoderCountsToMove = (int) (dblInches * Encoder_COUNTS_PER_INCH);
        arm.setTargetPosition(0);

        //stopDrive function
        arm.setTargetPosition(0);
        setMotorPower(0);
        resetEncoderCount();

        //"setForward" function (sets directions)
        if (direction == "up" || direction == "Up" || direction == "UP"){
            arm.setDirection(DcMotor.Direction.FORWARD);
        }
        else if (direction == "down" || direction == "Down" || direction == "DOWN"){
            arm.setDirection(DcMotor.Direction.REVERSE);
        }

        arm.setTargetPosition(encoderCountsToMove);
        setMotorPower(dblPower);

        while (arm.isBusy()) {

        }

        //stopDrive function
        arm.setTargetPosition(0);
        setMotorPower(0);
        resetEncoderCount();
    }

    public double geArmEncoderPosition() {
        return arm.getCurrentPosition();
    }
}