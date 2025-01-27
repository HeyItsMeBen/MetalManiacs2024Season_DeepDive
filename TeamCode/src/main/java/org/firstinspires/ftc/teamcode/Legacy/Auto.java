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

package org.firstinspires.ftc.teamcode.Legacy;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

/*
 * This sample demonstrates how to run analysis during INIT
 * and then snapshot that value for later use when the START
 * command is issued. The pipeline is re-used from SkystoneDeterminationExample
 */
@Disabled
@Autonomous
public class Auto extends LinearOpMode
{
    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;

    public DcMotor arm = null;
    static final double FORWARD_SPEED = 0.6;
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
    //SkystoneDeterminationExample.SkystoneDeterminationPipeline pipeline;
    //SkystoneDeterminationExample.SkystoneDeterminationPipeline.SkystonePosition snapshotAnalysis = SkystoneDeterminationExample.SkystoneDeterminationPipeline.SkystonePosition.LEFT; // default

    @Override
    public void runOpMode()
    {
        /**
         * NOTE: Many comments have been omitted from this sample for the
         * sake of conciseness. If you're just starting out with EasyOpenCv,
         * you should take a look at {@link InternalCamera1Example} or its
         * webcam counterpart, {@link WebcamExample} first.
         */

        leftDrive = hardwareMap.get(DcMotor.class, "Left"); // "FrontLeftWheel" matching with driver station configuration
        rightDrive = hardwareMap.get(DcMotor.class, "Right");
        arm = hardwareMap.get(DcMotor.class, "Arm");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        waitForStart();
        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive())
        {
            leftDrive.setPower(5);
            rightDrive.setPower(4);

            sleep(10000);

            leftDrive.setPower(-1);
            rightDrive.setPower(12);

            sleep(5000);

            leftDrive.setPower(5);
            rightDrive.setPower(5);

            sleep(1000);

            leftDrive.setPower(-5);
            rightDrive.setPower(-5);

            sleep(1000);

            leftDrive.setPower(12);
            rightDrive.setPower(-1);

            sleep(2500);

            leftDrive.setPower(-5);
            rightDrive.setPower(-5);

            break;
            //leftDrive.setPower(2);
            //rightDrive.setPower(2);
        }
    }
}