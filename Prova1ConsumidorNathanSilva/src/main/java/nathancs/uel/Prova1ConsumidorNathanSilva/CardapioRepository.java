package nathancs.uel.Prova1ConsumidorNathanSilva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardapioRepository extends JpaRepository<Cardapio,Integer> {
    List<Cardapio> findByRestauranteId(int idRestaurante);
}
