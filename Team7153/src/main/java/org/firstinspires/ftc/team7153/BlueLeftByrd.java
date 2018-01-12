package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_ENABLED;

@Autonomous(name="BlueLeftByrd")
public class BlueLeftByrd extends AutoByrdMK2 {
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
            moveWithEncoders(24,.3,FORWARDS);
            turn(TURN_RIGHT,.3);
            moveWithEncoders(14,.3,FORWARDS);
            moveToCubby(MOVE_FORWARDS,TURN_FORWARDS,false, VUFORIA_ENABLED);
            /*while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(60,24);
            	moveToCubby(MOVE_FORWARDS,TURN_FORWARDS,BLUE, VUFORIA_DISABLED);
			}*/
			stopMoving();
            intake(false);
		}
	}
}
