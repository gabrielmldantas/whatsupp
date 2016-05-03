package br.com.ufs.sd.whatsupp.infra;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.ufs.sd.whatsupp.rabbitmq.RabbitMQController;

public class WhatsuppServletContextListener implements ServletContextListener {

	@Inject
	private RabbitMQController rabbitMQController;
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		if (rabbitMQController.isStarted()) {
			rabbitMQController.stop();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	}
}
