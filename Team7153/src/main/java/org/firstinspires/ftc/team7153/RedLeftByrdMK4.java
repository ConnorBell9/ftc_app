package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.RED;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_BACK;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_LEFT;

@Autonomous(name="RedLeftByrdMK4")
public class RedLeftByrdMK4 extends AutoByrd {

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		telemetry.addData(">", "Gyro Calibrating. Do Not move!");
		telemetry.update();
		robot.gyro.calibrate();

		while (!isStopRequested() && robot.gyro.isCalibrating()) {
			Thread.sleep(50);
			idle();
		}

		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();

		waitForStart();
		robot.gyro.resetZAxisIntegrator();
		IS_GYRO_ON = true;
		if (!isStopRequested()) {
            robot.idolY.setTargetPosition(300);
			grab(true);
			forkX(true);
			sleep(1000);
			forkY(true);
			hammer(RED);
            move(MOVE_FORWARDS,1000,.5);
            turn(TURN_LEFT,.3);
            move(MOVE_RIGHT,1000,.5);
            turn(TURN_BACK,.3);
            move(MOVE_BACKWARDS,750,.5);
			insert(MOVE_RIGHT);
		}
	}
}
