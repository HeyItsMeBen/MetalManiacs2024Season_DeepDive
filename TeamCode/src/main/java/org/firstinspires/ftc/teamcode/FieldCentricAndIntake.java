package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.xyzOrientation;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


/**
 * The field centric driving assumes that an IMU is configured on the robot with the name "imu".
 *
 *   Pitch value should INCREASE as the robot is tipped UP at the front. (Rotation about X)
 *   Roll value should INCREASE as the robot is tipped UP at the left side. (Rotation about Y)
 *   Yaw value should INCREASE as the robot is rotated Counter Clockwise. (Rotation about Z)
 *
 * The yaw can be reset (to zero) by pressing the X button on the gamepad.
 *
 * The code allows the control hub to be mounted however we like as long as we specify how an
 * orthogonal control hub becomes transforms to a non-orthogonal control hub.
 */
@TeleOp(name = "Mecanum Drive with Intake", group = "Sensor")
@Disabled
public class FieldCentricAndIntake extends LinearOpMode {

    //Drive variables
    IMU imu; // The IMU sensor object
    private DcMotor FrontLeft = null;
    private DcMotor FrontRight = null;
    private DcMotor BackRight = null;
    private DcMotor BackLeft = null;

    //Intake variables
    private DcMotor Intake = null;

    //----------------------------------------------------------------------------------------------
    // Main logic
    //----------------------------------------------------------------------------------------------

