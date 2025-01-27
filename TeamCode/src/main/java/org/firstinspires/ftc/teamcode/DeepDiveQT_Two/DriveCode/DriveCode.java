package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DeepDiveQT_Two.DriveCode.DriveCode;

@TeleOp(name = "QTDriveCode", group = "Linear OpMode")

public class DriveCode extends LinearOpMode {

    // Driver Code
    public GamepadEx driver;
    public GamepadEx operator;

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    public DcMotor arm = null;

    private Servo armServo = null;
    private Servo armPivotServo = null;

    private PIDController armController;
    private static double p = 0.002, i = 0.1, d = 0.005;
    private static double f = 0.03;

    //Outtake subsystem
    private DcMotor leftSlide = null;
    private DcMotor rightSlide = null;

    private Servo outtakeClawServo = null;
    private Servo slideLeftServo = null;
    private Servo slideRightServo = null;

    private PIDController slideController;
    //private FinalDriveCodeTest DriveCode = new FinalDriveCodeTest();

    private static double Kp = 0, Ki = 0, Kd = 0, Kf = 0;


    //Gobilda 202 19.2:1
    private final double ticks_in_degree = 537.7/360;

    int currpos = 1;

    //pick up arm servo pos
    double STATE_1[] = {1,0};

    //Stand-by arm servo pos
    double STATE_2[] = {0.85,.15};

    //ready to score arm servo pos
    double STATE_3[] = {0,1};

    //Scored arm servo pos
    double STATE_4[] = {0.15,0.85};

    // Note: pushing stick forward gives negative value

    @Override
    public void runOpMode() {

        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);

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

        //arm and slides needs to run with encoder
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //arm Subsystem
        arm = hardwareMap.get(DcMotor.class, "arm");
        armServo = hardwareMap.get(Servo.class, "intakeClawServo");
        armPivotServo = hardwareMap.get(Servo.class, "intakePivotServo");

        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //Outtake Subsystem
        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        slideLeftServo = hardwareMap.get(Servo.class, "leftOuttake");
        slideRightServo = hardwareMap.get(Servo.class, "rightOuttake");
        outtakeClawServo = hardwareMap.get(Servo.class, "outtakeServo");

        leftSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rightSlide.setDirection(DcMotorSimple.Direction.FORWARD);

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //Set Servos to stand-by
        neutralOuttakeArmState();
        //Set pivot to neutral
        setArmPivotServoBack();

