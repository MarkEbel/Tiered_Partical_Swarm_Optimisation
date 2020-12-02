package tierPSO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class App {
	private final static int MAX_TIERS = 1;
	private final static int NUM_OF_PARTICLES = 50;
	private final static int MAX_PARTICLES_PER_TIER = (int) Math.floor(NUM_OF_PARTICLES/ 5);
	public final static int DIMENSIONS = 3;
	private final static int ITERATIONS = 50;
	private final static int DISTANCE_BETWEEN_PARTICLES_FOR_TIER= 1;
	private final static double TIER_INERTIA = 0.5;
	private final static double GlOBAL_INERTIA = 0.1;
	

	public static final double MINIMUMSPACING = 0.25;
	
	
	
	
	public static void main(String[] args) {
		pso(new double[] {0.6,0.6,0.6}, 0.1, 0.1, new double[][] {{0,1},{0,1},{0,1}});
	}
	
	
	

	
	private static void pso(double[] inertia, double cognitiveCo, double socialCo, double[][] bounds) {
		Tier tierZero = new Tier(0, null);
		AntennaArray aa = new AntennaArray(3, 90);
		tierZero.addParticle(new Particle(aa, randomVelocity(), randomSolution(aa), inertia, cognitiveCo, socialCo, bounds));
	    for(int i = 0; i < NUM_OF_PARTICLES - 1; i++){
	        tierZero.addParticle(new Particle(randomSolution(aa), randomVelocity()));
	    }

	    for(int i = 0; i < ITERATIONS; i++){
	        double[] gbest = new double[DIMENSIONS];
	        double gbestCost = 0;

	        for(Particle particle: tierZero.getParticles()){
	        	
                if(gbestCost == 0){
                    gbest = particle.getBestPosition();
                    gbestCost = Particle.getCost(gbest);
                } else {
                    if(gbestCost < Particle.getCost(particle.getBestPosition())){
                    gbest = particle.getBestPosition();
                    gbestCost = Particle.getCost(gbest);
                    }
                }
	            
	        }

	        tierZero.updateParticles(gbest);
	        tierZero.updateTier();
	    }
	}
		
	
	
//	private static double[] randomLocation(){
//		Random random = new Random();
//		double [] location = {};
//		while(inTier(location) > 0) {
//			location = random;
//		}
//	    return location;
//	    // location is not in a tier.
//	}

	public static double[] randomSolution(AntennaArray antArr) {
		double[][] allBounds = antArr.bounds();
		double[] design = new double[allBounds.length];
		Random random = new Random();
		while(!(antArr.is_valid(design) && minSpacing(design, MINIMUMSPACING))  || (antArr.evaluate(design) < 0)) {
			int i = 0;
			design = new double[allBounds.length];
			for(double[] bounds : allBounds) {
				double lb = bounds[0];
				double ub = bounds[1];
				
				if(i == 0) {	
					design[i] = design.length/2.0;
					
				} else {
					double maxLength  = (design.length - sum(design))/2 - (design.length - i)*MINIMUMSPACING;
					if(MINIMUMSPACING >= lb) {
						if(ub <= maxLength) {
							design[i]  = (MINIMUMSPACING + sum(design)) + (ub - MINIMUMSPACING - sum(design))*(random.nextDouble());						
						} else {
							design[i]  = MINIMUMSPACING + sum(design) + (maxLength -  sum(design) - MINIMUMSPACING)*(random.nextDouble());	
						}
					} else {
						if(ub <= maxLength) {
							design[i]  = lb + sum(design) + (ub - lb -  sum(design) )*(random.nextDouble());						
						} else {
							design[i]  = lb +  sum(design) + (maxLength - lb -  sum(design) )*(random.nextDouble());	
						}						
					}
					
				}
				i++;
			}			
		}
		Arrays.sort(design);
		return design;
	}
	
	private static double sum(double[] values) {
		double total = 0;
		for(double value: values) {
			total += value;
		}
		return total;
	}
	private static boolean minSpacing(double[] design, double minSpace) {
		for(int i = 0; i < design.length; i++) {
			for(int j = 0; j < design.length; j++) {
				if(i == j) {
					break;
				}
				if(minSpace > Math.abs(design[i] - design[j])) {
					return false;
				}
			}
		}
		return true;
	}
	private static double[] randomVelocity() {
		Random random = new Random();
		double[] v = new double[DIMENSIONS];
		for(int i =0; i < DIMENSIONS; i ++) {
			v[i] = random.nextDouble();
		}
		return v;
	}

}	