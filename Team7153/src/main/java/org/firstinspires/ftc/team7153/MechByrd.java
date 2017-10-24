package org.firstinspires.ftc.team7153;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;


@TeleOp(name="MechByrd")
public class MechByrd extends OpMode{

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
	Servo hammer;
	Servo plateL;
	Servo plateR;
	Servo grabber;
	
	ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //
	ModernRoboticsI2cColorSensor color;

	double positionX;

	boolean mode;
	double positionY;
	final double travelY = 6;
	final double countsPerInchY = 1440/2.75;//1440 is the # of pulses 3 is the perimeter

	double positionIZ;
	double positionIY;

	boolean grab;
	boolean succ;
	boolean plate;
	boolean idolGrab;

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
	    
	    hammer = hardwareMap.servo.get("hammer");
	    grabber = hardwareMap.servo.get("grabber");
	    
	    plateL = hardwareMap.servo.get("plateL");
	    plateR = hardwareMap.servo.get("plateR");
        
	    forkX.setTargetPosition(0);
	    forkX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	    forkY.setTargetPosition(0);
	    forkY.setMode(DcMotor.RunMode.RUN_TO_POSITION);

	    idolZ.setTargetPosition(1000);
	    idolZ.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		
	    armL.setPosition(.8);
	    armR.setPosition(.7);
	    hammer.setPosition(.9);
	    grabber.setPosition(.5);

	    plateL.setPosition(0);
	    plateR.setPosition(1);
	    
	    color.enableLed(false);
    }

    @Override
    public void loop() {
	    double maxSpeed = 1;//Defines what fraction of speed the robot will run at
	    double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
	    double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
	    double rightX = gamepad1.right_stick_x;
	    final double v1 = r * Math.cos(robotAngle) + rightX;
	    final double v2 = r * Math.sin(robotAngle) - rightX;
	    final double v3 = r * Math.sin(robotAngle) + rightX;
	    final double v4 = r * Math.cos(robotAngle) - rightX;

	    frontLeft.setPower(v1*maxSpeed);
	    frontRight.setPower(v2*maxSpeed);
	    backLeft.setPower(v3*maxSpeed);
	    backRight.setPower(v4*maxSpeed);
         
	    if(gamepad2.y && System.currentTimeMillis() > setTime+500){
		    if(mode){mode=false;forkY.setMode(DcMotor.RunMode.RUN_TO_POSITION);forkX.setMode(DcMotor.RunMode.RUN_TO_POSITION);}else{mode=true;forkY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);forkX.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}
			setTime = System.currentTimeMillis();
	    }

	    if(gamepad2.a && System.currentTimeMillis() > setTime+500){
			if(grab){grab=false;}else{grab=true;}
			setTime = System.currentTimeMillis();
	    }

	    if(gamepad1.a && System.currentTimeMillis() > setTime+500){
			if(succ){succ=false;}else{succ=true;}
			setTime = System.currentTimeMillis();
	    }

	    if(gamepad1.b && System.currentTimeMillis() > setTime+500){
		    if(plate){plate=false;}else{plate=true;}
		    setTime = System.currentTimeMillis();
	    }

	    if(gamepad1.y && System.currentTimeMillis() > setTime+500){
		    if(idolGrab){idolGrab=false;}else{idolGrab=true;}
		    setTime = System.currentTimeMillis();
	    }

	    if(succ){
		    suckL.setPower(1);
		    suckR.setPower(0);
	    } else {
		    suckL.setPower(.5);
		    suckR.setPower(.5);
	    }

	    if(grab){
		    armL.setPosition(.4);
		    armR.setPosition(1);
	    } else {
		    armL.setPosition(.8);
		    armR.setPosition(.7);
	    }

	    if(plate){
			plateL.setPosition(1);
			plateR.setPosition(0);
		} else {
			plateL.setPosition(0);
			plateR.setPosition(1);
		}

		if(idolGrab){
			grabber.setPosition(1);
		} else {
			grabber.setPosition(.5);
		}

	    if(mode){
			if(gamepad2.left_stick_y>.1){
				forkY.setPower(gamepad2.left_stick_y);
			} else if(gamepad2.left_stick_y<-.1){
				forkY.setPower(gamepad2.left_stick_y*.5);
			} else {forkY.setPower(0);}
		} else {
			forkX.setPower(.5);
			forkY.setPower(.5);
			forkY.setTargetPosition((int)positionY);
			forkX.setTargetPosition((int)positionX);
			if(gamepad2.dpad_up && !forkY.isBusy() && positionY>-18*countsPerInchY && positionX > 0){
				positionY-=travelY*countsPerInchY;
				if(positionY>18*countsPerInchY){positionY=14*countsPerInchY;}
			} else if(gamepad2.dpad_down && !forkY.isBusy() && positionY<0){
				positionY+=travelY*countsPerInchY;
			}

			if(gamepad2.dpad_right){
				positionX = ((1440/(1.25*3.1415))*7);//1440 is the # of pulses 1.25 is the diameter and 11 is the # of inches traveled
			} else if(gamepad2.dpad_left){
				positionX = 0;
			}
		}

		idolZ.setPower(.5);
		idolZ.setTargetPosition((int)positionIZ);

		if(gamepad2.right_trigger>.1 && positionIY<100000){
			idolY.setPower(.5*gamepad2.right_trigger);
		} else if (gamepad2.left_trigger>.1){
			idolY.setPower(-.5*gamepad2.left_trigger);
		} else if(gamepad2.right_bumper && positionIZ < 100000){
			positionIZ+=100;
			//idolY.setPower(-.4);
		}else if(gamepad2.left_bumper && positionIZ > -10000){
			positionIZ-=100;
			//idolY.setPower(.4);
		} else {
			idolY.setPower(0);
		}

	    telemetry.addData("Mode is: ", mode);
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
	    telemetry.addData("frontLeft", v1);
	    telemetry.addData("frontRight", v2);
	    telemetry.addData("backLeft", v3);
	    telemetry.addData("backRight", v4);
	    telemetry.addData("Gyro", gyro);
	    telemetry.addData("Color Blue: ", color.blue());
	    telemetry.addData("Color Red: ", color.red());
	    telemetry.update();
    }
}
