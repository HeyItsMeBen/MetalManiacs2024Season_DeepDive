//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//
///* Copyright (c) 2017 FIRST. All rights reserved.
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
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
///**
// * This OpMode opens a claw when a is pressed, then closes when b is pressed
// * The code is structured as a LinearOpMode
// * INCREMENT sets how much to increase/decrease the servo position each cycle
// * CYCLE_MS sets the update period.
// *
// * This code assumes a Servo configured with the name "left_hand" as is found on a Robot.
// *
// * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
// * connected servos are able to move freely before running this test.
// *
// * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
// * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
// */
//@Autonomous(name = "SpiderCompAuto", group = "Linear OpMode")
////@Disabled
//public class SpiderCompAuto extends LinearOpMode {
//
//    // Driver Code
//    private ElapsedTime runtime = new ElapsedTime();
//    private DcMotor frontLeftDrive = null;
//    private DcMotor backLeftDrive = null;
//    private DcMotor frontRightDrive = null;
//    private DcMotor backRightDrive = null;
//
//
//    @Override
//    public void runOpMode() {
//
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
//        // Wait for the start button
//        telemetry.addData(">", "Status: Initialized" );
//        telemetry.update();
//        waitForStart();
//        runtime.reset();
//        while (opModeIsActive()) {
//            // Drive Code
//            //forward movement
//              /*  frontLeftDrive.setPower(.5);
//                frontRightDrive.setPower(.5);
//                backLeftDrive.setPower(.5);
//                backRightDrive.setPower(.5);
//                sleep(1000);
//                frontLeftDrive.setPower(.5);
//                frontRightDrive.setPower(.5);
//                backLeftDrive.setPower(.5);
//                backRightDrive.setPower(.5);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(0);
//                sleep(1000);*/
//
//            //backward movement
//            frontLeftDrive.setPower(-.25);
//            frontRightDrive.setPower(-.25);
//            backLeftDrive.setPower(-.25);
//            backRightDrive.setPower(-.25);
//            sleep(1000);
//            frontLeftDrive.setPower(-.25);
//            frontRightDrive.setPower(-.25);
//            backLeftDrive.setPower(-.25);
//            backRightDrive.setPower(-.25);
//            sleep(1000);
//            frontLeftDrive.setPower(0);
//            frontRightDrive.setPower(0);
//            backLeftDrive.setPower(0);
//            backRightDrive.setPower(0);
//            sleep(1000);
//
//            //diagonal upper right
//                /*frontLeftDrive.setPower(.5);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(.5);
//                sleep(1000);
//                frontLeftDrive.setPower(-.5);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(-.5);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(0);
//                sleep(1000);*/
//
//                /*diagonal upper left
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(.5);
//                backLeftDrive.setPower(.5);
//                backRightDrive.setPower(0);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(-.5);
//                backLeftDrive.setPower(-.5);
//                backRightDrive.setPower(0);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(0);
//                sleep(1000);*/
//
//                  /*diagonal backwards right
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(-.5);
//                backLeftDrive.setPower(-.5);
//                backRightDrive.setPower(0);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(-.5);
//                backLeftDrive.setPower(-.5);
//                backRightDrive.setPower(0);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(0);
//                sleep(1000);*/
//
//                /*diagonal backwards left
//                frontLeftDrive.setPower(-0.5);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(-0.5);
//                sleep(1000);
//                frontLeftDrive.setPower(-0.5);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(-0.5);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(0);
//                sleep(1000);*/
//
//                //right
//               /*frontLeftDrive.setPower(0.25);
//                frontRightDrive.setPower(-0.25);
//                backLeftDrive.setPower(-0.25);
//                backRightDrive.setPower(0.25);
//                sleep(1000);
//                frontLeftDrive.setPower(0.25);
//                frontRightDrive.setPower(-0.25);
//                backLeftDrive.setPower(-0.25);
//                backRightDrive.setPower(0.25);
//                sleep(1000);
//                frontLeftDrive.setPower(0);
//                frontRightDrive.setPower(0);
//                backLeftDrive.setPower(0);
//                backRightDrive.setPower(0);
//                sleep(1000);*/
//
//            //left
//           frontLeftDrive.setPower(-0.75);
//            frontRightDrive.setPower(0.75);
//            backLeftDrive.setPower(0.75);
//            backRightDrive.setPower(-0.75);
//            sleep(1000);
//            frontLeftDrive.setPower(-0.75);
//            frontRightDrive.setPower(0.75);
//            backLeftDrive.setPower(0.75);
//            backRightDrive.setPower(-0.75);
//            sleep(1000);
//            frontLeftDrive.setPower(0);
//            frontRightDrive.setPower(0);
//            backLeftDrive.setPower(0);
//            backRightDrive.setPower(0);
//            sleep(1000);
//
//        }
//        // Send calculated power to wheels
//
//
//        // Telemetry
//        // Claw Code
//        // Display the current value
//
//    }
//
//    // Signal done;
//
//
//}
