package gr.ds.unipi.noda.api.yyydatabase.filterOperators.textualOperators.conditionalTextualOperators;

public final class OperatorAllKeywords extends ConditionalTextualOperator {

    private OperatorAllKeywords(String fieldName, String[] elements){
        super(fieldName, elements);
    }

    public static OperatorAllKeywords newOperatorAllKeywords(String fieldName, String keyword1, String keyword2, String... keywords){
        return new OperatorAllKeywords(fieldName, twoKeywordsAtLeastToArray(keyword1, keyword2, keywords));
    }

}
