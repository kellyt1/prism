package us.mn.state.health.model.util.search;

import java.util.Locale;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lucene.LuceneUtils;
import us.mn.state.health.common.lucene.SmartDayQueryParser;

public class LuceneQueryBuilder {

    private Object bean;
    private BooleanQuery query;

    private static final boolean INCLUSIVE = true;
    private static final boolean EXCLUSIVE = false;

    public LuceneQueryBuilder(Object bean, BooleanQuery query) {
        this.bean = bean;
        this.query = query;
    }

    //Public Methods

    public void addAndAny(String property, String index) {
        addAnd(property, index, BooleanClause.Occur.SHOULD);
    }

    public void addAndAll(String property, String index) {
        addAnd(property, index, BooleanClause.Occur.MUST);
    }

    public void addAndDateRangeInclusive(String property1, String property2, String index) throws InfrastructureException {
        try {
            addAndDateRange(property1, property2, index, INCLUSIVE);
        }
        catch (Exception e) {
            throw new InfrastructureException("Failed executing addAndDateRangeInclusive: ", e);
        }
    }

    public void addAndDateRangeExclusive(String property1, String property2, String index) throws InfrastructureException {
        try {
            addAndDateRange(property1, property2, index, EXCLUSIVE);
        }
        catch (Exception e) {
            throw new InfrastructureException("Failed executing addAndDateRangeExclusive: ", e);
        }
    }

    public void addAndMatchPhrase(String property, String index) {
        String value = getProperty(property);
        if (value != null) {
            value = StringUtils.escapeSpecialCharactersInLucene(value);
            query.add(termQuery(index, value), BooleanClause.Occur.MUST);
        }
    }

    //Helper Methods

    private void addAndDateRange(String property1, String property2, String index, boolean inclusive) throws Exception {
        String value1 = getProperty(property1);
        String value2 = getProperty(property2);
        if (value1 == null && value2 == null) {
            //if both values are null we don't want to add any search criteria for dates            
            return;
        }
        if (value1 == null) {
            value1 = "01/01/1900";
        }
        if (value2 == null) {
            value2 = "01/01/2099";
        }
        if (value1 != null && value2 != null) {
            Query rangeQuery = rangeQuery(value1, value2, index, inclusive);
            SmartDayQueryParser parser = new SmartDayQueryParser(index, new StandardAnalyzer());
            parser.setLocale(Locale.US);
            rangeQuery = parser.parse(rangeQuery.toString(index));
            query.add(rangeQuery, BooleanClause.Occur.MUST);
        }
    }

    private void addAndRange(String property1, String property2, String index, boolean inclusive) {
        String value1 = getProperty(property1);
        String value2 = getProperty(property2);
        if (value1 != null && value2 != null) {
            Query rangeQuery = rangeQuery(value1, value2, index, inclusive);
            query.add(rangeQuery, BooleanClause.Occur.MUST);
        }
    }

    private void addAnd(String property, String index, BooleanClause.Occur occur) {
        String value = getProperty(property);
        if (!StringUtils.nullOrBlank(value)) {
            Query anyWordQuery = LuceneUtils.createSearchAllTheWordsQuery(value, index, occur);
            query.add(anyWordQuery, BooleanClause.Occur.MUST);
        }
    }

    private String getProperty(String property) {
        Object value = PropertyUtils.getProperty(bean, property);
        if (value != null) {
            if (!StringUtils.nullOrBlank(value.toString())) {
                return value.toString();
            }                                                   
        }
        return null;
    }

    private static RangeQuery rangeQuery(String value1,
                                         String value2,
                                         String index,
                                         boolean inclusive) {
        return new RangeQuery(new Term(index, value1), new Term(index, value2), inclusive);
    }

    private static Query prefixQuery(String index, String value) {
        return new PrefixQuery(new Term(index, value));
    }

    private static TermQuery termQuery(String index, String value) {
        return new TermQuery(new Term(index, value));
    }
}