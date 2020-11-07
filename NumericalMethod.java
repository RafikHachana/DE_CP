package DE_CP;

public interface NumericalMethod {
    NumericalApproximation approximate(FirstOrderDE equation);
    String name();
}
