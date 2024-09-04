package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous
public class LinearSlideAuto extends LinearOpMode {
    private DcMotor LinearSlide = null;
    private DcMotor LinearSlide1  = null;
    private CRServo ServoSlide = null;
    private CRServo ServoSlide1 = null;
    //public double fct=



    @Override
    public void runOpMode() {
        /*
         * NOTE: Many comments have been omitted from this sample for the
         * sake of conciseness. If you're just starting out with EasyOpenCv,
         * you should take a look at {@link InternalCamera1Example} or its
         * webcam counterpart, {@link WebcamExample} first.
         */

        LinearSlide = hardwareMap.get(DcMotor.class, "LinearSlideMotor"); //added 7/24/24
        ServoSlide = hardwareMap.get(CRServo.class, "ServoSlide");
        ServoSlide1 = hardwareMap.get(CRServo.class, "ServoSlide1");
        LinearSlide1 = hardwareMap.get(DcMotor.class, "LinearSlide1");

        waitForStart();

        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {

            LinearSlide.setPower(0.5);
            LinearSlide1.setPower(-0.5);
            sleep(2500);
            LinearSlide.setPower(0);
            LinearSlide1.setPower(0);

            sleep(1000);
            LinearSlide.setPower(-0.5);
            LinearSlide1.setPower(0.5);
            sleep(2500);
            LinearSlide.setPower(0);
            LinearSlide1.setPower(0);


            ServoSlide.setPower(0.5);
            ServoSlide1.setPower(-0.5);
            sleep(500);
            ServoSlide.setPower(0);
            ServoSlide1.setPower(0);

            sleep(1000);
            ServoSlide.setPower(-0.5);
            ServoSlide1.setPower(0.5);
            sleep(500);
            ServoSlide.setPower(0);
            ServoSlide1.setPower(0);
        }
        }
    }
