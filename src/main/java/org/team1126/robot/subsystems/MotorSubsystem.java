package org.team1126.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import org.team1126.lib.tunable.TunableTable;
import org.team1126.lib.tunable.Tunables;
import org.team1126.lib.util.command.GRRSubsystem;

public class MotorSubsystem extends GRRSubsystem {

    private SparkClosedLoopController controller;
    private SparkMax rev;

    //private static final TunableTable tunables = Tunables.getNested("motor");

    //private static final TunableDouble volts = tunables.value("volts", 0.7380951046943665);

    //private static final TunableDouble Stall_Current_Limit = tunables.value("stall_current_limit", 40.0);
    //private static final TunableDouble Stall_Velocity_Limit = tunables.value("stall_velocity_limit", 50.0);

    private RelativeEncoder encoder;

    private SparkMaxConfig config;

    private final SparkFlex intakeMotor;

    private SparkFlexConfig intakeConfig;
    private final RelativeEncoder intakeEncoder;
    private SparkClosedLoopController intakeController;
    private final Tunables.TunableInteger intakeSpeed = tunables.value("Intake Speed", 50);
    private static final TunableTable tunables = Tunables.getNested("intake");

    /**
     * Creates a MotorSubsystem device
     */
    public MotorSubsystem() {
        rev = new SparkMax(5, MotorType.kBrushless);
        controller = rev.getClosedLoopController();

        encoder = rev.getEncoder();

        config = new SparkMaxConfig();
        config.closedLoop.p(0.4).i(0).d(0).feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        intakeMotor = new SparkFlex(18, SparkLowLevel.MotorType.kBrushless);
        intakeEncoder = intakeMotor.getEncoder();
        intakeConfig = new SparkFlexConfig();

        intakeController = intakeMotor.getClosedLoopController();
        intakeConfig
            .smartCurrentLimit(40)
            .idleMode(SparkBaseConfig.IdleMode.kBrake)
            .inverted(false)
            .openLoopRampRate(0.25)
            .closedLoopRampRate(0.25);

        intakeConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            // Set PID values for position control. We don't need to pass a closed
            // loop slot, as it will default to slot 0.
            .p(0.0)
            .i(0)
            .d(0)
            .outputRange(-1, 1)
            // Set PID values for velocity control in slot 1
            .feedForward
            // kV is now in Volts, so we multiply by the nominal voltage (12V)
            .kV(.05);

        config.closedLoop.maxMotion
            .maxAcceleration(5) // REDUCED from 200 — slower ramp
            .allowedProfileError(5)
            .cruiseVelocity(60) // Changed from 1000 to match test setpoint
            .allowedProfileError(5);

        // config.closedLoop.maxMotion
        //     .maxAcceleration(5)
        //     .allowedProfileError(.1)
        //     .cruiseVelocity(10)
        //     .allowedProfileError(.1);
        intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        //        shooterController.setSetpoint(this.shooterShootSpeed, SparkBase.ControlType.kMAXMotionVelocityControl);
        tunables.add("Intake Motor", intakeMotor);
        rev.configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        tunables.add("Shooter Motor", rev);
    }

    // public boolean isStalled() {
    //     return (
    //         Math.abs(encoder.getVelocity()) < Stall_Velocity_Limit.get()
    //         && rev.getOutputCurrent() > Stall_Current_Limit.get()
    //     );
    // }

    // public void moveMotor() {
    //     this.controller.setSetpoint(volts.get(), SparkBase.ControlType.kVelocity);
    //     // rev.set.set(volts.get());
    // }

    // public Command moveMotorCommand() {
    //     return commandBuilder()
    //         .onExecute(() -> moveMotor())
    //         .onEnd(() -> rev.set(0));
    // }

    /**
     * Gets the voltage sent to the motor
     * @return the voltage
     */
    public double getVoltage() {
        return rev.getBusVoltage();
    }

    /**
     * Gets the amps output by the motor
     * @return the amps
     */
    public double getAmps() {
        return rev.getOutputCurrent();
    }

    /**
     * Gets the ohms by dividing volatage by amps
     * @return the ohms
     */
    public double getOhms() {
        return getVoltage() / getAmps();
    }

    /**
     * Returns the volcity in native units of RPM
     * @return the velocity
     */
    public double getRPM() {
        return encoder.getVelocity();
    }

    @Override
    public void periodic() {
        //Supposed to display voltage
        // SmartDashboard.putNumber("Motor Controller Voltage", getVoltage());
        // SmartDashboard.putNumber("Current Speed", volts.get());
        // SmartDashboard.putNumber("Motor Controller Amps", getAmps());
        // SmartDashboard.putNumber("Motor Controller Ohms", getOhms());
        // SmartDashboard.putNumber("RPM", getRPM());
        // SmartDashboard.putNumber("Pos", encoder.getPosition());
        SmartDashboard.putBoolean("Intake at set point?", intakeController.isAtSetpoint());
        SmartDashboard.putNumber("Velocity", this.intakeEncoder.getVelocity());
    }

    // public void moveMotorPos() {
    //     this.controller.setSetpoint(volts.get(), SparkBase.ControlType.kPosition);
    // }

    // public Command moveMotorPosCommand() {
    //     return commandBuilder()
    //         .onExecute(() -> moveMotorPos())
    //         .onEnd(() -> rev.set(0));
    // }

    // public void moveMotorPosHome() {
    //     this.controller.setSetpoint(0, SparkBase.ControlType.kPosition);
    // }

    // public Command moveMotorPosHomeCommand() {
    //     return commandBuilder()
    //         .onExecute(() -> moveMotorPosHome())
    //         .onEnd(() -> rev.set(0));
    // }

    public Command moveIntakeTest(boolean reverse) {
        return commandBuilder()
            .onExecute(() -> moveIntakeMotor(reverse))
            .onEnd(this::stopIntake);
    }

    public Command moveIntakeMotorCommand(boolean reverse) {
        return commandBuilder()
            .onExecute(() -> moveIntakeMotor(reverse))
            .onEnd(() -> intakeController.setSetpoint(0, SparkBase.ControlType.kMAXMotionVelocityControl));
    }

    public void moveIntakeMotor(boolean reverse) {
        if (reverse) {
            intakeController.setSetpoint(-this.intakeSpeed.get(), SparkBase.ControlType.kMAXMotionVelocityControl);
        } else {
            intakeController.setSetpoint(this.intakeSpeed.get(), SparkBase.ControlType.kMAXMotionVelocityControl);
        }
    }

    public Command spill() {
        //toggle(true);
        return commandBuilder()
            .onExecute(() -> moveIntakeMotor(true))
            .onEnd(this::stopIntake);
    }

    public Command intake() {
        //toggle(true);
        return commandBuilder()
            .onExecute(() -> moveIntakeMotor(false))
            .onEnd(() -> intakeMotor.set(0));
    }

    private void stopIntake() {
        intakeMotor.setVoltage(0);
    }
}
