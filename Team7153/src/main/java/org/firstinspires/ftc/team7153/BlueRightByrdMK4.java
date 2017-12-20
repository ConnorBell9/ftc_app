package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_BACK;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;

@Autonomous(name="BlueRightByrdMK4")
public class BlueRightByrdMK4 extends AutoByrdMK2 {

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		telemetry.addData(">", "Gyro Calibrating. Do Not move!");
		telemetry.update();
		robot.gyro.calibrate();

		while (!isStopRequested() && robot.gyro.isCalibrating()) {
			Thread.sleep(50);
			idle();
		}

		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();

		waitForStart();
		robot.gyro.resetZAxisIntegrator();
		IS_GYRO_ON = true;
		if (!isStopRequested()) {
            //robot.idolY.setTargetPosition(50);
			grab(true);
			sleep(500);
			grab(false);
			sleep(1000);
			grab(true);
			sleep(500);
			forkY(true);
			hammer(BLUE);
            moveWithEncoders(32,.5,FORWARDS);
            turn(TURN_LEFT,.3);
            moveWithEncoders(36,.5,FORWARDS);
            turn(TURN_BACK,.3);
            moveWithEncoders(36,.5,FORWARDS);
		}
	}
}
