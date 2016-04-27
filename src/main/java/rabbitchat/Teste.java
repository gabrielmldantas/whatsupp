package rabbitchat;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class Teste {
	public void teste() {
		System.out.println("Oi");
	}
}
