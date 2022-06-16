select distinct
    c.category_code||'-'||LPAD(s.icnbr, 4, '0') AS ICNBR,
    --ob.org_bdgt_id,
    ob.appr_name,
    ob.org_bdgt_name,
    --ob.dept_id,
    ob.org_bdgt_code,
    case when ob.end_date < to_date('2014-07-01', 'YYYY-MM-DD') then 'EXPIRED, NEEDS UPDATING' else 'POTENTIALLY INCORRECT' end as budget_status,
    p1.last_name || ', ' || p1.first_name as primary_contact,
    p2.last_name || ', ' || p2.first_name as secondary_contact
from prism_owner.stock_item_tbl s
left join prism_owner.item_tbl i on s.stock_item_id = i.item_id
left join prism_owner.category_tbl c on i.category_id = c.category_id
left join admin_db.org_bdgt_tbl ob on ob.org_bdgt_id = s.org_bdgt_id
inner join admin_db.person_tbl p1 on p1.person_id = s.primary_contact
inner join admin_db.person_tbl p2 on p2.person_id = s.secondary_contact
where s.status_id = 47583 and s.icnbr is not null and (ob.end_date < to_date('2014-07-01', 'YYYY-MM-DD') or ob.org_bdgt_id = 7914437)
order by primary_contact, secondary_contact, icnbr