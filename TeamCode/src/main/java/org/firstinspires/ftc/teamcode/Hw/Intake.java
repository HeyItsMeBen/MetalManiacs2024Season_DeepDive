package org.firstinspires.ftc.teamcode.Hw;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name = "Intake", group = "OpMode")
public class Intake extends OpMode {


    public DcMotor arm = null;
    private Servo armServo = null;
    private Servo armPivotServo = null;

    private PIDController armController;
    public static double p = 0.005, i = 0., d = 0.00075;
    public static double f = 0;

    public static int armtarget = 0;

    public double pivotTarget = 0.5;


    public void init() {
        //arm Subsystem
        arm = hardwareMap.get(DcMotor.class, "arm");
        armServo = hardwareMap.get(Servo.class, "intakeClawServo");
        armPivotServo = hardwareMap.get(Servo.class, "intakePivotServo");

        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armController = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void loop() {
       armRetract();

       if (gamepad1.x){
           setArmPivotServoOut();
       }

       if (gamepad1.b){
           setArmPivotServoBack();
       }

       if (gamepad1.right_bumper){
           armServoOpen(0.35);
       }

       if (gamepad1.left_bumper){
           armServoClose();
       }
    }

    public void armRetract() {
        armController.setPID(p, i, d);
        double ticks_in_degree = 537.7 / 360;
        int armPos = arm.getCurrentPosition();
        double armPID = armController.calculate(armPos, armtarget);
        double armFF = Math.cos(Math.toRadians(armtarget / ticks_in_degree)) * f;

        double armpower = armPID + armFF;

        arm.setPower(armpower * 0.75);

        telemetry.addData("armPos", armPos);
        telemetry.addData("armTarget", armtarget);
        telemetry.update();
    }

    public void armServoOpen(double pos){
        armServo.setPosition(pos);
    }

    public void armServoClose(){
        armServo.setPosition(0.0425);
    }

    public void setArmPivotServoOut(){
        pivotTarget += 0.15;
        armPivotServo.setPosition(pivotTarget);
    }

    public void setArmPivotServoBack(){
        armPivotServo.setPosition(0.5);
    }
}
