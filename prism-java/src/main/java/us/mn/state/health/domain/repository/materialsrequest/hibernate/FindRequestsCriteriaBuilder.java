package us.mn.state.health.domain.repository.materialsrequest.hibernate;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.ilike;
import static org.hibernate.criterion.Restrictions.le;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.util.NotYetImplementedException;
import us.mn.state.health.util.hibernate.CriteriaBuilder;

class FindRequestsCriteriaBuilder implements CriteriaBuilder {
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    RequestSearchCriteria searchCriteria;

    public FindRequestsCriteriaBuilder(RequestSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public void addCriteria(Criteria criteria) throws HibernateException {
        criteria.createAlias("requestLineItems", "rli");

        if (searchCriteria.isTrackingNumberSpecified()) {
            criteria.add(ilike("trackingNumber", searchCriteria.getTrackingNumber()));
        }

        if (searchCriteria.isDateRequestedFromSpecified()) {
            Date date = null;
            try {
                date = DateUtils.parseDate(searchCriteria.getDateRequestedFrom(), new String[]{DEFAULT_DATE_FORMAT});
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.add(ge("dateRequested", date));
        }

        if (searchCriteria.isDateRequestedToSpecified()) {
            Date date = null;
            try {
                date = DateUtils.parseDate(searchCriteria.getDateRequestedTo(), new String[]{DEFAULT_DATE_FORMAT});
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.add(le("dateRequested", date));
        }

        if (searchCriteria.isNeedByDateFromSpecified()) {
            Date date = null;
            try {
                date = DateUtils.parseDate(searchCriteria.getNeedByDateFrom(), new String[]{DEFAULT_DATE_FORMAT});
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.add(ge("needByDate", date));
        }
        if (searchCriteria.isNeedByDateToSpecified()) {
            Date date = null;
            try {
                date = DateUtils.parseDate(searchCriteria.getNeedByDateTo(), new String[]{DEFAULT_DATE_FORMAT});
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.add(le("needByDate", date));
        }

        if (searchCriteria.isStatusIdSpecified()) {
            criteria.add(eq("rli.status.statusId", new Long(searchCriteria.getStatusId())));
        }

        if (searchCriteria.isCategoryIdSpecified()) {
            Long catId = new Long(searchCriteria.getCategoryId());
//                criteria.add(eq("rli.catId", catId));
//                criteria.add(or(sqlRestriction(catId +"=all (select rli.ITEM_CATEGORY_ID from MATERIALS_REQ_LINE_ITEM_TBL rli" +
//                        " where rli.REQUEST_ID = {alias}.REQUEST_ID)"),
//                        sqlRestriction(catId + "= all (select item.CATEGORY_ID from MATERIALS_REQ_LINE_ITEM_TBL rli "
//                                +" inner join ITEM_TBL item "+
//                                " on item.item_id=rli.item_id" +
//                                " where rli.REQUEST_ID = {alias}.REQUEST_ID)")));

        }

        switch (searchCriteria.getSortBy()) {
            case RequestSearchCriteria.SORT_BY_REQUEST_ID:
                criteria
                        .addOrder(searchCriteria.getSortMethod().equals(RequestSearchCriteria.ASC) ? org.hibernate.criterion.Order
                                .asc("requestId")
                                : org.hibernate.criterion.Order
                                .desc("requestId"));
                break;
            default:
                throw new NotYetImplementedException();
        }
    }


}
