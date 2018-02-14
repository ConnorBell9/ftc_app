package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.DEFAULT_MOVE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_ENABLED;

@Autonomous(name="BlueLeftByrd")
public class BlueLeftByrd extends AutoByrdMK3 {
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
			hammer(BLUE);
            moveWithEncoders(24,DEFAULT_MOVE_SPEED,FORWARDS);
            turn(TURN_RIGHT,.3);
            moveWithEncoders(14,DEFAULT_MOVE_SPEED,FORWARDS);
            moveToCubby(TURN_FORWARDS, VUFORIA_ENABLED);
            /*while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(60,24);
            	moveToCubby(TURN_FORWARDS,BLUE, VUFORIA_DISABLED);
			}*/
			stopMoving();
		}
	}
}
