package org.team1126.robot;

import static edu.wpi.first.wpilibj.XboxController.Axis.*;
import static edu.wpi.first.wpilibj2.command.Commands.*;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.NotLogged;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.team1126.lib.logging.LoggedRobot;
import org.team1126.lib.logging.Profiler;
import org.team1126.robot.commands.Routines;
import org.team1126.robot.subsystems.Lights;
import org.team1126.robot.subsystems.MotorSubsystem;
import org.team1126.robot.util.ReefSelection;

@Logged
public final class Robot extends LoggedRobot {

    private final CommandScheduler scheduler = CommandScheduler.getInstance();

    public final Lights lights;
    // public final Swerve swerve;
    //
    // public final CookieFinder cookieFinder;

    public final MotorSubsystem motorSub;

    public final ReefSelection selection;

    public final Routines routines;
    // public final Autos autos;

    private final CommandXboxController driver;

    // private final CommandXboxController coDriver;

    public Robot() {
        // PhoenixUtil.disableDaemons();

        // Initialize subsystems
        lights = new Lights();
        // swerve = new Swerve();
        // cookieFinder = new CookieFinder();

        motorSub = new MotorSubsystem();

        // Initialize helpers
        selection = new ReefSelection();

        // Initialize compositions
        routines = new Routines(this);
        // autos = new Autos(this);

        // Initialize controllers
        driver = new CommandXboxController(Constants.DRIVER);
        // coDriver = new CommandXboxController(Constants.CO_DRIVER);

        // Set default commands
        // swerve.setDefaultCommand(swerve.drive(this::driverX, this::driverY, this::driverAngular));

        // Create triggers
        // Trigger allowGoosing = coDriver.a().negate();
        // Trigger changedReference = RobotModeTriggers.teleop().and(swerve::changedReference);
        // Trigger poo = (driver.leftBumper().or(driver.rightBumper()).negate()).and(selection::isL1);

        // Driver bindings
        // driver.axisLessThan(kRightY.value, -0.5).onTrue(selection.incrementLevel());
        // driver.axisGreaterThan(kRightY.value, 0.5).onTrue(selection.decrementLevel());
        // driver.leftTrigger().onTrue(swerve.tareRotation());

        // driver.povLeft().onTrue(swerve.tareRotation());
        // driver.povRight().whileTrue(swerve.apfDrive(selection::isLeft, () -> true, selection::isL4));
        // driver.povLeft().whileTrue(swerve.apfDrive(selection::isLeft, () -> true, selection::isL4));
        // driver.leftStick().whileTrue(swerve.turboSpin(this::driverX, this::driverY, this::driverAngular));

        // changedReference.onTrue(new RumbleCommand(driver, 1.0).withTimeout(0.2));

        // Co-driver bindings
        // coDriver.a().onTrue(none()); // Reserved (No goosing around)

        // coDriver.povUp().onTrue(selection.incrementLevel());
        // coDriver.povDown().onTrue(selection.decrementLevel());

        // Setup lights
        // routines.lightsSolidRed().schedule();

        // RobotModeTriggers.autonomous().whileTrue(lights.sides.flames(false));

        lights.top.setDefaultCommand(lights.top.setSolidRed());
        lights.sides.setDefaultCommand(lights.sides.setSolidRed());
        //  lights.sides.setDefaultCommand(lights.sides.levelSelection(selection.isL4()));

        // Disable loop overrun warnings from the command
        // scheduler, since we already log loop timings
        // DisableWatchdog.in(scheduler, "m_watchdog");

        // Configure the brownout threshold to match RIO 1
        RobotController.setBrownoutVoltage(6.3);

        driver.a().whileTrue(motorSub.moveMotorCommand());
        driver.povUp().whileTrue(lights.sides.shooting());
        driver.povDown().whileTrue(lights.sides.chase(Lights.Color.SHOOTING));
        driver.povLeft().whileTrue(lights.sides.convergeToMiddle(Lights.Color.SHOOTING));
        driver.povRight().whileTrue(lights.sides.gradientChase(Lights.Color.RED));
        driver.y().whileTrue(lights.sides.knightRider(Lights.Color.SHOOTING, Lights.Color.PURPLE));
        driver
            .x()
            .whileTrue(
                lights.sides.colorCyclingChase(
                    Lights.Color.RED,
                    Lights.Color.ORANGE,
                    Lights.Color.SHOOTING,
                    Lights.Color.LIME_GREEN,
                    Lights.Color.CYAN,
                    Lights.Color.BLUE,
                    Lights.Color.PURPLE
                )
            );
        // Enable real-time thread priority
        enableRT(true);
    }

    /**
     * Returns the current match time in seconds.
     */
    public double matchTime() {
        return Math.max(0.0, DriverStation.getMatchTime());
    }

    @NotLogged
    public double driverX() {
        return driver.getLeftX();
    }

    @NotLogged
    public double driverY() {
        return driver.getLeftY();
    }

    @NotLogged
    public double driverAngular() {
        return -driver.getRightX();
        // return driver.getLeftTriggerAxis() - driver.getRightTriggerAxis();
    }

    @Override
    public void robotPeriodic() {
        Profiler.run("scheduler", scheduler::run);
        SmartDashboard.putBoolean("Motor_Stalled", motorSub.isStalled());
        Profiler.run("lights", lights::update);
    }
}
