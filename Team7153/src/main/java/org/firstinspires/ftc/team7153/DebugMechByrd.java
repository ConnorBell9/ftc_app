package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static org.firstinspires.ftc.team7153.HardwareByrd.BLOCK_NO_PUSH;
import static org.firstinspires.ftc.team7153.HardwareByrd.BLOCK_PUSH;
import static org.firstinspires.ftc.team7153.HardwareByrd.DUMP_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrd.DUMP_UP;
import static org.firstinspires.ftc.team7153.HardwareByrd.IDOL_CLAMP_AJAR;
import static org.firstinspires.ftc.team7153.HardwareByrd.IDOL_CLAMP_CLOSED;
import static org.firstinspires.ftc.team7153.HardwareByrd.IDOL_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrd.IDOL_Z_DELTA_POSITION;
import static org.firstinspires.ftc.team7153.HardwareByrd.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrd.INTAKE_OFFSET;
import static org.firstinspires.ftc.team7153.HardwareByrd.INTAKE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrd.IS_BLOCK_PUSH;
import static org.firstinspires.ftc.team7153.HardwareByrd.IS_DUMP;
import static org.firstinspires.ftc.team7153.HardwareByrd.IS_LIFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.IS_PLATE;
import static org.firstinspires.ftc.team7153.HardwareByrd.LATCH_UNLOCKED;
import static org.firstinspires.ftc.team7153.HardwareByrd.LIFT_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrd.LIFT_UP;
import static org.firstinspires.ftc.team7153.HardwareByrd.PUSH_PLATE_DOWN;
import static org.firstinspires.ftc.team7153.HardwareByrd.PUSH_PLATE_UP;

@TeleOp(name="DebugMechByrd")
public class DebugMechByrd extends OpMode{
	private HardwareByrd robot = new HardwareByrd();
    @Override
    public void init() {
		robot.init(hardwareMap);
		robot.intakeLatch.setPosition(LATCH_UNLOCKED);
    }

