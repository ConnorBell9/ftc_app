package org.firstinspires.ftc.team11383;

/*
 * Created by Walt Morris on 9/25/17.
 */

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

    ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //

    @Override
    public void init() {
        frontright = hardwareMap.dcMotor.get("fr");
        frontleft = hardwareMap.dcMotor.get("fl");
        backright = hardwareMap.dcMotor.get("br");
        backleft = hardwareMap.dcMotor.get("bl");
        reel = hardwareMap.dcMotor.get("reel");
        track = harewareMap.dcMotor.get("track");
        armright = hardwareMap.servo.get("ar");
        armleft = hardwareMap.servo.get("al");
        
        armright.setPosition(0);
        armleft.setPosition(0);
    }
    @Override
    public void loop() {
        float x = gamepad1.left_stick_x;
        float y = gamepad1.left_stick_y;
        float r = gamepad1.right_stick_x;
        float u = gamepad2.left_stick_y;
        float v = gamepad2.right_stick_y;

        frontleft.setPower(x+r);  // Set wheels equal to left stick //
        frontright.setPower(y+r);  // direction plus amount of turn, //
        backright.setPower(r-x);  //   determined by right stick.   //
        backleft.setPower(r-y);
        reel.setPower(u*.1);
        track.setPower(v*.1);
        
        if (gamepad2.x) {
            armleft.setPosition(-.5);
            armright.setPosition(.5);
        }
        else if (gamepad2.b) {
            armleft.setPosition(.5);
            armright.setPosition(-.5);
        }
        
    }
}
