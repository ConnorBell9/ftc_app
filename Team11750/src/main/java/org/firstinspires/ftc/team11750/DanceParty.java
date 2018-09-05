package org.firstinspires.ftc.team11750;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the Bellatorum hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
int direction = 1;
@Autonomous(name="Dance", group="Bellatorum")
public class DanceParty extends BellatorumAuto {

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        autonomousInit(); // Initialize the autonomous method

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");
        if (!robot.clampInstalled) telemetry.addData("Status","### Clamp disabled ###");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        gyro.resetZAxisIntegrator(); // Reset the gyro
      while(opModeIsActive()){
        seed();
        if(seed == 1){
          robot.armDown();
          turn(360*4*direction, 1);
          robot.armUp();
        } else if (seed == 2){
          robot.armDown();
          move(robot.RIGHT, 1);
          robot.armUp();
          move(robot.FORWARD, 1);
          robot.armDown();
          move(robot.LEFT, 1);
          robot.armUp();
          move(robot.BACK, 1);
        } else if (seed == 3){
          turn(180*direction, 1); 
        } else if (seed == 4){
          turn(90*direction, 1); 
        } else if (seed == 5){
          robot.armDown();
          move(robot.LEFT, 1);
          robot.armUp();
          move(robot.RIGHT, 2);
          robot.armDown();
          move(robot.LEFT, 1);
          robot.armUp();
        } else if (seed == 6) {
          for(int i = 0, i<8, i++){
            robot.armUp();
            move(robot.RIGHT, 1);
            robot.armDown();
            turn(45*direction, 1);
          }
        } else if (seed == 7) {
          for(int i=0, i<4, i++){
            robot.armUp();
            move(robot.FORWARD,1);
            move(robot.BACK,1);
            robot.armDown();
            turn(90*direction, 1);
          }
        } else if (seed == 8){
          for(int i = 0, i<10, i++){
            robot.armUp();
            sleep(50);
            robot.armDown();
            sleep(50);
          }
        }  
        sleep(500);
      }
    }
  public void seed(){
    double seed = ThreadLocalRandom.current().nextInt(1, 8 + 1);
    direction*=-1;
    telemetry.addData("Seed: " + seed);
    telemetry.update();
  }
}
