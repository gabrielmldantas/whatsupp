package br.com.ufs.sd.whatsupp.chat;

public enum StatusMensagem {
	ENVIADO(1), ENVIANDO(2), FALHA(3) ;

    private int status;

    StatusMensagem(int status) {
        this.status = status;
    }

    public int getValor() {
        return status;
    }
}
