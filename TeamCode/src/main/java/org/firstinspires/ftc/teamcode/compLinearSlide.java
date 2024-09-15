package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class compLinearSlide {
    private DcMotor LinearSlideL = null;
    private DcMotor LinearSlideR  = null;

    private Servo ServoSpecimanDeployL = null;
    private Servo ServoSpecimanDeployR = null;

    public compLinearSlide(HardwareMap hardwareMap) {

        //LinearSlide
        LinearSlideL = hardwareMap.get(DcMotor.class, "LinearSlideMotor"); //added 7/24/24
        LinearSlideR = hardwareMap.get(DcMotor.class, "LinearSlide1"); // change display name after we design

        //Deploy the bucket for the servos
        ServoSpecimanDeployL = hardwareMap.get(Servo.class, "PlaceHolder");
        ServoSpecimanDeployR = hardwareMap.get(Servo.class, "PlaceHolder");

        LinearSlideL.setDirection(DcMotorSimple.Direction.FORWARD);
        LinearSlideR.setDirection(DcMotorSimple.Direction.REVERSE);

        ServoSpecimanDeployL.setDirection(Servo.Direction.FORWARD);
        ServoSpecimanDeployR.setDirection(Servo.Direction.REVERSE);

        LinearSlideL.setPower(0);
        LinearSlideR.setPower(0);


    }

    public void extendVertical (int vertPower) {
        LinearSlideL.setPower(vertPower);
        LinearSlideR.setPower(vertPower);
    }

    public void DropSpeciman() {
        ServoSpecimanDeployL.setPosition(0.5);
        ServoSpecimanDeployR.setPosition(0.5);
    }

}