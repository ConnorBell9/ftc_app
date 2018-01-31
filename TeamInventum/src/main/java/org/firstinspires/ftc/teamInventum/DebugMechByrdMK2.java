package org.firstinspires.ftc.teamInventum;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="DebugMechByrdMK2")
public class DebugMechByrdMK2 extends OpMode{
	private HardwareInventum robot = new HardwareInventum();
    @Override
    public void init() {
		robot.init(hardwareMap);
		robot.color.enableLed(true);
    }

	boolean IS_BLOCK_GRAB = false;
    long INPUT_TIMER=0;

    @Override
    public void loop() {
	    double maxSpeed = 1;//Defines what fraction of speed the robot will run at

	    final double y=gamepad1.left_stick_y;
		final double x=gamepad1.left_stick_x;

		robot.leftMotor.setPower(y-x);
		robot.rightMotor.setPower(y+x);

	    /*if(gamepad2.a && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_BLOCK_GRAB=!IS_BLOCK_GRAB;
			INPUT_TIMER = System.currentTimeMillis();
	    }

	    if(IS_BLOCK_GRAB){

		}*/
	    if(gamepad2.right_stick_y>.02){
	    	robot.armMotor.setPower(gamepad1.right_stick_y*.45);
		} else if(gamepad1.right_stick_y<-.02){
			robot.armMotor.setPower(gamepad1.right_stick_y*.2);
		}

		if(gamepad1.dpad_up){
			robot.rightClaw.setPosition(.01+robot.rightClaw.getPosition());
			robot.leftClaw.setPosition(-.01+robot.leftClaw.getPosition());
		} else if (gamepad1.dpad_up){
			robot.rightClaw.setPosition(.01+robot.rightClaw.getPosition());
			robot.leftClaw.setPosition(-.01+robot.leftClaw.getPosition());
		}
		if(gamepad1.dpad_left){
			robot.hammer.setPosition(.01+robot.hammer.getPosition());
		} else if(gamepad1.dpad_right){
			robot.hammer.setPosition(-.01+robot.hammer.getPosition());
		}

		telemetry.addData("rightClaw:  ", robot.rightClaw.getPosition());
		telemetry.addData("leftClaw:   ", robot.leftClaw.getPosition());
		telemetry.addData("hammer:     ", robot.hammer.getPosition());
		telemetry.addData("Color Blue: ", robot.color.blue());
		telemetry.addData("Color Red:  ", robot.color.red());
	    telemetry.update();
    }
}
