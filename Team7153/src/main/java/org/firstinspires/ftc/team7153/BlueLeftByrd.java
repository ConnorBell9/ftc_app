package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrd.DEFAULT_MOVE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrd.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.VUFORIA_ENABLED;

@Autonomous(name="BlueLeftByrd")
public class BlueLeftByrd extends AutoByrd {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
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
