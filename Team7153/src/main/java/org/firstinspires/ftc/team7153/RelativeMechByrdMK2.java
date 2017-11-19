package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import static org.firstinspires.ftc.team7153.HardwareByrd.*;

@TeleOp(name="RelativeMechByrdMK2" , group = "New")
public class RelativeMechByrdMK2 extends OpMode{
	private HardwareByrd robot = new HardwareByrd();
    @Override
    public void init() {
		robot.init(hardwareMap);

		robot.gyro.calibrate();
		while(robot.gyro.isCalibrating()) {
			telemetry.addData(">", "Gyro Calibrating. Do Not move!");
			telemetry.update();
		}
		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();
    }

    @Override
    public void loop() {
	    double maxSpeed = 1;//Defines what fraction of speed the robot will run atb
		double radGyro = (robot.gyro.getHeading() * Math.PI) / 180;
	    double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
	    double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4 - radGyro;
	    double rightX = gamepad1.right_stick_x;
	    final double v1 = r * Math.cos(robotAngle) + rightX;
	    final double v2 = r * Math.sin(robotAngle) - rightX;
	    final double v3 = r * Math.sin(robotAngle) + rightX;
	    final double v4 = r * Math.cos(robotAngle) - rightX;

	    robot.frontLeft.setPower(v1*maxSpeed);
		robot.frontRight.setPower(v2*maxSpeed);
		robot.backLeft.setPower(v3*maxSpeed);
		robot.backRight.setPower(v4*maxSpeed);

	    if(gamepad2.a && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_BLOCK_GRAB=!IS_BLOCK_GRAB;
			INPUT_TIMER = System.currentTimeMillis();
	    }

		if(gamepad1.b && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_PLATE=!IS_PLATE;
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad1.x && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_IDOL_GRAB=!IS_IDOL_GRAB;
			INPUT_TIMER = System.currentTimeMillis();
		}

	    if(IS_BLOCK_GRAB){
		    robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
	    } else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
	    }

	    if(IS_PLATE){
			robot.plate.setPosition(PUSH_PLATE_DOWN);
		} else {
			robot.plate.setPosition(PUSH_PLATE_UP);
		}

		if(IS_IDOL_GRAB){
			robot.grabber.setPosition(IDOL_CLAMP_CLOSED);
		} else {
			robot.grabber.setPosition(IDOL_CLAMP_OPEN);
		}

		if(gamepad2.left_stick_y>.1) {
			robot.forkY.setPower(gamepad2.left_stick_y);
		} else if(gamepad2.left_stick_y <- .1) {
			robot.forkY.setPower(gamepad2.left_stick_y * .5);
		} else {robot.forkY.setPower(0);}
		if(gamepad2.dpad_right) {
			robot.forkX.setTargetPosition((int)LIFT_X_OUT);//1440 is the # of pulses 1.25 is the diameter and 11 is the # of inches traveled
		} else if(gamepad2.dpad_left) {
			robot.forkX.setTargetPosition((int)LIFT_X_IN);
		}

		if(gamepad2.right_trigger>.1){
			robot.idolY.setPower(.5*gamepad2.right_trigger);
		} else if (gamepad2.left_trigger>.1){
			robot.idolY.setPower(-.5*gamepad2.left_trigger);
		} else {robot.idolY.setPower(0);}
		if(gamepad2.right_bumper && System.currentTimeMillis() > INPUT_TIMER+10){
			robot.idolZ.setTargetPosition(robot.idolZ.getCurrentPosition()+IDOL_Z_DELTA_POSITION);
			INPUT_TIMER = System.currentTimeMillis();
		}else if(gamepad2.left_bumper && System.currentTimeMillis() > INPUT_TIMER+10){
			robot.idolZ.setTargetPosition(robot.idolZ.getCurrentPosition()-IDOL_Z_DELTA_POSITION);
			INPUT_TIMER = System.currentTimeMillis();
		}

	    telemetry.addData("Grab is: ", IS_BLOCK_GRAB);
		telemetry.addData("Plate is: ", IS_PLATE);
	    telemetry.addData("forkY Running to: ", robot.forkY.getTargetPosition());
		telemetry.addData("forkY Running at: ", robot.forkY.getCurrentPosition());
		telemetry.addData("forkX Running to: ", robot.forkX.getTargetPosition());
		telemetry.addData("forkX Running at: ", robot.forkX.getCurrentPosition());
		telemetry.addData("idolY Running to: ", robot.idolY.getTargetPosition());
		telemetry.addData("idolY Running at: ", robot.idolY.getCurrentPosition());
		telemetry.addData("idolZ Running to: ", robot.idolZ.getTargetPosition());
		telemetry.addData("idolZ Running at: ", robot.idolZ.getCurrentPosition());
	    telemetry.addData("frontLeft", v1);
	    telemetry.addData("frontRight", v2);
	    telemetry.addData("backLeft", v3);
	    telemetry.addData("backRight", v4);
	    telemetry.addData("Gyro", robot.gyro.getHeading());
		telemetry.addData("Color Blue: ", robot.color.blue());
		telemetry.addData("Color Red: ", robot.color.red());
	    telemetry.update();
    }
}
