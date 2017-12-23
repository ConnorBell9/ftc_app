package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.BLUE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.MOVE_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_BACK;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TURN_LEFT;

@Autonomous(name="BlueLeftByrdMK4")
public class BlueLeftByrdMK4 extends AutoByrdMK2 {
	//private VuforiaLocalizer vuforia;

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);

		/*int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		parameters.vuforiaLicenseKey = "ARK0G5D/////AAAAGTNyS/9bI0eKk0BiZlza4w8qOLSfAS/JLHbvWMY95VY7PgFNgH178LKZTQVDke1Eu9JzX/o9QWeyU5ottyCSuPaRr98YId9QUZtfX918roLvNx3n5bXekGlcKSoxgw+UcH3HN+c8V57B3fFhNMt0uyKEWNAXYmAx1OkvoFUSSurH82uzsGg+aBZ3nlVfj043RPXSDyiJO7uDZmwVH14LPjdhP92Qj6byGdICOqc5dxKG1rVFdNgAWJjYVWbz53K1qNWyO9fYgE0lIjwgNopM2GCFVR2ycS0JHx5UW3Bk2m47kDoFCFJP+A8fWxfLyrtgH02JOzNyHb0VoKv4ZDan5Czl7Wcs+ItJBby3qyEmPRkf";
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
		this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);*/

		robot.forkY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		robot.forkY.setPower(1);

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
			grab(false);
			sleep(1000);
			grab(true);
			sleep(100);
			forkY(true);
			hammer(BLUE);
            moveWithEncoders(32,.5,FORWARDS);
            turn(TURN_LEFT,.3);
            moveWithEncoders(28,.5,FORWARDS);
            turn(TURN_BACK,.3);
            moveWithEncoders(13,.5,FORWARDS);
            turn(TURN_LEFT,.3);
            moveToCubby(MOVE_LEFT);
		}
	}
}