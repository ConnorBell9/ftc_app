package org.firstinspires.ftc.team7153;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="TestOp" , group = "Archaic")
public class TestOp extends OpMode{

	DcMotor frontRight; // Front Right Motor // Runs in ? Direction //
	DcMotor frontLeft; // Front Left Motor  // Runs in ? Direction //
	DcMotor backRight; // Back Right Motor  // Runs in ? Direction //
	DcMotor backLeft; // Back Left Motor   // Runs in ? Direction //
	
	DcMotor forkX;//Useful link https://www.reddit.com/r/FTC/comments/3qhfvj/help_with_encoders/
	DcMotor forkY;
	
	DcMotor idolZ;
	DcMotor idolY;


	Servo armL;
	Servo armR;
	CRServo suckL;
	CRServo suckR;
	Servo hammerX;
	CRServo hammerY;
	Servo plateL;
	Servo plateR;
	Servo grabber;

	ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //
	ModernRoboticsI2cColorSensor color;

	double positionX = ((1440/(1.25*3.1415))*7);

	double positionY;
	final double travelY = 6;
	final double countsPerInchY = 1440/3;//1440 is the # of pulses 3 is the perimeter

	double positionIZ=600;
	double positionIY;

	boolean grab = true;
	boolean succ;
	boolean plate = false;
	boolean idolGrab;
	boolean mode=false;

	long setTime;



    @Override
    public void init() {
	    gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
		color = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "color");
	    frontRight = hardwareMap.dcMotor.get("fr");
	    frontLeft = hardwareMap.dcMotor.get("fl");
	    backRight = hardwareMap.dcMotor.get("br");
	    backLeft = hardwareMap.dcMotor.get("bl");

	    forkX = hardwareMap.dcMotor.get("forkX");
	    forkY = hardwareMap.dcMotor.get("forkY");

	    idolZ = hardwareMap.dcMotor.get("idolZ");
	    idolY = hardwareMap.dcMotor.get("idolY");

	    forkY.setDirection(DcMotorSimple.Direction.REVERSE);
	    frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
	    backRight.setDirection(DcMotorSimple.Direction.REVERSE);

	    armL = hardwareMap.servo.get("armL");
	    armR = hardwareMap.servo.get("armR");
	    suckL = hardwareMap.crservo.get("suckL");
	    suckR = hardwareMap.crservo.get("suckR");

	    hammerY = hardwareMap.crservo.get("hammerY");
	    hammerX = hardwareMap.servo.get("hammerX");
	    grabber = hardwareMap.servo.get("grabber");

	    plateL = hardwareMap.servo.get("plateL");
	    plateR = hardwareMap.servo.get("plateR");

	    forkX.setTargetPosition(0);
	    forkX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	    forkX.setPower(.5);
		forkY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		idolZ.setTargetPosition(0);
		idolZ.setMode(DcMotor.RunMode.RUN_TO_POSITION);

	    armL.setPosition(.4);
	    armR.setPosition(1);
	    hammerY.setPower(.9);
		grabber.setPosition(0);

		plateL.setPosition(1);
		plateR.setPosition(0);

