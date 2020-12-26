package collaborative_filtering_using_cf4j;

import java.text.DecimalFormat;

import cf4j.Kernel;
import cf4j.Processor;

import cf4j.utils.PrintableQualityMeasure;

public class Jaccard_based_measures_analysis {
	public static void main(String []args)
	{
		
//		String dbName = "/Users/anisha/Desktop/dataset/ratings.dat";
//		Kernel.getInstance().open(dbName,"::");
//		User user1=Kernel.getInstance().getUsers()[2];
//		User user2=Kernel.getInstance().getUsers()[105];
//		int [] testItem= {};
//		double [] testRatings= {};
//		
//		TestUser x=new TestUser (user1.getUserCode(),user1.getUserIndex(),user1.getItems(),user1.getRatings(),0,testItem,testRatings); 
//		System.out.println(new DecimalFormat("#.#####").format(new bhattacharya_similarity().similarity(x,user2)));
		
		String dbName = "/Users/anisha/Desktop/dataset/ratings.dat";
		double testUsers = 0.20; // 20% of test users
		double testItems = 0.20; // 20% of test items

		Kernel.getInstance().open(dbName, testUsers, testItems, "::");

		String [] similarityMetrics = {"PC","cosine","Jaccard","JMSD","RJaccard","Bhattacharya","NWSM","CjacMD"};
		int [] numberOfNeighbors = {50, 100, 150, 200, 250, 300, 350, 400};
		

		PrintableQualityMeasure mae = new cf4j.utils.PrintableQualityMeasure("MAE", numberOfNeighbors, similarityMetrics);

		for (String sm : similarityMetrics) {

			// Compute similarity
			if (sm.equals("PC"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCorrelation()); 			
			else if (sm.equals("cosine")) 
					Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCosine());
			else if (sm.equals("Jaccard")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJaccard());
			else if (sm.equals("JMSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJMSD());
			else if (sm.equals("RJaccard")) 
				Processor.getInstance().testUsersProcess(new RJaccard());
			else if (sm.equals("Bhattacharya")) 
				Processor.getInstance().testUsersProcess(new bhattacharya_similarity());
			else if (sm.equals("NWSM")) 
				Processor.getInstance().testUsersProcess(new NWSM());
			else if (sm.equals("CjacMD")) 
				Processor.getInstance().testUsersProcess(new CjacMD());
				
			

			// For each value of k
			for (int k : numberOfNeighbors) {

				// Find the neighbors
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.neighbors.Neighbors(k));

				// Compute predictions using DFM
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.aggregationApproaches.DeviationFromMean());

				// Compute MAE
				Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.MAE());

				// Retrieve mae an store it
				mae.putError(k, sm,Double.valueOf(new DecimalFormat("#.#####").format( Kernel.gi().getQualityMeasure("MAE"))));
			}
		}

		// Print the results
		mae.print();
		
	}

}
