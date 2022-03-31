package com.danech.processor;

import org.apache.camel.Exchange;
import org.apache.camel.spi.annotations.Component;

import com.danech.model.User;

import lombok.extern.slf4j.Slf4j;

@Component(value="myCamelProcessor")
@Slf4j
public class MyCamelProcessor {

	public void process(Exchange exchange) {
		log.info("Porcessing");
		var in = exchange.getIn();
		var user = in.getBody(User.class);		
		log.info("User : {}",user);
		exchange.getIn().setBody(user);
	}
	
}
