DROP FUNCTION IF EXISTS func_updateprogress() cascade;

CREATE FUNCTION func_updateprogress() RETURNS trigger AS $updateprogress$
DECLARE
	v_count numeric;
        v_programchildcount int;
BEGIN
	--Monitor Progress
	--Monitor Ratings
	--Monitor actual utilization
	if(TG_OP != 'DELETE') then
		select count(*) into v_count from programdetail where parentid=NEW.parentId;
	else
		select (count(*)-1) into v_count from programdetail where parentid=OLD.parentId;		
	end if;

	if(TG_OP!='DELETE') then
	    --User marked this program/activity/task as closed or reopened it
           if(NEW.status='CLOSED') then
		NEW.progress=100.00;
           elsif(TG_OP='UPDATE' and (NEW.status='CLOSED' or OLD.status='CLOSED') and (NEW.status != OLD.status) ) then
               --user reopened a previously closed task
	       v_programchildcount=(select count(*) from programdetail where parentid=NEW.id);
	       if(v_programchildcount>0) then
                  select sum(progress)/v_programchildcount into NEW.progress from programdetail where parentid=NEW.id;
               else
                  NEW.progress=0.0;
	       end if;
           end if;
        end if;

	if(TG_OP = 'UPDATE') then
		--update commitment
		if(NEW.status is not null and (OLD.status is null or OLD.status!='CLOSED') and NEW.status='CLOSED' and NEW.datecompleted is null) then
			NEW.datecompleted=now();		
		end if;

		if(NEW.parentId is not null 
			and (NEW.progress is not null and OLD.progress is not null) 
			and NEW.progress!=OLD.progress) then
			--RAISE INFO 'UPDATEOP Id=%     NewProgress=%    OldProgress=%', NEW.id, NEW.progress, OLD.progress;
			update programdetail set progress=progress+(NEW.progress-OLD.progress)/v_count where id=NEW.parentId and (status is null or status!='CLOSED');
		end if;
		
	elsif(TG_OP = 'INSERT' and NEW.parentId is not null) then
		--INSERT - recalculate Parent % completion
		--Progress of inserted item is zero (Update parent progress only)
		--RAISE INFO 'INSERTOP Id=%   NewProgress=%', NEW.id, NEW.progress;
		if(NEW.status is not null and NEW.status='CLOSED' and NEW.datecompleted is null) then
			NEW.datecompleted=now();		
		end if;

		update programdetail set progress=((progress*v_count)+COALESCE(NEW.progress,0))/(v_count+1) where id=NEW.parentId and (status is null or status!='CLOSED');
		
	elsif(TG_OP='DELETE') then
		--Deleting	
		
		if(OLD.parentId is not null and OLD.progress is not null) then
			

			--RAISE INFO 'DELETOP Id=%   OldProgress=% parentChildCount=%', OLD.id, OLD.progress,v_count;
			
			if(v_count!=0) then
				update programdetail set progress=progress-(OLD.progress)/v_count where id=OLD.parentId and (status is null or status!='CLOSED');
			else
				update programdetail set progress=0 where id=OLD.parentId and (status is null or status!='CLOSED');
			end if;
		end if;
		
	end if;
	
	if(TG_OP != 'DELETE') then
		RETURN NEW;
	else
		RETURN OLD;
	end if;
	
	
END;
$updateprogress$ LANGUAGE plpgsql;

CREATE TRIGGER updateprogress BEFORE INSERT OR UPDATE OR DELETE ON programdetail
    FOR EACH ROW EXECUTE PROCEDURE func_updateprogress();	
