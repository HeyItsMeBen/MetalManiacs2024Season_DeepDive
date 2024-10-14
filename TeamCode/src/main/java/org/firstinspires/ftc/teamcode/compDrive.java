package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
//trigger code
public class compDrive {
    // Declare DcMotor objects for the drivetrain
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    private static final int ENCODER_COUNTS_PER_INCH = 43; // TEST value, replace with actual based on motor and wheel diameter

    public compDrive(HardwareMap hardwareMap){
        /*construct a new compDrive object*/

        // Initialize motors
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");

        // Set motors to run using encoders for precise movement
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //SET initial motor directions to all going FORWARD
        setForward();

        //SET Motors to STOP for safety
        stopDrive();

        //None of the above code will move the power since stopDrive was called setting motor powers to zero.
    }

    /*
     *  Approx Lines 45 - 165 are PRIVATE methods to set sample values of either encoder, motor directions, motor power.
     */
    //ALL PRIVATE methods cannot be called by autoMain files and only used within compDrive.java
    private void resetEncoderCount()
    {
        // Reset encoder counts
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void setBackward() {
        //SET all 4 directions to backwards
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
    }


    private void setForward() {
        //SET all 4 directions to move forward
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //opposite to move forward
        backRightDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move forward
    }

    private void setStrafeLeft() {
        //SET motor directions to have robot STRAFE LEFT
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move backward
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //opposite to move forward
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

    }

    private void setStrafeRight(){
        //SET motor directions to have robot STRAFE RIGHT
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move reverse
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move forward
    }


    private void setUpperRight(){
        //SET MOTOR directions to have robot move forward upper right!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        //backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        //frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move forward
    }


    private void setUpperLeft(){
        //SET MOTOR directions to have robot move forward upper left!
        //CHANGE VALUES BELOW then delete this comment line
        //frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move forward
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        //backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }


    private void setLowerLeft(){
        //SET MOTOR directions to have robot move backward lower left!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        //backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        //frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE); //opposite to move reverse
    }


    private void setLowerRight(){
        //SET MOTOR directions to have robot move backward lower right!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        //backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //opposite to move forward
        //frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
    }


    private void setCounterClockwiseTurn() {
        //SET MOTOR directions to have robot TURN Counter Clockwise!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move forward
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD); //opposite to move reverse
    }


    private void setClockwiseTurn() {
        //SET MOTOR directions to have robot TURN Clockwise
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE); //opposite to move reverse
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE); //opposite to move forward
    }

    private void setMotorPower(double[] dblPower) {
        // Set the motor mode to RUN_TO_POSITION
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Ensure all the values stored in dblPower are POSITIVE
        //NO negative power values sent since code is using motor directions
        for (int i = 0; i < dblPower.length; i++) {
            dblPower[i] = Math.abs(dblPower[i]);
        }

        //SET MOTOR Power for all 4 motors based on the whatever value has been passed in as dblPower;
        frontLeftDrive.setPower(dblPower[0]);
        backLeftDrive.setPower(dblPower[1]);
        frontRightDrive.setPower(dblPower[2]);
        backRightDrive.setPower(dblPower[3]);
    }

    //ALL PUBLIC methods can be called by autoMainB1.java, autoMainB2.java, autoMainR1.java, autoMainR2.java files once imported
    //STOP DRIVING by setting all 4 motors to zero
    public void stopDrive() {
        double[] dblMotorPower = new double[4] ; //new double[4] automatically assigns all array elements to 0.0

        // Set target position for each motor to ZERO
        frontLeftDrive.setTargetPosition(0);
        backLeftDrive.setTargetPosition(0);
        frontRightDrive.setTargetPosition(0);
        backRightDrive.setTargetPosition(0);

        //Stopping all power. No more movement!
        setMotorPower(dblMotorPower);

        // Reset encoder counts every time for accuracy of movement in inches
        resetEncoderCount();
    }

    //TO DO:  Keep doing MAX Power for remainder 8 paths and then delete comment
    /*
     * BELOW public methods allow robot to drive at FASTEST pre-determined speed of 100% power
     */

    //Robot will drive forward ## of inches indicated by dblInches, and at % power indicated by dblPower. CAREFUL! CLEAR the FIELD!
    //moveForward(6.0, {1.0, 1.0, 1.0, 1.0}) will result in robot to move forward 6 inches at maximum power
    public void moveForward(double dblInches, double[] dblPower) {
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);
        // Set target position for each motor to ZERO
        frontLeftDrive.setTargetPosition(0);
        backLeftDrive.setTargetPosition(0);
        frontRightDrive.setTargetPosition(0);
        backRightDrive.setTargetPosition(0);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        setForward(); //Set motors direction to move FORWARD

        // Set target position for each motor
        frontLeftDrive.setTargetPosition(encoderCountsToMove);
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove);

        //Set all 4 motors position and also RUN_TO_POSITION
        //value stored in dblPower[0] sets the FRONT LEFT motor power
        //value stored in dblPower[1] sets the FRONT RIGHT motor power
        //value stored in dblPower[2] sets the BACK LEFT motor power
        //value stored in dblPower[3] sets the BACK RIGHT motor power
        setMotorPower(dblPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
            //telemetry.update();
        }
        // Stop the motors and reset the power to 0 and also reset encoder count
        stopDrive(); //End Drive Reset Encoders
    }

    public void moveBackward (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        setBackward(); //Set motor directions to be reverse

        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove);
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove);

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
            //telemetry.update();
        }
        stopDrive(); //End Drive Reset Encoders
    }


    public void moveLeft (double dblInches, double[] dblPower){
    // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        setStrafeLeft(); //Set top left motor and back right motor reverse and top right and back left forward
        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove);
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove);

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
            //telemetry.update();
        }
        stopDrive(); //End Drive Reset Encoders

    }


    public void moveRight (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        setStrafeRight(); //Set top left motor and back right motor forward and top right and back left reverse
        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove);
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove);

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
            //telemetry.update();
        }
        stopDrive(); //End Drive Reset Encoders
    }

    public void moveUpperLeft (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        //Override Power to ensure only the correct motors move.
        //UPPER LEFT ZERO Power to Front Left and Back Right
        //dblRealPower[0] holds value for Front Left
        //dblRealPower[1] holds value for Front Right
        //dblRealPower[2] hold value for Back Left
        //dblRealPower[3] hold value for Back Right
        double[] dblRealPower= {0.0, dblPower[1],dblPower[2], 0.0};

        //stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove); //NO Target Position Movement
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove); //NO Target Position Movement

        setUpperLeft(); //Set FrontRight and BackLeft motor forward

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblRealPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
            //telemetry.update();
        }
        stopDrive(); //End Drive Reset Encoders
    }


    public void moveUpperRight (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        //Override Power to ensure only the correct motors move.
        //UPPER LEFT ZERO Power to Front Left and Back Right
        //dblRealPower[0] holds value for Front Left
        //dblRealPower[1] holds value for Front Right
        //dblRealPower[2] hold value for Back Left
        //dblRealPower[3] hold value for Back Right
        double[] dblRealPower= {dblPower[0], 0.0, 0.0, dblPower[3]};

        //stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove); //NO Target Position Movement
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove); //NO Target Position Movement

        setUpperRight(); //Set FrontRight and BackLeft motor forward

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblRealPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
            //telemetry.update();
        }
        stopDrive(); //End Drive Reset Encoders
    }



    public void moveLowerLeft (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);
        //Override Power to ensure only the correct motors move.
        //UPPER RIGHT ZERO Power to Front Left and Back Right
        //dblRealPower[0] holds value for Front Left
        //dblRealPower[1] holds value for Front Right
        //dblRealPower[2] hold value for Back Left
        //dblRealPower[3] hold value for Back Right
        double[] dblRealPower= {dblPower[0], 0.0,0.0, dblPower[3]};

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        setLowerLeft(); //Set FrontLeft and BackRight motor reverse

        dblPower[1] = 0;
        dblPower[2] = 0;
        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove);
        backLeftDrive.setTargetPosition(0);
        frontRightDrive.setTargetPosition(0);
        backRightDrive.setTargetPosition(encoderCountsToMove);

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
           // telemetry.update();
        }
        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }


    public void moveLowerRight (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        //Override Power to ensure only the correct motors move.
        //UPPER LEFT ZERO Power to Front Left and Back Right
        //dblRealPower[0] holds value for Front Left
        //dblRealPower[1] holds value for Front Right
        //dblRealPower[2] hold value for Back Left
        //dblRealPower[3] hold value for Back Right
        double[] dblRealPower= {dblPower[0], 0.0, 0.0, dblPower[3]};

        //stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove); //NO Target Position Movement
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove); //NO Target Position Movement

        setLowerRight();

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblRealPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
            //telemetry.update();
        }
        stopDrive(); //End Drive Reset Encoders
    }

    /* TO DO
     *
     */
    public void moveClockwiseTurn (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        setClockwiseTurn(); //Set left side to go forward, right side backwards

        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove);
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove);

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
            //telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
           // telemetry.update();
        }
        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }

    /* TO DO
     *
     */
    public void moveCounterClockwiseTurn (double dblInches, double[] dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift. Reset encoders
        setCounterClockwiseTurn(); //Set left side to go backwards, right side forward

        // Set target position for each motor. motor direction is reverse so encoder counts should be negative.
        // Since the motors directions have been set already in above, encoderCountsToMove will always be positive
        frontLeftDrive.setTargetPosition(encoderCountsToMove);
        backLeftDrive.setTargetPosition(encoderCountsToMove);
        frontRightDrive.setTargetPosition(encoderCountsToMove);
        backRightDrive.setTargetPosition(encoderCountsToMove);

        //Set all 4 motors position and also RUN_TO_POSITION
        setMotorPower(dblPower);

        // Wait until the motors reach the target position
        while (frontLeftDrive.isBusy() && backLeftDrive.isBusy() &&
                frontRightDrive.isBusy() && backRightDrive.isBusy()) {
            // Optionally, add telemetry to display progress
           // telemetry.addData("Moving Backward", "Distance: %d inches", dblInches);
           // telemetry.update();
        }

        stopDrive(); //Reset encoders and STOP motors.
    }

}
