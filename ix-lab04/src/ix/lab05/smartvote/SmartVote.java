package ix.lab05.smartvote;

import ix.lab05.faces.Faces;
import ix.lab05.faces.PCAResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Jama.Matrix;

import utils.CandidatesData;
import utils.Common;
import utils.EigenDecomposition;
import utils.SmartVoteUtils;

public class SmartVote {


    public static void variance(CandidatesData data) {
        Matrix dataMatrix=data.answersMatrix;
    	PCAResult result = Faces.pca(dataMatrix);
    	double[] eigV= result.values;
    	Common.linPlot(eigV);
    	double sum=0.0;
    	for(int i=0;i<eigV.length;i++){
    		sum+=eigV[i];
    	}
    	System.out.print("The variances of top 3 principle components are:");
    	
    		System.out.printf(" %.2f%%",(eigV[0]/sum*100));
    		System.out.printf(" %.2f%%",((eigV[1]+eigV[0])/sum*100));
    		System.out.printf(" %.2f%%",((eigV[1]+eigV[0]+eigV[2])/sum*100));
    	System.out.println("") ;
      
    }


    public static void project(CandidatesData data) {
        /*
         * TODO:
         *
         * 1. Compute the PCA of the candidates' answers matrix.
         * 2. Project the candidates on the two first principal components.
         * 3. Plot the 2D projection using SmartVoteUtils.plotProjection(...).
         */
    	Matrix dataMatrix=Common.center(data.answersMatrix);
    	PCAResult result = Faces.pca(dataMatrix);
    	List<Integer> dims = new ArrayList<Integer>();
    	for(Integer j: SmartVoteUtils.topThree(result.values)){
    		dims.add(j);
    	}
    	Matrix projected = dataMatrix.times(result.rotation);
    	double[] dim1= projected.getMatrix(0,dataMatrix.getRowDimension()-1,dims.get(0),dims.get(0)).getColumnPackedCopy();
    	double[] dim2= projected.getMatrix(0,dataMatrix.getRowDimension()-1,dims.get(1),dims.get(1)).getColumnPackedCopy();
    	double[] dim3= projected.getMatrix(0,dataMatrix.getRowDimension()-1,dims.get(2),dims.get(2)).getColumnPackedCopy();
    	//SmartVoteUtils.plotProjection(dim3,dim2,data.partyAffiliations);
    	SmartVoteUtils.plotProjection(dim1,dim2,data.partyAffiliations);
    
    }


    public static void questions(CandidatesData data, List<String> qs) {
        /*
         * TODO:
         *
         * 1. Compute the PCA of the candidates answers matrix.
         * 2. Extract the three first principal components
         * 3. For each of the three principal component, print the three questions
         *    that contribute most to the component.
         *
         * Hint: use SmartVoteUtils.topThree(..).
         */
    	Matrix dataMatrix=data.answersMatrix;
    	PCAResult result = Faces.pca(dataMatrix);
    	
    	
    	
    	System.out.println("First Component:");
    	Iterable<Integer> question;
    	question = SmartVoteUtils.topThree(result.rotation.transpose().getArray()[0]);
    	
    	Iterator it = question.iterator();
    	System.out.println(qs.get((Integer) it.next()));
    	System.out.println(qs.get((Integer) it.next()));
    	System.out.println(qs.get((Integer) it.next()));
    	
    	System.out.println("Second Component:");
    	
    	question = SmartVoteUtils.topThree(result.rotation.transpose().getArray()[1]);

    	 it = question.iterator();
    	System.out.println(qs.get((Integer) it.next()));
    	System.out.println(qs.get((Integer) it.next()));
    	System.out.println(qs.get((Integer) it.next()));
        
    	System.out.println("Third Component:");
    	
    	question = SmartVoteUtils.topThree(result.rotation.transpose().getArray()[2]);
    
    	 it = question.iterator();
    	System.out.println(qs.get((Integer) it.next()));
    	System.out.println(qs.get((Integer) it.next()));
    	System.out.println(qs.get((Integer) it.next()));
    	
    }


    /** Use this function to run the various parts. */
    public static void main(String[] args) {
        CandidatesData data = SmartVoteUtils.readCandidates();
        System.out.println(String.format("Dataset has N = %d candidates and M = %d questions",
        		data.answersMatrix.getRowDimension(), // TODO number of rows.
                data.answersMatrix.getColumnDimension()  // TODO number of columns.
                ));

        // Prompt the user for an action.
        String action = Common.getString("action [variance/project/questions]: ");

        if ("variance".equals(action)) {
            variance(data);

        } else if ("project".equals(action)) {
            project(data);

        } else if ("questions".equals(action)) {
            List<String> qs = SmartVoteUtils.readQuestions();
            questions(data, qs);
        }
    }
}
