package com.danech.router;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfOperationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.danech.model.User;
import com.danech.processor.MyCamelProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
@Transactional
public class MyCamelRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		
		onException(Exception.class).handled(true).process(exchange->{
			log.info("Excaption has been caught ");
			var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
			if(exception instanceof CxfOperationException) {
				
				CxfOperationException op = (CxfOperationException)exception;
				var status = op.getStatusCode();
				var message = op.getStatusText();
				
				log.info("Status caught {}",status);
				log.info("Message caught {}",message);
				
			}
		});
		
		from("{{my.camel.route}}").routeId("process_Id").log(LoggingLevel.INFO, "Processing")
				.bean(MyCamelProcessor.class, "process").to("direct:myRoute");

		// Lets assume we have to give User as a request body to third party API
		from("direct:myRoute").process(exchange -> {
				log.info("My second routing processing");
				exchange.getMessage().setBody(exchange.getIn().getBody());
			})
				.removeHeader("*")
				.setHeader(Exchange.HTTP_METHOD, constant("POST"))
				.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
				.setBody(new Expression() {
					
					@SuppressWarnings("unchecked")
					@Override
					public <String> String evaluate(Exchange exchange, Class<String> type) {
						ObjectMapper mapper = new ObjectMapper();
						mapper.registerModule(new JavaTimeModule());
						var obj = exchange.getIn().getBody(User.class);
						log.info("Request body Java obj {}",obj);
						String value = (String)"";
						try {
							value = (String)mapper.writeValueAsString(obj);
							log.info("Request body to be passed {}",value);
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						return value;
					}
				})
				.to(ExchangePattern.InOut, "cxfrs://http://localhost:8080/users?throwExceptionOnFailure=true").process(exchange -> {
					log.info("Endpoint processed successfully");
					log.info("Response code {} ", exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));
				}).end();
	}

}
