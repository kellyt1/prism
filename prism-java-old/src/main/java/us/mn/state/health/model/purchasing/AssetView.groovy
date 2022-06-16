package us.mn.state.health.model.purchasing

/**
 * User: kiminn1
 * Date: 11/8/2017
 * Time: 4:00 PM
 */

class AssetView {
    Long orderLineItemId
    String mdhPo, assetNumber, mrq, assetDescription, assetsType
    Double cost
    Date orderDate
    String fundingStreams
    Long quantity
    String deliverToName, deliverToAddress, updatedBy
}
