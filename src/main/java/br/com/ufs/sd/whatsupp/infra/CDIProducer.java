package br.com.ufs.sd.whatsupp.infra;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

public class CDIProducer {
	
	@Produces
	@Resource(name = "jdbc/RabbitChatDB")
	private DataSource dataSourceWeb;
}
