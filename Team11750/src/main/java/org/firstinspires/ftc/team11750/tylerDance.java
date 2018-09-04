package org.firstinspires.ftc.team11750;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Tyler's Vastly Superior Dance Mode", group="Bellatorum")
public class Teleop extends OpMode{
    HardwareBellatorum robot       = new HardwareBellatorum(); // use the class created to define a Pushbot's hardware
    ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //

    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");

        // make sure the gyro is calibrated before continuing
        // Calibrate the gyro
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        gyro.calibrate();
        while (gyro.isCalibrating()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
    	double maxSpeed = 1;//Defines what fraction of speed the robot will run at
	double maxTurn = .5; 
	double ratio = 0;
	    
	double radGyro = (robot.gyro.getHeading() * Math.PI) / 180;
	double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y)*maxSpeed;
	double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
    	double turn = gamepad1.right_stick_x*Math.abs((maxTurn-1)/(maxTurn+1));

    	double frontLeft = Math.sqrt(2)*Math.cos(robotAngle + radGyro) + turn;
    	double frontRight = Math.sqrt(2)*Math.sin(robotAngle + radGyro) - turn;
    	double backLeft = Math.sqrt(2)*Math.sin(robotAngle + radGyro) + turn;
	double backRight = Math.sqrt(2)*Math.cos(robotAngle + radGyro) - turn;

    	if(Math.abs(frontLeft)>ratio) {
      		ratio=Math.abs(frontLeft);
    	} if(Math.abs(frontRight)>ratio){
      		ratio=Math.abs(frontRight);
    	} if(Math.abs(backRight)>ratio){
      		ratio=Math.abs(backRight);
    	} if(Math.abs(backLeft)>ratio){
      		ratio=Math.abs(backLeft);
    	}
    	frontLeft/=ratio;
    	frontRight/=ratio;
    	backLeft/=ratio;
    	backRight/=ratio;
	    
    	System.out.println(gyro);
    	robot.frontLeft.setPower(r*frontLeft);
    	robot.frontRight.setPower(r*frontRight);
    	robot.backLeft.setPower(r*backRight);
    	robot.backRight.setPower(r*backLeft);
    }

    @Override
    public void stop() {
    }

}
