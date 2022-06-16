-- Dennis Rausch 9/30/2014 v1.1
select
    org.fund_code,
    org.dept_id,
    org.appr_code,
    org_bdgt_code,
    org.project_id,
    org.org_bdgt_name,
    minimum_amount,
    case when person.last_name is not null then person.last_name || ', ' || person.first_name else null end as approver
from prism_owner.business_rules_tbl br
inner join admin_db.org_bdgt_tbl_view org on org.org_bdgt_id = br.org_budget_id and (org.end_date is null or org.end_date > sysdate)
inner join admin_db.group_tbl gr on gr.group_name = br.primary_evaluator
left join admin_db.entity_target_group_link etgl on etgl.group_id = gr.group_id
left join admin_db.person_tbl person on person.person_id = etgl.entity_target_id
where (br.end_date is null or br.end_date > sysdate) and (etgl.end_date is null or etgl.end_date > sysdate)
order by org.dept_id,appr_code,org_bdgt_code,org.project_id,org.org_bdgt_code