        telemetry.addData(">", "Status: Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Drive Code
            double x = gamepad1.left_stick_x * 0.8;
            double y = -gamepad1.left_stick_y * 0.8;
            double turn = gamepad1.right_stick_x * 0.8;

            double theta = Math.atan2(y, x);
            double power = Math.hypot(x, y);
            double sin = Math.sin(theta - Math.PI/4);
            double cos = Math.cos(theta - Math.PI/4);
            double max = Math.max(Math.abs(sin), Math.abs(cos));

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = power * cos/max + turn;
            double rightFrontPower = power * sin/max - turn;
            double leftBackPower = power * sin/max + turn;
            double rightBackPower = power * cos/max - turn;

            if ((power + Math.abs(turn)) > 1){
                leftFrontPower /= power + turn;
                rightFrontPower /= power + turn;
                leftBackPower /= power + turn;
                rightBackPower /= power + turn;
            }

            // Send calculated power to wheels
            frontLeftDrive.setPower(leftFrontPower);
            frontRightDrive.setPower(rightFrontPower);
            backLeftDrive.setPower(leftBackPower);
            backRightDrive.setPower(rightBackPower);

            if (gamepad1.left_trigger > 0.01){
                arm.setPower(gamepad1.left_trigger);
            } else {
                arm.setPower(0);
            }
            if (gamepad1.right_trigger > 0.01){
                arm.setPower(-gamepad1.right_trigger);
            } else {
                arm.setPower(0);
            }
            // arm claw open
            if (driver.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
                armServoOpen(0.4);
            }
            // arm claw close
            if (driver.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                armServoClose();
            }
            //Makes intake pivot go out 0.15 so variable is now 0.65
            if (driver.getButton(GamepadKeys.Button.X)) {
                setArmPivotServoOut();
            }
            //Brings pivot back to 0.5
            if (driver.getButton(GamepadKeys.Button.B)){
                setArmPivotServoBack();
            }
            //brings arm back and allows for it to be picked up by outtake arm
            if (driver.getButton(GamepadKeys.Button.A)){
                armRetract(0);
            }
            // Moves slides up to basket
            if (operator.getButton(GamepadKeys.Button.DPAD_UP)){
                slidesMove(-10 * 122);
            }
            //Moves Slides down
            if (operator.getButton(GamepadKeys.Button.DPAD_DOWN)) {
                slidesMove(0);
            }
            // slide arm claw open
            if (operator.getButton(GamepadKeys.Button.RIGHT_BUMPER)){
                outtakeServoOpen();
            }
            // arm claw close

            if (operator.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                outtakeServoClose();
            }

            if (operator.getButton(GamepadKeys.Button.X)){
                neutralOuttakeArmState();
            }

            if (operator.getButton(GamepadKeys.Button.Y)){
                increaseOuttakeArmState();
            }
            if (operator.getButton(GamepadKeys.Button.A)){
                decreaseOuttakeArmState();
            }

            //when OpMode is Active
        }
        //Run OpMode
    }
    public void armRetract(int armtarget) {
        armController.setPID(p, i, d);
        armtarget = armtarget/5;
        for (int i =0; i < 5; i++) {
            int armPos = arm.getCurrentPosition();
            double armPID = armController.calculate(armPos, armtarget);
            double armFF = Math.cos(Math.toRadians(armtarget / ticks_in_degree)) * f;

            double armpower = armPID + armFF;

            arm.setPower(armpower);

            telemetry.addData("armPos", armPos);
            telemetry.addData("armTarget", armtarget);
            telemetry.update();

//            DriveCode.xx = gamepad1.left_stick_x * 0.8;
//            DriveCode.yy = -gamepad1.left_stick_y * 0.8;
//            DriveCode.turn = gamepad1.right_stick_x * 0.8;

            armtarget = armtarget + armtarget/5;
        }
        currpos = 1;
        checkOuttakeArmState();
        outtakeServoClose();
        armServoOpen(0.2);

    }
    public void armServoOpen(double pos){
        armServo.setPosition(pos);
    }

    public void armServoClose(){
        armServo.setPosition(0.025);
    }

    public void setArmPivotServoOut(){
        armPivotServo.setPosition(0.65);
    }

    public void setArmPivotServoBack(){
        armPivotServo.setPosition(0.5);
    }
    public void slidesMove(int slidetargetOG) {

        slideController.setPID(Kp, Ki, Kd);

        int slidetarget = slidetargetOG;
        slidetarget = slidetarget/7;

        for (int i =0; i < 7; i++) {
            int slidePos = leftSlide.getCurrentPosition();
            double slidePID = slideController.calculate(slidePos, slidetarget);
            double slideFF = Math.cos(Math.toRadians(slidetarget / ticks_in_degree)) * Kf;

            double slidePower = slidePID + slideFF;

            leftSlide.setPower(slidePower);
            rightSlide.setPower(slidePower);

            telemetry.addData("slidePos", slidePos);
            telemetry.addData("slideTarget", slidetarget);
            telemetry.update();

//            DriveCode.xx = gamepad1.left_stick_x * 0.8;
//            DriveCode.yy = -gamepad1.left_stick_y * 0.8;
//            DriveCode.turn = gamepad1.right_stick_x * 0.8;

            slidetarget = slidetarget + slidetargetOG/7;
        }
    }

    public void outtakeServoOpen(){
        outtakeClawServo.setPosition(0.4);
    }

    public void outtakeServoClose(){
        outtakeClawServo.setPosition(0.025);
    }

    public void increaseOuttakeArmState(){
        currpos = currpos + 1;
        checkOuttakeArmState();
    }

    public void decreaseOuttakeArmState(){
        currpos = currpos - 1;
        checkOuttakeArmState();
    }

    public void checkOuttakeArmState(){
        switch (currpos){
            case 1:
                slideLeftServo.setPosition(STATE_1[0]);
                slideRightServo.setPosition(STATE_1[1]);
                break;
            case 2:
                slideLeftServo.setPosition(STATE_2[0]);
                slideRightServo.setPosition(STATE_2[1]);
                break;
            case 3:
                slideLeftServo.setPosition(STATE_3[0]);
                slideRightServo.setPosition(STATE_3[1]);
                break;
            case 4:
                slideLeftServo.setPosition(STATE_4[0]);
                slideRightServo.setPosition(STATE_4[1]);
                break;
        }
    }

    public void neutralOuttakeArmState(){
        currpos = 2;
        checkOuttakeArmState();
    }
    //end
}