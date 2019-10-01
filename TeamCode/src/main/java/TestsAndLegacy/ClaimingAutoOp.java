package TestsAndLegacy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.drivers.ArmDriver;
import org.firstinspires.ftc.teamcode.drivers.Mecanum4WheelDriver;
import org.firstinspires.ftc.teamcode.drivers.Multitasker;
import org.firstinspires.ftc.teamcode.drivers.TelemetryUpdater;

/**
 * Created by SCRoboticsDev on 12/6/2018.
 */

//@TeleOp(name="Claiming Auto Op Basic", group="2018")
public class ClaimingAutoOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // initailize here
        ColorSensor sensorColor;
        DistanceSensor sensorDistance;

        Mecanum4WheelDriver driver = new Mecanum4WheelDriver();
        ArmDriver arm = new ArmDriver(hardwareMap.dcMotor.get("pully"), hardwareMap.dcMotor.get("angle"), hardwareMap.crservo.get("goboi"));
        Multitasker multi = new Multitasker(this);
        DcMotor[] motors = {hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br")};
        driver.init(motors, -1, .1f);
        multi.addTask(driver);
        multi.addTask(arm);
        multi.addTask(new TelemetryUpdater());
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");


        waitForStart();

        driver.setY(1);
        arm.rotate(-50);
        multi.waitTime(600);
        driver.setY(0);

        multi.waitTime(300);

        // extend some
        arm.extend();
        multi.waitTime(500);
        arm.pullyoff();

        // drop token thingy
        arm.ColectBoiBack();
        multi.waitTime(1000);

        driver.setY(-1);
        multi.waitTime(500);
        driver.setY(0);
        arm.ColectBoiOff();



        multi.waitTime(15000);
    }
}
