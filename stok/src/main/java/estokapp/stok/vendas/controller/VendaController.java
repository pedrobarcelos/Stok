package estokapp.stok.vendas.controller;

import estokapp.stok.vendas.domain.Carrinho;
import estokapp.stok.vendas.domain.ItemCarrinho;
import estokapp.stok.vendas.domain.Venda;
import estokapp.stok.vendas.repository.CarrinhoRepository;
import estokapp.stok.vendas.repository.ProdutoRepository;
import estokapp.stok.vendas.repository.VendaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class VendaController {

    private VendaRepository vendaRepo;

    private CarrinhoRepository carrinhoRepo;

    private ProdutoRepository produtoRepository;

    public VendaController(VendaRepository vendaRepository, CarrinhoRepository carrinhoRepo, ProdutoRepository produtoRepository) {
        this.vendaRepo = vendaRepository;
        this.carrinhoRepo = carrinhoRepo;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/vendas/historico")
    public String verHistorico(Model model){
        model.addAttribute("historico", vendaRepo.findAll());

        return "/vendas/historicoVendas";
    }

    @GetMapping("/venda/salvar/{carrinho}/{desconto}")
    public String vendaSalvar(@PathVariable("carrinho") Long carrinho_id, @PathVariable("desconto") float desconto){
        Carrinho carrinho = carrinhoRepo.getById(carrinho_id);
        List<ItemCarrinho> itensCarrin = carrinho.getItens();
        double valortotal = 0;
        if(carrinho.getDesconto() > 0.0){
            valortotal = carrinho.getValorDescontado();
        }else {
            valortotal = carrinho.getValorTotal();
        }
        Venda venda = new Venda(itensCarrin.size(), valortotal, 0.0);
        vendaRepo.save(venda);
        carrinhoRepo.deleteAll();
        return "redirect:/vendas/historico";
    }
}
