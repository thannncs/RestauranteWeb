package nathancs.uel.Prova1ConsumidorNathanSilva;

import java.io.Serializable;

public class Pedido implements Serializable {
    private int id;

    private Cardapio cardapio;

    int quantidade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cardapio getCardapio() {
        return cardapio;
    }

    public void setCardapio(Cardapio cardapio) {
        this.cardapio = cardapio;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Pedido(Cardapio cardapio, int quantidade) {
        this.cardapio = cardapio;
        this.quantidade = quantidade;
    }
}