    @Override
    public void loop() {
	    double maxSpeed = 1;//Defines what fraction of speed the robot will run at
		//double radGyro = (robot.gyro.getHeading() * Math.PI) / 180;
	    double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
		double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
	    if(gamepad1.right_bumper){robotAngle=0; r=1;}
	    else if (gamepad1.left_bumper){robotAngle=180; r=1;}
	    double rightX = gamepad1.right_stick_x;
	    final double v1 = r * Math.cos(robotAngle) + rightX;
	    final double v2 = r * Math.sin(robotAngle) - rightX;
	    final double v3 = r * Math.sin(robotAngle) + rightX;
	    final double v4 = r * Math.cos(robotAngle) - rightX;

	    robot.frontLeft.setPower(v1*maxSpeed);
		robot.frontRight.setPower(v2*maxSpeed);
		robot.backLeft.setPower(v3*maxSpeed);
		robot.backRight.setPower(v4*maxSpeed);

		if(gamepad1.b && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_PLATE=!IS_PLATE;
			INPUT_TIMER = System.currentTimeMillis();
		}
		if(gamepad1.a && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_DUMP=!IS_DUMP;
			INPUT_TIMER = System.currentTimeMillis();
		}

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
		if(gamepad2.a && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_LIFT=!IS_LIFT;
			INPUT_TIMER = System.currentTimeMillis();
		}
		if(gamepad2.y && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_BLOCK_PUSH=!IS_BLOCK_PUSH;
			INPUT_TIMER = System.currentTimeMillis();
		}
		if(gamepad2.x && System.currentTimeMillis() > INPUT_TIMER+500){
	    	if(robot.grabber.getPosition()==IDOL_CLAMP_OPEN){robot.grabber.setPosition(IDOL_CLAMP_CLOSED);}else
	    	if(robot.grabber.getPosition()==IDOL_CLAMP_AJAR){robot.grabber.setPosition(IDOL_CLAMP_OPEN);}else
	    	if(robot.grabber.getPosition()==IDOL_CLAMP_CLOSED){robot.grabber.setPosition(IDOL_CLAMP_AJAR);}
			INPUT_TIMER = System.currentTimeMillis();
		}
		if(gamepad2.dpad_up && System.currentTimeMillis() > INPUT_TIMER+100){
			robot.blockPusher.setPosition(robot.blockPusher.getPosition()+.01);
			INPUT_TIMER = System.currentTimeMillis();
		}
		if(gamepad2.dpad_down && System.currentTimeMillis() > INPUT_TIMER+100){
			robot.blockPusher.setPosition(robot.blockPusher.getPosition()-.01);
			INPUT_TIMER = System.currentTimeMillis();
		}
		if(gamepad2.dpad_left && System.currentTimeMillis() > INPUT_TIMER+100){
			robot.intakeLatch.setPosition(robot.intakeLatch.getPosition()-.01);
			INPUT_TIMER = System.currentTimeMillis();
		}
		if(gamepad2.dpad_right && System.currentTimeMillis() > INPUT_TIMER+100){
			robot.intakeLatch.setPosition(robot.intakeLatch.getPosition()+.01);
			INPUT_TIMER = System.currentTimeMillis();
		}

		INTAKE_SPEED= gamepad2.left_stick_y;
		INTAKE_OFFSET= gamepad2.right_stick_x;

		robot.intakeFrontLeft.setPower(INTAKE_SPEED+.5*INTAKE_OFFSET);
		robot.intakeFrontRight.setPower(INTAKE_SPEED-.5*INTAKE_OFFSET);
		robot.intakeBackLeft.setPower(INTAKE_SPEED+.5*INTAKE_OFFSET);
		robot.intakeBackRight.setPower(INTAKE_SPEED-.5*INTAKE_OFFSET);

	    /*if(IS_PLATE){
			robot.plate.setPosition(PUSH_PLATE_DOWN);
		} else {
			robot.plate.setPosition(PUSH_PLATE_UP);
		}*/

	    if(IS_DUMP){
			IS_BLOCK_PUSH=false;
	    	robot.dump.setTargetPosition(100);
	    } else {
	    	robot.dump.setTargetPosition(DUMP_DOWN);
	    }
	    if(IS_LIFT){
			IS_BLOCK_PUSH=false;
	    	robot.lift.setTargetPosition(-100);
		} else {
	    	robot.lift.setTargetPosition((int)LIFT_DOWN);
		}

		telemetry.addData("Plate is: ", IS_PLATE);
	    telemetry.addData("Dump is:  ", IS_DUMP);
	    telemetry.addData("Lift is:  ", IS_LIFT);
	    telemetry.addData("Block is: ", IS_BLOCK_PUSH);
	    telemetry.addData("DUMP POSITION", robot.dump.getCurrentPosition());
	    telemetry.addData("Block Pusher: ", robot.blockPusher.getPosition());
		telemetry.addData("Intake Latch: ", robot.intakeLatch.getPosition());
	    telemetry.addData("lift Running to:  ", robot.lift.getTargetPosition());
		telemetry.addData("lift Running at:  ", robot.lift.getCurrentPosition());
	    telemetry.addData("dump Running to:  ", robot.dump.getTargetPosition());
		telemetry.addData("dump Running at:  ", robot.dump.getCurrentPosition());
		telemetry.addData("idolY Running to: ", robot.idolY.getTargetPosition());
		telemetry.addData("idolY Running at: ", robot.idolY.getCurrentPosition());
		telemetry.addData("idolZ Running to: ", robot.idolZ.getTargetPosition());
		telemetry.addData("idolZ Running at: ", robot.idolZ.getCurrentPosition());
	    telemetry.addData("frontLeft:  ", robot.frontLeft.getPower());
	    telemetry.addData("frontRight: ", robot.frontRight.getPower());
	    telemetry.addData("backLeft:   ", robot.backLeft.getPower());
	    telemetry.addData("backRight:  ", robot.backRight.getPower());
	    telemetry.addData("Gyro:       ", robot.gyro.getHeading());
		telemetry.addData("Color Blue: ", robot.color.blue());
		telemetry.addData("Color Red:  ", robot.color.red());
		telemetry.addData("Block Range:", robot.blockRange.cmUltrasonic());
		telemetry.addData("Cubby Range:", robot.cubbyRange.cmUltrasonic());
	    telemetry.update();
    }
}
