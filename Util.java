package DE_CP;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import static java.lang.Math.max;

public class Util {
    public static double maxYValue(ObservableList<XYChart.Data<Double,Double>> data)
    {
        double max = -1e9;
        for(XYChart.Data<Double,Double> i:data) max = max(max,i.getYValue());
        return max;
    }

    public static XYChart.Series<Double, Double> seriesOf(ObservableList<XYChart.Data<Double,Double>> data, String name) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setData(data); series.setName(name);
        return series;
    }

}
