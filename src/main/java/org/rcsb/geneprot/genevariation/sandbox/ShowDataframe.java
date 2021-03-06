package org.rcsb.geneprot.genevariation.sandbox;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rcsb.geneprot.common.utils.SparkUtils;

/**
 * Created by yana on 4/19/17.
 */
public class ShowDataframe {

    public static void main(String[] args) {

        Dataset<Row>  df2 = SparkUtils.getSparkSession().read().parquet("/Users/yana/data/parquet/metal-binding-residues");
        df2.show();

        SparkUtils.stopSparkSession();
    }
}
