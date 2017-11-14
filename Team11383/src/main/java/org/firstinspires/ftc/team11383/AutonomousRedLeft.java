package org.firstinspires.ftc.team11383;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Walt on 11/06/17.
 */
@Autonomous (name = "AutonomousRedLeft")
public class AutonomousRedLeft extends LinearOpMode {
    DcMotor frontleft; // Front Right Motor // Runs in Y Direction //
    DcMotor frontright; // Front Left Motor  // Runs in X Direction //
    DcMotor backright; // Back Right Motor  // Runs in X Direction //
    DcMotor backleft; // Back Left Motor   // Runs in Y Direction //
    DcMotor reel; // reel //
    DcMotor track; // slides and pinions //
    Servo armleft;
    Servo armright;
    Servo color;
    boolean COLOR_BLUE = true;
    boolean COLOR_RED = false;
    //c = hardwareMap.colorSensor.get("c");
    ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //
   // ModernRoboticsI2cColorSensor c; // Color Sensor //
    ColorSensor c; // Color Sensor //{


   /*boolean isRed() {
        if (c.red() < c.blue()) {
            return true;
        }
        return false;
    }
      /*void Jewels() throws InterruptedException {
        if(!opModeIsActive())
        {backleft.setPower(0); backright.setPower(0); frontright.setPower(0); frontleft.setPower(0);return;}{
            if(isRed()){
                color.setPosition(.8);
                sleep(500);
                backright.setPower(-1); frontleft.setPower(1);
                sleep(500);
                backright.setPower(0); frontleft.setPower(0);
                sleep(1000);
                color.setPosition(.2);
                sleep(500);
                backright.setPower(-1); frontleft.setPower(1);
                sleep(1100);
                backright.setPower(0); frontleft.setPower(0);
                sleep(1000);
            }
            if (!isRed()) {
                color.setPosition(.8);
                sleep(500);
                backright.setPower(1); frontleft.setPower(-1);
                sleep(300);
                color.setPosition(.2);
                sleep(500);
                backright.setPower(-1); frontleft.setPower(1);
                sleep(1900);
            }
        }
    }*/
   /*boolean isRed(){
       if (c.red() < (c.blue())){
           return true;
       }
       return false;
   }*/
   void turnright(double power){
    double direction = 1;
    frontleft.setPower(power);
    backright.setPower(power);
    frontright.setPower(power);
    backleft.setPower(power);
   }

   void turnleft(double power){
       double direction = 1;
       frontright.setPower(-1*power);
       backright.setPower(-1*power);
       frontleft.setPower(-1*power);
       backleft.setPower(-1*power);
   }

   void startRotate(double angle, double power) {
       double direction = 1.0; // The direction to turn

       if (angle < 0) { // If the angle is negative
           direction = -1; // Toggle the direction
           angle *= -1; // Make the angle positive
       }

       // Set all motors to turn in direction at power
       frontright.setPower(power * direction);
       frontleft.setPower(power * direction);
       backright.setPower(power * direction);
       backleft.setPower(power * direction);
   }

    void displaceJewel(boolean colorflag){
        double turnAngle = 0;
        color.setPosition(.8); // color sensor arm is down //

        // Displace the blue color ball
        if (c.blue() >= 1) turnAngle += 20;
        if (c.red() >= 1) turnAngle -= 20;
        //if (c.blue() >=1) frontleft.setPower(.2);
       //backright.setPower(.2);
        //if (c.red() >= 1) frontleft.setPower(-.2);
        //backright.setPower(-.2);


        // Turn the other way to displace thr
        if(colorflag == COLOR_RED) turnAngle*=-1; // Turn the other way
        //non-gyroscope
        /*turnleft(.2);
        sleep(200);
        turnleft(0);
        color.setPosition(.2);
        turnright(.2);
        sleep(200);
        turnright(0);*/
        //gyroscope
        startRotate(turnAngle, .2); // Turn to knock off the jewel
        sleep(200);
        startRotate(turnAngle, 0);
        color.setPosition(.2);   // Raise the arm
        startRotate(-turnAngle, .2);// Turn back
        sleep(200);
        startRotate(turnAngle,0);
    }
    void redTeamJewel(){ displaceJewel(COLOR_BLUE);}
    void blueTeamJewel() {displaceJewel(COLOR_RED);}

    @Override
    public void runOpMode() {

        frontright = hardwareMap.dcMotor.get("fr");
        frontleft = hardwareMap.dcMotor.get("fl");
        backright = hardwareMap.dcMotor.get("br");
        backleft = hardwareMap.dcMotor.get("bl");
        reel = hardwareMap.dcMotor.get("reel");
        track = hardwareMap.dcMotor.get("track");
        armright = hardwareMap.servo.get("ar");
        armleft = hardwareMap.servo.get("al");
        color = hardwareMap.servo.get("ac");
        track.setDirection(DcMotor.Direction.REVERSE);
        reel.setDirection(DcMotor.Direction.REVERSE);
        c = hardwareMap.colorSensor.get("c");

        waitForStart();
        color.setPosition(.2);
        armleft.setPosition(-.5);
        armright.setPosition(.5);
        c.enableLed(true);
       // c.red();
       // c.blue();
        //color.setPosition(.8);
      //  if (c.red())=(true()
          //  (
          //          frontleft.setPower(1);
           // backright.setPower(-1);

           //     )


       /* sleep(1000);
        backleft.setPower(0);
        frontright.setPower(0);
        sleep(1000);
        color.setPosition(1);
        sleep(500);
        backright.setPower(-1);
        frontleft.setPower(1);
        sleep(500);
        backright.setPower(0);
        frontleft.setPower(0);
        sleep(1000);
        color.setPosition(.2);
        sleep(500);
        backright.setPower(-1);
        frontleft.setPower(1);
        sleep(1100);
        frontleft.setPower(1);
        backright.setPower(-1);
        sleep(1300);
        backright.setPower(0);
        frontleft.setPower(0);
        sleep(1000);*/
       displaceJewel(COLOR_BLUE);
    }
}
