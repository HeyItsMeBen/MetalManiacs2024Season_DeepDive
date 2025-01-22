//package org.firstinspires.ftc.teamcode.Legacy;
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
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
////Hardware Imports
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//
////AprilTag Imports
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//
////Tensor Flow Imports
//import org.firstinspires.ftc.vision.VisionPortal;
////import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
//import org.firstinspires.ftc.vision.tfod.TfodProcessor;
//
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Arrays;
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
//@Disabled
//@Autonomous(name = "AutonomousBlueCloseSpikePath", group = "Robot")
////@Disabled
//public class AutonomousBlueCloseSpikePath extends LinearOpMode {
//
//    public DcMotor frontLeftDrive = null;
//    public DcMotor backLeftDrive = null;
//    public DcMotor frontRightDrive = null;
//    public DcMotor backRightDrive = null;
//    static final double FORWARD_SPEED = 0.6;
//    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
//    private static final int DESIRED_TAG_ID = 0;     // Choose the tag you want to approach or set to -1 for ANY tag.
//    private VisionPortal visionPortal;               // Used to manage the video source.
//    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag
//    private TfodProcessor tfod;
//    public ArrayList<String> label = new ArrayList<>();
//
//    //private TFObjectDetector tfod;
//    public boolean tested = false;
//    /*public List<Recognition> work = null;
//    public List<String> label = new ;
//    public int listposition = 0;*/
//
//    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/BlueRedCastle.tflite";
//    boolean Detected = false;
//    private static final String[] TargetProp = {
//            "blue castle"
//    };
//
//    @Override
//    public void runOpMode() {
//
//        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
//        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
//        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
//        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");
//        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
//        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
//        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
//        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
//        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
//        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
//        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
//
//        // Initialize the Apriltag Detection process
//
//        // Initialize the TensorFlow Detection process
//        initTfod();
//
//        // Wait for driver to press start
//        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
//        telemetry.addData(">", "Touch Play to start OpMode");
//        telemetry.update();
//        waitForStart();
//
//        telemetry.addData("working ", "still1");
//        telemetry.update();
//        sleep(500);
//
//        if (opModeIsActive()) {
//            desiredTag = null;
//            while (opModeIsActive()) {
//
//                telemetryTfod();
//                telemetry.addData("Detection value:",Detected);
//                telemetry.update();
//
//                //robot starts on right
//
//                if (tfod != null) {
//                    if (Detected == true) {
//                        telemetry.addData("working...", "scanned 1");
//                        telemetry.addData("Detection value:",Detected);
//                        telemetry.update();
//                        sleep(2000);
//                        // getUpdatedRecognitions() will return null if no new information is available since
//                        // the last time that call was made.
//                    /*List<Recognition> updatedRecognitions = tfod.getRecognitions();
//                    if ((updatedRecognitions != null) && (updatedRecognitions.size() != 0) && (tested == false)) {
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
//                            work = tfod.getRecognitions();
//                            label.add(objRecognition.getLabel());
//                            listposition++;
//                        }
//                        */
//                    } else {
//                        frontLeftDrive.setPower(2);
//                        frontRightDrive.setPower(2);
//                        backLeftDrive.setPower(2);
//                        backRightDrive.setPower(2);
//                        if (Detected == true) {
//                            telemetry.addData("working...", "scanned 2");
//                            telemetry.addData("Detection value:",Detected);
//                            telemetry.update();
//                            sleep(2000);
//                        } else {
//                            frontLeftDrive.setPower(2);
//                            frontRightDrive.setPower(2);
//                            backLeftDrive.setPower(2);
//                            backRightDrive.setPower(2);
//                            if (Detected == true) {
//                                telemetry.addData("working...", "scanned 3");
//                                telemetry.addData("Detection value:",Detected);
//                                telemetry.update();
//                                sleep(2000);
//                            } else {
//                                telemetry.addData(">", "...somethings wrong...");
//                                telemetry.addData("Detection value:",Detected);
//                                telemetry.update();
//                                sleep(2000);
//                            }
//                        }
//                    }
//                        tested = true;
//                    }
//                }
//
//            }
//        }
//
//
//
//
//
//    /**
//     * Initialize the TensorFlow Object Detection engine.
//     */
//    private void initTfod() {
//
//        // Create the TensorFlow processor by using a builder.
//        tfod = new TfodProcessor.Builder()
//
//                // With the following lines commented out, the default TfodProcessor Builder
//                // will load the default model for the season. To define a custom model to load,
//                // choose one of the following:
//                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
//                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
//                //.setModelAssetName(TFOD_MODEL_ASSET)
//                .setModelFileName(TFOD_MODEL_FILE)
//
//                // The following default settings are available to un-comment and edit as needed to
//                // set parameters for custom models.
//                .setModelLabels(TargetProp)
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
//        builder.enableLiveView(true);
//
//        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
//        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
//
//        // Choose whether or not LiveView stops if no processors are enabled.
//        // If set "true", monitor shows solid orange screen if no processors enabled.
//        // If set "false", monitor shows camera view without annotations.
//        builder.setAutoStopLiveView(false);
//
//        // Set and enable the processor.
//        builder.addProcessor(tfod);
//
//        // Build the Vision Portal, using the above settings.
//        visionPortal = builder.build();
//
//        // Set confidence threshold for TFOD recognitions, at any time.
//        //tfod.setMinResultConfidence(0.75f);
//
//        // Disable or re-enable the TFOD processor at any time.
//        visionPortal.setProcessorEnabled(tfod, true);
//
//    }   // end method initTfod()
//
//    /**
//     * private void initTfod() {
//     * <p>
//     * // Create the TensorFlow processor by using a builder.
//     * tfop = new TfodProcessor.Builder()
//     * <p>
//     * // Use setModelAssetName() if the TF Model is built in as an asset.
//     * // Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
//     * //.setModelAssetName(TFOD_MODEL_ASSET)
//     * //.setModelFileName(TFOD_MODEL_FILE)
//     * <p>
//     * //.setModelLabels(LABELS)
//     * //.setIsModelTensorFlow2(true)
//     * //.setIsModelQuantized(true)
//     * //.setModelInputSize(300)
//     * //.setModelAspectRatio(16.0 / 9.0)
//     * <p>
//     * .build();
//     * <p>
//     * // Create the vision portal by using a builder.
//     * VisionPortal.Builder builder = new VisionPortal.Builder();
//     * <p>
//     * // Set the camera (webcam vs. built-in RC phone camera).
//     * if (USE_WEBCAM) {
//     * builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
//     * } else {
//     * builder.setCamera(BuiltinCameraDirection.BACK);
//     * }
//     * <p>
//     * // Choose a camera resolution. Not all cameras support all resolutions.
//     * //builder.setCameraResolution(new Size(640, 480));
//     * <p>
//     * // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
//     * //builder.enableCameraMonitoring(true);
//     * <p>
//     * // Set the stream format; MJPEG uses less bandwidth than default YUY2.
//     * //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
//     * <p>
//     * // Choose whether or not LiveView stops if no processors are enabled.
//     * // If set "true", monitor shows solid orange screen if no processors enabled.
//     * // If set "false", monitor shows camera view without annotations.
//     * //builder.setAutoStopLiveView(false);
//     * <p>
//     * // Set and enable the processor.
//     * builder.addProcessor(tfop);
//     * <p>
//     * // Build the Vision Portal, using the above settings.
//     * visionPortal = builder.build();
//     * <p>
//     * // Set confidence threshold for TFOD recognitions, at any time.
//     * //tfod.setMinResultConfidence(0.75f);
//     * <p>
//     * // Disable or re-enable the TFOD processor at any time.
//     * //visionPortal.setProcessorEnabled(tfod, true);
//     * <p>
//     * }   // end method initTfod()
//     * /**
//     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
//     */
//    private void telemetryTfod() {
//
//        List<Recognition> currentRecognitions = tfod.getRecognitions();
//        if ((currentRecognitions.size() == 1)) {
//            Detected = true;
//        telemetry.addData("# Objects Detected", currentRecognitions.size());
//
//        // Step through the list of recognitions and display info for each one.
//        for (Recognition recognition : currentRecognitions) {
//            double x = (recognition.getLeft() + recognition.getRight()) / 2;
//            double y = (recognition.getTop() + recognition.getBottom()) / 2;
//            telemetry.addData("", " ");
//            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
//            telemetry.addData("- Position", "%.0f / %.0f", x, y);
//            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
//        }   // end for() loop
//
//    }   // end method telemetryTfod(
//    }   // end method telemetryAprilTag()
//}
//
//    /*
//     Manually set the camera gain and exposure.
//     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
//    */
//    /**private void    setManualExposure(int exposureMS, int gain) {
//     // Wait for the camera to be open, then use the controls
//
//     if (visionPortal == null) {
//     return;
//     }
//
//     // Make sure camera is streaming before we try to set the exposure controls
//     if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
//     telemetry.addData("Camera", "Waiting");
//     telemetry.update();
//     while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
//     sleep(20);
//     }
//     telemetry.addData("Camera", "Ready");
//     telemetry.update();
//     }
//
//     // Set camera controls unless we are stopping.
//     if (!isStopRequested())
//     {
//     ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
//     if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
//     exposureControl.setMode(ExposureControl.Mode.Manual);
//     sleep(50);
//     }
//     exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
//     sleep(20);
//     GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
//     gainControl.setGain(gain);
//     sleep(20);
//     }
//     }
//     **/
