package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrd.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrd.MOVE_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_RIGHT;

@Autonomous(name="DumbLeftByrd")
public class DumbLeftByrd extends AutoByrd {

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
			turn(TURN_RIGHT,.3);
			hammer(BLUE);
			turn(TURN_FORWARDS,.3);
            move(MOVE_FORWARDS,1000,.5);
            turn(315,.3);
            move(315, 1000,.5);
            forkY(false);
            sleep(1000);
            grab(false);
            move(135,500,.1);
            forkX(false);

		}
	}
}
