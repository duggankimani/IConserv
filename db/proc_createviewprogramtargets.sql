drop view if exists vprogramtargets;

create or replace view vprogramtargets as
select programId,id,parentid,periodid,type,startdate,enddate,status,target, actualoutcome from (with recursive programdetail_tree as (
select pd.id as programId,pd.id, pd.parentid,pd.periodid,pd.type,pd.startdate,pd.enddate,pd.status,t.target, t.actualoutcome,1 as level, array[pd.id] as path_info 
from programdetail pd left join targetandoutcome t on (t.programid=pd.id) 
where pd.id in (select id from programdetail where type='PROGRAM') 
union all
select path_info[1] as programId,c.id,c.parentid,p.periodid,c.type,c.startdate,c.enddate,c.status,tt.target, tt.actualoutcome, p.level+1, p.path_info||c.id 
from programdetail c join programdetail_tree p on c.parentid=p.id
left join targetandoutcome tt on (tt.programid=c.id)
)select programId,id,parentid,periodid,type,startdate,enddate,status,target, actualoutcome from programdetail_tree order by path_info) as programhierachy order by programId,id;
