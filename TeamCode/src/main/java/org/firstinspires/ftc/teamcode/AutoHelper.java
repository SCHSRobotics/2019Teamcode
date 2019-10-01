package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drivers.ArmDriver;
import org.firstinspires.ftc.teamcode.drivers.LinearActuator;
import org.firstinspires.ftc.teamcode.drivers.Mecanum4WheelDriver;
import org.firstinspires.ftc.teamcode.drivers.Multitasker;
import org.firstinspires.ftc.teamcode.drivers.TelemetryUpdater;

public abstract class AutoHelper extends LinearOpMode {

    public DcMotor lm;
    public Mecanum4WheelDriver driver;
    public LinearActuator lift;
    public ArmDriver arm;
    public Multitasker multi;
    public DcMotor[] motors;

    public ModernRoboticsI2cRangeSensor rangeSensor;

    private SamplingOrderDetector detector;

    public void initHardware(){
        lm = hardwareMap.dcMotor.get("lift");
        //sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "distance");
        driver = new Mecanum4WheelDriver();
        lift = new LinearActuator(lm);
        arm = new ArmDriver(hardwareMap.dcMotor.get("pully"), hardwareMap.dcMotor.get("angle"), hardwareMap.crservo.get("goboi"));
        multi = new Multitasker(this);
        motors = new DcMotor[]{hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br")};
        //gyro = (IntegratingGyroscope) modernRoboticsI2cGyro;
        driver.init(motors, -1, .1f);
        multi.addTask(driver);
        multi.addTask(arm);
        multi.addTask(new TelemetryUpdater());

        detector = new SamplingOrderDetector(); // Create the detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize detector with app context and camera
        detector.useDefaults(); // Set detector to use default settings

        detector.downscale = 0.8; // How much to downscale the input frames

        // Optional tuning
        //detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;

        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable(); // Start detector
    }
    public String detectCube(){
        return detector.getLastOrder().toString();
    }
    public void turn(float angle){
        float mag = (float)Math.copySign(.4,angle);
        driver.setR(mag);
        multi.waitTime(10);
        angle = Math.abs(angle);
        while(opModeIsActive() && Math.abs(driver.rotation)<angle){
            multi.yield();
            if(Math.abs(driver.rotation)-angle>-8f)driver.setR(mag/5);
        }
        driver.setR(0);
    }
    public void goY(double d){
        float mag = (float)Math.copySign(.45,d);
        driver.setY(mag);
        multi.waitTime(10);
        d = Math.abs(d);
        while(opModeIsActive() && Math.abs(driver.distance)<d){
            multi.yield();
            if(Math.abs(driver.distance)-d>-.46f)driver.setY(mag/5);
        }
        driver.setY(0);
    }
    public void lower(){

        // save start position
        long ref = lm.getCurrentPosition();

        // drop
        lift.setState(1);
        // this loop makes the linear actuator displacement independent of battery life using encoders.
        while (opModeIsActive() && (lm.getCurrentPosition() - ref) > -8035) {
            multi.yield();
        }
        lift.setState(0);
        if (isStopRequested()) {
            shutdown();
            return;
        }

        // unlatch
        multi.waitTime(100);
        //driver.setY(-.3);
        goY(-.17);//multi.waitTime(400);
        //driver.setY(0);

        // retract
        lift.setState(-1);
        while (opModeIsActive() && (lm.getCurrentPosition() - ref) < -10) {
            multi.yield();
        }
        lift.setState(0);
        if (isStopRequested()) {
            shutdown();
            return;
        }
        //driver.setY(.3);
        goY(.17);//multi.waitTime(400);
        //driver.setY(0);
    }
    public void stopMotors(){
        driver.motorSet(0,0,0,0);
        arm.off();
        arm.pullyoff();
        lift.setState(0);
    }
    public void waitShort(){
        multi.waitTime(400);
    }
    public void waitLong(){
        multi.waitTime(2500);
    }
    public void shutdown() {
        stopMotors();
        detector.getLastOrder();
        detector.disable();
        detector.getLastOrder();
        detector.disable();
    }
    public void lowerAndSample() {

        // save start position
        long ref = lm.getCurrentPosition();

        // drop
        lift.setState(1);
        // this loop makes the linear actuator displacement independent of battery life using encoders.
        while (opModeIsActive() && (lm.getCurrentPosition() - ref) > -8035) {
            multi.yield();
        }
        lift.setState(0);
        if (isStopRequested()) {
            shutdown();
            return;
        }
        // detect before moving right or left
        String option = detectCube();
        detector.getLastOrder();
        detector.disable();

        // unlatch
        multi.waitTime(100);
        driver.setY(-.3);
        multi.waitTime(600);
        driver.setY(0);

        // retract
        lift.setState(-1);
        multi.waitTime(1200);
        driver.setY(.3);
        multi.waitTime(400);
        driver.setY(0);
        while (opModeIsActive() && (lm.getCurrentPosition() - ref) < -10) {
            multi.yield();
        }
        lift.setState(0);
        if (isStopRequested()) {
            shutdown();
            return;
        }

        if (option.equals("LEFT")) {
            turn(-120);
            waitShort();
            goY(.9);
            goY(-.88);
            turn(30);
        } else if (option.equals("CENTER")) {
            turn(-75);
            waitShort();
            goY(.8);
            goY(-.75);
            turn(-5);
        } else if (option.equals("RIGHT")) {
            turn(-40);
            waitShort();
            goY(1.08);
            goY(-1.03);
            turn(-30);
        } else {
            turn(-90);
        }
        goY(.44);

        telemetry.addData("opt=", option);
        telemetry.update();
    }
    public void rideWall(double distance){
        float mag = (float)Math.copySign(.45,distance);
        driver.setY(mag);
        multi.waitTime(10);
        distance = Math.abs(distance);
        double targ = 8;
        while(opModeIsActive() && Math.abs(driver.distance)<distance){
            multi.yield();
            if(readDistance()<3.5){
                driver.setR(-.12 * mag);
                driver.setY(0);
            }else {
                if (Math.abs(driver.distance) - distance > -.46f) driver.setY(mag / 5);
                else {
                    if (readDistance() > targ)
                        driver.setR(Math.min(.12 * mag, 0.05 * mag * Math.abs(targ - readDistance())));
                    else
                        driver.setR(Math.max(-.12 * mag, -0.05 * mag * Math.abs(targ - readDistance())));
                }
            }
        }
        driver.setY(0);
    }
    public double readDistance(){
        return rangeSensor.getDistance(DistanceUnit.CM);
    }
}
