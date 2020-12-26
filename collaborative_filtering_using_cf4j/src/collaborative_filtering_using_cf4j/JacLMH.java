package collaborative_filtering_using_cf4j;

/**
 *  Implements the following CF similarity metric: Soojung Lee. Improving 
 *  Jaccard Index for Measuring Similarity in Collaborative Filtering,
 *  Information Science and Applications 2017 pp 799-806
 *  
 */

import cf4j.TestUser;
import cf4j.User;
import cf4j.knn.userToUser.similarities.UsersSimilarities;
import java.util.ArrayList;

public class JacLMH extends UsersSimilarities {

    // returns the JacLMH similarity considering Lʙᴅ = 2 and Hʙᴅ = 5
	public double similarity (TestUser activeUser, User targetUser) {	
		
			double sim;
			
			ArrayList<Integer> activeUser_low=new ArrayList<Integer>();   // items having rating <= Lʙᴅ
			ArrayList<Integer> activeUser_medium=new ArrayList<Integer>();  //  items having rating > Lʙᴅ and rating < Hʙᴅ
			ArrayList<Integer> activeUser_high=new ArrayList<Integer>();  //  items having rating >= Hʙᴅ
			
			
			ArrayList<Integer> targetUser_low=new ArrayList<Integer>();  // items having rating <= Lʙᴅ
			ArrayList<Integer> targetUser_medium=new ArrayList<Integer>();  //  items having rating > Lʙᴅ and rating < Hʙᴅ
			ArrayList<Integer> targetUser_high=new ArrayList<Integer>();  //  items having rating >= Hʙᴅ
			
			int i = 0;
			// Divide the items of activeUser into three subsets according to Lʙᴅ and Hʙᴅ
			while (i < activeUser.getNumberOfRatings()) 
			{
				if (activeUser.getRatings()[i] <=2) 
					activeUser_low.add(i);
				else if(activeUser.getRatings()[i] >=5 )
					activeUser_high.add(i);
				else activeUser_medium.add(i);
				
				i++;	
			}
			
			int j=0;
			// Divide the items of targetUser into three subsets according to Lʙᴅ and Hʙᴅ
			while(j < targetUser.getNumberOfRatings())
			{
				if(targetUser.getRatings()[j] <=2 )
					targetUser_low.add(j);
				else if(targetUser.getRatings()[j] >=5 )
					targetUser_high.add(j);
				else targetUser_medium.add(j);
				
				j++;
			}
			
			// Compute jaccard similarity of all three subsets
			double jaclow=find_jaccard(activeUser,targetUser,activeUser_low,targetUser_low);
			double jacmedium=find_jaccard(activeUser,targetUser,activeUser_medium,targetUser_medium);
			double jachigh=find_jaccard(activeUser,targetUser,activeUser_high,targetUser_high);
			
			sim=(double)(jaclow + jacmedium + jachigh) / 3.0;
			return sim;
		
	}
	
	// Calculate Jaccard similarity of each subset of items
	double find_jaccard(TestUser activeUser, User targetUser , ArrayList<Integer> v1 , ArrayList<Integer> v2)
	{
		
		double jaccard;
		int i = 0, j = 0, common = 0 , union=0;
		while (i < v1.size() && j < v2.size()) {
			if (activeUser.getItems()[v1.get(i)] < targetUser.getItems()[v2.get(j)]) {
				i++;
			} else if (activeUser.getItems()[v1.get(i)] > targetUser.getItems()[v2.get(j)]) {
				j++;
			} else {
				common++;
				i++;
				j++;
			}	
		}
		
		union = v1.size() + v2.size() - common;
		
		// if subset is empty , similarity is zero
		if(union==0)
			return 0;
		
		// return similarity
		jaccard=(double)common / union;
		return jaccard;
		
	}
	
	
}