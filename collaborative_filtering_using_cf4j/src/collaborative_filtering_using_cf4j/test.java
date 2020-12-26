package collaborative_filtering_using_cf4j;

import java.text.DecimalFormat;

import cf4j.Kernel;
import cf4j.Processor;
import cf4j.utils.PrintableQualityMeasure;

public class test {
	public static void main(String[] args) {
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
//		System.out.println(new DecimalFormat("#.#####").format(new NHSM().similarity(x,user2)));

//		//String dbName = "/Users/anisha/Desktop/dataset/ratings.dat";
//		String dbName = "/Users/anisha/Desktop/dataset100k/u.data";
//		
//		double testUsers = 0.20; 
//		double testItems = 0.20; 
//		
//
//		//Kernel.getInstance().open(dbName, testUsers, testItems,"::");
//		Kernel.getInstance().open(dbName, testUsers, testItems,"\t");
//
//		String [] similarityMetrics = {"CJMSD", "RJaccard","JacRA","JacLMH","CjacMD","BCF","NWSM","NHSM","PCC","Jaccard"};
//		int [] numberOfNeighbors = {20,40,60,80,100};
//
//		PrintableQualityMeasure RMSE = new cf4j.utils.PrintableQualityMeasure("RMSE", numberOfNeighbors, similarityMetrics);
//
//		for (String sm : similarityMetrics) {
//
//			// Compute similarity
//			if (sm.equals("CJMSD"))
//				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCJMSD());
//			else if (sm.equals("RJaccard"))
//				Processor.getInstance().testUsersProcess(new RJaccard()); 			
//			else if (sm.equals("JacRA")) 
//					Processor.getInstance().testUsersProcess(new JacRA());
//			else if (sm.equals("JacLMH")) 
//				Processor.getInstance().testUsersProcess(new JacLMH());
//			else if (sm.equals("CjacMD")) 
//				Processor.getInstance().testUsersProcess(new CjacMD());
//			else if (sm.equals("BCF")) 
//				Processor.getInstance().testUsersProcess(new bhattacharya_similarity());
//			else if (sm.equals("NWSM")) 
//				Processor.getInstance().testUsersProcess(new NWSM());
//			else if (sm.equals("NHSM")) 
//				Processor.getInstance().testUsersProcess(new NHSM());
//			else if (sm.equals("PCC")) 
//				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCorrelation());
//			else if (sm.equals("Jaccard")) 
//				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJaccard());
//	
//		
//
//			// For each value of k
//			for (int k : numberOfNeighbors) {
//
//				// Find the neighbors
//				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.neighbors.Neighbors(k));
//
//				// Compute predictions using DFM
//				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.aggregationApproaches.DeviationFromMean());
//
//				// Compute RMSE
//				Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.RMSE());
//
//				// Retrieve RMSE an store it
//				RMSE.putError(k, sm,Double.valueOf(new DecimalFormat("#.#####").format( Kernel.gi().getQualityMeasure("RMSE"))));
//				
//			}
//		}
//		
//		// Print the results
//		RMSE.print();

		//String dbName = "/Users/anisha/Desktop/dataset/ratings.dat";
		 //String dbName = "/Users/anisha/Desktop/dataset100k/u.data";
		// String dbName = "/Users/anisha/Downloads/anisha/filmtrust/ratings.txt";
		String dbName = "/Users/anisha/Downloads/anisha/yahoo_music/used_by_me/yahoo_music.txt";

		double testUsers = 0.20; // 20% of test users
		double testItems = 0.20; // 20% of test items

		// Kernel.getInstance().open(dbName, testUsers, testItems, "::");
		Kernel.getInstance().open(dbName, testUsers, testItems, "\t");

		//String[] similarityMetrics = { "JMSD", "CJMSD", "RJaccard", "JacRA", "JacLMH", "CjacMD", "NWSM", "NHSM", "PCC","Jaccard" };
		String [] similarityMetrics = {"NWSM"};
		int[] numberOfNeighbors = { 20, 40, 60, 80, 100, 120, 140, 160, 180, 200 };
		double relevant[] = { 4.0, 5.0 };
		double irrelevant[] = { 1.0, 2.0, 3.0 };

		PrintableQualityMeasure RMSE = new cf4j.utils.PrintableQualityMeasure("RMSE", numberOfNeighbors,
				similarityMetrics);

		for (String sm : similarityMetrics) {

			if (sm.equals("JMSD"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJMSD());
			if (sm.equals("CJMSD"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCJMSD());
			else if (sm.equals("RJaccard"))
				Processor.getInstance().testUsersProcess(new RJaccard());
			else if (sm.equals("JacRA"))
				Processor.getInstance().testUsersProcess(new JacRA());
			else if (sm.equals("JacLMH"))
				Processor.getInstance().testUsersProcess(new JacLMH());
			else if (sm.equals("CjacMD"))
				Processor.getInstance().testUsersProcess(new CjacMD());
			else if (sm.equals("BCF")) {
				Processor.getInstance().testUsersProcess(new bhattacharya_similarity());
			} else if (sm.equals("NWSM"))
				Processor.getInstance().testUsersProcess(new NWSM());
			else if (sm.equals("NHSM"))
				Processor.getInstance().testUsersProcess(new NHSM());
			else if (sm.equals("PCC"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCorrelation());
			else if (sm.equals("Jaccard"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJaccard());

			// For each value of k
			for (int k : numberOfNeighbors) {

				// Find the neighbors
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.neighbors.Neighbors(k));

				// Compute predictions using DFM
				Processor.getInstance()
						.testUsersProcess(new cf4j.knn.userToUser.aggregationApproaches.DeviationFromMean());

				// Compute RMSE
				Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.MSD());

				// Retrieve RMSE an store it
				double temp=Kernel.gi().getQualityMeasure("MSD");
				RMSE.putError(k, sm, Math.sqrt(temp));
			}
		}

		// Print the results
		RMSE.print();
	}
}
