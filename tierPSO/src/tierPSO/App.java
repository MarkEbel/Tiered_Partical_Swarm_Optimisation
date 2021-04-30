package tierPSO;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import costFunctions.*;

public class App {

	public  static double DISTANCE_BETWEEN_PARTICLES_FOR_TIER = 100;
	public final static int MINIMUN_PARTICLES_THAT_ARE_CLOSE = 3;

	private static ArrayList<CostMethods> cms = new ArrayList<CostMethods>();
	private static int cost = 0;
	public static int DIMENSIONS = 2;

	public static void main(String[] args) {
		// Set variables used for different runs of the PSO
		int NUM_OF_PARTICLES = 150;
		int ITERATIONS = 1000;
		cms.add(new SphereFunction());
		cms.add(new StyblinskiTangFunction());
		cms.add(new SalomonFunction());
		cms.add(new RidgeFunction());
		cms.add(new GriewankFunction());
		cms.add(new QingFunction());
		cms.add(new XinSheYangFunction());
		
		// Iterates though dimensions
		for(int i =6; i < 11; i++) {
			// Line below scales the particle distance for each dimension
			DISTANCE_BETWEEN_PARTICLES_FOR_TIER = 0.9*Math.pow(1.7, i) - 1.1*i; // works for dimension 6 and upwards
			double[] TIER_INERTIA = new double[i];
			double[] INERTIA = new double[i];
			Arrays.fill(TIER_INERTIA, 0.6);
			Arrays.fill(INERTIA, 0.6);
			
			// Iterates though groups of particles
			for(int j = 100; j < NUM_OF_PARTICLES; j += 50) {
				pso(0, INERTIA, TIER_INERTIA, 0.1, 0.1,"sphereFunction"+ j +"Data" + i, ITERATIONS, j, i);
				pso(1, INERTIA, TIER_INERTIA, 0.1, 0.1,"stFunction" +j +"Data"+ i, ITERATIONS, j, i);
				pso(2, INERTIA, TIER_INERTIA, 0.1, 0.1,"sFunction"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(3, INERTIA, TIER_INERTIA, 0.1, 0.1,"rFunction"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(4, INERTIA, TIER_INERTIA, 0.1, 0.1,"gFunction"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(5, INERTIA, TIER_INERTIA, 0.1, 0.1,"qFunction"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(6, INERTIA, TIER_INERTIA, 0.1, 0.1,"xFunction"+ j +"Data"+ i, ITERATIONS, j, i);							
			}
		}

		DISTANCE_BETWEEN_PARTICLES_FOR_TIER = 0; // This makes the algorithm run as standard PSO as cannot form tiers.
		for(int i =6; i < 10; i++) {
			double[] TIER_INERTIA = new double[i];
			double[] INERTIA = new double[i];
			Arrays.fill(TIER_INERTIA, 0.6);
			Arrays.fill(INERTIA, 0.6);
			for(int j = 100; j < NUM_OF_PARTICLES; j += 50) {
				pso(0, INERTIA, TIER_INERTIA, 0.1, 0.1,"sphereFunctionNormalPSO"+ j +"Data" + i, ITERATIONS, j, i);
				pso(1, INERTIA, TIER_INERTIA, 0.1, 0.1,"stFunctionNormalPSO" +j +"Data"+ i, ITERATIONS, j, i);
				pso(2, INERTIA, TIER_INERTIA, 0.1, 0.1,"sFunctionNormalPSO"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(3, INERTIA, TIER_INERTIA, 0.1, 0.1,"rFunctionNormalPSO"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(4, INERTIA, TIER_INERTIA, 0.1, 0.1,"gFunctionNormalPSO"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(5, INERTIA, TIER_INERTIA, 0.1, 0.1,"qFunctionNormalPSO"+ j +"Data"+ i, ITERATIONS, j, i);
				pso(6, INERTIA, TIER_INERTIA, 0.1, 0.1,"xFunctionNormalPSO"+ j +"Data"+ i, ITERATIONS, j, i);							
			}
		}
		
		
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
		// Initialises particles
		 cms.get(cost).setDimensions(dimensions);
		tierZero.addParticle(
				new Particle(randomVelocity(dimensions), randomSolution(), inertia, tierInertia, cognitiveCo, socialCo));
		for (int i = 0; i < particlesNum - 1; i++) {
			tierZero.addParticle(new Particle(randomSolution(), randomVelocity(dimensions)));
		}

		String data = "";
		// For loop runs for set amount of iterations
		for (int i = 0; i < iterations; i++) {
			double[] gbestNotInTier = new double[dimensions]; // used to find the best particle which is not in a tier
			double gbestCostNotInTier = 0;
			// Finds best global solution
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
			// Generates data for the CSV
			data += Double.toString(standardDeviationFromMinimum(tierZero, cms.get(cost).min()));
			data += "," + Double.toString(standardDeviation(tierZero));
			data += "," + tierZero.bestPosition().getBestCost();
			data += "," + tierZero.getNumOfTiers();
			data += "\n";
			
			// updates particles
			tierZero.updateParticles(gbestNotInTier);
			tierZero.updateTier();

		}
		writeToCSV(data, filename);
		Particle gbest = tierZero.bestPosition();

		outputSolution(gbest.getBestPosition(), gbest.getBestCost());
	}


	// Returns the standard deviation between all particles using euclidean distance.
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

	//Finds the standard deviation of all particles from the given position
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