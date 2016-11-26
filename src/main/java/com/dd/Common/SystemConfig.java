package com.dd.Common;


public class SystemConfig {
	
	public static String CURL_WAIT_TIMEOUT=PropertiesUtil.getInstance().getStringValue("sys", "curl_wait_timeout_ms");
	public static String CURL_PREFIX=PropertiesUtil.getInstance().getStringValue("sys", "curl_prefix");	
	public static String CURL_GET_INFO=PropertiesUtil.getInstance().getStringValue("sys", "curl_get_info");
	public static String KAFKA_TOPIC=PropertiesUtil.getInstance().getStringValue("sys", "kafka_topic");
	public static String KAFKA_IP_PORT=PropertiesUtil.getInstance().getStringValue("sys", "kafka_ip_port");
	public static int    SERVER_PORT=PropertiesUtil.getInstance().getIntValue("sys", "server_port");
	public static String LAST_OFFSET_FILE=PropertiesUtil.getInstance().getStringValue("sys", "last_offset_file");
	public static String FULLTIMESTAMP_FILE=PropertiesUtil.getInstance().getStringValue("sys", "full_timestamp_offset_file");
	public static String CLINT_NAME=PropertiesUtil.getInstance().getStringValue("sys", "clint_name");
	public static int    BUFFER_SIZE=PropertiesUtil.getInstance().getIntValue("sys", "buffer_size");
	public static int    FETCH_SIZE=PropertiesUtil.getInstance().getIntValue("sys", "fetch_size");
	public static int    PARTITION=PropertiesUtil.getInstance().getIntValue("sys", "partition");
	public static int    MARK_NEXT_OFFSET=PropertiesUtil.getInstance().getIntValue("sys", "mark_next_offset");
	public static int    THREAD_NUM=PropertiesUtil.getInstance().getIntValue("sys", "thread_num");
	public static int    SLEEP_TIME=PropertiesUtil.getInstance().getIntValue("sys", "sleep_time");
	public static int    JOB_PUT_HBASE_COUNT=PropertiesUtil.getInstance().getIntValue("sys", "job_put_habse_count");
	public static String MODE=PropertiesUtil.getInstance().getStringValue("sys", "mode");


}
