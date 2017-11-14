package org.firstinspires.ftc.team7153;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="RedLeftByrdMK2")
public class  RedLeftByrdMK2 extends LinearOpMode {
	private DcMotor frontRight; // Front Right Motor // Runs in ? Direction //
	private DcMotor frontLeft; // Front Left Motor  // Runs in ? Direction //
	private DcMotor backRight; // Back Right Motor  // Runs in ? Direction //
	private DcMotor backLeft; // Back Left Motor   // Runs in ? Direction //

	private DcMotor forkX;//Useful link https://www.reddit.com/r/FTC/comments/3qhfvj/help_with_encoders/
	private DcMotor forkY;

	private DcMotor idolZ;
	private DcMotor idolY;

	private Servo armL;
	private Servo armR;
	private CRServo hammerY;
	private Servo hammerX;
	private Servo plateL;
	private Servo plateR;
	private Servo grabber;

	private TouchSensor touchL;
	private TouchSensor touchR;

	private ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //
	private ModernRoboticsI2cColorSensor color;

	double positionX;

	boolean mode;
	double positionY;
	final double travelY = 6;
	final double countsPerInchY = 1440 / 3;//1440 is the # of pulses 3 is the perimeter

	double positionIZ;
	double positionIY;

	boolean grab;
	boolean succ;
	boolean plate;

	long setTime;

	double FORWARD = 90;
	double LEFT=180;
	double BACK=270;
	double RIGHT=0;

	double error=0;
	int z = 0;

	void move(double angle, double time, double power) throws InterruptedException {
		if (!opModeIsActive()) {
			stopMoving();
			return;
		}
		double radGyro = (gyro.getIntegratedZValue() * Math.PI) / 180;
		double r = power;
		double robotAngle = angle * (Math.PI / 180) - Math.PI / 4 - radGyro;

		if (robotAngle > Math.PI) {
			robotAngle = Math.PI * -2 + robotAngle;
		} else if (robotAngle < -Math.PI) {
			robotAngle = Math.PI * 2 + robotAngle;
		}

		final double v1 = r * Math.cos(robotAngle);
		final double v2 = r * Math.sin(robotAngle);
		final double v3 = r * Math.sin(robotAngle);
		final double v4 = r * Math.cos(robotAngle);

		frontLeft.setPower(v1);
		frontRight.setPower(v2);
		backLeft.setPower(v3);
		backRight.setPower(v4);
		sleep((long) time);
		stopMoving();
		straighten();
	}

	void straighten() throws InterruptedException {
		while (gyro.getIntegratedZValue() < error - 2 || gyro.getIntegratedZValue() > error  + 2 && opModeIsActive()) {
			if (gyro.getIntegratedZValue()<-error) {
				frontLeft.setPower(.2);
				frontRight.setPower(-.2);
				backLeft.setPower(.2);
				backRight.setPower(-.2);
			} else if (gyro.getIntegratedZValue()>-.2) {
				frontLeft.setPower(-.2);
				frontRight.setPower(.2);
				backLeft.setPower(-.2);
				backRight.setPower(.2);
			}
		}
		stopMoving();
	}

	private void grab(boolean grab) {
		if (grab) {
			armL.setPosition(.4);
			armR.setPosition(1);
		} else {
			armL.setPosition(.7);
			armR.setPosition(.7);
		}
	}

	private void forkX(boolean mode) {
		if (mode) {
			forkX.setTargetPosition((int) ((1440 / (1.25 * 3.1415)) * 7));
		} else {
			forkX.setTargetPosition(0);
		}
	}

