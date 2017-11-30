package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

@Autonomous(name="RedLeftByrdMK5")
public class RedLeftByrdMK5 extends LinearOpMode {
	private HardwareByrd robot = new HardwareByrd();
	private double imaginaryAngle=0;         //Sets the robot's initial angle to 0
	/*private VuforiaLocalizer vuforia;        //Stored instance of the vuforia engine
	//These load the Relic Vuforia Marks for use
	private VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
	private VuforiaTrackable relicTemplate = relicTrackables.get(0);*/
	//
	private void dismount(double direction) throws InterruptedException {
		//The robot first checks to see if there are no dramatic changes in the robot's orientation while moving consistantly in a preset direction
		//After the first fall it will once again check to see if there is a dramatic change (at this point 2 wheels should be on the platform and two wheels off)
		//After this the robot should be off of the platform
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			moveWithoutStopping(direction,1);
		}
		while(robot.gyro.rawX()<500 || robot.gyro.rawY()<500 || robot.gyro.rawX()>-500 || robot.gyro.rawY()>-500){
			moveWithoutStopping(direction,1);
		}
		stopMoving();
	}

	private void forkX(boolean mode) {
		//If the arguement is true then the lift will go out otherwise the lift will return to its original orientation
		if (mode) {
			robot.forkX.setTargetPosition((int)LIFT_X_OUT);
		} else {
			robot.forkX.setTargetPosition((int)LIFT_X_IN);
		}
	}

	private void grab(boolean grab) {
		//If the arguement is true then the clamp will close otherwise the clamp will return to its original orientation
		if (grab) {
			robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
		} else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
		}
	}

	private void hammer(boolean colorRemaining) throws InterruptedException {
		telemetry.addData("Function: ", "Hammer");
		telemetry.update();
		//Bring the hammer down and wait in order to get around the shakiness of the hammer
		robot.color.enableLed(true);
		robot.hammerY.setPower(HAMMER_DOWN);
		sleep(1000);
		//If the hammer does not detect any color the hammer will move closer to the jewel's potential position
		scan();
		//If the hammer still does not detect any color the robot will move closer to the jewels and the wall
		resetTimer();
		while (robot.color.blue() == robot.color.red() && System.currentTimeMillis() < INPUT_TIMER + 1000) {
			moveWithoutStopping(MOVE_BACKWARDS,.08);
		}
		stopMoving();
		telemetry.addData("Function: ", "Hammer");
		telemetry.update();
		sleep(1000);
		//If the hammer, by the will of God, still does not detect any color the hammer will move closer to the jewel's potential position
		scan();
		//If the color red is greater than the color blue then if the arguement is red it will putt the blue ball off (Left) otherwise it will putt the red ball off (Right)
		if(robot.color.red()>robot.color.blue()){
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(LEFT);} else {putt(RIGHT);}
		} else if (robot.color.red()<robot.color.blue()){
			//Else if the color red is less than the color blue then if the arguement is red it will put the blue ball off (Right) otherwise it will putt the red ball off (Left)
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(RIGHT);} else {putt(LEFT);}
		}
		//Reset the hammer position to up and turn off the light
		robot.hammerY.setPower(HAMMER_UP);
		robot.hammerX.setPosition(HAMMER_CENTER);
		robot.color.enableLed(false);
		sleep(1000);
	}

	private void insert(double direction) throws InterruptedException{
		//Turn towards the direction of the cryptobox
		turn(direction,.5);
		//Move towards the cryptobox
		move(direction,1000,.2);
		//Release the block
		grab(false);
		forkX(false);
		//Push the block in further
		move(direction,200,.2);
		//Back up and turn around away from the cryptobox
		direction-=180;
		if(direction<0){direction+=360;}
		move(direction,500,.2);
		turn(direction,.5);
	}

	private void move(double angle, double time, double power) throws InterruptedException {
		if (!opModeIsActive()) {
			stopMoving();
			return;
		}
		//Straighten before moving
		straighten();
		//Find the robots exact orientation in radians
		double radGyro = (robot.gyro.getIntegratedZValue() * Math.PI) / 180;
		//Set the robots current movement to the angle in the arguement modified by the robots current angle
		double robotAngle = angle * (Math.PI / 180) - Math.PI / 4 - radGyro;
		//If the angle becomes greater than a 180 degrees then it starts to count down inverting the sign and if the angle becomes less than zero it starts to count up inverting the sign
		if (robotAngle > Math.PI) {
			robotAngle = Math.PI * -2 + robotAngle;
		} else if (robotAngle < -Math.PI) {
			robotAngle = Math.PI * 2 + robotAngle;
		}
		//Sets the robot's wheels to the respective power values for their mecanum angles
		final double v1 = power * Math.cos(robotAngle);
		final double v2 = power * Math.sin(robotAngle);
		final double v3 = power * Math.sin(robotAngle);
		final double v4 = power * Math.cos(robotAngle);
		//Sets the robot's wheels to their power value
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
		//Wait for the time set in the arguement before stopping
		sleep((long) time);
		stopMoving();
	}

	private void moveToCubby(boolean direction) throws InterruptedException {
		straighten();
		//While the hammer is not detecting any color or the time-out hasn't occured the robot will move towards a cubby slot
		resetTimer();
		while(robot.color.blue()+robot.color.red()< 10 && INPUT_TIMER+5000>System.currentTimeMillis()){
			if(direction){
				moveWithoutStopping(MOVE_RIGHT,.5);
			} else {
				moveWithoutStopping(MOVE_LEFT, .5);
			}
		}
	}

	private void moveWithoutStopping(double angle, double power) throws InterruptedException {
		//See the move function. Just doesn't have the stopMoving() function built in.
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

	private void putt(boolean direction){
		telemetry.addData("Function: ", "Putt");
		telemetry.update();
		//If the arguement says right or left then the hammer will putt to the respective positions
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

	private void resetTimer(){
		INPUT_TIMER = System.currentTimeMillis();
	}

	private void scan(){
		//Set the hammer to move closer to the Jewel that the hammer can sense.
		robot.hammerX.setPosition(HAMMER_CENTER-.1);
		sleep(500);
		//If no color is detected then it will reset the hammer to its original position.
		if(robot.color.blue() == robot.color.red()){
			robot.hammerX.setPosition(HAMMER_CENTER);
		}
	}

	private void stopMoving() throws InterruptedException {
		//Sets the power of all motors to zero and then waits for half a second
		robot.frontLeft.setPower(0);
		robot.frontRight.setPower(0);
		robot.backLeft.setPower(0);
		robot.backRight.setPower(0);
		sleep(500);
	}

	private void straighten() throws InterruptedException {
		//Inputs into the turn function the angle that the robot is supposed to be in
		turn(imaginaryAngle,.4);
	}

	private void turn(double angle, double speed) throws InterruptedException {
		//Sets the angle that the robot is supposed to be in to the angle arguement
		imaginaryAngle = angle;
		//While the angel is > the gyroscope+2 or < the gyroscope-2
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
		stopMoving();
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


	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		telemetry.log().add("Gyro Calibrating. Do Not Move!");
		robot.gyro.calibrate();

		while (!isStopRequested() && robot.gyro.isCalibrating())  {
			telemetry.log().add("Gyro Calibrating. Do Not Move!");
			telemetry.update();
			sleep(50);
		}

		telemetry.log().clear(); telemetry.log().add("Gyro Calibrated. Press Start.");
		telemetry.clear(); telemetry.update();

		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();

		waitForStart();
		robot.gyro.resetZAxisIntegrator();
		if (!isStopRequested()) {
			grab(true);
			//forkX(true);
			//int cubby = vuValue(RIGHT);
			hammer(RED);
			//move(MOVE_RIGHT,1000,.5);
			//dismount(0);
			vuCubby(RIGHT, 2);
			insert(MOVE_RIGHT);
		}
	}
}
