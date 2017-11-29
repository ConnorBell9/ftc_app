package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static org.firstinspires.ftc.team7153.HardwareByrd.*;

@Autonomous(name="RedLeftByrdMK3")
public class RedLeftByrdMK3 extends LinearOpMode {
	private HardwareByrd robot = new HardwareByrd();
	private double imaginaryAngle=0;

	private void move(double angle, double time, double power) throws InterruptedException {
		if (!opModeIsActive()) {
			stopMoving();
			return;
		}
		straighten();
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
		telemetry.addData("Function: ", "Move");
		telemetry.addData("FrontLeft:  ", robot.frontLeft.getPower());
		telemetry.addData("FrontRight: ", robot.frontRight.getPower());
		telemetry.addData("BackLeft:   ", robot.backLeft.getPower());
		telemetry.addData("BackRight:  ", robot.backRight.getPower());
		telemetry.addData("Moving at Angle: ", angle);
		telemetry.addData("Moving for Time: ", time);
		telemetry.addData("Moving at Speed: ", power);
		telemetry.update();
		sleep((long) time);
		stopMoving();
	}

	private void moveWithoutStopping(double angle, double power) throws InterruptedException {
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
		telemetry.addData("Function: ", "MoveWithoutStopping");
		telemetry.addData("FrontLeft:  ", robot.frontLeft.getPower());
		telemetry.addData("FrontRight: ", robot.frontRight.getPower());
		telemetry.addData("BackLeft:   ", robot.backLeft.getPower());
		telemetry.addData("BackRight:  ", robot.backRight.getPower());
		telemetry.addData("Moving at Angle: ", angle);
		telemetry.addData("Moving at Speed: ", power);
		telemetry.update();
	}

	private void straighten() throws InterruptedException {
		turn(imaginaryAngle,.4);
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
		imaginaryAngle = angle;
		while(angle > (robot.gyro.getHeading()+2)%360 || angle < robot.gyro.getHeading()-2){
		    if((angle>robot.gyro.getHeading() && angle<robot.gyro.getHeading()+181) || (angle<robot.gyro.getHeading()-180)){
                robot.frontLeft.setPower(-speed);
                robot.frontRight.setPower(speed);
                robot.backLeft.setPower(-speed);
                robot.backRight.setPower(speed);
            } else {
                robot.frontLeft.setPower(speed);
                robot.frontRight.setPower(-speed);
                robot.backLeft.setPower(speed);
                robot.backRight.setPower(-speed);
            }
			telemetry.addData("Function: ", "Turn");
			telemetry.addData("Current Angle: ", robot.gyro.getIntegratedZValue());
			telemetry.addData("Target Angle:  ", angle);
			telemetry.addData("Current Speed: ", speed);
			telemetry.update();
        }
		/*while (robot.gyro.getIntegratedZValue() < angle - 2 || robot.gyro.getIntegratedZValue() > angle + 2 && !isStopRequested()) {
		    if(robot.gyro.getIntegratedZValue()>-angle) {

            }
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
		*/
		stopMoving();
	}

	private void stopMoving() throws InterruptedException {
		robot.frontLeft.setPower(0);
		robot.frontRight.setPower(0);
		robot.backLeft.setPower(0);
		robot.backRight.setPower(0);
		sleep(500);
	}

	private void hammer(boolean colorRemaining) throws InterruptedException {
		telemetry.addData("Function: ", "Hammer");
		telemetry.update();
		robot.color.enableLed(true);
		robot.hammerY.setPower(HAMMER_DOWN);
		sleep(1000);
		resetTimer();
		scan();
		while (robot.color.blue() == robot.color.red() && System.currentTimeMillis() < INPUT_TIMER + 1000) {
			moveWithoutStopping(MOVE_BACKWARDS,.08);
		}
		stopMoving();
		telemetry.addData("Function: ", "Hammer");
		telemetry.update();
		sleep(1000);
		scan();
		if(robot.color.red()>robot.color.blue()){
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(LEFT);} else {putt(RIGHT);}
		} else if (robot.color.red()<robot.color.blue()){
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(RIGHT);} else {putt(LEFT);}
		}
		robot.hammerY.setPower(HAMMER_UP);
		robot.hammerX.setPosition(HAMMER_CENTER);
		sleep(1000);
		robot.color.enableLed(false);
		sleep(500);
	}

	private void scan(){
		robot.hammerX.setPosition(HAMMER_CENTER-.1);
	}

	private void putt(boolean direction){
		telemetry.addData("Function: ", "Putt");
		telemetry.update();
		if(direction == RIGHT){
			telemetry.addData("Hammer Position: ", "Right");
			telemetry.update();
			robot.hammerX.setPosition(HAMMER_RIGHT);
			sleep(2000);
		} else {
			telemetry.addData("Hammer Position: ", "Left");
			telemetry.update();
			robot.hammerX.setPosition(HAMMER_LEFT);
			sleep(2000);
		}
	}

	private void vuCubby(boolean direction, int target) throws InterruptedException{
		robot.color.enableLed(true);
		while(target>0){
			robot.hammerX.setPosition(HAMMER_CENTER);
			robot.hammerY.setPower(HAMMER_DOWN);
			moveToCubby(direction);
			if(direction){
				robot.hammerX.setPosition(HAMMER_LEFT);
			} else {
				robot.hammerX.setPosition(HAMMER_RIGHT);
			}
			robot.hammerY.setPower(HAMMER_UP);
			sleep(250);
			stopMoving();
			sleep(1000);
			target-=1;
		}
	}

	private void insert(double direction) throws InterruptedException{
		turn(direction,.5);
		move(direction,1000,.2);
		grab(false);
		forkX(false);
		move(direction,200,.2);
		direction-=180;
		if(direction<0){direction+=360;}
		move(direction,500,.2);
		turn(direction,.5);
	}

	private void moveToCubby(boolean direction) throws InterruptedException {
		straighten();
		resetTimer();
		while(robot.color.blue()+robot.color.red()< 10 && INPUT_TIMER+5000>System.currentTimeMillis()){
			if(direction){
				moveWithoutStopping(MOVE_RIGHT,.5);
			} else {
				moveWithoutStopping(MOVE_LEFT, .5);
			}
		}
	}

	private void resetTimer(){
		INPUT_TIMER = System.currentTimeMillis();
	}

	private void dismount(double direction) throws InterruptedException {
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			moveWithoutStopping(direction,1);
		}
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			moveWithoutStopping(direction,1);
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
			//move(MOVE_RIGHT,1000,.5);
			dismount(0);
			vuCubby(RIGHT,3);
			insert(MOVE_RIGHT);
		}
	}
}