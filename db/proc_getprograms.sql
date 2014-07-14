-- contraint only for non admins
--select d.id,d.name,a.userid,a.groupid,a.type from programdetail d 
--inner join programaccess a on (a.programdetailid=d.id)
--where (a.userid='calcacuervo' or a.groupid in ('USERS'));


with recursive programdetail_tree as (
select id as programId,id, parentid,type,startdate,enddate,status, 1 as level, array[id] as path_info 
from programdetail where id in (3,1,2,47,50,4,48) 
union all
select path_info[1] as programId,c.id,c.parentid,c.type,c.startdate,c.enddate,c.status, p.level+1, p.path_info||c.id 
from programdetail c join programdetail_tree p on c.parentid=p.id)
select programId,id,parentid,type,startdate,enddate,status from programdetail_tree order by path_info;

