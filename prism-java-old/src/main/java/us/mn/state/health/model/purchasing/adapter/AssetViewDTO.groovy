package us.mn.state.health.model.purchasing.adapter

/**
 * User: kiminn1
 * Date: 11/13/2017
 * Time: 10:45 AM
 */

class AssetViewDTO {
    Long orderLineItemId
    String mdhPo, assetNumber, mrq, assetDescription, assetsType
    Double cost
    Date orderDate
    String fundingStreams
    Long quantity
    String deliverToName, deliverToAddress, updatedBy
}
