package hatchPanel;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class hatchPanel {

    public static DoubleSolenoid solside;
    public static DoubleSolenoid solforward;
    public static DigitalInput limitSwitch;

    protected void hatchInit() {
        solside = new DoubleSolenoid(0, 1);
        solforward = new DoubleSolenoid(2, 3);
        limitSwitch = new DigitalInput(1);
    }

    public static void openHatch() {
        solside.set(DoubleSolenoid.Value.kReverse);
    }
    public static void closeHatch() {
        solside.set(DoubleSolenoid.Value.kForward);
    }
    public static void goHatchForward() {
        solforward.set(DoubleSolenoid.Value.kForward);
    }
    public static void goHatchBackward() {
        solforward.set(DoubleSolenoid.Value.kReverse);
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
