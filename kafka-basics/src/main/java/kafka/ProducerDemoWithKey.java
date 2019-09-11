
package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoWithKey {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithKey.class);
        String bootsrapServers = "127.0.0.1:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootsrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


        for (int i = 0; i < 10; i++) {
            String topic = "first_topic";
            String value = "Hello world " + Integer.toString(i);
            String key = "_id" + Integer.toString(i);

            logger.info("Key: " + key);

            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);


            producer.send(record, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            if (exception == null) {
                                logger.info("Received new metadata.\n" +
                                        "Topic: " + metadata.topic() + "\n" +
                                        "Particion: " + metadata.partition() + "\n" +
                                        "Offset: " + metadata.offset() + "\n" +
                                        "Timestamp: " + metadata.timestamp());
                            } else {
                                logger.error("Error while producing", exception);
                            }
                        }
                    }).get();
        }


                producer.flush();

                producer.close();
    }
}
