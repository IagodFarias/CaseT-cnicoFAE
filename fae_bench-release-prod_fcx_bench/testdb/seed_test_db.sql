-- =============================================================================
-- seed_test_db.sql -- Dados minimos para rodar UM ciclo de calibracao completo
--                     com 20 medidores simulados no banco ufae_bench_test.
--
-- PRE-REQUISITO: rodar testdb/schema_test_db.sql antes (a aplicacao NAO cria as
--                tabelas sozinha -- ver cabecalho daquele arquivo).
--
-- Uso:  psql -U postgres -d ufae_bench_test -f testdb/schema_test_db.sql
--       psql -U postgres -d ufae_bench_test -f testdb/seed_test_db.sql
--
-- Convencao de IDs: este script usa IDs explicitos e, ao final, alinha TODAS as
-- sequences com setval(max_id). Como todos os @SequenceGenerator do projeto usam
-- allocationSize = 1, o proximo nextval devolve max_id + 1 -- sem colisao com o
-- gerador do Hibernate.
--
-- Valores fisicos (vazoes, limites, PID, temperaturas) sao PLAUSIVEIS, nao
-- medidos: servem para o ciclo fechar. Ajuste conforme a bancada real.
-- =============================================================================

BEGIN;

-- -----------------------------------------------------------------------------
-- 0. conversmodel.string_ip e UNIQUE no schema gerado pelo Hibernate.
--    Como os 20 medidores simulados vao rodar todos em 127.0.0.1 (um por porta),
--    a constraint precisa sair NESTE BANCO DE TESTE. O nome e auto-gerado
--    (UK_xxxxx), entao removemos por lookup no catalogo, sem depender do nome.
--
--    ALTERNATIVA sem mexer no schema: usar 127.0.0.1 .. 127.0.0.20 (um alias de
--    loopback por medidor). Foi testado neste ambiente e funciona (bind+connect
--    OK em 127.0.0.2 e 127.0.0.20). Se preferir, pule este bloco e troque os IPs
--    na secao 11.
-- -----------------------------------------------------------------------------
DO $$
DECLARE
    c_name text;
BEGIN
    FOR c_name IN
        SELECT con.conname
        FROM pg_constraint con
        JOIN pg_class rel ON rel.oid = con.conrelid
        JOIN pg_attribute att ON att.attrelid = rel.oid AND att.attnum = ANY (con.conkey)
        WHERE rel.relname = 'conversmodel'
          AND con.contype = 'u'
          AND att.attname = 'string_ip'
    LOOP
        EXECUTE format('ALTER TABLE conversmodel DROP CONSTRAINT %I', c_name);
        RAISE NOTICE 'Constraint UNIQUE removida de conversmodel.string_ip: %', c_name;
    END LOOP;
END
$$;

-- -----------------------------------------------------------------------------
-- 1. clientmodel  (sem FK)
-- -----------------------------------------------------------------------------
INSERT INTO clientmodel (id_client, string_razaoSocial, string_nomefantasia, string_cnpj,
                         string_inscricaoestadual, string_email, string_logradouro,
                         string_bairro, string_cep, string_uf, string_cidade,
                         string_telefone, string_celular, string_contato)
VALUES (1, 'Cliente de Teste LTDA', 'Cliente Teste', '00.000.000/0001-00',
        'ISENTO', 'teste@exemplo.local', 'Rua de Teste, 100',
        'Centro', '00000-000', 'MG', 'Santa Rita do Sapucai',
        '(35) 0000-0000', '(35) 90000-0000', 'Responsavel Teste');

-- -----------------------------------------------------------------------------
-- 2. firmwaremodel  (sem FK)
--    lob_firmwarefile e oid NOT NULL (large object). lo_from_bytea cria um LO
--    vazio so para satisfazer a constraint -- o arquivo nao e usado no ciclo.
--    A checagem de versao esta desativada no codigo (ServiceInterfaceMain usa
--    'if (true)' no lugar da comparacao), entao o valor aqui e apenas informativo.
-- -----------------------------------------------------------------------------
INSERT INTO firmwaremodel (id_firmware, string_description, string_firmwarename,
                           string_version, boolean_active, lob_firmwarefile)
