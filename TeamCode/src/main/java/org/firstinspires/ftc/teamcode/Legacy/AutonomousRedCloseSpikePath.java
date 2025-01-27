//package org.firstinspires.ftc.teamcode;
//
///* Copyright (c) 2019 FIRST. All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without modification,
// * are permitted (subject to the limitations in the disclaimer below) provided that
// * the following conditions are met:
// *
// * Redistributions of source code must retain the above copyright notice, this list
// * of conditions and the following disclaimer.
// *
// * Redistributions in binary form must reproduce the above copyright notice, this
// * list of conditions and the following disclaimer in the documentation and/or
// * other materials provided with the distribution.
// *
// * Neither the name of FIRST nor the names of its contributors may be used to endorse or
// * promote products derived from this software without specific prior written permission.
// *
// * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
// * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
// * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
// * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
////import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
////import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
//import org.firstinspires.ftc.vision.VisionPortal;
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//import org.firstinspires.ftc.vision.tfod.TfodProcessor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//
///**
// * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
// * determine which image is being presented to the robot.
// *
// * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
// * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
// *
// * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
// * is explained below.
// */
//@Autonomous(name = "AutonomousRedCloseSpikePath", group = "Robot")
//@Disabled
//public class AutonomousRedCloseSpikePath extends LinearOpMode {
//
//    // Adjust these numbers to suit your robot.
//    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)
//
//    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
//    //  applied to the drive motors to correct the error.
//    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
//    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
//    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
//    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)
//
//    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
//    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
//    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)
//
//    private DcMotor leftFrontDrive   = null;  //  Used to control the left front drive wheel
//    private DcMotor rightFrontDrive  = null;  //  Used to control the right front drive wheel
//    private DcMotor leftBackDrive    = null;  //  Used to control the left back drive wheel
//    private DcMotor rightBackDrive   = null;  //  Used to control the right back drive wheel
//    public ElapsedTime runtime = new ElapsedTime();
//    static final long FOOT_SECONDS = 2000;
//    public DcMotor frontLeftDrive = null;
//    public DcMotor backLeftDrive = null;
//    public DcMotor frontRightDrive = null;
//    public DcMotor backRightDrive = null;
//
//
//    static final double FORWARD_SPEED = 0.6;
//
//    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
//    private static final int DESIRED_TAG_ID = 0;     // Choose the tag you want to approach or set to -1 for ANY tag.
//    private VisionPortal visionPortal;               // Used to manage the video source.
//    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
//    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag
//    private TfodProcessor tfop;
//    private TFObjectDetector tfod;
//    public boolean tested = false;
//    public  List<Recognition> work = null;
//    public List<String> label = new ArrayList<>();
//    public int listposition = 0;
//
//    @Override public void runOpMode()
//    {
//        boolean targetFound     = false;    // Set to true when an AprilTag target is detected
//        double  drive           = 0;        // Desired forward power/speed (-1 to +1)
//        double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
//        double  turn            = 0;        // Desired turning power/speed (-1 to +1)
//
//        // Initialize the Apriltag Detection process
//        initAprilTag();
//
//
//        // Initialize the TensorFlow Detection process
//        initTfod();
//
//        // Initialize the hardware variables. Note that the strings used here as parameters
//        // to 'get' must match the names assigned during the robot configuration.
//        // step (using the FTC Robot Controller app on the phone).
//        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
//        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
//        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
//        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");
//
//        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
//        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
//        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
//        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
//        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
//        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
//        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
//
//        if (USE_WEBCAM)
//            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
//
//        // Wait for driver to press start
//        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
//        telemetry.addData(">", "Touch Play to start OpMode");
//        telemetry.update();
//        waitForStart();
//
//
//
//        telemetry.addData("working ", "still1");
//        telemetry.update();
//        sleep(500);
//
//        if (opModeIsActive())
//        {
//            targetFound = false;
//            desiredTag  = null;
//            while (opModeIsActive()) {
//                if (tfod != null) {
//                    telemetry.addData("working ", "still3");
//                    telemetry.update();
//                    sleep(500);
//                    // getUpdatedRecognitions() will return null if no new information is available since
//                    // the last time that call was made.
//                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
//                    if ((updatedRecognitions != null) && (updatedRecognitions.size() != 0) && (tested == false)){
//                        telemetry.addData("working ", "still4");
//                        telemetry.update();
//                        sleep(500);
//                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
//                        telemetry.update();
//                        sleep(500);
//
//                        // step through the list of recognitions and display image position/size information for each one
//                        // Note: "Image number" refers to the randomized image orientation/number
//                        for (Recognition objRecognition : updatedRecognitions) {
//                            telemetry.addData("working ", "hopefully");
//                            telemetry.update();
//                            sleep(500);
//
//                            double col = (objRecognition.getLeft() + objRecognition.getRight()) / 2;
//                            double row = (objRecognition.getTop() + objRecognition.getBottom()) / 2;
//                            double width = Math.abs(objRecognition.getRight() - objRecognition.getLeft());
//                            double height = Math.abs(objRecognition.getTop() - objRecognition.getBottom());
//
//                            telemetry.addData("working ", objRecognition.getLabel());
//                            telemetry.update();
//                            sleep(500);
//
//                            telemetry.addData("", " ");
//                            telemetry.addData("Image", "%s (%.0f %% Conf.)", objRecognition.getLabel(), objRecognition.getConfidence() * 100);
//                            telemetry.addData("- Position (Row/Col)", "%.0f / %.0f", row, col);
//                            telemetry.addData("- Size (Width/Height)", "%.0f / %.0f", width, height);
//                            work = tfod.getUpdatedRecognitions();
//                            label.add(objRecognition.getLabel());
//                            listposition++;
//                        }
//                        telemetry.update();
//                        //List<String> labels = new ArrayList<String>();;
//                        //labels = Arrays.asList(label);
//                        telemetry.addData("working ", "still6");
//                        telemetry.update();
//
//                        //May need to change the function to TensorFlow
//                        if (label.contains("LeftSpikeMark")) {
//                            //Drive Towards LeftSpikeMark
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(FORWARD_SPEED);
//                            sleep(5700);
//                            telemetry.addData("path", "one");
//                            telemetry.update();
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Face SpikeMark by turning 90 Degrees
//                            frontLeftDrive.setPower(-FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(-FORWARD_SPEED);
//                            backRightDrive.setPower(FORWARD_SPEED);
//                            sleep(2000);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Drop Purple Pixel on RightSpikeMark; Need add in Arm Code later
//
//
//                            //Turn Towards Backdrop
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(-FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(-FORWARD_SPEED);
//                            sleep(4000);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Drive Towards Backdrop
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(FORWARD_SPEED);
//                            sleep(5700);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Place Yellow Pixel on Backdrop; Need to add in arm code later
//
//                            tested = true;
//                        }
//
//                        else if (label.contains("CenterSpikeMark")) {
//                            //Drive Towards CenterSpikeMark
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(FORWARD_SPEED);
//                            sleep(5700);
//                            telemetry.addData("path", "two");
//                            telemetry.update();
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Drop Purple Pixel on CenterSpikeMark; Need add in Arm Code later
//
//
//                            //Turn Towards Backdrop
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(-FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(-FORWARD_SPEED);
//                            sleep(2000);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Drive Towards Backdrop
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(FORWARD_SPEED);
//                            sleep(5700);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Place Yellow Pixel on Backdrop; Need to add in arm code later
//
//                            tested = true;
//                        }
//
//                        else if (label.contains("RightSpikeMark")) {
//                            //Drive Towards RightSpikeMark
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(FORWARD_SPEED);
//                            sleep(3800);
//                            telemetry.addData("path", "three");
//                            telemetry.update();
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Face SpikeMark by turning 30 Degrees
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(-FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(-FORWARD_SPEED);
//                            sleep(667);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Drop Purple Pixel on RightSpikeMark; Need add in Arm Code later
//
//
//                            //Turn Towards Backdrop
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(-FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(-FORWARD_SPEED);
//                            sleep(1333);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            //Drive Towards Backdrop
//                            frontLeftDrive.setPower(FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(FORWARD_SPEED);
//                            sleep(5700);
//                            frontLeftDrive.setPower(0);
//                            frontRightDrive.setPower(0);
//                            backLeftDrive.setPower(0);
//                            backRightDrive.setPower(0);
//                            frontLeftDrive.setPower(-FORWARD_SPEED);
//                            frontRightDrive.setPower(FORWARD_SPEED);
//                            backLeftDrive.setPower(FORWARD_SPEED);
//                            backRightDrive.setPower(-FORWARD_SPEED);
//                            sleep(1900);
//                            //Place Yellow Pixel on Backdrop; Need to add in arm code later
//
//                            tested = true;
//                        }
//                    }
//
//                }
//            }    }
//    }
//
//
//    /**
//     * Initialize the TensorFlow Object Detection engine.
//     */
//
//
//    private void initTfod() {
//
//        // Create the TensorFlow processor by using a builder.
//        tfop = new TfodProcessor.Builder()
//
//                // Use setModelAssetName() if the TF Model is built in as an asset.
//                // Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
//                //.setModelAssetName(TFOD_MODEL_ASSET)
//                //.setModelFileName(TFOD_MODEL_FILE)
//
//                //.setModelLabels(LABELS)
//                //.setIsModelTensorFlow2(true)
//                //.setIsModelQuantized(true)
//                //.setModelInputSize(300)
//                //.setModelAspectRatio(16.0 / 9.0)
//
//                .build();
//
//        // Create the vision portal by using a builder.
//        VisionPortal.Builder builder = new VisionPortal.Builder();
//
//        // Set the camera (webcam vs. built-in RC phone camera).
//        if (USE_WEBCAM) {
//            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
//        } else {
//            builder.setCamera(BuiltinCameraDirection.BACK);
//        }
//
//        // Choose a camera resolution. Not all cameras support all resolutions.
//        //builder.setCameraResolution(new Size(640, 480));
//
//        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
//        //builder.enableCameraMonitoring(true);
//
//        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
//        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
//
//        // Choose whether or not LiveView stops if no processors are enabled.
//        // If set "true", monitor shows solid orange screen if no processors enabled.
//        // If set "false", monitor shows camera view without annotations.
//        //builder.setAutoStopLiveView(false);
//
//        // Set and enable the processor.
//        builder.addProcessor(tfop);
//
//        // Build the Vision Portal, using the above settings.
//        visionPortal = builder.build();
//
//        // Set confidence threshold for TFOD recognitions, at any time.
//        //tfod.setMinResultConfidence(0.75f);
//
//        // Disable or re-enable the TFOD processor at any time.
//        //visionPortal.setProcessorEnabled(tfod, true);
//
//    }   // end method initTfod()
//    /**
//     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
//     */
//    private void telemetryTfod() {
//
//        List<Recognition> currentRecognitions = tfod.getRecognitions();
//        telemetry.addData("# Objects Detected", currentRecognitions.size());
//
//        // Step through the list of recognitions and display info for each one.
//        for (Recognition recognition : currentRecognitions) {
//            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
//            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;
//
//            telemetry.addData(""," ");
//            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
//            telemetry.addData("- Position", "%.0f / %.0f", x, y);
//            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
//        }   // end for() loop
//
//    }   // end method telemetryTfod()
//    // end class
//
//    private void initAprilTag() {
//        // Create the AprilTag processor by using a builder.
//        aprilTag = new AprilTagProcessor.Builder().build();
//
//        // Create the vision portal by using a builder.
//        if (USE_WEBCAM) {
//            visionPortal = new VisionPortal.Builder()
//                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
//                    .addProcessor(aprilTag)
//                    .build();
//        } else {
//            visionPortal = new VisionPortal.Builder()
//                    .setCamera(BuiltinCameraDirection.BACK)
//                    .addProcessor(aprilTag)
//                    .build();
//        }
//    }
//
//    /*
//     Manually set the camera gain and exposure.
//     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
//    */
//    private void    setManualExposure(int exposureMS, int gain) {
//        // Wait for the camera to be open, then use the controls
//
//        if (visionPortal == null) {
//            return;
//        }
//
//        // Make sure camera is streaming before we try to set the exposure controls
//        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
//            telemetry.addData("Camera", "Waiting");
//            telemetry.update();
//            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
//                sleep(20);
//            }
//            telemetry.addData("Camera", "Ready");
//            telemetry.update();
//        }
//
//        // Set camera controls unless we are stopping.
//        if (!isStopRequested())
//        {
//            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
//            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
//                exposureControl.setMode(ExposureControl.Mode.Manual);
//                sleep(50);
//            }
//            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
//            sleep(20);
//            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
//            gainControl.setGain(gain);
//            sleep(20);
//        }
//    }
//
//
//}