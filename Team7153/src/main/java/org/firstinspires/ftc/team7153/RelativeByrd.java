package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name="RelativeByrd")
public class RelativeByrd extends OpMode{

	DcMotor frontRight; // Front Right Motor // Runs in ? Direction //
	DcMotor frontLeft; // Front Left Motor  // Runs in ? Direction //
	DcMotor backRight; // Back Right Motor  // Runs in ? Direction //
	DcMotor backLeft; // Back Left Motor   // Runs in ? Direction //

	ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //



    @Override
    public void init() {
        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro.calibrate();
        frontRight = hardwareMap.dcMotor.get("fr");
        frontLeft = hardwareMap.dcMotor.get("fl");
        backRight = hardwareMap.dcMotor.get("br");
        backLeft = hardwareMap.dcMotor.get("bl");
	    
	frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
	backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
	double maxSpeed = 1;//Defines what fraction of speed the robot will run at
	gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");

	double radGyro=(gyro.getHeading()*Math.PI)/180;
	double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
	double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
	double rightX = gamepad1.right_stick_x;

        if(robotAngle>Math.PI){
            robotAngle=Math.PI*-2+robotAngle;
        } else if(robotAngle<-Math.PI){
            robotAngle=Math.PI*2+robotAngle;
        }

        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        frontLeft.setPower(v1*maxSpeed);
        frontRight.setPower(v2*maxSpeed);
        backLeft.setPower(v3*maxSpeed);
        backRight.setPower(v4*maxSpeed);
        telemetry.addData("frontLeft", v1);
        telemetry.addData("frontRight", v2);
        telemetry.addData("backLeft", v3);
        telemetry.addData("backRight", v4);
        telemetry.addData("robotAngle", robotAngle);
        telemetry.addData("radGyro", radGyro);
        telemetry.update();
    }
}

