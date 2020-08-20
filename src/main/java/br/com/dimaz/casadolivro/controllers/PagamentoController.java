package br.com.dimaz.casadolivro.controllers;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.dimaz.casadolivro.models.CarrinhoCompras;
import br.com.dimaz.casadolivro.models.DadosPagamento;

@RequestMapping("/pagamento")
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PagamentoController {

    @Autowired
    private CarrinhoCompras carrinho;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value="/finalizar", method=RequestMethod.POST)
    public Callable<ModelAndView> finalizar(RedirectAttributes model){		// Callable (pra servidor se comportar ASSINCRONO)
        return () -> {														// classe anonima para o retorno do Callable 
            String uri = "http://book-payment.herokuapp.com/payment";		// envio precisa ser no formato {"value": 500} por isso a classe DadosPagamento

            try {
                String response = restTemplate.postForObject(uri, 
                    new DadosPagamento(carrinho.getTotal()), String.class);
                model.addFlashAttribute("sucesso", response);
                System.out.println(response);
                return new ModelAndView("redirect:/produtos");
            } catch (HttpClientErrorException e) {
                e.printStackTrace();
                model.addFlashAttribute("falha", "Valor maior que o permitido");
                return new ModelAndView("redirect:/produtos");
            }
        };
    }
}