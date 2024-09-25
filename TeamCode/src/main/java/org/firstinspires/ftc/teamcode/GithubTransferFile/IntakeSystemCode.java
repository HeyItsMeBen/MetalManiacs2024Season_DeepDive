package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
@Disabled
@TeleOp(name="IntakeSystemCode - Competition Code", group="Drive")
//@Disabled
public class IntakeSystemCode extends LinearOpMode {

    private DcMotor Intake   = null;
    private DcMotor LinearSlide = null;
    private DcMotor Arm1 = null;
    private DcMotor Arm2 = null;
    private CRServo LeftServo = null;
    private CRServo RightServo = null;
    private Servo DroneLaunch = null;

    public void runOpMode() {

        telemetry.addData("Press start+b on gamepad","");
        telemetry.addData("To use Intake Motor: Press y for forward power, Press a for reverse power","");
        telemetry.addData("To use Linear Slide Motor: Left joystick up/down","");
        telemetry.addData("To use Arm Motor: Right joystick up/down","");
        telemetry.addData("To use Box Servos: Press left trigger to move up, Press right trigger to move down","");
        telemetry.update();

        Intake  = hardwareMap.get(DcMotor.class, "Intake");
        LinearSlide = hardwareMap.get(DcMotor.class, "Linear_Slide"); //still need to configure
        Arm1 = hardwareMap.get(DcMotor.class, "Arm_1_Motor"); //still need to configure
        Arm2 = hardwareMap.get(DcMotor.class, "Arm_2_Motor"); //still need to configure
        LeftServo = hardwareMap.get(CRServo.class, "Left_Servo"); //still need to configure
        RightServo = hardwareMap.get(CRServo.class, "Right_Servo"); //still need to configure

        DroneLaunch.setPosition(0);

        waitForStart();

        while (opModeIsActive()) {

            //Intake System
            while (gamepad2.y) {
                Intake.setPower(1.0);
            }
            while (gamepad2.a) {
                Intake.setPower(-1.0);
            }
            Intake.setPower(0);

            //LinearSlide
            while (gamepad2.left_stick_y > 0) {
                LinearSlide.setPower(1.0);
            }
            while (gamepad2.left_stick_y < 0) {
                LinearSlide.setPower(-1.0);
            }
            LinearSlide.setPower(0);

            //Arm
            while (gamepad2.right_stick_y > 0) {
                Arm1.setPower(1.0);
                Arm2.setPower(1.0);
            }
            while (gamepad2.right_stick_y < 0) {
                Arm1.setPower(-1.0);
                Arm2.setPower(-1.0);
            }
            Arm1.setPower(0);
            Arm2.setPower(0);

            //Servo
            if (gamepad2.left_trigger > 0) {
                LeftServo.setPower(1.0);
                RightServo.setPower(1.0);
            }
            if (gamepad2.right_trigger > 0) {
                LeftServo.setPower(-1.0);
                RightServo.setPower(-1.0);
            }
            LeftServo.setPower(0);
            RightServo.setPower(0);

            if (gamepad2.x) {
                DroneLaunch.setPosition(3);
            }
        }
    }

}