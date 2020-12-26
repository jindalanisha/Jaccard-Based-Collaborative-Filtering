package collaborative_filtering_using_cf4j;

import java.text.DecimalFormat;

import cf4j.Kernel;
import cf4j.Processor;
import cf4j.TestUser;
import cf4j.User;
import cf4j.utils.PrintableQualityMeasure;

public class RJaccard_analysis {
	public static void main(String []args)
	{
		
		
//		String dbName = "/Users/anisha/Desktop/myRatings.dat";
//		Kernel.getInstance().open(dbName,"::");
//		
//		User user1=Kernel.getInstance().getUsers()[0];
//		User user2=Kernel.getInstance().getUsers()[1];
//		
//		int items1[]=user1.getItems();
//		double ratings1[]=user1.getRatings();
//		
//		int items2[]=user2.getItems();
//		double ratings2[]=user2.getRatings();
//		
//		for(int i=0;i<items1.length;i++)
//		{
//			System.out.println(items1[i]+":"+ratings1[i]);
//		}
//		System.out.println("----------------------------------------------");
//		for(int i=0;i<items2.length;i++)
//		{
//			System.out.println(items2[i]+":"+ratings2[i]);
//		}
//		int [] testItems= {};
//		double [] testRatings= {};
//		
//		TestUser x=new TestUser (user1.getUserCode(),user1.getUserIndex(),user1.getItems(),user1.getRatings(),0,testItems,testRatings); 
//			
//		System.out.println();
//		System.out.println(new DecimalFormat("#.#####").format(new RJMSD().similarity(x,user2)));
		
		
		
		//String dbName = "/Users/anisha/Desktop/dataset/ratings.dat";
		String dbName = "/Users/anisha/Desktop/dataset100k/u.data";
		
		double testUsers = 0.30; 
		double testItems = 0.30; 
		

		//Kernel.getInstance().open(dbName, testUsers, testItems,"::");
		Kernel.getInstance().open(dbName, testUsers, testItems,"\t");

		String [] similarityMetrics = {"RJaccard", "RJMSD","JMSD","Jaccard","PCC","MSD","cosine"};
		double [] threshold = {3.5,4.0,4.5};

		PrintableQualityMeasure F1 = new cf4j.utils.PrintableQualityMeasure("F1", threshold, similarityMetrics);

		for (String sm : similarityMetrics) {

			// Compute similarity
			if (sm.equals("RJaccard"))
				Processor.getInstance().testUsersProcess(new RJaccard());
			if (sm.equals("RJMSD"))
				Processor.getInstance().testUsersProcess(new RJMSD());
			else if (sm.equals("JMSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJMSD());
			else if (sm.equals("Jaccard")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJaccard());
			else if (sm.equals("PCC"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCorrelation()); 	
			else if (sm.equals("MSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricMSD());
			else if (sm.equals("cosine")) 
					Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCosine());
	
			for (double val : threshold)
			{
				// Find the neighbors
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.neighbors.Neighbors(20));

				// Compute predictions using DFM
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.aggregationApproaches.DeviationFromMean());

				if(val==3.5)
				Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.F1(30,val));

				else if(val==4)
				Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.F1(15,val));
				else Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.F1(5,val));

				// Retrieve F1 an store it
				
				F1.putError(val, sm,Kernel.gi().getQualityMeasure("F1"));
				}
				
			}
			
		
		// Print the results
		F1.print();

	}

}
