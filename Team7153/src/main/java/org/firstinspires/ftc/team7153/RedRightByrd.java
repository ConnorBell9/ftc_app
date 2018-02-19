package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.DEFAULT_MOVE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrd.RED;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_BACK;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.VUFORIA_DISABLED;
import static org.firstinspires.ftc.team7153.HardwareByrd.VUFORIA_ENABLED;

@Autonomous(name="RedRightByrd")
public class RedRightByrd extends AutoByrd {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			hammer(RED);
            moveWithEncoders(24,DEFAULT_MOVE_SPEED,BACKWARDS);
            turn(TURN_LEFT,.3);
            moveWithEncoders(14,DEFAULT_MOVE_SPEED,BACKWARDS);
            moveToCubby(TURN_BACK, VUFORIA_ENABLED);
            while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(60,24, TURN_LEFT);
            	moveToCubby(TURN_BACK, VUFORIA_DISABLED);
			}
			stopMoving();
		}
	}
}
