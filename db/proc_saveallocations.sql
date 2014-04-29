--DROP TRIGGER IF EXISTS updateallocations ON programfund cascade;
DROP FUNCTION IF EXISTS func_updateallocations() cascade;

CREATE FUNCTION func_updateallocations() RETURNS trigger AS $updateallocations$
DECLARE
	v_parentprogramid bigint; --parent program
	v_parentallocation numeric; -- Amount allocated from parent program so far
	v_previousallocation numeric; -- Amount allocated to the child prior to this update
	v_newallocation numeric; -- new allocation
	v_fundid bigint;
	v_fundallocationid bigint;
BEGIN
	
	v_newallocation=0;
	
	if(TG_OP='DELETE') then
		v_fundid=OLD.fundid;
		v_previousallocation=OLD.amount;

		select parentid into v_parentprogramid from programdetail where id=OLD.programid;		
		
		--total budget for an activity or outcome from various sources
		update programdetail set budgetamount= (COALESCE(budgetamount,0.0)-COALESCE(v_previousallocation,0.0)) where id=OLD.programid;
		
	elsif(TG_OP='INSERT') then
		v_newallocation=NEW.amount;
		v_fundid=NEW.fundid;
		
		select parentid into v_parentprogramid from programdetail where id=NEW.programid;
		
		--total budget for an activity or outcome from various sources
		update programdetail set budgetamount= (COALESCE(budgetamount,0.0)+COALESCE(v_newallocation,0.0)) where id=NEW.programid;
	else
		v_newallocation=NEW.amount;		
		v_fundid=COALESCE(NEW.fundid,OLD.fundid);
		v_previousallocation=OLD.amount;
		
		select parentid into v_parentprogramid from programdetail where id=OLD.programid;
		
		--total budget for an activity or outcome from various sources
		update programdetail set budgetamount= (COALESCE(budgetamount,0.0) + COALESCE(v_newallocation,0.0) - COALESCE(v_previousallocation,0.0)) where id=OLD.programid;
	end if;
	
	--find corresponding fund;
	IF(v_parentprogramid is not null) THEN
		
		select id into v_fundallocationid from fundallocation where programfundid=(select id from programfund where programid=v_parentprogramid and fundid=v_fundid);
		
		--How much funding has been allocated so far in the parent
		select allocation into v_parentallocation from fundallocation where id=v_fundallocationid;
		
		--RAISE INFO '%     %    %    %', v_parentprogramid, v_parentallocation, v_newallocation,v_previousallocation;
	
		--calculate new allocation
		v_parentallocation=COALESCE(v_parentallocation,0.0) + COALESCE(v_newallocation,0.0) - COALESCE(v_previousallocation,0.0);
		
		if(v_fundallocationid is null) then
			insert into fundallocation(allocation,programfundid) values (v_parentallocation, (select id from programfund where programid=v_parentprogramid and fundid=v_fundid));
		else
			update fundallocation set allocation=v_parentallocation where id=v_fundallocationid;
		end if;
		
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