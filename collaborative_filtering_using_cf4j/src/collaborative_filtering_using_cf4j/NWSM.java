package collaborative_filtering_using_cf4j;

/**
 *  Implements the following CF similarity metric:Xuefeng Zang, Tianqi Liu, Shuyu Qiao, 
 *  Wenzhu Gao, Jiatong Wang, Xiaoxin Sun, Bangzuo Zhang. A New Weighted Similarity 
 *  Method Based on Neighborhood User Contributions for Collaborative Filtering,
 *  2016 IEEE First International Conference on Data Science in Cyberspace
 * 
 */

import cf4j.Kernel;
import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.UsersSimilarities;


public class NWSM extends UsersSimilarities {


		public double similarity (TestUser activeUser, User targetUser) {		
			
			double sim;
			
			int items_of_activeUser[]=activeUser.getItems(); // consists of items-codes of items rated by the activeUser. 
			int items_of_targetUser[]=targetUser.getItems(); // consists of items-codes of items rated by the targetUser.
			int total_items=Kernel.getInstance().getNumberOfItems(); // total number of items
			
			double influenceWeight;
			
			// Calculate influence weight 
			if(isSubset(items_of_activeUser,items_of_targetUser,items_of_activeUser.length,items_of_targetUser.length))
			{	
				influenceWeight = 1.0 / ( total_items * 1.0 );
			}
			else
			{
				
				int Ib=targetUser.getItems().length;
				
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
				
				influenceWeight = (double)(Ib - common) / total_items;
			}
//			if (Double.isNaN(influenceWeight)) 
//			     System.out.println(influenceWeight+".1");
			
			
            // Get PCC similarity between the users
			double pearson=new cf4j.knn.userToUser.similarities.MetricCorrelation().similarity(activeUser, targetUser);
			sim=influenceWeight * pearson;
			
			// Get Jaccard similarity between the users
			double jaccard=new cf4j.knn.userToUser.similarities.MetricJaccard().similarity(activeUser, targetUser);
//			if (Double.isNaN(jaccard)) 
//			     System.out.println(jaccard+".2");
			sim=sim * jaccard;
		
			
			double mean1=activeUser.getRatingAverage();   // mean of ratings of activeUser
			double mean2=targetUser.getRatingAverage();   // mean of ratings of targetUser
			
			double sdv1=activeUser.getRatingStandardDeviation();  // standard deviation of ratings of activeUser
			double sdv2=targetUser.getRatingStandardDeviation();  // standard deviation of ratings of targetUser
			
			//System.out.println(mean1+"::"+mean2+"::"+sdv1+"::"+sdv2);
			//System.out.println(activeUser.getNumberOfRatings());
			// Calculate user rating preferencedouble
				
			if(Double.isNaN(mean1))
				mean1=0;
			if(Double.isNaN(mean1))
				mean2=0;
			if(Double.isNaN(sdv1))
				sdv1=0;
			if(Double.isNaN(sdv2))
				sdv2=0;
			double temp = -Math.abs(mean1-mean2)*Math.abs(sdv1-sdv2);
			double denom = 1+ Math.exp(temp);
//			if (Double.isNaN(mean1)) 
//			{
//				System.out.println("Heyyyyyy");
//				System.out.println(activeUser.getNumberOfRatings());
//				System.out.println("Yppppp");
//				System.exit(1);
//			}
			    // System.out.println(mean1+".3");
			double userRatingPreference = 1 - ( 1.0 / denom );
			//if (Double.isNaN(userRatingPreference)) 
			    // System.out.println(userRatingPreference+".4");
			sim = sim * userRatingPreference;
			
			return sim;
			
		}

		
		
    // Return true if items_of_targetUser[] is a subset of  items_of_activeUser[] 
	static boolean isSubset(int items_of_activeUser[], int items_of_targetUser[], int m, int n) 
         { 
				    int i = 0; 
				    int j = 0; 
				    for (i = 0; i < n; i++) 
				    { 
				        for (j = 0; j < m; j++) 
				            if(items_of_targetUser[i] == items_of_activeUser[j]) 
				                break; 
				          
				        if (j == m) 
				            return false; 
				    } 
				    return true; 
         } 
}