VALUES (1, 'Firmware de teste FCX', 'fcx_test_fw', '3.0.0.0', true,
        lo_from_bytea(0, '\x00'::bytea));

-- -----------------------------------------------------------------------------
-- 3. valvemodel  (sem FK) -- tags conforme devicesTagReference.properties
--    ValveController.fillValvesMap() carrega TODAS por findAll() e indexa por tag;
--    openValveByTag falha se a tag nao estiver no mapa.
-- -----------------------------------------------------------------------------
INSERT INTO valvemodel (id_valve, string_tag, string_description, state) VALUES
 ( 1, 'XV1',  'Entrada de linha do reservatorio superior', 'CLOSED'),
 ( 2, 'XV2',  'Valvula de montante (upstream)',            'CLOSED'),
 ( 3, 'XV3',  'Valvula de jusante (downstream)',           'CLOSED'),
 ( 4, 'XV4',  'Purga de ar',                               'CLOSED'),
 ( 5, 'XV5',  'Reguladora de pressao',                     'CLOSED'),
 ( 6, 'XV6',  'Alimentacao do reservatorio inferior',      'CLOSED'),
 ( 7, 'XV7',  'Medidor de referencia DN32',                'CLOSED'),
 ( 8, 'XV8',  'Medidor de referencia DN08',                'CLOSED'),
 ( 9, 'XV9',  'Medidor de referencia DN02',                'CLOSED'),
 (10, 'XV10', 'Ramal 1',                                   'CLOSED'),
 (11, 'XV11', 'Ramal 2',                                   'CLOSED'),
 (12, 'XV12', 'Ramal 3',                                   'CLOSED'),
 (13, 'XV13', 'Ramal 4',                                   'CLOSED'),
 (14, 'XV14', 'Ramal 5',                                   'CLOSED'),
 (15, 'XV15', 'Ramal 6',                                   'CLOSED'),
 (16, 'XV16', 'Ramal 7',                                   'CLOSED'),
 (17, 'XV17', 'Ramal 8',                                   'CLOSED'),
 (18, 'XV18', 'Ramal 9',                                   'CLOSED'),
 (19, 'XV19', 'Ramal 10',                                  'CLOSED'),
 (20, 'XV20', 'Diversor da balanca',                       'CLOSED'),
 (21, 'XV21', 'Container da balanca',                      'CLOSED');

-- -----------------------------------------------------------------------------
-- 4. pumpmodel  (sem FK)
--    PumpController.definePump(flow) percorre findAll() e pega a PRIMEIRA bomba
--    com min <= flow < max. As faixas abaixo sao DISJUNTAS de proposito, para o
--    resultado nao depender da ordem de retorno do banco.
-- -----------------------------------------------------------------------------
INSERT INTO pumpmodel (id_pump, string_tag, string_description, double_load,
                       double_minflowrate, double_maxflowrate, state) VALUES
 (1, 'BP1',  'Bomba principal (vazoes medias)',        0.0,  150.0,  3200.0,  'OFF'),
 (2, 'BP2',  'Bomba principal (vazoes altas)',         0.0, 3200.0, 20000.0,  'OFF'),
 (3, 'BREP', 'Bomba do reservatorio (vazoes baixas)',  0.0,    0.0,   150.0,  'OFF');

-- -----------------------------------------------------------------------------
-- 5. refmetermodel  (FK opcional -> valvemodel)
--    RefMeterController.defineRefMeterByFlowRate(flow) usa:
--       flow < maxFlowRate AND flow >= minFlowRate
-- -----------------------------------------------------------------------------
INSERT INTO refmetermodel (id_refmeter, string_tag, string_description,
                           double_minflowrate, double_maxflowrate, double_freqmaxflowrate,
                           double_flowrate, double_volume, int_pulses, int_timeread,
                           flowStability, valve_id_valve) VALUES
 (1, 'HSCFTA', 'Medidor de referencia DN32', 1200.0, 20000.0, 1000.0, 0.0, 0.0, 0, 0, 'UNKNOWN', 7),
 (2, 'HSCFTM', 'Medidor de referencia DN08',  120.0,  1200.0, 1000.0, 0.0, 0.0, 0, 0, 'UNKNOWN', 8),
 (3, 'HSCFTB', 'Medidor de referencia DN02',    5.0,   120.0, 1000.0, 0.0, 0.0, 0, 0, 'UNKNOWN', 9);

