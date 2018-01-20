package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_FORWARDS;

@Autonomous(name="TesterAuto")
public class TesterAuto extends AutoByrdMK2 {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			moveWithoutStopping(MOVE_FORWARDS,.1);
			while(!isStopRequested()){
				telemetry();
				sleep(50);
			}
			stopMoving();
		}
	}
}
