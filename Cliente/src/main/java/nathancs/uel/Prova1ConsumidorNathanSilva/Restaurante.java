package nathancs.uel.Prova1ConsumidorNathanSilva;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Restaurante implements Serializable {

    @Id
    private int id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Override
    public boolean equals(Object o){
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        return ((Restaurante)o).id == (this.id);
    }


    @Override
    public int hashCode() {
        return id * 12345;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "restaurante")
    private List<Cardapio> cardapios ;

    public List<Cardapio> getCardapios() {
        return cardapios;
    }

    public void setCardapios(List<Cardapio> cardapios) {
        this.cardapios = cardapios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
