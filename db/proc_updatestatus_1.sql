DROP FUNCTION IF EXISTS func_updateprogress() cascade;

CREATE FUNCTION func_updateprogress() RETURNS trigger AS $updateprogress$
DECLARE
	v_count numeric;
	v_childcount numeric;
BEGIN
	--Monitor Progress
	--Monitor Ratings
	--Monitor actual utilization
	v_childcount=0;
	
	if(TG_OP = 'DELETE') then
		if(OLD.parentId is not null and OLD.progress is not null) then
			--RAISE INFO 'UPDATEOP Id=%     NewProgress=%    OldProgress=%', NEW.id, NEW.progress, OLD.progress;
			select count(*) into v_count from programdetail where parentid=OLD.parentId and type!='OBJECTIVE';
			update programdetail set progress=progress-(OLD.progress)/v_count where id=OLD.parentId;
		end if;
		
	elsif(TG_OP = 'UPDATE') then
		select count(*) into v_childcount from programdetail where parentid=NEW.id and type!='OBJECTIVE';
	
		--update commitment
		if(NEW.parentId is not null 
			and (NEW.progress is not null and OLD.progress is not null) 
			and NEW.progress!=OLD.progress) then
			--RAISE INFO 'UPDATEOP Id=%     NewProgress=%    OldProgress=%', NEW.id, NEW.progress, OLD.progress;
			select count(*) into v_count from programdetail where parentid=NEW.parentId and type!='OBJECTIVE';
			update programdetail set progress=progress+(NEW.progress-OLD.progress)/v_count where id=NEW.parentId;
		end if;
		
	elsif(TG_OP = 'INSERT' and NEW.parentId is not null) then
		select count(*) into v_childcount from programdetail where parentid=NEW.id and type!='OBJECTIVE';
	
		--INSERT - recalculate Parent % completion
		--Progress of inserted item is zero (Update parent progress only)
		--RAISE INFO 'INSERTOP Id=%   NewProgress=%', NEW.id, NEW.progress;
		select count(*) into v_count from programdetail where parentid=NEW.parentId and type!='OBJECTIVE';
		update programdetail set progress=(progress*v_count)/(v_count+1) where id=NEW.parentId;
	end if;
	
	if(TG_OP = 'INSERT' and TG_OP = 'UPDATE') then
		if(v_childcount>0) then
			if(NEW.progress>99.9) then
				NEW.status='CLOSED';
			elsif(NEW.progress>0) then
				NEW.status='OPENED';
			else
				NEW.status='CREATED';
			end if;
		end if;
		RETURN NEW;
	else
	
		RETURN OLD;
	end if;	
END;
$updateprogress$ LANGUAGE plpgsql;

CREATE TRIGGER updateprogress BEFORE INSERT OR UPDATE OR DELETE ON programdetail
    FOR EACH ROW EXECUTE PROCEDURE func_updateprogress();	
