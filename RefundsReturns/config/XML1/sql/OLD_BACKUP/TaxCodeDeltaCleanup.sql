Select * From Table(GetTaxData('Y')) TaxData
  Where store <> 'SKIP'
  Order By groupcode, taxfee desc
