package costFunctions;

public class SphereFunction implements costMethods{

	@Override
	public double evaluate(double[] position) {
		double tmp = 0.0;
		for(int i =0; i < position.length; i++) {
			tmp += Math.pow(position[i], 2);
		}
		return tmp;
		// TODO Auto-generated method stub
		
	}
// give solutions in 2 dimensions therefore third axis is f(x)
	@Override
	public double[] randomSolution() {
		// TODO Auto-generated method stub
		return null;
	}

}
