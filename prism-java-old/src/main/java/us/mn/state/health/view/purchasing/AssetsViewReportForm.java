package us.mn.state.health.view.purchasing;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.purchasing.adapter.AssetViewDTO;

import java.util.List;

/**
 * User: kiminn1
 * Date: 11/13/2017
 * Time: 10:04 AM
 */

public class AssetsViewReportForm extends ValidatorForm {

    private String orderDate;
    private List<AssetViewDTO> results;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<AssetViewDTO> getResults() {
        return results;
    }

    public void setResults(List<AssetViewDTO> results) {
        this.results = results;
    }
}
