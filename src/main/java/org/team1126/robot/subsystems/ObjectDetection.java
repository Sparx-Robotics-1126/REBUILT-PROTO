package org.team1126.robot.subsystems;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.team1126.lib.util.command.GRRSubsystem;

@Logged
public final class ObjectDetection extends GRRSubsystem {

    private PhotonCamera camera;
    List<PhotonTrackedTarget> targets;

    public ObjectDetection() {
        camera = new PhotonCamera("Fuel");
    }

    @Override
    public void periodic() {
        try {
            var results = camera.getAllUnreadResults();
            var latest = results.get(0);
            targets = latest.getTargets();
            SmartDashboard.putNumber("numTargets", targets.size());
            SmartDashboard.putBoolean("hasTarget", latest.hasTargets());
        } catch (Exception e) {}
    }
}
