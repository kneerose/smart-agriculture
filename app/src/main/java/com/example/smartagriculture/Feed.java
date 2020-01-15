package com.example.smartagriculture;

public class Feed {
    String temperature,humidity;

    public Feed(String temperature, String humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }
}