package gr.ds.unipi.noda.api.mongo.filterOperators.geoperators.geoTextualOperators.geoTextualApproximateOperators;

import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geographicalOperators.GeographicalOperator;
import gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geometries.Geometry;

public abstract class GeoTextualApproximateOperator<U extends Geometry> extends gr.ds.unipi.noda.api.core.operators.filterOperators.geoperators.geoTextualOperators.geoTextualApproximateOperators.GeoTextualApproximateOperator<StringBuilder,U> {

    protected GeoTextualApproximateOperator(GeographicalOperator<StringBuilder, U> geographicalOperator, String fieldName, String[] keywords) {
        super(geographicalOperator, fieldName, keywords);
    }
}
