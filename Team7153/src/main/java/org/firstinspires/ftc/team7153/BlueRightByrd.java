package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_ENABLED;

@Autonomous(name="BlueRightByrd")
public class BlueRightByrd extends AutoByrdMK2 {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			grab(false);
			sleep(1000);
			grab(true);
			sleep(100);
			clamp(CLAMP_POSITION_2);
			hammer(BLUE);
            moveWithEncoders(36,.3,FORWARDS);
            moveToCubby(MOVE_LEFT,TURN_LEFT,BLUE, VUFORIA_ENABLED);
            /*while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(0,44);
            	moveToCubby(MOVE_LEFT,TURN_LEFT,BLUE, VUFORIA_DISABLED);
			}*/
			stopMoving();
            intake(false);
		}
	}
}
