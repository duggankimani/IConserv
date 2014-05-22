update programdetail set code = (case type
 when 'PROGRAM' then 'LWF1'||id
 when 'OBJECTIVE' then 'LWF2'||id
 when 'OUTCOME' then 'LWF3'||id
 when 'ACTIVITY' then 'LWF4'||id
 else 'LWF5'||id
 end)
where code is null;