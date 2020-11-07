package DE_CP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.HashMap;

public class NumericalApproximation {
    String methodUsed;
    int N;
    private ObservableList<XYChart.Data<Double,Double>> dataObservableList;
    private ObservableList<XYChart.Data<Double,Double>> globalErrorObservableList;
    private ObservableList<XYChart.Data<Double,Double>> localErrorObservableList;
    private double lastLocalError;


   public NumericalApproximation(int n,String methodUsed)
   {
       this.N = n;
       this.methodUsed = methodUsed;
       dataObservableList = FXCollections.observableArrayList();
       globalErrorObservableList = FXCollections.observableArrayList();
       localErrorObservableList = FXCollections.observableArrayList();
       lastLocalError = 0;
   }

   public void addValue(double x,double y,double yExact)
   {
       dataObservableList.add(new XYChart.Data<>(x,y));
       globalErrorObservableList.add(new XYChart.Data<>(x,Math.abs(y-yExact)));
       localErrorObservableList.add(new XYChart.Data<>(x,y-yExact -lastLocalError));
       lastLocalError = y - yExact;
   }

   public ObservableList<XYChart.Data<Double,Double>> data(){ return dataObservableList; }
   public ObservableList<XYChart.Data<Double,Double>> globalError(){ return globalErrorObservableList; }
   public ObservableList<XYChart.Data<Double,Double>> localError(){ return localErrorObservableList; }
}
