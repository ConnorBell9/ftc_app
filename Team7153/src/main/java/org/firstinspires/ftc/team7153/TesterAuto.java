package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.*;

@Autonomous(name="TesterAuto")
public class TesterAuto extends AutoByrdMK3 {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		grab(false);
		if (!isStopRequested()) {
			while(!isStopRequested()) {
				moveWithoutStopping(MOVE_FORWARDS,.15);
				telemetry();
			}
			stopMoving();
		}
	}
}
