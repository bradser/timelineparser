package com.poseablesoftware.timelineparser;

import dev.hossain.timeline.model.Position;

public record ParsedPosition(Position position, Double lat, Double lng) {
}
