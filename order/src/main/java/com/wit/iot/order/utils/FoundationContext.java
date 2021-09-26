package com.wit.iot.order.utils;

/**
 * 订单id生成器
 */
public class FoundationContext {
    private static KeyGenerator<Long> longKeyGenerator = new KeyGeneratorSnowflake(1);

    /**
     * 设置key generator.
     *
     * @param longKeyGenerator key generator,用于生成long类型的key
     */
    public static void setLongKeyGenerator(KeyGenerator<Long> longKeyGenerator) {
        FoundationContext.longKeyGenerator = longKeyGenerator;
    }

    /**
     * 获取long类型的key generator.
     *
     * @return key generator,用于生成long类型的key
     */
    public static KeyGenerator<Long> getLongKeyGenerator() {
        return longKeyGenerator;
    }
}
