package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="TesterAuto")
public class TesterAuto extends AutoByrdMK3 {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			while(!isStopRequested()){
				for(double x=0,y=0; x<.5 && y>-.5; x+=.01, y-=.01){
					robot.frontLeft.setPower(y);
					robot.frontRight.setPower(x);
					robot.backLeft.setPower(y);
					robot.backRight.setPower(x);
					telemetry();
					sleep(1000);
				}
				for(double x=0,y=0; x>0 && y<0; x-=.01, y+=.01){
					robot.frontLeft.setPower(y);
					robot.frontRight.setPower(x);
					robot.backLeft.setPower(y);
					robot.backRight.setPower(x);
					telemetry();
					sleep(1000);
				}
			}
			stopMoving();
		}
	}
}
