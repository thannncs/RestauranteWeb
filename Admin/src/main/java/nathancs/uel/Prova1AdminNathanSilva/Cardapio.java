    package nathancs.uel.Prova1AdminNathanSilva;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;

    import java.io.Serializable;

    @Entity
    public class Cardapio implements Serializable {
    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
    @NotBlank(message = "O nome é obrigatório")
        private String nome;

    private String descricao;
    @NotNull(message = "O preço é obrigatório")
    private double preco;

        @Override
        public boolean equals(Object o){
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            return ((Cardapio)o).id == (this.id);
        }


        @Override
        public int hashCode() {
            return id * 12345;
        }

        @ManyToOne
        @JoinColumn(name = "idRestaurante",nullable = false)
        private Restaurante restaurante;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public double getPreco() {
            return preco;
        }

        public void setPreco(double preco) {
            this.preco = preco;
        }


        public Restaurante getRestaurante() {
            return restaurante;
        }

        public void setRestaurante(Restaurante restaurante) {
            this.restaurante = restaurante;
        }
    }
