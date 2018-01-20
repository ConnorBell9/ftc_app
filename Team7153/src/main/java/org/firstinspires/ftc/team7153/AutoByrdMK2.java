package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACK_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACK_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_1;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.DELTA_RAMP;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FRONT_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FRONT_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_CENTER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.HAMMER_UP;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.OFFSET;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RAMP;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.SLOT_1;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.SLOT_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.SLOT_3;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_BACK;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_ERROR;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_RIGHT;


public class AutoByrdMK2 extends LinearOpMode {
	private HardwareByrdMK2 robot = new HardwareByrdMK2(); //Gets robot from HardwareByrd class
	private double imaginaryAngle=0;         //Sets the robot's initial angle to 0

	private ElapsedTime runTime = new ElapsedTime();

	//Vuforia
	private VuforiaLocalizer vuforia;
	private VuforiaLocalizer.Parameters parameters;
	private VuforiaTrackables relicTrackables;
	private VuforiaTrackable relicTemplate;
	private RelicRecoveryVuMark relicVuMark = RelicRecoveryVuMark.UNKNOWN;
	//
	void autonomousInit(){


		////////////////////////////////////////////////////////////////////////////////////Hardware////////////////////////////////////////////////////////////////////////////////
		robot.init(hardwareMap);
		robot.colorR.enableLed(false);
		robot.colorL.enableLed(false);

		//Activate Clamps's encoders
		robot.clamp.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.clamp.setPower(1);

		//Calibrate the Gyroscope
		telemetry.addData("Gyro", " Calibrating. Do Not move!");
		telemetry.update();
		robot.gyro.calibrate();

		while (!isStopRequested() && robot.gyro.isCalibrating()) {
			sleep(50);
			idle();
		}

		telemetry.addData("Gyro", " Calibrated.");
		telemetry.update();

		////////////////////////////////////////////////////////////////////////////////////Vuforia/////////////////////////////////////////////////////////////////////////////////

		//Get the view for the camera monitor
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

		//Use Vindicem license key
		parameters.vuforiaLicenseKey = "ARK0G5D/////AAAAGTNyS/9bI0eKk0BiZlza4w8qOLSfAS/JLHbvWMY95VY7PgFNgH178LKZTQVDke1Eu9JzX/o9QWeyU5ottyCSuPaRr98YId9QUZtfX918roLvNx3n5bXekGlcKSoxgw+UcH3HN+c8V57B3fFhNMt0uyKEWNAXYmAx1OkvoFUSSurH82uzsGg+aBZ3nlVfj043RPXSDyiJO7uDZmwVH14LPjdhP92Qj6byGdICOqc5dxKG1rVFdNgAWJjYVWbz53K1qNWyO9fYgE0lIjwgNopM2GCFVR2ycS0JHx5UW3Bk2m47kDoFCFJP+A8fWxfLyrtgH02JOzNyHb0VoKv4ZDan5Czl7Wcs+ItJBby3qyEmPRkf";

		//Assign which camera is to be used
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
		this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

		//Load the VuMarks
		relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
		relicTemplate = relicTrackables.get(0);

		relicTemplate.setName("relicVuMarkTemplate");

		telemetry.addData("Vuforia", " Activating. Do Not Start!");
		telemetry.update();

		relicTrackables.activate();
		telemetry.addData("Vuforia", " Activated. Start");
		telemetry.update();
	}

	void autonomousStart() throws InterruptedException{
		//Reset the gyroscope to account for drift
		robot.gyro.resetZAxisIntegrator();

		//Reset the timer to 0
		runTime.reset();

		//Tell the robot that the Gyroscope has been correctly calibrated (Not used yet)
		IS_GYRO_ON = true;

		statusCheck();
	}

	void clamp(int position){
		if(isStopRequested()){
			return;
		}
		/* Moves the clamp to either
		*CLAMP_POSITION_1 (Which is ground level)
		*CLAMP_POSITION_2 (Which is the position for the second block)
		*CLAMP_POSITION_3 (Which is the position for the third block)
		*CLAMP_POSITION_4 (Which is the position for the fourth block)
		*/
		robot.clamp.setTargetPosition(position);
	}
	
