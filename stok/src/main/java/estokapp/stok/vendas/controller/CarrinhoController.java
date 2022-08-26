package estokapp.stok.vendas.controller;

import estokapp.stok.vendas.domain.Carrinho;
import estokapp.stok.vendas.domain.ItemCarrinho;
import estokapp.stok.vendas.domain.Produto;
import estokapp.stok.vendas.domain.Venda;
import estokapp.stok.vendas.repository.CarrinhoRepository;
import estokapp.stok.vendas.repository.VendaRepository;
import estokapp.stok.vendas.repository.ProdutoRepository;
import estokapp.stok.vendas.repository.ItemCarrinhoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class CarrinhoController {

    private CarrinhoRepository carrinhoRepo;

    private ProdutoRepository produtoRepo;

    private ItemCarrinhoRepository itemCarrinhoRepo;

    private VendaRepository vendaRepo;

    public CarrinhoController(CarrinhoRepository carrinhoRepo, ProdutoRepository produtoRepo, ItemCarrinhoRepository itemCarrinhoRepo, VendaRepository vendaRepo) {
        this.carrinhoRepo = carrinhoRepo;
        this.produtoRepo = produtoRepo;
        this.itemCarrinhoRepo = itemCarrinhoRepo;
        this.vendaRepo = vendaRepo;
    }

    @GetMapping("/carrinho")
    public String verCarrinho(){
        return "/vendas/Carrinho";
    }

    @GetMapping("/carrinho/limpar")
    public String limpar(){
        carrinhoRepo.deleteAll();
        return  "redirect:/estoque";
    }

    @PostMapping("/carrinho/adicionar/{produto_id}")
    public String adicionarAoCarrinho(@PathVariable("produto_id") Long produto_id){

        long carrinho_id = 0;
        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
            }
        }


        Carrinho carrinho = carrinhoRepo.getById(carrinho_id);
        Produto produto = produtoRepo.getById(produto_id);
        ItemCarrinho item = new ItemCarrinho(1,carrinhoRepo.getById(carrinho_id), produtoRepo.getById(produto_id));
        boolean repete = false;
        boolean limite = false;
        int item_id_index = 0;
        int qtd = 0;
        for (ItemCarrinho itemTmp: carrinho.getItens()) {

            if(itemTmp.getProd().getCode().equals(produto.getCode())){
                repete = true;
                qtd = itemTmp.getQtde();
            }

            if(!(repete)){
                item_id_index++;
            }
        }

        if(produto.getQuantidade() < qtd+1){
            return "redirect:/estoque?error=quantidadeLimite";
        }else{
            if(!(repete)){
                itemCarrinhoRepo.save(item);
                carrinho.setValorTotal((carrinho.getValorTotal()) + item.getProd().getPreco());
                carrinho.setValorDescontado((carrinho.getValorTotal() * ((100 - carrinho.getDescontoAtual())/100)));
                double valordescon = carrinho.getValorDescontado();
            }else{
                carrinho.adicionarItem(item);
            }
            carrinhoRepo.save(carrinho);
        }

        return "redirect:/estoque";
    }

    @GetMapping("/carrinho/finalizar")
    public String finalizar(){

        long carrinho_id = 0;
        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
            }
        }
        Carrinho carrinho = carrinhoRepo.getById(carrinho_id);
        String str = "";
        if(!(carrinho.getValorTotal() == 0)) {
            int quantidade = 0;
            for (ItemCarrinho item : carrinho.getItens()) {
                quantidade = item.getQtde();
                long code_prd = item.getProd().getCode();
                Produto produtotmp = produtoRepo.getById(code_prd);
                produtotmp.setQuantidade(produtotmp.getQuantidade() - quantidade);

                if (produtotmp.getQuantidade() <= 0) {
                    produtoRepo.delete(produtotmp);
                } else {
                    produtoRepo.save(produtotmp);
                }
            }
            str = "redirect:/venda/salvar/" + carrinho_id + "/" + 0;
        }else{
            str = "redirect:/estoque?error=CarrinhoVazio";
        }
        return str;
    }

    @GetMapping("/item/excluir/{code}")
    public String excluirItem(@PathVariable("code") long code){

        long carrinho_id = 0;
        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
            }
        }

        ItemCarrinho itemOpt = itemCarrinhoRepo.getById(code);

        Carrinho carrinho = carrinhoRepo.getById(carrinho_id);
        carrinho.setValorTotal(carrinho.getValorTotal() - itemOpt.getValorInteiro());

        carrinhoRepo.save(carrinho);
        itemCarrinhoRepo.delete(itemOpt);

        return "redirect:/estoque";
    }

    @PostMapping("/carrinho/alterar/desconto/{desconto}")
    public String descontar(@PathVariable("desconto") double desconto){

        long carrinho_id = 0;
        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
            }
        }

        Carrinho carrinho = carrinhoRepo.getById(carrinho_id);

        carrinho.setDescontoAtual(desconto);
        carrinho.setValorDescontado(carrinho.getValorTotal() * ((100.00 - desconto)/100));
        carrinho.setDesconto(carrinho.getValorTotal() - carrinho.getValorDescontado());
        carrinhoRepo.save(carrinho);

        return "redirect:/estoque";
    }
}
