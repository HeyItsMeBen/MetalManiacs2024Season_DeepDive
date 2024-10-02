package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;




import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
//1
public class compClaw {
    private DcMotor arm = null;
    private Servo leftClaw  = null;
    private Servo rightClaw = null;




    public compClaw(HardwareMap hMap) {




        //LinearSlide
        arm = hMap.get(DcMotor.class, "arm"); //added 7/24/24
        leftClaw = hMap.get(Servo.class, "armLeftServo"); // change display name after we design
        rightClaw = hMap.get(Servo.class, "armRightServo");




        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setPower(0);
        leftClaw.setDirection(Servo.Direction.FORWARD);
        rightClaw.setDirection(Servo.Direction.REVERSE);




    }




    public void open_close (double left, double right) {
        leftClaw.setPosition(left);
        rightClaw.setPosition(right);
    }




    public void moveArm(double power) {
        arm.setPower(power);
    }
}