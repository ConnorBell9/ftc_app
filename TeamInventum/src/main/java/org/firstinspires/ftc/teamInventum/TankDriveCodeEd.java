package org.firstinspires.ftc.teamInventum;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="TankDriveCodeEd")
public class TankDriveCodeEd extends OpMode{
    DcMotor frontLeft = hardwareMap.dcMotor.get("frontLeft");
    DcMotor frontRight = hardwareMap.dcMotor.get("frontRight");
    DcMotor backLeft = hardwareMap.dcMotor.get("backLeft");
    DcMotor backRight = hardwareMap.dcMotor.get("backRight");

    @Override
    public void init() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if(gamepad1.left_stick_y>.02 || gamepad1.left_stick_y<-.02){
            frontLeft.setPower(gamepad1.left_stick_y);
            backLeft.setPower(gamepad1.left_stick_y);
        } else {
            frontLeft.setPower(0);
            backLeft.setPower(0);
        }

        if(gamepad1.right_stick_y>.02 || gamepad1.right_stick_y<-.02){
            frontRight.setPower(gamepad1.right_stick_y);
            backRight.setPower(gamepad1.right_stick_y);
        } else
            frontRight.setPower(0);
            backRight.setPower(0);
        }
    }
