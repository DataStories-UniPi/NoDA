package gr.ds.unipi.noda.api.mongo.sortOperators;

final class OperatorAsc extends SortOperator {

    private OperatorAsc(String fieldName) {
        super(fieldName);
    }

    public static SortOperator newOperatorAsc(String fieldName) {
        return new OperatorAsc(fieldName);
    }

    @Override
    protected String getSortConditionNum() {
        return "1";
    }
}