	void grab(boolean grab) {
		if(isStopRequested()){
			return;
		}
		/*If grab is true then the clamps will move to their respective close positions
		*otherwise they will move to their respective open positions
		*/
		if (grab) {
			robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
			robot.armT.setPosition(TOP_CLAMP_CLOSE);
		} else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
			robot.armT.setPosition(TOP_CLAMP_OPEN);
		}
	}
	
	void hammer(boolean colorRemaining) throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		//Brings the hammer down and turns on the LED
		robot.colorR.enableLed(true);
		robot.colorL.enableLed(true);
		robot.hammerY.setPosition(HAMMER_DOWN);
		sleep(1000);
		if(isStopRequested()){
			return;
		}
		/*If the color red is greater than the color blue
		*then, if the argument is red it will putt the blue ball off (Left)
		*otherwise it will putt the red ball off (Right)
		*/
		for(double increment=0; (robot.colorR.red()==robot.colorR.blue() && robot.colorL.red()==robot.colorL.blue()) && increment<2; increment+=.01){
			robot.hammerX.setPosition(HAMMER_CENTER+increment);
		}
		if((robot.colorR.red()!=robot.colorR.blue() || robot.colorL.red()!=robot.colorL.blue()) && robot.colorR.red()>=robot.colorR.blue() && robot.colorL.red()<=robot.colorL.blue()){
			telemetry.addData("Found Color: ", "red");
			telemetry();
			if(colorRemaining==RED){putt(RIGHT);} else {putt(LEFT);}
		} else if ((robot.colorR.red()!=robot.colorR.blue() || robot.colorL.red()!=robot.colorL.blue()) && robot.colorR.red()<=robot.colorR.blue() && robot.colorL.red()>=robot.colorL.blue()){
			/*If the color red is less than the color blue
			*then, if the argument is red it will putt the blue ball off (Right)
			*otherwise it will putt the red ball off (Left)
			*/
			telemetry.addData("Found Color: ", "blue");
			telemetry();
			if(colorRemaining==RED){putt(LEFT);} else {putt(RIGHT);}
		} else {
			//If no color is found then nothing will occur
			telemetry.addData("Found Color: ", "Nothing");
			telemetry();
		}
		statusCheck();
		sleep(1000);
		statusCheck();
		//Reset the hammer position to up and turn off the LED
		robot.hammerY.setPosition(HAMMER_UP);
		robot.hammerX.setPosition(HAMMER_CENTER);
		robot.colorR.enableLed(false);
		robot.colorL.enableLed(false);
	}

	void harvest(double X_AXIS, double Y_AXIS) throws InterruptedException{
		turn(TURN_RIGHT,.3);
		//Open up the clamp and activate the intake in preparation of blocks.
		grab(false);
		intake(true);
		//Move away from the wall for Y_AXIS inches.
		moveWithEncoders(Y_AXIS,.3,FORWARDS);
		/* X_AXIS is based off of the bot's forward direction.
		* Forwards is positive.
		* Backwards is negative.
		* So if X_AXIS is a positive number then it will turn forwards functions.
		* If the X_AXIS is a negative number then it will turn backwards.
		* If the robot doesn't have to move in the X direction then it will assume it has 
		* reached its destination and attempt to pick up a block.
		* After looping the first time it will then correct for the bot's offset.
		*
		*/
		boolean block=true;
		for(int i=0; i<2; i++){
		if(X_AXIS>0){
			turn(TURN_FORWARDS,.3);
			moveWithEncoders(X_AXIS,.3,block);
		} else if (X_AXIS<0) {
			turn(TURN_BACK,.3);
			moveWithEncoders(-X_AXIS,.3,block);
		}
		grab(true);
		intake(false);
		clamp(CLAMP_POSITION_2);
		//Add the offset incurred by placing the cryptoblock
		X_AXIS-=OFFSET;
		block=false;
		}
		/*
		* Return using the Y_AXIS now going back towards the cryptobox
		*/
		turn(TURN_RIGHT,.3);
		moveWithEncoders(Y_AXIS,.3,BACKWARDS);
		turn(TURN_LEFT,.3);

	}

	void intake(boolean mode){
		/*if(mode){
			robot.intakeTopLeft.setPower(INTAKE_ON);
			robot.intakeTopRight.setPower(INTAKE_ON);
			robot.intakeBottomLeft.setPower(INTAKE_ON);
			robot.intakeBottomRight.setPower(INTAKE_ON);
		} else {
			robot.intakeTopLeft.setPower(INTAKE_OFF);
			robot.intakeTopRight.setPower(INTAKE_OFF);
			robot.intakeBottomLeft.setPower(INTAKE_OFF);
			robot.intakeBottomRight.setPower(INTAKE_OFF);
		}*/
	}


	void moveWithEncoders(double distance, double power, boolean direction) throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		straighten();
		if(!direction){distance*=-1;}

		robot.frontLeft.setTargetPosition((int)(robot.frontLeft.getCurrentPosition()+(distance/4)*280));
		robot.frontRight.setTargetPosition((int)(robot.frontRight.getCurrentPosition()+(distance/4)*280));
		robot.backLeft.setTargetPosition((int)(robot.backLeft.getCurrentPosition()+(distance/4)*280));
		robot.backRight.setTargetPosition((int)(robot.backRight.getCurrentPosition()+(distance/4)*280));

		robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		resetTimer();

		while(robot.frontLeft.getTargetPosition()!=robot.frontLeft.getCurrentPosition() && INPUT_TIMER+5000>runTime.milliseconds()){
			if(isStopRequested()){
				stopMoving();
				return;
			}
			/*while((robot.gyro.getHeading()<imaginaryAngle-TURN_ERROR || robot.gyro.getHeading()>imaginaryAngle+TURN_ERROR) && (imaginaryAngle-TURN_ERROR<=-1 && robot.gyro.getHeading() != 360-TURN_ERROR || imaginaryAngle-TURN_ERROR>-1) && (imaginaryAngle+TURN_ERROR>=360 && robot.gyro.getHeading()>TURN_ERROR-1 || imaginaryAngle+TURN_ERROR<360) && INPUT_TIMER+5000>runTime.milliseconds()){
				if(isStopRequested()) {
					stopMoving();
					return;
				}
					stopMoving();
					FRONT_LEFT=robot.frontLeft.getTargetPosition()-robot.frontLeft.getCurrentPosition();
					FRONT_RIGHT=robot.frontRight.getTargetPosition()-robot.frontRight.getCurrentPosition();
					BACK_LEFT=robot.backLeft.getTargetPosition()-robot.backLeft.getCurrentPosition();
					BACK_RIGHT=robot.backRight.getTargetPosition()-robot.backRight.getCurrentPosition();
					straighten();
					robot.frontLeft.setTargetPosition(FRONT_LEFT);
					robot.frontRight.setTargetPosition(FRONT_RIGHT);
					robot.backLeft.setTargetPosition(BACK_LEFT);
					robot.backRight.setTargetPosition(BACK_RIGHT);
					telemetry();
			}*/
			if(RAMP<power){
				RAMP+=DELTA_RAMP;
				robot.frontLeft.setPower(RAMP);
				robot.frontRight.setPower(RAMP);
				robot.backLeft.setPower(RAMP);
				robot.backRight.setPower(RAMP);
			}
			telemetry();
			sleep(10);
		}
		sleep(500);
		robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		robot.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	}

	void moveToCubby(double turnDirection, boolean mode) throws InterruptedException {
		if (isStopRequested()) {
			return;
		}
		straighten();
		//While the hammer is not detecting any color or the time-out hasn't occurred the robot will move towards a cubby slot
		if(!SLOT_2 && !mode){
			SLOT_2=true;
			OFFSET = 0;
		} else if ((relicVuMark == RelicRecoveryVuMark.LEFT && mode) || (!SLOT_1 && !mode)) {
			SLOT_1 = true;
			OFFSET = -7.63;
			moveWithEncoders(7.63,.3,BACKWARDS);
		} else if ((relicVuMark == RelicRecoveryVuMark.RIGHT && mode) || (!SLOT_3 && !mode)) {
			SLOT_3 = true;
			OFFSET = 7.63;
			moveWithEncoders(7.63,.3,FORWARDS);
		} else if (mode) {
			SLOT_2 = true;
		}
		turn(turnDirection,.3);
		clamp(CLAMP_POSITION_1);
		sleep(1000);
		grab(false);
		moveWithoutStopping(turnDirection+90,.3);
		sleep(1000);
		moveWithoutStopping(turnDirection+90+80,1);
		sleep(500);
		moveWithoutStopping(turnDirection+90,.3);
		sleep(500);
		moveWithoutStopping(turnDirection+90-80,1);
		sleep(1000);
		moveWithoutStopping(turnDirection+90,.3);
		sleep(500);
		stopMoving();
		straighten();
		grab(false);
		moveWithEncoders(6,.3,BACKWARDS);
		stopMoving();
	}

	void moveWithoutStopping(double angle, double power) throws InterruptedException {
		//See the move function. Just doesn't have the stopMoving() function built in.
		if(isStopRequested()){
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

		telemetry.addData("Function: ", "MoveWithoutStopping");
		telemetry.addData("Target Angle:  ", angle);
		telemetry.addData("Current Speed: ", power);
		telemetry();
	}
	
	private void putt(boolean direction){
		if(isStopRequested()){
			return;
		}
		//If the argument says right or left then the hammer will putt to the respective positions
		if(direction == RIGHT){
			telemetry.addData("Hammer Position: ", "Right");
			telemetry();
			robot.hammerX.setPosition(HAMMER_RIGHT);
		} else {
			telemetry.addData("Hammer Position: ", "Left");
			telemetry();
			robot.hammerX.setPosition(HAMMER_LEFT);
		}
	}

	private void resetTimer(){
		INPUT_TIMER = runTime.milliseconds();
	}

	private void statusCheck() throws InterruptedException{
		if(isStopRequested()){
			stopMoving();
			intake(false);
			return;
		}

		if(RelicRecoveryVuMark.from(relicTemplate) != RelicRecoveryVuMark.UNKNOWN){
			relicVuMark = RelicRecoveryVuMark.from(relicTemplate);
		}
	}

	void stopMoving() throws InterruptedException {
		//Sets the power of all motors to zero and then waits for half a second
		robot.frontLeft.setPower(0);
		robot.frontRight.setPower(0);
		robot.backLeft.setPower(0);
		robot.backRight.setPower(0);
	}
	
	private void straighten() throws InterruptedException {
		//Inputs into the turn function the angle that the robot is supposed to be in
		resetTimer();

		while((robot.gyro.getHeading()<imaginaryAngle-TURN_ERROR || robot.gyro.getHeading()>imaginaryAngle+TURN_ERROR) && (imaginaryAngle-TURN_ERROR<=-1 && robot.gyro.getHeading() != 360-TURN_ERROR || imaginaryAngle-TURN_ERROR>-1) && (imaginaryAngle+TURN_ERROR>=360 && robot.gyro.getHeading()>TURN_ERROR-1 || imaginaryAngle+TURN_ERROR<360) && INPUT_TIMER+5000>runTime.milliseconds()){
			if(isStopRequested()) {
				stopMoving();
				return;
			}
			stopMoving();
			turn(imaginaryAngle,.28);
			telemetry();
		}
	}

	private void telemetry(){
		telemetry.addData("/////VUFORIA", "/////");
		telemetry.addData("VuMark", "%s visible", relicVuMark);
		telemetry.addData("/////SENSORS", "/////");
		telemetry.addData("ColorR Red      ", robot.colorR.red());
		telemetry.addData("ColorR Blue:    ", robot.colorR.blue());
		telemetry.addData("ColorL Red      ", robot.colorL.red());
		telemetry.addData("ColorL Blue:    ", robot.colorL.blue());
		telemetry.addData("Current Angle: ", robot.gyro.getIntegratedZValue());
		telemetry.addData("/////ENCODERS", "////");
		telemetry.addData("Target Position:  ", robot.frontLeft.getTargetPosition());
		telemetry.addData("Current Position: ", robot.frontLeft.getCurrentPosition());
		telemetry.addData("/////MOTORS", "//////");
		telemetry.addData("FrontLeft:  ", robot.frontLeft.getPower());
		telemetry.addData("FrontRight: ", robot.frontRight.getPower());
		telemetry.addData("BackLeft:   ", robot.backLeft.getPower());
		telemetry.addData("BackRight:  ", robot.backRight.getPower());
		telemetry.update();
	}

	void turn(double angle, double speed) throws InterruptedException {
		if(isStopRequested()){
			return;
		}
		//Sets the angle that the robot is supposed to be in to the angle arguement
		imaginaryAngle = angle;
		//While the angel is > the gyroscope+TURN_ERROR or < the gyroscope-TURN_ERROR
		resetTimer();
		while((robot.gyro.getHeading()<angle-TURN_ERROR || robot.gyro.getHeading()>angle+TURN_ERROR) && (angle-TURN_ERROR<=-1 && robot.gyro.getHeading() != 360-TURN_ERROR || angle-TURN_ERROR>-1) && (angle+TURN_ERROR>=360 && robot.gyro.getHeading()>TURN_ERROR-1 || angle+TURN_ERROR<360) && INPUT_TIMER+5000>runTime.milliseconds()){
			if(isStopRequested()){
				stopMoving();
				return;
			}
			//Checks to see if turning left or right
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
			telemetry.addData("Target Angle:  ", angle);
			telemetry.addData("Current Speed: ", speed);
			telemetry();
            }
		stopMoving();
	}

	@Override
	public void runOpMode() throws InterruptedException {

	}
}
