package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IDOL_CLAMP_CLOSED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IDOL_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IDOL_Z_DELTA_POSITION;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_BLOCK_GRAB;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_IDOL_GRAB;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_PLATE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.PUSH_PLATE_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.PUSH_PLATE_UP;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_OPEN;

@TeleOp(name="RelativeMechByrdMK3")
public class RelativeMechByrdMK3 extends OpMode{
	private HardwareByrdMK2 robot = new HardwareByrdMK2();
    @Override
    public void init() {
		robot.init(hardwareMap);
		if(!IS_GYRO_ON) {
			robot.gyro.calibrate();
			IS_GYRO_ON=true;
		}
		IS_GYRO_ON=false;
		IS_BLOCK_GRAB=true;
    }

    @Override
    public void loop() {
	    double maxSpeed = 1;//Defines what fraction of speed the robot will run at
		double radGyro = (robot.gyro.getHeading() * Math.PI) / 180;
	    double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
		double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
	    if(IS_GYRO_ON){
			robotAngle -= radGyro;
		}
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

		if(gamepad1.y && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_GYRO_ON=!IS_GYRO_ON;
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad2.x && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_IDOL_GRAB=!IS_IDOL_GRAB;
			INPUT_TIMER = System.currentTimeMillis();
		}

	    if(IS_BLOCK_GRAB){
		    robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
			robot.armT.setPosition(TOP_CLAMP_CLOSE);
	    } else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
			robot.armT.setPosition(TOP_CLAMP_OPEN);
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

		if(gamepad2.left_stick_y>.02) {
			robot.clamp.setPower(gamepad2.left_stick_y);
		} else if(gamepad2.left_stick_y <- .02) {
			robot.clamp.setPower(gamepad2.left_stick_y);
		} else {robot.clamp.setPower(0);}

		if(gamepad2.right_trigger>.02){
			robot.idolY.setPower(.75*gamepad2.right_trigger);
		} else if (gamepad2.left_trigger>.02){
			robot.idolY.setPower(-.75*gamepad2.left_trigger);
		} else {robot.idolY.setPower(0);}
		if(gamepad2.right_bumper && System.currentTimeMillis() > INPUT_TIMER+5){
			robot.idolZ.setTargetPosition(robot.idolZ.getCurrentPosition()+IDOL_Z_DELTA_POSITION);
			INPUT_TIMER = System.currentTimeMillis();
		}else if(gamepad2.left_bumper && System.currentTimeMillis() > INPUT_TIMER+5){
			robot.idolZ.setTargetPosition(robot.idolZ.getCurrentPosition()-IDOL_Z_DELTA_POSITION);
			INPUT_TIMER = System.currentTimeMillis();
		}

	    telemetry.addData("Grab is:  ", IS_BLOCK_GRAB);
		telemetry.addData("Plate is: ", IS_PLATE);
		telemetry.addData("Idol is:  ", IS_IDOL_GRAB);
		telemetry.addData("Gyro is:  ", IS_GYRO_ON);
	    telemetry.addData("clamp Running to: ", robot.clamp.getTargetPosition());
		telemetry.addData("clamp Running at: ", robot.clamp.getCurrentPosition());
		telemetry.addData("idolY Running to: ", robot.idolY.getTargetPosition());
		telemetry.addData("idolY Running at: ", robot.idolY.getCurrentPosition());
		telemetry.addData("idolZ Running to: ", robot.idolZ.getTargetPosition());
		telemetry.addData("idolZ Running at: ", robot.idolZ.getCurrentPosition());
	    telemetry.addData("frontLeft:  ", robot.frontLeft.getPower());
	    telemetry.addData("frontRight: ", robot.frontRight.getPower());
	    telemetry.addData("backLeft:   ", robot.backLeft.getPower());
	    telemetry.addData("backRight:  ", robot.backRight.getPower());
	    telemetry.addData("Gyro:       ", robot.gyro.getHeading());
		telemetry.addData("ColorR Blue: ", robot.colorR.blue());
		telemetry.addData("ColorR Red:  ", robot.colorR.red());
		telemetry.addData("ColorL Blue: ", robot.colorL.blue());
		telemetry.addData("ColorL Red:  ", robot.colorL.red());
	    telemetry.update();
    }
}