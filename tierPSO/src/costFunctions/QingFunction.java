package costFunctions;

import java.util.Arrays;
import java.util.Random;

public class QingFunction implements CostMethods{

	private int dimensions = 2;
	private Random random = new Random();
	@Override
	public double evaluate(double[] position) {
		// TODO Auto-generated method stub
		double fx = 0;
		for(int i = 1; i <= dimensions; i++) {
			fx += Math.pow((Math.pow(position[i], 2) - i),2);
		}
		
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

	@Override
	public double[] min() {
		return null;
	}
}
