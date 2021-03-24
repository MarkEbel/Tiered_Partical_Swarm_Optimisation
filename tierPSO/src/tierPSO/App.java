package tierPSO;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import costFunctions.*;

public class App {

	public final static double DISTANCE_BETWEEN_PARTICLES_FOR_TIER = 0.2;
	public final static int MINIMUN_PARTICLES_THAT_ARE_CLOSE = 3;
	public final static double[] TIER_INERTIA = new double[] { 0.6, 0.6};
	public final static double[] INERTIA = new double[] { 0.6, 0.6};

	private static ArrayList<CostMethods> cms = new ArrayList<CostMethods>();
	private static int cost = 0;
	public static int DIMENSIONS = 2;

	public static void main(String[] args) {
		int NUM_OF_PARTICLES = 500;
		int ITERATIONS = 100;
		cms.add(new SphereFunction());
		cms.add(new StyblinskiTangFunction());
		cms.add(new SalomonFunction());
		cms.add(new RidgeFunction());
		cms.add(new GriewankFunction());
		cms.add(new QingFunction());
		cms.add(new XinSheYangFunction());
		
		pso(0, INERTIA, TIER_INERTIA, 0.1, 0.1,"sphereFunctionData", 100, 500, DIMENSIONS);
		pso(1, INERTIA, TIER_INERTIA, 0.1, 0.1,"stFunctionData", ITERATIONS, NUM_OF_PARTICLES, DIMENSIONS);
		pso(2, INERTIA, TIER_INERTIA, 0.1, 0.1,"sFunctionData", ITERATIONS, NUM_OF_PARTICLES, DIMENSIONS);
		pso(3, INERTIA, TIER_INERTIA, 0.1, 0.1,"rFunctionData", ITERATIONS, NUM_OF_PARTICLES, DIMENSIONS);
		pso(4, INERTIA, TIER_INERTIA, 0.1, 0.1,"gFunctionData", ITERATIONS, NUM_OF_PARTICLES, DIMENSIONS);
		pso(5, INERTIA, TIER_INERTIA, 0.1, 0.1,"qFunctionData", ITERATIONS, NUM_OF_PARTICLES, DIMENSIONS);
		pso(6, INERTIA, TIER_INERTIA, 0.1, 0.1,"xFunctionData", ITERATIONS, NUM_OF_PARTICLES, DIMENSIONS);
		System.out.println("\n End");
	}

	private static void outputSolution(double[] gbest, double cost) {

		System.out.println("Solution: \n");
		for (double section : gbest) {
			System.out.print(section + "  ");
		}
		System.out.print("\nCost " + cost + "  ");
	}
	

	public static double[][] bounds() {
		return cms.get(cost).bounds();
	}

	public static double positionCost(double[] position) {
		return cms.get(cost).evaluate(position);
	}

	public static double[] randomSolution() {
		return cms.get(cost).randomSolution();
	}

	private static void pso(int cost2,double[] inertia, double[] tierInertia, double cognitiveCo, double socialCo, String filename, int iterations, int particlesNum, int dimensions) {
		System.out.println("\nPSO solution");
		Tier tierZero = new Tier(0, null);
		App.cost = cost2;
		App.DIMENSIONS = dimensions;
		/**
		 * Set dimension for particle
		 */

		 cms.get(cost).setDimensions(dimensions);
		tierZero.addParticle(
				new Particle(randomVelocity(dimensions), randomSolution(), inertia, tierInertia, cognitiveCo, socialCo));
		for (int i = 0; i < particlesNum - 1; i++) {
			tierZero.addParticle(new Particle(randomSolution(), randomVelocity(dimensions)));
		}

		String data = "";
		for (int i = 0; i < iterations; i++) {
			double[] gbestNotInTier = new double[dimensions];
			double gbestCostNotInTier = 0;

			for (Particle particle : tierZero.getParticles()) {

				if (gbestCostNotInTier == 0) {
					gbestNotInTier = particle.getBestPosition();
					gbestCostNotInTier = Particle.getCost(gbestNotInTier);
				} else {
					if (gbestCostNotInTier < Particle.getCost(particle.getBestPosition())) {
						gbestNotInTier = particle.getBestPosition();
						gbestCostNotInTier = Particle.getCost(gbestNotInTier);
					}
				}

			}
			if (gbestCostNotInTier == 0) {
				gbestNotInTier = randomSolution();
			}
			// THIS NEEDS SORTING OUT
			data += Double.toString(standardDeviationFromMinimum(tierZero, cms.get(cost).min()));
			data += "," + Double.toString(standardDeviation(tierZero));
			data += "," + tierZero.bestPosition().getBestCost();
			data += "," + tierZero.getNumOfTiers();
			data += "\n";
		
			tierZero.updateParticles(gbestNotInTier);
			tierZero.updateTier();

		}
		writeToCSV(data, filename);
		Particle gbest = tierZero.bestPosition();

		outputSolution(gbest.getBestPosition(), gbest.getBestCost());
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

	private static double standardDeviation(Tier t) {
		ArrayList<double[]> values = new ArrayList<double[]>();
		ArrayList<Particle> particles = t.getAllParticles();
		
		for (Particle p : particles) {
			values.add(p.getCurrentPosition());
		}
		double[] mean = meanValue(values);
		double diffSquarSum = 0;
		for (double[] value : values) {
			diffSquarSum += euclideanDistanceSquared(value, mean);
		}
		diffSquarSum = diffSquarSum / (values.size() - 1);
		diffSquarSum = Math.sqrt(diffSquarSum);
		return diffSquarSum;
	}

	private static double euclideanDistanceSquared(double[] v, double[] v2) {
		double minusV = 0;
		for (int i = 0; i < v.length; i++) {
			minusV += Math.pow((v[i] - v2[i]), 2);
		}

		return minusV;

	}

	private static double standardDeviationFromMinimum(Tier t, double[] position) {
		ArrayList<double[]> values = new ArrayList<double[]>();
		ArrayList<Particle> particles = t.getAllParticles();
		if(position == null) {
			position = new double[particles.get(0).getCurrentPosition().length];
		}

		for (Particle p : particles) {
			values.add(p.getCurrentPosition());
		}
		double diffSquarSum = 0;
		for (double[] value : values) {
			diffSquarSum += euclideanDistanceSquared(value, position);
		}
		diffSquarSum = diffSquarSum / (values.size() - 1);
		diffSquarSum = Math.sqrt(diffSquarSum);
		return diffSquarSum;
	}

	

	private static void writeToCSV(String data, String fileName) {
		try (PrintWriter writer = new PrintWriter(new File( fileName + ".csv"))) {
			writer.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static double[] meanValue(ArrayList<double[]> values) {
		double[] total = new double[values.get(0).length];
		for (double[] v : values) {
			total = add(v, total);
		}
		total = divide(total, values.size());
		return total;
	}

	private static double[] divide(double[] input1, double input2) {
		double[] a1 = input1.clone();
		double[] output = new double[a1.length];
		for (int i = 0; i < a1.length; i++) {
			output[i] = a1[i] / input2;
		}
		return output;
	}

	private static double[] add(double[] input1, double[] input2) {
		double[] a1 = input1.clone();
		double[] a2 = input2.clone();
		double[] output = new double[a1.length];
		for (int i = 0; i < a1.length; i++) {
			output[i] = a1[i] + a2[i];
		}
		return output;
	}


	private static double[] randomVelocity(int dimensions) {
		Random random = new Random();
		double[] v = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			v[i] = random.nextDouble();
		}
		return v;
	}

}