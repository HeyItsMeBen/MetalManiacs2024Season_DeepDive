
/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
//////  MAKE SURE to update hardware map  ////////
/*Main things the whole program does:
    1. Setup (including attaching pipeline)
    2. Detect tags
    3. Get one wanted tag Id
    4. Get other info based about that tag (telemetry) like distance
    5. Autonomous. Make decisions based on the given info.
 */
@Autonomous //makes program autonomous?
public class compCam extends LinearOpMode {
    public compCam(HardwareMap hardwareMap){

    }
    //assign variables
    //hopefully deletable
    /*private DcMotor frontleftMotor;
    private DcMotor frontrightMotor;
    private DcMotor backleftMotor;
    private DcMotor backrightMotor;
    */
    //deletable?
    private Blinker control_Hub;
    private Gyroscope imu;

    double tgtPower = 0;
    //setup?
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    //mor3 variables
    //deletable?
    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    //tag ID 1,2,3 from the 36h11 family
    int Left = 1;
    int Middle = 2;
    int Right = 3;

    //This variable should hold the value(ID) of the April tag it detects
    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {
        //mapping (syncing code to the control hub or whatever)
        //deletable?
        /*frontleftMotor = hardwareMap.get(DcMotor.class, "Left");
        frontrightMotor = hardwareMap.get(DcMotor.class, "Right");*/
        //backleftMotor = hardwareMap.get(DcMotor.class, "backleftMotor");
        //backrightMotor = hardwareMap.get(DcMotor.class, "backrightMotor");
        //control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        //imu = hardwareMap.get(Gyroscope.class, "imu");



        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        //Not sure what pipeline is, but it's probably just some necessary code fo rit to run. So, this should just attach the pipeline file to this one
        camera.setPipeline(aprilTagDetectionPipeline);
        //Looks like event listeners
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            //starts streaming
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            //assuming this is a fail-safe mechanism
            public void onError(int errorCode)
            {

            }
        });
        //how often the camera sends info to the main "control hub" (or whatever it's called)
        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        //"while program is being run, run this code"
        while (!isStarted() && !isStopRequested())
        {
            //list of april tags that it detected?
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
            //not sure, either .size means the length in meters, or Id, or the length of the list
            if(currentDetections.size() != 0)
            {
                //resets value for use in the next steps
                boolean tagFound = false;
                //runs the the exact amount of times as there are april tags detcted
                for(AprilTagDetection tag : currentDetections)
                {
                    //if the tag is of of the wanted tags, then we found what we are looking for!
                    if(tag.id == Left || tag.id == Middle || tag.id == Right)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }
                //if we found it, then put this text into the console and give info about the tag
                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                //if we did not find it, do this
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            //if no tags are seen
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }
            //update the info, since new info may have been found
            telemetry.update();
            //wait before checking for april tags again
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */
        //I'm assuming that the previous comment means that stop has been requested or something

        /* Update the telemetry */
        //if tag was found, edit info then update text
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("Warning: No tag was found! Last resort code will be ran for a 33% chance of succession :(");
            telemetry.update();
        }

        //deletable?
        /*frontleftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontrightMotor.setDirection(DcMotorSimple.Direction.REVERSE);*/

        // Autonumus Code               ///   MAIN SECTION   //////
        //The if statements see if certain tags are detected or if none are detected
        /*if(tagOfInterest == null || tagOfInterest.id == Left){
            //left code
            telemetry.addLine("Left Tag Scanned");
            telemetry.update();
            frontleftMotor.setPower(0);
            frontrightMotor.setPower(1);
            sleep(1000);
        }else if(tagOfInterest.id == Middle) {
            //middle code
            telemetry.addLine("Middle Tag Scanned");
            telemetry.update();
            frontleftMotor.setPower(1);
            frontrightMotor.setPower(1);
            sleep(1000);
        }else if(tagOfInterest.id == Right){
            //right code
            telemetry.addLine("Right Tag Scanned");
            telemetry.update();
            frontleftMotor.setPower(1);
            frontrightMotor.setPower(0);
            sleep(1000);
        }else {
            telemetry.addLine("No Tag Scanned");
            telemetry.update();
        }*/


        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        //while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        //This was commented out because it was causing errors. It might be cuz the pipeline is notCoves
        /*telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));*/
    }
    //does this have to be put somewhere else? (deletable comment)
    public int tagToId(){
        return tagOfInterest.id;
        //return AprilTagDetection tagOfInterest.id;
        //return 1;
    }
}