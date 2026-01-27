package org.team1126.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.team1126.lib.util.command.GRRSubsystem;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@Logged
public final class ObjectDetection extends GRRSubsystem{
    private PhotonCamera camera;
    List<PhotonTrackedTarget> targets;

    public ObjectDetection(){
        camera = new PhotonCamera("objectDetection");
    }

    @Override
    public void periodic(){
        var latestResult = camera.getAllUnreadResults().get(0);
        targets = latestResult.getTargets();
        SmartDashboard.putNumber("numTargets", targets.size());
        SmartDashboard.putBoolean("hasTarget", latestResult.hasTargets());
    }
}
