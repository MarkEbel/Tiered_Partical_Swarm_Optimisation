package tierPSO;

import java.util.Arrays;
import java.util.Random;



public class Particle {

	private double[] velocity;
	private double bpCost;
	private double[] currentPosition;
	private double[] bestPosition;

	private static double[] inertia;
	private static double[] tierInertia;
	private static double cognitiveCo;
	private static double socialCo;
	
	public Particle(double[] currentPosition, double[] velocity) {
		this.currentPosition = currentPosition;
		this.velocity = velocity;
		bestPosition = currentPosition;
		bpCost  = getCost(bestPosition);
	}
	
	public Particle(double[] velocity, double[] currentPosition, double[] inertia, double[] tierInertia, double cognitiveCo, double socialCo) {
		Particle.inertia = inertia;
		Particle.cognitiveCo = cognitiveCo;
		Particle.socialCo = socialCo;
		Particle.tierInertia = tierInertia;

		this.currentPosition = currentPosition;
		this.velocity  = velocity;
		bestPosition = currentPosition;
		bpCost  = getCost(bestPosition);
	}
	
	public void update(double[] gbest) {
		double[] tempV = add(multiply(inertia,velocity) , multiply(multiply( getRandom(gbest.length), cognitiveCo), subtract(bestPosition, currentPosition)));
		tempV = add(tempV,  multiply(multiply( getRandom(gbest.length), socialCo), subtract(gbest, currentPosition)));
		double[] newPosition = add(currentPosition, velocity);
		newPosition[newPosition.length - 1] = gbest.length/2.0;
		Arrays.sort(newPosition); 
		if(constraints(newPosition)) {
			currentPosition = newPosition;
			velocity = tempV;
			if(bpCost > getCost(currentPosition)&&  getCost(currentPosition) > 0) {
				bestPosition = currentPosition;
				bpCost = getCost(bestPosition); 
			}
		}
	}
	
	
	public void update(double[] gbest,double[] tierAverage) {
		// use tierAverage to find distance from current position then use this as a ratio for making V
		// particles closer to average need to be pushed harder outwards
		double dis = Tier.distanceBetweenPositions(currentPosition, tierAverage);
		dis = App.DISTANCE_BETWEEN_PARTICLES_FOR_TIER/dis; // ??????
		gbest = multiply(gbest, dis);
		
		double[] tempV = add(multiply(tierInertia,velocity) , multiply(multiply( getRandom(gbest.length), cognitiveCo), subtract(bestPosition, currentPosition)));
		tempV = add(tempV,  multiply(multiply( getRandom(gbest.length), socialCo), subtract(gbest, currentPosition)));
		double[] newPosition = add(currentPosition, velocity);
		newPosition[newPosition.length - 1] = gbest.length/2.0;
		Arrays.sort(newPosition); 
		if(constraints(newPosition)) {
			currentPosition = newPosition;
			velocity = tempV;
			if(bpCost > getCost(currentPosition)&&  getCost(currentPosition) > 0) {
				bestPosition = currentPosition;
				bpCost = getCost(bestPosition); 
			}
		}
	}
	
	public double[] getBestPosition() {
		return bestPosition;
	}
	public double[] getCurrentPosition() {
		return currentPosition;
	}
	public double getBestCost() {
		return bpCost;
	}
//	public boolean constraints(double[] position){
//		int i = 0;
//		
//		for(double[] bound : bounds) {
//			double lb = bound[0];
//			double ub = bound[1];
//			if(position[i] < lb || position[i] > ub ) {					
//				return false;
//			}
//			i++;
//		}			
//		
//	
//		return true;
//	}
	
	public boolean constraints(double[] position){
		double[][] allBounds = App.bounds();
		int i = 0;
		if(allBounds == null){
			return true;
		}
		for(double[] bounds : allBounds) {
			double lb = bounds[0];
			double ub = bounds[1];
			if(position[i] < lb || position[i] > ub ) {					
				return false;
			}
			i++;
		}	
		return true;
	}
	
	
	
	
	private double[] add(double[] input1, double[] input2) {
		double[] d1 = input1.clone();
		double[] d2 = input2.clone();
		for(int i = 0; i < d1.length; i ++) {
			d1[i] = d1[i] + d2[i];
		}
		return d1;
	}
	private double[] subtract(double[] input1, double[] input2) {
		double[] d1 = input1.clone();
		double[] d2 = input2.clone();
		for(int i = 0; i < d1.length; i ++) {
			d1[i] = d1[i] - d2[i];
		}
		return d1;
	}
	public static double[] multiply(double[] input1, double[] input2) {
		double[] d1 = input1.clone();
		double[] d2 = input2.clone();
		for(int i = 0; i < d1.length; i ++) {
			d1[i] = d1[i] * d2[i];
		}
		return d1;
	}
	private double[] multiply(double[] input1, double input2) {
		double[] d1 = input1.clone();
		double d2 = input2;
		for(int i = 0; i < d1.length; i ++) {
			d1[i] = d1[i] * d2;
		}
		return d1;
	}
	
	public static double[] getRandom (int size) {
		double[] temp = new double[size];
		Random random = new Random();
		for(int i = 0; i < size; i++) {
			temp[i] = random.nextDouble();
		}
		return temp;
	}
	
	public static double getCost(double[] position) {
		return App.positionCost(position);
	}
	
}
