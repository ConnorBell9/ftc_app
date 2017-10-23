package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="ServoByrd")
public class ServoByrd extends OpMode{

	DcMotor frontRight; // Front Right Motor // Runs in ? Direction //
	DcMotor frontLeft; // Front Left Motor  // Runs in ? Direction //
	DcMotor backRight; // Back Right Motor  // Runs in ? Direction //
	DcMotor backLeft; // Back Left Motor   // Runs in ? Direction //
	
	DcMotor forkX;//Useful link https://www.reddit.com/r/FTC/comments/3qhfvj/help_with_encoders/
	DcMotor forkY;
	
	DcMotor idolX;
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

	double positionX;

	boolean mode;
	double positionY;
	final double travelY = 6;
	final double countsPerInchY = 1440/3;//1440 is the # of pulses 3 is the perimeter

	boolean grab;
	boolean succ;

	long setTime;



    @Override
    public void init() {
	    gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
	    gyro.calibrate();
	    frontRight = hardwareMap.dcMotor.get("fr");
	    frontLeft = hardwareMap.dcMotor.get("fl");
	    backRight = hardwareMap.dcMotor.get("br");
	    backLeft = hardwareMap.dcMotor.get("bl");
	    
	    forkX = hardwareMap.dcMotor.get("forkX");
	    forkY = hardwareMap.dcMotor.get("forkY");
	    
	    idolX = hardwareMap.dcMotor.get("idolX");
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
		
	    armL.setPosition(.5);
	    armR.setPosition(.2);
	    hammer.setPosition(.9);
    }

    @Override
    public void loop() {
	    /*double maxSpeed = 1;//Defines what fraction of speed the robot will run at
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
	    backRight.setPower(v4*maxSpeed);*/
         
	    if(gamepad2.y && System.currentTimeMillis() > setTime+500){
		    if(mode){mode=false;}else{mode=true;}
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

	    if(succ){
			hammer.setPosition(gamepad1.left_stick_x);
	    } else {
		    suckL.setPower(.5);
		    suckR.setPower(.5);
	    }

	    if(grab){
		    plateL.setPosition(gamepad1.left_stick_x);
		    plateR.setPosition(gamepad1.right_stick_x);
	    } else {
		    armL.setPosition(1);
		    armR.setPosition(.8);
	    }
		
	    if(mode){
			if(gamepad2.dpad_right){
				forkX.setPower(.2);
			} else if(gamepad2.dpad_left){
				forkX.setPower(-.2);
			} else {forkX.setPower(0);}
			if(gamepad2.dpad_up){
				forkY.setPower(.2);
			} else if (gamepad2.dpad_down){
				forkY.setPower(-.2);
			} else {forkY.setPower(0);}
		} else {
			forkX.setPower(.5);
			forkY.setPower(.5);
			forkY.setTargetPosition((int)positionY);
			forkX.setTargetPosition((int)positionX);
			if(gamepad2.dpad_up && !forkY.isBusy() && positionY<18*countsPerInchY && positionX > 0){
				positionY-=travelY*countsPerInchY;
			} else if(gamepad2.dpad_down && !forkY.isBusy() && positionY>0){
				positionY+=travelY*countsPerInchY;
			}

			if(gamepad2.dpad_right){
				positionX = ((1440/(1.25*3.1415))*6);//1440 is the # of pulses 1.25 is the diameter and 11 is the # of inches traveled
			} else if(gamepad2.dpad_left){
				positionX = 0;
			}
		}

	    telemetry.addData("Mode is: ", mode);
	    telemetry.addData("Grab is: ", grab);
	    telemetry.addData("Broom is: ", succ);
	    telemetry.addData("forkY Running to: ", positionY);
	    telemetry.addData("forkY Running at: ", forkY.getCurrentPosition());
	    telemetry.addData("forkX Running to: ", positionX);
	    telemetry.addData("forkX Running at: ", forkX.getCurrentPosition());
	    telemetry.addData("Left", gamepad1.left_stick_x);
	    telemetry.addData("Right", gamepad1.right_stick_x);
	    /*telemetry.addData("backLeft", v3);
	    telemetry.addData("backRight", v4);*/
	    telemetry.addData("Gyro", gyro);
	    telemetry.update();
    }
}
