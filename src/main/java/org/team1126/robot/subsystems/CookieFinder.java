package org.team1126.robot.subsystems;

import com.ctre.phoenix6.configs.CANrangeConfigurator;
import com.ctre.phoenix6.hardware.CANrange;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team1126.lib.util.command.GRRSubsystem;

/**
 * Fictious component that we will use to find the cookies. Cookie Monster style cookies.
 */
public class CookieFinder extends GRRSubsystem {

    private CANrange sensor;
    private CANrangeConfigurator configurator;

    /**
     * Constructor used to initializing the sensor and the configurator.
     */
    public CookieFinder() {
        this.sensor = new CANrange(36);
        this.configurator = sensor.getConfigurator();
    }

    /**
     * Accessor for the sensor.
     *
     * @return the sensor
     */
    public CANrange getSensor() {
        return sensor;
    }

    public void setSensor(CANrange sensor) {
        this.sensor = sensor;
    }

    public CANrangeConfigurator getConfigurator() {
        return configurator;
    }

    public void setConfigurator(CANrangeConfigurator configurator) {
        this.configurator = configurator;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        SmartDashboard.putNumber("Cookie distance", sensor.getDistance().getValueAsDouble());
    }
}
