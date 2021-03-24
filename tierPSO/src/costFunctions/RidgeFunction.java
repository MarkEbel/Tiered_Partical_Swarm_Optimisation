package costFunctions;

import java.util.Arrays;
import java.util.Random;

public class RidgeFunction implements CostMethods{


	private int dimensions = 2;
	private Random random = new Random();
	@Override
	public double evaluate(double[] position) {
		// TODO Auto-generated method stub
		double fx = position[0];
		double tmp = 0;
		for(int i = 2; i < dimensions; i++) {
			tmp += Math.pow(position[i], 2);
		}
		fx += 2*Math.pow(tmp,0.1);
		return fx;
	}

	@Override
	public double[] randomSolution() {
		double[] tmp = new double[dimensions];
		for(int i = 0; i < dimensions; i++) {
			tmp[i] = random.nextDouble() + random.nextInt(5);			
			if(random.nextBoolean()) {
				tmp[i] = -tmp[i];
			}
		}
		return tmp;
	}

	@Override
	public double[][] bounds() {
		// TODO Auto-generated method stub

		double[] ub = new double[dimensions];
		Arrays.fill(ub, 5);
		double[] lb = new double[dimensions];
		Arrays.fill(lb, -5);
		double[][] tmp = new double[dimensions][2];
		for(int i = 0; i < dimensions; i++) {
			tmp[i][0] = ub[i];
			tmp[i][1] = lb[i];
		}
		return tmp;
	}

	@Override
	public void setDimensions(int dimensions2) {
		// TODO Auto-generated method stub
		dimensions = dimensions2;
	}

	@Override
	public double[] min() {

		double[] tmp = new double[dimensions];
		tmp[0] = -5;
		return tmp;
	}
}

