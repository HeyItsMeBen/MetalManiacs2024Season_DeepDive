package org.firstinspires.ftc.teamcode;

/* Copyright (c) 2019 FIRST. All rights reserved.
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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine which image is being presented to the robot.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Disabled
@TeleOp(name = "MecanumMathTest - Test Code", group = "Linear OpMode")
//@Disabled
public class NewMecanumDrivingCodeWithMath extends LinearOpMode {
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
    double power;
    double max;

    @Override
    public void runOpMode() {
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel"); // "FrontLeftWheel" matching with driver station configuration
        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {

            double x = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double y = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            theta = Math.atan2(y, x);
            power = Math.hypot(x, y);

            sin = Math.sin(theta - Math.PI / 4);
            cos = Math.cos(theta - Math.PI / 4);
            max = Math.max(Math.abs(sin), Math.abs(cos));

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = power * cos / max + turn;
            double rightFrontPower = power * sin / max - turn;
            double leftBackPower = power * sin / max + turn;
            double rightBackPower = power * cos / max - turn;
            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            if ((power + Math.abs(turn)) > 1) {
                leftFront /= power + turn;
                rightFront /= power + turn;
                leftRear /= power + turn;
                rightRear /= power + turn;        // Send calculated power to wheels
                frontLeftDrive.setPower(leftFrontPower);
                frontRightDrive.setPower(rightFrontPower);
                backLeftDrive.setPower(leftBackPower);
                backRightDrive.setPower(rightBackPower);

                // Telemetry
                // Claw Code
                // Display the current value
                //telemetry.addData("Servo Position 1 and 2", "%5.2f", position_1_2);
                //telemetry.addData("Servo Position 3", "%5.2f", position_3);
                //telemetry.update();
                // Driver Code
                // Show the elapsed game time and wheel power.
                //telemetry.addData("Status", "Run Time: " + runtime.toString());
                //telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
                //telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
                //telemetry.update();

                idle();


            }
        }
    }
}