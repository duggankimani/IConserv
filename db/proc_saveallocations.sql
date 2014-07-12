--DROP TRIGGER IF EXISTS updateallocations ON programfund cascade;
DROP FUNCTION IF EXISTS func_updateallocations() cascade;

CREATE FUNCTION func_updateallocations() RETURNS trigger AS $updateallocations$
DECLARE
	v_parentprogramid bigint; --parent program
	v_parentallocation numeric; -- Amount allocated from parent program so far
	v_parentactual numeric; --parent actual consumption
	v_parentcommitedamount numeric;--total amount commited so far
	v_previousbudget numeric; -- Amount allocated to the child prior to this update
	v_previousactual numeric; --parent previous consumption
	v_previouscommitedamount numeric;
	v_newbudget numeric; -- new budget
	v_newactual numeric; --new actual
	v_newcommitedamount numeric;
	v_fundid bigint;
BEGIN
	
	v_newbudget=0;
	
	if(TG_OP='DELETE') then
		v_fundid=OLD.fundid;
		v_previousbudget=OLD.amount;
		v_previousactual=OLD.actualAmount;
		v_previouscommitedamount=OLD.commitedamount;
		
		select parentid into v_parentprogramid from programdetail where id=OLD.programid;		
		
		--total budget for an activity or outcome from various sources
		update programdetail set budgetamount= (COALESCE(budgetamount,0.0)-COALESCE(v_previousbudget,0.0)),
		actualAmount= (COALESCE(actualAmount,0.0)-COALESCE(v_previousactual,0.0)),
		commitedAmount= (COALESCE(commitedAmount,0.0)-COALESCE(v_previouscommitedamount,0.0))
		where id=OLD.programid;
		
	elsif(TG_OP='INSERT') then
		v_newbudget=NEW.amount;
		v_newactual=NEW.actualAmount;
		v_newcommitedamount=NEW.commitedAmount;
		v_fundid=NEW.fundid;
		
		select parentid into v_parentprogramid from programdetail where id=NEW.programid;
		
		--total budget for an activity or outcome from various sources
		update programdetail set budgetamount= (COALESCE(budgetamount,0.0)+COALESCE(v_newbudget,0.0)),
		actualAmount= (COALESCE(actualAmount,0.0)+COALESCE(v_newactual,0.0)),
		commitedAmount= (COALESCE(commitedAmount,0.0)+COALESCE(v_newcommitedamount,0.0))
		where id=NEW.programid;
	else
		v_fundid=COALESCE(NEW.fundid,OLD.fundid);
		v_newbudget=NEW.amount;	
		v_newactual=NEW.actualAmount;
		v_newcommitedamount=NEW.commitedAmount;
		v_previousbudget=OLD.amount;
		v_previousactual=OLD.actualAmount;
		v_previouscommitedamount=OLD.commitedamount;
		
		select parentid into v_parentprogramid from programdetail where id=OLD.programid;
		
		--total budget for an activity or outcome from various sources
		update programdetail set budgetamount= (COALESCE(budgetamount,0.0) + COALESCE(v_newbudget,0.0) - COALESCE(v_previousbudget,0.0)),
		actualAmount= (COALESCE(actualAmount,0.0) + COALESCE(v_newactual,0.0) - COALESCE(v_previousactual,0.0)),
		commitedAmount= (COALESCE(commitedAmount,0.0) + COALESCE(v_newcommitedamount,0.0) - COALESCE(v_previouscommitedamount,0.0))
		where id=OLD.programid;
	end if;
	
	--find corresponding fund;
	IF(v_parentprogramid is not null and (COALESCE(v_newbudget,0.0) != COALESCE(v_previousbudget,0.0) or 
	   (COALESCE(v_newactual,0.0) != COALESCE(v_previousactual,0.0)) or
	   (COALESCE(v_newcommitedamount,0.0) != COALESCE(v_previouscommitedamount,0.0)) )) THEN
		
		--How much funding has been allocated so far in the parent
		select allocatedAmount,actualAmount,commitedAmount into v_parentallocation,v_parentactual,v_parentcommitedamount from programfund where programid=v_parentprogramid and fundid=v_fundid;
		
		--RAISE INFO '%     %    %    %', v_parentprogramid, v_parentbudget, v_newbudget,v_previousbudget;
	
		--calculate new allocation/actual/commitedTotal
		v_parentallocation=COALESCE(v_parentallocation,0.0) + COALESCE(v_newbudget,0.0) - COALESCE(v_previousbudget,0.0);
		v_parentactual=COALESCE(v_parentactual,0.0) + COALESCE(v_newactual,0.0) - COALESCE(v_previousactual,0.0);
		v_parentcommitedamount=COALESCE(v_parentcommitedamount,0.0) + COALESCE(v_newcommitedamount,0.0) - COALESCE(v_previouscommitedamount,0.0);
		
		update programfund set allocatedAmount=v_parentallocation,actualAmount=v_parentactual,commitedamount=v_parentcommitedamount where programid=v_parentprogramid and fundid=v_fundid;
		
		/*if(v_fundbudgetid is null) then
			insert into fundbudget(budget,actual,programfundid) values (v_parentbudget,v_parentactual, (select id from programfund where programid=v_parentprogramid and fundid=v_fundid));
		else
			update fundbudget set budget=v_parentbudget,actual=v_parentactual where id=v_fundbudgetid;
		end if;*/
		
	END IF;
	if(TG_OP='DELETE') then
    	RETURN OLD;
    else
    	RETURN NEW;
   end if;
END;
$updateallocations$ LANGUAGE plpgsql;

CREATE TRIGGER updateallocations BEFORE INSERT OR UPDATE OR DELETE ON programfund
    FOR EACH ROW EXECUTE PROCEDURE func_updateallocations();