-- -----------------------------------------------------------------------------
-- 6. ramalmodel  (FK opcional -> valvemodel)
--    BenchController.runFlowRate() usa flowRate.getRamal() sem checar null:
--    toda flowratemodel abaixo aponta para o RML1.
-- -----------------------------------------------------------------------------
INSERT INTO ramalmodel (id_ramal, string_tag, string_description,
                        double_minflowrate, double_maxflowrate, valve_id_valve) VALUES
 ( 1, 'RML1',  'Ramal 1',  0.0, 20000.0, 10),
 ( 2, 'RML2',  'Ramal 2',  0.0, 20000.0, 11),
 ( 3, 'RML3',  'Ramal 3',  0.0, 20000.0, 12),
 ( 4, 'RML4',  'Ramal 4',  0.0, 20000.0, 13),
 ( 5, 'RML5',  'Ramal 5',  0.0, 20000.0, 14),
 ( 6, 'RML6',  'Ramal 6',  0.0, 20000.0, 15),
 ( 7, 'RML7',  'Ramal 7',  0.0, 20000.0, 16),
 ( 8, 'RML8',  'Ramal 8',  0.0, 20000.0, 17),
 ( 9, 'RML9',  'Ramal 9',  0.0, 20000.0, 18),
 (10, 'RML10', 'Ramal 10', 0.0, 20000.0, 19);

-- -----------------------------------------------------------------------------
-- 7. waterlinemodel / sensores / balanca  (sem FK)
--    Os sensores sao lidos no polling do BenchDataController (~3,3 Hz).
-- -----------------------------------------------------------------------------
INSERT INTO waterlinemodel (id_line, string_tag, string_description, state)
VALUES (1, 'LINHAF', 'Linha de agua da bancada', 'CLOSED');

INSERT INTO tempsensormodel (id_temperature, string_tag, string_description, double_temperature) VALUES
 (1, 'TTLI',  'Temperatura da linha - montante',      20.0),
 (2, 'TTLO',  'Temperatura da linha - jusante',       20.0),
 (3, 'TTWL',  'Temperatura da balanca',               20.0),
 (4, 'TTRI',  'Temperatura do reservatorio inferior', 20.0),
 (5, 'TTRS',  'Temperatura do reservatorio superior', 20.0),
 (6, 'TTAMB', 'Temperatura ambiente',                 20.0);

INSERT INTO pressuresensormodel (id_pressure, string_tag, string_description, double_pressure) VALUES
 (1, 'PTLI',  'Pressao da linha - montante', 1.0),
 (2, 'PTLO',  'Pressao da linha - jusante',  1.0),
 (3, 'PTDIF', 'Pressao diferencial',         0.1),
 (4, 'PTBAR', 'Pressao barometrica',         0.92);

INSERT INTO levelsensormodel (id, string_tag, string_description, boolean_level) VALUES
 (1, 'SN1', 'Nivel baixo do reservatorio inferior', true),
 (2, 'SN2', 'Nivel medio do reservatorio inferior', true),
 (3, 'SN3', 'Nivel alto do reservatorio inferior',  true),
 (4, 'SN4', 'Nivel alto do reservatorio superior',  true);

INSERT INTO humiditysensormodel (id_humidity, string_tag, string_description, double_humidity)
VALUES (1, 'TRH', 'Umidade relativa ambiente', 55.0);

INSERT INTO scalemodel (id_scale, string_tag, string_description, double_weight)
VALUES (1, 'BAL1', 'Balanca gravimetrica', 0.0);

