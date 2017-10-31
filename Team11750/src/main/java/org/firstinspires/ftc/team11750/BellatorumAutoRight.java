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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the Bellatorum hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Bellatorum: Auto Right", group="Bellatorum")
public class BellatorumAutoRight extends LinearOpMode {

    /* Declare OpMode members. */
    private HardwareBellatorum robot   = new HardwareBellatorum();   // Use Bellatorum's hardware
    private ElapsedTime runtime = new ElapsedTime();

    private void turn(double angle, double power) {

        robot.startRotate(angle, power); // Start rotating in the angle direction at power
        if (angle < 0) { angle *= -1; } // If the angle is negative make it positive

        // Turn long enough to make the angle
        runtime.reset();
        while (runtime.seconds() < angle*robot.DEGREES_PER_SEC/power + robot.TURN_START_SECS) {
            telemetry.addData("Turning: ", "%2.5f secs Elapsed", runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {robot.stopMoving(); return;} // Stop and return
        }
        robot.stopMoving();
    }
    private void turn (double angle) {turn(angle, robot.TURN_POWER);} // Overload with default power


    private void move(double angle, double distance, double power){
        robot.startMovingInDirection(angle, power); // Start moving in the right direction

        // Run long enough to make the distance
        runtime.reset();
        while (runtime.seconds() < distance*robot.FEET_PER_SEC/power + robot.MOVE_START_SECS) {
            telemetry.addData("Moving: ", "%2.5f deg, %2.5f secs Elapsed", angle, runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {robot.stopMoving(); return;} // Stop and return
        }
        robot.stopMoving();
    }
    private void move(double angle, double distance){ // Overload with default power
        move(angle, distance, robot.FORWARD_POWER);
    }

    private void lift(double directionPower, double distance){
        robot.liftMotor.setPower(directionPower);
        if (directionPower<0)directionPower*=-1; // Make sure the power positive
        runtime.reset();
        while (runtime.seconds() < distance * robot.LIFT_FEET_PER_SEC/directionPower) {
            telemetry.addData("Lift", "Time: %2.5f secs Elapsed", runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {robot.stopMoving(); return;} // Stop and return
        }
        robot.liftMotor.setPower(0.0);
    }
    private void liftUp(double distance) { lift(robot.LIFT_UP_POWER, distance);}
    private void liftDown(double distance) { lift(robot.LIFT_DOWN_POWER, distance);}

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.clampClose(); // Grab the glyph
        liftUp(3/12); // Raise the lift 3 in

        move(robot.RIGHT, 3); // Move right 3 feet
        turn(robot.AROUND); // Turn 180 degrees
        move(robot.FORWARD, 2); // Move forward 2 feet

        robot.clampOpen(); // Drop the glyph

        move(robot.BACK, 2/12); // Back up 2 inches

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
