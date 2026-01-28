package org.team1126.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team1126.lib.tunable.Tunables;
import org.team1126.lib.tunable.Tunables.TunableDouble;
import org.team1126.lib.util.command.GRRSubsystem;

public class MotorSubsystem extends GRRSubsystem {

    private SparkClosedLoopController controller;
    private SparkMax rev;
    private TunableDouble currSpeed = Tunables.value("Speed", 0.0);

    /**
     * Creates a MotorSubsystem device
     */
    public MotorSubsystem() {
        rev = new SparkMax(5, MotorType.kBrushless);
        controller = rev.getClosedLoopController();
        currSpeed.set(getSpeed());
    }

    /**
     * Gets the current speed of the motor
     * @return the current speed
     */
    public double getSpeed() {
        return rev.get();
    }

    /**
     * Sets the speed of rev
     * @param speed
     */
    public void setSpeed(double speed) {
        rev.set(speed);
        currSpeed.set(speed);
    }

    /**
     * Gets the voltage sent to the motor
     * @return the voltage
     */
    public double getVoltage() {
        return rev.getBusVoltage();
    }

    @Override
    public void periodic() {
        //Supposed to display voltage
        SmartDashboard.putNumber("Bus Voltage", getVoltage());
        SmartDashboard.putNumber("Current Speed", currSpeed.get());
        if (currSpeed.get() != getSpeed()) {
            setSpeed(getSpeed());
        }
    }
}
