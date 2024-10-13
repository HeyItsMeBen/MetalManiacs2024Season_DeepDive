package org.firstinspires.ftc.teamcode;
import static android.os.SystemClock.sleep;




import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
//1
public class compLinearSlide {
    private DcMotor LinearSlideL = null;
    private DcMotor LinearSlideR  = null;




    private Servo ServoSpecimanDeployL = null;
    private Servo ServoSpecimanDeployR = null;




    private static final int ENCODER_COUNTS_PER_INCH = 8;   //added from Comp_Drive




    public compLinearSlide(HardwareMap hardwareMap) {




        //LinearSlide
        LinearSlideL = hardwareMap.get(DcMotor.class, "leftSlide"); //added 7/24/24
        LinearSlideR = hardwareMap.get(DcMotor.class, "rightSlide"); // change display name after we design




        //Deploy the bucket for the servos
        ServoSpecimanDeployL = hardwareMap.get(Servo.class, "leftOuttake");
        ServoSpecimanDeployR = hardwareMap.get(Servo.class, "rightOuttake");


        LinearSlideL.setDirection(DcMotorSimple.Direction.FORWARD);
        LinearSlideR.setDirection(DcMotorSimple.Direction.FORWARD);


        /*ServoSpecimanDeployL.setDirection(Servo.Direction.REVERSE);
        ServoSpecimanDeployR.setDirection(Servo.Direction.FORWARD);*/



        /*LinearSlideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);      //Commented. Used for encoders, which we don't have yet
        LinearSlideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LinearSlideL.setTargetPosition(0);
        LinearSlideR.setTargetPosition(0);
        LinearSlideL.setPower(0);
        LinearSlideR.setPower(0);
         */
    }




    public void extendVertical (double vertPower) {
        LinearSlideL.setPower(vertPower);
        LinearSlideR.setPower(vertPower);
    }




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
    }




}
