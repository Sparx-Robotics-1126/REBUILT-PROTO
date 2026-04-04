package org.team1126.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import org.team1126.lib.tunable.TunableTable;
import org.team1126.lib.tunable.Tunables;
import org.team1126.lib.tunable.Tunables.TunableDouble;
import org.team1126.lib.util.command.GRRSubsystem;
import org.team1126.lib.util.vendors.PhoenixUtil;

public final class KrakenTest extends GRRSubsystem {

    private static final TunableTable tunables = Tunables.getNested("kraken");
       private final TunableDouble motorVelocity;
       private final TunableDouble motorPosition;
    private final TalonFX motor;
   private final VelocityTorqueCurrentFOC motorVelocityControl;

   public KrakenTest() {
        motor = new TalonFX(15); // or the name your CTRE docs use for the roboRIO bus
     this.motorVelocity = tunables.value("velocity", 0.254);
     this.motorPosition = tunables.value("position", .25);

 configureMotor();

           PhoenixUtil.run(() ->
            ParentDevice.optimizeBusUtilizationForAll(15,    motor)
        );

         motorVelocityControl = new VelocityTorqueCurrentFOC(0.0);
        motorVelocityControl.UpdateFreqHz = 0.0;

           tunables.add("Motor", motor);
            tunables.add("Velocity Control", motorVelocityControl);
   }
   @Override
   public void periodic(){
    SmartDashboard.putNumber("Motor Velocity", motorVelocity.get());
    SmartDashboard.putNumber("Current Velocity", motor.get());
    SmartDashboard.putNumber("Motor Position", motor.getPosition().getValueAsDouble() );
   SmartDashboard.putBoolean("At Target", motor.getMotionMagicAtTarget().getValue());
   }

 public Command spinMotor() {
        return commandBuilder("Intake.runState()")
            .onExecute(() -> {
               var maxVelocity = new MotionMagicVelocityVoltage(motorVelocity.get())
               .withAcceleration(100);

               motor.setControl(maxVelocity);

                // motorVelocityControl.withVelocity(motorVelocity.get());
                // if (Math.abs(motorVelocityControl.Velocity) > 1e-6) {
                //     motor.setControl(motorVelocityControl);
                // } else {
                //     motor.stopMotor();
                // }
            })
            .onEnd(() -> {
                motor.stopMotor();
            });
    }

 public Command positionMotor() {
        return commandBuilder("Intake.runState()")
            .onExecute(() -> {
               var maxVelocity = new MotionMagicVoltage(motorPosition.get());

               motor.setControl(maxVelocity);

                // motorVelocityControl.withVelocity(motorVelocity.get());
                // if (Math.abs(motorVelocityControl.Velocity) > 1e-6) {
                //     motor.setControl(motorVelocityControl);
                // } else {
                //     motor.stopMotor();
                // }
            })
            .onEnd(() -> {
                motor.stopMotor();
            });
    }
    public void applyOrchestra(Orchestra orchestra){
        orchestra.addInstrument(motor);
    }
public void zeroMotorPosition() {
    // Phoenix 6 position units are rotations
    PhoenixUtil.run(() -> motor.setPosition(0.0));
}

// Optional: command wrapper so you can bind it to a button
public Command zeroMotorPositionCommand() {
    return commandBuilder("KrakenTest.zeroMotorPosition()")
        .onInitialize(() -> PhoenixUtil.run(() -> motor.setPosition(0.0)));
}
     private void configureMotor() {
        final TalonFXConfiguration config = new TalonFXConfiguration();

        config.CurrentLimits.StatorCurrentLimit = 180.0;
        config.CurrentLimits.SupplyCurrentLimit = 70.0;
        config.CurrentLimits.SupplyCurrentLowerTime = 0.0;

        config.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        config.Slot0.kP = 0.10;
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kG = 0.0;
        config.Slot0.kS = 0.0;
        config.Slot0.kV = 0.120;
        config.Slot0.kA = 0.0;

        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        config.MotionMagic.MotionMagicCruiseVelocity = 50;
        config.MotionMagic.MotionMagicAcceleration = 100;
        config.MotionMagic.MotionMagicJerk = 1000;
        motor.clearStickyFaults();
 motor.getConfigurator().apply(config);
        PhoenixUtil.run(() -> motor.clearStickyFaults());
        PhoenixUtil.run(() -> motor.getConfigurator().apply(config));
    }
}
