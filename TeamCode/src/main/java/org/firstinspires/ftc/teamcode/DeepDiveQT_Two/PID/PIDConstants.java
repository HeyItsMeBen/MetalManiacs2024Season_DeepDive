package org.firstinspires.ftc.teamcode.DeepDiveQT_Two.PID;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@Config
public class PIDConstants {
   /*public static double Kp = 1.8;   // Proportional gain
   public static double Ki = 0.0;   // Integral gain
   public static double Kd = 0.031; // Derivative gain
    */


    public static double Kp = 0.005;   // Proportional gain
    public static double Ki = 0;   // Integral gain
    public static double Kd = 0; // Derivative gain
    public static double targetInches=5;
}