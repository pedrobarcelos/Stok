package estokapp.stok.rh.controller;

import estokapp.stok.rh.domain.Funcionario;
import estokapp.stok.rh.repository.FuncionarioRepository;
import estokapp.stok.vendas.domain.Limites;
import estokapp.stok.vendas.repository.LimiteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
@Controller
public class FuncionarioController {

    Funcionario auxiliar;

    private FuncionarioRepository funcionarioRepo;
    private LimiteRepository limiteRepo;

    public FuncionarioController(FuncionarioRepository funcionarioRepo, LimiteRepository limiteRepo) {
        this.funcionarioRepo = funcionarioRepo;
        this.limiteRepo = limiteRepo;
    }

    @GetMapping("/admin/painel")
    public String Funcionarios(Model model){
        if(limiteRepo.findAll().size() > 0){
            model.addAttribute("limite", limiteRepo.findAll().get(0));
        }else{
            Limites limite = new Limites();
            model.addAttribute("limite", limite );
        }
        model.addAttribute("listafuncionarios", funcionarioRepo.findAll());
        return "rh/funcionarios/index";
    }

    @GetMapping("admin/rh/funcionario/novo")
    public String novoFuncionario(@ModelAttribute("funcionario") Funcionario funcionario){
        return "rh/funcionario/cadastro";
    }

    @PostMapping("/rh/funcionario/salvar")
    public String salvarFuncionario(@ModelAttribute("funcionario") Funcionario funcionario){
        funcionarioRepo.save(funcionario);
        return "redirect:/admin/painel";
    }

    @PostMapping("rh/funcionario/atualizar/{id}")
    public String atualizarFuncioanrio(@PathVariable("id") Long id ,@ModelAttribute("funcionario") Funcionario funcionario){
        funcionario.setID(id);
        funcionarioRepo.save(funcionario);
        return "redirect:/admin/painel";
    }

    @GetMapping("/rh/funcionario/atualizar/{id}")
    public String alterarFuncionario(@PathVariable("id") Long id, Model model) {
        Optional<Funcionario> clienteOpt = funcionarioRepo.findById(id);
        if(clienteOpt.isEmpty()){
            throw new IllegalArgumentException("Cliente inválido");
        }else{
            auxiliar = clienteOpt.get();
        }

        model.addAttribute("funcionario", clienteOpt.get());

        return "rh/funcionario/atualizar";
    }

    @GetMapping("admin/rh/funcionario/login")
    public String login(@ModelAttribute("funcionario") Funcionario funcionario){
        return "rh/funcionario/login";
    }

    @GetMapping("/rh/funcionario/excluir/{id}")
    public String excluirFuncionario(@PathVariable("id") long id){
        Optional<Funcionario> funcionarioOpt = funcionarioRepo.findById(id);
        if(funcionarioOpt.isEmpty()){
            throw new IllegalArgumentException("funcionario inválido");
        }
        funcionarioRepo.delete(funcionarioOpt.get());

        return "redirect:/admin/painel";
    }


}

