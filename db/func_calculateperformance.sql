drop function if exists func_calculateperformance(periodid int);

CREATE FUNCTION func_calculateperformance(in p_periodid int)
RETURNS table(
        name varchar(255),
	description varchar(255),
  	id bigint, 
	countsuccess int, 
	countfail int, 
	countnodata int, 
	percsuccess double precision,
	percfail double precision,
        avegpercsuccess double precision
) AS $$
DECLARE
  v_programcount int;
  arow RECORD;
  v_successc int;
  v_failc int;
  v_nodatac int;
  v_successper double precision;
  v_failper double precision;
  v_total int;
  v_totalsuccessper double precision;
BEGIN

create temp table programcount on commit drop as select v.programId, count(*) count from vprogramdetail v where v.periodid=p_periodid group by v.programId;
create temp table programstats(id bigint, countsuccess int, countfail int, countnodata int, percsuccess double precision,percfail double precision) on commit drop;

v_totalsuccessper=0.0;
for arow in 
  select * from programcount
loop
  v_successper=0.0;
  v_failper=0.0;
  v_total = 0; 
  --RAISE INFO 'Executing  id = %, count=% ',arow.programId,arow.count;

   select count(*) into v_successc  from vprogramdetail p where p.status='CLOSED' and p.actualamount is not null and p.actualamount!=0 and p.budgetamount>=p.actualamount and p.programId=arow.programId and p.periodid=p_periodid;

   select count(*) into v_failc  from vprogramdetail p where p.status='CLOSED' and p.actualamount is not null and p.actualamount!=0 and p.budgetamount<p.actualamount and p.programId=arow.programId and p.periodid=p_periodid;

   select count(*) into v_nodatac  from vprogramdetail p where p.status='CLOSED' and (p.actualamount is null or p.actualamount=0) and p.programId=arow.programId and  p.periodid=p_periodid;
  
  v_total = (v_successc+v_failc);
  if(v_total!=0) then
     v_successper = v_successc*100/ v_total;
     v_failper = v_failc*100/v_total;
  end if;
  v_totalsuccessper = v_totalsuccessper+v_successper;

  --insert
  insert into programstats values (arow.programId, v_successc, v_failc,v_nodatac, v_successper,v_failper);
  
end loop;
	
select count(*) into v_programcount from programstats p where p.percsuccess!=0;

if(v_programcount!=0) then 
     RETURN QUERY select * from (select (select d.name from programdetail d where d.id=p.id),(select d.description from programdetail d where d.id=p.id),*, v_totalsuccessper/v_programcount from programstats p) as rslt order by name;
else
     RETURN QUERY select * from (select (select d.name from programdetail d where d.id=p.id),(select d.description from programdetail d where d.id=p.id),*, 0.0::double precision from programstats p) as rslt order by name;
end if;


drop table programcount;
drop table programstats;

END;
$$ LANGUAGE plpgsql;
