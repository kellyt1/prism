package us.mn.state.health.model.inventory;

import javax.persistence.Entity;
import javax.persistence.Transient;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.inventory.ItemDAO;
import us.mn.state.health.model.util.search.EntityIndex;
import us.mn.state.health.model.util.search.PurchaseItemIndex;
import us.mn.state.health.model.util.search.Searchable;

import java.util.Date;

/**
 * This class is basically meant to be the concrete version of Item.  It represents
 * an 'purchase' item that would be in the catalog (i.e., a frequently purchased item),
 * but that we do not keep on-hand in any way. This class would probably not define any properties
 * or methods that should not be in the abstract super class, and so its really just the 'instantiate-able' version
 * of the super class.
 */
@Entity
//@Indexed(index = "PurchaseItem")
public class PurchaseItem extends Item implements Comparable, Searchable {
    private static final long serialVersionUID = 4569039924508124845L;
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



    public PurchaseItem() {
    }

    public PurchaseItem(String description) {
        super(description);
    }


    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Item)) return false;

        final Item that = (Item) o;

        if (this.getItemId() == null) {
            if (that.getItemId() == null) {
                //dig deeper, using comparison by value
                if (!that.getItemType().equals(Item.PURCHASE_ITEM)) {
                    return false;
                }
                if (this.getDescription() != null && !this.getDescription().equals(that.getDescription())) {
                    return false;
                }

                return true;
            } else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        } else {  //we know we can't get a NullPointerException now...
            return this.getItemId().equals(that.getItemId());
        }
    }

    public int hashCode() {
        int result = 14;
        result = (getDescription() != null ? getDescription().hashCode() : 0);
        result = 29 * result + (getInsertionDate() != null ? getInsertionDate().hashCode() : 0);
        return result;
    }


    public int compareTo(Object o) {
        if (o instanceof PurchaseItem) {
            return this.getDescription().compareTo(((PurchaseItem) o).getDescription());
        }
        return 0;
    }

    @Transient
    public EntityIndex getEntityIndex() throws InfrastructureException {
        return new PurchaseItemIndex();
    }

    public void save() throws InfrastructureException {
        ItemDAO itemDAO = daoFactory.getItemDAO();
        itemDAO.makePersistent(this);
    }

    public void delete() throws InfrastructureException {
        ItemDAO itemDAO = daoFactory.getItemDAO();
        if (this.getItemId() != null) {  //can't delete an already transient instance
            itemDAO.makeTransient(this);
        }
    }

    @Transient
    public String getItemType() {
        return PURCHASE_ITEM;
    }
}
