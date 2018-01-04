package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IDOL_Z_DELTA_POSITION;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.INPUT_TIMER;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_BLOCK_GRAB;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_GYRO_ON;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_IDOL_GRAB;
import static org.firstinspires.ftc.team7153.HardwareByrdMK2.IS_PLATE;

@TeleOp(name="DebugMechByrdMK2")
public class
DebugMechByrdMK2 extends OpMode{
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
		IS_GYRO_ON=false;
		IS_BLOCK_GRAB=true;
		robot.color.enableLed(true);
		relicTrackables.activate();
    }

    @Override
    public void loop() {
	    double maxSpeed = 1;//Defines what fraction of speed the robot will run at

	    final double v1=gamepad1.left_stick_y;
		final double v2=gamepad1.left_stick_y;
		final double v3=gamepad1.left_stick_y;
		final double v4=gamepad1.left_stick_y;

	    /*robot.frontLeft.setPower(v1*maxSpeed);
		robot.frontRight.setPower(v2*maxSpeed);
		robot.backLeft.setPower(v3*maxSpeed);
		robot.backRight.setPower(v4*maxSpeed);*/

		relicVuMark = RelicRecoveryVuMark.from(relicTemplate);

		if(gamepad1.dpad_right && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.hammerX.setPosition(-.1+robot.hammerX.getPosition());
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad1.dpad_left && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.hammerX.setPosition(.1+robot.hammerX.getPosition());
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad1.dpad_up && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.hammerY.setPosition(.1+robot.hammerY.getPosition());
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad1.dpad_down && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.hammerY.setPosition(-.1+robot.hammerY.getPosition());
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad2.dpad_left && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.armL.setPosition(-.1+robot.armL.getPosition());
			robot.armR.setPosition(.1+robot.armR.getPosition());
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad2.dpad_right && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.armL.setPosition(.1+robot.armL.getPosition());
			robot.armR.setPosition(-.1+robot.armR.getPosition());
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad2.dpad_up && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.frontLeft.setPower(.01+robot.frontLeft.getPower());
			robot.frontRight.setPower(-.01+robot.frontRight.getPower());
			robot.backLeft.setPower(.01+robot.backLeft.getPower());
			robot.backRight.setPower(-.01+robot.backRight.getPower());
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad2.dpad_down && System.currentTimeMillis() > INPUT_TIMER+500){
			robot.frontLeft.setPower(-.01+robot.frontLeft.getPower());
			robot.frontRight.setPower(.01+robot.frontRight.getPower());
			robot.backLeft.setPower(-.01+robot.backLeft.getPower());
			robot.backRight.setPower(.01+robot.backRight.getPower());
			INPUT_TIMER = System.currentTimeMillis();
		}

	    if(gamepad2.a && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_BLOCK_GRAB=!IS_BLOCK_GRAB;
			INPUT_TIMER = System.currentTimeMillis();
	    }

		if(gamepad1.b && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_PLATE=!IS_PLATE;
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad1.y && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_GYRO_ON=!IS_GYRO_ON;
			INPUT_TIMER = System.currentTimeMillis();
		}

		if(gamepad2.x && System.currentTimeMillis() > INPUT_TIMER+500){
			IS_IDOL_GRAB=!IS_IDOL_GRAB;
			INPUT_TIMER = System.currentTimeMillis();
		}

	    /*if(IS_BLOCK_GRAB){
		    robot.armL.setPosition(LEFT_CLAMP_CLOSE);
			robot.armR.setPosition(RIGHT_CLAMP_CLOSE);
			robot.armT.setPower(TOP_CLAMP_CLOSE);
	    } else {
			robot.armL.setPosition(LEFT_CLAMP_OPEN);
			robot.armR.setPosition(RIGHT_CLAMP_OPEN);
			robot.armT.setPower(TOP_CLAMP_OPEN);
	    }

	    if(IS_PLATE){
			robot.plate.setPosition(PUSH_PLATE_DOWN);
		} else {
			robot.plate.setPosition(PUSH_PLATE_UP);
		}

		if(IS_IDOL_GRAB){
			robot.grabber.setPosition(IDOL_CLAMP_CLOSED);
		} else {
			robot.grabber.setPosition(IDOL_CLAMP_OPEN);
		}*/

	    robot.armT.setPosition(1);

		if(gamepad2.left_stick_y>.1) {
			robot.clamp.setPower(gamepad2.left_stick_y);
		} else if(gamepad2.left_stick_y <- .1) {
			robot.clamp.setPower(gamepad2.left_stick_y);
		} else {robot.clamp.setPower(0);}

		if(gamepad2.right_trigger>.1){
			robot.idolY.setPower(.75*gamepad2.right_trigger);
		} else if (gamepad2.left_trigger>.1){
			robot.idolY.setPower(-.75*gamepad2.left_trigger);
		} else {robot.idolY.setPower(0);}
		if(gamepad2.right_bumper && System.currentTimeMillis() > INPUT_TIMER+10){
			robot.idolZ.setTargetPosition(robot.idolZ.getCurrentPosition()+IDOL_Z_DELTA_POSITION);
			INPUT_TIMER = System.currentTimeMillis();
		}else if(gamepad2.left_bumper && System.currentTimeMillis() > INPUT_TIMER+10){
			robot.idolZ.setTargetPosition(robot.idolZ.getCurrentPosition()-IDOL_Z_DELTA_POSITION);
			INPUT_TIMER = System.currentTimeMillis();
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
		telemetry.addData("Color Red: ", robot.color.red());
	    telemetry.update();
    }
}
