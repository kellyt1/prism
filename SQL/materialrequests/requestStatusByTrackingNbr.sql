select r.tracking_number, s.name, t.quantity, i.description
from PRISM_OWNER.MATERIALS_REQ_LINE_ITEM_TBL t, PRISM_OWNER.STATUS_TBL s, 
     PRISM_OWNER.ITEM_TBL i, PRISM_OWNER.MATERIALS_REQUEST_TBL r
where t.request_id IN (select m.request_id from PRISM_OWNER.MATERIALS_REQUEST_TBL m
                      where m.tracking_number IN ('MRQ-1862', 'MRQ-1864',
                                                  'MRQ-1861', 'MRQ-1860'))
and t.status_id = s.status_id
and t.item_id = i.item_id
and t.request_id = r.request_id