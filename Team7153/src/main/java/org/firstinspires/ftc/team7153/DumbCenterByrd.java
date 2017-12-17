package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_FORWARDS;

@Autonomous(name="DumbCenterByrd")
public class DumbCenterByrd extends AutoByrd {

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
            robot.idolY.setTargetPosition(300);
			grab(true);
			forkX(true);
			sleep(1000);
			forkY(true);
            move(MOVE_FORWARDS,1500,.5);
            forkY(false);
            sleep(1000);
            grab(false);
            move(MOVE_BACKWARDS,500,.1);
            forkX(false);

		}
	}
}