    @Override
    public void runOpMode() throws InterruptedException {

        // these are the mecanum wheel motors
        FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        FrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        BackLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        BackRight = hardwareMap.get(DcMotor.class, "BackRight");

        Intake = hardwareMap.get(DcMotor.class, "Intake"); // the intake system motor

        // Retrieve and initialize the IMU.
        // This sample expects the IMU to be in a REV Hub and named "imu".
        imu = hardwareMap.get(IMU.class, "imu");

        telemetry.addData("Say", "Press Play");    // inform the player that the robot is ready to play
        telemetry.update();

        waitForStart(); // wait for the player to press the start button

//DRIVE CODE

        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        // This button choice was made so that it is hard to hit on accident,
        // it can be freely changed based on preference.
        // The equivalent button is start on Xbox-style controllers.
        if (gamepad1.options) {
            imu.resetYaw();
        }

        /* Define how the hub is mounted to the robot to get the correct Yaw, Pitch and Roll values.
         *
         * You can apply up to three axis rotations to orient your Hub according to how it's mounted on the robot.
         *
         * The starting point for these rotations is the "Default" Hub orientation, which is:
         * 1) Hub laying flat on a horizontal surface, with the Printed Logo facing UP
         * 2) Rotated such that the USB ports are facing forward on the robot.
         *
         * The order that the rotations are performed matters, so this sample shows doing them in the order X, Y, then Z.
         * For specifying non-orthogonal hub mounting orientations, we must temporarily use axes
         * defined relative to the Hub itself, instead of the usual Robot Coordinate System axes
         * used for the results the IMU gives us. In the starting orientation, the Hub axes are
         * aligned with the Robot Coordinate System:
         *
         * X Axis:  Starting at Center of Hub, pointing out towards I2C connectors
         * Y Axis:  Starting at Center of Hub, pointing out towards USB connectors
         * Z Axis:  Starting at Center of Hub, pointing Up through LOGO
         *
         * Positive rotation is defined by right-hand rule with thumb pointing in +ve direction on axis.
         *
         * Some examples.
         *
         * ----------------------------------------------------------------------------------------------------------------------------------
         * Example A) Assume that the hub is mounted on a sloped plate at the back of the robot, with the USB ports coming out the top of the hub.
         *  The plate is tilted UP 60 degrees from horizontal.
         *
         *  To get the "Default" hub into this configuration you would just need a single rotation.
         *  1) Rotate the Hub +60 degrees around the X axis to tilt up the front edge.
         *  2) No rotation around the Y or Z axes.
         *
         *  So the X,Y,Z rotations would be 60,0,0
         *
         * ----------------------------------------------------------------------------------------------------------------------------------
         * Example B) Assume that the hub is laying flat on the chassis, but it has been twisted 30 degrees towards the right front wheel to make
         *  the USB cable accessible.
         *
         *  To get the "Default" hub into this configuration you would just need a single rotation, but around a different axis.
         *  1) No rotation around the X or Y axes.
         *  1) Rotate the Hub -30 degrees (Clockwise) around the Z axis, since a positive angle would be Counter Clockwise.
         *
         *  So the X,Y,Z rotations would be 0,0,-30
         *
         * ----------------------------------------------------------------------------------------------------------------------------------
         *  Example C) Assume that the hub is mounted on a vertical plate on the right side of the robot, with the Logo facing out, and the
         *  Hub rotated so that the USB ports are facing down 30 degrees towards the back wheels of the robot.
         *
         *  To get the "Default" hub into this configuration will require several rotations.
         *  1) Rotate the hub +90 degrees around the X axis to get it standing upright with the logo pointing backwards on the robot
         *  2) Next, rotate the hub +90 around the Y axis to get it facing to the right.
         *  3) Finally rotate the hub +120 degrees around the Z axis to take the USB ports from vertical to sloping down 30 degrees and
         *     facing towards the back of the robot.
         *
         *  So the X,Y,Z rotations would be 90,90,120
         */

        // The next three lines define the desired axis rotations.
        // To Do: EDIT these values to match YOUR mounting configuration.
        double xRotation = 0;  // enter the desired X rotation angle here.
        double yRotation = 0;  // enter the desired Y rotation angle here.
        double zRotation = 0;  // enter the desired Z rotation angle here.


        Orientation hubRotation = xyzOrientation(xRotation, yRotation, zRotation);

        // Now initialize the IMU with this mounting orientation
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(hubRotation);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        // Loop and update the dashboard
        while (!isStopRequested()) {
            telemetry.addData("Hub orientation", "X=%.1f,  Y=%.1f,  Z=%.1f \n", xRotation, yRotation, zRotation);
            double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
            double rx = gamepad1.right_stick_x;
            double x = gamepad1.left_stick_x * 1.1;

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            // calculate what the power each motor should be going to go forward relative to the field
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            // Set the motor direction
            FrontLeft.setDirection(DcMotor.Direction.REVERSE);
            FrontRight.setDirection(DcMotor.Direction.FORWARD);
            BackLeft.setDirection(DcMotor.Direction.FORWARD);
            BackRight.setDirection(DcMotor.Direction.REVERSE);

            // Check to see if heading reset is requested
            if (gamepad1.x) {
                telemetry.addData("Yaw", "Resetting\n");
                imu.resetYaw();
            } else {
                telemetry.addData("Yaw", "Press x on Gamepad to reset\n");
            }

            // Retrieve Rotational Angles and Velocities
            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
            AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

            FrontLeft.setPower(frontLeftPower);
            FrontRight.setPower(frontRightPower);
            BackRight.setPower(backRightPower);
            BackLeft.setPower(backLeftPower);

            // Send information to the player
            telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES));
            telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
            telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", angularVelocity.xRotationRate);
            telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", angularVelocity.yRotationRate);
            telemetry.addData("frontLeftMotor", "%.2f Power", frontLeftPower);
            telemetry.addData("backLeftMotor", "%.2f Power", backLeftPower);
            telemetry.addData("frontRightMotor", "%.2f Power", frontRightPower);
            telemetry.addData("backRightMotor", "%.2f Power", backRightPower);
            telemetry.addData("Skill Issue", "Grammalatte died and now she's Ghostlatte abandoned Kale Kabob Jerky Salad >:(");
            telemetry.update();

//INTAKE SYSTEM CODE

            //the following code is for the intake system
            while (gamepad1.y)
                Intake.setPower(0.25);
            Intake.setPower(0);

            while (gamepad1.a)
                Intake.setPower(-0.25);
            Intake.setPower(0);

        }
    }
}