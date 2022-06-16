--Dennis Rausch 6/12/2014 v1.1
create or replace view
    admin_db.org_bdgt_tbl_view
    (
        org_bdgt_id,
        org_bdgt_code,
        org_bdgt_name,
        program_code,
        bdgt_account_code,
        fund_code,
        appr_code,
        inserted_by,
        insertion_date,
        effective_date,
        end_date,
        comments,
        provisions,
        report_category,
        changed_by,
        change_date,
        manager_group_id,
        orgdisplay,
        realorg_indicator,
        dept_id,
        project_id,
        source_type
    ) as
select
    org_bdgt_id,
    org_bdgt_code,
    case
        WHEN instr(org_bdgt_code, 'XX') > 0
        then dept_id_name || ' / ' || appr_code || ' - ' || appr_name
        when project_id is not null
        then org_bdgt_name || ' ' || project_id
        else org_bdgt_name
    end as org_bdgt_name,
    program_code,
    bdgt_account_code,
    fund_code,
    appr_code,
    inserted_by,
    insertion_date,
    effective_date,
    end_date,
    comments,
    provisions,
    report_category,
    changed_by,
    change_date,
    manager_group_id,
    case
        when instr(org_bdgt_code, 'XX') > 0
        then 'N/A'
        else org_bdgt_code
    end as orgdisplay,
    null,
    dept_id,
    project_id,
    source_type
from
    org_bdgt_tbl
where
    end_date < to_date('01-jul-' || extract(year from round(sysdate, 'yyyy')), 'dd-mm-yyyy')
    or next_day(effective_date - 14, 'monday') < sysdate
union
select
    a.org_bdgt_id,
    a.org_bdgt_code,
    a.org_bdgt_name,
    b.program_code,
    b.bdgt_account_code,
    b.fund_code,
    b.appr_code,
    a.inserted_by,
    a.insertion_date,
    a.effective_date,
    b.end_date,
    a.comments,
    null,
    b.report_category,
    a.changed_by,
    a.change_date,
    manager_group_id,
    org_bdgt_id_display,
    'N',
    b.dept_id,
    b.project_id,
    b.source_type
from
    org_bdgt_auxillary_tbl a,
    org_bdgt_tbl b
where
    a.org_bdgt_code = b.org_bdgt_code
    and b.end_date > sysdate - 1
    and(
        b.end_date < to_date('01-jul-' || extract(year from round(sysdate, 'yyyy')), 'dd-mm-yyyy')
        or next_day(b.effective_date - 14, 'monday') < sysdate)
    and b.end_date =
    (
        select
            min(c.end_date)
        from
            org_bdgt_tbl c
        where
            b.org_bdgt_code = c.org_bdgt_code
            and(
                c.end_date > sysdate - 1
                or c.end_date is null))
union
select
    a.org_bdgt_id,
    a.org_bdgt_code,
    a.org_bdgt_name,
    null,
    null,
    null,
    null,
    a.inserted_by,
    a.insertion_date,
    a.effective_date,
    sysdate - 30,
    a.comments,
    null,
    null,
    a.changed_by,
    a.change_date,
    null,
    org_bdgt_id_display,
    'N',
    null,
    null,
    null
from
    org_bdgt_auxillary_tbl a
where
    not exists
    (
        select
            a.org_bdgt_code
        from
            org_bdgt_tbl b
        where
            a.org_bdgt_code = b.org_bdgt_code
            and b.end_date > sysdate - 1
            and b.end_date =
            (
                select
                    min(c.end_date)
                from
                    org_bdgt_tbl c
                where
                    b.org_bdgt_code = c.org_bdgt_code
                    and(
                        c.end_date > sysdate - 1
                        or c.end_date is null))) ;