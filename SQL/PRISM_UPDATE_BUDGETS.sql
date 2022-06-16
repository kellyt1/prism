CREATE OR REPLACE PROCEDURE "PRISM_OWNER"."PRISM_UPDATE_BUDGETS" is
begin
/* Dennis Rausch 9/10/2015 v1.9.5 */
/*Create groups*/
insert into admin_db.group_tbl(group_id, group_code, group_short_name, group_name, group_purpose, inserted_by, insertion_date)
select 
	admin_db.enterprise_seq.nextval,
	upper(gt.group_code),
	gt.group_short_name,
	upper(gt.group_code),
	'PRISM APPROVAL GROUP',
	'SProc',
	sysdate
from(select distinct
    case
        when ob.project_id is null then ob.dept_id||'|'||ob.appr_code||'|'||'NONE'
        else ob.dept_id||'|'||ob.appr_code||'|'||ob.project_id||'|'||substr(ob.dept_id, 5, 4)
    end as group_code,
	('PRISM'||substr(dept_id,-4)) as group_short_name
from admin_db.org_bdgt_tbl ob
where ob.end_date > to_date('01-jul-' || extract(year from round(sysdate - 365, 'yyyy')), 'dd-mm-yyyy')
) gt where upper(gt.group_code) not in (select upper(group_code) from admin_db.group_tbl);

/*Create business rules*/
insert into prism_owner.business_rules_tbl(object_value, object_type, minimum_amount, business_rule_id, primary_evaluator, evaluate_field, lastupdated, lastupdatedby, approval_level, org_budget_id)
select
	ob.org_bdgt_id,
	'ORGID',
	ob.minimum_amount,
	admin_db.enterprise_seq.nextval,
	ob.primary_evaluator,
	'ORGBUDGET.ORGBUDGETID',
	sysdate,
	'SProc',
	ob.approval_level,
	ob.org_bdgt_id
from(select distinct
		ob.org_bdgt_id as object_value,
		nvl(br.approval_level, 1) as approval_level,
		ob.org_bdgt_id,
		nvl(br.minimum_amount, 500) as minimum_amount,
		case
            when ob.project_id is null then ob.dept_id||'|'||ob.appr_code||'|'||'NONE'
            else ob.dept_id||'|'||ob.appr_code||'|'||ob.project_id||'|'||substr(ob.dept_id, 5, 4)
        end as primary_evaluator
	from admin_db.org_bdgt_tbl ob
    left join prism_owner.business_rules_tbl br on br.org_budget_id = ob.org_bdgt_id
	where ob.end_date > sysdate - 180 and ob.org_bdgt_id not in (select org_budget_id from prism_owner.business_rules_tbl where org_budget_id is not null)
) ob;

/*Carry over approvers*/
insert into admin_db.entity_target_group_link(entity_target_id, group_id, start_date, relationship_type, inserted_by, insertion_date)
select distinct
	gl.entity_target_id,
	gt2.group_id,
	sysdate,
	'MEMBERSHIP',
	'SProc',
	sysdate
from admin_db.org_bdgt_tbl ob1
inner join admin_db.org_bdgt_tbl ob2 on ob1.end_date between sysdate - 30 and sysdate
    and (ob1.dept_id = ob2.dept_id or substr(ob1.dept_id, 0, 4) || ob1.org_bdgt_code = ob2.dept_id)
    and (ob1.appr_code = ob2.appr_code or substr(ob1.appr_code, 0, 3) || substr(ob1.project_id, -4, 3) || substr(ob1.appr_code, -1) = ob2.appr_code)
    and substr(nvl(ob1.project_id,'NONE'), -4, 3) = substr(nvl(ob2.project_id,'NONE'), -4, 3)
inner join prism_owner.business_rules_tbl br1 on br1.org_budget_id = ob1.org_bdgt_id
inner join prism_owner.business_rules_tbl br2 on br2.org_budget_id = ob2.org_bdgt_id
inner join admin_db.group_tbl gt1 on gt1.group_code = br1.primary_evaluator and gt1.end_date is null
inner join admin_db.group_tbl gt2 on gt2.group_code = br2.primary_evaluator and gt2.end_date is null
inner join admin_db.entity_target_group_link gl on gl.group_id = gt1.group_id and gl.end_date is null and gl.termination_date is null
where gl.entity_target_id||'-'||gt2.group_id not in (select distinct entity_target_id||'-'||group_id from admin_db.entity_target_group_link);
end;
