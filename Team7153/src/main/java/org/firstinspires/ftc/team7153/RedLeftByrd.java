package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.CLAMP_POSITION_2;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RED;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.VUFORIA_ENABLED;

@Autonomous(name="RedLeftByrd")
public class RedLeftByrd extends AutoByrdMK2 {
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		robot.color.enableLed(false);

		//Activate Clamps's encoders
		robot.clamp.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.clamp.setPower(1);

		//Calibrate the Gyroscope
		telemetry.addData(">", "Gyro Calibrating. Do Not move!");
		telemetry.update();
		robot.gyro.calibrate();

		while (!isStopRequested() && robot.gyro.isCalibrating()) {
			sleep(50);
			idle();
		}

		telemetry.addData(">", "Gyro Calibrated.  Press Start.");
		telemetry.update();
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
			hammer(RED);
            moveWithEncoders(36,.3,BACKWARDS);
            turn(TURN_LEFT,.3);
            moveToCubby(MOVE_LEFT,1, VUFORIA_ENABLED);
            /*while(runTime.milliseconds()<25000 && opModeIsActive()){
            	harvest(0,44);
            	moveToCubby(MOVE_LEFT,1,VUFORIA_DISABLED);
			}*/
			stopMoving();
            intake(false);
            telemetry.update();
		}
	}
}