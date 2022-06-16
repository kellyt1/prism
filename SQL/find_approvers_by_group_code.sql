SELECT distinct
    pt.person_id,
    pt.first_name || ' ' || pt.mid_name || ' ' || pt.last_name as name
FROM admin_db.entity_target_group_link gl
inner join admin_db.group_tbl gt on gl.group_id = gt.group_id
inner join admin_db.person_tbl pt on gl.entity_target_id = pt.person_id
WHERE gl.insertion_date > sysdate - 400 and gt.group_code like '%H1236206%'