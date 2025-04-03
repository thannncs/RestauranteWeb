package nathancs.uel.Prova1AdminNathanSilva;



import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CardapioController {

    @Autowired
    CardapioRepository cardapioRepository;
    @Autowired
    RestauranteRepository restauranteRepository;


    @GetMapping("/novo-cardapio/{idRestaurante}")
    public String mostrarFormNovoCardapio(@PathVariable("idRestaurante") int idRestaurante, Cardapio cardapio, Model model) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(idRestaurante);
        Restaurante restaurante = restauranteOptional.orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + idRestaurante));
        model.addAttribute("restaurante", restaurante);
        return "novo-cardapio";
    }


    @GetMapping("/index-cardapios/{idRestaurante}")
    public String mostrarListaCardapios(@PathVariable("idRestaurante") int idRestaurante, Model model) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(idRestaurante);
        Restaurante restaurante = restauranteOptional.get();
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("cardapios", restaurante.getCardapios());
        return "index-cardapios";
    }



    @PostMapping("/adicionar-cardapio/{idRestaurante}")
    public String adicionarCardapio(@PathVariable("idRestaurante") int idRestaurante,@Valid Cardapio cardapio, BindingResult result) {
        if (result.hasErrors()) {
            return "novo-cardapio";
        }
        ;
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(idRestaurante);
        Restaurante restaurante = restauranteOptional.orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + idRestaurante));

        cardapio.setRestaurante(restaurante);


        cardapioRepository.save(cardapio);

        return "redirect:/index-cardapios/" + restaurante.getId();
    }


    @GetMapping("/editar-cardapio/{id}")
    public String mostrarFormAtualizarCardapio(@PathVariable("id") int id, Model model) {
        Cardapio cardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do cardápio é inválido:" + id));

        model.addAttribute("cardapio", cardapio);
        model.addAttribute("restaurantes", restauranteRepository.findAll());
        return "atualizar-cardapio";
    }

    @PostMapping("/atualizar-cardapio/{id}")
    public String atualizarCardapio(@PathVariable("id") int id, @Valid Cardapio cardapio, BindingResult result, Model model) {
        if (result.hasErrors()) {
            cardapio.setId(id);
            model.addAttribute("restaurantes", restauranteRepository.findAll());
            return "atualizar-cardapio";
        }

        Cardapio updatedCardapio = cardapioRepository.save(cardapio);
        int restauranteId = updatedCardapio.getRestaurante().getId();
        return "redirect:/index-cardapios/" + restauranteId;
    }


    @GetMapping("/remover-cardapio/{id}")
    public String removerCardapio(@PathVariable("id") int id) {
        Cardapio cardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do cardápio é inválido:" + id));

        int restauranteId = cardapio.getRestaurante().getId();


        cardapioRepository.delete(cardapio);

        return "redirect:/index-cardapios/" + restauranteId;
    }




}
