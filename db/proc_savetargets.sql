--DROP TRIGGER IF EXISTS savetargets ON programfund cascade;
DROP FUNCTION IF EXISTS func_savetargets() cascade;

CREATE FUNCTION func_savetargets() RETURNS trigger AS $savetargets$
DECLARE
	v_parentprogramid numeric; --parent outcome id
	v_parentstatus varchar(20);--parent status
	v_parentoutcomentid numeric; --parent outcome id
	v_parentcurrentactual numeric; -- Total actual outcoment value in parent program so far
	v_previousactual numeric; -- Actual outcome value child prior to this update
	v_newactual numeric; -- new actual
BEGIN
	
	v_newactual=0;
	v_previousactual=0;
	
	if(TG_OP='DELETE') then
		v_previousactual=OLD.actualoutcome;
--		v_parentprogramid = (select parentid from programdetail where id=OLD.programid);
		select parentid into v_parentprogramid from programdetail where id=OLD.programid;
		select status into v_parentstatus from programdetail where id=v_parentprogramid;
		select id,actualoutcome into v_parentoutcomentid,v_parentcurrentactual from targetandoutcome where key=OLD.key and programid=v_parentprogramid;	
		
	elsif(TG_OP='INSERT') then
		v_newactual=NEW.actualoutcome;
--		v_parentprogramid = (select parentid from programdetail where id=NEW.programid);
		select parentid into v_parentprogramid from programdetail where id=NEW.programid;
		select status into v_parentstatus from programdetail where id=v_parentprogramid;
		select id,actualoutcome into v_parentoutcomentid,v_parentcurrentactual from targetandoutcome where key=NEW.key and programid=v_parentprogramid;
		
	else
		v_newactual=NEW.actualoutcome;
		v_previousactual=OLD.actualoutcome;
--		v_parentprogramid = (select parentid from programdetail where id=OLD.programid);
		select parentid into v_parentprogramid from programdetail where id=NEW.programid;
		select status into v_parentstatus from programdetail where id=v_parentprogramid;
		select id,actualoutcome into v_parentoutcomentid,v_parentcurrentactual from targetandoutcome where key=NEW.key and programid=v_parentprogramid;
	
	end if;
	
	v_newactual=COALESCE(v_newactual,0.0);
	v_previousactual = COALESCE(v_previousactual,0.0);
	
	--only update targetsandoutcomes of programs that are still open (!closed)
	IF((v_parentstatus is null or v_parentstatus!='CLOSED') and (v_parentoutcomentid is not null) and  (COALESCE(v_newactual,0) != COALESCE(v_previousactual,0))) THEN
		--RAISE INFO 'parentoutcomeid=%, actual=%, previousactual=% ', v_parentoutcomentid, v_newactual, v_previousactual;
		--calculate new allocation
		v_parentcurrentactual=COALESCE(v_parentcurrentactual,0.0) + COALESCE(v_newactual,0.0) - COALESCE(v_previousactual,0.0);
		update targetandoutcome set actualoutcome=v_parentcurrentactual where id=v_parentoutcomentid;
	END IF;
	
	if(TG_OP='DELETE') then
    	RETURN OLD;
    else
    	RETURN NEW;
   end if;
END;
$savetargets$ LANGUAGE plpgsql;

CREATE TRIGGER savetargets BEFORE INSERT OR UPDATE OR DELETE ON targetandoutcome
    FOR EACH ROW EXECUTE PROCEDURE func_savetargets();
