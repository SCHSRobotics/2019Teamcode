package TestsAndLegacy;

/**
 * Created by SCRoboticsDev on 11/14/2018.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivers.Multitasker;
import org.firstinspires.ftc.teamcode.drivers.UltimateDriver;


//@TeleOp(name="StrafingBoi", group="2018")
public class StrafingBoi extends LinearOpMode {
    public void runOpMode(){
        UltimateDriver driver = new UltimateDriver();
        Multitasker multi = new Multitasker(this);
        multi.addTask(driver);
        try {
            DcMotor[] motors = {hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br")};
            driver.init(motors);
        }catch (Exception e){
            System.out.println("\n------    HARDWARE ERROR IN INIT!   ------\n");
            e.printStackTrace();
        }
        try {
            waitForStart();
        }catch (Exception e) {
            e.printStackTrace();

        }
        for(int i=0;i<2;i++) {//robot runs the commands 3 times
            try {
                driver.autoDrive(0, 1);
                Thread.sleep(500);
                driver.autoDrive(0, .5);
                Thread.sleep(1000);
                driver.autoDrive(0, 0);
                Thread.sleep(500);
                driver.autoDrive(90, .5);
                Thread.sleep(500);
                driver.autoDrive(270, .5);
                Thread.sleep(500);
            }catch(Exception e){
                telemetry.addLine("ERROR IN MAIN CODE!!!!");
                return;
            }
        }
    }
}