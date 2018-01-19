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
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.2);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.21);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.22);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.23);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.24);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.25);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.26);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.27);
			sleep(1000);
			moveWithoutStopping(MOVE_FORWARDS,.28);
			sleep(1000);
		}
	}
}
