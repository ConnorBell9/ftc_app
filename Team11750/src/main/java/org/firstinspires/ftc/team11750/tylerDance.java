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
		//double radGyro = (robot.gyro.getHeading() * Math.PI) / 180;
	    double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
		double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
	    if(gamepad1.right_bumper){robotAngle=Math.PI*7/4; r=1;}
	    else if (gamepad1.left_bumper){robotAngle=Math.PI*3/4; r=1;}
	    double rightX = gamepad1.right_stick_x;
	    final double v1 = r * Math.sqrt(2) * Math.cos(robotAngle) + rightX;
	    final double v2 = r * Math.sqrt(2) * Math.sin(robotAngle) - rightX;
	    final double v3 = r * Math.sqrt(2) * Math.sin(robotAngle) + rightX;
	    final double v4 = r * Math.sqrt(2) * Math.cos(robotAngle) - rightX;

	    robot.frontLeft.setPower(v1*maxSpeed);
		robot.frontRight.setPower(v2*maxSpeed);
		robot.backLeft.setPower(v3*maxSpeed);
		robot.backRight.setPower(v4*maxSpeed);
        
    }

    @Override
    public void stop() {
    }

}
