package collaborative_filtering_using_cf4j;

import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.UsersSimilarities;

/**
 *  Implements the following CF similarity metric: Xiaokun Wu1, Yongfeng Huang1, 
 *  Shihui Wang2 (2017).A New Similarity Computation Method in Collaborative 
 *  Filtering based Recommendation System , IEEE 86th Vehicular Technology
 *  Conference (VTC-Fall)
 */

public class JacRA  extends UsersSimilarities {
	@Override
	public double similarity (TestUser activeUser, User targetUser) {
		
		double sim;
		
		double sim1,sim2=0;
		
		int i = 0, j = 0, common = 0, union=0 ;
		
		// Calculate number of items rated by both activeUser and targetUser
		while (i < activeUser.getNumberOfRatings() && j < targetUser.getNumberOfRatings()) {
			if (activeUser.getItems()[i] < targetUser.getItems()[j]) {
				i++;
			} else if (activeUser.getItems()[i] > targetUser.getItems()[j]) {
				j++;
			} else {
				
				// Calculate similarity 2
				double activeRating=activeUser.getRatings()[i];
				double targetRating=targetUser.getRatings()[j];
				sim2 += ((double)Math.min(activeRating,targetRating)) / Math.max(activeRating,targetRating);
				
				common++;
				i++; 
				j++;
				
			}	
		}
		
		union = activeUser.getNumberOfRatings() + targetUser.getNumberOfRatings() - common;
		
		sim1 = (double)common / union;
		sim2 = sim2 / common;
		
		sim=sim1*sim2;
	    	
		return sim;
	}
		

}
