package io.kt.cloud.tanda.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaListener {

	private static final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

	@StreamListener(target = Processor.INPUT)
	public void handle(@Payload String unknownEvent) {
		// System.out.println(unknownEvent);
	}

}
