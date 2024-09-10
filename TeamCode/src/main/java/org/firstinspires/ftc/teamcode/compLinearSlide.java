//Harsika and Ram Code

package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class compLinearSlide {
    private DcMotor LinearSlide = null;
    private DcMotor LinearSlide1  = null;
    private CRServo ServoSlide = null;
    private CRServo ServoSlide1 = null;
    private double vertPower = 0.5;
    private double horzPower = 1;

    public compLinearSlide(HardwareMap hardwareMap){

        LinearSlide = hardwareMap.get(DcMotor.class, "LinearSlideMotor"); //added 7/24/24
        ServoSlide = hardwareMap.get(CRServo.class, "ServoSlide");
        ServoSlide1 = hardwareMap.get(CRServo.class, "ServoSlide1");
        LinearSlide1 = hardwareMap.get(DcMotor.class, "LinearSlide1"); // change display name after we design bot
        LinearSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        LinearSlide1.setDirection(DcMotorSimple.Direction.REVERSE);
        ServoSlide.setDirection(CRServo.Direction.FORWARD);
        ServoSlide1.setDirection(CRServo.Direction.REVERSE);

        LinearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LinearSlide1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        LinearSlide.setPower(0);
        LinearSlide1.setPower(0);
        ServoSlide.setPower(0);
        ServoSlide1.setPower(0);
    }

    public void extendUp () {
        LinearSlide.setPower(vertPower);
        LinearSlide1.setPower(vertPower);
        sleep(2500); // will extend up for 2.5 seconds
        LinearSlide.setPower(0);
        LinearSlide1.setPower(0);
    }

    public void extendDown () {
        LinearSlide.setPower(-vertPower);
        LinearSlide1.setPower(-vertPower);
        sleep(2500); // will extend down for 2.5 seconds
        LinearSlide.setPower(0);
        LinearSlide1.setPower(0);
    }

    public void extendOut () {
        ServoSlide.setPower(horzPower);
        ServoSlide1.setPower(horzPower);
        sleep(500); // will extend outwards for .5 seconds
        ServoSlide.setPower(0);
        ServoSlide1.setPower(0);
    }

    public void extendIn () {
        ServoSlide.setPower(-horzPower);
        ServoSlide1.setPower(-horzPower);
        sleep(500); // will extend inwards for .5 seconds
        ServoSlide.setPower(0);
        ServoSlide1.setPower(0);
    }
}
