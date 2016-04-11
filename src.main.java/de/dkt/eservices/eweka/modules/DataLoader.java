package de.dkt.eservices.eweka.modules;

import eu.freme.common.exception.BadRequestException;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DataLoader {

	public static Instances loadDataFromFile(String filePath) throws Exception {
		String fileType = filePath.substring(filePath.lastIndexOf('.')+1);
		if(fileType.equalsIgnoreCase("csv") || fileType.equalsIgnoreCase("arff") || fileType.equalsIgnoreCase("xrff")){
			Instances data1 = DataSource.read(filePath);
			return data1;
		}
		else{
			throw new BadRequestException("Unsupported input file format.");
		}
	}
	
}
