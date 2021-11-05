package com.wit.iot.mqtt.repository;

import com.wit.iot.mqtt.domain.Kvcf;

import java.util.List;

public interface KvcfRepository extends BaseRepository<Kvcf>{

    List<Kvcf> list(Kvcf queryInfo);
}
