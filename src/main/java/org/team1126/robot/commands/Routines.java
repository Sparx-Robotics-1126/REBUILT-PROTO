package org.team1126.robot.commands;

import static edu.wpi.first.wpilibj2.command.Commands.*;

import edu.wpi.first.wpilibj2.command.Command;
import org.team1126.lib.tunable.TunableTable;
import org.team1126.lib.tunable.Tunables;
import org.team1126.lib.tunable.Tunables.TunableBoolean;
import org.team1126.robot.Robot;
import org.team1126.robot.subsystems.Lights;

/**
 * The Routines class contains command compositions, such as sequences
 * or parallel command groups, that require multiple subsystems.
 */
@SuppressWarnings("unused")
public final class Routines {

    private static final TunableTable tunables = Tunables.getNested("routines");
    private static final TunableBoolean autoDrive = tunables.value("autoDrive", true);
    private final Robot robot;
    private final Lights lights;

    //private final Swerve swerve;
    // private final ReefSelection selection;
    public Routines(Robot robot) {
        this.robot = robot;
        lights = robot.lights;
        //swerve = robot.swerve;
        // selection  = ;//robot.selection;
    }

    public Command lightsSolidRed() {
        return lights.topLeftBottom.setSolidRed().withName("Routines.lightsSolidRed()");
        // return lights.top.setSolidRed();
    }

    public Command shootingLights() {
        return parallel(
            lights.sides.chase(Lights.Color.SHOOTING),
            lights.topLeftBottom.convergeToMiddle(Lights.Color.SHOOTING),
            lights.topRightBottom.convergeToMiddle(Lights.Color.SHOOTING)
        ).withName("Routines.shootingLights()");
    }

    public Command selfDriveLights() {
        return parallel(
            lights.sides.fade(Lights.Color.BLUE, Lights.Color.RED),
            lights.topLeftBottom.knightRider(Lights.Color.BLUE, Lights.Color.RED),
            lights.topRightBottom.knightRider(Lights.Color.BLUE, Lights.Color.RED)
        ).withName("Routines.selfDriveLights()");
    }
    /**
     * Displays the pre-match animation.
     * @param defaultAutoSelected If the default auto is selected.
     */
    // public Command lightsPreMatch(BooleanSupplier defaultAutoSelected) {
    //     return lights
    //         .preMatch(swerve::getPose, swerve::seesAprilTag, defaultAutoSelected::getAsBoolean)
    //         .withName("Routines.lightsPreMatch()");
    // }
}
