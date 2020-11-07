package DE_CP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class FirstOrderDE {
    private SingleVarFunction exactSolution;
    private TwoVarFunction derivative;
    double x0,y0,X;
    int N;

    public FirstOrderDE(TwoVarFunction derivative)
    {
        this.derivative = derivative;
    }
    public void updateIVP(SingleVarFunction exact,double x0,double y0,double X,int N)
    {
        this.exactSolution = exact;
        this.x0 = x0;
        this.y0 = y0;
        this.X = X;
        this.N = N;
    }

    public double derivativeValue(double x,double y)
    {
        return derivative.f(x,y);
    }
    public double exactSolutionValue(double x)
    {
        return exactSolution.f(x);
    }
    public ObservableList<XYChart.Data<Double,Double>> exactData()
    {
        ObservableList<XYChart.Data<Double,Double>> result;
        result= FXCollections.observableArrayList();

        double h = (X-x0)/N;

        double currentX = x0;
        double currentY = y0;
        while(currentX<=X)
        {
            result.add(new XYChart.Data<>(currentX,currentY));
            currentX = currentX + h;
            currentY = exactSolutionValue(currentX);
        }

        return result;
    }
}
