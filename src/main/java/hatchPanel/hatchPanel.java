package hatchPanel;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class hatchPanel {

    public static DoubleSolenoid solSide;
    public static DoubleSolenoid solForward;
    public static DigitalInput limitSwitch;

    protected void hatchInit() {
        solSide = new DoubleSolenoid(0, 1);
        solForward = new DoubleSolenoid(2, 3);
        limitSwitch = new DigitalInput(1);
    }

    public static void openHatch() {
        solSide.set(DoubleSolenoid.Value.kReverse);
    }
    public static void closeHatch() {
        solSide.set(DoubleSolenoid.Value.kForward);
    }
    public static void goHatchForward() {
        solForward.set(DoubleSolenoid.Value.kForward);
    }
    public static void goHatchBackward() {
        solForward.set(DoubleSolenoid.Value.kReverse);
    }
    public static void limitswitch() {
        if (limitSwitch.get() == true);{
            openHatch();
        }
        if (limitSwitch.get() == false); {
            closeHatch();
        }
    }
}
