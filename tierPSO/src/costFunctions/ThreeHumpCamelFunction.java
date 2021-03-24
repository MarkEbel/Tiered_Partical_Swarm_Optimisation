package costFunctions;

import java.util.Random;

public class ThreeHumpCamelFunction implements CostMethods{

	@Override
	public double evaluate(double[] position) {
		return 2*Math.pow(position[0],2) - 1.05*Math.pow(position[0], 4) + (Math.pow(position[0], 6)/ 6) + position[0]*position[1] + Math.pow(position[1], 2);
		// TODO Auto-generated method stub
		
	}

	private Random random = new Random();
	private int dimensions = 2;
	
	@Override
	public double[] randomSolution() {double[] tmp = new double[2];
	tmp[0] = random.nextDouble() + random.nextInt(6);
	tmp[1] = random.nextDouble() + random.nextInt(6);

	if(random.nextBoolean()) {
		tmp[0] = -tmp[0];
	}
	if(random.nextBoolean()) {
		tmp[1] = -tmp[1];
	}
		return tmp;
	}

	@Override
	public double[][] bounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDimensions(int dimensions2) {
		// TODO Auto-generated method stub
	}

	@Override
	public double[] min() {
		// TODO Auto-generated method stub
		return null;
	}

}
