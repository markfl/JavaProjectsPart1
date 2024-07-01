Select LGLKEY,LGDBFR,LGLFIL, LGDATE, LGPROG, LGLFLD, LGDAFT From
ALGLOG Where LGLFIL = 'INVMST' And LGLFLD = 'IATRB2' And LGDATE                 
In (Select DATCUR From EODDAT) Order By LGDATE, LGTIME                             
