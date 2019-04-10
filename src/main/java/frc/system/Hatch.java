package frc.system;

import org.team6083.lib.dashboard.DashBoard;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class Hatch {

    public static Compressor air;
    public static DoubleSolenoid dhatch;
    public static VictorSP hatch;

    public static Timer timer = new Timer();
    public static boolean isPushed = false;

    public static boolean protectOverride = false;
    public static DashBoard dashBoard = new DashBoard("hatch");

    public static void init() {
        air = new Compressor(2);
        controlCompressor(true);
        dhatch = new DoubleSolenoid(2, 0, 1);
        hatch = new VictorSP(0);

        dashBoard.markReady();
    }

    public static void controlCompressor(boolean on) {
        air.setClosedLoopControl(on);
        SmartDashboard.putBoolean("pneumatic/compCloseLoop", on);
    }

    public static void tele() {
        if (Shooting.an() == true || protectOverride) {
            if (Robot.xBox.getStickButtonPressed(Hand.kRight)) {
                if (dhatch.get() == DoubleSolenoid.Value.kForward) {
                    dhatch.set(DoubleSolenoid.Value.kReverse);
                } else {
                    dhatch.set(DoubleSolenoid.Value.kForward);
                }
            }
        } else {
            dhatch.set(Value.kReverse);
        }

        dashboard();

        if (Robot.controler.check(Robot.xBox.getBButton(), false) || Robot.xBox.getPOV() == 270) {
            hatch.set(0.35);
        } else if (Robot.controler.check(Robot.xBox.getYButton(), false) || Robot.xBox.getPOV() == 90) {
            hatch.set(-0.35);
        } else {
            hatch.set(0);
        }

        if (Robot.xBox1.getPOV() == 0) {
            hatch.set(0.35);
        } else if (Robot.xBox1.getPOV() == 180) {
            hatch.set(-0.35);
        } else {
            hatch.set(0);
        }
    }

    public static void dhreverse(){
        dhatch.set(DoubleSolenoid.Value.kReverse);    
    }

    public static boolean rehatch(){
        if(dhatch.get() == DoubleSolenoid.Value.kForward){
            return true;
        }else{
            return false;
        }
    }
    

    public static void dashboard() {
        // if (air.getCompressorShortedFault()) {
        // dashBoard.markError();
        // } else if (air.getCompressorNotConnectedFault()) {
        // dashBoard.markWarning();
        // } else {
        // dashBoard.markReady();
        // }

        SmartDashboard.putBoolean("pneumatic/compPower", !air.getPressureSwitchValue());
        controlCompressor(SmartDashboard.getBoolean("pneumatic/compCloseLoop", false));

        SmartDashboard.putBoolean("panel/hatch", dhatch.get() == Value.kForward);
        SmartDashboard.putNumber("panel/motorOut", hatch.get());

        protectOverride = SmartDashboard.getBoolean("panel/protectOverride", false);
        SmartDashboard.putBoolean("panel/protectOverride", protectOverride);
    }
}
