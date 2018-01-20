package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_BACK;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_ENABLED;

@Autonomous(name="RedRightByrd")
public class RedRightByrd extends AutoByrdMK2 {
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
            moveWithEncoders(24,.3,BACKWARDS);
            turn(TURN_LEFT,.3);
            moveWithEncoders(14,.3,BACKWARDS);
            moveToCubby(TURN_BACK, VUFORIA_ENABLED);
           /* while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(60,24);
            	moveToCubby(TURN_BACK,RED, VUFORIA_DISABLED);
			}*/
			stopMoving();
            intake(false);
		}
	}
}
