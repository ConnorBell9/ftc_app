package org.firstinspires.ftc.team7153;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.team7153.HardwareByrd.BACKWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.DEFAULT_MOVE_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrd.DEFAULT_TURN_SPEED;
import static org.firstinspires.ftc.team7153.HardwareByrd.DUMP_INACTIVE;
import static org.firstinspires.ftc.team7153.HardwareByrd.RED;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_FORWARDS;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_LEFT;
import static org.firstinspires.ftc.team7153.HardwareByrd.TURN_RIGHT;
import static org.firstinspires.ftc.team7153.HardwareByrd.VUFORIA_DISABLED;
import static org.firstinspires.ftc.team7153.HardwareByrd.VUFORIA_ENABLED;

@Autonomous(name="RedLeftByrd")
public class RedLeftByrd extends AutoByrd {
	@Override
	public void runOpMode() throws InterruptedException {
		autonomousInit();
		waitForStart();
		autonomousStart();
		if (!isStopRequested()) {
			hammer(RED);
			/* This should knock off the BLUE ball
			 * If it knocks off the wrong one first thing to do would be check the color sensor or move the sensor to the other side
			 * Otherwise this code has worked perfectly since its conception and shouldn't pose any problems
			 */
            moveWithEncoders(24,DEFAULT_MOVE_SPEED,BACKWARDS);
			/* This should simply get the robot off of the platform
			 * If it doesn't go far enough or ends up extremely far enough away simply change the first value distance
			 * If it goes in the wrong direction simply swap BACKWARDS for FORWARDS
			 */
	    	moveWithEncoders(16,DEFAULT_MOVE_SPEED,BACKWARDS);
	    	/* This should get the robot to the middle of the cubbies
			 * If it doesn't go far enough or ends up extremely far enough away simply change the first value distance
			 * If it goes in the wrong direction simply swap FORWARDS for BACKWARDS
			 * I have noticed that the robot tends to rotate towards the right end that has the idol grabber
			 * if the robot does so then it may be best to adjust the distance value to account for this movement.
			 */
            moveToCubby(TURN_RIGHT, VUFORIA_ENABLED);
 			/* This should do everything necessary to place the block
             * If this turns the wrong direction make sure the Turn_Direction value is set to which position has the
             * robot facing away from the cubby. Forwards is considered the direction the robot was facing when it was turned on
             * If the robot ends up getting too close or too far from the cubby consider changing the first MoveWithEncoders() function.
             * If the robot ends up too much left or right of the cubby consider changing the second MoveWithEncoders() function.
             */
			/* IGNORE This is test code
			while(runTime.milliseconds()<20000 && opModeIsActive()){
            	harvest(0,42,TURN_FORWARDS);
            	moveToCubby(TURN_RIGHT,VUFORIA_DISABLED);
			}
			stopMoving();
            intake(DUMP_INACTIVE);
            moveToCubby2();*/
            turn(TURN_LEFT,DEFAULT_TURN_SPEED);
            /* This should make the robot face back towards the cubbies after all is said and done.
			 * In the case it is facing the wrong direction change the turn direction and all should be well.
			 */
            sleep(1000);
            stopMoving();
		}
	}
}
