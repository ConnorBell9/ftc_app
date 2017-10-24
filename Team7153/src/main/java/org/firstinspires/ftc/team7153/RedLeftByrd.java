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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="RedLeftByrd")
public class RedLeftByrd extends LinearOpMode {
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

    void move(double angle, double distance, double power) throws InterruptedException{
    	        if (!opModeIsActive()) {
			stopMoving();
			return;
		}
	    double radGyro=(gyro.getHeading()*Math.PI)/180;
	    double r = power;
	    double robotAngle = angle*(Math.PI/180) - Math.PI / 4;

        if(robotAngle>Math.PI){
            robotAngle=Math.PI*-2+robotAngle;
        } else if(robotAngle<-Math.PI){
            robotAngle=Math.PI*2+robotAngle;
        }

        final double v1 = r * Math.cos(robotAngle);
        final double v2 = -r * Math.sin(robotAngle);
        final double v3 = r * Math.sin(robotAngle);
        final double v4 = -r * Math.cos(robotAngle);

		frontLeft.setPower(v1);
		frontRight.setPower(v2);
		backLeft.setPower(v3);
		backRight.setPower(v4);
	    	sleep((long)(distance/power*100));
		stop();
    }
	
	void stop(){
		frontLeft.setPower(0);
		frontRight.setPower(0);
		backLeft.setPower(0);
		backRight.setPower(0);
	}
	
	void hammer(){
		hammer.setPosition(.3);
		if(color.blue()>.3){
			move(180,3,.3);
		} else if (color.red()>.3){
			move(0,3,.3);
		}
		hammer.setPosition(.9);
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

		idolZ.setTargetPosition(0);
		idolZ.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		/*idolY.setTargetPosition(0);
		idolY.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/
		
	    armL.setPosition(.8);
	    armR.setPosition(.7);
	    hammer.setPosition(.9);

		plateL.setPosition(0);
		plateR.setPosition(1);
	    
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
	    
	    move(180,3,.4)
	    hammer();
	    move(0,12,.5);
    }
}
