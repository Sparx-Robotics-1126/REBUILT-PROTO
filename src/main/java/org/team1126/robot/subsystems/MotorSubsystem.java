package org.team1126.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import org.team1126.lib.util.command.GRRSubsystem;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;

public class MotorSubsystem extends GRRSubsystem {
    
    private SparkClosedLoopController controller;
    private SparkMax rev;

    /**
     * Creates a MotorSubsystem device
     */
    public MotorSubsystem(){
        rev = new SparkMax(5, MotorType.kBrushless);
        controller = rev.getClosedLoopController();
    }

    /**
     * Sets the speed of rev
     * @param speed
     */
    public void setSpeed(double speed)
    {
        rev.set(speed);
    }


    @Override
    public void periodic()
    {
        //Supposed to display voltage
    }

}
