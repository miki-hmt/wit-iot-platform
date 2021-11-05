package com.wit.iot.mqtt.repository.impl;

import com.wit.iot.mqtt.domain.Kvcf;
import com.wit.iot.mqtt.repository.BaseRepositoryImpl;
import com.wit.iot.mqtt.repository.KvcfRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KvcfRepositoryImpl extends BaseRepositoryImpl<Kvcf> implements KvcfRepository {

    @Override
    public List<Kvcf> list(Kvcf queryInfo) {

        String cql=String.format("select * from master_order where tenant_id = '%s' and sequence_id='%s'",queryInfo ,queryInfo);
        List<Kvcf> list = list(cql);
        return list;
    }
}