	private void turn(double angle, double speed) throws InterruptedException {
		angle -= 90;
		error=angle;
		while (gyro.getIntegratedZValue() < angle - 2 || gyro.getIntegratedZValue() > angle + 2 && opModeIsActive()) {
			if (gyro.getIntegratedZValue()<-angle) {
				frontLeft.setPower(speed);
				frontRight.setPower(-speed);
				backLeft.setPower(speed);
				backRight.setPower(-speed);
			} else if (gyro.getIntegratedZValue()>-angle) {
				frontLeft.setPower(-speed);
				frontRight.setPower(speed);
				backLeft.setPower(-speed);
				backRight.setPower(speed);
			}
		}
		stopMoving();
		straighten();
	}
	/*	if(angle>180+gyro.getIntegratedZValue() || angle<gyro.getIntegratedZValue()) {//turn left - 90
			while (gyro.getIntegratedZValue() < angle && angle > 0) {

			}
			while (gyro.getIntegratedZValue() > angle && angle < 0) {

			}
		}*/

	private void stopMoving() throws InterruptedException {
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
	private void hammer() throws InterruptedException {
		sleep(100);
		hammerY.setPower(0.1);
		color.enableLed(true);
		sleep(1000);
		setTime=System.currentTimeMillis();
		while(color.blue()==color.red() && System.currentTimeMillis()<setTime+1000 && opModeIsActive()){
			frontLeft.setPower(-.07);
			frontRight.setPower(-.07);
			backLeft.setPower(-.07);
			backRight.setPower(-.07);
		}
		sleep(100);
		stopMoving();
		sleep(1000);
		do {
			z+=1;
			sleep(100);
		} while(color.blue()==color.red() && z<10);
		if(color.blue()<color.red()){
			double temp=color.blue();
			telemetry.addData("Found Blue! ", temp);
			//move(180,500,.7);
			frontLeft.setPower(.5);
			frontRight.setPower(-.5);
			backLeft.setPower(.5);
			backRight.setPower(-.5);
			sleep(100);
			stopMoving();
			hammerY.setPower(.9);
			sleep(1000);
			//move(0,500,.7);
			frontLeft.setPower(-.5);
			frontRight.setPower(.5);
			backLeft.setPower(-.5);
			backRight.setPower(.5);
			sleep(100);
			stopMoving();
		} else if (color.blue()>color.red()){
			double temp=color.red();
			telemetry.addData("Found Red! ", temp);
			frontLeft.setPower(-.5);
			frontRight.setPower(.5);
			backLeft.setPower(-.5);
			backRight.setPower(.5);
			sleep(200);
			stopMoving();
			hammerY.setPower(.9);
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
		hammerY.setPower(.9);
		sleep(500);
	}

	private void touch(){

	}

	private void dismount(double direction) throws InterruptedException{
		//while(gyro.rawX()<3 || gyro.rawY()<3){}
		double X=0;
		double Y=0;
		for(int i=0; i<200; i++){
			move(direction,10,1);
			if(gyro.rawX()>X){X=gyro.rawX();}
			if(gyro.rawY()>Y){Y=gyro.rawY();}
			telemetry.addData("X:", X);
			telemetry.addData("Y:", Y);
			telemetry.update();
		}
		telemetry.addData("X:", X);
		telemetry.addData("Y:", Y);
		telemetry.update();
		sleep(10000);
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

		hammerY = hardwareMap.crservo.get("hammerY");
		hammerX = hardwareMap.servo.get("hammerX");
		grabber = hardwareMap.servo.get("grabber");

		plateL = hardwareMap.servo.get("plateL");
		plateR = hardwareMap.servo.get("plateR");

		touchL = hardwareMap.touchSensor.get("touchL");
		touchR = hardwareMap.touchSensor.get("touchR");

		forkX.setTargetPosition(0);
		forkX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		forkY.setTargetPosition(0);
		forkY.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		idolZ.setTargetPosition(1000);
		idolZ.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		armL.setPosition(.8);
		armR.setPosition(.7);
		hammerY.setPower(.5);
		hammerX.setPosition(0);
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
			/*grab(true);
			forkX(true);
			hammer();
			move(90, 500, .2);
			move(0, 3000, 1);
			turn(200,.5);
			move(200,2000,.6);
			grab(false);
			forkX(false);
			stopMoving();*/
			dismount(0);
			for(double i=-1; i==1; i+=.1){
				hammerX.setPosition(i);
				telemetry.addData("Position", i);
				sleep(2000);
			}
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
