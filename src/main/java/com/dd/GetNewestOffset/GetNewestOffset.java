package com.dd.GetNewestOffset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.dd.Common.SystemConfig;

import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import TestKafka.first.MySimpleConsumer;

public class GetNewestOffset {
	
	/*
	 * 得到最新的offset，其中topic的设置以及ip端口的设置在配置文件里
	 * 
	 * **/
	public static int getOffset() { 
  
        String topic = SystemConfig.KAFKA_TOPIC;
        String[] ip_port = SystemConfig.KAFKA_IP_PORT.split(":");
        String seed = ip_port[0];  
        int port = Integer.parseInt(ip_port[1]);  
        List<String> seeds = new ArrayList<String>();  
        seeds.add(seed);  
        GetNewestOffset kot = new GetNewestOffset();  
  
        TreeMap<Integer,PartitionMetadata> metadatas = kot.findLeader(seeds, port, topic);  
          
        int sum = 0;  
          
        for (Entry<Integer,PartitionMetadata> entry : metadatas.entrySet()) {  
            int partition = entry.getKey();  
            String leadBroker = entry.getValue().leader().host();  
            String clientName = SystemConfig.CLINT_NAME;
            SimpleConsumer consumer = new SimpleConsumer(leadBroker, port, 100000,  
                    64 * 1024, clientName);  
            long readOffset = MySimpleConsumer.getLastOffset(consumer, topic, partition,  
                    kafka.api.OffsetRequest.LatestTime(), clientName);  
            sum += readOffset;  
            System.out.println(partition+":"+readOffset);  
            if(consumer!=null)consumer.close();  
        }  
        System.out.println("all ："+sum);  
        return sum;
  
    }  
    private TreeMap<Integer,PartitionMetadata> findLeader(List<String> a_seedBrokers,  
            int a_port, String a_topic) {  
        TreeMap<Integer, PartitionMetadata> map = new TreeMap<Integer, PartitionMetadata>();  
        loop: for (String seed : a_seedBrokers) {  
            SimpleConsumer consumer = null;  
            try {  
                consumer = new SimpleConsumer(seed, a_port, 100000, 64 * 1024,  
                        "leaderLookup");  
                List<String> topics = Collections.singletonList(a_topic);  
                TopicMetadataRequest req = new TopicMetadataRequest(topics);  
                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);  
  
                List<TopicMetadata> metaData = resp.topicsMetadata();  
                for (TopicMetadata item : metaData) {  
                    for (PartitionMetadata part : item.partitionsMetadata()) {  
                        map.put(part.partitionId(), part);  
                    }  
                }  
            } catch (Exception e) {  
                System.out.println("Error communicating with Broker [" + seed  
                        + "] to find Leader for [" + a_topic + ", ] Reason: " + e);  
            } finally {  
                if (consumer != null)  
                    consumer.close();  
            }  
        }  
        return map;  
    }  
    

}
