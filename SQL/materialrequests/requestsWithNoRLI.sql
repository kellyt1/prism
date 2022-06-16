select * from PRISM_OWNER.MATERIALS_REQUEST_TBL t
where t.request_id not in (select request_id from PRISM_OWNER.MATERIALS_REQ_LINE_ITEM_TBL)