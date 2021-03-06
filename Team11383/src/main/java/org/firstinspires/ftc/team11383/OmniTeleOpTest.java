package org.firstinspires.ftc.team11383;

/*
 * Created by Walt Morris on 9/25/17.
 */
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "OmniTeleOpTest")

public class OmniTeleOpTest extends OpMode {
    DcMotor rightFrontMotor; // Front Right Motor // Runs in Y Direction //
    DcMotor leftFrontMotor; // Front Left Motor  // Runs in X Direction //
    DcMotor rightBackMotor; // Back Right Motor  // Runs in X Direction //
    DcMotor leftBackMotor; // Back Left Motor   // Runs in Y Direction //
    DcMotor reel; // reel //
    DcMotor track; // slides and pinions //
    Servo armleft;
    Servo armright;
    Servo color;
    Servo colorh;

    boolean FlipDrive = true;
    double timer = 0;

    ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //
    ModernRoboticsI2cColorSensor c; // Color Sensor //

    @Override
    public void init() {
        rightFrontMotor = hardwareMap.dcMotor.get("fr");
        leftFrontMotor = hardwareMap.dcMotor.get("fl");
        rightBackMotor = hardwareMap.dcMotor.get("br");
        leftBackMotor = hardwareMap.dcMotor.get("bl");
        reel = hardwareMap.dcMotor.get("reel");
        track = hardwareMap.dcMotor.get("track");
        armright = hardwareMap.servo.get("ar");
        armleft = hardwareMap.servo.get("al");
        color = hardwareMap.servo.get("ac");
        colorh = hardwareMap.servo.get("ach");
        track.setDirection(DcMotor.Direction.REVERSE);
        reel.setDirection(DcMotor.Direction.REVERSE);


        color.setPosition(.2);
        armright.setPosition(.35);
        armleft.setPosition(-.3);
        colorh.setPosition(.5);
    }
    @Override
    public void loop() {
        float x = gamepad1.left_stick_x;
        float y = gamepad1.left_stick_y;
        float r = gamepad1.right_stick_x;
        float u = gamepad2.left_stick_y;
        float v = gamepad2.right_stick_y;

        reel.setPower(u*.3);
        track.setPower(v*.3);

        if (gamepad2.x) {
            armleft.setPosition(-.4);
            armright.setPosition(.4);
        }
        else if (gamepad2.b) {
            armleft.setPosition(.3);
            armright.setPosition(-.3);
        }
        if (gamepad1.y &&System.currentTimeMillis() > timer+500) {
            FlipDrive = !FlipDrive;
            timer = System.currentTimeMillis();
        }
        if (FlipDrive) {
            leftFrontMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
            leftBackMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            rightBackMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        } else {

            leftFrontMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
            rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
            leftBackMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
            rightBackMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        }
        leftFrontMotor.setPower(x*.75-r*.75);  // Set wheels equal to left stick //
        rightFrontMotor.setPower(y*.75-r*.75);  // direction plus amount of turn, //
        rightBackMotor.setPower(-r*.75-x*.75);  //   determined by right stick.   //
        leftBackMotor.setPower(-r*.75-y*.75);
    }
}