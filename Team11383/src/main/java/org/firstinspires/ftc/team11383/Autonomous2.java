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
@Autonomous (name = "Autonomous2")
public class Autonomous2 extends LinearOpMode {
    DcMotor frontleft; // Front Right Motor // Runs in Y Direction //
    DcMotor frontright; // Front Left Motor  // Runs in X Direction //
    DcMotor backright; // Back Right Motor  // Runs in X Direction //
    DcMotor backleft; // Back Left Motor   // Runs in Y Direction //
    DcMotor reel; // reel //
    DcMotor track; // slides and pinions //
    Servo armleft;
    Servo armright;
    Servo color;

    ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //
    //ModernRoboticsI2cColorSensor c; // Color Sensor //
    ColorSensor c; // Color Sensor //

    boolean isRed() {
        if (c.red() < c.blue()) {
            return true;
        }
        return false;
    }
    void Jewels() throws InterruptedException {
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
    }
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

        color.setPosition(.2);
        armleft.setPosition(0);
        armright.setPosition(0);

        waitForStart();

        sleep(1000);
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
        backright.setPower(0);
        frontleft.setPower(0);
        sleep(1000);
    }
}
