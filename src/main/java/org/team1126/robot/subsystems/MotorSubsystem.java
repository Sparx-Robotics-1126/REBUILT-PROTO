package org.team1126.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel;
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

    // private final SparkFlex intakeMotor;

    // private SparkFlexConfig intakeConfig;
    // private final RelativeEncoder intakeEncoder;
    // private SparkClosedLoopController intakeController;
    // private final Tunables.TunableInteger intakeSpeed = tunables.value("Intake Speed", 50);
    private final SparkMax pivotMotor;
    private SparkMaxConfig pivotConfig;
    // private final SparkAbsoluteEncoder pivotEncoder;
    private SparkClosedLoopController pivotController;
    private final Tunables.TunableDouble pivotPosition = tunables.value("Pivot Position", -.7);
    private static final TunableTable tunables = Tunables.getNested("intake");

    /**
     * Creates a MotorSubsystem device
     */
    public MotorSubsystem() {
        pivotMotor = new SparkMax(5, SparkLowLevel.MotorType.kBrushless);
        // pivotEncoder = pivotMotor.getAbsoluteEncoder();
        pivotConfig = new SparkMaxConfig();
        pivotController = pivotMotor.getClosedLoopController();
        
        pivotConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .p(.9, ClosedLoopSlot.kSlot0)
            .i(0, ClosedLoopSlot.kSlot0)
            .d(0, ClosedLoopSlot.kSlot0)
            .feedForward.kCos(.8, ClosedLoopSlot.kSlot0);
        pivotConfig.closedLoop.maxMotion
            .maxAcceleration(100, ClosedLoopSlot.kSlot0) // REDUCED from 200 — slower ramp
            .allowedProfileError(1, ClosedLoopSlot.kSlot0)
            .cruiseVelocity(100, ClosedLoopSlot.kSlot0) // Changed from 1000 to match test setpoint
            .allowedProfileError(1, ClosedLoopSlot.kSlot0);

        pivotConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .p(.9, ClosedLoopSlot.kSlot1)
            .i(0, ClosedLoopSlot.kSlot1)
            .d(0, ClosedLoopSlot.kSlot1)
            .feedForward.kCos(.8, ClosedLoopSlot.kSlot1);
        pivotConfig.closedLoop.maxMotion
            .maxAcceleration(100, ClosedLoopSlot.kSlot1) // REDUCED from 200 — slower ramp
            .allowedProfileError(1, ClosedLoopSlot.kSlot1)
            .cruiseVelocity(100, ClosedLoopSlot.kSlot1) // Changed from 1000 to match test setpoint
            .allowedProfileError(1, ClosedLoopSlot.kSlot1);
        //   pivotConfig.closedLoop.maxMotion.maxAcceleration(100).cruiseVelocity(100).allowedProfileError(1)

        pivotMotor.configure(pivotConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        // rev = new SparkMax(5, MotorType.kBrushless);
        // controller = rev.getClosedLoopController();

        // encoder = rev.getEncoder();

        // config = new SparkMaxConfig();
        // config.closedLoop.p(0.4).i(0).d(0).feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        // intakeMotor = new SparkFlex(23, SparkLowLevel.MotorType.kBrushless);
        // intakeEncoder = intakeMotor.getEncoder();
        // intakeConfig = new SparkFlexConfig();

        // intakeController = intakeMotor.getClosedLoopController();
        // intakeConfig
        //     .smartCurrentLimit(40)
        //     .idleMode(SparkBaseConfig.IdleMode.kBrake)
        //     .inverted(false)
        //     .openLoopRampRate(0.25)
        //     .closedLoopRampRate(0.25);

        // intakeConfig.closedLoop
        //     .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        //     // Set PID values for position control. We don't need to pass a closed
        //     // loop slot, as it will default to slot 0.
        //     .p(0.0)
        //     .i(0)
        //     .d(0)
        //     .outputRange(-1, 1)
        //     // Set PID values for velocity control in slot 1
        //     .feedForward
        //     // kV is now in Volts, so we multiply by the nominal voltage (12V)
        //     .kV(.05);

        // config.closedLoop.maxMotion
        //     .maxAcceleration(5) // REDUCED from 200 — slower ramp
        //     .allowedProfileError(5)
        //     .cruiseVelocity(60) // Changed from 1000 to match test setpoint
        //     .allowedProfileError(5);

        // // config.closedLoop.maxMotion
        // //     .maxAcceleration(5)
        // //     .allowedProfileError(.1)
        // //     .cruiseVelocity(10)
        // //     .allowedProfileError(.1);
        // intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        // //        shooterController.setSetpoint(this.shooterShootSpeed, SparkBase.ControlType.kMAXMotionVelocityControl);
        // tunables.add("Intake Motor", intakeMotor);
        // rev.configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
        tunables.add("Pivot Motor", pivotMotor);
        tunables.add("Pivot Controller", pivotController);
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
        // SmartDashboard.putBoolean("Intake at set point?", intakeController.isAtSetpoint());
        // SmartDashboard.putNumber("Velocity", this.intakeEncoder.getVelocity());
        // SmartDashboard.putNumber("Pivot Position", pivotEncoder.getPosition());
        SmartDashboard.putNumber("Pivot Rel Position", pivotMotor.getEncoder().getPosition());

        // SmartDashboard.putNumber("Pivot Velocity", pivotEncoder.getVelocity());
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

    // public Command moveIntakeTest(boolean reverse) {
    //     return commandBuilder()
    //         .onExecute(() -> moveIntakeMotor(reverse))
    //         .onEnd(this::stopIntake);
    // }

    // public Command moveIntakeMotorCommand(boolean reverse) {
    //     return commandBuilder()
    //         .onExecute(() -> moveIntakeMotor(reverse))
    //         .onEnd(() -> intakeController.setSetpoint(0, SparkBase.ControlType.kMAXMotionVelocityControl));
    // }

    // public void moveIntakeMotor(boolean reverse) {
    //     if (reverse) {
    //         intakeController.setSetpoint(-this.intakeSpeed.get(), SparkBase.ControlType.kMAXMotionVelocityControl);
    //     } else {
    //         intakeController.setSetpoint(this.intakeSpeed.get(), SparkBase.ControlType.kMAXMotionVelocityControl);
    //     }
    // }

    // public Command spill() {
    //     //toggle(true);
    //     return commandBuilder()
    //         .onExecute(() -> moveIntakeMotor(true))
    //         .onEnd(this::stopIntake);
    // }

    // public Command intake() {
    //     //toggle(true);
    //     return commandBuilder()
    //         .onExecute(() -> moveIntakeMotor(false))
    //         .onEnd(() -> intakeMotor.set(0));
    // }

    // private void stopIntake() {
    //     intakeMotor.setVoltage(0);
    // }

    public void moveMotorPosOut(double position) {
        this.pivotController.setSetpoint(
            position,
            SparkBase.ControlType.kMAXMotionPositionControl,
            ClosedLoopSlot.kSlot0
        );
    }

    public void moveMotorPosIn(double position) {
        this.pivotController.setSetpoint(
            position,
            SparkBase.ControlType.kMAXMotionPositionControl,
            ClosedLoopSlot.kSlot1
        );
    }

    public Command extendIntakeTest() {
        return commandBuilder().onExecute(() -> this.moveMotorPosOut(pivotPosition.get()));
    }

    public Command extendIntake() {
        return commandBuilder()
            .onExecute(() -> this.moveMotorPosOut(pivotPosition.get()))
            .onEnd(interrupted -> {
                // Stop driving and let it relax wherever it ended up
                pivotMotor.setVoltage(0.0);

                // Put the pivot motor into Coast
                var coastCfg = new SparkFlexConfig().idleMode(SparkBaseConfig.IdleMode.kCoast);

                pivotMotor.configure(coastCfg, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
            });
    }

    public Command retrackIntakeTest() {
        return commandBuilder().onExecute(() -> this.moveMotorPosIn(0));
    }

    public Command retrackIntake() {
        return commandBuilder()
            .onExecute(() -> this.moveMotorPosIn(0))
            .onEnd(interrupted -> {
                // Keep actively holding home when the command ends
                pivotController.setSetpoint(0, SparkBase.ControlType.kMAXMotionPositionControl, ClosedLoopSlot.kSlot1);

                // Use Brake to resist drifting away from home
                var brakeCfg = new SparkFlexConfig().idleMode(SparkBaseConfig.IdleMode.kBrake);

                pivotMotor.configure(brakeCfg, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
            });
    }
}
