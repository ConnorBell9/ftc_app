package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.RED;
import static org.firstinspires.ftc.team7153.HardwareByrd.RIGHT;

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
		if (!isStopRequested()) {
			grab(true);
			forkX(true);
			//int cubby = vuValue(RIGHT);
			hammer(RED);
			move(MOVE_RIGHT,1000,.5);
			//dismount(0);
			vuCubby(RIGHT, 2);
			insert(MOVE_RIGHT);
		}
	}
}
