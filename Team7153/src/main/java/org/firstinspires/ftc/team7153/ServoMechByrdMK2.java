package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FRONT_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.FRONT_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IDOL_Z_DELTA_POSITION;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_BLOCK_GRAB;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_IDOL_GRAB;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_PLATE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.LEFT_CLAMP_OPEN;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.RIGHT_CLAMP_CLOSE;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.TOP_CLAMP_CLOSE;

@TeleOp(name="ServoMechByrdMK2")
public class
ServoMechByrdMK2 extends OpMode{
	private HardwareByrdMK2 robot = new HardwareByrdMK2();
	private VuforiaLocalizer vuforia;
	private VuforiaLocalizer.Parameters parameters;
	private VuforiaTrackables relicTrackables;
	private VuforiaTrackable relicTemplate;
	private RelicRecoveryVuMark relicVuMark = RelicRecoveryVuMark.UNKNOWN;
    @Override
    public void init() {
		robot.init(hardwareMap);
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

		//Use Vindicem license key
		parameters.vuforiaLicenseKey = "ARK0G5D/////AAAAGTNyS/9bI0eKk0BiZlza4w8qOLSfAS/JLHbvWMY95VY7PgFNgH178LKZTQVDke1Eu9JzX/o9QWeyU5ottyCSuPaRr98YId9QUZtfX918roLvNx3n5bXekGlcKSoxgw+UcH3HN+c8V57B3fFhNMt0uyKEWNAXYmAx1OkvoFUSSurH82uzsGg+aBZ3nlVfj043RPXSDyiJO7uDZmwVH14LPjdhP92Qj6byGdICOqc5dxKG1rVFdNgAWJjYVWbz53K1qNWyO9fYgE0lIjwgNopM2GCFVR2ycS0JHx5UW3Bk2m47kDoFCFJP+A8fWxfLyrtgH02JOzNyHb0VoKv4ZDan5Czl7Wcs+ItJBby3qyEmPRkf";

		//Assign which camera is to be used
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
		this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

		//Load the VuMarks
		relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
		relicTemplate = relicTrackables.get(0);

		relicTemplate.setName("relicVuMarkTemplate");
		if(!IS_GYRO_ON) {
			robot.gyro.calibrate();
			IS_GYRO_ON=true;
		}
		FRONT_LEFT=0;
		FRONT_RIGHT=0;
		IS_GYRO_ON=false;
		IS_BLOCK_GRAB=true;
		robot.color.enableLed(true);
		relicTrackables.activate();
    }

    @Override
    public void loop() {
	    double maxSpeed = 1;//Defines what fraction of speed the robot will run at

	    if(gamepad2.a && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_BLOCK_GRAB=!IS_BLOCK_GRAB;
			INPUT_TIMER = System.currentTimeMillis();
	    }

	    if(IS_BLOCK_GRAB){
		    robot.intakeBottomLeft.setPower(1);
			robot.intakeTopLeft.setPower(1);
			robot.intakeBottomRight.setPower(1);
			robot.intakeTopRight.setPower(1);
	    } else {
			robot.intakeBottomLeft.setPower(-1);
			robot.intakeTopLeft.setPower(-1);
			robot.intakeBottomRight.setPower(-1);
			robot.intakeTopRight.setPower(-1);
	    }

		telemetry.addData("VuMark:   ", relicVuMark);
	    telemetry.addData("Grab is:  ", IS_BLOCK_GRAB);
		telemetry.addData("Plate is: ", IS_PLATE);
		telemetry.addData("Idol is:  ", IS_IDOL_GRAB);
		telemetry.addData("Gyro is:  ", IS_GYRO_ON);
		telemetry.addData("ArmL is at:       ", robot.armL.getPosition());
		telemetry.addData("ArmR is at:       ", robot.armR.getPosition());
		telemetry.addData("ArmT is at:       ", robot.armT.getPosition());
		telemetry.addData("HammerX is at:    ", robot.hammerX.getPosition());
		telemetry.addData("HammerY is at:    ", robot.hammerY.getPosition());
		telemetry.addData("Grabber is at:    ", robot.grabber.getPosition());
	    telemetry.addData("forkY Running to: ", robot.clamp.getTargetPosition());
		telemetry.addData("forkY Running at: ", robot.clamp.getCurrentPosition());
		telemetry.addData("idolY Running to: ", robot.idolY.getTargetPosition());
		telemetry.addData("idolY Running at: ", robot.idolY.getCurrentPosition());
		telemetry.addData("idolZ Running to: ", robot.idolZ.getTargetPosition());
		telemetry.addData("idolZ Running at: ", robot.idolZ.getCurrentPosition());
	    telemetry.addData("frontLeft", robot.frontLeft.getPower());
	    telemetry.addData("frontRight", robot.frontRight.getPower());
	    telemetry.addData("backLeft", robot.backLeft.getPower());
	    telemetry.addData("backRight", robot.backRight.getPower());
	    telemetry.addData("Gyro", robot.gyro.getHeading());
		telemetry.addData("Color Blue: ", robot.color.blue());
		telemetry.addData("Color Red:  ", robot.color.red());
	    telemetry.update();
    }
}
