
package org.firstinspires.ftc.teamcode;
/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

//file imported here!!!
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.OpenCV.compCam;
import org.firstinspires.ftc.teamcode.compDrive;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

/**
 * This OpMode opens a claw when a is pressed, then closes when b is pressed
 * The code is structured as a LinearOpMode
 * INCREMENT sets how much to increase/decrease the servo position each cycle
 * CYCLE_MS sets the update period.
 *
 * This code assumes a Servo configured with the name "left_hand" as is found on a Robot.
 *
 * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
 * connected servos are able to move freely before running this test.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "autoMainR1E", group = "Linear OpMode")
//@Disabled
public class autoMainR1 extends LinearOpMode {

    // Driver Code
    //defining variables
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    OpenCvCamera camera;
    private double[] dblPowerList={0.5, 0.5, 0.5, 0.5};
    //compDrive drive1= new compDrive(hardwareMap);


    @Override
    public void runOpMode() {

        //hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        //compCam compCam;

        // Wait for the start button
        telemetry.addData(">", "Status: Initialized" );
        telemetry.update();
        waitForStart();
        runtime.reset();
        //option1
        compCam tagID = new compCam(hardwareMap);
        compDrive drive1 = new compDrive(hardwareMap);
        //The auto code
        while (opModeIsActive()) {
            //if (tagID.tagToId()==4){
            //frontLeftDrive.setPower(0.5);
            //drive %50 power left
            //drive1.moveLeft(12, dblPowerList);//
            //tells us (in the driver hub) if this loop ran
            telemetry.addData("Op mode", "is active");
            telemetry.update();
            sleep(2500);

            //sets direction
            frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); //should go inward (REVERSE)
            frontRightDrive.setDirection(DcMotor.Direction.FORWARD); //go outward (FORWARD)
            backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //should go inward (FORWARD)
            backRightDrive.setDirection(DcMotor.Direction.REVERSE); //go outward (REVERSE)

            //sets how far we want to drive
            frontLeftDrive.setTargetPosition(120);
            backLeftDrive.setTargetPosition(120);
            frontRightDrive.setTargetPosition(120);
            backRightDrive.setTargetPosition(120);

            //drives to the set position
            frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //sets power and decides how fast we travel towards the set position
            frontLeftDrive.setPower(0.5);
            frontRightDrive.setPower(0.5);
            backLeftDrive.setPower(0.5);
            backRightDrive.setPower(0.5);

            //waits a couple seconds before powering the wheels off
            sleep(2500);

            frontLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            backRightDrive.setPower(0);


        }
        /*compCam tagID = new compCam();
        while (opModeIsActive()) {
            if (tagID==1){
                frontLeftDrive.setPower(0.1);}
        }*/
        // Send calculated power to wheels


        // Telemetry
        // Claw Code
        // Display the current value

    }

    // Signal done;


}
