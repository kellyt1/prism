select
    cat.name,
    item_id,
    nvl(model,' '),
    --terminated_by,
    --termination_date,
    --item.version,
    --annual_usage,
    --availability_id,
    regexp_replace(description,'[^a-zA-Z0-9#:]',' ') as Description,
    --economic_order_qty,
    --hazardous,
    --manufacturer_id,
    --dispense_unit,
    --estimated_annual_usage,
    dispense_unit_cost
    --object_code_id,
    --hazardous_id,
    --descriptionforuser,
    --item.inserted_by,
    --item.insertion_date,
    --last_updated_by,
    --last_updated_date
from prism_owner.item_tbl item
inner join category_tbl cat on cat.category_id = item.category_id
where item.category_id like '600___' and termination_date is null and item_id not in (select item_id
from prism_owner.materials_request_tbl req
inner join prism_owner.materials_req_line_item_tbl li on li.request_id = req.request_id
where date_requested > sysdate - 365 and item_category_id like '600___' and item_id is not null)
order by cat.name