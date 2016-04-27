package br.com.ufs.sd.rabbitchat.infra;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

public class CDIProducer {
	
	@Produces
	@Resource(name = "jdbc/RabbitChatWebDB")
	private DataSource dataSourceWeb;
	
	@Produces @RabbitChatDS
	@Resource(name = "jdbc/RabbitChatDB")
	private DataSource dataSource;
}
