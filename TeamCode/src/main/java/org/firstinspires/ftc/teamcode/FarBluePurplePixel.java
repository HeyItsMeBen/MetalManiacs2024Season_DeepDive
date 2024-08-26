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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/*
 * This sample demonstrates how to run analysis during INIT
 * and then snapshot that value for later use when the START
 * command is issued. The pipeline is re-used from BlockDeterminationHalves
 */
@Autonomous (name="Far Blue", group="a")
public class FarBluePurplePixel extends LinearOpMode
{
    public DcMotor frontLeftDrive = null;
    public DcMotor backLeftDrive = null;
    public DcMotor frontRightDrive = null;
    public DcMotor backRightDrive = null;
    double leftFront;
    double rightFront;
    double leftRear;
    double rightRear;
    double theta;
    double sin;
    double cos;
    static final double power = .6;
    double turn;
    double max;
    OpenCvWebcam webcam;
    BlockDeterminationHalves.SkystoneDeterminationPipeline pipeline;
    BlockDeterminationHalves.SkystoneDeterminationPipeline.SkystonePosition snapshotAnalysis = BlockDeterminationHalves.SkystoneDeterminationPipeline.SkystonePosition.LEFT; // default

    @Override
    public void runOpMode()
    {
        /**
         * NOTE: Many comments have been omitted from this sample for the
         * sake of conciseness. If you're just starting out with EasyOpenCv,
         * you should take a look at {@link InternalCamera1Example} or its
         * webcam counterpart, {@link WebcamExample} first.
         */

        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        sin = Math.sin(theta - Math.PI/4);
        cos = Math.cos(theta - Math.PI/4);
        max = Math.max(Math.abs(sin), Math.abs(cos));

        leftFront = power * cos/max + turn;
        rightFront = power * sin/max - turn;
        leftRear = power * sin/max + turn;
        rightRear = power * cos/max - turn;

        if ((power + Math.abs(turn)) > 1) {
            leftFront /= power + turn;
            rightFront /= power + turn;
            leftRear /= power + turn;
            rightRear /= power + turn;
        }

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new BlockDeterminationHalves.SkystoneDeterminationPipeline();
        webcam.setPipeline(pipeline);

        pipeline.setBlockColor(BlockDeterminationHalves.SkystoneDeterminationPipeline.ObjectColor.BLUEBLOCK);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });


        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            telemetry.addData("Realtime analysis", pipeline.getAnalysis());
            telemetry.addData("Realtime average 1", pipeline.getAverage1());
            telemetry.addData("Realtime average 2", pipeline.getAverage2());
            //telemetry.addData("Realtime average 3", pipeline.getAverage3());
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        /*
         * The START command just came in: snapshot the current analysis now
         * for later use. We must do this because the analysis will continue
         * to change as the camera view changes once the robot starts moving!
         */
        snapshotAnalysis = pipeline.getAnalysis();

        /*
         * Show that snapshot on the telemetry
         */
        telemetry.addData("Snapshot post-START analysis", snapshotAnalysis);
        telemetry.update();

        switch (snapshotAnalysis)
        {
            case LEFT:
            {
                /* Your autonomous code */
                telemetry.addData("path", "path 1 started");
                telemetry.update();

                //Left Spike Mark WORK IN PROGRESS
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(1400);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);


                //Turn Left 90 Degrees
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1000);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(250);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(250);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Arm Code for Purple Pixel Placement

                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1500);
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Drive Backwards
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(4600);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(leftRear);
                backRightDrive.setPower(rightRear);
                sleep(1400);
                //Stop for Parking
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Arm Code for Yellow Pixel Placement

                //Strafe Right
                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1300);
                //Stop for Parking
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                break;

            }

            case RIGHT:
            {
                /* Your autonomous code */
                telemetry.addData("path", "path 3 started");
                telemetry.update();

                //Right Spike Mark
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(1450);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Turn Right 90 Degrees
                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(leftRear);
                backRightDrive.setPower(rightRear);
                sleep(1000);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(250);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(250);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Turn Right 180 Degrees
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(2000);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1700);
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Drive Backwards
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(4800);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(leftRear);
                backRightDrive.setPower(rightRear);
                sleep(1600);
                //Stop for Parking
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Arm Code for Yellow Pixel Placement

                //Strafe Right
                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1300);
                //Stop for Parking
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Drive Backwards
               /** frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(4600);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Arm Code for Yellow Pixel Placement

                //Strafe Right
                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1300);
                //Stop for Parking
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);**/
                break;

            }

            case CENTER:
            {
                /* Your autonomous code*/
                telemetry.addData("path", "path 2 started");
                telemetry.update();
                //Center Spike Mark
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(1650);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(250);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Arm Code for Purple Pixel Placement

                //Turn Left 90 Degrees
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(-rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1150);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Drive Backwards
                frontLeftDrive.setPower(-leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(rightRear);
                sleep(4600);
                //Stop
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                //Arm Code for Yellow Pixel Placement

                //Strafe Right
                frontLeftDrive.setPower(leftFront);
                frontRightDrive.setPower(rightFront);
                backLeftDrive.setPower(-leftRear);
                backRightDrive.setPower(-rightRear);
                sleep(1300);
                //Stop for Parking
                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);
                break;
            }
        }

        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive())
        {
            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }
    }
}