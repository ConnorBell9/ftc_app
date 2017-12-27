package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;

@Autonomous(name="RedLeftByrd")
public class RedLeftByrd extends AutoByrdMK2 {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			hammer(RED);
			sleep(1000);
			hammer(BLUE);
			sleep(1000);
			hammer(RED);
			sleep(1000);
			hammer(BLUE);
			sleep(1000);
			hammer(RED);
			sleep(1000);
			hammer(BLUE);
			sleep(1000);
			/*grab(false);
			sleep(1000);
			grab(true);
			sleep(100);
			clamp(CLAMP_POSITION_2);
			hammer(RED);
            moveWithEncoders(36,.3,BACKWARDS);
            turn(TURN_LEFT,.3);
            moveToCubby(MOVE_LEFT,1, VUFORIA_ENABLED);
            /*while(runTime.milliseconds()<25000 && opModeIsActive()){
            	harvest(0,44);
            	moveToCubby(MOVE_LEFT,1,VUFORIA_DISABLED);
			}*/
			/*stopMoving();
            intake(false);*/
		}
	}
}