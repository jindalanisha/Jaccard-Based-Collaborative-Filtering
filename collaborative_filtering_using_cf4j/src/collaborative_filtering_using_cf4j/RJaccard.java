package collaborative_filtering_using_cf4j;

/**
 *  Implements the following CF similarity metric: Sujoy Bag, Sri Krishna Kumar,
 *  Manoj Kumar Tiwari (2019).An efficient recommendation generation using
 *  relevant Jaccard similarity,Information Sciences , Volume 483, Pages 53-64
 */

import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.UsersSimilarities;

public class RJaccard extends UsersSimilarities {
	@Override
	public double similarity (TestUser activeUser, User targetUser) {		
		
		double sim;
		
		// Calculate number of items rated by both activeUser and targetUser
		int i = 0, j = 0, common = 0;
		while (i < activeUser.getNumberOfRatings() && j < targetUser.getNumberOfRatings()) {
			if (activeUser.getItems()[i] < targetUser.getItems()[j]) {
				i++;
			} else if (activeUser.getItems()[i] > targetUser.getItems()[j]) {
				j++;
			} else {
				common++;
				i++;
				j++;
			}	
		}
		
		// If there are no common items, similarity is 0
		if (common == 0) return 0;
			
		int x=activeUser.getNumberOfRatings()-common ; // number of items un-corated by activeUser
		int y=targetUser.getNumberOfRatings()-common;  // number of items un-corated by targetUser
		
		
		// Calculate similarity
		sim=  1.0 / (double)( 1 + 1.0/((double)common) + ((double)x)/(1+x) + 1.0/((double)(1+y)) );
		return sim;
	}
	

}
