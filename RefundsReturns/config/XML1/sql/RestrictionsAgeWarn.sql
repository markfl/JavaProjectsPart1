SELECT WRSKU                                                           
 , WRSTORE AS STORE                                                                                                                          
     , CASE                                                            
         WHEN B.STRNUM in(1, 61, 98, 99)                               
           THEN TRIM(B.STPVST) || '_' || SUBSTR(B.STCITY,1,3) ||    
                right(REPEAT('0', 3) || b.strnum,3)   			
           ELSE TRIM(B.STPVST) || '_' || SUBSTR(B.STCITY,1,6)            
       END AS REGION                                                       
     , WRRTLAGE AS AGE                                               
FROM MRIRCW1P  A                                                       
     , TBLSTR B                                                        
  WHERE WRSKU = 999999999                                                 
AND B.STRNUM = A.WRSTORE AND A.WRBLOCK = 'A' AND A.WRACTION = 'A' AND B.STPOLL = 'Y'          
WITH UR                                                                                                