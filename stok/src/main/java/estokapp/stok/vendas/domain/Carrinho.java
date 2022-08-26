package estokapp.stok.vendas.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float valorTotal = 0;

    private int aberto = 1;

    private double descontoAtual = 0.0;

    private double desconto = 0.0;

    private double valorDescontado = 0.0;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "carrinho_id")
    private List<ItemCarrinho> itens;

    @Deprecated
    public Carrinho() {}

    public Carrinho(Long id, float valorTotal, List<ItemCarrinho> itens) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.itens = itens;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getDescontoAtual() {
        return descontoAtual;
    }

    public void setDescontoAtual(double descontoAtual) {
        this.descontoAtual = descontoAtual;
    }

    public double getValorDescontado() {
        return valorDescontado;
    }

    public void setValorDescontado(double valorDescontado) {
        this.valorDescontado = valorDescontado;
        this.setDesconto(this.valorTotal - this.valorDescontado);
    }

    public int getAberto() {
        return aberto;
    }

    public void setAberto(int aberto) {
        this.aberto = aberto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public void adicionarItem(ItemCarrinho produto) {
        boolean repete = false;
        int item_id_index = 0;
        for (ItemCarrinho item: this.getItens()) {

            if(item.getProd().getCode().equals(produto.getProd().getCode())){
                repete = true;
            }

            if(!(repete)){
                item_id_index++;
            }
        }
        if(!(repete)){
            this.itens.add(produto);
        }else{
            int qtdatual = this.itens.get(item_id_index).getQtde();
            this.itens.get(item_id_index).setQtde(qtdatual+1);

        }
        this.valorTotal += produto.getProd().getPreco();
        this.desconto = this.valorTotal - this.desconto;
        this.valorDescontado = this.valorTotal * ((100 - this.descontoAtual)/100);
    }
}
