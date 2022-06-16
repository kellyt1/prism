package us.mn.state.health.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.model.common.Category;

public class UtilStaticMethods {
    
    /**
     * Method that returns a string enumeration of the category codes for the objects from the colection for a SQL query
     * @param categories - a collection of Category objects
     * @return
     */
    public static String createSQLEnumerationStringFromCategories(Collection categories) {
        StringBuffer result = new StringBuffer("(");
        Iterator iterator = categories.iterator();
        while (iterator.hasNext()) {
            Category category = (Category) iterator.next();
            result.append("'" + category.getCategoryCode() + "', ");
        }
        if (result.length() > 2) {
            result.delete(result.length() - 2, result.length());
        }
        result.append(")");
        if (result.length() < 3) {
            result = new StringBuffer("(NULL)");
        }
        return result.toString();
    }

    /**
     * Method that returns an ArrayList with the category codes for the objects from the colection for a Lucene query
     * @param categories - a collection of Category objects
     * @return
     */
    public static ArrayList createStringEnumerationFromCategories(Collection categories) {
        ArrayList result = new ArrayList();
        Iterator iterator = categories.iterator();
        while (iterator.hasNext()) {
            Category category = (Category) iterator.next();
            result.add(category.getCategoryCode());
        }
        return result;
    }
}
