//FILE PURPOSE: Should make the robot strafe using PID. I made this file by modifying a copy arm_PID, which was the only PID file that worked so far
package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name = "strafe_PID2_fromArm", group = "Linear OpMode")
public class strafe_PID2_fromArm extends OpMode {
    private PIDController controller;


    public static double p = 0, i = 0, d = 0;
    public static double f = 0;


    public static int target = 0;   //0-->1


    private final double ticks_in_degree = 700 / 180.0;

    //creates all four motors using the extended version of DcMotor
    private DcMotorEx frontLeftDrive;
    private DcMotorEx backLeftDrive;
    private DcMotorEx frontRightDrive;
    private DcMotorEx backRightDrive;


    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        //hardware-maps all four motors
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");

        //sets directions for the motors
        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    }


    @Override
    public void loop() {
        //sets up controller
        controller.setPID(p, i, d);

        //gets new wheel position
        int wheelPos = frontRightDrive.getCurrentPosition();

        //calculates PID and ff
        double pid = controller.calculate(wheelPos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        //calculates power. I commented out the ff variable because I'm pretty sure it's not needed and would actually make things worse.
        double power = pid;// + ff;

        //sends power to wheels
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(power);

        //adds telemetry data to graph
        telemetry.addData("pos", wheelPos);
        telemetry.addData("target", target);
        telemetry.update();
    }
}
