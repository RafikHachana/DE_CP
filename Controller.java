package DE_CP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import static DE_CP.Util.seriesOf;
import static java.lang.Math.*;

//todo test the whole thing with different values for the interval x0,X
public class Controller implements Initializable {
    @FXML
    private TextField x0Field,y0Field,XField,NField,n0FieldTab2,NFieldTab2;
    @FXML
    public CheckBox showEuler,showExact,showRungeKutta,showImprovedEuler,showEulerTab2,showRungeKuttaTab2,showImprovedEulerTab2;
    @FXML
    private LineChart<Double, Double> solutionGraph,globalErrorGraph,maxLocalError;
    @FXML
    private VBox errorTab1,errorTab2;

    NumericalMethod euler,improvedEuler,rungeKutta;
    FirstOrderDE equation;
    final TwoVarFunction derivative = (x,y)-> (2*sqrt(max(y,0))*cos(x)-2*y)/x;
    final double initx0 = PI,inity0 = 2,initX = 5*PI; final int initN = 100;
    private boolean validateFieldsTab1()
    {
        double x0,y0,X;
        int N;
        try {
            x0 = Double.parseDouble(x0Field.getText()); y0 = Double.parseDouble(y0Field.getText());
            X = Double.parseDouble(XField.getText()); N = Integer.parseInt(NField.getText());
        } catch(Exception e) {return false;}
        return !(x0 > X) && !(y0 < 0) && (!(x0 <= 0) || !(X >= 0)) && N >= 1;
    }
    private boolean validateFieldsTab2()
    {
        int n0,N;
        try { n0 = Integer.parseInt(n0FieldTab2.getText()); N = Integer.parseInt(NFieldTab2.getText()); } catch(Exception e) { return false; }
        return N >= 1 && N > n0 && n0 >= 1;
    }
    private void updateEquation(double x0,double y0,double X,int N)
    {
        double c = x0*sqrt(y0)-sin(x0);
        equation.updateIVP(x -> ((sin(x)*sin(x))+2*c*sin(x)+c*c)/(x*x),x0,y0,X,N);
    }


    public void graphTab1(){
        solutionGraph.getData().clear();
        globalErrorGraph.getData().clear();
        if(!validateFieldsTab1())
        {
            errorTab1.setVisible(true);
            return;
        }
        errorTab1.setVisible(false);
        List<NumericalApproximation> solution = new ArrayList<>();
        //get the new data
        double x0,y0,X;
        int N;
        x0 = Double.parseDouble(x0Field.getText());
        y0 = Double.parseDouble(y0Field.getText());
        X = Double.parseDouble(XField.getText());
        N = Integer.parseInt(NField.getText());
        updateEquation(x0,y0,X,N);
        //approximate
        solution.add(euler.approximate(equation));
        solution.add(improvedEuler.approximate(equation));
        solution.add(rungeKutta.approximate(equation));
        //make the graphs
        if(showExact.isSelected())  solutionGraph.getData().add(seriesOf(equation.exactData(),"Exact Solution"));
        for(NumericalApproximation a:solution)
        {
            if(a.methodUsed.compareTo("Euler Method")==0 && !showEuler.isSelected()) continue;
            if(a.methodUsed.compareTo("Improved Euler Method")==0 && !showImprovedEuler.isSelected()) continue;
            if(a.methodUsed.compareTo("Runge-Kutta Method")==0 && !showRungeKutta.isSelected()) continue;
            solutionGraph.getData().add(seriesOf(a.data(),a.methodUsed));
            globalErrorGraph.getData().add(seriesOf(a.globalError(),a.methodUsed));
        }
        graphTab2();
    }



    public void graphTab2()
    {
        maxLocalError.getData().clear();
        if(!validateFieldsTab2())
        {
            errorTab2.setVisible(true);
            return;
        }
        errorTab2.setVisible(false);
        maxLocalError.setTitle("Max Local Error for x0 = " + x0Field.getText() + ", y0 = "+y0Field.getText() + ", X = " + XField.getText());
        double x0,y0,X;
        x0 = Double.parseDouble(x0Field.getText());  //take the values from the fields
        y0 = Double.parseDouble(y0Field.getText());
        X = Double.parseDouble(XField.getText());
        int n0 = Integer.parseInt(n0FieldTab2.getText());
        int N = Integer.parseInt(NFieldTab2.getText());

        ObservableList<XYChart.Data<Double,Double>> dataEuler = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<Double,Double>> dataImprovedEuler = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<Double,Double>> dataRungeKutta = FXCollections.observableArrayList();
        for(int i = n0; i<=N; i++)
        {
            updateEquation(x0,y0,X,i);
            ObservableList<XYChart.Data<Double,Double>> result;
            result = euler.approximate(equation).localError();
            dataEuler.add(new XYChart.Data<Double,Double>((double)i,Util.maxYValue(result)));
            result = improvedEuler.approximate(equation).localError();
            dataImprovedEuler.add(new XYChart.Data<Double,Double>((double)i,Util.maxYValue(result)));
            result = rungeKutta.approximate(equation).localError();
            dataRungeKutta.add(new XYChart.Data<Double,Double>((double)i,Util.maxYValue(result)));
        }
        if(showEulerTab2.isSelected()) maxLocalError.getData().add(seriesOf(dataEuler,"Euler Method"));
        if(showImprovedEulerTab2.isSelected()) maxLocalError.getData().add(seriesOf(dataImprovedEuler,"Improved Euler Method"));
        if(showRungeKuttaTab2.isSelected()) maxLocalError.getData().add(seriesOf(dataRungeKutta,"Runge-Kutta Method"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        euler = new EulerMethod(); improvedEuler = new ImprovedEulerMethod(); rungeKutta = new RungeKuttaMethod();
        equation = new FirstOrderDE(derivative);
        x0Field.setText(String.valueOf(initx0)); y0Field.setText(String.valueOf(inity0));
        NField.setText(String.valueOf(initN)); XField.setText(String.valueOf(initX));
        n0FieldTab2.setText(String.valueOf(10)); NFieldTab2.setText(String.valueOf(100));
        updateEquation(initx0,inity0,initX,initN);

        graphTab1();
    }
}
