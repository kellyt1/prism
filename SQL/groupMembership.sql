select e.group_id, g.group_code, p.first_name, p.last_name, p.person_id
from ADMIN_DB.ENTITY_target_group_link e, ADMIN_DB.GROUP_TBL g, ADMIN_DB.PERSON_TBL p
where g.Group_Purpose like 'PRISM%'
and g.group_id = e.group_id
and e.entity_target_id = p.person_id