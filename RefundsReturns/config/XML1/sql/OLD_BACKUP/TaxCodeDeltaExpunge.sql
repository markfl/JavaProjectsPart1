Select * From Table(GetTaxData('N')) TaxData
  Where store <> 'SKIP' and groupcode <> ' '
