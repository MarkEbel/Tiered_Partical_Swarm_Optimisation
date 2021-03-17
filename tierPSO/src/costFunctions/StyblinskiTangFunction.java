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
	@Override
	public double[] randomSolution() {
		double[] tmp = new double[2];
		tmp[0] = random.nextDouble() + random.nextInt(5);
		tmp[1] = random.nextDouble() + random.nextInt(5);
	
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

}
