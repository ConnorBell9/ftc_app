package org.firstinspires.ftc.team7153;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="BlueRightByrd" , group = "Archaic")
public class BlueRightByrd extends LinearOpMode {
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
	final double countsPerInchY = 1440/3;//1440 is the # of pulses 3 is the perimeter

	double positionIZ;
	double positionIY;

	boolean grab;
	boolean succ;
	boolean plate;

	long setTime;

	boolean LEFT=false;
	boolean RIGHT=true;

	int z=0;

	void move(double angle, double time, double power) throws InterruptedException {
		if (!opModeIsActive()) {
			stopMoving();
			return;
		}
		double radGyro=(gyro.getIntegratedZValue()*Math.PI)/180;
		double r = power;
		double robotAngle = angle*(Math.PI/180) - Math.PI / 4 - radGyro;

		if(robotAngle>Math.PI){
			robotAngle=Math.PI*-2+robotAngle;
		} else if(robotAngle<-Math.PI){
			robotAngle=Math.PI*2+robotAngle;
		}

		final double v1 = r * Math.cos(robotAngle);
		final double v2 = r * Math.sin(robotAngle);
		final double v3 = r * Math.sin(robotAngle);
		final double v4 = r * Math.cos(robotAngle);

		frontLeft.setPower(v1);
		frontRight.setPower(v2);
		backLeft.setPower(v3);
		backRight.setPower(v4);
		sleep((long)time);
		stopMoving();
	}

	void straighten(){
		gyro.getIntegratedZValue();
	}

	void grab(boolean grab){
		if(grab){
			armL.setPosition(.4);
			armR.setPosition(1);
		} else {
			armL.setPosition(.7);
			armR.setPosition(.7);
		}
	}

	void turn(double angle, double speed, boolean direction) throws InterruptedException {
		if(direction){
			angle-=90;
			if(angle<0){angle=+360;}
			while(gyro.getIntegratedZValue()>angle && opModeIsActive()){
				frontLeft.setPower(speed);
				frontRight.setPower(-speed);
				backLeft.setPower(speed);
				backRight.setPower(-speed);
			}
		} else {
			while (gyro.getIntegratedZValue() < angle && opModeIsActive()) {
				frontLeft.setPower(-speed);
				frontRight.setPower(speed);
				backLeft.setPower(-speed);
				backRight.setPower(speed);
			}
			stopMoving();
		}
	/*	if(angle>180+gyro.getIntegratedZValue() || angle<gyro.getIntegratedZValue()) {//turn left - 90
			while (gyro.getIntegratedZValue() < angle && angle > 0) {

			}
			while (gyro.getIntegratedZValue() > angle && angle < 0) {

			}
		}*/
	}

	void stopMoving() throws InterruptedException {
		frontLeft.setPower(0);
		frontRight.setPower(0);
		backLeft.setPower(0);
		backRight.setPower(0);
		sleep(500);
	}

	/*void hammer() throws InterruptedException {
		sleep(100);
		hammer.setPosition(0);
		sleep(500);
		setTime = System.currentTimeMillis();
		while(color.blue()<.3 || color.red()<.3 || System.currentTimeMillis()<3000+setTime){}
		if(color.blue()>.3){
			double temp=color.blue();
			telemetry.addData("Found Blue! ", temp);
			move(180,6,.6);
			move(0,6,.6);
		} else if (color.red()>.3){
			double temp=color.red();
			telemetry.addData("Found Red! ", temp);
			move(0,6,.6);
			move(180,6,.6);
		} else{telemetry.addData("Found Nothing! ",0);}
		telemetry.update();
		hammer.setPosition(.9);
		sleep(500);b
	}*/
	void hammer() throws InterruptedException {
		sleep(100);
		hammer.setPosition(0.1);
		color.enableLed(true);
		sleep(1000);
		setTime=System.currentTimeMillis();
		while(color.blue()==color.red() && System.currentTimeMillis()<setTime+500 && opModeIsActive()){
			frontLeft.setPower(-.07);
			frontRight.setPower(-.07);
			backLeft.setPower(-.07);
			backRight.setPower(-.07);
		}
		stopMoving();
		sleep(1000);
		do {
			z+=1;
			sleep(100);
		} while(color.blue()==color.red() && z<10);
		if(color.blue()>color.red()){
			double temp=color.blue();
			telemetry.addData("Found Blue! ", temp);
			//move(180,500,.7);
			frontLeft.setPower(.5);
			frontRight.setPower(-.5);
			backLeft.setPower(.5);
			backRight.setPower(-.5);
			sleep(200);
			stopMoving();
			hammer.setPosition(.9);
			sleep(1000);
			//move(0,500,.7);
			frontLeft.setPower(-.5);
			frontRight.setPower(.5);
			backLeft.setPower(-.5);
			backRight.setPower(.5);
			sleep(200);
			stopMoving();
		} else if (color.blue()<color.red()){
			double temp=color.red();
			telemetry.addData("Found Red! ", temp);
			frontLeft.setPower(-.5);
			frontRight.setPower(.5);
			backLeft.setPower(-.5);
			backRight.setPower(.5);
			sleep(200);
			stopMoving();
			hammer.setPosition(.9);
			sleep(1000);
			//move(0,500,.7);
			frontLeft.setPower(.5);
			frontRight.setPower(-.5);
			backLeft.setPower(.5);
			backRight.setPower(-.5);
			sleep(200);
			stopMoving();
		} else{
			telemetry.addData("Found Nothing! ",0);
			telemetry.addData("Blue:", color.blue());
			telemetry.addData("Red:", color.red());
		}
		telemetry.update();
		color.enableLed(false);
		hammer.setPosition(.9);
		sleep(500);
	}
	@Override
	public void runOpMode() throws InterruptedException {
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

		plateL.setPosition(1);
		plateR.setPosition(0);

		telemetry.addData(">", "Gyro Calibrating. Do Not move!");
		telemetry.update();
		gyro.calibrate();

		while(!isStopRequested() && gyro.isCalibrating()){
			Thread.sleep(50);
			idle();
		}

		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();

		color.enableLed(false);

		waitForStart();

		gyro.resetZAxisIntegrator();
		if(!isStopRequested()) {
			/*while(true && opModeIsActive()){
				color.enableLed(true);
				hammer.setPosition(.3);
				telemetry.addData("Color Red", color.red());
				telemetry.addData("Color Blue", color.blue());
				telemetry.update();
			}*/
			grab(true);
			hammer();
			move(90, 500, .2);
			move(180, 3000, 1);
			turn(135,.5,LEFT);
			move(270,2000,.6);
			grab(false);
			move(90,750,.3);
			stopMoving();


		}
	}
}



//turn concept code
/*
	double Oangle=0;
	double Ogyro=0;
        while(Oangle<=360){
				double gyro=Math.sin((Ogyro+180)*Math.PI/180);
				double angle=Math.sin((Oangle+180)*(Math.PI/180));
				if(gyro<=Math.sin((Math.asin(angle))+Math.PI/90) && gyro>=Math.sin((Math.asin(angle))-Math.PI/90)){
				System.out.println("Go straight");
				}else if(gyro-angle<0){
		System.out.println("Turn Right");
		}else{
		System.out.println("Turn Left");
		}
		System.out.println(Oangle);
		Oangle+=1;
		}
		*/