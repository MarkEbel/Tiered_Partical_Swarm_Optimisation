package costFunctions;

import java.util.Random;

public class BukinsFunction implements costMethods{

	@Override
	public double evaluate(double[] position) {
		
		return 100*(Math.abs(position[1] - (0.01*Math.pow(position[0],2)))) + 0.01*Math.abs(position[0] + 10);
		
	}


	private Random random = new Random();
	private int dimensions;
	
	@Override
	public double[] randomSolution() {


		double[] tmp = new double[2];
		tmp[0] = -random.nextDouble() - random.nextInt(10) - 5;
		tmp[1] = random.nextDouble() + random.nextInt(3);
	
		
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
		dimensions = dimensions2;
	}

}
