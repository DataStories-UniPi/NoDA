package gr.ds.unipi.noda.api.redisearch;

import gr.ds.unipi.noda.api.core.constants.AggregationKeywords;
import gr.ds.unipi.noda.api.core.constants.StringPool;
import gr.ds.unipi.noda.api.core.nosqldb.NoSqlDbConnector;
import gr.ds.unipi.noda.api.core.nosqldb.NoSqlDbOperators;
import gr.ds.unipi.noda.api.core.operators.aggregateOperators.AggregateOperator;
import gr.ds.unipi.noda.api.core.operators.filterOperators.FilterOperator;
import gr.ds.unipi.noda.api.core.operators.sortOperators.SortOperator;
import gr.ds.unipi.noda.api.redisearch.filterOperators.RediSearchPostFilterOperator;
import gr.ds.unipi.noda.api.redisearch.filterOperators.geographicalOperators.geoSpatialOperators.OperatorGeoNearestNeighbors;
import gr.ds.unipi.noda.api.redisearch.filterOperators.geographicalOperators.geoSpatialOperators.RediSearchGeoSpatialOperator;
import gr.ds.unipi.noda.api.redisearch.filterOperators.geographicalOperators.geoSpatialOperators.RediSearchGeoSpatialOperatorFactory;
import io.redisearch.AggregationResult;
import io.redisearch.aggregation.SortedField;
import io.redisearch.aggregation.reducers.Reducer;
import io.redisearch.querybuilder.Node;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RediSearchOperators extends NoSqlDbOperators {
    private RediSearchQueryHelper queryHelper;

    private RediSearchOperators(NoSqlDbConnector connector, String indexName, SparkSession sparkSession) {
        super(connector, indexName, sparkSession);
    }

    static RediSearchOperators newRedisOperators(NoSqlDbConnector connector, String indexName, SparkSession sparkSession) {
        return new RediSearchOperators(connector, indexName, sparkSession);
    }

    RediSearchQueryHelper queryHelper() {
        if (queryHelper == null) {
            queryHelper = new RediSearchQueryHelper(getDataCollection(), RediSearchConnectionManager.getInstance().getConnection(getNoSqlDbConnector()));
        }
        return queryHelper;
    }

    @Override
    public NoSqlDbOperators filter(FilterOperator filterOperator, FilterOperator... filterOperators) {
        if(func.apply(filterOperator, filterOperators).anyMatch(RediSearchGeoSpatialOperatorFactory.isGeo) && queryHelper().isAggregate())
            throw new UnsupportedOperationException("RediSearchGeoSpatialOperator is not supported as post filter query.");
        else if (RediSearchGeoSpatialOperatorFactory.isOperatorGeoBox(filterOperator) && filterOperators.length == 0) {
            queryHelper().setzRangeInfo(((RediSearchGeoSpatialOperator) filterOperator)
                    .getZRangeInfo().apply(queryHelper().getJedisResource(), getDataCollection()));
        } else if (RediSearchGeoSpatialOperatorFactory.isOperatorGeoBox(filterOperator) && filterOperators.length > 0) {
            throw new IllegalArgumentException("OperatorInGeoRectangle cannot be combined with other FilterOperators.");
        } else {
            applyQuery(filterOperator, filterOperators);
        }
        return this;
    }

    private void applyQuery(FilterOperator filterOperator, FilterOperator[] filterOperators) {
        if (queryHelper().isAggregate()) {
            queryHelper().applyPostQuery(func.apply(filterOperator, filterOperators).map(f -> ((RediSearchPostFilterOperator) f).getPostOperatorExpression().toString()).collect(Collectors.joining()));
        } else {
            func.apply(filterOperator, filterOperators).forEach(f -> queryHelper().applyPreQuery((Node) f.getOperatorExpression()));
        }
        func.apply(filterOperator, filterOperators).filter(RediSearchGeoSpatialOperatorFactory::isOperatorGeoNearestNeighbor).findAny()
                .ifPresent(filterOperator1 -> queryHelper().applyResultLimit(((OperatorGeoNearestNeighbors) filterOperator1).getNeighborsCount()));
    }

    public NoSqlDbOperators groupBy(String fieldName, AggregateOperator... aggregateOperator) {
        queryHelper().applyGroupBy(StringPool.AT.concat(fieldName), Arrays.stream(aggregateOperator).map(AggregateOperator::getOperatorExpression).map(Reducer.class::cast).toArray(Reducer[]::new));
        return this;
    }

    @Override
    public NoSqlDbOperators distinct(String fieldName) {
        return groupBy(fieldName);
    }

    @Override
    public void printScreen() {
        queryHelper().printResults();
    }

    @Override
    public Optional<Double> max(String fieldName) {
        queryHelper().applyGroupBy((Reducer) AggregateOperator.aggregateOperator.newOperatorMax(fieldName).getOperatorExpression());
        AggregationResult aggregate = queryHelper().executeAggregation();
        return Optional.of(aggregate.getRow(0).getDouble(AggregationKeywords.MAX.toString().concat(fieldName)));
    }

    @Override
    public Optional<Double> min(String fieldName) {
        queryHelper().applyGroupBy((Reducer) AggregateOperator.aggregateOperator.newOperatorMin(fieldName).getOperatorExpression());
        AggregationResult aggregate = queryHelper().executeAggregation();
        return Optional.of(aggregate.getRow(0).getDouble(AggregationKeywords.MIN.toString().concat(fieldName)));
    }

    @Override
    public Optional<Double> sum(String fieldName) {
        queryHelper().applyGroupBy((Reducer) AggregateOperator.aggregateOperator.newOperatorSum(fieldName).getOperatorExpression());
        AggregationResult aggregate = queryHelper().executeAggregation();
        return Optional.of(aggregate.getRow(0).getDouble(AggregationKeywords.SUM.toString().concat(fieldName)));
    }

    @Override
    public Optional<Double> avg(String fieldName) {
        queryHelper().applyGroupBy((Reducer) AggregateOperator.aggregateOperator.newOperatorAvg(fieldName).getOperatorExpression());
        AggregationResult aggregate = queryHelper().executeAggregation();
        return Optional.of(aggregate.getRow(0).getDouble(AggregationKeywords.AVG.toString().concat(fieldName)));
    }

    @Override
    public int count() {
        return queryHelper().totalResults();
    }

    @Override
    public NoSqlDbOperators sort(SortOperator sortOperator, SortOperator... sortingOperators) {
        SortedField[] sortedFields = Stream.concat(Stream.of(sortOperator), Arrays.stream(sortingOperators))
                .map(SortOperator::getOperatorExpression).map(SortedField.class::cast).toArray(SortedField[]::new);
        queryHelper().applySortBy(sortedFields);
        return this;
    }

    @Override
    public NoSqlDbOperators limit(int limit) {
        queryHelper().applyResultLimit(limit);
        return this;
    }

    @Override
    public NoSqlDbOperators project(String fieldName, String... fieldNames) {
        String[] fields = Stream.concat(Stream.of(fieldName), Arrays.stream(fieldNames)).toArray(String[]::new);
        queryHelper().applyReturnFields(fields);
        return this;
    }

    @Override
    public Dataset<Row> toDataframe() {
        throw new UnsupportedOperationException("ToDataframe primitive is not supported");
    }

    private final BiFunction<FilterOperator, FilterOperator[], Stream<FilterOperator>> func = (filterOperator, filterOperators) -> Stream.concat(Stream.of(filterOperator), Arrays.stream(filterOperators));
}