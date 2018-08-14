
WITH valOperacao AS (
SELECT SUM(VL_PRLA_OPRO) AS valOperacao
FROM TSGF_VSAO_CNRL_RSCO
WHERE CG_CNTO IN (
    SELECT CG_CNTO
    FROM TSGF_VSAO_CNRL_RSCO
    WHERE DT_CNBL_MVTO = :dataContabil
    AND CG_PRTO_FNRO IN (:codigoProdutos)
)),

qtdDiasCorridos AS (
SELECT  
    (SELECT MAX(DT_VNTO_PRLA)
    FROM TSGF_VSAO_CNRL_RSCO_FCTO
    WHERE DT_CNBL_MVTO = :dataContabil
    AND CG_PRTO_FNRO IN (:codigoProdutos)
) - :dataContabil AS qtdDiasCorridos from dual),

iof AS (
SELECT SUM(agpr.VL_MVTO_FNRO) as iof
FROM TSGF_MVTO_GSTR_FNRO agpr
JOIN TSGF_PRTO_CPSC_OPRO compo on compo.CG_EVTO_MVCO_FNRA = agpr.CG_EVTO_MVCO_FNRA
where agpr.CG_AGPR in (
    SELECT distinct mov.CG_AGPR
    FROM TSGF_MVTO_GSTR_FNRO mov
    JOIN TSGF_VSAO_CNRL_RSCO risco ON risco.CG_MVTO_GSTR_FNRO_ERDA = mov.CG_MVTO_GSTR_FNRO
    WHERE risco.DT_CNBL_MVTO = :dataContabil
    AND risco.CG_PRTO_FNRO IN (:codigoProdutos)
    )
and compo.CG_TIPO_EVTO_CPSC_OPRO in 3),

valEncargoOperacional as (
SELECT SUM(agpr.VL_MVTO_FNRO) as valEncargoOperacional
FROM TSGF_MVTO_GSTR_FNRO agpr
JOIN TSGF_PRTO_CPSC_OPRO compo on compo.CG_EVTO_MVCO_FNRA = agpr.CG_EVTO_MVCO_FNRA
where agpr.CG_AGPR in (
    SELECT distinct mov.CG_AGPR
    FROM TSGF_MVTO_GSTR_FNRO mov
    JOIN TSGF_VSAO_CNRL_RSCO risco ON risco.CG_MVTO_GSTR_FNRO_ERDA = mov.CG_MVTO_GSTR_FNRO
    WHERE risco.DT_CNBL_MVTO = :dataContabil
    AND risco.CG_PRTO_FNRO IN (:codigoProdutos)
    )
and compo.CG_TIPO_EVTO_CPSC_OPRO in 22),

txMedJuros as (
select SUM(F.TX_ANUL_OPRO * C.VL_PRLA_OPRO) / NULLIF(SUM(TX_ANUL_OPRO), 1) as txMedJuros
FROM TSGF_VSAO_CNRL_RSCO C
JOIN TSGF_VSAO_CNRL_RSCO_FCTO F ON C.CG_CLTE = F.CG_CLTE
    AND C.CG_CNTO = F.CG_CNTO
    AND C.CG_PRTO_FNRO = F.CG_PRTO_FNRO
    AND C.DT_VNTO_PRLA = F.DT_VNTO_PRLA
    AND F.DT_MVTO_OPRO = (
        SELECT MAX(f1.DT_MVTO_OPRO)
        FROM TSGF_VSAO_CNRL_RSCO_FCTO f1
        WHERE C.cg_cnto = f1.cg_cnto
        AND C.cg_prto_fnro = f1.cg_prto_fnro
        AND C.dt_vnto_prla = f1.dt_vnto_prla
        and C.NU_PRLA = f1.NU_PRLA
    )
WHERE C.DT_CNBL_MVTO = :dataContabil
AND c.CG_PRTO_FNRO IN (:codigoProdutos)
)

----------------------

