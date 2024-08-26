package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Gamepad;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/*
       ---------------------------------!Important!---------------------------------------------

 As the name suggests, this file is responsible for testing a linear slide

 To ensure a test goes smoothly, perform the following:

 1) Ensure all hardware issues are taken care of

 2) Connect the new motor powering the linear slide to a free port

 3) Configure the robot (if unsure, read the text file "Configuring" in the github)
           - Make sure you do not override any other ports or motors

 4) Name the motor "LinearSlideMotor" under the corresponding port

 5) Save and test it using the controller
           - To utilize the controller, press [Start Button] + B and toggle the left joystick up and down

 Feel free to compress this comment once you are done, and modify any values as you please
*/

@Autonomous(name="VerticalMotor-LinearSlideFile-Test", group ="Concept")
public class VerticalMotorLinearSlideFile extends LinearOpMode
{
    ElapsedTime runtime = new ElapsedTime();    // Use to determine when end game is starting.

    private DcMotor VerticalMotorLinearSlideLeft = null;
    private DcMotor VerticalMotorLinearSlideRight = null;

    @Override
    public void runOpMode()
    {


        //Hardware mapping (configuring) will connect to here
        VerticalMotorLinearSlideLeft = hardwareMap.get(DcMotor.class, "LinearSlideMotorLeft"); //added 7/24/24
        VerticalMotorLinearSlideRight = hardwareMap.get(DcMotor.class, "LinearSlideMotorRight");

        VerticalMotorLinearSlideLeft.setDirection(DcMotor.Direction.FORWARD);
        VerticalMotorLinearSlideRight.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData(">", "Press Start");
        telemetry.addData(">","Both vertical slides will be extended upwards, then retracted after 3 seconds");
        telemetry.update();

        waitForStart();
        runtime.reset();    // Start game timer.

        // Loop while monitoring buttons for rumble triggers
        while (opModeIsActive())
        {

            VerticalMotorLinearSlideLeft.setPower(1);
            VerticalMotorLinearSlideRight.setPower(1);
            sleep(3000);
            VerticalMotorLinearSlideLeft.setPower(-1);
            VerticalMotorLinearSlideRight.setPower(-1);

            telemetry.addData(">", "Code Finished");
            telemetry.update();
            sleep(5000);
        }
    }
}
