package us.mn.state.health.common.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import us.mn.state.health.common.lang.StringUtils;

public class LuceneUtils {
    /**
     * This method builds a query for all the words that the 'value' contain.
     * It also escapes the special lucene charactersand it generates a query like this:
     * (+concatenatedContent:token1* + concatenatedContent:token2* + ...)(+concatenatedContent:token1 + concatenatedContent:token2 + ...)
     *
     * @param value
     * @param indexField
     * @return lucene Query
     */

    public static Query createSearchAllTheWordsQuery(String value, String indexField, BooleanClause.Occur occur) {
        BooleanQuery booleanQuery = new BooleanQuery();
        BooleanQuery booleanQuery1 = new BooleanQuery();
        BooleanQuery booleanQuery2 = new BooleanQuery();
        Query tempQuery1;
        Query tempQuery2;
        String[] tokens = value.split("[\\s]");
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            token = StringUtils.escapeSpecialCharactersInLucene(token);
            if (token.length() > 0) {
                /*
                We have to create the lucene query this way because if we
                use only the prefix query and we have the queryString=20# the query becomes
                concatenatedContent:20#* and it doesn't match any document.
                If we use only the TermQuery, the query becomes concatenatedContent:20,
                but it doesn't mach the results that have words starting with the searching token.
                So that's why we are using them both, generating a query like this:
                (+concatenatedContent:token1* + concatenatedContent:token2* + ...)(+concatenatedContent:token1 + concatenatedContent:token2 + ...)
                */
                tempQuery1 = new PrefixQuery(new Term(indexField, token));
                tempQuery2 = new TermQuery(new Term(indexField, token));
                booleanQuery1.add(tempQuery1, occur);
                booleanQuery2.add(tempQuery2, occur);
            }
        }
        booleanQuery.add(booleanQuery1, BooleanClause.Occur.SHOULD);
        booleanQuery.add(booleanQuery2, BooleanClause.Occur.SHOULD);
        return booleanQuery;
    }
}
