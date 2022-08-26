package estokapp.stok.vendas.controller;

import estokapp.stok.rh.domain.Funcionario;
import estokapp.stok.rh.repository.FuncionarioRepository;
import estokapp.stok.vendas.domain.Carrinho;
import estokapp.stok.vendas.domain.ItemCarrinho;
import estokapp.stok.vendas.domain.Produto;
import estokapp.stok.vendas.repository.CarrinhoRepository;
import estokapp.stok.vendas.repository.ItemCarrinhoRepository;
import estokapp.stok.vendas.repository.LimiteRepository;
import estokapp.stok.vendas.repository.ProdutoRepository;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProdutoController {

    Produto auxiliar;

    private ProdutoRepository produtoRepo;

    private CarrinhoRepository carrinhoRepo;

    private ItemCarrinhoRepository itemCarrinhoRepo;

    private List<Produto> actualList;

    private LimiteRepository limiteRepo;

    public ProdutoController(ProdutoRepository produtoRepo, CarrinhoRepository carrinhoRepo, ItemCarrinhoRepository itemCarrinhoRepo, LimiteRepository limiteRepo) {
        this.produtoRepo = produtoRepo;
        this.carrinhoRepo = carrinhoRepo;
        this.itemCarrinhoRepo = itemCarrinhoRepo;
        this.limiteRepo = limiteRepo;
    }

    @GetMapping("/estoque")
    public String verEstoque(Model model, @ModelAttribute("produto") Produto produto){

        List<Carrinho> carrinhos = carrinhoRepo.findAll();

        if(carrinhos.size() == 0){
            Carrinho carrinho = new Carrinho();
            carrinhoRepo.save(carrinho);
        }

        List<Produto> aux = new ArrayList<Produto>();
        List<Produto> domain = produtoRepo.findAll();

        if(!domain.isEmpty()){
            aux.add(domain.get(0));
        }

        for (Produto prod:domain) {
            boolean isRepetido = false;
            for (int i = 0; i < aux.size();i++){
                if((prod.getNome().equals(aux.get(i).getNome()))) {
                    isRepetido = true;
                    i = aux.size();
                }
            }
            if(!(isRepetido))aux.add(prod);
        }

        long carrinho_id = 0;

        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
                break;
            }
        }

        List<ItemCarrinho> itens = new ArrayList<>();

        model.addAttribute("estoque", aux);
        model.addAttribute("carrinho", carrinhoRepo.getById(carrinho_id));
        model.addAttribute("itens", carrinhoRepo.getById(carrinho_id).getItens());
        model.addAttribute("limite", limiteRepo.findAll().get(0));
        actualList = aux;
        return "vendas/IEstoque";
    }

    @GetMapping("/estoque/buscar/{code}")
    @ResponseBody
    public Map<String, Object> pesquisarProduto(@PathVariable("code") long code){
        Produto produto = produtoRepo.getById(code);
        Map<String, Object> rtn = new LinkedHashMap<>();

        rtn.put("code", produto.getCode());
        rtn.put("nome", produto.getNome());
        rtn.put("preco", produto.getPreco());
        rtn.put("descricao", produto.getDescricao());
        rtn.put("tamanho", produto.getSize());
        rtn.put("status", produto.getStatus());
        rtn.put("quantidade", produto.getQuantidade());

       return rtn;

    }

    @GetMapping("/estoque/buscar/{size}/{nome}")
    @ResponseBody
    public Map<String, Object> pesquisarProdutoTamanho(@PathVariable("size") int size, @PathVariable("nome") String nome){

        long code_produto = 0;

        for (Produto prd: produtoRepo.findAll()) {
            if((prd.getNome().equals(nome) && (prd.getSize() == size))){
                code_produto =  prd.getCode();
            }
        }


        Map<String, Object> rtn = new LinkedHashMap<>();

        rtn.put("code", code_produto);

        return rtn;

    }

    @GetMapping("/estoque/buscar/tamanhos/{nome}")
    @ResponseBody
    public Map<String, Object> pesquisarProdutoNome(@PathVariable("nome") String nome){
        List<Integer> tamanhos = new ArrayList<Integer>();

        for (Produto prd: produtoRepo.findAll()) {
            if((prd.getNome().equals(nome))){
                tamanhos.add(prd.getSize());
            }
        }

        Map<String, Object> rtn = new LinkedHashMap<>();

        rtn.put("tam", tamanhos);

        return rtn;

    }


    @GetMapping("/estoque/filtrar/alfabetico")
    public String filtrarListaAlfabetico(Model model, @ModelAttribute("produto")Produto produto){

        List<Carrinho> carrinhos = carrinhoRepo.findAll();

        if(carrinhos.size() == 0){
            Carrinho carrinho = new Carrinho();
            carrinhoRepo.save(carrinho);
        }

        List<Produto> domain = actualList;

        long carrinho_id = 0;

        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
                break;
            }
        }

        List<ItemCarrinho> itens = new ArrayList<>();

        List<Produto> filtrado = domain.stream().sorted(Comparator.comparing(Produto::getNome)).collect(Collectors.toList());
        actualList = filtrado;
        model.addAttribute("estoque", filtrado);
        model.addAttribute("carrinho", carrinhoRepo.getById(carrinho_id));
        model.addAttribute("itens", carrinhoRepo.getById(carrinho_id).getItens());
        model.addAttribute("limite", limiteRepo.findAll().get(0));
        return "/vendas/IEstoque";
    }

    @GetMapping("/estoque/filtrar/preco_decrescente")
    public String filtrarListaPrecoCrescente(Model model, @ModelAttribute("produto")Produto produto){

        long carrinho_id = 0;

        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
                break;
            }
        }

        List<Produto> domain = actualList;
        List<Produto> filtrado = domain.stream().sorted(Comparator.comparingDouble(Produto::getPreco)).collect(Collectors.toList());
        Collections.reverse(filtrado);

        List<ItemCarrinho> itens = new ArrayList<>();
        actualList = filtrado;
        model.addAttribute("estoque", filtrado);
        model.addAttribute("carrinho", carrinhoRepo.getById(carrinho_id));
        model.addAttribute("itens", carrinhoRepo.getById(carrinho_id).getItens());
        model.addAttribute("limite", limiteRepo.findAll().get(0));
        return "/vendas/IEstoque";
    }

    @GetMapping("/estoque/filtrar/preco_crescente")
    public String filtrarListaPrecoDecrescente(Model model, @ModelAttribute("produto")Produto produto){

        List<Produto> domain = actualList;
        List<Produto> filtrado = domain.stream().sorted(Comparator.comparingDouble(Produto::getPreco)).collect(Collectors.toList());

        long carrinho_id = 0;

        for (Carrinho carrin: carrinhoRepo.findAll()) {
            if(carrin.getAberto()==1){
                carrinho_id = carrin.getId();
                break;
            }
        }
        actualList = filtrado;
        List<ItemCarrinho> itens = new ArrayList<>();
        model.addAttribute("estoque", filtrado);
        model.addAttribute("carrinho", carrinhoRepo.getById(carrinho_id));
        model.addAttribute("itens", carrinhoRepo.getById(carrinho_id).getItens());
        model.addAttribute("limite", limiteRepo.findAll().get(0));
        return "/vendas/IEstoque";
    }

    @GetMapping("/estoque/filtrar/quantidade_decrescente")
    public String filtrarListaQuantidadeDecrescente(Model model, @ModelAttribute("produto")Produto produto){

        List<Produto> domain = actualList;
        List<Produto> filtrado = domain.stream().sorted(Comparator.comparingInt(Produto::getQuantidade)).collect(Collectors.toList());
        Collections.reverse(filtrado);
        model.addAttribute("estoque", filtrado);
        return "/vendas/IEstoque";
    }

    @GetMapping("/estoque/filtrar/quantidade_crescente")
    public String filtrarListaQuantidadeCrescente(Model model, @ModelAttribute("produto")Produto produto){

        List<Produto> domain = actualList;
        List<Produto> filtrado = domain.stream().sorted(Comparator.comparingInt(Produto::getQuantidade)).collect(Collectors.toList());
        model.addAttribute("estoque", filtrado);
        return "/vendas/IEstoque";
    }

    @GetMapping("/estoque/pesquisar/{string}")
    public String pesquisarProduto(Model model, @PathVariable("string")String string, @ModelAttribute("produto")Produto produto){
            String url = "redirect:/estoque";
            if(!(string.equals("returnBackHomeEstoque"))) {
                List<Produto> ultimate = new ArrayList<Produto>();
                List<Produto> tempNome = new ArrayList<Produto>();
                List<Produto> tempDesc = new ArrayList<Produto>();
                string = string.toLowerCase();

                int M = string.length();
                int N = 0;

                for (Produto prd : actualList) {
                    prd.setNome((prd.getNome()).toLowerCase());
                    N = (prd.getNome()).length();

                    /* A loop to slide pat one by one */
                    for (int i = 0; i <= N - M; i++) {

                        int j;

                    /* For current index i, check for pattern
                      match */
                        for (j = 0; j < M; j++)
                            if (prd.getNome().charAt(i + j) != string.charAt(j))
                                break;

                        if (j == M) // if pat[0...M-1] = txt[i, i+1, ...i+M-1]
                            tempNome.add(prd);
                    }
                }

                for (Produto prd : actualList) {
                    prd.setDescricao((prd.getDescricao()).toLowerCase());
                    N = (prd.getDescricao()).length();

                    /* A loop to slide pat one by one */
                    for (int i = 0; i <= N - M; i++) {

                        int j;

                    /* For current index i, check for pattern
                      match */
                        for (j = 0; j < M; j++)
                            if (prd.getDescricao().charAt(i + j) != string.charAt(j))
                                break;

                        if (j == M) // if pat[0...M-1] = txt[i, i+1, ...i+M-1]
                            tempDesc.add(prd);
                    }
                }

                for (Produto prdNome : tempNome) {
                    ultimate.add(prdNome);
                }

                List<Produto> auxiliar = new ArrayList<Produto>();

                if (ultimate.size() < 1 && tempDesc.size() > 0) {
                    ultimate.add(tempDesc.get(0));
                    auxiliar.add(tempDesc.get(0));
                }

                for (Produto prdDesc : tempDesc) {
                    for (Produto prdUltimate : ultimate) {
                        if (!(prdDesc.getCode() == (prdUltimate.getCode()))) {
                            auxiliar.add(prdDesc);
                        }
                    }
                }

                int count = 0;
                for (Produto aux : auxiliar) {
                    if (count > 0) {
                        ultimate.add(aux);
                    }
                    count++;
                }

                if (string.length() > 0) {
                    model.addAttribute("estoque", ultimate);
                    actualList = ultimate;

                    long carrinho_id = 0;

                    for (Carrinho carrin: carrinhoRepo.findAll()) {
                        if(carrin.getAberto()==1){
                            carrinho_id = carrin.getId();
                            break;
                        }
                    }
                    model.addAttribute("carrinho", carrinhoRepo.getById(carrinho_id));
                    model.addAttribute("itens", carrinhoRepo.getById(carrinho_id).getItens());
                    model.addAttribute("limite", limiteRepo.findAll().get(0));
                    url = "/vendas/Iestoque";
                }else{
                    url = "redirect:/estoque";
                }

            }
        return url;
    }


    @PostMapping("/produto/salvar/{code}")
    public String salvarProdutoId(@ModelAttribute("produto") Produto produto, @PathVariable("code") Long code){
        Produto temp = produtoRepo.getById(code);
        List<Produto> domain = produtoRepo.findAll();
        List<Produto> templist = new ArrayList<Produto>();

        String url = "redirect:/estoque";
        produto.setPreco(temp.getPreco());
        produto.setNome(temp.getNome());
        produto.setDescricao(temp.getDescricao());
        produto.setStatus(temp.getStatus());

        for (Produto prd : domain) {
            if(prd.getNome().equals(temp.getNome()) && prd.getSize() == produto.getSize()){
                url = "redirect:/estoque?error=produto%20ja%20existe";
            }
        }
        if(url.equals("redirect:/estoque")) {
            produtoRepo.save(produto);
        }
        return url;
    }

    @PostMapping("/produto/salvar")
    public String salvarProduto(@ModelAttribute("produto") Produto produto){
        String nameProduct = produto.getNome().toUpperCase();
        List<Produto> domain = produtoRepo.findAll();
        String aux;
        String url = "";

        for (Produto temp : domain) {
            aux = temp.getNome().toUpperCase();
            if(aux.equals(nameProduct)) {
                    url = "redirect:/estoque?error=produto%20ja%20existe";
            }
        }

        if(url.equals("")){
            produto.setStatus("Em estoque");
            produtoRepo.save(produto);
            url = "redirect:/estoque";
        }
        return url;
    }

    @PostMapping("estoque/produto/atualizar/{code}")
    public String atualizarProduto(@PathVariable("code") Long code , @ModelAttribute("produto") Produto produto){
        Produto temp = produtoRepo.getById(code);
        List<Produto> listTemp = new ArrayList<>();
        List<Produto> domain = produtoRepo.findAll();

        for (Produto prd: domain) {
            if(prd.getNome().equals(temp.getNome())){
                listTemp.add(prd);
            }
        }

        for (Produto prd : listTemp) {
            prd.setDescricao(produto.getDescricao());
            prd.setPreco(produto.getPreco());
            produtoRepo.save(prd);
        }

        return "redirect:/estoque";
    }

    @GetMapping("/estoque/produto/excluir/{code}")
    public String excluirProduto(@PathVariable("code") long code){
        long produto_code = 0;
        Optional<Produto> produtoOpt = produtoRepo.findById(code);
        if(produtoOpt.isEmpty()){
            throw new IllegalArgumentException("Produto inválido");
        }
        produtoRepo.delete(produtoOpt.get());

        for (Produto prd: produtoRepo.findAll()) {
            if(prd.getNome().equals(produtoOpt.get().getNome())){
                produto_code = prd.getCode();
            }
        }

        if(produto_code == 0){
            return "redirect:/estoque";
        }else{
            return "redirect:/produto/" + produto_code;
        }
    }

    @GetMapping("/estoque/produto/excluir/nome/{nome}")
    public String excluirProdutoPorNome(@PathVariable("nome") String nome){

        for (Produto prd: produtoRepo.findAll()) {
            if(prd.getNome().equals(nome)){
                produtoRepo.delete(prd);
            }
        }

        return "redirect:/estoque";
    }

    @PostMapping("/estoque/produto/incrementar/{code}/{qtd}")
    public String incrementarProduto(@PathVariable("code") Long code, @PathVariable("qtd") Integer qtd){
        Produto temp = produtoRepo.getById(code);

        temp.setQuantidade(temp.getQuantidade() + qtd);

        produtoRepo.save(temp);

        return "redirect:/estoque";
    }


    @GetMapping("/estoque/produto/atualizar/{code}")
    public String alterarProduto(@PathVariable("code") Long code, Model model) {
        Optional<Produto> produtoOpt = produtoRepo.findById(code);
        if(produtoOpt.isEmpty()){
            throw new IllegalArgumentException("Produto inválido");
        }

        model.addAttribute("produto", produtoOpt.get());

        return "vendas/atualizar";
    }

    @GetMapping("/produto/{code}")
    public String maisDetalhes(@PathVariable("code") Long code, Model model){

        Optional<Produto> produto = produtoRepo.findById(code);
        Produto prdtmp = produtoRepo.getById(code);

        model.addAttribute("produto", prdtmp);

        List<Produto> listaTabela = new ArrayList<>();

        if(produto.isEmpty()){
            return "redirect:/estoque?error=produto%20nao%20existe";
        }else{
            Produto atual = produtoRepo.getById(code);

            for (Produto prd: produtoRepo.findAll()) {
                if(prd.getNome().equals(atual.getNome())){
                    listaTabela.add(prd);
                }
            }

            long carrinho_id = 0;

            for (Carrinho carrin: carrinhoRepo.findAll()) {
                if(carrin.getAberto()==1){
                    carrinho_id = carrin.getId();
                    break;
                }
            }
            model.addAttribute("listaTabela", listaTabela);
            model.addAttribute("carrinho", carrinhoRepo.getById(carrinho_id));

            return "vendas/IProduto";
        }
    }

//    @GetMapping("/images/stocklogo.png")
//    public String redirectBug(){
//        return "redirect:/estoque";
//    }
}


