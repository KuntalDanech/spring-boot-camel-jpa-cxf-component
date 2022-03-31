package com.danech.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.apache.camel.component.jpa.Consumed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
@NamedNativeQuery(name="unprocessedUsers", query = "SELECT * FROM  users WHERE processed_flag = 'N' limit 1", resultClass = User.class)
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;

	@Column(name="processed_flag")
	private String processedFlag;
	
	@Column(name="processed_date_time")
	private LocalDateTime processedDateTime;
	
	
	@Consumed
	public void process() {
		this.setProcessedFlag("Y");
		this.setProcessedDateTime(LocalDateTime.now());
		log.info("Processed successfully and fields have been updated");
	}
	
}
