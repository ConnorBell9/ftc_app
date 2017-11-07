//package org.firstinspires.ftc.robotcontroller.external.samples;
package org.firstinspires.ftc.team11383;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left Front drive motor:   "left_front_drive"
 * Motor channel:  Right Front drive motor:  "right_front_drive"
 * Motor channel:  Left Back drive motor:    "left_back_drive"
 * Motor channel:  Right Back drive motor:   "right_back_drive"
 * Motor channel:  Lift Drive motor:         "lift_arm"
 * Servo channel:  Servo to move left clamp: "left_hand"
 * Servo channel:  Servo to move right clamp:"right_hand"
 */
class HardwareFortissimus
{
    /* Public OpMode members. */
    DcMotor  frontright   = null;
    DcMotor  frontleft  = null;
    DcMotor  backleft    = null;
    DcMotor  backright   = null;
    DcMotor  reel   = null;
    Servo    armright   = null;
    Servo    armleft  = null;

    final double armleftopen  =  0.5;
    final double armrightopen = 0.5;
    final double armleftclosed  = 1.0;
    final double armrightclosed = 0.0;
    final double reelup    =  0.45 ;
    final double reeldown  = -0.45 ;
    final double forward = 0.6;
    final double FEET_PER_SEC = 1;
    final double MOVE_START_SECS = 0.1;
    final double TURN_POWER    = 0.5;
    final double FORWARD =0.0;
    final double RIGHT = 90.0;
    final double LEFT = -90.0;
    final double BACK = 180.0;
    final double AROUND = 180.0;
    final double DEGREES_PER_SEC = 45.0;
    final double TURN_START_SECS = 0.1;
    final double LIFT_FEET_PER_SEC = 0.5;

    /* local OpMode members. */
    private HardwareMap hwMap   =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    HardwareFortissimus(){

    }

    /* Initialize standard Hardware interfaces */
    void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frontright   = hwMap.dcMotor.get("fr");
        frontleft  = hwMap.dcMotor.get("fl");
        backleft    = hwMap.dcMotor.get("bl");
        backright   = hwMap.dcMotor.get("br");
        reel   = hwMap.dcMotor.get("reel");
        frontright.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        frontleft.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        backleft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        backright.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors


        // Set all motors to zero power
        frontright.setPower(0);
        frontleft.setPower(0);
        backleft.setPower(0);
        backright.setPower(0);
        reel.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        reel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        armleft = hwMap.servo.get("al");
        armright = hwMap.servo.get("ar");
        armleft.setPosition(armleftopen);
        armright.setPosition(armrightopen);
    }

    // Stop the robot from moving
    void stopMoving() {
        backleft.setPower(0.0); // Stop
        frontleft.setPower(0.0);
        frontright.setPower(0.0);
        backright.setPower(0.0);
    }

    // Start the robot turning in the angle direction at specified power
    void startRotate(double angle, double power) {
        double direction = 1.0; // The direction to turn

        if (angle < 0) { // If the angle is negative
            direction = -1; // Toggle the direction
            angle *= -1; // Make the angle positive
        }

        // Set all motors to turn in direction at power
        backright.setPower(power * direction);
        frontleft.setPower(power * direction);
        frontright.setPower(power * direction);
        backleft.setPower(power * direction);
    }

    // Start the robot moving in the direction specified by angle (relative to the robot front)
    void startMovingInDirection(double angle, double power){
        frontleft.setPower(-power * Math.sin((Math.PI / 180) * angle));
        backleft.setPower(power * Math.sin((Math.PI / 180) * angle));
        frontright.setPower(power * Math.cos((Math.PI / 180) * angle));
        backright.setPower(-power * Math.cos((Math.PI / 180) * angle));
    }

    // Set the clamp to the specified open angle
    void clampOpen(double angle){
        armleft.setPosition(armleftclosed - angle/2/180);
        armright.setPosition(armrightclosed + angle/2/180);
    }
    void clampOpen() {clampOpen(180);} // Open the clamp all the way
    void clampClose() {clampOpen(90);} // Close the clamp on a glyph

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}