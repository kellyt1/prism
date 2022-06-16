package us.mn.state.health.model.util.search;

import us.mn.state.health.common.exceptions.InfrastructureException;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: RodenT1
 * Date: Jan 30, 2009
 * Time: 10:26:29 AM
 * To change this template use File | Settings | File Templates.
 */

public class BuildMissingLuceneIndexes extends TimerTask {

    public BuildMissingLuceneIndexes() {}

       public void run() {
          this.indexAsset();
          this.indexStockItem();
          this.indexRequest();
       }

        public void indexAsset() {
            try {
                if (!AssetIndex.indexExists) {
                    AssetIndex.createIndexAtRuntime();
                }
            } catch (InfrastructureException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public void indexStockItem() {
            try {
                if (!StockItemIndex.indexExists) {
                    StockItemIndex.createIndexAtRuntime();
                }
            } catch (InfrastructureException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public void indexRequest() {
            try {
                if (!RequestIndex.indexExists) {
                    RequestIndex.createIndexAtRuntime();
                }
                else if (!RequestLineItemIndex.indexExists) {
                    RequestLineItemIndex.createIndexAtRuntime();
                }
            } catch (InfrastructureException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
}
