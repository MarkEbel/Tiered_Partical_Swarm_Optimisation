package costFunctions;

import java.util.Random;

public class SphereFunction implements costMethods{


	private Random random = new Random();
	@Override
	public double evaluate(double[] position) {
		double tmp = 0.0;
		for(int i =0; i < position.length; i++) {
			tmp += Math.pow(position[i], 2);
		}
		return tmp;
		
	}
// give solutions in 2 dimensions therefore third axis is f(x)
// within certain bounds
	@Override
	public double[] randomSolution() {
		double[] tmp = new double[2];
		tmp[0] = 0.12*random.nextDouble() + random.nextInt(5);
		tmp[1] = 0.12*random.nextDouble() + random.nextInt(5);

		if(random.nextBoolean()) {
			tmp[0] = -tmp[0];
		}
		if(random.nextBoolean()) {
			tmp[1] = -tmp[1];
		}
 		return tmp;
	}

}
