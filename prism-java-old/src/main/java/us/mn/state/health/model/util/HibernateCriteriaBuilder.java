package us.mn.state.health.model.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import us.mn.state.health.common.lang.StringUtils;

/**
 * A helper class that appends diferent criterias to an existing criteria
 */

public class HibernateCriteriaBuilder {
    private Criteria criteria;

    public HibernateCriteriaBuilder(Criteria criteria) {
        this.criteria = criteria;
    }

    /**
     * @param aliases - a map that contains pairs alias(key)-association path(value) for building the aliases in order
     *  to have a join and eagerly fetch certain properties that are mapped as associations
     */

    public void buildAliases(Map aliases){
        Set keys = aliases.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String alias = (String) iterator.next();
            String associationPath = (String) aliases.get(alias);
            if(!StringUtils.nullOrBlank(alias) && !StringUtils.nullOrBlank(associationPath)){
                criteria.createAlias(associationPath, alias);
            }
        }

    }
}
