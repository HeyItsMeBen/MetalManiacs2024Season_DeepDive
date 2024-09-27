package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
<<<<<<< HEAD
//trigger code
=======

>>>>>>> origin/Test-Branch
public class compLinearSlide {
    private DcMotor LinearSlideL = null;
    private DcMotor LinearSlideR  = null;

    private Servo ServoSpecimanDeployL = null;
    private Servo ServoSpecimanDeployR = null;

<<<<<<< HEAD
    public compLinearSlide(HardwareMap hardwareMap) {

        //LinearSlide
        LinearSlideL = hardwareMap.get(DcMotor.class, "LinearSlideMotor"); //added 7/24/24
        LinearSlideR = hardwareMap.get(DcMotor.class, "LinearSlide1"); // change display name after we design

        //Deploy the bucket for the servos
        ServoSpecimanDeployL = hardwareMap.get(Servo.class, "PlaceHolder");
        ServoSpecimanDeployR = hardwareMap.get(Servo.class, "PlaceHolder");
=======
    private static final int ENCODER_COUNTS_PER_INCH = 8;   //added from Comp_Drive

    public compLinearSlide(HardwareMap hardwareMap) {

        //LinearSlide
        LinearSlideL = hardwareMap.get(DcMotor.class, "leftSlide"); //added 7/24/24
        LinearSlideR = hardwareMap.get(DcMotor.class, "rightSlide"); // change display name after we design

        //Deploy the bucket for the servos
        ServoSpecimanDeployL = hardwareMap.get(Servo.class, "leftOuttake");
        ServoSpecimanDeployR = hardwareMap.get(Servo.class, "rightOuttake");
>>>>>>> origin/Test-Branch

        LinearSlideL.setDirection(DcMotorSimple.Direction.FORWARD);
        LinearSlideR.setDirection(DcMotorSimple.Direction.REVERSE);

        ServoSpecimanDeployL.setDirection(Servo.Direction.FORWARD);
        ServoSpecimanDeployR.setDirection(Servo.Direction.REVERSE);

<<<<<<< HEAD
        LinearSlideL.setPower(0);
        LinearSlideR.setPower(0);


    }

    public void extendVertical (int vertPower) {
=======

        LinearSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideL.setTargetPosition(0);
        LinearSlideR.setTargetPosition(0);
        LinearSlideL.setPower(0);
        LinearSlideR.setPower(0);
    }

    public void extendVertical (double vertPower) {
>>>>>>> origin/Test-Branch
        LinearSlideL.setPower(vertPower);
        LinearSlideR.setPower(vertPower);
    }

<<<<<<< HEAD
    public void DropSpeciman() {
        ServoSpecimanDeployL.setPosition(0.5);
        ServoSpecimanDeployR.setPosition(0.5);
=======
    public void open_close(double left, double right) {
        ServoSpecimanDeployL.setPosition(left);
        ServoSpecimanDeployR.setPosition(right);
    }
    public void test1(){
        LinearSlideL.setTargetPosition(50);
        LinearSlideR.setTargetPosition(50);

        LinearSlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LinearSlideR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        LinearSlideL.setPower(0.25);
        LinearSlideL.setPower(0.25);

        LinearSlideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LinearSlideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
>>>>>>> origin/Test-Branch
    }

}
