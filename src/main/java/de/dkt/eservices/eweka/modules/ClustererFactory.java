package de.dkt.eservices.eweka.modules;

import eu.freme.common.exception.BadRequestException;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.AbstractDensityBasedClusterer;
import weka.clusterers.CLOPE;
import weka.clusterers.Clusterer;
import weka.clusterers.Cobweb;
import weka.clusterers.DBScan;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.FilteredClusterer;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.MakeDensityBasedClusterer;
import weka.clusterers.OPTICS;
import weka.clusterers.RandomizableClusterer;
import weka.clusterers.RandomizableDensityBasedClusterer;
import weka.clusterers.RandomizableSingleClustererEnhancer;
import weka.clusterers.SimpleKMeans;
import weka.clusterers.SingleClustererEnhancer;
import weka.clusterers.XMeans;
import weka.clusterers.sIB;

public class ClustererFactory {

	/**
	 * Implemented Algorithms
	 * 
	 * Cobweb, EM, SimpleKMeans, CLOPE, DBSCAN, FarthestFirst, 
	 * FilteredClusterer, HierarchicalClusterer, MakeDensityBasedClusterer, OPTICS, 
	 * sIB, XMeans
	 */

	/**
	 * NOT Implemented Algorithms
	 * 
	 * AbstractClusterer, AbstractDensityBasedClusterer, RandomizableClusterer, RandomizableDensityBasedClusterer, 
	 * RandomizableSingleClustererEnhancer, SingleClustererEnhancer,
	 */

	public static Clusterer getClusterer(String type) throws Exception {
		if(type.equalsIgnoreCase("cobweb")){
			Cobweb cb = new Cobweb();
			return cb;
		}
		else if(type.equalsIgnoreCase("simplekmeans")){
			SimpleKMeans skm = new SimpleKMeans();
			String[] options = new String[4];
			options[0] = "-I";                 // max. iterations
			options[1] = "100";
			options[2] = "-N";                 // max. iterations
			options[3] = "10";
			skm.setOptions(options);     // set the options
			return skm;
		}
		else if(type.equalsIgnoreCase("em")){
			EM em = new EM();
			String[] options = new String[2];
			options[0] = "-I";                 // max. iterations
			options[1] = "100";
			em.setOptions(options);     // set the options
			return em;
		}
		else if(type.equalsIgnoreCase("CLOPE")){
			CLOPE clu = new CLOPE();
			return clu;
		}
		else if(type.equalsIgnoreCase("DBSCAN")){
			DBScan clu = new DBScan();
			return clu;
		}
		else if(type.equalsIgnoreCase("FarthestFirst")){
			FarthestFirst clu = new FarthestFirst();
			return clu;
		}
		else if(type.equalsIgnoreCase("FilteredClusterer")){
			FilteredClusterer clu = new FilteredClusterer();
			return clu;
		}
		else if(type.equalsIgnoreCase("HierarchicalClusterer")){
			HierarchicalClusterer clu = new HierarchicalClusterer();
			return clu;
		}
		else if(type.equalsIgnoreCase("MakeDensityBasedClusterer")){
			MakeDensityBasedClusterer clu = new MakeDensityBasedClusterer();
			return clu;
		}
		else if(type.equalsIgnoreCase("OPTICS")){
			OPTICS clu = new OPTICS();
			return clu;
		}
		else if(type.equalsIgnoreCase("sIB")){
			sIB clu = new sIB();
			return clu;
		}
		else if(type.equalsIgnoreCase("XMeans")){
			XMeans clu = new XMeans();
			return clu;
		}
		else{
			throw new BadRequestException("Unsupported classifier type");
		}
	}
}
