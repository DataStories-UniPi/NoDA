package gr.ds.unipi.noda.api.mongo.filterOperators.geoperators.geographicalOperators;

import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geometries.Rectangle;

public final class OperatorInGeoRectangle extends GeographicalOperator<Rectangle> {

    private OperatorInGeoRectangle(String fieldName, Rectangle rectangle) {
        super(fieldName, rectangle);
    }

    public static OperatorInGeoRectangle newOperatorInGeoRectangle(String fieldName, Rectangle rectangle) {
        return new OperatorInGeoRectangle(fieldName, rectangle);
    }

    @Override
    public StringBuilder getOperatorExpression() {
        return formOperatorExpressionForMultiPointGeometry(getFieldName(), getGeometry().getCoordinatesArray());
    }
}