package collaborative_filtering_using_cf4j;

import java.text.DecimalFormat;

import cf4j.Kernel;
import cf4j.Processor;
import cf4j.utils.PrintableQualityMeasure;

public class Recall {
	public static void main(String []args)
	{
		
		String dbName = "/Users/anisha/Desktop/dataset/ratings.dat";
		double testUsers = 0.20; // 20% of test users
		double testItems = 0.20; // 20% of test items
		double relevant[]= {4.0,5.0};
		double irrelevant[]= {1.0,2.0,3.0};

		Kernel.getInstance().open(dbName, testUsers, testItems, "::");

		String [] similarityMetrics = {"PC", "PC_constrained","cosine","adjusted_cosine","Jaccard","MSD","Spearman","JMSD","CJMSD","singularity","PIP"};
		int [] numberOfNeighbors = {50, 100, 150, 200, 250, 300, 350, 400};

		PrintableQualityMeasure recall = new cf4j.utils.PrintableQualityMeasure("Recall", numberOfNeighbors, similarityMetrics);

		for (String sm : similarityMetrics) {

			// Compute similarity
			if (sm.equals("PC"))
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCorrelation()); 			
			else if (sm.equals("PC_constrained")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCorrelationConstrained());
			else if (sm.equals("cosine")) 
					Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCosine());
			else if (sm.equals("adjusted_cosine")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricAjustedCosine());
			else if (sm.equals("Jaccard")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJaccard());
			else if (sm.equals("MSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricMSD());
			else if (sm.equals("Spearman")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricSpearmanRank());
			else if (sm.equals("JMSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricJMSD());
			else if (sm.equals("CJMSD")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricCJMSD());
			else if (sm.equals("singularity")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricSingularities(relevant,irrelevant));
			else if (sm.equals("PIP")) 
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.similarities.MetricPIP());
		

			// For each value of k
			for (int k : numberOfNeighbors) {

				// Find the neighbors
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.neighbors.Neighbors(k));

				// Compute predictions using DFM
				Processor.getInstance().testUsersProcess(new cf4j.knn.userToUser.aggregationApproaches.DeviationFromMean());

				// Compute MAE
				Processor.getInstance().testUsersProcess(new cf4j.qualityMeasures.Recall(1000000,2.5));

				// Retrieve mae an store it
				recall.putError(k, sm,Double.valueOf(new DecimalFormat("#.#####").format( Kernel.gi().getQualityMeasure("Recall"))));
			}
			
		}
		// Print the results
		recall.print();
	}


}
