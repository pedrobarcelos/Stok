package estokapp.stok.vendas.controller;

import estokapp.stok.vendas.domain.Limites;
import estokapp.stok.vendas.repository.CarrinhoRepository;
import estokapp.stok.vendas.repository.ProdutoRepository;
import estokapp.stok.vendas.repository.VendaRepository;
import estokapp.stok.vendas.repository.LimiteRepository;
import estokapp.stok.vendas.repository.ItemCarrinhoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LimiteController {

    private CarrinhoRepository carrinhoRepo;

    private ProdutoRepository produtoRepo;

    private ItemCarrinhoRepository itemCarrinhoRepo;

    private VendaRepository vendaRepo;

    private LimiteRepository limiteRepo;

    public LimiteController(CarrinhoRepository carrinhoRepo, ProdutoRepository produtoRepo, ItemCarrinhoRepository itemCarrinhoRepo, VendaRepository vendaRepo, LimiteRepository limiteRepo) {
        this.carrinhoRepo = carrinhoRepo;
        this.produtoRepo = produtoRepo;
        this.itemCarrinhoRepo = itemCarrinhoRepo;
        this.vendaRepo = vendaRepo;
        this.limiteRepo = limiteRepo;
    }

    @PostMapping("/limite/definir")
    public String definirLimite(@ModelAttribute("limite")Limites limite){
        limiteRepo.save(limite);
        return "redirect:/admin/painel";
    }
}
