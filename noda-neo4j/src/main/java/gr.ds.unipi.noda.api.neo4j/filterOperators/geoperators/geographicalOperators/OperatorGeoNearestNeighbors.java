package gr.ds.unipi.noda.api.neo4j.filterOperators.geoperators.geographicalOperators;

import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geometries.Point;

public final class OperatorGeoNearestNeighbors extends GeographicalOperator<Point> {

    private final int neighbors;

    private OperatorGeoNearestNeighbors(String fieldName, Point point, int neighbors) {
        super(fieldName, point);
        this.neighbors = neighbors;
    }

    public static OperatorGeoNearestNeighbors newOperatorGeoNearestNeighbors(String fieldName, Point point, int neighbors) {
        return new OperatorGeoNearestNeighbors(fieldName, point, neighbors);
    }


    @Override
    public StringBuilder getOperatorExpression() {
        return null;
    }
}
