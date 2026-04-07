package org.team1126.robot;

import static edu.wpi.first.wpilibj.XboxController.Axis.*;
import static edu.wpi.first.wpilibj2.command.Commands.*;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.NotLogged;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import org.team1126.lib.logging.LoggedRobot;
import org.team1126.lib.logging.Profiler;
import org.team1126.robot.commands.Routines;
import org.team1126.robot.subsystems.KrakenTest;
import org.team1126.robot.subsystems.Lights;
import org.team1126.robot.subsystems.MotorSubsystem;
import org.team1126.robot.util.ReefSelection;

import com.ctre.phoenix6.Orchestra;

@Logged
public final class Robot extends LoggedRobot {

    private final CommandScheduler scheduler = CommandScheduler.getInstance();

    public final Lights lights;
    private final Orchestra orchestra;
    // public final Swerve swerve;
    //
    // public final CookieFinder cookieFinder;

    // public final MotorSubsystem motorSub;

    // public final ReefSelection selection;
// public final KrakenTest krakenTest;
    public final Routines routines;
    // public final Autos autos;

    private final CommandXboxController driver;

    // private final Orchestra orchestra;

    // private TalonFX motor;

    // private final CommandXboxController coDriver;

    public Robot() {
        // this.motor = new TalonFX(15);
        // this.orchestra = new Orchestra();
        // this.orchestra.addInstrument(this.motor);

        // PhoenixUtil.disableDaemons();

        // Initialize subsystems
          orchestra = new Orchestra();
        lights = new Lights();
        // krakenTest = new KrakenTest();
        // krakenTest.applyOrchestra(orchestra);
        // swerve = new Swerve();
        // cookieFinder = new CookieFinder();

        // motorSub = new MotorSubsystem();

        // Initialize helpers
        // selection = new ReefSelection();

        // Initialize compositions
        routines = new Routines(this);
        // autos = new Autos(this);

        // Initialize controllers
        driver = new CommandXboxController(Constants.DRIVER);
        // driver.a().whileTrue(krakenTest.positionMotor());
        // driver.y().whileTrue(krakenTest.zeroMotorPositionCommand());
        driver.b().onTrue(playMusic("holydriver").ignoringDisable(true));
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

        RobotModeTriggers.autonomous().whileTrue(routines.selfDriveLights());
        lights.topLeftBottom.setDefaultCommand(lights.topLeftBottom.setSolidRed());
        lights.topRightBottom.setDefaultCommand(lights.topRightBottom.setSolidRed());
        lights.sides.setDefaultCommand(lights.sides.setSolidRed());
        //  lights.sides.setDefaultCommand(lights.sides.levelSelection(selection.isL4()));

        // Disable loop overrun warnings from the command
        // scheduler, since we already log loop timings
        // DisableWatchdog.in(scheduler, "m_watchdog");

        // Configure the brownout threshold to match RIO 1
        RobotController.setBrownoutVoltage(6.3);

        // driver.x().whileTrue(motorSub.moveIntakeTest(false));
        // driver.b().whileTrue(motorSub.moveIntakeTest(true));

        // driver.y().whileTrue(motorSub.extendIntakeTest());
        // driver.a().whileTrue(motorSub.retrackIntakeTest());

        driver.povUp().whileTrue(lights.sides.lightningMcQueenChase());
        //driver.povDown().whileTrue(lights.rainbow());
    driver.povLeft().whileTrue(parallel(
        lights.topLeftBottom.convergeToMiddle(Lights.Color.SHOOTING),
        lights.topRightBottom.convergeToMiddle(Lights.Color.SHOOTING)));
        driver.povRight().whileTrue(lights.sides
            .gradientChase(Lights.Color.RED));
    driver.y().whileTrue(parallel(
        lights.topLeftBottom.knightRider(Lights.Color.BLUE, Lights.Color.RED),
        lights.topRightBottom.knightRider(Lights.Color.BLUE, Lights.Color.RED),
        lights.sides.fade(Lights.Color.RED,Lights.Color.BLUE)));
        
        driver.rightTrigger().whileTrue(routines.shootingLights());
        // driver.b().whileTrue(routines.selfDriveLights());
        // replaced colorCyclingChase binding with alliance fade on X button
        // Original: driver.x().whileTrue(lights.sides.colorCyclingChase(...));
    driver.x().whileTrue(parallel(
        lights.sides.fadeAllianceSlow(),
        lights.topLeftBottom.fadeAllianceSlow(),
        lights.topRightBottom.fadeAllianceSlow()));

        // Pair bumpers: left = moving intake (rev=false), right = moving intake (rev=true)
    driver.leftBumper().whileTrue(parallel(
        lights.sides.movingIntake(false),
        lights.topLeftBottom.movingIntake(false),
        lights.topRightBottom.movingIntake(false)));
    driver.rightBumper().whileTrue(parallel(
        lights.sides.movingIntake(true),
        lights.topLeftBottom.movingIntake(true),
        lights.topRightBottom.movingIntake(true)));
        // Enable real-time thread priority
        //enableRT(true);
        //driver.x().whileTrue(Lights.sides.LightningMcQueenChase())
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
        //SmartDashboard.putBoolean("Motor_Stalled", motorSub.isStalled());
        Profiler.run("lights", lights::update);
    }


    public Command playMusic(String song) {
        return runEnd(
            () -> {
                if (!orchestra.isPlaying()) {
                    sing(song);
                }
            },
            orchestra::stop
        )
            .until(DriverStation::isEnabled)
            .ignoringDisable(true);
        // orchestra.loadMusic(song);
        // return run(() -> orchestra.play()).withName("Swerve.playMusic(" + song + ")");
    }

    public void sing(String song) {
        orchestra.loadMusic(song + ".chrp");
        System.out.println("Playing " + song);
        orchestra.play();
    }
}