SELECT distinct
    :dataContabil,

    -- 1 txMedJuros
    txMedJuros,
    
    -- 2 txMedEncFiscais
    (POWER( iof / valOperacao + 1, 360 / 
        (SELECT SUM(VL_PRLA_OPRO *
            (CASE WHEN CG_PRTO_FNRO IN (9, 50)
                THEN 30
                ELSE qtdDiasCorridos
            END))
        FROM TSGF_VSAO_CNRL_RSCO
        WHERE CG_CNTO IN (
            SELECT CG_CNTO
            FROM TSGF_VSAO_CNRL_RSCO_FCTO
            WHERE DT_CNBL_MVTO = :dataContabil
            AND CG_PRTO_FNRO IN (:codigoProdutos)
        )) / valOperacao
    ) - 1) * 100 as txMedEncFiscais,
    
    -- 3 txMedEncOperacionais
    (POWER( valEncargoOperacional / valOperacao + 1, 360 / 
        (SELECT SUM(VL_PRLA_OPRO *
            (CASE WHEN CG_PRTO_FNRO IN (9, 50)
                THEN 30
                ELSE qtdDiasCorridos
            END))
        FROM TSGF_VSAO_CNRL_RSCO
        WHERE CG_CNTO IN (
            SELECT CG_CNTO
            FROM TSGF_VSAO_CNRL_RSCO_FCTO
            WHERE DT_CNBL_MVTO = :dataContabil
            AND CG_PRTO_FNRO IN (:codigoProdutos)
        )) / valOperacao
    ) - 1) * 100 as txMedEncOperacionais,
    
    -- 4 txMinima
   (CASE WHEN 
        txMedJuros 
        <
        (SELECT nvl(MIN(F.TX_MNMO_MDIO), 0)
        FROM TSGF_TAXA_MDIO P
        JOIN TSGF_TAXA_MDIO_DTHE F
        ON P.SQ_TAXA_MDIO_DTHE = F.SQ_TAXA_MDIO_DTHE
        AND F.DT_TRNI_VGCA_TAXA_MDIO_DTHE IS NULL
        WHERE P.IN_ATVO_TAXA_MDIO = 1
        AND P.CG_PRTO_FNRO IN(67)
        GROUP BY P.CG_PRMT_CTRA_CART) 
    THEN 
        (SELECT nvl(MIN(vsao.TX_ANUL_OPRO), 0) 
        FROM TSGF_VSAO_CNRL_RSCO_FCTO vsao
        WHERE vsao.CG_PRTO_FNRO IN(:codigoProdutos)
        )
    ELSE
        (SELECT nvl(MIN(F.TX_MNMO_MDIO),0) 
        FROM TSGF_TAXA_MDIO P 
        JOIN TSGF_TAXA_MDIO_DTHE F ON P.SQ_TAXA_MDIO_DTHE = F.SQ_TAXA_MDIO_DTHE AND F.DT_TRNI_VGCA_TAXA_MDIO_DTHE IS NULL
        WHERE P.IN_ATVO_TAXA_MDIO = 1 
        AND P.CG_PRTO_FNRO IN (:codigoProdutos)
        GROUP BY P.CG_PRMT_CTRA_CART)
    END) as txMinima,
    
    -- 5 txMaxima
     (CASE WHEN 
        txMedJuros 
        >
        (SELECT nvl(MAX(F.TX_MXMO_MDIO), 0)
        FROM TSGF_TAXA_MDIO P
        JOIN TSGF_TAXA_MDIO_DTHE F
        ON P.SQ_TAXA_MDIO_DTHE = F.SQ_TAXA_MDIO_DTHE
        AND F.DT_TRNI_VGCA_TAXA_MDIO_DTHE IS NULL
        WHERE P.IN_ATVO_TAXA_MDIO = 1
        AND P.CG_PRTO_FNRO IN(67)
        GROUP BY P.CG_PRMT_CTRA_CART) 
    THEN 
        (SELECT nvl(MAX(vsao.TX_ANUL_OPRO), 0) 
        FROM TSGF_VSAO_CNRL_RSCO_FCTO vsao
        WHERE vsao.CG_PRTO_FNRO IN(:codigoProdutos)
        )
    ELSE
        (SELECT nvl(MAX(F.TX_MXMO_MDIO),0) 
        FROM TSGF_TAXA_MDIO P 
        JOIN TSGF_TAXA_MDIO_DTHE F ON P.SQ_TAXA_MDIO_DTHE = F.SQ_TAXA_MDIO_DTHE AND F.DT_TRNI_VGCA_TAXA_MDIO_DTHE IS NULL
        WHERE P.IN_ATVO_TAXA_MDIO = 1 
        AND P.CG_PRTO_FNRO IN (:codigoProdutos)
        GROUP BY P.CG_PRMT_CTRA_CART)
    END) as txMaxima, 

    -- 6 vlrConcessoes
    (SELECT SUM(VL_PRLA_FTRO) / 1000
    FROM TSGF_VSAO_CNRL_RSCO_FCTO
    WHERE CG_CNTO IN (
        SELECT CG_CNTO contratosDia
        FROM TSGF_VSAO_CNRL_RSCO
        WHERE DT_CNBL_MVTO = :dataContabil
        AND CG_PRTO_FNRO IN (:codigoProdutos)
    )
    GROUP BY DT_CNBL_MVTO) as vlrConcessoes,
    
    -- 7 przDecMedConcessoes
    (CASE WHEN CG_PRTO_FNRO IN (9, 50) THEN
        30
    ELSE
        (SELECT
            :dataContabil - 
            (SELECT LEAST(:dataContabil, MAX(DT_VNTO_PRLA)) FROM TSGF_VSAO_CNRL_RSCO_FCTO
            WHERE DT_CNBL_MVTO = :dataContabil AND p.CG_CNTO = CG_CNTO)
            --AND CG_PRTO_FNRO IN (:codigoProdutos))
        FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO = :dataContabil
        AND CG_PRTO_FNRO IN (:codigoProdutos)
        GROUP BY DT_CNBL_MVTO)
    END) as przDecMedConcessoes,
    
    (SELECT COUNT(*) FROM TSGF_VSAO_CNRL_RSCO_FCTO fcto
       JOIN TSGF_VSAO_CNRL_RSCO v ON  fcto.CG_CLTE = v.CG_CLTE
                                  AND fcto.CG_CNTO = v.CG_CNTO
                                  AND fcto.CG_PRTO_FNRO = v.CG_PRTO_FNRO
                                  AND fcto.DT_VNTO_PRLA = v.DT_VNTO_PRLA
       WHERE v.DT_BAXA_PRLA IS NULL
       AND v.DT_VNTO_PRLA =  :dataContabil
   ) AS qtdNovContratosRotativo,
   
   -- 8 qtdNovContratos 
    (SELECT COUNT(*) 
    FROM  TSGF_VSAO_CNRL_RSCO v 
    WHERE v.DT_BAXA_PRLA IS NULL
    AND v.DT_VNTO_PRLA =  :dataContabil
    AND v.CG_TIPO_CRCR = 6 /* Define o tipo de cartão, deve ser parametrizado conforme modalidade.*/
   ) AS qtdNovContratos,
    
    -- 9 sldCarAtiva 
    (SELECT SUM(saldo.VL_MVTO_EVTO)
        FROM TSGF_VSAO_SLDO_EVTO saldo
        JOIN TSGF_PRMT_LNHA_EVTO lnha
            ON saldo.CG_EVTO_MVCO_FNRA = lnha.CG_EVTO_MVCO_FNRA
        JOIN TSGF_PRMT_AGPR_LNHA agpr_lnha
            ON lnha.CG_PRMT_LNHA = agpr_lnha.CG_PRMT_LNHA
        JOIN TSGF_PRMT_RLRO_CART_AGPR cart_agpr
            ON agpr_lnha.CG_PRMT_AGPR = cart_agpr.CG_PRMT_AGPR
        WHERE saldo.DT_CNBL = :dataContabil
        AND cart_agpr.CG_RLRO_CART in (:codRelatorioCart) -- parametro
    ) AS sldCarAtiva

FROM TSGF_VSAO_CNRL_RSCO p
LEFT JOIN valOperacao ON  1 = 1
LEFT JOIN qtdDiasCorridos ON  1 = 1
LEFT JOIN txMedJuros on 1 = 1 
LEFT JOIN iof ON  1 = 1
LEFT JOIN valEncargoOperacional ON 1 = 1
WHERE p.DT_CNBL_MVTO = :dataContabil