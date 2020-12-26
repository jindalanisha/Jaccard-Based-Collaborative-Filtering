package collaborative_filtering_using_cf4j;

/**
 *  Implements the following CF similarity metric: Haifeng Liu,Zheng Hu,Ahmad Mian,
 *  Hui Tian, Xuzhen Zhu. A new user similarity model to improve the accuracy 
 *  of collaborative filtering (2014), Knowledge-Based Systems  
 *  Volume 56,Pages 156-166 
 */


import cf4j.Kernel;
import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.UsersSimilarities;

public class NHSM extends UsersSimilarities {
	
	double medianRating=3;  // median rating of rating scale
	

	public double similarity (TestUser activeUser, User targetUser) {	
		
		double pss=0;    // proximity significance singularity
		
		double sdv1=activeUser.getRatingStandardDeviation(); // standard deviation of ratings of user 1
		double sdv2=targetUser.getRatingStandardDeviation(); // standard deviation of ratings of user 2
		
		double mean1=activeUser.getRatingAverage(); // mean of ratings of user 1
		double mean2=targetUser.getRatingAverage(); // mean of ratings of user 2
		
		
		
		int i = 0, j = 0, common = 0;
		while (i < activeUser.getNumberOfRatings() && j < targetUser.getNumberOfRatings()) {
			if (activeUser.getItems()[i] < targetUser.getItems()[j]) {
				i++;
			} else if (activeUser.getItems()[i] > targetUser.getItems()[j]) {
				j++;
			} else {
				double rating1=activeUser.getRatings()[i];
				double rating2=targetUser.getRatings()[j];
				double meanRatingOfItem=Kernel.getInstance().getItemByCode(activeUser.getItems()[i]).getRatingAverage();
				
				// Update pss
				double proximity=1 - (double)1 / ( 1 + Math.exp(-Math.abs(rating1-rating2)) );
				double significance=(double)1 / (1 + Math.exp(-Math.abs(rating1-medianRating)*Math.abs(rating2-medianRating)));
				double singularity=1- (double)1 / (1 + Math.exp(-(Math.abs((rating1+rating2)/2.0)-meanRatingOfItem)));
				pss += proximity*significance*singularity;
				
				common++;
				i++;
				j++;
			}	
		}
		
		double jaccard_= (double)common/(activeUser.getNumberOfRatings() * targetUser.getNumberOfRatings()); // modified jaccard
		double jpss = pss*jaccard_;
		
		// Calculate User Rating Preference
		double temp=-Math.abs(mean1-mean2)*Math.abs(sdv1-sdv2);
		double userRatingPreference=1-(1.0/(1+Math.exp(temp)));
		
		double NHSM = jpss * userRatingPreference;
		
		return NHSM;
		
	}
	
	
}
