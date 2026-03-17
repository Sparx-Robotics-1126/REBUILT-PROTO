package org.team1126.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1126.lib.tunable.TunableTable;
import org.team1126.lib.tunable.Tunables;
import org.team1126.lib.tunable.Tunables.TunableDouble;
import org.team1126.lib.util.command.GRRSubsystem;
import org.team1126.lib.util.vendors.PhoenixUtil;

public final class KrakenTest extends GRRSubsystem {

    private static final TunableTable tunables = Tunables.getNested("kraken");
       public final TunableDouble motorVelocity;
    private final TalonFX motor;
   private final VelocityTorqueCurrentFOC motorVelocityControl;

   public KrakenTest() {
        motor = new TalonFX(5,"RoboRio");
     this.motorVelocity = tunables.value("velocity", 0.254);

 configureMotor();

           PhoenixUtil.run(() ->
            ParentDevice.optimizeBusUtilizationForAll(4,    motor)
        );

         motorVelocityControl = new VelocityTorqueCurrentFOC(0.0);
        motorVelocityControl.UpdateFreqHz = 0.0;

           tunables.add("Motor", motor);
   }

 private Command runMotor() {
        return commandBuilder("Intake.runState()")
            .onExecute(() -> {
               
                motorVelocityControl.withVelocity(motorVelocity.get());
                if (Math.abs(motorVelocityControl.Velocity) > 1e-6) {
                    motor.setControl(motorVelocityControl);
                } else {
                    motor.stopMotor();
                }
            })
            .onEnd(() -> {
                motor.stopMotor();
            });
    }


     private void configureMotor() {
        final TalonFXConfiguration config = new TalonFXConfiguration();

        config.CurrentLimits.StatorCurrentLimit = 180.0;
        config.CurrentLimits.SupplyCurrentLimit = 70.0;
        config.CurrentLimits.SupplyCurrentLowerTime = 0.0;

        config.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        config.Slot0.kP = 20.0;
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kG = 0.0;
        config.Slot0.kS = 3.0;
        config.Slot0.kV = 0.0;
        config.Slot0.kA = 0.0;

        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        PhoenixUtil.run(() -> motor.clearStickyFaults());
        PhoenixUtil.run(() -> motor.getConfigurator().apply(config));
    }
}
