//package org.firstinspires.ftc.teamcode;
//
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;
//
//@SuppressWarnings("unused")
//@Disabled
//@TeleOp (name="Pixel Placement Code", group="test")
//public class PixelPlacementCode extends LinearOpMode {
//
//    //Set up servo for rotating the box (may not be needed)
//    private Servo Rotate;
//
//    //Set up servos for opening and closing the box
//    private Servo LeftBox;
//    private Servo RightBox;
//
//    //Set up motors for rotating and extending the arm
//    private DcMotor Arm;
//
//    //set up power variables
//    double InitPositionServo = 0.0; //sets original position for all servos
//    //double ArmPower = 1.0; //sets power for Arm motor
//
//    //@Override
//    public void runOpMode() {
//
//        //notify the driver to tell them that the code is ready to be ran
//        telemetry.addData("PRESS PLAY!!!!!", "Start+A");
//        telemetry.update();
//
//        //box
//        LeftBox = hardwareMap.get(Servo.class, "left_box");
//        RightBox = hardwareMap.get(Servo.class, "right_box");
//        Rotate = hardwareMap.get(Servo.class, "rotate_box");
//
//        //arm
//        Arm = hardwareMap.get(DcMotor.class, "arm");
//        Arm.setDirection(DcMotor.Direction.FORWARD);
//        int ArmPosition = Arm.getCurrentPosition();
//
//        //initial position
//        LeftBox.setPosition(InitPositionServo);
//        RightBox.setPosition(InitPositionServo);
//
//        waitForStart();
//
//        //executing
//        while (opModeIsActive()) {
//
//            /*
//            //setting arm in deployment place
//            while (gamepad2.left_stick_y > 0) {
//                Arm.setPower(ArmPower);
//            }
//            //bringing arm back
//            while (gamepad2.left_stick_y < 0) {
//                Arm.setPower(-ArmPower);
//            }
//            Arm.setPower(0); //ensures it only moves when triggered
//            */
//
//            double ArmPower = gamepad2.left_stick_y;
//            Arm.setPower(ArmPower);
//
//            //rotating and releasing pixels
//            while (gamepad2.a) {
//                Rotate.setPosition(InitPositionServo);
//                LeftBox.setPosition(InitPositionServo);
//                RightBox.setPosition(-InitPositionServo);
//            }
//            //closing box and returning to original position (may become obsolete later)
//            while (gamepad2.y) {
//                Rotate.setPosition(-InitPositionServo);
//                LeftBox.setPosition(-InitPositionServo);
//                RightBox.setPosition(InitPositionServo);
//            }
//
//            //auto-retract/cycle
//
//            //auto-returning everything to the original position
//            while (gamepad2.b) {
//                Rotate.setPosition(-InitPositionServo);
//                LeftBox.setPosition(-InitPositionServo);
//                RightBox.setPosition(InitPositionServo);
//                Arm.setTargetPosition(ArmPosition);
//            }
//
//            //autoCycle
//            /*
//            while (gamepad2.x) {
//                Arm.setPower(5.0);
//                LinearSlide.setPower(5.0);
//                Arm.setPower(0);
//                LinearSlide.setPower(0);
//                Rotate.setPosition(InitPositionServo);
//                LeftBox.setPosition(InitPositionServo);
//                RightBox.setPosition(-InitPositionServo);
//                sleep(1000);
//                Rotate.setPosition(-InitPositionServo);
//                LeftBox.setPosition(-InitPositionServo);
//                RightBox.setPosition(InitPositionServo);
//                Arm.setPower(-5.0);
//                LinearSlide.setPower(-5.0);
//                Arm.setPower(0);
//                LinearSlide.setPower(0);
//
//
//            }
//        */
//        }
//    }
//}
