package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.RED;
import static org.firstinspires.ftc.team7153.HardwareByrd.RIGHT;

@Autonomous(name="RedLeftByrdMK4" , group = "New")
public class RedLeftByrdMK4 extends AutoByrd {

	@Override
	public void runOpMode() throws InterruptedException {
		robot.gyro.resetZAxisIntegrator();
		if (!isStopRequested()) {
			grab(true);
			forkX(true);
			int cubby = vuValue(RIGHT);
			hammer(RED);
			//move(MOVE_RIGHT,1000,.5);
			dismount(0);
			vuCubby(RIGHT, cubby);
			insert(MOVE_RIGHT);
		}
	}
}
