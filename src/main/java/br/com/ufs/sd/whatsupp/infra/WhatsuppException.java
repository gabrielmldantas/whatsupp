package br.com.ufs.sd.whatsupp.infra;

public class WhatsuppException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public WhatsuppException() {
    }

    public WhatsuppException(String message) {
        super(message);
    }

    public WhatsuppException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WhatsuppException(Throwable cause) {
        super(cause);
    }
}
