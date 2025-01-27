//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;
//
//@TeleOp(name="EndgameLaunch - Test Code (Disabled)", group="Drive")
//@Disabled
//public class EndgameLaunch extends LinearOpMode {
//
//    private DcMotor LeftRigging = null;
//    private DcMotor RightRigging = null;
//    private Servo DroneLaunch = null;
//
//    public void runOpMode() {
//
//        telemetry.addData("Press start+a on gamepad", "");
//        telemetry.addData("To use LeftRigging/RightRigging Motors: Press Left trigger for up, Press Right trigger for down", "");
//        telemetry.addData("To launch the drone: Press x", "");
//        telemetry.update();
//
//        LeftRigging = hardwareMap.get(DcMotor.class, "Left_Rigging_Motor"); //still need to configure
//        RightRigging = hardwareMap.get(DcMotor.class, "Right_Rigging_Motor"); //still need to configure
//        DroneLaunch = hardwareMap.get(Servo.class, "Drone_Launch_Servo"); //still need to configure
//
//        DroneLaunch.setPosition(0);
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//
//            while(gamepad1.left_trigger > 0){
//                LeftRigging.setPower(1.0);
//                RightRigging.setPower(1.0);
//            }
//            while(gamepad1.right_trigger > 0){
//                LeftRigging.setPower(-1.0);
//                RightRigging.setPower(-1.0);
//            }
//            LeftRigging.setPower(0);
//            RightRigging.setPower(0);
//
//            if (gamepad1.x){
//                DroneLaunch.setPosition(3);
//            }
//        }
//    }
//}
