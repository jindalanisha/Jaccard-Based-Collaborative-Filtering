package collaborative_filtering_using_cf4j;

import java.text.DecimalFormat;

import cf4j.Kernel;
import cf4j.Processor;
import cf4j.TestUser;
import cf4j.User;
import cf4j.utils.PrintableQualityMeasure;

public class JacLMH_analysis {
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
//		System.out.println(new DecimalFormat("#.#####").format(new JacLMH().similarity(x,user2)));
//		
		
		
		//String dbName = "/Users/anisha/Desktop/dataset/ratings.dat";
		String dbName = "/Users/anisha/Desktop/dataset100k/u.data";
		
		double testUsers = 0.20; 
		double testItems = 0.20; 
		

		//Kernel.getInstance().open(dbName, testUsers, testItems,"::");
		Kernel.getInstance().open(dbName, testUsers, testItems,"\t");

		String [] similarityMetrics = {"JacLMH", "PCC","cosine","Jaccard","MSD","JMSD","CJMSD"};
		int [] numberOfNeighbors = {5,10,15,20,25,30,35,40,45,50,55,60};

		PrintableQualityMeasure f1 = new cf4j.utils.PrintableQualityMeasure("F1", numberOfNeighbors, similarityMetrics);

		for (String sm : similarityMetrics) {

			// Compute similarity
			if (sm.equals("JacLMH"))
				Processor.getInstance().testUsersProcess(new JacLMH());
			else if (sm.equals("PCC"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCorrelation()); 			
			else if (sm.equals("cosine")) 
					Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCosine());
			else if (sm.equals("Jaccard")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJaccard());
			else if (sm.equals("MSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricMSD());
			else if (sm.equals("JMSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJMSD());
			else if (sm.equals("CJMSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCJMSD());
	
		

			// For each value of k
			for (int k : numberOfNeighbors) {

				// Find the neighbors
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.neighbors.Neighbors(k));

				// Compute predictions using DFM
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.aggregationApproaches.DeviationFromMean());

				// Compute MAE
				Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.F1(20,5));

				// Retrieve mae an store it
				
				f1.putError(k, sm,Kernel.gi().getQualityMeasure("F1"));
				
			}
			
		}
		// Print the results
		f1.print();
	}


}