-- -----------------------------------------------------------------------------
-- 8. pidconfigmodel + flowratemodel
--    Ha um ciclo entre as duas tabelas:
--        flowratemodel.pidConfig_id_pidconfig -> pidconfigmodel
--        pidconfigmodel.flowRate_id_flowrate  -> flowratemodel
--    Ambas as FKs sao NULLABLE, entao inserimos o PID com flowrate NULL e
--    fechamos o ciclo com UPDATE no final desta secao.
--
--    Vazoes em L/h para um medidor tipo DN20 (Q3 = 2500 L/h, R = 125):
--        Q1 (minima)      =   20
--        Q2 (transicao)   =   32
--        Q3 (nominal)     = 2500
--        Q4 (maxima)      = 3125
--    Limites: +/- 5 % em torno do setpoint (banda de estabilizacao da bancada).
-- -----------------------------------------------------------------------------
INSERT INTO pidconfigmodel (id_pidconfig, double_proportionalkp, double_integrativetm,
                            double_derivativetv, double_error, flowRate_id_flowrate) VALUES
 (1, 0.80, 2.50, 0.00, 0.0, NULL),
 (2, 0.80, 2.50, 0.00, 0.0, NULL),
 (3, 1.00, 2.00, 0.00, 0.0, NULL),
 (4, 1.20, 1.50, 0.00, 0.0, NULL),
 (5, 1.50, 1.20, 0.00, 0.0, NULL),
 (6, 1.50, 1.20, 0.00, 0.0, NULL);

--     double_uncertanty_limit NAO PODE SER ZERO. No estado FLOWRATE ha um laco
--         do { ... } while (repeatFlowForUncertanty)
--     que repete a vazao enquanto
--         benchDataController.getLastCalcStdFLow() > flowRate.getUncertantyLimit()
--     e a checkbox "forcar cancelamento de repeticao" estiver marcada na tela.
--     O valor comparado e uma FRACAO (CalibrationService.relativeFlowStdDeviation):
--         |vazaoEsperada - media(vazaoLida)| / vazaoEsperada
--     Com limite 0, qualquer desvio maior que zero repete a vazao PARA SEMPRE --
--     o processo nunca sai de FLOWRATE. 0.02 = 2 %, com folga sobre o ruido de
--     0,4 % do simulador da BCI (bench.flow_noise em bci_config.json).
INSERT INTO flowratemodel (id_flowrate, double_flowrate, double_lowerlimit, double_upperlimit,
                           double_uncertanty_limit, string_description, bool_running, state,
                           pump_id_pump, refMeter_id_refmeter, ramal_id_ramal, pidConfig_id_pidconfig) VALUES
 (1,   20.0,    19.0,    21.0, 0.02, 'Q1 - vazao minima',    false, 'UNKNOWN', 3, 3, 1, 1),
 (2,   32.0,    30.4,    33.6, 0.02, 'Q2 - vazao transicao', false, 'UNKNOWN', 3, 3, 1, 2),
 (3,  100.0,    95.0,   105.0, 0.02, 'Ponto intermediario',  false, 'UNKNOWN', 3, 3, 1, 3),
 (4, 1250.0,  1187.5,  1312.5, 0.02, 'Ponto intermediario',  false, 'UNKNOWN', 1, 1, 1, 4),
 (5, 2500.0,  2375.0,  2625.0, 0.02, 'Q3 - vazao nominal',   false, 'UNKNOWN', 1, 1, 1, 5),
 (6, 3125.0,  2968.75, 3281.25, 0.02, 'Q4 - vazao maxima',   false, 'UNKNOWN', 1, 1, 1, 6);

UPDATE pidconfigmodel SET flowRate_id_flowrate = id_pidconfig WHERE id_pidconfig BETWEEN 1 AND 6;

-- -----------------------------------------------------------------------------
-- 9. metertypemodel  (4 FKs NOT NULL -> flowratemodel)
--    Dimensoes em METROS -- sao consumidas direto pelo calculo:
--      ultraSoundPathLengh e reductionDiameter vao no pacote CMD_LOAD_CALIB_PARAMETERS
--      e entram em estimatePathLength() / calculateMeterReductionArea().
-- -----------------------------------------------------------------------------
INSERT INTO metertypemodel (id_meter_type, string_meterModelDescription, string_meteringClass,
                            string_manufacturer, int_range, bool_is_radio_wmbus,
                            double_carcassLength, double_nominalDiameter, double_reductionDiameter,
                            double_ultraSoundPathLengh, string_inmetroQnId, string_codProduto,
                            double_lowcutoff, double_highcutoff,
                            double_minflow_error_limit, double_nominalflow_error_limit,
                            double_maxflow_error_limit, double_transflow_error_limit,
                            id_minFlowrate, id_tansitionFlowrate, id_nominalflowrate, id_maxflowrate)
