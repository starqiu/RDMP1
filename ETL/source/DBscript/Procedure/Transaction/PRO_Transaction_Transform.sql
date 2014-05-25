DELIMITER //
CREATE PROCEDURE PRO_Transaction_Transform()
BEGIN

  delete from tmail_firstseason where type =10;

  insert into tmail_firstseason
  select * from WRK_tmail_firstseason
  where visit_datetime < curdate();

  insert into EXP_Table 
  ( system,
    app,
    key1,
    key2,
    time,
    error_code,
    error_desc
  )
  select 'TaoBao','Transaction',id,'',now(),'ERROR_CODE_001','Visit time is not illegal.' 
  from TEMP_tmail_firstseason
  where visit_datetime > curdate();

END
//
DELIMITER ;
