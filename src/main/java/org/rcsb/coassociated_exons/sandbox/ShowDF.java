package org.rcsb.coassociated_exons.sandbox;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rcsb.genevariation.io.DataLocationProvider;
import org.rcsb.genevariation.utils.SaprkUtils;

import static org.apache.spark.sql.functions.col;

/**
 * Created by yana on 4/19/17.
 */
public class ShowDF {

    public static void main(String[] args) {

//        String[] chromosomes = {"chr1", "chr2", "chr3", "chr4", "chr5", "chr6", "chr7", "chr8", "chr9", "chr10", "chr11",
//                "chr12", "chr13", "chr14", "chr15", "chr16", "chr17", "chr18", "chr19", "chr20", "chr21", "chr22", "chrX", "chrY"};

        String[] chromosomes = {"chr1"};

        for (String chr : chromosomes) {

            Dataset<Row> map = SaprkUtils.getSparkSession().read().parquet(DataLocationProvider.getGencodePDBLocation() + "/" + chr);
            map.filter(col("uniProtId").equalTo("Q8WZA1").and(col("pdbId").equalTo("5GGF")).and(col("chainId").equalTo("B")).
                    and(col("start").equalTo(46189273))).show();

//            Dataset<Row> map2 = SaprkUtils.getSparkSession().read().parquet(DataLocationProvider.getGencodeStructuralMappingLocation() + "/" + chr);
//            map.filter(col("uniProtId").equalTo("Q8WZA1").and(col("pdbId").equalTo("5GGF")).and(col("chainId").equalTo("B")).
//                    and(col("start").equalTo(46189273))).show();

            Dataset<Row> map3 = SaprkUtils.getSparkSession().read().parquet(DataLocationProvider.getUniprotPdbMappinlLocation());
            map3.filter(col("uniProtId").equalTo("Q8WZA1").and(col("pdbId").equalTo("5GGF")).and(col("chainId").equalTo("B")).and(col("uniProtPos").equalTo(632))).show();

            Dataset<Row> map4 = SaprkUtils.getSparkSession().read().parquet(DataLocationProvider.getGencodeUniprotLocation()+"/chr1");
            map4.show();
        }
    }
}