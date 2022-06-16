delete from prism_owner.attached_file_tbl
where file_name is null or file_size is null or file_size = 0;

update prism_owner.attached_file_tbl
set item_id = (0-item_id), end_date = sysdate
where attached_file_id in
(
	select attached_file_id
	from prism_owner.item_tbl, prism_owner.attached_file_tbl
	where attached_file_tbl.item_id = item_tbl.item_id and category_id like '6000__' and attached_file_id not in
	(select x from(select item_id, max(attached_file_id) as x from prism_owner.attached_file_tbl where file_contents is not null and file_name is not null group by item_id))
);