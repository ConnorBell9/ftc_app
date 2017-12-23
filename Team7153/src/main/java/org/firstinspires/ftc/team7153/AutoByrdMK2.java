package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_CENTER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_UP;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_OPEN;


public class AutoByrdMK2 extends LinearOpMode {
	HardwareByrdMK2 robot = new HardwareByrdMK2(); //Gets robot from HardwareByrd class
	private double imaginaryAngle=0;         //Sets the robot's initial angle to 0

	//Vuforia
	//private VuforiaLocalizer vuforia;
	//

	void forkY(boolean direction){
		if(isStopRequested()){
			return;
		}
		//robot.forkY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		if(direction) {
			robot.forkY.setTargetPosition(-1440);
		} else {
			robot.forkY.setTargetPosition(0);
		}
	}
	
	void grab(boolean grab) {
		//If the arguement is true then the clamp will close otherwise the clamp will return to its original orientation
		if(isStopRequested()){
			return;
		}
		if (grab) {
			robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
			robot.armT.setPower(TOP_CLAMP_CLOSE);
		} else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
			robot.armT.setPower(TOP_CLAMP_OPEN);
		}
	}
	
	void hammer(boolean colorRemaining) throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		telemetry.addData("Function: ", "Hammer");
		telemetry.update();
		//Bring the hammer down and wait in order to get around the shakiness of the hammer
		robot.color.enableLed(true);
		robot.hammerY.setPower(HAMMER_DOWN);
		sleep(1000);
		if(isStopRequested()){
			return;
		}
		//If the color red is greater than the color blue then if the arguement is red it will putt the blue ball off (Left) otherwise it will putt the red ball off (Right)
		if(robot.color.red()>robot.color.blue()){
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(LEFT);} else {putt(RIGHT);}
		} else if (robot.color.red()<robot.color.blue()){
			//Else if the color red is less than the color blue then if the argument is red it will put the blue ball off (Right) otherwise it will putt the red ball off (Left)
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(RIGHT);} else {putt(LEFT);}
		}
		sleep(1000);
		//Reset the hammer position to up and turn off the light
		telemetry.clear();
		telemetry.addData("Color Red: ", robot.color.red());
		telemetry.addData("Color Blue: ", robot.color.blue());
		telemetry.update();
		robot.hammerY.setPower(HAMMER_UP);
		robot.hammerX.setPosition(HAMMER_CENTER);
		robot.color.enableLed(false);
	}

	void moveWithEncoders(double distance, double power, boolean direction) throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		straighten();
		if(!direction){distance*=-1;}

		robot.frontLeft.setPower(power);
		robot.frontRight.setPower(power);
		robot.backLeft.setPower(power);
		robot.backRight.setPower(power);

		robot.frontLeft.setTargetPosition((int)(robot.frontLeft.getCurrentPosition()+(distance/4)*280));
		robot.frontRight.setTargetPosition((int)(robot.frontRight.getCurrentPosition()+(distance/4)*280));
		robot.backLeft.setTargetPosition((int)(robot.backLeft.getCurrentPosition()+(distance/4)*280));
		robot.backRight.setTargetPosition((int)(robot.backRight.getCurrentPosition()+(distance/4)*280));

		robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		while(robot.frontLeft.getTargetPosition()!=robot.frontLeft.getCurrentPosition()){
			if(isStopRequested()){
				return;
			}
			telemetry.addData("Target Position: ", robot.frontLeft.getTargetPosition());
			telemetry.addData("Current Position:  ", robot.frontLeft.getCurrentPosition());
			telemetry.clear();
			telemetry.update();
			sleep(10);
		}
		sleep(500);
		robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		robot.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	}
	
	void moveToCubby(double direction) throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		straighten();
		//While the hammer is not detecting any color or the time-out hasn't occurred the robot will move towards a cubby slot
		if (false){
			moveWithoutStopping(direction+90,1);
			sleep(750);
		}else if (true){
			moveWithoutStopping(direction-90,1);
			sleep(750);
		}
		stopMoving();
		straighten();
		forkY(false);
		sleep(1000);
		grab(false);
		moveWithoutStopping(direction,.3);
		sleep(1000);
		stopMoving();
		moveWithEncoders(6,.3,BACKWARDS);
	}

	private void moveWithoutStopping(double angle, double power) throws InterruptedException {
		//See the move function. Just doesn't have the stopMoving() function built in.
		if(isStopRequested()){
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
		if(isStopRequested()){
			return;
		}
		telemetry.addData("Function: ", "Putt");
		telemetry.update();
		//If the arguement says right or left then the hammer will putt to the respective positions
		if(direction == RIGHT){
			telemetry.addData("Hammer Position: ", "Right");
			telemetry.update();
			robot.hammerX.setPosition(HAMMER_RIGHT);
		} else {
			telemetry.addData("Hammer Position: ", "Left");
			telemetry.update();
			robot.hammerX.setPosition(HAMMER_LEFT);
		}
	}

	private void resetTimer(){
		INPUT_TIMER = System.currentTimeMillis();
	}

	private void statusCheck() {
		/*VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
		VuforiaTrackable relicTemplate = relicTrackables.get(0);
		RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
		RelicRecoveryVuMark currentVuMark;
		relicTrackables.activate();
		if(!opModeIsActive()){stop();}
		if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
			currentVuMark = vuMark;
                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
			/*Utelemetry.addData("VuMark", "%s visible", vuMark);
		} else {
			return 0;
		}
		if (currentVuMark == RelicRecoveryVuMark.LEFT){
			return 1;
		} else if (currentVuMark == RelicRecoveryVuMark.RIGHT){
			return 3;
		} else {return 2;}*/
	}

	private void stopMoving() throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		//Sets the power of all motors to zero and then waits for half a second
		robot.frontLeft.setPower(0);
		robot.frontRight.setPower(0);
		robot.backLeft.setPower(0);
		robot.backRight.setPower(0);
	}
	
	private void straighten() throws InterruptedException {
		//Inputs into the turn function the angle that the robot is supposed to be in
		turn(imaginaryAngle,.4);
	}

	void turn(double angle, double speed) throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		//Sets the angle that the robot is supposed to be in to the angle arguement
		imaginaryAngle = angle;
		//While the angel is > the gyroscope+2 or < the gyroscope-2
		while(angle > (robot.gyro.getHeading()+2)%360 || angle < robot.gyro.getHeading()-2){
			if(isStopRequested()){
				return;
			}
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

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		/*int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		parameters.vuforiaLicenseKey = "ARK0G5D/////AAAAGTNyS/9bI0eKk0BiZlza4w8qOLSfAS/JLHbvWMY95VY7PgFNgH178LKZTQVDke1Eu9JzX/o9QWeyU5ottyCSuPaRr98YId9QUZtfX918roLvNx3n5bXekGlcKSoxgw+UcH3HN+c8V57B3fFhNMt0uyKEWNAXYmAx1OkvoFUSSurH82uzsGg+aBZ3nlVfj043RPXSDyiJO7uDZmwVH14LPjdhP92Qj6byGdICOqc5dxKG1rVFdNgAWJjYVWbz53K1qNWyO9fYgE0lIjwgNopM2GCFVR2ycS0JHx5UW3Bk2m47kDoFCFJP+A8fWxfLyrtgH02JOzNyHb0VoKv4ZDan5Czl7Wcs+ItJBby3qyEmPRkf";
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
		this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);*/



		robot.color.enableLed(false);
	}
}
