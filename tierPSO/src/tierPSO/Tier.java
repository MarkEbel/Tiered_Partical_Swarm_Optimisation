package tierPSO;

import java.util.ArrayList;

public class Tier {
	private ArrayList<Tier> subTiers;
	public Tier parent;
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
	
	public  ArrayList<Particle> getAllParticles() {
		ArrayList<Particle> tmp = new ArrayList<Particle>();
		tmp.addAll(particles);
		if(subTiers.isEmpty()) {
			return tmp;
		}
		for (Tier t : subTiers) {
			tmp.addAll(t.getAllParticles());
		}
		return tmp;
	}
	
	public int getNumOfTiers() {
		int num = 0;
		if(!subTiers.isEmpty()){
			
			num += subTiers.size();
			for (Tier t : subTiers) {
				t.getNumOfTiers();
			}
		}
		return num;
	}
	
	public ArrayList<Tier> getSubTiers(){
		return subTiers;
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
	
		/*
		 * This updates all the particles, checks whether it is in a tier and updates accordingly
		 * Tier level zero is not a tier but a tier class as for recursive methods it makes it more straightforward
		 * */
	public void updateParticles(double[] gbest) {
		
		if(level == 0) {
			for(Particle particle: particles){ 
				particle.update(gbest);	        	
			}			
		} else {
			double[] tierAverage = new double[App.DIMENSIONS];
			
			for(Particle particle: particles){
				tierAverage = add(tierAverage, particle.getBestPosition());				
			}
			
			tierAverage = divide(tierAverage, particles.size());
			
			for(Particle particle: particles){
				particle.update(gbest, tierAverage);
			}			
		}
		
        
        for(Tier tier: subTiers) {
			tier.updateParticles(gbest);
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
	
	
	// adds new tiers and removes tiers based on critera
	public void updateTier(){
		
		ArrayList<Tier> copyOfSubTiers = (ArrayList<Tier>) subTiers.clone();
		for(Tier tier: copyOfSubTiers) {
			tier.updateTier();
		}
		subTiers = copyOfSubTiers;
		
// 			check if particles are close enough to current subtiers if so add to tier
		ArrayList<Particle> allParticles = (ArrayList<Particle>) particles.clone();
		for(Particle p: allParticles) {
			for(Tier tier: subTiers) {
				if(particles.contains(p)) {
					if(closeEnoughToTier(p, tier)) {
						tier.addParticle(p);
						particles.remove(p);
					}					
				}
			}
			
//			if(particle 1 and 2 and 3 all close enough goes into tier
			ArrayList<Particle> potentialNeighbours = particlesCloseEnough(p);
			if(!potentialNeighbours.isEmpty()) {
				addTier(potentialNeighbours);
			}
		}
		
		if(level != 0) {
//			if ( particle is not near rest of tier) then kick it out to parent
			// reverse of allParticles section
			for(Particle p: allParticles) {
				if(particles.contains(p)) {
					if(!closeEnoughToTier(p, this)) { // might need editing for this!!
						parent.addParticle(p);
						particles.remove(p);
					}					
				}
			}
		}
		// for all subtiers check size if less than 2 particles then lower Tier
		ArrayList<Tier> cloneSubTiers = (ArrayList<Tier>) subTiers.clone();
		// clone to avoid comodification errors
		for(Tier tier: cloneSubTiers) {
			if(tier.getParticles().size() < 2) {
				lowerTier(tier);
			}
			
		}
	}
	
	// returns a list of potential particles that could form a tier
	public ArrayList<Particle> particlesCloseEnough(Particle p){
		ArrayList<Particle> potentialNeighbours = new ArrayList<Particle>();
		for(Particle particle: particles) {
			if(particle.equals(p)) {
				continue;
			}
			if(App.DISTANCE_BETWEEN_PARTICLES_FOR_TIER/(level + 1) >= distanceBetweenPositions(p.getCurrentPosition(), particle.getCurrentPosition())) {
				potentialNeighbours.add(particle);
			}
		}
		
		return potentialNeighbours;			
	}
	
	// if a particle is close enough to a tier to be added it will return true
	public boolean closeEnoughToTier(Particle p,Tier t) {
		int numOfParticlesCloseTo = 0;
		for(Particle particle: t.particles) {
			if(p.equals(particle)) {
				continue;
			}
			if(App.DISTANCE_BETWEEN_PARTICLES_FOR_TIER/t.level >= distanceBetweenPositions(p.getCurrentPosition(), particle.getCurrentPosition())) {
				numOfParticlesCloseTo ++;
			}			
		}
		
		if(numOfParticlesCloseTo >= App.MINIMUN_PARTICLES_THAT_ARE_CLOSE) {
			return true;
		}
		return false;
	}
	

	public static double distanceBetweenPositions(double[] position1, double[] position2) {
		double value = 0;
		for(int i=0; i < position1.length; i++) {
			value += Math.pow(position1[i] - position2[i], 2);
		}
		value = Math.sqrt(value);
		return value;
	}

	public void addTier(ArrayList<Particle> particlesForTier){
		Tier newTier = new Tier(level + 1, this);
		for(Particle particle: particlesForTier) {
			newTier.addParticle(particle);
			particles.remove(particle);
		}
		subTiers.add(newTier);
	}
	// removes tier but keeps subtiers and particles
	public void lowerTier(Tier t){
		// add subtier particles to this tier
		// add subtiers to this tier
		ArrayList<Particle> particlesForTier = t.getParticles();
		for(Particle particle: particlesForTier) {
			particles.add(particle);
		}
		 for(Tier tier: t.subTiers) {
				tier.lowerLevels();
				subTiers.add(tier);
			}
		 subTiers.remove(t);
	}
	// lowers the level of a tier including its subtiers.
	public void lowerLevels() {
		if(level != 0) {
			level --;
			for(Tier tier: subTiers) {
				tier.lowerLevels();
			}
		}
	}
	
	public Particle bestPosition(){
		Particle best = null;
		for(Particle p : particles) {
			if(best == null) {
				best = p;
			} else {
				if(best.getBestCost() < p.getBestCost()) {
					best = p;
				}
			}
		}
		for(Tier t: subTiers) {
			// if subtier best is better than this best return tha
			
			
			if(t.bestPosition() != null) {
				if( best != null) {
					if(t.bestPosition().getBestCost() < best.getBestCost()) {
						best = t.bestPosition();
					}
				} else {
					best = t.bestPosition();
				}
			}				
		}
		return best;
	}
}
