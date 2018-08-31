package org.firstinspires.ftc.team11750;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

class HardwareDance
{
    /* Public OpMode members. */
    DcMotor  frontRight        = null;
    DcMotor  frontLeft         = null;
    DcMotor  backRight         = null;
    DcMotor  backLeft          = null;

    DcMotor  foreLift          = null;
    DcMotor  aftLift           = null;

    Servo    leftClamp         = null;
    Servo    rightClamp        = null;
    Servo    topClamp          = null;
    
    Servo    colorArm          = null;

    ModernRoboticsI2cGyro gyro         = null;
    ModernRoboticsI2cColorSensor color = null;
    ModernRoboticsI2cRangeSensor cubbyRange = null;
    ModernRoboticsI2cRangeSensor blockRange = null;

    final double CLAMP_LEFT_OPEN  =  0.2;
    final double CLAMP_RIGHT_OPEN = 0.8;
    final double CLAMP_LEFT_CLOSED  = 1.0;
    final double CLAMP_RIGHT_CLOSED = 0.0;
    final double CLAMP_TOP_OPEN = -0.1;
    final double CLAMP_TOP_CLOSED = 1.0;

    final double LIFT_UP_POWER    =  0.25 ;
    final double LIFT_DOWN_POWER  = -0.13 ;
    final double LIFT_FEET_PER_SEC = 5;
    final double FORWARD_POWER = 0.6;
    final double FEET_PER_SEC = 4;
    final double MOVE_START_SECS = 0.1;
    final double TURN_POWER    = 1.0;
    final double FORWARD =0.0;
    final double RIGHT = 90.0;
    final double LEFT = -90.0;
    final double BACK = 180.0;
    final double AROUND = 180.0;
    final double DEGREES_PER_SEC = 500;
    final double TURN_START_SECS = 0;
    final double ARM_UP = 0.22;
    final double ARM_DOWN = 115;
    final int COLOR_RED = 1;
    final int COLOR_BLUE = 2;
    final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: AndyMark NeverRest 40 Motor Encoder
    final double     DRIVE_GEAR_REDUCTION    = 1.0;      // This is < 1.0 if geared UP
    final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                (WHEEL_DIAMETER_INCHES * 3.1415);

    /* Constructor */
    HardwareByrd(){

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

        lift       = hwMap.get(DcMotor.class, "lift");
        dump       = hwMap.get(DcMotor.class, "dump");

        idolZ      = hwMap.get(DcMotor.class, "idolZ");
        idolY      = hwMap.get(DcMotor.class, "idolY");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.FORWARD);
        dump.setDirection(DcMotor.Direction.REVERSE);
        idolZ.setDirection(DcMotor.Direction.FORWARD);
        idolY.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        idolZ.setTargetPosition((int)IDOL_Z_START_POSITION);
        dump.setTargetPosition(DUMP_DOWN);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dump.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idolZ.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        idolY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set all motors to zero power
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        idolZ.setPower(1);
        idolY.setPower(0);
        dump.setPower(.1);
        lift.setPower(.5);

        // Define and initialize ALL installed servos.
/*        armL = hwMap.servo.get("armL");
        armR = hwMap.servo.get("armR");
        armT = hwMap.servo.get("armT");*/

        intakeFrontLeft = hwMap.crservo.get("intakeFrontLeft");
        intakeFrontRight= hwMap.crservo.get("intakeFrontRight");
        intakeBackLeft  = hwMap.crservo.get("intakeBackLeft");
        intakeBackRight = hwMap.crservo.get("intakeBackRight");

        intakeFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeBackRight.setDirection(DcMotorSimple.Direction.FORWARD);

        hammerY = hwMap.servo.get("hammerY");
        hammerX = hwMap.servo.get("hammerX");

        //plate = hwMap.servo.get("plate");

        grabber = hwMap.servo.get("grabber");

        intakeLatch = hwMap.servo.get("intakeLatch");
        blockPusher = hwMap.servo.get("blockPusher");

/*        armL.setPosition(LEFT_CLAMP_INIT);
        armR.setPosition(RIGHT_CLAMP_INIT);
        armT.setPosition(TOP_CLAMP_INIT);*/
        hammerY.setPosition(HAMMER_UP);
        hammerX.setPosition(HAMMER_CENTER);
        //plate.setPosition(PUSH_PLATE_UP);
        grabber.setPosition(IDOL_CLAMP_OPEN);
        intakeLatch.setPosition(LATCH_LOCKED);
        blockPusher.setPosition(BLOCK_PUSH);

        intakeFrontLeft.setPower(0);
        intakeFrontRight.setPower(0);
        intakeBackLeft.setPower(0);
        intakeBackRight.setPower(0);

        // Define Sensors
        gyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        color = hwMap.get(ModernRoboticsI2cColorSensor.class, "color");
        cubbyRange = hwMap.get(ModernRoboticsI2cRangeSensor.class, "cubbyRange");
        blockRange = hwMap.get(ModernRoboticsI2cRangeSensor.class, "blockRange");

        color.enableLed(false);

    }
 }
