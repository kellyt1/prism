package us.mn.state.health.dao.inventory;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.util.search.StockItemIndex;

import java.util.ArrayList;
import java.util.Collection;

public class LuceneStockItemDAO extends LuceneItemDAO implements StockItemDAO {

    public LuceneStockItemDAO() {
    }

    public StockItem getStockItemById(Long itemId, boolean lock) throws InfrastructureException {
        return null;
    }

    public Collection findAll() throws InfrastructureException {
        PhraseQuery query = new PhraseQuery();
        query.add(new Term(StockItemIndex.CLASSNAME, StockItem.class.getName()));
        Collection items;
        StockItemIndex itemIndex = new StockItemIndex();
        try {
            items = itemIndex.search(query);
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findAll(int firstResult, int maxResults) throws InfrastructureException {
        PhraseQuery query = new PhraseQuery();
        query.add(new Term(StockItemIndex.CLASSNAME, StockItem.class.getName()));
        Collection items = new ArrayList();
        StockItemIndex itemIndex = new StockItemIndex();
        try {
            items.addAll(itemIndex.search(query));
            Item[] itemsArray = (Item[]) items.toArray();
            items.clear();
            for (int i = firstResult; i < itemsArray.length && i <= maxResults + 1; i++) {
                items.add(itemsArray[i]);
            }
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findAllActive(int firstResult, int maxResults) throws InfrastructureException {
        PhraseQuery query = new PhraseQuery();
        query.add(new Term(StockItemIndex.CLASSNAME, StockItem.class.getName()));

        Query queryStatus = new TermQuery(new Term(StockItemIndex.STATUS, "ACT"));
        BooleanQuery outerQuery = new BooleanQuery();
        outerQuery.add(query, BooleanClause.Occur.MUST);
        outerQuery.add(queryStatus, BooleanClause.Occur.MUST);

        Collection items = new ArrayList();
        StockItemIndex itemIndex = new StockItemIndex();
        try {
            items.addAll(itemIndex.search(outerQuery));
            Item[] itemsArray = (Item[]) items.toArray();
            items.clear();
            for (int i = firstResult; i < itemsArray.length && i <= maxResults + 1; i++) {
                items.add(itemsArray[i]);
            }
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByExample(StockItem item) throws InfrastructureException {
        return null;
    }

    public Collection findByCategoryCode(String categoryCode) throws InfrastructureException {
        PhraseQuery query = new PhraseQuery();
        query.add(new Term(StockItemIndex.CATEGORY_CODE, categoryCode));
        Collection items = null;
        StockItemIndex itemIndex = new StockItemIndex();
        try {
            items = itemIndex.search(query);
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByCategoryCode(String categoryCode, int firstResult, int maxResults, String orderBy) throws InfrastructureException {
        return null;
    }


    public void makePersistent(StockItem item) throws InfrastructureException {
//        StockItemIndex itemIndex = new StockItemIndex();
//        try {
//            itemIndex.add(item);
//        }
//        catch (Exception ex) {
//            throw new InfrastructureException(ex);
//        }
    }

    public void makeTransient(StockItem item) throws InfrastructureException {
//        StockItemIndex itemIndex = new StockItemIndex();
//        try {
//            itemIndex.drop(item);
//        } catch (Exception ex) {
//            throw new InfrastructureException(ex);
//        }
    }

    public Collection findByContactPerson(Person person) throws InfrastructureException {
        StockItemIndex itemIndex = new StockItemIndex();
        Collection stockItems;
        BooleanQuery query = new BooleanQuery();
        Query primaryContactQuery = new TermQuery(new Term(StockItemIndex.PRIMARY_CONTACT_ID, person.getPersonId().toString()));
        Query secondaryContactQuery = new TermQuery(new Term(StockItemIndex.SECONDARY_CONTACT_ID, person.getPersonId().toString()));
        query.add(primaryContactQuery, BooleanClause.Occur.SHOULD);
        query.add(secondaryContactQuery, BooleanClause.Occur.SHOULD);
        try {
            stockItems = itemIndex.search(query);
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return stockItems;
    }

    public Collection findActiveByContactPerson(Person person) throws InfrastructureException {
        StockItemIndex itemIndex = new StockItemIndex();
        Collection stockItems;
        BooleanQuery query = new BooleanQuery();
        Query primaryContactQuery = new TermQuery(new Term(StockItemIndex.PRIMARY_CONTACT_ID, person.getPersonId().toString()));
        Query secondaryContactQuery = new TermQuery(new Term(StockItemIndex.SECONDARY_CONTACT_ID, person.getPersonId().toString()));
        query.add(primaryContactQuery, BooleanClause.Occur.SHOULD);
        query.add(secondaryContactQuery, BooleanClause.Occur.SHOULD);

        Query queryStatus = new TermQuery(new Term(StockItemIndex.STATUS, "ACT"));
        BooleanQuery outerQuery = new BooleanQuery();
        outerQuery.add(query, BooleanClause.Occur.MUST);
        outerQuery.add(queryStatus, BooleanClause.Occur.MUST);
        try {
            stockItems = itemIndex.search(outerQuery);
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return stockItems;
    }

    public Collection findByContactPerson(Person person, int firstResult, int maxResults) throws InfrastructureException {
        return null;
    }

    public StockItem findStockItemByCategoryCodeAndItemCode(String categoryCode, String icnbr) throws InfrastructureException {
        return null;
    }

    public Integer findNextICNBR() throws InfrastructureException {
        return null;
    }

    public Collection findByStatusCode(String statusCode) throws InfrastructureException {
        return null;
    }
}
