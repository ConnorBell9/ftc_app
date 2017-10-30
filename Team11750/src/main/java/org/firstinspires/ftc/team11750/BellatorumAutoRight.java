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

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
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

@Autonomous(name="Bellatorum: Auto Drive By Time", group="Bellatorum")
@Disabled
public class BellatorumAutoRight extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareBellatorum         robot   = new HardwareBellatorum();   // Use Bellatorum's hardware
    private ElapsedTime     runtime = new ElapsedTime();


    static final double FORWARD_POWER = 0.6;
    static final double FEET_PER_SEC = 1;
    static final double MOVE_START_SECS = 0.1;
    static final double TURN_POWER    = 0.5;
    static final double STRAIGHT =0.0;
    static final double RIGHT = 90.0;
    static final double LEFT = -90.0;
    static final double BACK = 180.0;
    static final double AROUND = 180.0;
    static final double DEGREES_PER_SEC = 45.0;
    static final double TURN_START_SECS = 0.1;
    static final double LIFT_FEET_PER_SEC = 0.5;

    void stopMoving() {
        robot.leftBackMotor.setPower(0.0); // Stop
        robot.rightFrontMotor.setPower(0.0);
        robot.leftFrontMotor.setPower(0.0); // Stop
        robot.rightBackMotor.setPower(0.0);
    }

    void turn(double angle, double power) {
        double direction = 1.0; // The direction to turn

        if (angle < 0) { // If the angle is negative
            direction = -1; // Toggle the direction
            angle *= -1; // Make the angle positive
        }

        // Set all motors to turn in direction at power
        robot.rightBackMotor.setPower(power * direction);
        robot.rightFrontMotor.setPower(power * direction);
        robot.leftFrontMotor.setPower(power * direction);
        robot.leftBackMotor.setPower(power * direction);

        // Turn long enough to make the angle
        runtime.reset();
        while (runtime.seconds() < angle*DEGREES_PER_SEC/power + TURN_START_SECS) {
            telemetry.addData("Turning: ", "%2.5f secs Elapsed", runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {stopMoving(); return;} // Stop and return
        }
        stopMoving();
    }
    void turn (double angle) {turn(angle, TURN_POWER);} // Overload with default power

    void startMovingInDirection(double angle, double power){
        robot.rightFrontMotor.setPower(-power * Math.sin((Math.PI / 180) * angle));
        robot.leftBackMotor.setPower(power * Math.sin((Math.PI / 180) * angle));
        robot.leftFrontMotor.setPower(power * Math.cos((Math.PI / 180) * angle));
        robot.rightBackMotor.setPower(-power * Math.cos((Math.PI / 180) * angle));
    }

    void move(double angle, double distance, double power){
        startMovingInDirection(angle, power); // Start moving in the right direction

        // Run long enough to make the distance
        runtime.reset();
        while (runtime.seconds() < distance*FEET_PER_SEC/power + MOVE_START_SECS) {
            telemetry.addData("Moving: ", "%2.5f deg, %2.5f secs Elapsed", runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {stopMoving(); return;} // Stop and return
        }
        stopMoving();
    }
    void move(double angle, double distance){ // Overload with default power
        move(angle, distance, FORWARD_POWER);
    }

    void lift(double directionPower, double distance){
        robot.liftMotor.setPower(directionPower);
        if (directionPower<0)directionPower*=-1; // Make sure the power positive
        runtime.reset();
        while (runtime.seconds() < distance * LIFT_FEET_PER_SEC/directionPower) {
            telemetry.addData("Lift", "Time: %2.5f secs Elapsed", runtime.seconds());
            telemetry.update();
            if (!opModeIsActive()) {stopMoving(); return;} // Stop and return
        }
        robot.liftMotor.setPower(0.0);
    }
    void liftUp(double distance) { lift(robot.LIFT_UP_POWER, distance);}
    void liftDown(double distance) { lift(robot.LIFT_DOWN_POWER, distance);}

    void clamp(double angle){
        robot.leftClamp.setPosition(robot.BACK_SERVO + angle/180);
        robot.rightClamp.setPosition(robot.BACK_SERVO - angle/180);
    }
    void clampOpen() {clamp(0);} // Open the clamp
    void clampClose() {clamp(60);} // Close the clamp


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

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        clampClose(); // Grab the glyph

        liftUp(0.5); // Raise the lift

        move(RIGHT, 3); // Move right 3 feet
        turn(AROUND); // Turn 180 degrees
        move(STRAIGHT, 2); // Move forward 2 feet

        clampOpen(); // Drop the glyph

        move(BACK, 2/12); // Back up 2 inches

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
