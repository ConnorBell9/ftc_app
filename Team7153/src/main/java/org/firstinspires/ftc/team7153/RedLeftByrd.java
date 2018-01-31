package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.DEFAULT_MOVE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;

@Autonomous(name="RedLeftByrd")
public class RedLeftByrd extends AutoByrdMK3 {
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
			hammer(RED);
            moveWithEncoders(36,DEFAULT_MOVE_SPEED,BACKWARDS);
            /*moveToCubby(TURN_LEFT, VUFORIA_ENABLED);
            while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(0,30,TURN_FORWARDS);
            	moveToCubby(TURN_LEFT,VUFORIA_DISABLED);
			}
			stopMoving();
            intake(false);*/
            moveToCubby2();
            stopMoving();
		}
	}
}
