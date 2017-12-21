package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_CENTER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_UP;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_BACK;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_RIGHT;


public class AutoByrdMK2 extends LinearOpMode {
	HardwareByrdMK2 robot = new HardwareByrdMK2(); //Gets robot from HardwareByrd class
	private double imaginaryAngle=0;         //Sets the robot's initial angle to 0

	//Vuforia
	private VuforiaLocalizer vuforia;
	//

	void forkY(boolean direction){
		statusCheck();
		if(direction) {
			robot.forkY.setPower(-.5);
		} else {
			robot.forkY.setPower(.5);
		}
		sleep(2000);
		robot.forkY.setPower(0);
	}
	
	void grab(boolean grab) {
		//If the arguement is true then the clamp will close otherwise the clamp will return to its original orientation
		statusCheck();
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
		statusCheck();
		telemetry.addData("Function: ", "Hammer");
		telemetry.update();
		//Bring the hammer down and wait in order to get around the shakiness of the hammer
		robot.color.enableLed(true);
		robot.hammerY.setPosition(HAMMER_DOWN);
		sleep(2000);
		statusCheck();
		//If the color red is greater than the color blue then if the arguement is red it will putt the blue ball off (Left) otherwise it will putt the red ball off (Right)
		if(robot.color.red()>robot.color.blue()){
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(RIGHT);} else {putt(LEFT);}
		} else if (robot.color.red()<robot.color.blue()){
			//Else if the color red is less than the color blue then if the argument is red it will put the blue ball off (Right) otherwise it will putt the red ball off (Left)
			telemetry.addData("Color Red: ", robot.color.red());
			telemetry.addData("Color Blue: ", robot.color.blue());
			telemetry.update();
			if(colorRemaining==RED){putt(LEFT);} else {putt(RIGHT);}
		}
		//Reset the hammer position to up and turn off the light
		telemetry.clear();
		telemetry.addData("Color Red: ", robot.color.red());
		telemetry.addData("Color Blue: ", robot.color.blue());
		telemetry.update();
		robot.hammerY.setPosition(HAMMER_UP);
		robot.hammerX.setPower(HAMMER_CENTER);
		robot.color.enableLed(false);
		sleep(1000);
	}

	void moveWithEncoders(double distance, double power, boolean direction) throws InterruptedException {
		statusCheck();
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
			statusCheck();
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
	
	void moveToCubby() throws InterruptedException {
		statusCheck();
		straighten();
		//While the hammer is not detecting any color or the time-out hasn't occurred the robot will move towards a cubby slot
		if (statusCheck() == 1){
			turn(TURN_LEFT,.3);
			moveWithEncoders(12,.3,FORWARDS);
		}else if ( statusCheck() == 3){
			turn(TURN_RIGHT,.3);
			moveWithEncoders(12,.3,FORWARDS);
		}
		turn(TURN_BACK,.3);
		moveWithoutStopping(MOVE_BACKWARDS,.3);
		grab(false);
		sleep(1000);
		stopMoving();
		moveWithEncoders(12,.3,BACKWARDS);
	}

	private void moveWithoutStopping(double angle, double power) throws InterruptedException {
		//See the move function. Just doesn't have the stopMoving() function built in.
		statusCheck();

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
		statusCheck();
		telemetry.addData("Function: ", "Putt");
		telemetry.update();
		//If the arguement says right or left then the hammer will putt to the respective positions
		if(direction == RIGHT){
			telemetry.addData("Hammer Position: ", "Right");
			telemetry.update();
			robot.hammerX.setPower(HAMMER_RIGHT);
		} else {
			telemetry.addData("Hammer Position: ", "Left");
			telemetry.update();
			robot.hammerX.setPower(HAMMER_LEFT);
		}
	}

	private void resetTimer(){
		INPUT_TIMER = System.currentTimeMillis();
	}

	private int statusCheck() {
		VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
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
			telemetry.addData("VuMark", "%s visible", vuMark);
		} else {
			return 0;
		}
		if (currentVuMark == RelicRecoveryVuMark.LEFT){
			return 1;
		} else if (currentVuMark == RelicRecoveryVuMark.RIGHT){
			return 3;
		} else {return 2;}
	}

	private void stopMoving() throws InterruptedException {
		statusCheck();
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

	void turn(double angle, double speed) throws InterruptedException {
		statusCheck();
		//Sets the angle that the robot is supposed to be in to the angle arguement
		imaginaryAngle = angle;
		//While the angel is > the gyroscope+2 or < the gyroscope-2
		while(angle > (robot.gyro.getHeading()+2)%360 || angle < robot.gyro.getHeading()-2){
			statusCheck();
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

	/*int vuValue(boolean direction){
		RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        relicTrackables.activate();
		if(vuMark != RelicRecoveryVuMark.UNKNOWN){
			if(vuMark == RelicRecoveryVuMark.LEFT){
				if(direction == RIGHT){return 1;} else{return 3;}
			} else if(vuMark == RelicRecoveryVuMark.RIGHT){
				if(direction == RIGHT){return 3;} else{return 1;}
			} else {return 2;}
		}
		return 1;
	}*/

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		parameters.vuforiaLicenseKey = "ARK0G5D/////AAAAGTNyS/9bI0eKk0BiZlza4w8qOLSfAS/JLHbvWMY95VY7PgFNgH178LKZTQVDke1Eu9JzX/o9QWeyU5ottyCSuPaRr98YId9QUZtfX918roLvNx3n5bXekGlcKSoxgw+UcH3HN+c8V57B3fFhNMt0uyKEWNAXYmAx1OkvoFUSSurH82uzsGg+aBZ3nlVfj043RPXSDyiJO7uDZmwVH14LPjdhP92Qj6byGdICOqc5dxKG1rVFdNgAWJjYVWbz53K1qNWyO9fYgE0lIjwgNopM2GCFVR2ycS0JHx5UW3Bk2m47kDoFCFJP+A8fWxfLyrtgH02JOzNyHb0VoKv4ZDan5Czl7Wcs+ItJBby3qyEmPRkf";
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
		this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);



		robot.color.enableLed(false);
	}
}
