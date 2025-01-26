package com.poseablesoftware.timelineparser;

import java.util.List;

public class FeatureCollection {
    public String type;
    public List<Feature> features;

    // Getters and Setters

    public static class Feature {
        public String type;
        public Geometry geometry;
        public Properties properties;

        // Getters and Setters
    }

    public static class Geometry {
        public String type;
        public List<Double> coordinates;

        // Getters and Setters
    }

    public static class Properties {
        public long osm_id;
        public String country;
        public String city;
        public String countrycode;
        public String postcode;
        public String county;
        public String type;
        public String osm_type;
        public String osm_key;
        public String housenumber;
        public String street;
        public String osm_value;
        public String state;

        // Getters and Setters
    }
}