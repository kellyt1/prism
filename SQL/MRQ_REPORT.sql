select distinct
    substr(mrt.tracking_number, 5) as "MRQ",
    case when rli.item_description is null then it.description else rli.item_description end as "Description",
    rli.quantity as "Quantity",
    case when rli.item_cost = 0 then it.dispense_unit_cost * rli.quantity else rli.item_cost * rli.quantity end as "Estimated Cost",
    pt.last_name || ', ' || pt.first_name as "Requested By",
    mrt.date_requested as "Date Requested",
    obt.dept_id || '-FY' || substr(extract(year from round(sysdate, 'yy')),3) || '-' || obt.org_bdgt_name ||
    ' / ' || obt.appr_code || ' - ' || obt.appr_name as "Funding Source",
    fst.amount
from prism_owner.materials_request_tbl mrt
inner join admin_db.person_tbl pt on mrt.requestor_person_id = pt.person_id
inner join prism_owner.materials_req_line_item_tbl rli on mrt.request_id = rli.request_id
inner join prism_owner.materials_rli_fnd_src_tbl fst on fst.request_line_item_id = rli.request_line_item_id
inner join admin_db.org_bdgt_tbl obt on obt.org_bdgt_id = fst.org_bdgt_id
left join prism_owner.item_tbl it on it.item_id = rli.item_id
--Change the list of in parameters for different MRQ numbers.
where substr(mrt.tracking_number, 5) in ('195612', '195840', '202154')
order by MRQ desc