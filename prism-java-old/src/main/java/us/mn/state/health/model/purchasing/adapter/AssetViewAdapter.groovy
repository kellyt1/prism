package us.mn.state.health.model.purchasing.adapter

import us.mn.state.health.model.purchasing.AssetView

/**
 * User: kiminn1
 * Date: 11/13/2017
 * Time: 10:46 AM
 */

class AssetViewAdapter {

    static AssetViewDTO toDTO(AssetView model){
        AssetViewDTO dto = new AssetViewDTO()
        dto.orderLineItemId = model.orderLineItemId
        dto.mdhPo = model.mdhPo
        dto.assetNumber = model.assetNumber
        dto.mrq = model.mrq
        dto.assetDescription = model.assetDescription
        dto.assetsType = model.assetsType
        dto.cost = model.cost
        dto.orderDate = model.orderDate
        dto.fundingStreams = model.fundingStreams
        dto.quantity = model.quantity
        dto.deliverToName = model.deliverToName
        dto.deliverToAddress = model.deliverToAddress
        dto.updatedBy = model.updatedBy

        return dto
    }
}
