package gr.ds.unipi.noda.api.core.nosqldb;

import gr.ds.unipi.noda.api.core.dataframe.BaseDataframeManipulator;
import gr.ds.unipi.noda.api.core.dataframe.DataframeManipulator;
import gr.ds.unipi.noda.api.core.operators.aggregateOperators.AggregateOperator;
import gr.ds.unipi.noda.api.core.operators.aggregateOperators.BaseAggregateOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.filterOperators.comparisonOperators.BaseComparisonOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.filterOperators.comparisonOperators.ComparisonOperator;
import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geoTemporalOperators.BaseGeoTemporalOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geoTemporalOperators.GeoTemporalOperator;
import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geoTextualOperators.BaseGeoTextualOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geoTextualOperators.GeoTextualOperator;
import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geographicalOperators.BaseGeographicalOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geographicalOperators.GeographicalOperator;
import gr.ds.unipi.noda.api.core.operators.filterOperators.logicalOperators.BaseLogicalOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.filterOperators.logicalOperators.LogicalOperator;
import gr.ds.unipi.noda.api.core.operators.filterOperators.textualOperators.BaseTextualOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.filterOperators.textualOperators.TextualOperator;
import gr.ds.unipi.noda.api.core.operators.sortOperators.BaseSortOperatorFactory;
import gr.ds.unipi.noda.api.core.operators.sortOperators.SortOperator;
import org.apache.spark.sql.SparkSession;

public abstract class NoSqlConnectionFactory {

    protected NoSqlConnectionFactory() {
        setBaseOperators();
    }

    public abstract NoSqlDbOperators noSqlDbOperators(NoSqlDbConnector connector, String s, SparkSession sparkSession);

    public abstract void closeConnection(NoSqlDbConnector noSqlDbConnector);

    public abstract boolean closeConnections();

    private void setBaseOperators() {
        ComparisonOperator.comparisonOperator = getBaseComparisonOperatorFactory();

        GeographicalOperator.geoSpatialOperator = getBaseGeoSpatialOperatorFactory();
        GeoTemporalOperator.geoTemporalOperator = getBaseGeoTemporalOperatorFactory();
        GeoTextualOperator.geoTextualOperator = getBaseGeoTextualOperatorFactory();

        LogicalOperator.logicalOperator = getBaseLogicalOperatorFactory();
        AggregateOperator.aggregateOperator = getBaseAggregateOperatorFactory();
        SortOperator.sortOperator = getBaseSortOperatorFactory();

        TextualOperator.textualOperator = getBaseTextualOperatorFactory();

        DataframeManipulator.baseDataframeManipulator = getBaseDataframeManipulator();
    }

    protected abstract BaseAggregateOperatorFactory getBaseAggregateOperatorFactory();

    protected abstract BaseComparisonOperatorFactory getBaseComparisonOperatorFactory();

    protected abstract BaseGeographicalOperatorFactory getBaseGeoSpatialOperatorFactory();

    protected abstract BaseGeoTemporalOperatorFactory getBaseGeoTemporalOperatorFactory();

    protected abstract BaseGeoTextualOperatorFactory getBaseGeoTextualOperatorFactory();

    protected abstract BaseLogicalOperatorFactory getBaseLogicalOperatorFactory();

    protected abstract BaseSortOperatorFactory getBaseSortOperatorFactory();

    protected abstract BaseTextualOperatorFactory getBaseTextualOperatorFactory();

    protected abstract BaseDataframeManipulator getBaseDataframeManipulator();

}
