package TestsAndLegacy;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.drivers.ArmDriver;
import org.firstinspires.ftc.teamcode.drivers.Mecanum4WheelDriver;
import org.firstinspires.ftc.teamcode.drivers.Multitasker;
import org.firstinspires.ftc.teamcode.drivers.TelemetryUpdater;

/**
 * Created by SCRoboticsDev on 12/5/2018.
 */
//@TeleOp(name="Claiming Auto Op", group="2018")
public class ClaimingAutoOpAdv extends LinearOpMode{

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    @Override
    public void runOpMode() throws InterruptedException {

        // initailize here

        Mecanum4WheelDriver driver = new Mecanum4WheelDriver();
        ArmDriver arm = new ArmDriver(hardwareMap.dcMotor.get("pully"),hardwareMap.dcMotor.get("angle"),hardwareMap.crservo.get("goboi"));
        Multitasker multi = new Multitasker(this);
        DcMotor[] motors = {hardwareMap.dcMotor.get("fl"),hardwareMap.dcMotor.get("fr"),hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br")};
        driver.init(motors,-1,.1f);
        multi.addTask(driver);
        multi.addTask(arm);
        //multi.addTask(new TelemetryUpdater());
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
        VoltageSensor voltSensor = hardwareMap.voltageSensor.get("");

        waitForStart();

        int pos = -1;

        driver.setY(.5);
        multi.waitTime(455);
        driver.setY(0);
        multi.waitTime(1000);
        // it goes forward

        boolean isWhite = getColorIsWhite();
        // it scans the minaral for color

        telemetry.addData("isWhite", isWhite);
        telemetry.update();
        // tells wether it is white or not

        //if it is white then ...
        if (isWhite){
            telemetry.addLine("going to next block");
            telemetry.update();
            multi.waitTime(1000);

            driver.setY(-.4);
            multi.waitTime(100);
            driver.setY(0);
            multi.waitTime(50);
            driver.setX(.5);
            multi.waitTime(800);
            driver.setX(0);
            multi.waitTime(50);
            driver.setY(.4);
            multi.waitTime(140);
            driver.setY(0);
            //it goes back then strafes then stops then goes forward (hopefully to next block)

            // it dose another color check
            multi.waitTime(1000);
            isWhite = getColorIsWhite();

            // if it's white again ...
            if (isWhite){
                telemetry.addLine("executing move to yellow");
                telemetry.update();
                multi.waitTime(1000);

                driver.setY(-.4);
                multi.waitTime(160);
                driver.setY(0);
                multi.waitTime(50);
                driver.setX(-.5);
                multi.waitTime(1600);
                driver.setX(0);
                multi.waitTime(50);
                driver.setY(.5);
                multi.waitTime(800);
                driver.setY(0);
                // hopefully it should go to the last block

                multi.waitTime(150);

                //if not ...
            }else {
                telemetry.addLine("found yellow");
                telemetry.update();
                multi.waitTime(1000);

                pos = 0;

                multi.waitTime(150);
            }
            // if not ALL that then ...
        }else{
            telemetry.addLine("found yellow");
            telemetry.update();
            multi.waitTime(1000);

            multi.waitTime(150);

            pos=1;
            // if the very first one is not white then it's yellow so it runs into it and extends arm
        }
        telemetry.update();

        multi.waitTime(300);

        if(pos==0){
            driver.setR(.2);
            driver.setY(.4);
            multi.waitTime(800);
            driver.setR(0);
            driver.setY(0);

            // drop token thingy
            arm.ColectBoiBack();
            multi.waitTime(1000);

            driver.setY(-.4);
            multi.waitTime(500);
            driver.setY(0);
        }else if(pos==1){
            driver.setY(.4);
            multi.waitTime(600);
            driver.setY(0);

            // drop token thingy
            arm.ColectBoiBack();
            multi.waitTime(1000);

            driver.setY(-.4);
            multi.waitTime(500);
            driver.setY(0);
        }else{
            driver.setR(-.2);
            driver.setY(.4);
            multi.waitTime(800);
            driver.setR(0);
            driver.setY(0);

            // drop token thingy
            arm.ColectBoiBack();
            multi.waitTime(1000);

            driver.setX(.4);
            multi.waitTime(500);
            driver.setX(0);
        }


        driver.setY(-.7);
        multi.waitTime(500);
        driver.setY(0);
        arm.ColectBoiOff();

        multi.waitTime(200);
        driver.setY(0);
        driver.setX(0);
        multi.waitTime(5000);
        // the drivers turn off
    }


    public boolean getColorIsWhite() {
        float hsvValues[] = {0F, 0F, 0F};
        Color.RGBToHSV((int) (sensorColor.red() * 255.0), (int) (sensorColor.green() * 255.0), (int) (sensorColor.blue() * 255.0), hsvValues);

        telemetry.addLine("hue = "+hsvValues[0]);
        return hsvValues[0]>50;
    }
}
