package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.DEFAULT_MOVE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrd.DEFAULT_TURN_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrd.DUMP_INACTIVE;
import static org.firstinspires.ftc.team7153.HardwareByrd.RED;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.VUFORIA_DISABLED;
import static org.firstinspires.ftc.team7153.HardwareByrd.VUFORIA_ENABLED;

@Autonomous(name="RedLeftByrd")
public class RedLeftByrd extends AutoByrd {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			hammer(RED);
            moveWithEncoders(24,DEFAULT_MOVE_SPEED,BACKWARDS);//Dismount off of the platform
	    	moveWithEncoders(16,DEFAULT_MOVE_SPEED,BACKWARDS);//Drive forwards to line up with the middle cryptobox
            moveToCubby(TURN_RIGHT, VUFORIA_ENABLED);//Place block in the selected cryptobox
            /*while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(0,42,TURN_FORWARDS);
            	moveToCubby(TURN_RIGHT,VUFORIA_DISABLED);
			}
			stopMoving();
            intake(DUMP_INACTIVE);
            //moveToCubby2();*/
            turn(TURN_LEFT,DEFAULT_TURN_SPEED);
            sleep(1000);
            stopMoving();
		}
	}
}
