package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class compClaw {
    private DcMotor arm = null;
    private Servo leftClaw  = null;
    private Servo rightClaw = null;

    public compClaw(HardwareMap hMap) {

        //LinearSlide
        arm = hMap.get(DcMotor.class, "PlaceHolder"); //added 7/24/24
        leftClaw = hMap.get(Servo.class, "PlaceHolder"); // change display name after we design
        rightClaw = hMap.get(Servo.class, "PlaceHolder");

        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setPower(0);

    }

    public void open_close (double left, double right) {
        leftClaw.setPosition(left);
        rightClaw.setPosition(right);
    }

    public void moveArm(double power) {
        arm.setPower(power);
    }
}