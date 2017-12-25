package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;

@Autonomous(name="BlueRightByrdMK5")
public class BlueRightByrdMK5 extends AutoByrdMK2 {
	//private VuforiaLocalizer vuforia;

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		IS_GYRO_ON = true;
		if (!isStopRequested()) {
			grab(false);
			sleep(1000);
			grab(true);
			sleep(100);
			forkY(true);
			hammer(BLUE);
            moveWithEncoders(36,.5,FORWARDS);
            turn(TURN_LEFT,.3);
            moveToCubby(MOVE_LEFT);
            stopMoving();
		}
	}
}