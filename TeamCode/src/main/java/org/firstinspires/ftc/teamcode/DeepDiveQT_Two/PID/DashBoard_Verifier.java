package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "DashBoard Verifier", group = "Linear OpMode")
public class DashBoard_Verifier extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Initialize dashboard
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry(); // Link telemetry to the dashboard

        telemetry.addData("Preparing...", "");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData(">", "Connected to dashboard");
            telemetry.update();
        }
    }
}
