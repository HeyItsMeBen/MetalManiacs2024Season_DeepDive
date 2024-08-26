// Note: Port 2 on expansion hub wil be modified to Port 5. Make sure to change back later.

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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

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
@TeleOp(name = "Final Drive Code", group = "a")
//@Disabled
public class Merged_Intake_And_Mecanum extends LinearOpMode {

    // Driver Code
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor Intake   = null;
    private DcMotor LinearSlide = null;
    private DcMotor Arm1 = null;
    private DcMotor Arm2 = null;
    private CRServo LeftServo = null;
    private CRServo RightServo = null;
    private Servo DroneLaunch = null;
    private Servo DeployIntake = null;

    @Override
    public void runOpMode() {

        // Driver Code
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeftWheel");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BackLeftWheel");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightWheel");
        backRightDrive = hardwareMap.get(DcMotor.class, "BackRightWheel");
        // set direction for motors
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Press start+b on gamepad","");
        telemetry.addData("To use Intake Motor: Press y for forward power, Press a for reverse power","");
        telemetry.addData("To use Linear Slide Motor: Left joystick up/down","");
        telemetry.addData("To use Arm Motor: Right joystick up/down","");
        telemetry.addData("To use Box Servos: Press left trigger to move up, Press right trigger to move down","");
        telemetry.update();
        //Hardware Mapping
        DeployIntake = hardwareMap.get(Servo.class, "intakemove");
        Intake  = hardwareMap.get(DcMotor.class, "Intake");
        LinearSlide = hardwareMap.get(DcMotor.class, "Linear_Slide");
        Arm1 = hardwareMap.get(DcMotor.class, "Arm_1_Motor");
        Arm2 = hardwareMap.get(DcMotor.class, "Arm_2_Motor");
        LeftServo = hardwareMap.get(CRServo.class, "Left_Servo"); //still need to configure
        RightServo = hardwareMap.get(CRServo.class, "Right_Servo"); //still need to configure
        // DroneLaunch = hardwareMap.get(Servo.class, "DroneLaunch_Servo");
        //DroneLaunch.setPosition(0);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Drive Code
            double max;
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;
            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;
            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));
            //max = POWER_REDUCTION*max; //Reduces power to slow down robot. This can be modified to increase or reduce robot speed by will.
            if (max > 1.5) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }
            // Send calculated power to wheels
            frontLeftDrive.setPower(leftFrontPower);
            frontRightDrive.setPower(rightFrontPower);
            backLeftDrive.setPower(leftBackPower);
            backRightDrive.setPower(rightBackPower);

            //Intake System
            while (gamepad1.x) {
                DeployIntake.setPosition(3);
            }
            while (gamepad1.b) {
                DeployIntake.setPosition(0);
            }
            while (gamepad1.left_trigger > 0) {
                Intake.setPower(1.0);
            }
            while (gamepad1.left_bumper) {
                Intake.setPower(1.0);
                frontLeftDrive.setPower(0.25);
                frontRightDrive.setPower(0.25);
                backLeftDrive.setPower(0.25);
                backRightDrive.setPower(0.25);
            }
            while (gamepad1.right_trigger > 0) {
                Intake.setPower(-1.0);
            }
            while (gamepad1.right_bumper) {
                Intake.setPower(-1.0);
                frontLeftDrive.setPower(-0.25);
                frontRightDrive.setPower(-0.25);
                backLeftDrive.setPower(-0.25);
                backRightDrive.setPower(-0.25);
            }
            Intake.setPower(0);
            //LinearSlide
            while (gamepad2.left_stick_y > 0) {
                LinearSlide.setPower(0.5);
            }
            while (gamepad2.left_stick_y < 0) {
                LinearSlide.setPower(-0.5);
            }
            LinearSlide.setPower(0);
            //Arm
            while (gamepad2.right_stick_y > 0) {
                Arm1.setPower(0.4);
                Arm2.setPower(-0.4);
            }
            while (gamepad2.right_stick_y < 0) {
                Arm1.setPower(-0.4);
                Arm2.setPower(0.4);
            }
            Arm1.setPower(0);
            Arm2.setPower(0);
            //Servo
            while (gamepad2.left_trigger > 0) {
                LeftServo.setPower(.25);
                RightServo.setPower(-.25);
            }
            while (gamepad2.right_trigger > 0) {
                LeftServo.setPower(-.25);
                RightServo.setPower(.25);
            }
            LeftServo.setPower(0);
            RightServo.setPower(0);
            //Launches Drone
            if (gamepad2.b) {
                DroneLaunch.setPosition(-1);
            }

            idle();
        }

        // Signal done;

        telemetry.addData(">", "Done");
        telemetry.update();
    }
}