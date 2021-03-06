package com.chookie.sense.client.mood;

import com.chookie.sense.infrastructure.MessageListener;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static java.time.LocalTime.now;

public class HappinessChartData implements MessageListener<TweetMood> {
    private final XYChart.Series<String, Double> dataSeries = new XYChart.Series<>();
    private final Map<Integer, Integer> minuteToDataPosition = new HashMap<>();

    public HappinessChartData() {
        // TODO: get minute value for right now
        int minute = LocalDateTime.now().getMinute();

        // TODO: create an empty bar for every minute for the next ten minutes
        IntStream.range(minute, minute + 10).forEach(this::initialiseBarToZero);
    }

    @Override
    public void onMessage(TweetMood message) {
        if (message.isHappy()) {
            int x = now().getMinute();

            Integer dataIndex = minuteToDataPosition.get(x);
            Data<String, Double> barForNow = dataSeries.getData().get(dataIndex);
            barForNow.setYValue(barForNow.getYValue() + 1);
        }
    }

    public XYChart.Series<String, Double> getDataSeries() {
        return dataSeries;
    }

    private void initialiseBarToZero(int minute) {
        dataSeries.getData().add(new Data<>(String.valueOf(minute), 0.0));
        minuteToDataPosition.put(minute, dataSeries.getData().size() - 1);
    }

}

