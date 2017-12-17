package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrd.LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_LEFT;

@Autonomous(name="BlueRightByrd")
public class BlueRightByrd extends AutoByrd {

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
			//forkX(true);
			//int cubby = vuValue(RIGHT);
			hammer(BLUE);
			//move(MOVE_RIGHT,1000,.5);
			//dismount(0);
            move(MOVE_FORWARDS,1000,.5);
            turn(TURN_LEFT,.3);
            move(MOVE_LEFT,750,.5);
            turn(TURN_FORWARDS,.3);
            move(MOVE_BACKWARDS,750,.5);
			vuCubby(LEFT, 2);
			//insert(MOVE_RIGHT);
		}
	}
}
