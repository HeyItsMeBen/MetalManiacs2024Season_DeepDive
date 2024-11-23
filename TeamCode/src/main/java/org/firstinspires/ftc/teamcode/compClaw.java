package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;




import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class compClaw {
    private DcMotor arm;
    private Servo leftClaw;
    private Servo rightClaw;
    private static double Encoder_COUNTS_PER_INCH = 44.5633841;

    public compClaw(HardwareMap hMap) {

        //LinearSlide
        arm = hMap.get(DcMotor.class, "arm"); //added 7/24/24
        leftClaw = hMap.get(Servo.class, "armLeftServo"); // change display name after we design
        rightClaw = hMap.get(Servo.class, "armRightServo");

        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        //arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setPower(0);

        leftClaw.setDirection(Servo.Direction.FORWARD);
        rightClaw.setDirection(Servo.Direction.FORWARD);

    }

    public void open_close (double left, double right) {
        leftClaw.setPosition(left);
        rightClaw.setPosition(right);
    }

    public void moveArm(double power) {
        arm.setPower(power);
    }
    /*public void moveArmUsingEncoder(double dblInches, double dblPower, String direction){
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        dblInches=dblInches*37.6991118;//*0.444444444;
        if (direction == "up" || direction == "Up" || direction == "UP") {
            arm.setDirection(DcMotorSimple.Direction.FORWARD);
        } else if (direction == "down" || direction == "Down" || direction == "DOWN") {
            arm.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        int encoderCountsToMove = (int) (dblInches * Encoder_COUNTS_PER_INCH);

        arm.setTargetPosition(encoderCountsToMove);
        arm.setPower(Math.abs(dblPower));
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (arm.isBusy()) {

        }

        arm.setPower(0);
    }*/

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