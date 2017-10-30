//package org.firstinspires.ftc.robotcontroller.external.samples;
package org.firstinspires.ftc.team11750;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
public class HardwareBellatorum
{
    /* Public OpMode members. */
    public DcMotor  leftFrontMotor   = null;
    public DcMotor  rightFrontMotor  = null;
    public DcMotor  leftBackMotor    = null;
    public DcMotor  rightBackMotor   = null;
    public DcMotor  liftMotor   = null;
    public Servo    leftClamp   = null;
    public Servo    rightClamp  = null;

    public static final double BACK_SERVO      =  0.5;
    public static final double LIFT_UP_POWER    =  0.45 ;
    public static final double LIFT_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareBellatorum(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFrontMotor   = hwMap.dcMotor.get("left_front_drive");
        rightFrontMotor  = hwMap.dcMotor.get("right_front_drive");
        leftBackMotor    = hwMap.dcMotor.get("left_back_drive");
        rightBackMotor   = hwMap.dcMotor.get("right_back_drive");
        liftMotor    = hwMap.dcMotor.get("lift_arm");
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors


        // Set all motors to zero power
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightBackMotor.setPower(0);
        liftMotor.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        leftClamp = hwMap.servo.get("left_hand");
        rightClamp = hwMap.servo.get("right_hand");
        leftClamp.setPosition(BACK_SERVO);
        rightClamp.setPosition(BACK_SERVO);
    }

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

