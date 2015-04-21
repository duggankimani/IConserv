-- contraint only for non admins
--select d.id,d.name,a.userid,a.groupid,a.type from programdetail d 
--inner join programaccess a on (a.programdetailid=d.id)
--where (a.userid='calcacuervo' or a.groupid in ('USERS'));


with recursive programdetail_tree as (
select id as programId,id, parentid,type,startdate,enddate,status, 1 as level, array[id] as path_info 
from programdetail where id in (select id from programdetail where type='PROGRAM') 
union all
select path_info[1] as programId,c.id,c.parentid,c.type,c.startdate,c.enddate,c.status, p.level+1, p.path_info||c.id 
from programdetail c join programdetail_tree p on c.parentid=p.id)
select programId,id,parentid,type,startdate,enddate,status from programdetail_tree order by path_info;


--targets
--Surpassed target
select count(*) from programdetail p inner join targetandoutcome o on (o.programid=p.id) where p.status='CLOSED' and actualoutcome>=target;
-- Less than target
select count(*) from programdetail p inner join targetandoutcome o on (o.programid=p.id) where p.status='CLOSED' and actualoutcome<target;
--No data
select count(*) from programdetail p inner join targetandoutcome o on (o.programid=p.id) where p.status='CLOSED' and actualoutcome is null;


--Budgets
--Within budgets (Totals only considered)
select count(*) from programdetail p where p.status='CLOSED' and actualamount is not null and actualamount!=0 and budgetamount>=actualamount;
--above budgets
select count(*) from programdetail p where p.status='CLOSED' and actualamount!=0 and budgetamount<actualamount;
--no data
select count(*) from programdetail p where p.status='CLOSED' and (actualamount is null or actualamount=0)


--within timelines
select count(*) from programdetail p where p.status='CLOSED' and datecompleted is not null and datecompleted<enddate;
--exceeded timelines
select count(*) from programdetail p where p.status='CLOSED' and datecompleted is not null and datecompleted>enddate;


with recursive programdetail_tree as (
select m.id as programId,m.id,m.name, m.parentid,m.type,status,m.outcomeid,(select i.name from programdetail i where i.id=m.outcomeid) outcomename, 1 as level, array[id] as path_info 
from programdetail m where m.id in (1) 
union all
select path_info[1] as programId,c.id,c.name,c.parentid,c.type,c.status,c.outcomeid,(select i.name from programdetail i where i.id=c.outcomeid) outcomename, p.level+1, p.path_info||c.id 
from programdetail c join programdetail_tree p on c.parentid=p.id)
select programId,id,name,parentid,type,status,outcomeid,outcomename from programdetail_tree order by programid,outcomeid;

select id, name,description from programdetail where type='OUTCOME';



---amounts query --
with recursive programdetail_tree as (
select id as programId,id, parentid,type,status,budgetamount,actualamount,commitedamount,totalallocation, 1 as level, array[id] as path_info 
from programdetail where id in (select id from programdetail where type='PROGRAM') 
union all
select path_info[1] as programId,c.id,c.parentid,c.type,c.status,c.budgetamount,c.actualamount,c.commitedamount,c.totalallocation, p.level+1, p.path_info||c.id 
from programdetail c join programdetail_tree p on c.parentid=p.id)
select programId,id,parentid,type,status,budgetamount,actualamount,commitedamount,totalallocation from programdetail_tree where level<2 order by path_info;


--Program Funds for child items---
select fundid,sum(amount) amount,sum(commitedamount) commitedamount, sum(actualamount) actualamount from programfund where programid in (select id from programdetail where parentid=2) group by fundid;
--Program Fund for a Program(s)
select fundid,amount,commitedamount,actualamount from programfund where programid=2;
select budgetamount,commitedAmount, actualAmount from programdetail where id=2;

