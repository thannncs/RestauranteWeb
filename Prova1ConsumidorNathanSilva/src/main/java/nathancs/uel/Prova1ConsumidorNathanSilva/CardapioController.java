package nathancs.uel.Prova1ConsumidorNathanSilva;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CardapioController {

    @Autowired
    CardapioRepository cardapioRepository;
    @Autowired
    RestauranteRepository restauranteRepository;
    private static final String SESSION_PEDIDOS = "sessionPedidos";


    @GetMapping("/mostrar-restaurantes")
    public String mostrarListaRestaurante(Model model){
            model.addAttribute("restaurantes",restauranteRepository.findAll());
            return "lista-restaurantes";
        }

    @GetMapping("/index-cardapios/{idRestaurante}")
    public String mostrarListaCardapios(@PathVariable("idRestaurante") int idRestaurante, Model model) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(idRestaurante);
        Restaurante restaurante = restauranteOptional.get();
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("cardapios", restaurante.getCardapios());
        return "index-cardapios";
    }


    @GetMapping("/mostrar-pedido")
    public String mostrarListaPedido(Model model, HttpServletRequest request){
        List<Pedido> pedidos = (List<Pedido>) request.getSession().getAttribute(SESSION_PEDIDOS);
        List<Pedido> pedidosExistentes = new ArrayList<>();
        double valorTotal = 0;

        if(pedidos!=null) {
            for (Pedido pedido : pedidos) {
                Optional<Restaurante> restauranteOptional = restauranteRepository.findById(pedido.getCardapio().getRestaurante().getId());
                Optional<Cardapio> cardapioOptional = cardapioRepository.findById(pedido.getCardapio().getId());
                if (restauranteOptional.isPresent() && cardapioOptional.isPresent()) {
                    Restaurante restaurante = restauranteOptional.get();
                    Cardapio cardapio = cardapioOptional.get();
                    pedido.getCardapio().setRestaurante(restaurante);
                    pedido.setCardapio(cardapio);
                    pedidosExistentes.add(pedido);
                    valorTotal += pedido.getCardapio().getPreco()*pedido.getQuantidade();
                }
            }
            request.getSession().setAttribute(SESSION_PEDIDOS, pedidosExistentes);
            model.addAttribute("valorTotal",valorTotal);
            model.addAttribute("sessionPedidos",
                    !CollectionUtils.isEmpty(pedidosExistentes) ? pedidosExistentes : new ArrayList<>());
            return "pedidos";
        }
        model.addAttribute(valorTotal);
        model.addAttribute("sessionPedidos",
                !CollectionUtils.isEmpty(pedidos) ? pedidos : new ArrayList<>());
        return "pedidos";
    }




    @GetMapping(value = {"/index","/"})
    public String mostrarTelaInicial(Model model){
        return "index";
    }

    @GetMapping("/criar-pedido/{id}")
    public String fazerPedido(@PathVariable("id") int id, HttpServletRequest request){
        Cardapio cardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do cardapio é inválido: " + id));


        List<Pedido> pedidos =
                (List<Pedido>)request.getSession().getAttribute(SESSION_PEDIDOS);


        if (CollectionUtils.isEmpty(pedidos)) {
            pedidos = new ArrayList<>();
        }

        boolean primeiro = true;
        for(Pedido pedido:pedidos){
            if(pedido.getCardapio().getId()==id){
                pedido.setQuantidade(pedido.getQuantidade()+1);
                primeiro = false;
                break;
            }
        }

        if(primeiro==true) {
            Pedido pedido = new Pedido(cardapio, 1);
            pedidos.add(pedido);
        }
        request.getSession().setAttribute(SESSION_PEDIDOS,pedidos);


        return "redirect:/mostrar-pedido";
    }

    @GetMapping("/pedidos/remover/{id}")
    public String removerPedido(@PathVariable("id") int id, HttpServletRequest request) {


        List<Pedido> sessionPedidos =
                (List<Pedido>) request.getSession().getAttribute(SESSION_PEDIDOS);


        Pedido pedidoToRemove = null;
        for (Pedido pedido : sessionPedidos) {
            if (pedido.getCardapio().getId() == id) {
                pedidoToRemove = pedido;
                break;
            }
        }

        if (pedidoToRemove != null) {
            sessionPedidos.remove(pedidoToRemove);
        }

        request.getSession().setAttribute(SESSION_PEDIDOS, sessionPedidos);
        return "redirect:/mostrar-pedido";
    }

    @GetMapping("/aumentar-quantidade/{id}")
    public String aumentarQuantidade(@PathVariable("id") int id, HttpServletRequest request) {
        List<Pedido> sessionPedidos = (List<Pedido>) request.getSession().getAttribute(SESSION_PEDIDOS);

        for (Pedido pedido : sessionPedidos) {
            if (pedido.getId() == id) {
                pedido.setQuantidade(pedido.getQuantidade() + 1);
                break;
            }
        }

        request.getSession().setAttribute(SESSION_PEDIDOS, sessionPedidos);
        return "redirect:/mostrar-pedido";
    }

    @GetMapping("/diminuir-quantidade/{id}")
    public String diminuirQuantidade(@PathVariable("id") int id, HttpServletRequest request) {
        List<Pedido> sessionPedidos = (List<Pedido>) request.getSession().getAttribute(SESSION_PEDIDOS);

        Pedido pedidoToRemove = null;
        for (Pedido pedido : sessionPedidos) {
            if (pedido.getId() == id) {
                int quantidade = pedido.getQuantidade();
                if (quantidade > 1) {
                    pedido.setQuantidade(quantidade - 1);
                } else {
                    pedidoToRemove = pedido;
                }
                break;
            }
        }

        if (pedidoToRemove != null) {
            sessionPedidos.remove(pedidoToRemove);
        }

        request.getSession().setAttribute(SESSION_PEDIDOS, sessionPedidos);
        return "redirect:/mostrar-pedido";
    }



}