VALUES (1, 'FCX DN20 Q3=2500 (teste)', 'R125', 'FAE', 125, false,
        0.115, 0.020, 0.010,
        0.084, 'A', 'FCX-DN20-TESTE',
        0.0, 0.0,
        5.0, 2.0,
        2.0, 5.0,
        1, 2, 5, 6);

-- -----------------------------------------------------------------------------
-- 10. processconfigmodel + vinculo com as vazoes
--     Todos os NOT NULL preenchidos. long_zero_flow_time / time_out_flow_rate em ms.
-- -----------------------------------------------------------------------------
INSERT INTO processconfigmodel (id_processconfig, string_descricao,
                                int_init_calib_temp, long_zero_flow_time, bool_zero_flow_enabled,
                                int_flow_buff_size, int_window_size,
                                long_flow_stability_check_time, long_pre_flow_stabilization_time,
                                long_time_out_flow_rate,
                                double_line_pressure_pump_load, int_time_check_pressure_pump_load,
                                int_meter_download_constants_tries, int_meter_date_tries, int_meter_radio_tries,
                                int_trim_agc_max_tries,
                                bool_save_mat_data, bool_save_mat_data_reproved_meters,
                                bool_save_calib_lut_mat_data, bool_only_verification,
                                bool_chooseQaSamples, double_perc_to_verif)
VALUES (1, 'Configuracao de processo - banco de teste',
        20, 30000, true,
        45, 45,
        5000, 10000,
        120000,
        30.0, 10000,
        200, 5, 5,
        100,
        false, false,
        false, false,
        false, 10.0);

-- Vazoes de CALIBRACAO (usadas quando calibrationType = FULL_PROD).
-- O ProcessController ordena decrescente: 2500 -> 1250 -> 100 -> 20.
INSERT INTO processconfig_flowrate_calib (id_processconfig, id_flowrate) VALUES
 (1, 5), (1, 4), (1, 3), (1, 1);

-- Vazoes de VERIFICACAO (Q3, Q2, Q1).
INSERT INTO processconfig_flowrate_verif (id_processconfig, id_flowrate) VALUES
 (1, 5), (1, 2), (1, 1);

-- -----------------------------------------------------------------------------
-- 11. conversmodel -- os 20 medidores
--     ATENCAO: int_port 2051..2070 = uma porta por medidor simulado.
--     Se voce optou por NAO remover a constraint UNIQUE da secao 0, troque
--     string_ip por '127.0.0.1' .. '127.0.0.20' (verificado: funciona no Windows).
-- -----------------------------------------------------------------------------
INSERT INTO conversmodel (id_conversor, string_name, string_ip, int_port, hex_color, bool_enabled) VALUES
 ( 1, 'CONVERSOR1',  '127.0.0.1', 2051, '#E6194B', true),
 ( 2, 'CONVERSOR2',  '127.0.0.1', 2052, '#3CB44B', true),
 ( 3, 'CONVERSOR3',  '127.0.0.1', 2053, '#FFE119', true),
 ( 4, 'CONVERSOR4',  '127.0.0.1', 2054, '#4363D8', true),
 ( 5, 'CONVERSOR5',  '127.0.0.1', 2055, '#F58231', true),
 ( 6, 'CONVERSOR6',  '127.0.0.1', 2056, '#911EB4', true),
 ( 7, 'CONVERSOR7',  '127.0.0.1', 2057, '#42D4F4', true),
 ( 8, 'CONVERSOR8',  '127.0.0.1', 2058, '#F032E6', true),
 ( 9, 'CONVERSOR9',  '127.0.0.1', 2059, '#BFEF45', true),
 (10, 'CONVERSOR10', '127.0.0.1', 2060, '#FABED4', true),
 (11, 'CONVERSOR11', '127.0.0.1', 2061, '#469990', true),
 (12, 'CONVERSOR12', '127.0.0.1', 2062, '#DCBEFF', true),
 (13, 'CONVERSOR13', '127.0.0.1', 2063, '#9A6324', true),
 (14, 'CONVERSOR14', '127.0.0.1', 2064, '#FFFAC8', true),
 (15, 'CONVERSOR15', '127.0.0.1', 2065, '#800000', true),
 (16, 'CONVERSOR16', '127.0.0.1', 2066, '#AAFFC3', true),
 (17, 'CONVERSOR17', '127.0.0.1', 2067, '#808000', true),
 (18, 'CONVERSOR18', '127.0.0.1', 2068, '#FFD8B1', true),
 (19, 'CONVERSOR19', '127.0.0.1', 2069, '#000075', true),
 (20, 'CONVERSOR20', '127.0.0.1', 2070, '#A9A9A9', true);

