package collaborative_filtering_using_cf4j;

/**
 *  Implements the following CF similarity metric: Suryakant and Tripti Mahara.
 *  A New Similarity Measure Based on Mean Measure of Divergence for 
 *  Collaborative Filtering in Sparse Environment (2016), Procedia Computer Science
 *  Volume 89, 2016, Pages 450-456 
 */


import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.UsersSimilarities;

public class CjacMD  extends UsersSimilarities {

	@Override
	public double similarity(TestUser activeUser, User targetUser) {
		
		double sim=0;
	
		
		int numberOf_activeUser_items = activeUser.getNumberOfRatings();
		int numberOf_targetUser_items = targetUser.getNumberOfRatings();
		
		double ratings1[] = activeUser.getRatings(); // rating vector of activeUser
		double ratings2[] = targetUser.getRatings(); // rating vector of targetUser
		
		double minRating = 1.0; //  Minimum Rating of scale
		double maxRating = 5.0; // get Maximum Rating of scale 
		double r= maxRating - minRating + 1;
		
		double temp=0;
		
		for(double k=minRating ; k<=maxRating ; k+=1 )
		{

			temp = temp+ ( Math.pow(count(ratings1,k)-count(ratings2,k),2) - 1.0/numberOf_activeUser_items - 1.0/numberOf_targetUser_items );
		}
		
		
		// Calculate mean measure of divergence
		double mmd=1.0/(double)(1 + temp/r); 
		 // Get Jaccard similarity between users 
		double jaccard=new cf4j.knn.userToUser.similarities.MetricJaccard().similarity(activeUser, targetUser);
		 // Get Cosine similarity between users 
		double cos=new cf4j.knn.userToUser.similarities.MetricCosine().similarity(activeUser, targetUser);
		
		
		sim=cos+jaccard+mmd;
		
	
		return sim;
		
	}
	
	// Calculates number of times rating r occurs in rating-vector ratings[]
	public int count(double ratings[],double r)
	{
		int result=0;
		for(int i=0;i<ratings.length;i++)
		{
			if(ratings[i]==r)
				result++;
		}
		return result;
	}

}
