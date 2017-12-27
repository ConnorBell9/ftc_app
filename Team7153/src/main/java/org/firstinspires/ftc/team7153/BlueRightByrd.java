package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_DISABLED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_ENABLED;

@Autonomous(name="BlueRightByrd")
public class BlueRightByrd extends AutoByrdMK2 {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();

		//Reset the gyroscope to account for drift
		robot.gyro.resetZAxisIntegrator();

		//Reset the timer to 0
		runTime.reset();

		//Tell the robot that the Gyroscope has been correctly calibrated
		IS_GYRO_ON = true;
		if (!isStopRequested()) {
			grab(false);
			sleep(1000);
			grab(true);
			sleep(100);
			clamp(CLAMP_POSITION_2);
			hammer(BLUE);
            moveWithEncoders(36,.5,FORWARDS);
            turn(TURN_LEFT,.3);
            moveToCubby(MOVE_LEFT,1, VUFORIA_ENABLED);
            while(runTime.milliseconds()<25000 && opModeIsActive()){
            	harvest(0,42);
            	moveToCubby(MOVE_LEFT,1,VUFORIA_DISABLED);
			}
			stopMoving();
            intake(false);
		}
	}
}