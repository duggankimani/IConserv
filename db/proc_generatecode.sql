DROP FUNCTION IF EXISTS func_generatecode() cascade;

CREATE FUNCTION func_generatecode() RETURNS trigger AS $generateprogramcode$
DECLARE
  v_codeprefix varchar(20);
BEGIN

	if(NEW.code is null) then
		--RAISE INFO 'Inserting new record with ID = %', NEW.id;
		
		if(NEW.type='PROGRAM') then
			v_codeprefix='LWF1';
		elsif (NEW.type='OBJECTIVE') then
			v_codeprefix='LWF2';
		elsif (NEW.type='OUTCOME') then
			v_codeprefix='LWF3';
		elsif (NEW.type='ACTIVITY') then
			v_codeprefix='LWF4';
		else
			v_codeprefix='LWF5';
		end if;
		
		NEW.code = v_codeprefix || NEW.id;
	
	end if;
	
    RETURN NEW;
END;
$generateprogramcode$ LANGUAGE plpgsql;

CREATE TRIGGER generateprogramcode BEFORE INSERT ON programdetail
    FOR EACH ROW EXECUTE PROCEDURE func_generatecode();