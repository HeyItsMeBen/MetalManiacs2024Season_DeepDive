package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;




import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
//1
public class compLinearSlide {
    private DcMotor LinearSlideL;
    private DcMotor LinearSlideR;




    private Servo ServoSpecimanDeployL;
    private Servo ServoSpecimanDeployR;




    private static final int ENCODER_COUNTS_PER_INCH = 8;   //added from Comp_Drive

    //Linear slide: hardware, or hardware-software compliance, issues   //Linear slide servos: hardware
    //Arm:  software                                                    //arm claw: hardware


    public compLinearSlide(HardwareMap hMap) {




        //LinearSlide
        LinearSlideL = hMap.get(DcMotor.class, "leftSlide"); //added 7/24/24
        LinearSlideR = hMap.get(DcMotor.class, "rightSlide"); // change display name after we design




        //Deploy the bucket for the servos
        ServoSpecimanDeployL = hMap.get(Servo.class, "leftOuttake");
        ServoSpecimanDeployR = hMap.get(Servo.class, "rightOuttake");


        LinearSlideL.setDirection(DcMotorSimple.Direction.FORWARD);
        LinearSlideR.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void extendVertical (double vertPower) {
        LinearSlideL.setPower(vertPower);
        LinearSlideR.setPower(vertPower);
    }

    public void open_close_outtake (double left, double right) {
        ServoSpecimanDeployL.setPosition(left);
        ServoSpecimanDeployR.setPosition(right);
    }
}
