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

package org.firstinspires.ftc.team7153;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
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
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareByrd
{
    /* Public OpMode members. */
    public DcMotor  frontRight = null;
    public DcMotor  frontLeft  = null;
    public DcMotor  backRight  = null;
    public DcMotor  backLeft   = null;

    public DcMotor  forkX      = null;
    public DcMotor  forkY      = null;

    public DcMotor  idolZ      = null;
    public DcMotor  idolY      = null;

    public Servo    armL       = null;
    public Servo    armR       = null;

    public Servo    hammerX    = null;
    public CRServo  hammerY    = null;

    public Servo    plate     = null;

    public Servo    grabber    = null;

    //public TouchSensor touchL  = null;
    //public TouchSensor touchR  = null;

    public ModernRoboticsI2cGyro gyro         = null;
    public ModernRoboticsI2cColorSensor color = null;

    public static final double LIFT_X_OUT = ((1440/(1.25*3.1415))*7);
    public static final double LIFT_X_IN  = 0;

    public static final double IDOL_Z_START_POSITION = 600;
    public static final double IDOL_Z_POSITION       = 0;
    public static final int    IDOL_Z_DELTA_POSITION = 21;
    public static final double IDOL_Y_POSITION       = 0;

    public static boolean       IS_BLOCK_GRAB     = false;
    public static final double  LEFT_CLAMP_CLOSE  = .4;
    public static final double  RIGHT_CLAMP_CLOSE = 1;
    public static final double  LEFT_CLAMP_OPEN   = .7;
    public static final double  RIGHT_CLAMP_OPEN  = .7;

    public static boolean       IS_HAMMER     = false;
    public static final double  HAMMER_DOWN   = -.4;
    public static final double  HAMMER_UP     = -1;
    public static final double  HAMMER_LEFT   = .8;
    public static final double  HAMMER_RIGHT  = .4;
    public static final double  HAMMER_CENTER = .6;

    public static boolean       IS_PLATE        = false;
    public static final double  PUSH_PLATE_DOWN = 0;
    public static final double  PUSH_PLATE_UP   = 1;

    public static boolean       IS_IDOL_GRAB      = false;
    public static final double  IDOL_CLAMP_OPEN   = 0;
    public static final double  IDOL_CLAMP_CLOSED = 1;

    public static long INPUT_TIMER = 0;

    public static final boolean LEFT  = false;
    public static final boolean RIGHT = true;

    public static final boolean RED = true;
    public static final boolean BLUE = true;


    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareByrd(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frontRight = hwMap.get(DcMotor.class, "fr");
        frontLeft  = hwMap.get(DcMotor.class, "fl");
        backRight  = hwMap.get(DcMotor.class, "br");
        backLeft   = hwMap.get(DcMotor.class, "bl");

        forkX      = hwMap.get(DcMotor.class, "forkX");
        forkY      = hwMap.get(DcMotor.class, "forkY");

        idolZ      = hwMap.get(DcMotor.class, "idolZ");
        idolY      = hwMap.get(DcMotor.class, "idolY");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        forkX.setDirection(DcMotor.Direction.FORWARD);
        forkY.setDirection(DcMotor.Direction.REVERSE);
        idolZ.setDirection(DcMotor.Direction.FORWARD);
        idolY.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        forkX.setPower(1);
        forkY.setPower(0);
        idolZ.setPower(1);
        idolY.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        forkX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        forkX.setTargetPosition((int)LIFT_X_IN);
        forkY.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        idolZ.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idolZ.setTargetPosition((int)IDOL_Z_START_POSITION);
        idolY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        armL = hwMap.servo.get("armL");
        armR = hwMap.servo.get("armR");

        hammerX = hwMap.servo.get("hammerX");
        hammerY = hwMap.crservo.get("hammerY");

        plate = hwMap.servo.get("plate");

        grabber = hwMap.servo.get("grabber");

        armL.setPosition(LEFT_CLAMP_OPEN);
        armR.setPosition(RIGHT_CLAMP_OPEN);
        hammerX.setPosition(HAMMER_CENTER);
        hammerY.setPower(HAMMER_UP);
        plate.setPosition(PUSH_PLATE_UP);
        grabber.setPosition(IDOL_CLAMP_OPEN);

        // Define Sensors
        gyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        color = hwMap.get(ModernRoboticsI2cColorSensor.class, "color");

        color.enableLed(false);
    }
 }