		telemetry.addData(">", "Gyro Calibrating. Do Not move!");
		telemetry.update();
		gyro.calibrate();
		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();
    }

    @Override
    public void loop() {
    	if(!mode) {
			double maxSpeed = 1;//Defines what fraction of speed the robot will run atb
			double radGyro = (gyro.getHeading() * Math.PI) / 180;
			double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
			double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4 - radGyro;
			double rightX = gamepad1.right_stick_x;
			final double v1 = r * Math.cos(robotAngle) + rightX;
			final double v2 = r * Math.sin(robotAngle) - rightX;
			final double v3 = r * Math.sin(robotAngle) + rightX;
			final double v4 = r * Math.cos(robotAngle) - rightX;

			frontLeft.setPower(v1 * maxSpeed);
			frontRight.setPower(v2 * maxSpeed);
			backLeft.setPower(v3 * maxSpeed);
			backRight.setPower(v4 * maxSpeed);
		} else {
			frontLeft.setPower(0);
			frontRight.setPower(0);
			backLeft.setPower(0);
			backRight.setPower(0);
			hammerX.setPosition(gamepad1.left_stick_x);
			hammerY.setPower(gamepad1.left_stick_y);
			telemetry.addData("HammerX is at : ", hammerX.getPosition());
			telemetry.addData("HammerY is at : ", hammerY.getPower());
			telemetry.update();

		}

	    if(gamepad2.a && System.currentTimeMillis() > setTime+500){
			if(grab){grab=false;}else{grab=true;}
			setTime = System.currentTimeMillis();
	    }

		if(gamepad1.b && System.currentTimeMillis() > setTime+500){
			if(plate){plate=false;}else{plate=true;}
			setTime = System.currentTimeMillis();
		}

		if(gamepad1.x && System.currentTimeMillis() > setTime+500){
			if(idolGrab){idolGrab=false;}else{idolGrab=true;}
			setTime = System.currentTimeMillis();
		}

		if(gamepad1.y && System.currentTimeMillis() > setTime+500){
			if(mode){mode=false;}else{mode=true;}
			setTime = System.currentTimeMillis();
		}

	    if(grab){
		    armL.setPosition(.4);
		    armR.setPosition(1);
	    } else {
		    armL.setPosition(.7);
		    armR.setPosition(.7);
	    }

	    if(plate){
			plateL.setPosition(0);
			plateR.setPosition(1);
		} else {
			plateL.setPosition(1);
			plateR.setPosition(0);
		}

		if(idolGrab){
			grabber.setPosition(1);
		} else {
			grabber.setPosition(0);
		}

		if(gamepad2.left_stick_y>.1) {
			forkY.setPower(gamepad2.left_stick_y);
		} else if(gamepad2.left_stick_y <- .1) {
			forkY.setPower(gamepad2.left_stick_y * .5);
		} else {forkY.setPower(0);}
		if(gamepad2.dpad_right) {
			positionX = ((1440 / (1.25 * 3.1415)) * 7);//1440 is the # of pulses 1.25 is the diameter and 11 is the # of inches traveled
		} else if(gamepad2.dpad_left) {
			positionX = 0;
		}

		idolZ.setPower(.5);
		idolZ.setTargetPosition((int)positionIZ);
		forkX.setTargetPosition((int)positionX);

		if(gamepad2.right_trigger>.1 && positionIY<100000){
			idolY.setPower(.5*gamepad2.right_trigger);
		} else if (gamepad2.left_trigger>.1){
			idolY.setPower(-.5*gamepad2.left_trigger);
		} else {idolY.setPower(0);}
		if(gamepad2.right_bumper && System.currentTimeMillis() > setTime+10){
			positionIZ+=21.6;
			setTime = System.currentTimeMillis();
		}else if(gamepad2.left_bumper && System.currentTimeMillis() > setTime+10){
			positionIZ-=21.6;
			setTime = System.currentTimeMillis();
		}

		/*telemetry.addData("Mode is: ", mode);
	    telemetry.addData("Grab is: ", grab);
	    telemetry.addData("Broom is: ", succ);
		telemetry.addData("Plate is: ", plate);
	    telemetry.addData("forkY Running to: ", positionY);
		telemetry.addData("forkY Running at: ", forkY.getCurrentPosition());
		telemetry.addData("forkX Running to: ", positionX);
		telemetry.addData("forkX Running at: ", forkX.getCurrentPosition());
		telemetry.addData("idolY Running to: ", positionIY);
		telemetry.addData("idolY Running at: ", idolY.getCurrentPosition());
		telemetry.addData("idolZ Running to: ", positionIZ);
		telemetry.addData("idolZ Running at: ", idolZ.getCurrentPosition());
	    telemetry.addData("Gyro", gyro);
		telemetry.addData("Color Blue: ", color.blue());
		telemetry.addData("Color Red: ", color.red());
	    telemetry.update();
	    */
    }
}
