package org.team1126.robot.subsystems;

import org.team1126.lib.tunable.TunableTable;
import org.team1126.lib.tunable.Tunables;
import org.team1126.lib.util.command.GRRSubsystem;

import com.ctre.phoenix6.hardware.TalonFX;

public final class KrakenTest extends GRRSubsystem {
       private static final TunableTable tunables = Tunables.getNested("kraken");
           private TalonFX motor;
           
}
