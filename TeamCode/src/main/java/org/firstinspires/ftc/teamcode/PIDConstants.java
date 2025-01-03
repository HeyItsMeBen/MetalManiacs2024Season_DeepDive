package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@Config
public class PIDConstants {
   /*public static double Kp = 1.8;   // Proportional gain
   public static double Ki = 0.0;   // Integral gain
   public static double Kd = 0.031; // Derivative gain
    */


    public static double Kp = 1;   // Proportional gain
    public static double Ki = 0.01;   // Integral gain
    public static double Kd = 0.031; // Derivative gain
}