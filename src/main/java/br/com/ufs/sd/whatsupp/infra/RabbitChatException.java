package br.com.ufs.sd.whatsupp.infra;

public class RabbitChatException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RabbitChatException() {
    }

    public RabbitChatException(String message) {
        super(message);
    }

    public RabbitChatException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RabbitChatException(Throwable cause) {
        super(cause);
    }
}
