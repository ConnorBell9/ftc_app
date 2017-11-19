package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static org.firstinspires.ftc.team7153.HardwareByrd.*;

@Autonomous(name="RedLeftByrdMK3" , group = "New")
public class RedLeftByrdMK3 extends LinearOpMode {
	private HardwareByrd robot = new HardwareByrd();
	private double imaginaryAngle=0;

	private void move(double angle, double time, double power) throws InterruptedException {
		if (!opModeIsActive()) {
			stopMoving();
			return;
		}
		double radGyro = (robot.gyro.getIntegratedZValue() * Math.PI) / 180;
		double robotAngle = angle * (Math.PI / 180) - Math.PI / 4 - radGyro;

		if (robotAngle > Math.PI) {
			robotAngle = Math.PI * -2 + robotAngle;
		} else if (robotAngle < -Math.PI) {
			robotAngle = Math.PI * 2 + robotAngle;
		}

		final double v1 = power * Math.cos(robotAngle);
		final double v2 = power * Math.sin(robotAngle);
		final double v3 = power * Math.sin(robotAngle);
		final double v4 = power * Math.cos(robotAngle);

		robot.frontLeft.setPower(v1);
		robot.frontRight.setPower(v2);
		robot.backLeft.setPower(v3);
		robot.backRight.setPower(v4);
		sleep((long) time);
		stopMoving();
		straighten();
	}

	private void straighten() throws InterruptedException {
		while (robot.gyro.getIntegratedZValue() < imaginaryAngle - 2 || robot.gyro.getIntegratedZValue() > imaginaryAngle + 2 && opModeIsActive()) {
			if (robot.gyro.getIntegratedZValue() < -imaginaryAngle) {
				robot.frontLeft.setPower(.2);
				robot.frontRight.setPower(-.2);
				robot.backLeft.setPower(.2);
				robot.backRight.setPower(-.2);
			} else if (robot.gyro.getIntegratedZValue() > -.2) {
				robot.frontLeft.setPower(-.2);
				robot.frontRight.setPower(.2);
				robot.backLeft.setPower(-.2);
				robot.backRight.setPower(.2);
			}
		}
		stopMoving();
	}

	private void grab(boolean grab) {
		if (grab) {
			robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
		} else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
		}
	}

	private void forkX(boolean mode) {
		if (mode) {
			robot.forkX.setTargetPosition((int)LIFT_X_OUT);
		} else {
			robot.forkX.setTargetPosition((int)LIFT_X_IN);
		}
	}

	private void turn(double angle, double speed) throws InterruptedException {
		angle -= 90;
		imaginaryAngle = angle;
		while (robot.gyro.getIntegratedZValue() < angle - 2 || robot.gyro.getIntegratedZValue() > angle + 2 && opModeIsActive()) {
			if (robot.gyro.getIntegratedZValue() < -angle) {
				robot.frontLeft.setPower(speed);
				robot.frontRight.setPower(-speed);
				robot.backLeft.setPower(speed);
				robot.backRight.setPower(-speed);
			} else if (robot.gyro.getIntegratedZValue() > -angle) {
				robot.frontLeft.setPower(-speed);
				robot.frontRight.setPower(speed);
				robot.backLeft.setPower(-speed);
				robot.backRight.setPower(speed);
			}
		}
		stopMoving();
		straighten();
	}

	private void stopMoving() throws InterruptedException {
		robot.frontLeft.setPower(0);
		robot.frontRight.setPower(0);
		robot.backLeft.setPower(0);
		robot.backRight.setPower(0);
		sleep(500);
	}

	private void hammer(boolean colorRemaining) throws InterruptedException {
		robot.color.enableLed(true);
		robot.hammerY.setPower(HAMMER_DOWN);
		sleep(1000);
		resetTimer();
		while (robot.color.blue() == robot.color.red() && System.currentTimeMillis() < INPUT_TIMER + 1000 && opModeIsActive()) {
			robot.frontLeft.setPower(-.07);
			robot.frontRight.setPower(-.07);
			robot.backLeft.setPower(-.07);
			robot.backRight.setPower(-.07);
		}
		sleep(500);
		stopMoving();
		sleep(1000);
		while(robot.color.red()>robot.color.blue()){
			if(colorRemaining==RED){putt(LEFT);}  else {putt(RIGHT);}
		}
		while(robot.color.red()<robot.color.blue()){
			if(colorRemaining==RED){putt(RIGHT);} else {putt(LEFT);}
		}

		robot.color.enableLed(false);
		sleep(500);
	}

	private void putt(boolean direction){
		if(direction == RIGHT){
			robot.hammerX.setPosition(HAMMER_RIGHT);
			resetTimer();
			while(robot.hammerX.getPosition()!=HAMMER_RIGHT && INPUT_TIMER+2000<System.currentTimeMillis()) {
				telemetry.addData("Hammer Position: ", robot.hammerX.getPosition());
				telemetry.addData("Hammer Target Position: ", HAMMER_RIGHT);
				telemetry.addData("Time Left: ", INPUT_TIMER+2000-System.currentTimeMillis());
				telemetry.update();
			}
			robot.hammerX.setPosition(HAMMER_CENTER);
			sleep(1000);
		} else {
			robot.hammerX.setPosition(HAMMER_LEFT);
			resetTimer();
			while(robot.hammerX.getPosition()!=HAMMER_LEFT && INPUT_TIMER+2000<System.currentTimeMillis()) {
				telemetry.addData("Hammer Position: ", robot.hammerX.getPosition());
				telemetry.addData("Hammer Target Position: ", HAMMER_LEFT);
				telemetry.addData("Time Left: ", INPUT_TIMER+2000-System.currentTimeMillis());
				telemetry.update();
			}
			robot.hammerX.setPosition(HAMMER_CENTER);
			sleep(1000);
		}
	}

	private void vuCubby(double direction) throws InterruptedException{
		robot.color.enableLed(true);

		resetTimer();
		while(robot.color.blue()+robot.color.red()< 100){
			move(180,10,.7);
		}
	}

	private void insert(double direction) throws InterruptedException{
		turn(270,.5);
		move(270,1000,.2);
		grab(false);
		forkX(false);
		move(270,200,.2);
		move(90,500,.2);
		turn(90,.5);
	}

	private void resetTimer(){
		INPUT_TIMER = System.currentTimeMillis();
	}

	private void dismount(double direction) throws InterruptedException {
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			move(direction, 10,1);
		}
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			move(direction, 10,1);
		}
		stopMoving();
	}

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		robot.color.enableLed(false);

		telemetry.addData(">", "Gyro Calibrating. Do Not move!");
		telemetry.update();
		robot.gyro.calibrate();

		while (!isStopRequested() && robot.gyro.isCalibrating()) {
			Thread.sleep(50);
			idle();
		}

		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();

		waitForStart();

		robot.gyro.resetZAxisIntegrator();
		if (!isStopRequested()) {
			grab(true);
			forkX(true);
			hammer(RED);
			dismount(0);
			vuCubby(0);
			insert(0);
		}
	}
}