-- -----------------------------------------------------------------------------
-- 12. batchmodel -- o lote a selecionar na tela
--     long_idxprocessconfig e uma FK "solta" (long simples, sem constraint):
--     BatchController.incBatchRunCount() faz
--         processConfigService.findById(batch.getIdxProcessConfig())
--     Se apontar para um id inexistente, o processo quebra logo no
--     INITIAL_RUN_CONFIGURATION. Precisa valer 1 (o processconfig acima).
-- -----------------------------------------------------------------------------
INSERT INTO batchmodel (id_batch, string_batchid, string_description,
                        int_nummeters, int_startserialSeq, int_lastassignedserial,
                        string_manufacturercods, date_yearofmanufacture, bool_finished,
                        long_idxprocessconfig, id_client, id_meter_type, id_firmware)
VALUES (1, 'LOTE-TESTE-001', 'Lote de teste contra o simulador',
        20, 1, -1,
        'F', DATE '2026-01-01', false,
        1, 1, 1, 1);

-- -----------------------------------------------------------------------------
-- 13. bateladamodel -- execucao inicial
--     int_runcount = 0 e proposital: incBatchRunCount() entra no ramo count==0,
--     atualiza esta linha para runCount=1 e vincula o processconfig. Assim
--     checkForBateladas() ja encontra uma batelada e nao cria outra.
-- -----------------------------------------------------------------------------
--     ATENCAO: os oito contadores int_reproved_meters_* sao NULLABLE no schema,
--     mas mapeiam para campos `int` PRIMITIVOS em BateladaModel. Se ficarem NULL,
--     o Hibernate quebra ao LER a linha:
--         PropertyAccessException: Null value was assigned to a property
--         [BateladaModel.reprovedMetersOnCalcConst] of primitive type
--     e o processo morre logo no INITIAL_RUN_CONFIGURATION
--     (BatchController.checkForBateladas -> getBatchLastBatelada).
--     Por isso todos vao explicitamente com 0. O mesmo cuidado vale para
--     qualquer coluna anulavel cujo campo Java seja tipo primitivo.
INSERT INTO bateladamodel (id_batelada, int_runcount, enum_calibration_type,
                           id_batch, id_process_config,
                           bool_isrework, initTime,
                           int_connected_meters, int_approved_meters,
                           int_reproved_meters_down_init_const,
                           int_reproved_meters_trim_agc,
                           int_reproved_meters_calc_zero_flow,
                           int_reproved_meters_down_zero_flow,
                           int_reproved_meters_calc_const,
                           int_reproved_meters_down_const,
                           int_reproved_meters_calc_errors,
                           int_reproved_meters_verif,
                           double_minflow_error_limit, double_nominalflow_error_limit,
                           double_maxflow_error_limit)
VALUES (1, 0, 'FULL_PROD',
        1, 1,
        false, NOW(),
        0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        5.0, 2.0, 2.0);

-- -----------------------------------------------------------------------------
-- 14. criticalpointmodel  -- OPCIONAL
--     So e lido quando calibrationType = ESTIMATED_CONST. Ver a nota do relatorio:
--     CriticalPointModel NAO esta registrada em DataBasePersistence.initialize(),
--     entao a consulta falha com "Unknown entity" mesmo com as linhas presentes.
--     Mantido aqui apenas para o dia em que o registro da entidade for corrigido.
--     Com FULL_PROD (o caso deste seed) estas linhas nao sao tocadas.
-- -----------------------------------------------------------------------------
INSERT INTO criticalpointmodel (id_criticalpoint, id_batch, id_flowrate) VALUES
 (1, 1, 5),
 (2, 1, 4),
 (3, 1, 1);

