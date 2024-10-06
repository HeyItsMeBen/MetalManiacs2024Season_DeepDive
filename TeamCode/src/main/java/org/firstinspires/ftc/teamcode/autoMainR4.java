
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
import com.qualcomm.robotcore.util.ElapsedTime;
//

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
//trigger code
@Autonomous(name = "autoMainR4", group = "Linear OpMode")
//@Disabled
public class autoMainR4 extends LinearOpMode {

    // Driver Code
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private double[] dblPowerList={0.25, 0.25, 0.25, 0.25}; //MOTOR POWERS

    @Override
    public void runOpMode() {

        // Driver Code
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
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
        //compCam tagID = new compCam(hardwareMap);
        compDrive drive1 = new compDrive(hardwareMap);
        int intTestMode = 0; //0 IS NOT IN TEST MODE

        //*******THIS MUST BE CHANGED to 0, 1, 2, 3 *********//
        //*******Depending on what team wants to test *******//
        intTestMode = 2; //This must be changed depending on what team is testing

        while (opModeIsActive()) {
            //if (tagID.tagToId()==4){
            //frontLeftDrive.setPower(0.5);
            //drive %50 power left
            //drive1.moveLeft(12, dblPowerList);//
            telemetry.addData("Op mode", "is active");
            telemetry.update();
            sleep(2500);
            if (intTestMode == 0) {
                //ACTUAL COMPETITION PATH
                telemetry.addData("COMP PATH", "TEST MODE 0. Sleep 1 seconds.");
                telemetry.update();
                sleep(1000);
            }
            if (intTestMode==1) {
                //Move Forward
                frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); //should go inward (REVERSE)
                frontRightDrive.setDirection(DcMotor.Direction.FORWARD); //go outward (FORWARD)
                backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //should go inward (FORWARD)
                backRightDrive.setDirection(DcMotor.Direction.REVERSE); //go outward (REVERSE)

                telemetry.addData("Op mode", "Set Direction");
                telemetry.update();
                telemetry.addData("Op mode", "TEST MODE 1. Sleep 2 seconds.");
                telemetry.update();
                sleep(2000);

                frontLeftDrive.setTargetPosition(100);
                backLeftDrive.setTargetPosition(100);
                frontRightDrive.setTargetPosition(100);
                backRightDrive.setTargetPosition(100);

                frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                frontLeftDrive.setTargetPosition(100);
                backLeftDrive.setTargetPosition(100);
                frontRightDrive.setTargetPosition(100);
                backRightDrive.setTargetPosition(100);

                telemetry.addData("Op mode", "Set Target Position");
                telemetry.update();
                sleep(2000);

                frontLeftDrive.setPower(0.5);
                frontRightDrive.setPower(0.5);
                backLeftDrive.setPower(0.5);
                backRightDrive.setPower(0.5);

                sleep(2500);

                frontLeftDrive.setPower(0);
                frontRightDrive.setPower(0);
                backLeftDrive.setPower(0);
                backRightDrive.setPower(0);

            } else if (intTestMode == 2) {
                //TEST PATHS with Encoders using compDrive
                telemetry.addData("runCompDriveTest", "Set Target Position");
                telemetry.update();
                telemetry.addData("runCompDriveTest", "TEST MODE 2. Sleep 2 seconds.");
                telemetry.update();
                sleep(1000);


                //TEST 2 compDrive Code
                telemetry.addData("runCompDriveTest", "Move Forward 2 inches");
                telemetry.update();
                drive1.moveForward(2, dblPowerList); //Move Forward 2 inches

                sleep(1000); //Pause for 1 seconds
                telemetry.addData("runCompDriveTest", "Move Backward 2 inches");
                telemetry.update();
                drive1.moveBackward(2, dblPowerList); //Move Backward 2 inches
                sleep(1000); //Pause for 1 seconds

                telemetry.addData("runCompDriveTest", "Strafe Left 2 inches");
                telemetry.update();
                drive1.moveLeft(2, dblPowerList); //Strafe Left 2 inches
                sleep(1000); //Pause for 1 seconds

                telemetry.addData("runCompDriveTest", "Strafe Right 2 inches");
                telemetry.update();

                drive1.moveRight(2,dblPowerList); //Strafe Right 2 inches
                sleep(2000); //Pause for 2 seconds


                drive1.moveLowerLeft(2, dblPowerList); //Diagonal LowerLeft 2 inches
                sleep(2000); //Pause for 2 seconds
                drive1.moveLowerRight(2, dblPowerList); //Diagonal LowerRight 2 inches
                sleep(2000); //Pause for 2 seconds
                drive1.moveUpperLeft(2,dblPowerList); //Diagonal UpperLeft 2 inches
                sleep(2000); //Pause for 2 seconds
                drive1.moveUpperRight(2,dblPowerList); //Diagonal UpperRight 2 inches
                sleep(2000); //Pause for 2 seconds
                drive1.moveClockwiseTurn(2,dblPowerList); //Turn Clockwise for 2 inches long
                sleep(2000); //Pause for 2 seconds
                drive1.moveCounterClockwiseTurn(2,dblPowerList); //Turn CounterClockwise for 3 inches long
                sleep(2000); //Pause for 2 seconds*/
            }
            else if (intTestMode == 3){
                //TEST PATHS with WITHOUT ENCODERS using compDrive
                telemetry.addData("runCompDriveWOEncoderTest", "NO ENCODERS USED");
                telemetry.update();
                sleep(1000);

//                drive1.moveForwardWOEncoders(dblPowerList); //(using time
//                sleep(2000); //run 2 seconds
//                drive1.stopMotorWOEncoders(); //STOP MOTORS
//                drive1.moveBackwardWOEncoders(dblPowerList); //MOVE BACKWARDS (using time)
//                sleep(2000);
//                drive1.stopMotorWOEncoders();//STOP MOTORS


            }
        }
        /*compCam tagID = new compCam();
        while (opModeIsActive()) {f
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