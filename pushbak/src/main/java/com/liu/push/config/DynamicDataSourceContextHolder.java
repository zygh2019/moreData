package com.liu.push.config;

public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            //配置初始数据源
            return "shi";
        }
    };

    /**
     * 根据put进的key来设置某项数据源
     *
     * @param key the key
     */
    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    /**
     * 得到当前数据源的key
     *
     * @return data source key
     */
    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    /**
     * 清楚数据源
     */
    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

}