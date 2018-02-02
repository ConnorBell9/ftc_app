package org.firstinspires.ftc.teamInventum;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamInventum.HardwareInventum.RED;
import static org.firstinspires.ftc.teamInventum.HardwareInventum.TURN_FORWARDS;
import static org.firstinspires.ftc.teamInventum.HardwareInventum.TURN_LEFT;
import static org.firstinspires.ftc.teamInventum.HardwareInventum.TURN_RIGHT;

@Autonomous(name="RedLeftInno")
public class RedLeftInno extends AutoInnoMK3 {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			grab(true);
			jewelDisplacer(RED);
            move(-.3,750);
            turn(TURN_LEFT-45,.3);
            move(-.3,500);
            grab(false);
		}
	}
}
