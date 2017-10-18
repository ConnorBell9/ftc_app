package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;


@TeleOp(name="TestOp")
public class TestOp extends OpMode{

    ModernRoboticsI2cGyro gyro; // Gyroscope Sensor //
    OpticalDistanceSensor odsOut;
    ColorSensor color;
    ModernRoboticsI2cRangeSensor range;

    @Override
    public void init() {
        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro.calibrate();
        odsOut = hardwareMap.opticalDistanceSensor.get("odsIn");
        color = hardwareMap.colorSensor.get("color");
        range = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
        color.enableLed(false);
    }

    @Override
    public void loop() {
        telemetry.addData("Gyro", gyro);
        telemetry.addData("Raw odsOut: ", odsOut.getRawLightDetected());
        telemetry.addData("Normal odsOut: ", odsOut.getLightDetected());
        telemetry.addData("Color Red: ", color.red());
        telemetry.addData("Color Blue: ", color.blue());
        telemetry.addData("CM Distance: ", range.getDistance(DistanceUnit.CM));
        telemetry.update;
    }
}
