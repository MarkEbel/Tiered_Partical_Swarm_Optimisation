package costFunctions;

public interface CostMethods {
	public double evaluate(double[] position);
	public double[] randomSolution();	
	public double[][] bounds();
	public void setDimensions(int dimensions2);
}
