-----------------------------------------------------------------
-- 	#155: Poder ordenar bústies per nom d'unitat organitzat
-----------------------------------------------------------------
-----------------------------------------------------------------
-- 	#165: Gestió i Sincronització de UO
-----------------------------------------------------------------
	
UPDATE IPA_BUSTIA SET UNITAT_ID = (SELECT ID FROM IPA_UNITAT_ORGANITZATIVA WHERE CODI = IPA_BUSTIA.UNITAT_CODI);
   
UPDATE IPA_REGLA SET UNITAT_ID = (SELECT ID FROM IPA_UNITAT_ORGANITZATIVA WHERE CODI = IPA_REGLA.UNITAT_CODI);
