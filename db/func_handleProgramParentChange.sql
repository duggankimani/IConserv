--DROP TRIGGER IF EXISTS handleparentchange ON programfund cascade;
DROP FUNCTION IF EXISTS func_handleProgramParentChange() cascade;

CREATE FUNCTION func_handleProgramParentChange() RETURNS trigger AS $handleparentchange$
DECLARE
  v_parentstatus varchar(20);
  v_fundids bigint[];
  v_fundid bigint;
  v_targetnames varchar(255)[];
  v_target varchar(255);
  v_childallocation double precision;
  v_childactual double precision;
  v_childcommitedamount double precision;
  v_childoutcome double precision;
  v_count int;
BEGIN

  if(NEW.parentId is not null and NEW.parentId!=OLD.parentId) then
	select status into v_parentstatus from programdetail where id=OLD.parentid;

      if(v_parentstatus!='CLOSED') then	

	v_fundids:=array(select fundid from programfund where programid=OLD.parentId);

	if(v_fundids is not null) then
          foreach v_fundid in ARRAY v_fundids
	  loop
		select allocatedAmount,actualAmount,commitedAmount into v_childallocation,v_childactual,v_childcommitedamount from programfund where programid=OLD.id and 			fundid=v_fundid; --amounts from child

		--reduce programfunds of the parent by the amount of the removed child
		update programfund set allocatedAmount=COALESCE(allocatedAmount,0.0) - COALESCE(v_childallocation,0.0),
        	actualAmount=COALESCE(actualAmount,0.0)- COALESCE(v_childactual,0.0),
		commitedamount=COALESCE(commitedamount,0.0)- COALESCE(v_childcommitedamount,0.0)
		where programid=OLD.parentId and fundid=v_fundid;

		update programfund set allocatedAmount=COALESCE(allocatedAmount,0.0) + COALESCE(v_childallocation,0.0),
        	actualAmount=COALESCE(actualAmount,0.0)+ COALESCE(v_childactual,0.0),
		commitedamount=COALESCE(commitedamount,0.0)+ COALESCE(v_childcommitedamount,0.0)
		where programid=NEW.parentId and fundid=v_fundid;
	  end loop;
	end if;

	 v_targetnames:=array(select key from targetandoutcome where programid=OLD.id);
  	 if(v_targetnames is not null) then
		 foreach v_target in ARRAY v_targetnames
		  loop
			--RAISE INFO 'TargetAndOutcome target=%  detailid=%  parentid=%  previousparent=%', v_target, NEW.id, NEW.parentId, OLD.parentid;
			--remove targets aggregated into old.parent
			select actualoutcome into v_childoutcome from targetandoutcome where programid=OLD.id and key=v_target; --amounts from child
			update targetandoutcome set actualoutcome=COALESCE(actualoutcome,0.0) - COALESCE(v_childoutcome,0.0) where programid=OLD.parentId and key=v_target;
			update targetandoutcome set actualoutcome=COALESCE(actualoutcome,0.0) + COALESCE(v_childoutcome,0.0) where programid=NEW.parentId and key=v_target;
		  end loop; 
	end if;
         
	--update old parent
         select count(*) into v_count from programdetail where parentid=OLD.parentId; 	
	 if(v_count!=0) then
		update programdetail set progress=progress-(OLD.progress)/v_count where id=OLD.parentId;
	 else
		update programdetail set progress=0 where id=OLD.parentId;
	 end if;

	--update new parent
         select count(*) into v_count from programdetail where parentid=NEW.parentId; 	
	 RAISE INFO 'V_count =% for parentid=%',v_count,NEW.parentId;
	 update programdetail set progress=((progress*v_count)+COALESCE(NEW.progress,0))/(v_count+1) where id=NEW.parentId;
	 --remove progress in old.parent attributable to this programdetail		
     end if;

  end if;

  RETURN NEW;
END;
$handleparentchange$ LANGUAGE plpgsql;

CREATE TRIGGER handleparentchange BEFORE UPDATE ON programdetail 
    FOR EACH ROW EXECUTE PROCEDURE func_handleProgramParentChange();
