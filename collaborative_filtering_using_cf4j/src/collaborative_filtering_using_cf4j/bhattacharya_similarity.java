package collaborative_filtering_using_cf4j;

/**
 *  Implements the following CF similarity metric: Bidyut Kr. Patra, Raimo Launonen,
 *  Ville Ollikainen, Sukumar Nandi. A new similarity measure using Bhattacharyya
 *  coefficient for collaborative filtering in sparse data , Knowledge-Based 
 *  Systems 82 (2015) 163–177
 * 
 */

import cf4j.Item;
import cf4j.Kernel;
import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.MetricJaccard;
import cf4j.knn.userToUser.similarities.UsersSimilarities;

public class bhattacharya_similarity extends  UsersSimilarities {
	@Override
	public double similarity(TestUser activeUser, User targetUser) {		
		
		double sim=0.0;
		
		double jaccard=new MetricJaccard().similarity(activeUser,targetUser);  // get Jaccard similarity
		sim=jaccard;
		
		double sdv1=1.5;//activeUser.getRatingStandardDeviation(); // standard deviation of ratings of activeUser
		double sdv2=targetUser.getRatingStandardDeviation(); // standard deviation of ratings of targetUser
		
		double mean1=activeUser.getRatingAverage(); // mean of ratings of activeUser
		double mean2=targetUser.getRatingAverage(); // mean of ratings of targetUser
		
		
		int items_of_activeUser[]=activeUser.getItems();  //  item-codes of items rated by the activeUser
		int items_of_targetUser[]=targetUser.getItems();  //  item-codes of items rated by the taregetUser
		
		
		
		for(int i=0 ; i<items_of_activeUser.length ; i++)
		{
					Item item1=Kernel.getInstance().getItemByCode(items_of_activeUser[i]);
					double ratings1[]=item1.getRatings();                            // rating vector of item1
					int individual1[]=count_individual_ratings(ratings1);
			
					for(int j=0 ; j<items_of_targetUser.length ; j++)
					{
							Item item2=Kernel.getInstance().getItemByCode(items_of_targetUser[j]);	
							double ratings2[]=item2.getRatings();                   // rating vector of item2
							int individual2[]=count_individual_ratings(ratings2);
								
							int p=item1.getUserIndex(activeUser.getUserCode());  // User-index of activeUser in rating-vector of item1
							int q=item2.getUserIndex(targetUser.getUserCode());  // User-index of targetUser in rating-vector of item2
								
				            // Calculate Bhattacharyya measure  
							double bhattacharya_coeff=0;
							for(int h=0;h<11;h++)
							{
								double a=(double)(individual1[h])/(ratings1.length);
								double b=(double)(individual2[h])/(ratings2.length);
								bhattacharya_coeff+=(double)Math.sqrt(a*b);
							}
							// sim += Bhattacharyya measure * local similarity
							sim += bhattacharya_coeff * ( (double)(item1.getRatingAt(p)-mean1)*(item2.getRatingAt(q)-mean2) / (sdv1*sdv2) );
					}
		}
		
		
		return sim;
	}
	
	// method used in calculating  Bhattacharyya measure 
	public int[] count_individual_ratings(double ratings[])
	{
		int result[]= {0,0,0,0,0};
		for(int i=0;i<ratings.length;i++)
		{
			result[(int)ratings[i]-1]++;
		}
		return result;
	}
}
