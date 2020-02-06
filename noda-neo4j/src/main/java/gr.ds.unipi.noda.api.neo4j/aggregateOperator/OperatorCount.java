package gr.ds.unipi.noda.api.neo4j.aggregateOperator;

class OperatorCount extends AggregateOperator {

    private OperatorCount(String fieldName) {
        super(fieldName, "count");
    }

    public static OperatorCount newOperatorCount() {
        return new OperatorCount("");
    }

    @Override
    public T getOperatorExpression() {
        return null;
    }
}
