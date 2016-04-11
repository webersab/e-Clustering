package de.dkt.eservices.eweka.modules;

import eu.freme.common.exception.BadRequestException;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesianLogisticRegression;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

public class ClassifierFactory {

	/**
	 * Implemented Algorithms
	 * 
	 * BayesianLogisticRegression, J48, LinearRegression, MultilayerPerceptron, NaiveBayes, 
	 * RandomForest, RandomTree, SMO, 
	 */

	/**
	 * NOT Implemented Algorithms
	 * 
	 * ADTree, AODE, AODEsr, BayesNet, CitationKNN, ClassificationViaClustering, 
	 * ComplementNaiveBayes, ConjunctiveRule, DecisionStump, DecisionTable, DMNBtext, FT, GaussianProcesses, 
	 * HNB, HyperPipes, IB1, IBk, Id3, IsotonicRegression, J48graft, JRip, KStar, LADTree, LBR, LeastMedSq, 
	 * LibLINEAR, LMT, Logistic, LogisticBase, M5Base, MDD, MIDD, MILR, MINND, MIOptimalBall, 
	 * MISMO, MISVM, MultipleClassifiersCombiner, NaiveBayesMultinomial, NaiveBayesSimple, 
	 * NBTree, NNge, OneR, PaceRegression, PART, PLSClassifier, PMMLClassifier, PreConstructedLinearModel, Prism, 
	 * RandomizableClassifier, RBFNetwork, REPTree, Ridor, RuleNode, SerializedClassifier, 
	 * SimpleLinearRegression, SimpleLogistic, SingleClassifierEnhancer, SMOreg, SPegasos, UserClassifier, VFI, 
	 * VotedPerceptron, WAODE, Winnow, ZeroR
	 */

	public static Classifier getClassifier(String type){
		if(type.equalsIgnoreCase("naive")){
			return new NaiveBayes();
		}
		else if(type.equalsIgnoreCase("linearregression")){
			return new LinearRegression();
		}
		else if(type.equalsIgnoreCase("J48")){
			return new J48();
		}
		else if(type.equalsIgnoreCase("MultilayerPerceptron")){
			return new MultilayerPerceptron();
		}
		else if(type.equalsIgnoreCase("RandomForest")){
			return new RandomForest();
		}
		else if(type.equalsIgnoreCase("RandomTree")){
			return new RandomTree();
		}
		else if(type.equalsIgnoreCase("BayesianLogisticRegression")){
			return new BayesianLogisticRegression();
		}
		else if(type.equalsIgnoreCase("SMO")){
			return new SMO();
		}
		throw new BadRequestException("Unsupported classifier type");
	}
}
