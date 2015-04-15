--DROP TRIGGER IF EXISTS recalculateAll ON programfund cascade;
DROP FUNCTION IF EXISTS func_recalculateAll(bigint);

CREATE FUNCTION func_recalculateAll(in p_programid bigint) RETURNS VOID AS $recalculateAll$
DECLARE
	v_newactual numeric; --new actual
	v_newcommitedamount numeric; -- commited amount
    	v_childcount int;
	arow RECORD;
	
BEGIN
	--v_valueArray[| budgetamount |     actualamount     | commitedamount | totalallocation]
	SELECT count(*) into v_childcount from programdetail where parentid=p_programid;
	if(v_childcount>0) then
		
		FOR arow IN 
			select id from programdetail where parentid=p_programid and status!='CLOSED'				
		  LOOP
  		    PERFORM 1 FROM func_recalculateAll(arow.id);
		  END LOOP;
		
		--update programfund		
		FOR arow IN 
		--sum programfunds of the children
		select fundid, sum(commitedamount) commitedamount, sum(actualamount) actualamount from programfund where programid in (select id from programdetail where parentid=p_programid) group by fundid

		LOOP
		--Update programfunds for programid e.g |programTT|usaid|30000; |programTT|EU|30000;
		update programfund set commitedamount=arow.commitedamount, actualamount= arow.actualamount where programid=p_programid and fundid=arow.fundid;
		--end loop
		End LOOP;

		--Update Total sum of funds in ProgramDetail for programid i.e Budget, Commited amount, Actual Amount
		SELECT sum(commitedamount),sum(actualamount) into v_newcommitedamount,v_newactual from programfund where programid=p_programid;
		update programdetail set commitedAmount= v_newcommitedamount, actualAmount= v_newactual where id=p_programid;

	end if;

END;
$recalculateAll$ LANGUAGE plpgsql;

