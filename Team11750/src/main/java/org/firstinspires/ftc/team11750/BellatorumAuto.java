/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.team11750;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the Bellatorum hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public class BellatorumAuto extends LinearOpMode {

    /* Declare OpMode members. */
    private HardwareBellatorum robot   = new HardwareBellatorum();   // Use Bellatorum's hardware
    private ElapsedTime runtime = new ElapsedTime();

    private void turn(double angle, double power) {

        if (angle==0) return; // Return immediately for 0 degree turn
        robot.startRotate(angle, power); // Start rotating in the angle direction at power
        if (angle < 0) { angle *= -1; } // If the angle is negative make it positive

        // Turn long enough to make the angle
        runtime.reset();
        while (runtime.seconds() < angle/robot.DEGREES_PER_SEC/power + robot.TURN_START_SECS) {
            telemetry.addData("Turning: ", "%2.5f secs Elapsed", runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {robot.stopMoving(); return;} // Stop and return
        }
        robot.stopMoving();
    }
    void turn(double angle) {turn(angle, robot.TURN_POWER);} // Overload with default power

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftBackMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightFrontMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftBackMotor.setTargetPosition(newLeftTarget);
            robot.rightFrontMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftBackMotor.setPower(Math.abs(speed));
            robot.rightFrontMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftBackMotor.isBusy() && robot.rightFrontMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftBackMotor.getCurrentPosition(),
                        robot.rightFrontMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftBackMotor.setPower(0);
            robot.rightFrontMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
    private void move(double angle, double distance, double power){
        robot.startMovingInDirection(angle, power); // Start moving in the right direction

        // Run long enough to make the distance
        runtime.reset();
        while (runtime.seconds() < distance/robot.FEET_PER_SEC/power) {
            telemetry.addData("Moving: ", "%2.5f deg, %2.5f ft, %2.5f secs Elapsed",
                    angle, distance, runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {robot.stopMoving(); return;} // Stop and return
        }
        robot.stopMoving();
    }
    void move(double angle, double distance){ // Overload with default power
        move(angle, distance, robot.FORWARD_POWER);
    }

    private void lift(double directionPower, double distance){
        if(!robot.clampInstalled) return;
        robot.liftMotor.setPower(directionPower);
        if (directionPower<0)directionPower*=-1; // Make sure the power positive
        runtime.reset();
        while (runtime.seconds() < distance / robot.LIFT_FEET_PER_SEC/directionPower) {
            telemetry.addData("Lift", "Time: %2.5f secs Elapsed", runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {robot.stopMoving(); return;} // Stop and return
        }
        robot.liftMotor.setPower(0.0);
    }
    void liftUp(double distance) { lift(robot.LIFT_UP_POWER, distance);}
    void liftDown(double distance) { lift(robot.LIFT_DOWN_POWER, distance);}

    void displaceJewel(int color){
        double turnAngle = 0;
        robot.armDown(); // Drop the color sensor arm

        runtime.reset();
        while (runtime.seconds() < 1) {
            telemetry.addData("Clear", robot.colorSensor.alpha());
            telemetry.addData("Red  ", robot.colorSensor.red());
            telemetry.addData("Green", robot.colorSensor.green());
            telemetry.addData("Blue ", robot.colorSensor.blue());
            telemetry.update();
            if (!opModeIsActive()) {robot.stopMoving(); return;} // Stop and return
        }
        // Displace the blue color ball
        if (robot.colorSensor.blue() >= 1) turnAngle += 20;
        if (robot.colorSensor.red() >= 1) turnAngle -= 20;

        // Turn the other way to displace thr
        if(color == robot.COLOR_RED) turnAngle*=-1; // Turn the other way

        turn(turnAngle); // Turn to knock off the jewel
        robot.armUp();   // Raise the arm
        turn(-turnAngle);// Turn back
    }
    void redTeamDisplaceJewel(){ displaceJewel(robot.COLOR_BLUE);}
    void blueTeamDisplaceJewel() {displaceJewel(robot.COLOR_RED);}

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.leftBackMotor.getCurrentPosition(),
                          robot.rightFrontMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(DRIVE_SPEED,  48,  48, 5.0);  // S1: Forward 48 Inches with 5 Sec timeout
        encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout

        robot.leftClamp.setPosition(0.0);            // S4: Stop and close the claw.
        robot.rightClamp.setPosition(1.0);
        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
}
