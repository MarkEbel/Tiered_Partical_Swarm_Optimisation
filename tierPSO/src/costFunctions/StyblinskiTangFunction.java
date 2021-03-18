package costFunctions;

import java.util.Random;

public class StyblinskiTangFunction implements costMethods{

	@Override
	public double evaluate(double[] position) {double tmp = 0.0;
		for(int i =0; i < position.length; i++) {
			tmp += (Math.pow(position[i], 4) - 16*(Math.pow(position[i], 2)) +5*position[i]);
		}
		tmp = 0.5*tmp;
		return tmp;
	}
	

	private Random random = new Random();
	private int dimensions = 0;
	@Override
	public double[] randomSolution() {
		double[] tmp = new double[dimensions];
		for(int i = 0; i < dimensions; i++) {
			tmp[i] = 0.12*random.nextDouble() + random.nextInt(5);			
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
	public void setDimensions(int dimensions2) {
		// TODO Auto-generated method stub
		dimensions = dimensions2;
	}

}
