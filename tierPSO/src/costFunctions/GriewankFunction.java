package costFunctions;

import java.util.Random;

public class GriewankFunction implements CostMethods{

	private int dimensions = 2;
	private Random random = new Random();
	@Override
	public double evaluate(double[] position) {
		// TODO Auto-generated method stub
		double fx = 1;
		double tmp = 1;
		for(int i = 1; i <= dimensions; i++) {
			fx += Math.pow(position[i], 2);
			tmp *= Math.cos(position[i]/Math.sqrt(i));
		}
		fx -= tmp;
		
		return fx;
	}

	@Override
	public double[] randomSolution() {
		double[] tmp = new double[dimensions];
		for(int i = 0; i < dimensions; i++) {
			tmp[i] = random.nextDouble() + random.nextInt(500);			
			if(random.nextBoolean()) {
				tmp[i] = -tmp[i];
			}
		}
		return tmp;
	}

	@Override
	public double[][] bounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDimensions(int dimensions2) {
		// TODO Auto-generated method stub
		dimensions = dimensions2;
	}

}
