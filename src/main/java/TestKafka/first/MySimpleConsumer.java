package TestKafka.first;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.cluster.BrokerEndPoint;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.dd.Common.SystemConfig;
import com.dd.ReadConfig.Errorlog;
import com.dd.ReadConfig.ReadConfig;

import java.nio.ByteBuffer;
import java.util.*;

public class MySimpleConsumer {
	static boolean first=true;
	public static long mark_offset=0;
	static long fulltime_offset=0;
	static boolean fulltime_change=false;
	public static long update_count = 0;
	public static long time = 0;
	String last_offset_filename=SystemConfig.LAST_OFFSET_FILE;
	String full_time_stamp_filename=SystemConfig.FULLTIMESTAMP_FILE;

    private final static Logger logger = Logger.getLogger(MySimpleConsumer.class);

    private List<String> m_replicaBrokers = new ArrayList<String>();

    public MySimpleConsumer() {
        m_replicaBrokers = new ArrayList<String>();
    }
    

    /**
     * 通过offset读kafka数据
     * @param a_maxReads
     * 				：最大读取线程
     * @param a_topic
     * 				：所订阅的topid
     * @param a_partition
     * 				：订阅的分区
     * @param a_seedBrokers
     * 				：订阅的ip节点
     * @param a_port
     * 				：端口号
     * @throws Exception
     */
    public List<Object> run(long a_maxReads, String a_topic, int a_partition, List<String> a_seedBrokers, int a_port) throws Exception {
        // find the meta data about the topic and partition we are interested in
        //查询 PartitionMetadata
    	List <Object> result = new ArrayList<Object>();
    	
    	try{
        PartitionMetadata metadata = findLeader(a_seedBrokers, a_port, a_topic, a_partition);
        if (metadata == null) {
            logger.error("Can't find metadata for Topic and Partition. Exiting");
//            new EnerbosException("Can't find metadata for Topic and Partition. Exiting");
            return result;
        }
        if (metadata.leader() == null) {
            logger.error("Can't find Leader for Topic and Partition. Exiting");
//            new EnerbosException("Can't find Leader for Topic and Partition. Exiting");
            return result;
        }
        String leadBroker = metadata.leader().host();
        String clientName = SystemConfig.CLINT_NAME;
        int buffer_size = SystemConfig.BUFFER_SIZE;
        int fetch_size = SystemConfig.FETCH_SIZE;

        //SimpleConsumer consumer = new SimpleConsumer(leadBroker, a_port, 100000, 64 * 1024, clientName);
        SimpleConsumer consumer = new SimpleConsumer(leadBroker, a_port, 100000, buffer_size, clientName);

        long readOffset = 0;
        if(first)
        {
        	//readOffset = getLastOffset(consumer,a_topic, a_partition, kafka.api.OffsetRequest.EarliestTime(), clientName);
        	readOffset = GetFirstOffset(last_offset_filename);
        	mark_offset=readOffset;
        	first = false;
        }
        else
        	readOffset = mark_offset;
        //long readOffset = getLastOffset(consumer,a_topic, a_partition, 200, clientName);
        //readOffset=289;
        int numErrors = 0;
        while (a_maxReads > 0) {
            if (consumer == null) {
                consumer = new SimpleConsumer(leadBroker, a_port, 100000, buffer_size, clientName);
            }
            FetchRequest req = new FetchRequestBuilder()
                    .clientId(clientName)
                    .addFetch(a_topic, a_partition, readOffset, fetch_size)
                    //.addFetch(a_topic, a_partition, readOffset, 100000) // Note: this fetchSize of 100000 might need to be increased if large batches are written to Kafka
                    .build();
            FetchResponse fetchResponse = consumer.fetch(req);
            /*if(fetchResponse.messageSet(a_topic, a_partition).sizeInBytes()==0)
            	return;*/
            if(!fetchResponse.messageSet(a_topic, a_partition).iterator().hasNext()){
            	long newest = getLastOffset(consumer,a_topic, a_partition, kafka.api.OffsetRequest.LatestTime(), clientName);
            	//System.out.println("zuixin:"+newest+" offset:"+readOffset);
            	if(newest-1>readOffset){//when offset is 5 allow get offset=6 and no error,if not -1 will cause somthing wrong
            		readOffset++;// add myself 2016-07-29 in case offset cant read message,because one offset is not exist
            		System.out.println("zuixin:"+newest+" offset:"+readOffset);
            	}
            	mark_offset=readOffset;
            	}
            

            if (fetchResponse.hasError()) {
                numErrors++;
                // Something went wrong!
                short code = fetchResponse.errorCode(a_topic, a_partition);
                logger.error("Error fetching data from the Broker:" + leadBroker + " Reason: " + code);
                if (numErrors > 5) break;
                if (code == ErrorMapping.OffsetOutOfRangeCode())  {
                	System.out.print("no data and offset="+readOffset);
                    // We asked for an invalid offset. For simple case ask for the last element to reset
                    readOffset = getLastOffset(consumer,a_topic, a_partition, kafka.api.OffsetRequest.LatestTime(), clientName);
                    //mark_offset = readOffset;
                    System.out.println("new offset="+readOffset);
                    continue;
                }
                consumer.close();
                consumer = null;
                leadBroker = findNewLeader(leadBroker, a_topic, a_partition, a_port);
                continue;
            }
            numErrors = 0;

            long numRead = 0;
            for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(a_topic, a_partition)) {
                long currentOffset = messageAndOffset.offset();
                mark_offset=currentOffset;
                if (currentOffset < readOffset) {
                    logger.info("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
                    System.out.println("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
                    continue;
                }
                readOffset = messageAndOffset.nextOffset();
                if(1 == SystemConfig.MARK_NEXT_OFFSET)
                	mark_offset=readOffset;
                ByteBuffer payload = messageAndOffset.message().payload();

                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                logger.info(String.valueOf(messageAndOffset.offset()) + ": " + new String(bytes, "UTF-8"));

            	JSONObject jsonob = null;
        	    String message = new String(bytes,"UTF-8");
        	    //System.out.println(String.valueOf(messageAndOffset.offset()) + ": " + new String(bytes,"UTF-8"));
        	    HashMap<String,Object> mymap = new HashMap<String,Object>();
        	    mymap.put(String.valueOf(messageAndOffset.offset()), message);
        	    result.add(mymap);
                numRead++;
                a_maxReads--;
            }

            if (numRead == 0) {
            	if (consumer != null) consumer.close();
            	return result;
            	
            }
        }
        
        if (consumer != null) consumer.close();

        }catch(Exception e)
    	{
        	e.printStackTrace(System.out);
    	}finally
        {
        	Errorlog.writeerror(Long.toString(mark_offset), last_offset_filename,false);
        }
        return result;
    }

    
    public static long getLastOffset(SimpleConsumer consumer, String topic, int partition, long whichTime, String clientName) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
                requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            logger.error("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition) );
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }

    private String findNewLeader(String a_oldLeader, String a_topic, int a_partition, int a_port) throws Exception {
        for (int i = 0; i < 3; i++) {
            boolean goToSleep = false;
            PartitionMetadata metadata = findLeader(m_replicaBrokers, a_port, a_topic, a_partition);
            if (metadata == null) {
                goToSleep = true;
            } else if (metadata.leader() == null) {
                goToSleep = true;
            } else if (a_oldLeader.equalsIgnoreCase(metadata.leader().host()) && i == 0) {
                // first time through if the leader hasn't changed give ZooKeeper a second to recover
                // second time, assume the broker did recover before failover, or it was a non-Broker issue
                //
                goToSleep = true;
            } else {
                return metadata.leader().host();
            }
            if (goToSleep) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
        logger.error("Unable to find new leader after Broker failure. Exiting");
        throw new Exception("Unable to find new leader after Broker failure. Exiting");
    }

    private PartitionMetadata findLeader(List<String> a_seedBrokers, int a_port, String a_topic, int a_partition) {
        PartitionMetadata returnMetaData = null;
        loop:
        for (String seed : a_seedBrokers) {
            SimpleConsumer consumer = null;
            try {
            	int buffer_size = SystemConfig.BUFFER_SIZE;
                consumer = new SimpleConsumer(seed, a_port, 100000, buffer_size, "leaderLookup");
                List<String> topics = Collections.singletonList(a_topic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

                List<TopicMetadata> metaData = resp.topicsMetadata();
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        if (part.partitionId() == a_partition) {
                            returnMetaData = part;
                            break loop;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error communicating with Broker [" + seed + "] to find Leader for [" + a_topic
                        + ", " + a_partition + "] Reason: " + e);
            } finally {
                if (consumer != null) consumer.close();
            }
        }
        if (returnMetaData != null) {
            m_replicaBrokers.clear();
            for (BrokerEndPoint replica : returnMetaData.replicas()) {
                m_replicaBrokers.add(replica.host());
            }
        }
        return returnMetaData;
    }
    private long GetFirstOffset(String filename)
    {
    	long ret = 0;
    	ReadConfig readconfig = new ReadConfig();
    	Set<String> data = readconfig.ReadFile(filename);
    	for(String temp:data)
    	{
    		System.out.println("string:"+temp);
    		ret = Long.parseLong(temp);
    		System.out.println(ret);
    	}
    	//System.exit(0);
    		
    	return ret;
    }
    
}
