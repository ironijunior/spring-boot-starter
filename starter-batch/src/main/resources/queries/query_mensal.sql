SELECT 
    'carCrdNaoMigrado' as nomeTag,

    -- 1 sldBaiPrejuizo
    ((SELECT SUM(f.VL_PRLA_FTRO - f.VL_SLDO_ATPO) 
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    LEFT JOIN TSGF_NVEL_RSCO_BACEN_DTHE n
    ON f.CG_NVEL_RSCO_BACEN = n.CG_NVEL_RSCO_BACEN
    WHERE f.DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND n.DS_NVEL_RSCO_BACEN = 'HH'
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) - (SELECT SUM(f.VL_PRLA_FTRO - f.VL_SLDO_ATPO) 
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    LEFT JOIN TSGF_NVEL_RSCO_BACEN_DTHE n
    ON f.CG_NVEL_RSCO_BACEN = n.CG_NVEL_RSCO_BACEN
    WHERE f.DT_CNBL_MVTO BETWEEN :dataInicialAnterior AND :dataFinalAnterior
    AND n.DS_NVEL_RSCO_BACEN = 'HH'
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    )) as sldBaiPrejuizo,
    
    -- 2  sldCarAte14
    (SELECT SUM(VL_PRLA_FTRO - VL_SLDO_ATPO) 
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal < 15
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as sldCarAte14,
    
    -- 2  sldCarAte60
    (SELECT SUM(VL_PRLA_FTRO - VL_SLDO_ATPO) 
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal BETWEEN 15 AND 60
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as sldCarAte60,
    
    -- 2  sldCarAte90
    (SELECT SUM(VL_PRLA_FTRO - VL_SLDO_ATPO) 
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal BETWEEN 61 AND 90
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as sldCarAte90,
    
    -- 2  sldCarMaior90
    (SELECT SUM(VL_PRLA_FTRO - VL_SLDO_ATPO) 
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal > 90
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as sldCarMaior90,
    
    -- 3 qtdConAte14
     (SELECT COUNT(DISTINCT(CG_CNTO))
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal < 15
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as qtdConAte14,
    
    -- 3 qtdConAte60
     (SELECT COUNT(DISTINCT(CG_CNTO))
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal BETWEEN 15 AND 60
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as qtdConAte60,
    
    -- 3 qtdConAte90
     (SELECT COUNT(DISTINCT(CG_CNTO))
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal BETWEEN 61 AND 90
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as qtdConAte90,
    
    -- 3 qtdConMaior90
     (SELECT COUNT(DISTINCT(CG_CNTO))
    FROM TSGF_VSAO_CNRL_RSCO_FCTO f
    WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND (SELECT MIN(DT_VNTO_PRLA) FROM TSGF_VSAO_CNRL_RSCO_FCTO
        WHERE DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
        AND f.CG_CNTO = CG_CNTO)
        - :dataFinal > 90
    AND CG_PRTO_FNRO IN (:codigoProdutos)
    ) as qtdConMaior90,
    
    -- 6 vlrConcessoes
    (SELECT SUM(cr.VL_PRLA_OPRO - fcto.VL_SLDO_ATPO) / 1000 /* Por ser um valor considerado em milhares de reais. */
    FROM TSGF_VSAO_CNRL_RSCO_FCTO fcto
    JOIN TSGF_VSAO_CNRL_RSCO cr 
        ON  fcto.CG_CLTE = cr.CG_CLTE
        AND fcto.CG_CNTO = cr.CG_CNTO
        AND fcto.CG_PRTO_FNRO = cr.CG_PRTO_FNRO
        AND fcto.DT_VNTO_PRLA = cr.DT_VNTO_PRLA
    WHERE cr.DT_CNBL_MVTO BETWEEN :dataInicial AND :dataFinal
    AND fcto.TX_JROS_OPRO IS NOT NULL 
    AND cr.CG_PRTO_FNRO IN (:codigoProdutos)
    ) AS vlrConcessoes,
    
    -- 7 sldCarAtiva 
    (SELECT SUM(saldo.VL_MVTO_EVTO)
        FROM TSGF_VSAO_SLDO_EVTO saldo
        JOIN TSGF_PRMT_LNHA_EVTO lnha
            ON saldo.CG_EVTO_MVCO_FNRA = lnha.CG_EVTO_MVCO_FNRA
        JOIN TSGF_PRMT_AGPR_LNHA agpr_lnha
            ON lnha.CG_PRMT_LNHA = agpr_lnha.CG_PRMT_LNHA
        JOIN TSGF_PRMT_RLRO_CART_AGPR cart_agpr
            ON agpr_lnha.CG_PRMT_AGPR = cart_agpr.CG_PRMT_AGPR
        WHERE saldo.DT_CNBL BETWEEN :dataInicial AND :dataFinal -- parâmetro
        AND cart_agpr.CG_RLRO_CART in (:codRelatorioCart) -- parâmetro
    ) AS sldCarAtiva

FROM dual