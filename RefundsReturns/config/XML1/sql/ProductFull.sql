Select
e.posflg0, e.posflg1, e.posflg2, c.ilcod, c.dspncd, a.inumbr, a.idept, a.isdept,
a.iclas, a.isclas, a.isort, a.idescr, a.istyln, a.islum, a.istype, a.iprmpt, a.irplcd,
vbprfu1r(99999, a.inumbr, a.istyln, a.idept, a.isdept, a.iclas, a.isclas) 
As xml1Full
From INVMST a 
Left Outer Join MRASDM1P c On a.inumbr = c.skunbr
Left Outer Join MRASDM2P e On a.inumbr = e.skunbr
Inner Join INVDPT f On a.idept  = f.idept And a.isdept = f.isdept And a.iclas = f.iclas And a.isclas = f.isclas
Where 
a.inumbr In (Select inumbr From INVUPC) And a.inumbr Not In (Select snumbr From INSMST)
Order By a.inumbr