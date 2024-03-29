package com.wit.iot.order.utils;

public class KeyGeneratorSnowflake implements KeyGenerator<Long>{

    private int time_bit_count = 41; // bit数量,用于表示时间
    private int node_bit_count = 10; // bit数量,用于表示集群中节点
    private int sequence_bit_count = 12; // bit数量,用于表示序列号
    private long epoch = 1523289600000L;// 此算法编码完成的时间

    private int time_shift = sequence_bit_count + node_bit_count;
    private int node_shift = sequence_bit_count;

    private long max_timestamp = epoch + ((1L << time_bit_count) - 1); //　最大时间戳
    private int max_node_no = (1 << node_bit_count) - 1; // 最大节点编号
    private int max_sequence = (1 << sequence_bit_count) - 1; // 最大序列号

    private long timestamp;
    private long nodeNo;// 代表集群中节点的编号
    private long sequence = 0;

    /**
     * 为给定的节点构造一个key生成器.
     *
     * @param nodeNo 代表集群中节点的编号,从0开始
     */
    public KeyGeneratorSnowflake(int nodeNo) {
        checkNodeNo(nodeNo);
        this.nodeNo = nodeNo;
        try {
            this.timestamp = getTimestamp();
        } catch (KeyGenExceedMaxValueException | KeyGenTimeBackwardsException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 检查给定的节点编号是否在指定范围内.
     *
     * @param nodeNo 节点编号
     */
    private void checkNodeNo(int nodeNo) {
        if (nodeNo < 0 || nodeNo > max_node_no) {
            throw new IllegalArgumentException("节点编号允许的范围是:[0," + max_node_no + "],当前节点编号是:" + nodeNo);
        }
    }

    @Override
    public synchronized Long next() throws KeyGenExceedMaxValueException, KeyGenTimeBackwardsException {
        sequence++;
        if (sequence > max_sequence) {
            timestamp = getTimestamp();
            sequence = 0;
        }
        return ((timestamp - epoch) << time_shift) | (nodeNo << node_shift) | sequence;
    }

    @Override
    public synchronized Long[] next(int size) throws KeyGenExceedMaxValueException, KeyGenTimeBackwardsException {
        if (size <= 0) {
            throw new IllegalArgumentException("size必须大于0");
        }
        if (size == 1) {
            return new Long[]{next()};
        }
        Long[] keys = new Long[size];
        for (int i = 0; i < size; i++) {
            keys[i] = next();
        }
        return keys;
    }

    /**
     * 获取时间戳.
     *
     * @return 时间戳
     * @throws KeyGenExceedMaxValueException 此生成器不能无限生成key,会有一个最大值,当超过这个最大值时,抛出此异常
     * @throws KeyGenTimeBackwardsException  机器的时间被向后调整了
     */
    private long getTimestamp() throws KeyGenExceedMaxValueException, KeyGenTimeBackwardsException {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis < epoch) {
            throw new KeyGenTimeBackwardsException("时间被向后调了?当前机器的时间戳是:" + currentTimeMillis +
                    ",纪元时间戳是:" + epoch);
        }
        while (currentTimeMillis == timestamp) {
            currentTimeMillis = System.currentTimeMillis();
        }
        if (currentTimeMillis < timestamp) {
            throw new KeyGenTimeBackwardsException("时间被向后调了?当前机器的时间戳是:" + currentTimeMillis +
                    ",最近一次记录的时间戳是:" + timestamp);
        }
        if (currentTimeMillis > max_timestamp) {
            throw new KeyGenExceedMaxValueException("omg!你的系统已经运行了差不多69年,恭喜!然而此ID生成器已经无法再继续提供服务了,bye!");
        }
        return currentTimeMillis;
    }
}
