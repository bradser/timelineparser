package com.poseablesoftware.timelineparser;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Timeline {
    @JsonProperty("semanticSegments")
    private List<SemanticSegment> semanticSegments;

    // Getters and setters

    public static class SemanticSegment {
        @JsonProperty("startTime")
        private String startTime;

        @JsonProperty("endTime")
        private String endTime;

        @JsonProperty("startTimeTimezoneUtcOffsetMinutes")
        private Integer startTimeTimezoneUtcOffsetMinutes;

        @JsonProperty("endTimeTimezoneUtcOffsetMinutes")
        private Integer endTimeTimezoneUtcOffsetMinutes;

        @JsonProperty("timelinePath")
        private List<TimelinePath> timelinePath;

        @JsonProperty("visit")
        private Visit visit;

        // Getters and setters
    }

    public static class TimelinePath {
        @JsonProperty("point")
        private String point;

        @JsonProperty("time")
        private String time;

        // Getters and setters
    }

    public static class Visit {
        @JsonProperty("hierarchyLevel")
        private int hierarchyLevel;

        @JsonProperty("probability")
        private double probability;

        @JsonProperty("topCandidate")
        private TopCandidate topCandidate;

        // Getters and setters
    }

    public static class TopCandidate {
        @JsonProperty("placeId")
        private String placeId;

        @JsonProperty("semanticType")
        private String semanticType;

        @JsonProperty("probability")
        private double probability;

        @JsonProperty("placeLocation")
        private PlaceLocation placeLocation;

        // Getters and setters
    }

    public static class PlaceLocation {
        @JsonProperty("latLng")
        private String latLng;

        // Getters and setters
    }
}