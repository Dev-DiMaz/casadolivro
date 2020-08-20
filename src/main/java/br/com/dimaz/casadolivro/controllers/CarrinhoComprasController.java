package br.com.dimaz.casadolivro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import br.com.dimaz.casadolivro.daos.ProdutoDAO;
import br.com.dimaz.casadolivro.models.CarrinhoCompras;
import br.com.dimaz.casadolivro.models.CarrinhoItem;
import br.com.dimaz.casadolivro.models.Produto;
import br.com.dimaz.casadolivro.models.TipoPreco;

@Controller
@RequestMapping("/carrinho")
@Scope(value=WebApplicationContext.SCOPE_REQUEST)
public class CarrinhoComprasController {

    @Autowired
    private ProdutoDAO produtoDao;

    @Autowired
    private CarrinhoCompras carrinho;

    @RequestMapping("/add")
    public ModelAndView add(Integer produtoId, TipoPreco tipo) {

        ModelAndView modelAndView = new ModelAndView("redirect:/carrinho");
        CarrinhoItem carrinhoItem = criaItem(produtoId,tipo);

        carrinho.add(carrinhoItem);

        return modelAndView;   
    }

    private CarrinhoItem criaItem(Integer produtoId, TipoPreco tipoPreco) {

        Produto produto = produtoDao.find(produtoId);
        CarrinhoItem carrinhoItem = new CarrinhoItem(produto, tipoPreco);

        return carrinhoItem;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView itens(){
        return new ModelAndView("carrinho/itens");
    }
    
    @RequestMapping("/remover")
    public ModelAndView remover(Integer produtoId, TipoPreco tipoPreco) {
        carrinho.remover(produtoId, tipoPreco);
        return new ModelAndView("redirect:/carrinho");
    }
}