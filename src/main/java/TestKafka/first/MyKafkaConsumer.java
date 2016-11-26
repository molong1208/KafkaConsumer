package TestKafka.first;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.dd.Common.SystemConfig;
import com.dd.ReadConfig.Errorlog;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class MyKafkaConsumer
{
  private final ConsumerConnector consumer;
  private final String topic;
  private final static String kafka_ip_port = SystemConfig.KAFKA_IP_PORT;
  public static Logger logger = Logger.getLogger(MyKafkaConsumer.class);

  public MyKafkaConsumer(String topic)
  {
    this.consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());

    this.topic = topic;
  }

  private static ConsumerConfig createConsumerConfig()
  {
    Properties props = new Properties();
    props.put("auto.offset.reset", "smallest");//从上次未消费的地方开始
    props.put("zookeeper.connect", kafka_ip_port);
    props.put("group.id", "group1");
    props.put("zookeeper.session.timeout.ms", "400");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");

    return new ConsumerConfig(props);
  }

  /**
   * 一般消费者模式，直接读取kafka，相当于kafka推送的模式
   * */
  public void PushFromKafka() {
	  
	PropertyConfigurator.configure("./conf/log4j.properties");
	  
	MyKafkaConsumer cons=new MyKafkaConsumer(this.topic);
    Map topicCountMap = new HashMap();
    topicCountMap.put(cons.topic, new Integer(1));
    Map consumerMap = cons.consumer.createMessageStreams(topicCountMap);
    KafkaStream stream = (KafkaStream)((List)consumerMap.get(cons.topic)).get(0);
    ConsumerIterator it = stream.iterator();
    while (it.hasNext())
    {
	    String message = new String((byte[])it.next().message());
	    System.out.println(message);
    }

  } 
}