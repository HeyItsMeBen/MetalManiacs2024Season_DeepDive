package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class compDrive {
    // Declare DcMotor objects for the drivetrain
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    // Define the speed array
    // double array with 5 elements to store possible DC motor speeds
    // dblSpeed[0] = STOP zero power to motors
    // dblSpeed[1] = 25% SLOWEST power
    // dblSpeed[2] = 50% SLOWER power
    // dblSpeed[3] = 75% FASTER power
    // dblSpeed[4] = 100% power, MAX Power, CAREFUL!
    // private final double[] dblSpeed[5] = {0.0, 0.25, 0.5, 0.75, 1.0};

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
     *  Approx Lines 58 - 150 are PRIVATE methods to set sample values of either encoder, motor directions, motor power.
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
        //SET all 4 directions to forward
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    private void setForward() {
        //SET all 4 directions to forward
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    private void setStrafeLeft() {
        //SET motor directions to have robot STRAFE LEFT
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    private void setStrafeRight(){
        //SET motor directions to have robot STRAFE RIGHT
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    /****** TO DO *****/
    private void setUpperRight(){
        //SET MOTOR directions to have robot move forward upper right!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    /****** TO DO *****/
    private void setUpperLeft(){
        //SET MOTOR directions to have robot move forward upper left!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    /****** TO DO *****/
    private void setLowerLeft(){
        //SET MOTOR directions to have robot move backward lower left!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    /****** TO DO *****/
    private void setLowerRight(){
        //SET MOTOR directions to have robot move backward lower right!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    /****** TO DO *****/
    private void setCounterTurn() {
        //SET MOTOR directions to have robot TURN Counter Clockwise!
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    /****** TO DO *****/
    private void setClockwiseTurn() {
        //SET MOTOR directions to have robot TURN Clockwise
        //CHANGE VALUES BELOW then delete this comment line
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    private void setMotorPower(double dblPower) {
        // Set the motor mode to RUN_TO_POSITION
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //SET MOTOR Power for all 4 motors based on the whatever value has been passed in as dblPower;

        //Clear the FIELD! SET Motor Power to Max!
        frontLeftDrive.setPower(dblPower);
        backLeftDrive.setPower(dblPower);
        frontRightDrive.setPower(dblPower);
        backRightDrive.setPower(dblPower);
    }

    //ALL PUBLIC methods can be called by autoMainB1.java, autoMainB2.java, autoMainR1.java, autoMainR2.java files once imported
    //STOP DRIVING by setting all 4 motors to zero
    public void stopDrive() {
        //Stopping all power. No more movement!
        setMotorPower(0.0);

        // Reset encoder counts every time for accuracy of movement in inches
        resetEncoderCount();
    }

    //TO DO:  Keep doing MAX Power for remainder 8 paths and then delete comment
    /*
     * BELOW public methods allow robot to drive at FASTEST pre-determined speed of 100% power
     */

    //Robot will drive forward ## of inches indicated by dblInches, and at % power indicated by dblPower. CAREFUL! CLEAR the FIELD!
    //moveForward(6.0, 1.0) will result in robot to move forward 6 inches at maximum power
    public void moveForward(double dblInches, double dblPower) {
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
        setForward(); //Set motors direction to move FORWARD

        // Set target position for each motor
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
        // Stop the motors and reset the power to 0 and also reset encoder count
        stopDrive(); //End Drive Reset Encoders
    }

    /* TO DO
     *
     */
    public void moveBackward (double dblInches, double dblPower){
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

    /* TO DO
     *
     */
    public void moveLeft (double dblInches, double dblPower){
    // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.

    }

    /* TO DO
     *
     */
    public void moveRight (double dblInches, double dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }

    /* TO DO
     *
     */
    public void moveUpperLeft (double dblInches, double dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }

    /* TO DO
     *
     */
    public void moveUpperRight (double dblInches, double dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }

    /* TO DO
     *
     */
    public void moveLowerLeft (double dblInches, double dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }

    /* TO DO
     *
     */
    public void moveLowerRight (double dblInches, double dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }

    /* TO DO
     *
     */
    public void moveClockwiseTurn (double dblInches, double dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift.
    }

    /* TO DO
     *
     */
    public void moveCounterClockwiseTurn (double dblInches, double dblPower){
        // Convert distance to encoder counts
        int encoderCountsToMove = (int) (dblInches * ENCODER_COUNTS_PER_INCH);

        stopDrive(); //Before Changing Directions. STOP motor first so no drift. Reset encoders


        stopDrive(); //Reset encoders and STOP motors.
    }

}
