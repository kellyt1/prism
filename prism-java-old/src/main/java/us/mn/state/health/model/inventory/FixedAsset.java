package us.mn.state.health.model.inventory;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;

@Entity
@Indexed(index = "assetIndex")
public class FixedAsset extends SensitiveAsset {
    private String fixedAssetNumber;    
    
    public FixedAsset() {
    }
 
    public int hashCode() {
        int result = super.hashCode();
        result += (getFixedAssetNumber() != null ? getFixedAssetNumber().hashCode() : 0);
        return result;
    }

    public String toString() {
        return "\n" + "Fixed Asset ID: ('" + this.getSensitiveAssetId() + "'), " +
                      "Asset Number: '" + getFixedAssetNumber() + "' " +
                      "Serial Number: '" + getSerialNumber();
    }

    public int compareTo(Object o) {
        if(o instanceof FixedAsset) {
            return this.getFixedAssetNumber().compareTo(((FixedAsset)o).getFixedAssetNumber());
        }
        return 0;
    }

    public void setFixedAssetNumber(String fixedAssetNumber) {
        this.fixedAssetNumber = fixedAssetNumber;
    }

    @Field(name = ASSET_NUMBER, index = Index.TOKENIZED, store = Store.YES)
    public String getFixedAssetNumber() {
        return fixedAssetNumber;
    }
}