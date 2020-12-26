package collaborative_filtering_using_cf4j;

import cf4j.Kernel;

/**
 *  Implements the following CF similarity metric: Sujoy Bag, Sri Krishna Kumar,
 *  Manoj Kumar Tiwari (2019).An efficient recommendation generation using
 *  relevant Jaccard similarity,Information Sciences , Volume 483, Pages 53-64
 */

import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.UsersSimilarities;

public class RJMSD extends UsersSimilarities {
	
	private double maxDiff;
	
	@Override
	public void beforeRun () {		
		super.beforeRun();
		this.maxDiff = Kernel.gi().getMaxRating() - Kernel.gi().getMinRating();
	}
	
	@Override
	public double similarity (TestUser activeUser, User targetUser) {		
		
		double sim;
		
		double temp = 0d;
		
		// Calculate number of items rated by both activeUser and targetUser
		int i = 0, j = 0, common = 0;
		while (i < activeUser.getNumberOfRatings() && j < targetUser.getNumberOfRatings()) {
				if (activeUser.getItems()[i] < targetUser.getItems()[j]) {
					i++;
				} else if (activeUser.getItems()[i] > targetUser.getItems()[j]) {
					j++;
				} else {
					double diff = (activeUser.getRatings()[i] - targetUser.getRatings()[j]) / this.maxDiff;
					temp += diff * diff;		
					
					common++;
					i++;
					j++;
				}	
			}
				
		// If there are no common items, similarity is 0
		if (common == 0) return 0;
		
		
		 // Get RJaccard similarity between users 
		double RJaccard=new RJaccard().similarity(activeUser,targetUser);  
		 // Calculate MSD similarity between users
		double MSD = 1d - (temp / common);
		 // Calculate similarity	
		sim = RJaccard * MSD;
		
		
		return sim;
	}

}
