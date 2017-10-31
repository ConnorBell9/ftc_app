package org.firstinspires.ftc.team11383;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Walt on 10/30/17.
 */
@Autonomous (name = "Autonomous1")
public class Autonomous1 extends LinearOpMode {
    DcMotor frontleft; // Front Right Motor // Runs in Y Direction //
    DcMotor frontright; // Front Left Motor  // Runs in X Direction //
    DcMotor backright; // Back Right Motor  // Runs in X Direction //
    DcMotor backleft; // Back Left Motor   // Runs in Y Direction //
    DcMotor reel; // moves lift in and out //
    DcMotor track; // slides and pinions //
    Servo armleft; // moves left servo arm //
    Servo armright; // moves right servo arm //
    Servo color; // Used to get jewels //

    @Override
    public void runOpMode() throws InterruptedException {
        frontright = hardwareMap.dcMotor.get("fr");
        frontleft = hardwareMap.dcMotor.get("fl");
        backright = hardwareMap.dcMotor.get("br");
        backleft = hardwareMap.dcMotor.get("bl");
        reel = hardwareMap.dcMotor.get("reel");
        track = hardwareMap.dcMotor.get("track");
        armright = hardwareMap.servo.get("ar");
        armleft = hardwareMap.servo.get("al");
        color = hardwareMap.servo.get("c");
        track.setDirection(DcMotor.Direction.REVERSE);
        reel.setDirection(DcMotor.Direction.REVERSE);

        armright.setPosition(0);
        armleft.setPosition(0);

        waitForStart();
    }
}