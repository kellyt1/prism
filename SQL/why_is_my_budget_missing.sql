select
    "ORG_BDGT_ID",
    "ORG_BDGT_CODE",
    "ORG_BDGT_NAME",
    "PROGRAM_CODE",
    "BDGT_ACCOUNT_CODE",
    "FUND_CODE",
    "APPR_CODE",
    "INSERTED_BY",
    "INSERTION_DATE",
    "EFFECTIVE_DATE",
    "END_DATE",
    "COMMENTS",
    "PROVISIONS",
    "REPORT_CATEGORY",
    "CHANGED_BY",
    "CHANGE_DATE",
    "MANAGER_GROUP_ID",
    "PROVISION_NOTES",
    "DEPT_ID",
    "DEPT_ID_NAME",
    "APPR_NAME",
    "PROJECT_ID",
    "PROJECT_DESC",
    "SOURCE_TYPE",
    "AGENCY",
    "CONTACT_PERSON_ID",
    "CFDA",
    "FED_END_DATE"
from
(
select distinct
    ob.*,
    entity_target_id
from admin_db.org_bdgt_tbl ob
inner join prism_owner.business_rules_tbl br on ob.org_bdgt_id = br.org_budget_id
inner join admin_db.group_tbl gt on gt.group_code = br.primary_evaluator
left join admin_db.entity_target_group_link gl on gl.group_id = gt.group_id
where effective_date >= to_date('01-jul-2015', 'dd-mm-yyyy') and gt.inserted_by = 'SProc'
)
where ENTITY_TARGET_ID is null