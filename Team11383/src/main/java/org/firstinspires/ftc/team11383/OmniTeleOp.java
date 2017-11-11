package org.firstinspires.ftc.team11383;

/*
 * Created by Walt Morris on 9/25/17.
 */

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "OmniTeleOp")

public class OmniTeleOp extends OpMode {
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
    ModernRoboticsI2cColorSensor c; // Color Sensor //

    @Override
    public void init() {
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
        armright.setPosition(.5);
        armleft.setPosition(-.5);
    }
    @Override
    public void loop() {
        float x = gamepad1.left_stick_x;
        float y = gamepad1.left_stick_y;
        float r = gamepad1.right_stick_x;
        float u = gamepad2.left_stick_y;
        float v = gamepad2.right_stick_y;

        frontleft.setPower(x-r);  // Set wheels equal to left stick //
        frontright.setPower(y-r);  // direction plus amount of turn, //
        backright.setPower(-r-x);  //   determined by right stick.   //
        backleft.setPower(-r-y);
        reel.setPower(u*.3);
        track.setPower(v*.3);
        
        if (gamepad2.x) {
            armleft.setPosition(-.5);
            armright.setPosition(.5);
        }
        else if (gamepad2.b) {
            armleft.setPosition(.4);
            armright.setPosition(-.4);
        }
        
    }
}
