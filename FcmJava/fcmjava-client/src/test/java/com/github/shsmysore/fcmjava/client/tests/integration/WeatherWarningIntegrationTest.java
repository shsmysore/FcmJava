// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.client.tests.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.shsmysore.fcmjava.client.FcmClient;
import com.github.shsmysore.fcmjava.client.settings.PropertiesBasedSettings;
import com.github.shsmysore.fcmjava.client.tests.testutils.DateUtils;
import com.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import com.github.shsmysore.fcmjava.model.topics.Topic;
import com.github.shsmysore.fcmjava.requests.topic.TopicUnicastMessage;
import com.github.shsmysore.fcmjava.responses.TopicMessageResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class WeatherWarningIntegrationTest {

    private class GeoLocation {

        private final Double lat;
        private final Double lon;

        public GeoLocation(Double lat, Double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        @JsonProperty("lat")
        public Double getLat() {
            return lat;
        }

        @JsonProperty("lon")
        public Double getLon() {
            return lon;
        }
    }

    private class Station {

        private final String wban;
        private final String name;
        private final String state;
        private final String location;
        private final Integer timeZone;
        private final GeoLocation geoLocation;


        public Station(String wban, String name, String state, String location, Integer timeZone, GeoLocation geoLocation) {
            this.wban = wban;
            this.name = name;
            this.state = state;
            this.location = location;
            this.timeZone = timeZone;
            this.geoLocation = geoLocation;
        }

        @JsonProperty("wban")
        public String getWban() {
            return wban;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("state")
        public String getState() {
            return state;
        }

        @JsonProperty("location")
        public String getLocation() {
            return location;
        }

        @JsonProperty("timeZone")
        public Integer getTimeZone() {
            return timeZone;
        }

        @JsonProperty("geoLocation")
        public GeoLocation getGeoLocation() {
            return geoLocation;
        }
    }


    private class LocalWeatherData {

        private final Station station;
        private final Date dateTime;
        private final Float temperature;
        private final Float windSpeed;
        private final Float stationPressure;
        private final SkyConditionEnum skyCondition;

        public LocalWeatherData(Station station, Date dateTime, Float temperature, Float windSpeed, Float stationPressure, SkyConditionEnum skyCondition) {
            this.station = station;
            this.dateTime = dateTime;
            this.temperature = temperature;
            this.windSpeed = windSpeed;
            this.stationPressure = stationPressure;
            this.skyCondition = skyCondition;
        }

        @JsonProperty("station")
        public Station getStation() {
            return station;
        }

        @JsonProperty("dateTime")
        public Date getDateTime() {
            return dateTime;
        }

        @JsonProperty("temperature")
        public Float getTemperature() {
            return temperature;
        }

        @JsonProperty("windSpeed")
        public Float getWindSpeed() {
            return windSpeed;
        }

        @JsonProperty("stationPressure")
        public Float getStationPressure() {
            return stationPressure;
        }

        @JsonProperty("skyCondition")
        public SkyConditionEnum getSkyCondition() {
            return skyCondition;
        }
    }

    private class SevereHeatWarning {

        private final LocalWeatherData localWeatherData0;
        private final LocalWeatherData localWeatherData1;

        public SevereHeatWarning(LocalWeatherData localWeatherData0, LocalWeatherData localWeatherData1) {
            this.localWeatherData0 = localWeatherData0;
            this.localWeatherData1 = localWeatherData1;
        }

        @JsonProperty("localWeatherData0")
        public LocalWeatherData getLocalWeatherData0() {
            return localWeatherData0;
        }

        @JsonProperty("localWeatherData1")
        public LocalWeatherData getLocalWeatherData1() {
            return localWeatherData1;
        }
    }

    private enum WarningTypeEnum {

        @JsonProperty("severeHeatWarning")
        SevereHeatWarning,

        @JsonProperty("highWindWarning")
        HighWindWarning
    }

    public enum SkyConditionEnum {

        @JsonProperty("clear")
        Clear,

        @JsonProperty("mostlyClear")
        MostlyClear,

        @JsonProperty("partlyCloudy")
        PartlyCloudy,

        @JsonProperty("mostlyCloudy")
        MostlyCloudy,

        @JsonProperty("cloudy")
        Cloudy,

        @JsonProperty("fair")
        Fair

    }

    private class WarningMessage {

        private final WarningTypeEnum type;
        private final SevereHeatWarning data;

        public WarningMessage(WarningTypeEnum type, SevereHeatWarning data) {
            this.type = type;
            this.data = data;
        }

        @JsonProperty("type")
        public WarningTypeEnum getType() {
            return type;
        }

        @JsonProperty("data")
        public SevereHeatWarning getData() {
            return data;
        }
    }

    @Test
    @Ignore("This is an Integration Test using system properties to contact the FCM Server")
    public void SendMessageTest() throws Exception {

        // Create the Client using file-based settings:
        FcmClient client = new FcmClient(PropertiesBasedSettings.createFromDefault());

        // Message Options:
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofHours(1))
                .build();

        // Topic to send to, which is the Warnings Topic:
        Topic topic = new Topic("warnings");

        // The GPS Position of the Station:
        GeoLocation position = new GeoLocation(-33.867487, 151.206990);

        // The Station, which generates a Weather measurement:
        Station station = new Station("14756", "Sydney Weather Station", "New South Wales", "Australia", 10, position);

        // The measurements to send:
        LocalWeatherData measurement0 = new LocalWeatherData(
                station, // Station
                DateUtils.from(LocalDateTime.of(2016, 2, 10, 2, 0), ZoneOffset.ofHours(10)), // Measurement Time
                41.0f, // Temperature
                10.0f, // Wind
                120.0f, // Station Pressure
                SkyConditionEnum.Cloudy); // Sky Condition

        LocalWeatherData measurement1 = new LocalWeatherData(
                station, // Station
                DateUtils.from(LocalDateTime.of(2016, 2, 11, 2, 0), ZoneOffset.ofHours(10)), // Measurement Time
                44.0f, // Temperature
                10.0f, // Wind
                120.0f, // Station Pressure
                SkyConditionEnum.Clear); // Sky Condition


        // The Warning:
        SevereHeatWarning warning = new SevereHeatWarning(measurement0, measurement1);

        // The Warning Message to send:
        WarningMessage message = new WarningMessage(WarningTypeEnum.SevereHeatWarning, warning);

        // Send the Warning Message to FCM:
        TopicMessageResponse response = client.send(new TopicUnicastMessage(options, topic, message));

        // Assert Result is OK:
        Assert.assertNotNull(response);

        Assert.assertNull(response.getErrorCode());
    }
}
