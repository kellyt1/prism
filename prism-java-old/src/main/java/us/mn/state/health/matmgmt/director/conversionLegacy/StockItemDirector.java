package us.mn.state.health.matmgmt.director.conversionLegacy;

import us.mn.state.health.builder.conversionLegacy.StockItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class StockItemDirector {
    private StockItemBuilder builder;

    public StockItemDirector(StockItemBuilder builder) {
        this.builder = builder;
    }


    public void construct() throws InfrastructureException {
        /**
         * 1
         */
        builder.buildCategory();
        /**
         * 2
         * Brad said that we have the manufacturer only for fixed assets
         */
        builder.buildManufacturer();
        /**
         * 3
         */
        builder.buildDescription();
        /**
         * 4
         */
        builder.buildHazardous();

        /**
         * 5
         */
        builder.buildEOQ();
        /**
         * 7
         */
        builder.buildDispenseUnit();
        /**
         * 8
         */
        builder.buildEstimatedAnnualUsage();
        /**
         * 9
         */
        builder.buildAnnualUsage();

        /**
         * 10
         */
        builder.buildLastUpdateDate();
        /**
         * 11
         */
        builder.buildLastUpdatedBy();

        /**
         * 12
         */
        builder.buildTerminationDate();

        /**
         * 13
         */
        builder.buildterminatedBy();
        /**
         * 14
         */
        builder.buildInsertionDate();
        /**
         * 15
         */
        builder.buildCurrentDemand();
        /**
         *
         */
        builder.buildDispenseUnitCost();
        /**
         * 16
         */
        builder.buildICNBR();
        /**
         * 17
         */
        builder.buildQtyOnHand();

        /**
         * 18
         */
        builder.buildLocations();

        /**
         * 19
         */
        builder.buildROP_ROQ();
        /**
         * 20
         */
        builder.buildCycleCountPriority();
        /**
         * 21
         */
        builder.buildFillUntilDepleted();

        /**
         * 22
         */
        builder.buildReorderDate();

        /**
         * 23
         */
        builder.buildHoldUntilDate();
        /**
         * 24
         */

        builder.buildPackQty();
        /**
         * 25
         */
        builder.buildStaggedDelivery();

        /**
         * 26
         */
        builder.buildSeasonal();
        /**
         * 28
         * We convert only stock items with the status Active or On-Hold
         * Here we have to set the 'hold until date' and 'fill until depleted'
         */
        builder.buildStatus();
        /**
         * 29
         * this is the method that sets the primary and secondary contact
         */
        builder.buildContacts();

        /**
         * 30
         */

        builder.buildOrgBudget();

        /**
         * 33
         */

        builder.buildPotentialIndicator();

        /**
         * 34
         */

        builder.buildAssistanDivDirector();

        /**
         * 35
         */

        builder.buildInstructions();

        /**
         * 36
         */
        builder.buildObjectCode();
    }


    public void constructItemVendorsContracts() throws InfrastructureException {
        /**
         * 6
         * This method build the itemVendors links and the vendorContracts
         */
        builder.buildItemVendors();

    }

}
