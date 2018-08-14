select tag_prod_dthe.CG_PRTO_FNRO
from TSGF_TAG_PRTO_BACEN_DTHE tag_prod_dthe

-- joins para chegar no relacionamento arquivo x tag
join TSGF_ARVO_TAG_BACEN arq_tag
    on arq_tag.CG_ARVO_TAG_BACEN = tag_prod_dthe.CG_ARVO_TAG_BACEN
    and arq_tag.IN_ATVO_ARVO_TAG = 1
join TSGF_ARVO_TAG_BACEN_DTHE arq_tag_dthe
    on arq_tag_dthe.CG_ARVO_TAG_BACEN = arq_tag.CG_ARVO_TAG_BACEN
    and arq_tag_dthe.DH_TRNI_VGCA_ARVO_TAG_DTHE is null

-- joins para chegar no arquivo
join TSGF_ARVO_BACEN arq
    on arq.CG_ARVO_BACEN = arq_tag_dthe.CG_ARVO_BACEN
    and arq.IN_ATVO_ARVO_BACEN = 1
join TSGF_ARVO_BACEN_DTHE arq_dthe
    on arq_dthe.CG_ARVO_BACEN = arq.CG_ARVO_BACEN
    and arq_dthe.DH_TRNI_VGCA_ARVO_BACEN is null

-- joins para chegar na tag
join TSGF_TAG_BACEN tag
    on tag.CG_TAG_BANCEN = arq_tag_dthe.CG_TAG_BANCEN
    and tag.IN_ATVO_TAG_BACEN = 1
join TSGF_TAG_BACEN_DTHE tag_detalhe
    on tag_detalhe.CG_TAG_BANCEN = tag.CG_TAG_BANCEN
    and tag_detalhe.DH_TRNI_VGCA_TAG_BACEN is null
    
-- seleciona a tag do arquivo desejado
where arq_dthe.NO_ARVO_BACEN = :arquivo
and tag_detalhe.NO_TAG_BACEN = :tag