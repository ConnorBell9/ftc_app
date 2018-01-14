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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 *
 */
class HardwareByrdMK2
{
    /* Public OpMode members. */
    DcMotor  frontRight        = null;
    DcMotor  frontLeft         = null;
    DcMotor  backRight         = null;
    DcMotor  backLeft          = null;

    DcMotor  clamp             = null;

    DcMotor  idolZ             = null;
    DcMotor  idolY             = null;

    Servo    armL              = null;
    Servo    armR              = null;
    Servo    armT              = null;
/*
    CRServo  intakeTopLeft     = null;
    CRServo  intakeTopRight    = null;
    CRServo  intakeBottomLeft  = null;
    CRServo  intakeBottomRight = null;
*/
    Servo    hammerY           = null;
    Servo    hammerX           = null;

    Servo    plate             = null;

    Servo    grabber           = null;

    ModernRoboticsI2cGyro gyro          = null;
    ModernRoboticsI2cColorSensor colorR = null;
    ModernRoboticsI2cColorSensor colorL = null;

    private static final double IDOL_Z_START_POSITION =  0;//300
    static final double IDOL_Z_POSITION       =  0;
    static final int    IDOL_Z_DELTA_POSITION = 50;
    static final double IDOL_Y_POSITION       =  0;

    static boolean       IS_BLOCK_GRAB     = false;
    static final double  LEFT_CLAMP_CLOSE  =     1;
    static final double  RIGHT_CLAMP_CLOSE =     0;
    static final double  TOP_CLAMP_CLOSE   =   -.2;
    static final double  LEFT_CLAMP_OPEN   =    .6;
    static final double  RIGHT_CLAMP_OPEN  =    .4;
    static final double  TOP_CLAMP_OPEN    =     1;
    private static final double  LEFT_CLAMP_INIT   =    .4;
    private static final double  RIGHT_CLAMP_INIT  =    .6;
    private static final double  TOP_CLAMP_INIT    =    -1;

    static final double  INTAKE_ON         = 1;
    static final double  INTAKE_OFF        = 0;

    static final int     CLAMP_POSITION_1  =       0;
    static final int     CLAMP_POSITION_2  =  (7*1440)/3;//(InchesToTravel*1440/InchesPerRevolution)
    static final int     CLAMP_POSITION_3  = (13*1440)/3;
    static final int     CLAMP_POSITION_4  = (19*1440)/3;

    static final double  HAMMER_DOWN    =     1;
    static final double  HAMMER_UP      =   -.5;
    static final double  HAMMER_LEFT    =    .6;
    static final double  HAMMER_RIGHT   =    .2;
    static final double  HAMMER_CENTER  =    .4;

    static boolean       IS_PLATE        = false;
    static final double  PUSH_PLATE_DOWN =     0;
    static final double  PUSH_PLATE_UP   =     1;

    static boolean       IS_IDOL_GRAB      = false;
    static final double  IDOL_CLAMP_OPEN   =     0;
    static final double  IDOL_CLAMP_CLOSED =     1;

    static boolean       IS_GYRO_ON        = false;

    static double INPUT_TIMER = 0;

    static final boolean LEFT  = false;
    static final boolean RIGHT = true;

    static final boolean RED  = false;
    static final boolean BLUE = true;

    static final double MOVE_BACKWARDS = 270;
    static final double MOVE_FORWARDS  = 90;
    static final double MOVE_LEFT      = 180;
    static final double MOVE_RIGHT     = 0;

    static final boolean BACKWARDS = false;
    static final boolean FORWARDS  = true;

    static final double TURN_FORWARDS = 0;
    static final double TURN_LEFT     = 90;
    static final double TURN_RIGHT    = 270;
    static final double TURN_BACK     = 180;

    static final int TURN_ERROR = 2;

    static int FRONT_LEFT  = 0;
    static int FRONT_RIGHT = 0;
    static int BACK_LEFT   = 0;
    static int BACK_RIGHT  = 0;

    static boolean SLOT_1 = false;
    static boolean SLOT_2 = false;
    static boolean SLOT_3 = false;
    static double OFFSET = 0;


    static boolean VUFORIA_ENABLED  = true;
    static boolean VUFORIA_DISABLED = false;

    /* Constructor */
    HardwareByrdMK2(){

    }

    /* Initialize standard Hardware interfaces */
    void init(HardwareMap ahwMap) {
        // Save reference to Hardware map

        HardwareMap hwMap;

        hwMap = ahwMap;

        // Define and Initialize Motors
        frontRight = hwMap.get(DcMotor.class, "fr");
        frontLeft  = hwMap.get(DcMotor.class, "fl");
        backRight  = hwMap.get(DcMotor.class, "br");
        backLeft   = hwMap.get(DcMotor.class, "bl");

        clamp      = hwMap.get(DcMotor.class, "clamp");

        idolZ      = hwMap.get(DcMotor.class, "idolZ");
        idolY      = hwMap.get(DcMotor.class, "idolY");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        clamp.setDirection(DcMotor.Direction.REVERSE);
        idolZ.setDirection(DcMotor.Direction.FORWARD);
        idolY.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        clamp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        idolZ.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idolZ.setTargetPosition((int)IDOL_Z_START_POSITION);
        idolY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set all motors to zero power
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        clamp.setPower(0);
        idolZ.setPower(1);
        idolY.setPower(0);

        // Define and initialize ALL installed servos.
        armL = hwMap.servo.get("armL");
        armR = hwMap.servo.get("armR");
        armT = hwMap.servo.get("armT");
/*
        intakeTopLeft     = hwMap.crservo.get("intakeTopLeft");
        intakeTopRight    = hwMap.crservo.get("intakeTopRight");
        intakeBottomLeft  = hwMap.crservo.get("intakeBottomLeft");
        intakeBottomRight = hwMap.crservo.get("intakeBottomRight");

        intakeTopLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeTopRight.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeBottomLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeBottomRight.setDirection(DcMotorSimple.Direction.REVERSE);
*/
        hammerY = hwMap.servo.get("hammerY");
        hammerX = hwMap.servo.get("hammerX");

        plate = hwMap.servo.get("plate");

        grabber = hwMap.servo.get("grabber");

        armL.setPosition(LEFT_CLAMP_INIT);
        armR.setPosition(RIGHT_CLAMP_INIT);
        armT.setPosition(TOP_CLAMP_INIT);
        hammerY.setPosition(HAMMER_UP);
        hammerX.setPosition(HAMMER_CENTER);
        plate.setPosition(PUSH_PLATE_UP);
        grabber.setPosition(IDOL_CLAMP_OPEN);
/*
        intakeTopLeft.setPower(INTAKE_OFF);
        intakeTopRight.setPower(INTAKE_OFF);
        intakeBottomLeft.setPower(INTAKE_OFF);
        intakeBottomRight.setPower(INTAKE_OFF);
*/
        // Define Sensors
        gyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        colorR = hwMap.get(ModernRoboticsI2cColorSensor.class, "colorR");
        colorL = hwMap.get(ModernRoboticsI2cColorSensor.class, "colorL");

        colorR.enableLed(false);
        colorL.enableLed(false);

    }
 }

