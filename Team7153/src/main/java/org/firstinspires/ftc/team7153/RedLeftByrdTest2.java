package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.DEFAULT_MOVE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.DUMP_INACTIVE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_DISABLED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_ENABLED;

@Autonomous(name="RedLeftByrdTest2")
public class RedLeftByrdTest2 extends AutoByrd {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			hammer(RED);
            moveWithEncoders(36,DEFAULT_MOVE_SPEED,BACKWARDS);//36 is previous value
			boolean vuforia=true;
            while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(0,42,TURN_FORWARDS);
            	if(vuforia) {
					moveToCubby(TURN_RIGHT, VUFORIA_ENABLED);
					vuforia=false;
				}
            	moveToCubby(TURN_RIGHT,VUFORIA_DISABLED);
			}
			stopMoving();
            intake(DUMP_INACTIVE);
            stopMoving();
		}
	}
}
