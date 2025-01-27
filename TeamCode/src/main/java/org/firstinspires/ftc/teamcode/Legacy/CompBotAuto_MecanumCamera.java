//package org.firstinspires.ftc.teamcode;
//// Test
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline;
//import org.openftc.apriltag.AprilTagDetection;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvCameraRotation;
//import java.util.ArrayList;
//
//@Autonomous(name="Spider Variation Auto Code", group="a")
//public class CompBotAuto_MecanumCamera extends LinearOpMode{
//
//    private ElapsedTime runtime = new ElapsedTime();
//    private DcMotor frontLeftDrive = null;
//    private DcMotor backLeftDrive = null;
//    private DcMotor frontRightDrive = null;
//    private DcMotor backRightDrive = null;
//
//    @Override
//    public void runOpMode() {
//        // Driver Code
//        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel");
//        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel");
//        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
//        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");
//        // set direction for motors
//        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
//        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
//        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
//        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
//
//
//    }
//}
