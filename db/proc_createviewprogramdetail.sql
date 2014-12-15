drop view if exists vprogramdetail;
create or replace view vprogramdetail as
select programId,id,parentid,periodid,type,startdate,enddate,datecompleted,status,actualamount, budgetamount from (with recursive programdetail_tree as (
select id as programId,id, parentid,periodId,type,startdate,enddate,datecompleted,status, actualamount, budgetamount,1 as level, array[id] as path_info 
from programdetail where id in (select id from programdetail where type='PROGRAM') 
union all
select path_info[1] as programId,c.id,c.parentid,p.periodid,c.type,c.startdate,c.enddate,c.datecompleted,c.status,c.actualamount, c.budgetamount, p.level+1, p.path_info||c.id 
from programdetail c join programdetail_tree p on c.parentid=p.id)
select programId,id,parentid,periodid,type,startdate,enddate,datecompleted,status,actualamount, budgetamount from programdetail_tree order by path_info) as programhierachy;

