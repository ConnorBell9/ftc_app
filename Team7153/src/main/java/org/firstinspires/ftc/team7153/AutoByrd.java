package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.team7153.HardwareByrd.HAMMER_CENTER;
import static org.firstinspires.ftc.team7153.HardwareByrd.HAMMER_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrd.HAMMER_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.HAMMER_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.HAMMER_UP;
import static org.firstinspires.ftc.team7153.HardwareByrd.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrd.LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.LEFT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrd.LEFT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrd.LIFT_X_IN;
import static org.firstinspires.ftc.team7153.HardwareByrd.LIFT_X_OUT;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.RED;
import static org.firstinspires.ftc.team7153.HardwareByrd.RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.RIGHT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrd.RIGHT_CLAMP_OPEN;


public class AutoByrd extends LinearOpMode {
	HardwareByrd robot = new HardwareByrd();
	private double imaginaryAngle=0;
	private VuforiaLocalizer vuforia;
	private VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
	private VuforiaTrackable relicTemplate = relicTrackables.get(0);

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

	void grab(boolean grab) {
		if (grab) {
			robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
		} else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
		}
	}

	void forkX(boolean mode) {
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

	void hammer(boolean colorRemaining) throws InterruptedException {
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

	void vuCubby(boolean direction, int target) throws InterruptedException{
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

	void insert(double direction) throws InterruptedException{
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

	void dismount(double direction) throws InterruptedException {
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			moveWithoutStopping(direction,1);
		}
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			moveWithoutStopping(direction,1);
		}
		stopMoving();
	}

	int Vuvalue(boolean direction){
		RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
		if(vuMark != RelicRecoveryVuMark.UNKNOWN){
			if(vuMark == RelicRecoveryVuMark.LEFT){
				if(direction == RIGHT){return 1;} else{return 3;}
			} else if(vuMark == RelicRecoveryVuMark.RIGHT){
				if(direction == RIGHT){return 3;} else{return 1;}
			} else {return 2;}
		}
		return 0;
	}

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		parameters.vuforiaLicenseKey = "ARK0G5D/////AAAAGTNyS/9bI0eKk0BiZlza4w8qOLSfAS/JLHbvWMY95VY7PgFNgH178LKZTQVDke1Eu9JzX/o9QWeyU5ottyCSuPaRr98YId9QUZtfX918roLvNx3n5bXekGlcKSoxgw+UcH3HN+c8V57B3fFhNMt0uyKEWNAXYmAx1OkvoFUSSurH82uzsGg+aBZ3nlVfj043RPXSDyiJO7uDZmwVH14LPjdhP92Qj6byGdICOqc5dxKG1rVFdNgAWJjYVWbz53K1qNWyO9fYgE0lIjwgNopM2GCFVR2ycS0JHx5UW3Bk2m47kDoFCFJP+A8fWxfLyrtgH02JOzNyHb0VoKv4ZDan5Czl7Wcs+ItJBby3qyEmPRkf";
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
		this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

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
	}
}