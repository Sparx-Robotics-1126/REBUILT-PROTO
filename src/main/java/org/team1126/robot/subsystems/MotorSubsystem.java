package org.team1126.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import org.team1126.lib.tunable.TunableTable;
import org.team1126.lib.tunable.Tunables;
import org.team1126.lib.tunable.Tunables.TunableDouble;
import org.team1126.lib.util.command.GRRSubsystem;

public class MotorSubsystem extends GRRSubsystem {

    private SparkClosedLoopController controller;
    private SparkMax rev;

    private static final TunableTable tunables = Tunables.getNested("motor");

    private static final TunableDouble volts = tunables.value("volts", .1);

    private static final TunableDouble Stall_Current_Limit = tunables.value("stall_current_limit", 40.0);
    private static final TunableDouble Stall_Velocity_Limit = tunables.value("stall_velocity_limit", 50.0);

    private RelativeEncoder encoder;

    private SparkMaxConfig config;

    /**
     * Creates a MotorSubsystem device
     */
    public MotorSubsystem() {
        rev = new SparkMax(5, MotorType.kBrushless);
        controller = rev.getClosedLoopController();

        encoder = rev.getEncoder();

        config = new SparkMaxConfig();
        config.closedLoop.p(1).i(0).d(0).feedbackSensor(FeedbackSensor.kPrimaryEncoder);
        rev.configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    public boolean isStalled() {
        return (
            Math.abs(encoder.getVelocity()) < Stall_Velocity_Limit.get()
            && rev.getOutputCurrent() > Stall_Current_Limit.get()
        );
    }

    public void moveMotor() {
        rev.set(volts.get());
    }

    public Command moveMotorCommand() {
        return commandBuilder()
            .onExecute(() -> moveMotor())
            .onEnd(() -> rev.set(0));
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
        SmartDashboard.putNumber("Current Speed", volts.get());
    }
}
