package org.rcsb.genevariation.parser;

import org.biojava.nbio.genome.App;
import org.rcsb.genevariation.datastructures.Exon;
import org.rcsb.genevariation.datastructures.Transcript;
import org.rcsb.genevariation.utils.CommonUtils;
import org.biojava.nbio.core.util.InputStreamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** A parser that parses a file from the UCSC genome browser that contains mapping of gene name to chromosome positions
 *
 * @author Yana Valasatava (modified of parser written by Andreas Prlic)
 */
public class GenePredictionsParser {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static final String DEFAULT_MAPPING_URL="http://hgdownload.cse.ucsc.edu/goldenPath/hg38/database/refGene.txt.gz";
	
	public static void main(String[] args){
		try {

			List<Transcript> genePositions=	getChromosomeMappings();
			logger.info("got {} gene positions", genePositions.size());

			for (Transcript pos : genePositions){
				if ( pos.getGeneName().equals("FOLH1")) {
					logger.info("Gene Position: {}", pos);
					break;
				}
			}

		} catch(Exception e){
			logger.error("Exception: ", e);
		}
	}

	public static List<Transcript> getChromosomeMappings() throws IOException {
		
		URL url = new URL(DEFAULT_MAPPING_URL);
		InputStreamProvider prov = new InputStreamProvider();
		InputStream inStream = prov.getInputStream(url);
		return getChromosomeMappings(inStream);
	}

	public static List<Transcript> getChromosomeMappings(InputStream inStream) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		ArrayList<Transcript> transcripts = new ArrayList<Transcript>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			Transcript gcp = getGeneChromosomePosition(line);
			if ( gcp != null)
				transcripts.add(gcp);
		}
		// since this is a large list, remove empty content.
		transcripts.trimToSize();
		return transcripts;
	}

	private static Transcript getGeneChromosomePosition(String line) {
		
		if ( line == null)
			return null;
		
		String[] spl = line.split("\t");
		if ( spl.length < 16) {
			logger.warn("Line does not have 16 data items, but {}: {}", spl.length, line);
			return null;
		}

		Transcript t = new Transcript();

		t.setChromosomeName(spl[2]);
		t.setGeneName(spl[12]);
		t.setGeneBankId(spl[1]);
		
		t.setOrientation(spl[3]);
		
		t.setCodingStart(Integer.parseInt(spl[6]));
		t.setCodingEnd(Integer.parseInt(spl[7]));

		List<Integer> exonStarts = CommonUtils.getIntegerListFromString(spl[9], ",");
		List<Integer> exonEnds = CommonUtils.getIntegerListFromString(spl[10], ",");
		List<Integer> exonOffsets = CommonUtils.getIntegerListFromString(spl[15], ",");
		
		int exonsCount = Integer.parseInt(spl[8]);
		t.setExonsCount(exonsCount);
		
		List<Exon> exons = new ArrayList<Exon>();
		for ( int i=0; i < exonsCount; i++ ) {
			Exon exon = new Exon();
			exon.setStart(exonStarts.get(i));
			exon.setEnd(exonEnds.get(i));
			exon.setPhase(exonOffsets.get(i));
			exons.add(exon);
		}
		t.setExons(exons);
		return t;
	}
}
