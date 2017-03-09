package org.rcsb.genevariation.filters;

import org.rcsb.genevariation.constants.VariantType;
import org.rcsb.genevariation.datastructures.VariantInterface;

public class DataProviderFilterSNP implements IDataProviderFilter {

	@Override
	public boolean filter(VariantInterface variant) {
		if ( variant.getType().compareTo(VariantType.SNP) == 0 ) { 
			return true;
		}
		return false;
	}
}