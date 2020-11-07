package DE_CP;

public class ImprovedEulerMethod implements NumericalMethod {
    public NumericalApproximation approximate(FirstOrderDE equation)
    {
        double x0 = equation.x0;
        double y0 = equation.y0;
        double xmax = equation.X;
        int N = equation.N;

        NumericalApproximation result = new NumericalApproximation(N,this.name());

        double h = (xmax-x0)/N;

        double currentX = x0;
        double currentY = y0;

        while(currentX<=xmax)
        {
            result.addValue(currentX,currentY,equation.exactSolutionValue(currentX));

            double k1 = equation.derivativeValue(currentX,currentY);
            double k2 = equation.derivativeValue(currentX+h,currentY+h*k1);

            currentY += (h/2)*(k1+k2);
            currentX = currentX + h;
        }

        return result;
    }
    public String name()
    {
        return "Improved Euler Method";
    }
}