-- -----------------------------------------------------------------------------
-- 15. Alinhamento das sequences
--     allocationSize = 1 em todos os @SequenceGenerator do projeto, entao
--     setval(seq, max_id) faz o proximo nextval devolver max_id + 1.
--     As sequences sem linhas semeadas ficam intactas (start 1).
-- -----------------------------------------------------------------------------
SELECT setval('client_seq',              (SELECT COALESCE(MAX(id_client),      0) FROM clientmodel));
SELECT setval('firmware_seq',            (SELECT COALESCE(MAX(id_firmware),    0) FROM firmwaremodel));
SELECT setval('valvemodel_seq',          (SELECT COALESCE(MAX(id_valve),       0) FROM valvemodel));
SELECT setval('pumpmodel_seq',           (SELECT COALESCE(MAX(id_pump),        0) FROM pumpmodel));
SELECT setval('refmetermodel_seq',       (SELECT COALESCE(MAX(id_refmeter),    0) FROM refmetermodel));
SELECT setval('ramal_model_seq',         (SELECT COALESCE(MAX(id_ramal),       0) FROM ramalmodel));
SELECT setval('linemodel_seq',           (SELECT COALESCE(MAX(id_line),        0) FROM waterlinemodel));
SELECT setval('temperaturesens_seq',     (SELECT COALESCE(MAX(id_temperature), 0) FROM tempsensormodel));
SELECT setval('pressure_sensor_model_seq',(SELECT COALESCE(MAX(id_pressure),   0) FROM pressuresensormodel));
SELECT setval('seq_level_sensor_model',  (SELECT COALESCE(MAX(id),             0) FROM levelsensormodel));
SELECT setval('humidity_sensor_model_seq',(SELECT COALESCE(MAX(id_humidity),   0) FROM humiditysensormodel));
SELECT setval('scalemodel_seq',          (SELECT COALESCE(MAX(id_scale),       0) FROM scalemodel));
SELECT setval('pidconfigmodel_seq',      (SELECT COALESCE(MAX(id_pidconfig),   0) FROM pidconfigmodel));
SELECT setval('flowratemodel_seq',       (SELECT COALESCE(MAX(id_flowrate),    0) FROM flowratemodel));
SELECT setval('meter_type_seq',          (SELECT COALESCE(MAX(id_meter_type),  0) FROM metertypemodel));
SELECT setval('process_config_model_seq',(SELECT COALESCE(MAX(id_processconfig),0) FROM processconfigmodel));
SELECT setval('conversor_seq',           (SELECT COALESCE(MAX(id_conversor),   0) FROM conversmodel));
SELECT setval('batch_seq',               (SELECT COALESCE(MAX(id_batch),       0) FROM batchmodel));
SELECT setval('batelada_seq',            (SELECT COALESCE(MAX(id_batelada),    0) FROM bateladamodel));
SELECT setval('criticalpoint_seq',       (SELECT COALESCE(MAX(id_criticalpoint),0) FROM criticalpointmodel));

COMMIT;

-- =============================================================================
-- Validacao rapida (rode depois do COMMIT)
-- =============================================================================
-- SELECT string_name, string_ip, int_port, bool_enabled FROM conversmodel ORDER BY id_conversor;
-- SELECT b.string_batchid, b.long_idxprocessconfig, c.string_razaoSocial, mt.string_meterModelDescription
--   FROM batchmodel b
--   JOIN clientmodel c ON c.id_client = b.id_client
--   JOIN metertypemodel mt ON mt.id_meter_type = b.id_meter_type;
-- SELECT 'calib' AS tipo, f.double_flowrate FROM processconfig_flowrate_calib j
--   JOIN flowratemodel f ON f.id_flowrate = j.id_flowrate
-- UNION ALL
-- SELECT 'verif', f.double_flowrate FROM processconfig_flowrate_verif j
--   JOIN flowratemodel f ON f.id_flowrate = j.id_flowrate
-- ORDER BY 1, 2 DESC;
-- SELECT id_batelada, int_runcount, enum_calibration_type, id_batch, id_process_config FROM bateladamodel;
