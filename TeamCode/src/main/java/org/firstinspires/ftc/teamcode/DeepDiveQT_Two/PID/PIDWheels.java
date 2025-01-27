package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

@Autonomous(name = "PID Wheels", group = "Linear OpMode")
@Config
public class PIDWheels extends LinearOpMode {

    private DcMotor front_left, front_right, back_left, back_right; // User interface effect, just consolidates space
    private PIDController pid;
    private FtcDashboard dashboard;

    // Tune these constants
    double proportional = 0.01, integral = 0, derivative = 0; // User interface effect, just consolidates space

    static double power;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware
        front_left = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        front_right = hardwareMap.get(DcMotor.class, "frontRightDrive");
        back_left = hardwareMap.get(DcMotor.class, "backLeftDrive");
        back_right = hardwareMap.get(DcMotor.class, "backRightDrive");

        front_left.setDirection(DcMotor.Direction.REVERSE);
        front_right.setDirection(DcMotor.Direction.FORWARD);
        back_left.setDirection(DcMotor.Direction.REVERSE); //opposite to move forward
        back_right.setDirection(DcMotor.Direction.FORWARD); //opposite to move forward

        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        front_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // PID will handle control
        front_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize PID controller
        pid = new PIDController(proportional, integral, derivative);

        // Activate dashboard
        dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        // Wait for the game to start
        waitForStart();

        // Target position in encoder ticks
        double targetPosition = 0;

        while (opModeIsActive()) {
            // Get current motor position
            double currentPosition = front_left.getCurrentPosition();

            // Note: Robot has been observed to drift in the direction of front_left when left to run. Going to try and switch to front_right to see if that reverses the effect
            // Note: if above is proven to be true, expand to include PID calculations for all 4 motors

            // Calculate power using PID
            power = pid.calculate(targetPosition, currentPosition); // The returned values will allow us to input power

            TelemetryPacket packet = new TelemetryPacket();
            packet.put("Target Position", targetPosition);
            packet.put("Current Position", currentPosition);
            packet.put("Error", targetPosition - currentPosition);

            packet.put("Proportional", pid.getProportional());
            packet.put("Integral", pid.getIntegral());
            packet.put("Derivative", pid.getDerivative());

            dashboard.sendTelemetryPacket(packet);

            // Debugging info
            telemetry.addData("","");
            telemetry.addData("-----Debugging Information-----", "");
            telemetry.addData("Target", targetPosition);
            telemetry.addData("Current", currentPosition);
            telemetry.addData("Power", power);
            telemetry.update();

            sleep(5000);

            // Set motor power
            front_left.setPower(power*0.25);
            front_right.setPower(power*0.25);
            back_left.setPower(power*0.25);
            back_right.setPower(power*0.25);

            if (Math.abs(targetPosition-currentPosition) == targetPosition) {
                packet.put("Target Position", targetPosition);
                packet.put("Current Position", currentPosition);
                dashboard.sendTelemetryPacket(packet);
                telemetry.addData(">", "Position Reached");
                telemetry.update();
                power = 0;
                break;
            }

            // Allow time for other tasks
            sleep(20);
        }
    }
}
