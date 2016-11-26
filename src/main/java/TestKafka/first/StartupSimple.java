package TestKafka.first;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.dd.Common.Log;
import com.dd.Common.SystemConfig;
import com.dd.GetNewestOffset.GetNewestOffset;
import com.dd.ReadConfig.Errorlog;

public class StartupSimple {
    public static void StartProcess(String mode) {
    	if(mode.trim().equals("pull"))
    	{
    	MySimpleConsumer example = new MySimpleConsumer();
        long maxReads = Long.parseLong("3");
        String topic = SystemConfig.KAFKA_TOPIC;
        int partition = SystemConfig.PARTITION;
        List<String> seeds = new ArrayList<String>();
        String [] ip_port=SystemConfig.KAFKA_IP_PORT.split(":");
        seeds.add(ip_port[0]);
        int port = Integer.parseInt(ip_port[1]);
        try {
        		List<Object> kafka_data = new ArrayList<Object>();
        		Log.logErrorMsg("start get data from kafka!");
        		Date start_time = new Date();
        		kafka_data = example.run(maxReads, topic, partition, seeds, port);
        		System.out.println(kafka_data);
        		Date end_time = new Date();
        		long get_kafka_data_cost = end_time.getTime()-start_time.getTime();
        		Log.logErrorMsg("end get data from kafka count:"+kafka_data.size()+" cost:"+get_kafka_data_cost+"ms");
        		Thread.sleep(SystemConfig.SLEEP_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    	if(mode.trim().equals("push"))
    	{
    		MyKafkaConsumer pushkafka = new MyKafkaConsumer(SystemConfig.KAFKA_TOPIC);
    		pushkafka.PushFromKafka();
    	}
    	if(mode.trim().equals("newestoffset"))
    	{
    		int offset = GetNewestOffset.getOffset();
    		System.out.println("the newest offset is:"+offset);
    	}
    }

}
