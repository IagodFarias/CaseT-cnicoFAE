-- =============================================================================
-- schema_test_db.sql  -- DDL AUTORITATIVO do banco de teste (ufae_bench_test)
--
-- Gerado pelo proprio Hibernate (SchemaExport, dialeto PostgreSQL) a partir das
-- 41 entidades @Entity do projeto. NAO foi escrito a mao.
--
-- POR QUE ESTE ARQUIVO EXISTE:
-- DataBasePersistence.initialize() NAO define hibernate.hbm2ddl.auto e NAO chama
-- .configure(), ou seja, o conf/hibernate.cfg.xml nao e carregado em runtime.
-- Portanto a aplicacao NAO cria as tabelas sozinha. Rode este script ANTES do seed.
--
-- Ordem de uso:  schema_test_db.sql  ->  seed_test_db.sql
-- =============================================================================

create sequence batch_seq start 1 increment 1;
create sequence batchradioconfig_seq start 1 increment 1;
create sequence batelada_seq start 1 increment 1;
create sequence bench_data_seq start 1 increment 1;
create sequence calibconst_seq start 1 increment 1;
create sequence carcassbatch_seq start 1 increment 1;
create sequence client_seq start 1 increment 1;
create sequence conversor_seq start 1 increment 1;
create sequence criticalpoint_seq start 1 increment 1;
create sequence dimensional_seq start 1 increment 1;
create sequence dimensionmeasure_seq start 1 increment 1;
create sequence distancepoint_seq start 1 increment 1;
create sequence drawing_seq start 1 increment 1;
create sequence firmware_seq start 1 increment 1;
create sequence flowratemeanstd_seq start 1 increment 1;
create sequence flowratemodel_seq start 1 increment 1;
create sequence humidity_sensor_model_seq start 1 increment 1;
create sequence linemodel_seq start 1 increment 1;
create sequence logchangebatch_seq start 1 increment 1;
create sequence logchangeprocessbatch_seq start 1 increment 1;
create sequence meter_data_seq start 1 increment 1;
create sequence meter_seq start 1 increment 1;
create sequence meter_type_seq start 1 increment 1;
create sequence pidconfigmodel_seq start 1 increment 1;
create sequence pressure_sensor_model_seq start 1 increment 1;
create sequence process_config_model_seq start 1 increment 1;
create sequence pumpmodel_seq start 1 increment 1;
create sequence qaassured_seq start 1 increment 1;
create sequence quota_seq start 1 increment 1;
create sequence radioconfig_seq start 1 increment 1;
create sequence ramal_model_seq start 1 increment 1;
create sequence refmetermodel_seq start 1 increment 1;
create sequence scalemodel_seq start 1 increment 1;
create sequence seq_calc_fixed_const start 1 increment 1;
create sequence seq_level_sensor_model start 1 increment 1;
create sequence temperaturesens_seq start 1 increment 1;
create sequence valvemodel_seq start 1 increment 1;
create sequence verification_error_seq start 1 increment 1;
create sequence volumemodel_seq start 1 increment 1;
create sequence volumetricbench_seq start 1 increment 1;
create sequence volumetricerror_seq start 1 increment 1;

    create table batchmodel (
        id_batch int8 not null,
        string_batchid varchar(255) not null,
        string_description varchar(255),
        bool_finished boolean,
        long_idxprocessconfig int8,
        int_lastassignedserial int4,
        string_manufacturercods varchar(255) not null,
        int_nummeters int4 not null,
        int_startserialSeq int4 not null,
        date_yearofmanufacture date not null,
        id_client int8 not null,
        id_firmware int8,
        id_meter_type int8 not null,
        primary key (id_batch)
    );

    create table batchradiowmbusconfigmodel (
        id_batchradioconfig int8 not null,
        int_lastassignedserial int4 not null,
        string_manufacnumber varchar(255) not null,
        string_prodcod varchar(255) not null,
        int_startserialSeq int4 not null,
        short_transmitInterval int2 not null,
        string_versao varchar(255) not null,
        batchId int8,
        primary key (id_batchradioconfig)
    );

    create table bateladamodel (
        id_batelada int8 not null,
        int_approved_meters int4,
        enum_calibration_type varchar(255) not null,
        int_connected_meters int4,
        endTime timestamp,
        initTime timestamp,
        bool_isrework boolean,
        double_maxflow_error_limit float8,
        double_minflow_error_limit float8,
        double_nominalflow_error_limit float8,
        int_reproved_meters_calc_const int4,
        int_reproved_meters_calc_errors int4,
        int_reproved_meters_calc_zero_flow int4,
        int_reproved_meters_down_const int4,
        int_reproved_meters_down_init_const int4,
        int_reproved_meters_down_zero_flow int4,
        int_reproved_meters_trim_agc int4,
        int_reproved_meters_verif int4,
        int_runcount int4 not null,
        id_batch int8 not null,
        id_process_config int8,
        primary key (id_batelada)
    );

    create table benchdatamodel (
        id_bench_data int8 not null,
        int_counter int4 not null,
        double_expected_flowrate float8 not null,
        double_flowrate float8 not null,
        double_pressure float8 not null,
        double_pulseCalcFlowrate float8,
        date_readat timestamp not null,
        double_temperature_li float8 not null,
        double_temperature_lo float8 not null,
        int_timeval int4 not null,
        double_volume float8 not null,
        id_batch int8,
        id_refmeter int8,
        primary key (id_bench_data)
    );

    create table calculatedfixedconstmodel (
        id_calculatedfixedconstmodel int8 not null,
        string_descricao varchar(255),
        double_flowrate float8,
        double_k float8,
        double_re float8,
        double_temp float8,
        primary key (id_calculatedfixedconstmodel)
    );

    create table calibconstantsmodel (
        id_calib_const int8 not null,
        double_Avgflowrate float8,
        double_Avgpressure float8,
        double_avgtemperature float8,
        calcDate timestamp,
        double_constantk float8,
        double_flowrate float8,
        double_reynolds float8,
        id_meter int8 not null,
        primary key (id_calib_const)
    );

    create table carcassbatchmodel (
        id_carcassbatch int8 not null,
        date_dateofmanufacture timestamp,
        string_description varchar(255),
        int_numbercarcass int4 not null,
        id_drawing int8,
        primary key (id_carcassbatch)
    );

    create table clientmodel (
        id_client int8 not null,
        string_bairro varchar(255),
        string_celular varchar(255),
        string_cep varchar(255),
        string_cidade varchar(255),
        string_cnpj varchar(255),
        string_contato varchar(255),
        string_email varchar(255),
        string_inscricaoestadual varchar(255),
        string_logradouro varchar(255),
        string_nomefantasia varchar(255),
        string_razaoSocial varchar(255),
        string_telefone varchar(255),
        string_uf varchar(255),
        primary key (id_client)
    );

    create table conversmodel (
        id_conversor int8 not null,
        hex_color varchar(255) not null,
        bool_enabled boolean,
        string_ip varchar(255) not null,
        string_name varchar(255) not null,
        int_port int4 not null,
        primary key (id_conversor)
    );

    create table criticalpointmodel (
        id_criticalpoint int8 not null,
        id_batch int8 not null,
        id_flowrate int8 not null,
        primary key (id_criticalpoint)
    );

    create table dimensionalmodel (
        id_dimensional int8 not null,
        date_dateofsampling timestamp,
        string_description varchar(255),
        id_carcassBatch int8,
        primary key (id_dimensional)
    );

    create table dimensionmeasuremodel (
        id_dimensionmeasure int8 not null,
        date_measurementdate timestamp,
        double_value float8 not null,
        id_dimensional int8,
        id_quota int8,
        primary key (id_dimensionmeasure)
    );

    create table distancepointsmodel (
        id_distancepoint int8 not null,
        double_flowratefrom float8 not null,
        double_flowrateto float8 not null,
        double_xdistance float8 not null,
        double_ydistance float8 not null,
        id_batch int8 not null,
        primary key (id_distancepoint)
    );

    create table drawingmodel (
        id_drawing int8 not null,
        string_description varchar(255),
        enum_drawing varchar(255),
        lob_imagefile oid not null,
        int_numberquotas int4 not null,
        lob_pdffile oid not null,
        primary key (id_drawing)
    );

    create table firmwaremodel (
        id_firmware int8 not null,
        string_description varchar(255) not null,
        lob_firmwarefile oid not null,
        boolean_active boolean,
        string_firmwarename varchar(255) not null,
        string_version varchar(255) not null,
        primary key (id_firmware)
    );

    create table flowratemeanstdmodel (
        id_flowratemeanstd int8 not null,
        double_avg_constant_k float8 not null,
        double_avg_reynolds float8 not null,
        double_avg_temperature float8 not null,
        double_flowrate float8 not null,
        bool_is_approved boolean not null,
        double_std_constant_k float8 not null,
        id_batch int8 not null,
        id_batelada_end int8 not null,
        id_batelada_init int8 not null,
        primary key (id_flowratemeanstd)
    );

    create table flowratemodel (
        id_flowrate int8 not null,
        string_description varchar(255),
        double_flowrate float8 not null,
        flowRateReadTime date,
        double_lowerlimit float8 not null,
        bool_running boolean,
        state varchar(255),
        double_uncertanty_limit float8,
        double_upperlimit float8 not null,
        pidConfig_id_pidconfig int8,
        pump_id_pump int8,
        ramal_id_ramal int8,
        refMeter_id_refmeter int8,
        primary key (id_flowrate)
    );

    create table humiditysensormodel (
        id_humidity int8 not null,
        string_description varchar(255),
        double_humidity float8 not null,
        readTime timestamp,
        string_tag varchar(255) not null,
        primary key (id_humidity)
    );

    create table levelsensormodel (
        id int8 not null,
        string_description varchar(255),
        boolean_level boolean not null,
        string_tag varchar(255) not null,
        primary key (id)
    );

    create table logchangebatchmodel (
        id_logchangebatch int8 not null,
        date_dateofchange timestamp not null,
        int_meterufoid int4 not null,
        string_serialnumber varchar(255),
        id_batch_new int8 not null,
        id_batch_old int8 not null,
        id_meter int8 not null,
        id_meter_type_new int8 not null,
        id_meter_type_old int8 not null,
        primary key (id_logchangebatch)
    );

    create table logchangeprocessbatchmodel (
        id_logchangeprocessbatch int8 not null,
        enum_calibration_type varchar(255),
        bool_usebatelada boolean not null,
        id_batch int8 not null,
        id_batelada_end int8,
        id_batelada_init int8,
        primary key (id_logchangeprocessbatch)
    );

    create table meterdatamodel (
        meter_data_seq int8 not null,
        double_accrevolume float8 not null,
        double_accrevolumerev float8 not null,
        short_agstatus int2 not null,
        double_expected_flowrate float8 not null,
        short_gm1 int2 not null,
        double_hfc float8 not null,
        string_ip varchar(255) not null,
        short_r1 int2 not null,
        date_readat timestamp not null,
        double_temperature float8 not null,
        double_ttrev float8 not null,
        double_ttstd float8 not null,
        double_vel float8 not null,
        double_velre float8 not null,
        double_vel_swcalc float8 not null,
        double_volflowre float8 not null,
        double_volre float8 not null,
        meter_id int8,
        primary key (meter_data_seq)
    );

    create table metermodel (
        id_meter int8 not null,
        date_dateofcalibration timestamp,
        date_dateofreplace timestamp,
        double_dsos float8,
        double_dzc float8,
        double_estimatedpathlength float8,
        bool_approvedagc boolean,
        bool_approvedverification boolean,
        bool_calculatedconstants boolean,
        bool_calculatederrors boolean,
        bool_calculatedzeroflow boolean,
        bool_commdown boolean,
        bool_deviationzeroflow boolean,
        bool_downloadedconstants boolean,
        bool_downloadedinitcontants boolean,
        bool_downloadedzeroflow boolean,
        bool_meter_replace_date_configured boolean,
        bool_meter_serial_number_configured boolean,
        bool_meter_system_date_configured boolean,
        bool_qa boolean,
        bool_wmbus_configured boolean,
        int_meterufoid int4,
        string_serialnumber varchar(255),
        id_batch int8,
        id_batelada int8,
        id_conversor int8,
        id_firmware int8,
        id_meter_type int8,
        primary key (id_meter)
    );

    create table metertypemodel (
        id_meter_type int8 not null,
        double_carcassLength float8,
        string_codProduto varchar(255),
        double_highcutoff float8,
        string_inmetroQnId varchar(255),
        bool_is_radio_wmbus boolean,
        double_lowcutoff float8,
        string_manufacturer varchar(255),
        double_maxflow_error_limit float8,
        string_meterModelDescription varchar(255),
        string_meteringClass varchar(255),
        double_minflow_error_limit float8,
        double_nominalDiameter float8,
        double_nominalflow_error_limit float8,
        int_range int4,
        double_reductionDiameter float8,
        double_transflow_error_limit float8,
        double_ultraSoundPathLengh float8,
        id_maxflowrate int8 not null,
        id_minFlowrate int8 not null,
        id_nominalflowrate int8 not null,
        id_tansitionFlowrate int8 not null,
        primary key (id_meter_type)
    );

    create table pidconfigmodel (
        id_pidconfig int8 not null,
        double_derivativetv float8,
        double_error float8,
        double_integrativetm float8,
        double_proportionalkp float8,
        flowRate_id_flowrate int8,
        primary key (id_pidconfig)
    );

    create table pressuresensormodel (
        id_pressure int8 not null,
        string_description varchar(255),
        double_pressure float8,
        string_tag varchar(255) not null,
        primary key (id_pressure)
    );

    create table processconfig_calculatedfixedconst (
        id_processconfig int8 not null,
        id_calculatedfixedconstmodel int8 not null,
        primary key (id_processconfig, id_calculatedfixedconstmodel)
    );

    create table processconfig_flowrate_calib (
        id_processconfig int8 not null,
        id_flowrate int8 not null,
        primary key (id_processconfig, id_flowrate)
    );

    create table processconfig_flowrate_verif (
        id_processconfig int8 not null,
        id_flowrate int8 not null,
        primary key (id_processconfig, id_flowrate)
    );

    create table processconfigmodel (
        id_processconfig int8 not null,
        bool_chooseQaSamples boolean,
        string_descricao varchar(255),
        int_flow_buff_size int4 not null,
        long_flow_stability_check_time int8 not null,
        int_init_calib_temp int4 not null,
        bool_only_verification boolean,
        bool_zero_flow_enabled boolean,
        double_line_pressure_pump_load float8 not null,
        int_meter_date_tries int4 not null,
        int_meter_download_constants_tries int4 not null,
        int_meter_radio_tries int4 not null,
        double_perc_to_verif float8,
        long_pre_flow_stabilization_time int8 not null,
        bool_save_calib_lut_mat_data boolean,
        bool_save_mat_data boolean,
        bool_save_mat_data_reproved_meters boolean,
        int_time_check_pressure_pump_load int4 not null,
        long_time_out_flow_rate int8 not null,
        int_trim_agc_max_tries int4 not null,
        int_window_size int4 not null,
        long_zero_flow_time int8 not null,
        primary key (id_processconfig)
    );

    create table pumpmodel (
        id_pump int8 not null,
        string_description varchar(255),
        double_load float8 not null,
        double_maxflowrate float8,
        double_minflowrate float8,
        state varchar(255),
        string_tag varchar(255) not null,
        primary key (id_pump)
    );

    create table qaassuredmodel (
        id_qaassured int8 not null,
        date_dateofreprove timestamp,
        enum_qaassuredstatus varchar(255) not null,
        string_serialnumbermeter varchar(255),
        string_serialnumberwmbus varchar(255),
        id_meter int8,
        primary key (id_qaassured)
    );

    create table quotamodel (
        id_quota int8 not null,
        string_description varchar(255),
        int_fieldlength int4,
        int_fieldposx int4,
        int_fieldposy int4,
        bool_isrequired boolean,
        double_lowerlimit float8,
        double_nominalvalue float8 not null,
        double_upperlimit float8,
        id_drawing int8,
        primary key (id_quota)
    );

    create table radiowmbusconfigmodel (
        id_radiowmbusconfigmodel int8 not null,
        int_transmitinterval int4,
        string_wmBusCryptoKey varchar(255),
        string_wmBusSerialNumber varchar(255),
        id_meter int8,
        primary key (id_radiowmbusconfigmodel)
    );

    create table ramalmodel (
        id_ramal int8 not null,
        string_description varchar(255),
        double_maxflowrate float8,
        double_minflowrate float8,
        string_tag varchar(255),
        valve_id_valve int8,
        primary key (id_ramal)
    );

    create table refmetermodel (
        id_refmeter int8 not null,
        string_description varchar(255) not null,
        double_flowrate float8,
        flowStability varchar(255),
        double_freqmaxflowrate float8 not null,
        double_maxflowrate float8 not null,
        double_minflowrate float8 not null,
        int_pulses int4,
        readtime timestamp,
        string_tag varchar(255) not null,
        int_timeread int4,
        double_volume float8,
        valve_id_valve int8,
        primary key (id_refmeter)
    );

    create table scalemodel (
        id_scale int8 not null,
        string_description varchar(255),
        string_tag varchar(255) not null,
        double_weight float8,
        primary key (id_scale)
    );

    create table tempsensormodel (
        id_temperature int8 not null,
        string_description varchar(255),
        readTime timestamp,
        string_tag varchar(255) not null,
        double_temperature float8,
        primary key (id_temperature)
    );

    create table valvemodel (
        id_valve int8 not null,
        string_description varchar(255),
        state varchar(255),
        string_tag varchar(255) not null,
        primary key (id_valve)
    );

    create table verificationerrormodel (
        id_verification_error int8 not null,
        double_error float8 not null,
        double_expected_flow_rate float8 not null,
        double_meter_flow_rate float8 not null,
        double_ref_flow_rate float8 not null,
        meter_id_meter int8,
        primary key (id_verification_error)
    );

    create table volumemodel (
        id_volume int8 not null,
        double_description varchar(255),
        date_readtime timestamp,
        double_volume float8 not null,
        primary key (id_volume)
    );

    create table volumetricbenchmodel (
        id_volumetricbench int8 not null,
        string_description varchar(255),
        id_bench varchar(255) not null,
        int_numberpositions int4 not null,
        double_uncertainty float8 not null,
        primary key (id_volumetricbench)
    );

    create table volumetricerrormodel (
        id_volumetricerror int8 not null,
        date_dateofverification timestamp,
        double_duration float8 not null,
        double_finalvolume float8 not null,
        double_initialvolume float8 not null,
        double_tankvolume float8 not null,
        flowRate_id_flowrate int8,
        meter_id_meter int8,
        volumetricBench_id_volumetricbench int8,
        primary key (id_volumetricerror)
    );

    create table waterlinemodel (
        id_line int8 not null,
        string_description varchar(255),
        state varchar(255),
        string_tag varchar(255) not null,
        primary key (id_line)
    );

    alter table batchmodel 
        add constraint UK_hv2bhppcsnpyea5x4ujw1t83b unique (string_batchid);

    alter table batchmodel 
        add constraint UK_lixckj0uvoapy4geffxm4y5tc unique (string_description);

    alter table batchradiowmbusconfigmodel 
        add constraint UK_iawheoq2hsbb4vr5ldq100swf unique (batchId);

    alter table carcassbatchmodel 
        add constraint UK_e78x5yvakq7mb9fdajg7jklot unique (string_description);

    alter table conversmodel 
        add constraint UK_qwfijtgds6ymore4ft35hh76x unique (string_ip);

    alter table conversmodel 
        add constraint UK_6pom6u7t9pwudfc31fdvkkluc unique (string_name);

    alter table dimensionalmodel 
        add constraint UK_gsnrmfx3oi7qbyi97fg55q2pn unique (string_description);

    alter table drawingmodel 
        add constraint UK_3q3duwsdlcxdc34auutmgfsg0 unique (string_description);

    alter table firmwaremodel 
        add constraint UK_4gkgh51acv4v0e63jtbmg5v2w unique (string_description);

    alter table firmwaremodel 
        add constraint UK_9scboxr8qh3ebqsgji0ovb6km unique (string_firmwarename);

    alter table firmwaremodel 
        add constraint UK_6obchns2v9k4yga9mltjk8g82 unique (string_version);

    alter table metermodel 
        add constraint UK_n3hfscqb3qprc9du2uinidpq3 unique (string_serialnumber);

    alter table qaassuredmodel 
        add constraint UK_m8ip8yq12719ry2kbdbojqb5s unique (id_meter);

    alter table radiowmbusconfigmodel 
        add constraint UK_5bqr9s1mhkm32xd4l3o3cvkxo unique (string_wmBusCryptoKey);

    alter table radiowmbusconfigmodel 
        add constraint UK_bh9040gfa9efw2og3311tpvsw unique (string_wmBusSerialNumber);

    alter table radiowmbusconfigmodel 
        add constraint UK_kdvsaoyr76vdlwje6h7oe1caw unique (id_meter);

    alter table valvemodel 
        add constraint UK_srg2marfpxsf2k8c5oynlj4wl unique (string_tag);

    alter table batchmodel 
        add constraint FK1k1wxjiigrnd4kvrnh92nato8 
        foreign key (id_client) 
        references clientmodel;

    alter table batchmodel 
        add constraint FKntfha7mak21qqd403ob21tll 
        foreign key (id_firmware) 
        references firmwaremodel;

    alter table batchmodel 
        add constraint FK7ptsmjaqb8egm3odr5eyjs82y 
        foreign key (id_meter_type) 
        references metertypemodel;

    alter table batchradiowmbusconfigmodel 
        add constraint FK9gw7t2mh4kksg3y35v0bjp6js 
        foreign key (batchId) 
        references batchmodel;

    alter table bateladamodel 
        add constraint FKn04e4tkvxsbq4mk5rosdu4ehm 
        foreign key (id_batch) 
        references batchmodel;

    alter table bateladamodel 
        add constraint FK51npn1gtgn38fwo89yvxif6f3 
        foreign key (id_process_config) 
        references processconfigmodel;

    alter table benchdatamodel 
        add constraint FK6dboswcy9ko1q65screqo38ap 
        foreign key (id_batch) 
        references batchmodel;

    alter table benchdatamodel 
        add constraint FK5vb9vp4iult10wbfhnaqglx12 
        foreign key (id_refmeter) 
        references refmetermodel;

    alter table calibconstantsmodel 
        add constraint FKodbi7brnsqqu2i1iw68r23gqx 
        foreign key (id_meter) 
        references metermodel;

    alter table carcassbatchmodel 
        add constraint FKlksnei2doeujjoqooa9p6ctbw 
        foreign key (id_drawing) 
        references drawingmodel;

    alter table criticalpointmodel 
        add constraint FKf4jkwbjbb6ns4cojy9udnnwgh 
        foreign key (id_batch) 
        references batchmodel;

    alter table criticalpointmodel 
        add constraint FKpefq7jpwnye1nteg3gs95ed97 
        foreign key (id_flowrate) 
        references flowratemodel;

    alter table dimensionalmodel 
        add constraint FK762f5na8ey9ec68deaafgd6ow 
        foreign key (id_carcassBatch) 
        references carcassbatchmodel;

    alter table dimensionmeasuremodel 
        add constraint FK5vkvpdoc07flayhypc6hp1m8c 
        foreign key (id_dimensional) 
        references dimensionalmodel;

    alter table dimensionmeasuremodel 
        add constraint FK4hf5j08bw4xylgrvhcsu7196r 
        foreign key (id_quota) 
        references quotamodel;

    alter table distancepointsmodel 
        add constraint FK5ugm6ic62dgbiuppvhhpogk4s 
        foreign key (id_batch) 
        references batchmodel;

    alter table flowratemeanstdmodel 
        add constraint FKql9gut0xxfro3qsbyoqjlvfif 
        foreign key (id_batch) 
        references batchmodel;

    alter table flowratemeanstdmodel 
        add constraint FK1q9655wcuyl0o5d054712uyru 
        foreign key (id_batelada_end) 
        references bateladamodel;

    alter table flowratemeanstdmodel 
        add constraint FKk3b62q9pv0164pghfe2v6xsdi 
        foreign key (id_batelada_init) 
        references bateladamodel;

    alter table flowratemodel 
        add constraint FKbasbkvylplboov4ly8r7hl2oh 
        foreign key (pidConfig_id_pidconfig) 
        references pidconfigmodel;

    alter table flowratemodel 
        add constraint FKs38hotibrvtx0p74hipt50o43 
        foreign key (pump_id_pump) 
        references pumpmodel;

    alter table flowratemodel 
        add constraint FKe952fi1x6t4l7il7fhy4ws69q 
        foreign key (ramal_id_ramal) 
        references ramalmodel;

    alter table flowratemodel 
        add constraint FK74lyogddff64ilrgq16bph6fe 
        foreign key (refMeter_id_refmeter) 
        references refmetermodel;

    alter table logchangebatchmodel 
        add constraint FK3re30kwe73hwddow8un662epo 
        foreign key (id_batch_new) 
        references batchmodel;

    alter table logchangebatchmodel 
        add constraint FKfk8sv4syop0exp4itglm65lth 
        foreign key (id_batch_old) 
        references batchmodel;

    alter table logchangebatchmodel 
        add constraint FK8fqnll96w0ngijhf7g4jdejw8 
        foreign key (id_meter) 
        references metermodel;

    alter table logchangebatchmodel 
        add constraint FK2rujdw2v2lur4nqac7cfxi1mb 
        foreign key (id_meter_type_new) 
        references metertypemodel;

    alter table logchangebatchmodel 
        add constraint FKpe9swv8bd49k7bbhiq92wqq1k 
        foreign key (id_meter_type_old) 
        references metertypemodel;

    alter table logchangeprocessbatchmodel 
        add constraint FKa658ckhdvxq5qqn37fkqnes5w 
        foreign key (id_batch) 
        references batchmodel;

    alter table logchangeprocessbatchmodel 
        add constraint FKevkgxd1109u26o7jn62k7mryc 
        foreign key (id_batelada_end) 
        references bateladamodel;

    alter table logchangeprocessbatchmodel 
        add constraint FKpuropl7t3wwc5m83mgnyoa9j1 
        foreign key (id_batelada_init) 
        references bateladamodel;

    alter table meterdatamodel 
        add constraint FKdqni3e4ts810tpbn8oxiwcl 
        foreign key (meter_id) 
        references metermodel;

    alter table metermodel 
        add constraint FK8b6sc3ue5ihen9uq05kagjn2b 
        foreign key (id_batch) 
        references batchmodel;

    alter table metermodel 
        add constraint FKcn8ldx0t84vj9m1jqstbg660d 
        foreign key (id_batelada) 
        references bateladamodel;

    alter table metermodel 
        add constraint FKcvs9uaxn0cox5fm37bl9na2n9 
        foreign key (id_conversor) 
        references conversmodel;

    alter table metermodel 
        add constraint FKl2fyb6jg4cx0v8bl1exqvxd0s 
        foreign key (id_firmware) 
        references firmwaremodel;

    alter table metermodel 
        add constraint FKd2c1c9r0y5xjbvue2pp2g83tu 
        foreign key (id_meter_type) 
        references metertypemodel;

    alter table metertypemodel 
        add constraint FKh7jc72d6kxo2qocadypmlh5cy 
        foreign key (id_maxflowrate) 
        references flowratemodel;

    alter table metertypemodel 
        add constraint FK1o5qv0tkibmx9vifp7mjbsogo 
        foreign key (id_minFlowrate) 
        references flowratemodel;

    alter table metertypemodel 
        add constraint FKpwdebt06wko182ob4kng4ixu3 
        foreign key (id_nominalflowrate) 
        references flowratemodel;

    alter table metertypemodel 
        add constraint FKopc2g6qqwb60f9cy953le7j16 
        foreign key (id_tansitionFlowrate) 
        references flowratemodel;

    alter table pidconfigmodel 
        add constraint FKjwkgkenrnn17lps777n31bj4n 
        foreign key (flowRate_id_flowrate) 
        references flowratemodel;

    alter table processconfig_calculatedfixedconst 
        add constraint FKlhkxc21edghx61kq4a2tdp7vv 
        foreign key (id_calculatedfixedconstmodel) 
        references calculatedfixedconstmodel;

    alter table processconfig_calculatedfixedconst 
        add constraint FKkctv9228o92xn4c37p5eb52h4 
        foreign key (id_processconfig) 
        references processconfigmodel;

    alter table processconfig_flowrate_calib 
        add constraint FKh67odr1b0dfe92f90itbtygl4 
        foreign key (id_flowrate) 
        references flowratemodel;

    alter table processconfig_flowrate_calib 
        add constraint FK7kmc614x9uw2n7oinwdqkmm73 
        foreign key (id_processconfig) 
        references processconfigmodel;

    alter table processconfig_flowrate_verif 
        add constraint FKdu3ovsqtif99f5euf56gc6tf 
        foreign key (id_flowrate) 
        references flowratemodel;

    alter table processconfig_flowrate_verif 
        add constraint FKkdn8o7ta39inao40185rbyl23 
        foreign key (id_processconfig) 
        references processconfigmodel;

    alter table qaassuredmodel 
        add constraint FKke9xpv24fbbhxcl0bbfdi2fgv 
        foreign key (id_meter) 
        references metermodel;

    alter table quotamodel 
        add constraint FK7e6tatlkt2s4dxm69hhymqk2u 
        foreign key (id_drawing) 
        references drawingmodel;

    alter table radiowmbusconfigmodel 
        add constraint FKdp6hqa6aj3u7c5d1vkiyb52ou 
        foreign key (id_meter) 
        references metermodel;

    alter table ramalmodel 
        add constraint FKgcmqr2fhusfc2u8sa79wpb70o 
        foreign key (valve_id_valve) 
        references valvemodel;

    alter table refmetermodel 
        add constraint FKoqln3qbyjwqij1busaral8pd9 
        foreign key (valve_id_valve) 
        references valvemodel;

    alter table verificationerrormodel 
        add constraint FKipq3ngo01hxvbyyuyppeaud7j 
        foreign key (meter_id_meter) 
        references metermodel;

    alter table volumetricerrormodel 
        add constraint FKsiq9uq0ng8cspdyfoeulnyod2 
        foreign key (flowRate_id_flowrate) 
        references flowratemodel;

    alter table volumetricerrormodel 
        add constraint FKmfw7o4f5q9xveme3ydihj822v 
        foreign key (meter_id_meter) 
        references metermodel;

    alter table volumetricerrormodel 
        add constraint FKpyiykph2db84f4c3udpx5w16f 
        foreign key (volumetricBench_id_volumetricbench) 
        references volumetricbenchmodel;
