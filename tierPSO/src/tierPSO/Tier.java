package tierPSO;

import java.util.ArrayList;

public class Tier {
	private ArrayList<Tier> subTiers;
	private Tier parent;
	private int level;
	private ArrayList<Particle> particles;
	
	
	public Tier(int level, Tier parent) {
		this.parent = parent;
		this.level = level;
		particles = new ArrayList<Particle>();
		subTiers = new ArrayList<Tier>();
	}
	
	public void addTier(Tier child) {
		subTiers.add(child);
	}
	public int getLevel() {
		return level;
	}
	public void removeTier(Tier child) {
		subTiers.remove(child);
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	public ArrayList<Particle> getParticles() {
		return particles;
	}
	
	public void removeParticle(Particle p) {
		particles.remove(p);
	}
	/**
	 * currently only uses particles in current tier to update this needs to be changed!!!!
	 * want outer tiers to influence as then can dissolve tiers
	 */
	
	public void updateParticles() {
		double[] tierAverage = new double[App.DIMENSIONS];
        for(Particle particle: particles){
            tierAverage = add(tierAverage, particle.getBestPosition());
            
        }
        tierAverage = divide(tierAverage, particles.size());
        
        for(Particle particle: particles){
        	particle.update(tierAverage);
        }
        
        for(Tier tier: subTiers) {
			tier.updateParticles();
		}
	}
	

	public void updateParticles(double[] gbest) {
		
		for(Particle particle: particles){ 
        	particle.update(gbest);	        	
        }
		for(Tier tier: subTiers) {
			tier.updateParticles();
		}
	}
	
	private static double[] add(double[] input1, double[] input2){
	    double[] a1 = input1.clone();
	    double[] a2 = input2.clone();
	    double[] output = new double[a1.length];
	    for(int i =0; i< a1.length; i++){
	        output[i] = a1[i] + a2[i]; 
	    }
	    return output;
	}
	private static double[] divide(double[] input1, double input2){
	    double[] a1 = input1.clone();
	    double[] output = new double[a1.length];
	    for(int i =0; i< a1.length; i++){
	        output[i] = a1[i]/input2; 
	    }
	    return output;
	}
	
	
	public void updateTier(){
	    // adds new tiers and removes tiers based on critera
		
		// how to get over problem of minimum particle number being too small to create new tier
		// for loop through particles and then for loop within that to see if any particles are close enough
		if(particle 1 and 2 and 3 all close enough goes into tier)
		if(already tier add new particles)
			if(particles equals max create new tier)
		if(particle distances is too small get rid of particle from tier but only if tier lower tier dissolves/ has enough room for particles)
		if(tier doesnt contain enough particles dissolve tier)
		if(lower tier dissolves lower level of higher tiers)
	}

	public void addTierLevel(){
		add tier to tiers and put particles in
	}
	public void lowerTire(){
		remove particles from tier and move particles to lower tier
		
	}
}
