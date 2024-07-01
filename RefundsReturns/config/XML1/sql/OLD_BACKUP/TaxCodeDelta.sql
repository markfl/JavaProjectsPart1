Select * From Table(GetTaxData('N')) TaxData
  Where store <> 'SKIP'
  Order By groupcode, taxfee desc
