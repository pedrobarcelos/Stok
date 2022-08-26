package estokapp.stok.vendas.domain;

import org.springframework.security.core.parameters.P;

import javax.persistence.*;

@Entity
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Produto prod;

    private int qtde;

    private float valorInteiro = 0;

//    @ManyToOne
//    private Venda venda;

    @ManyToOne
    private Carrinho carrinho;

    @Deprecated
    public ItemCarrinho(){}

    public ItemCarrinho(int qtde, Carrinho carrinho, Produto produto) {
        this.prod = produto;
        this.qtde = qtde;
        this.carrinho = carrinho;
        this.valorInteiro = produto.getPreco();
    }

    public Long getId() {
        return id;
    }

    public float getValorInteiro() {
        return valorInteiro;
    }

    public void setValorInteiro(float valorInteiro) {
        this.valorInteiro = valorInteiro;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProd() {
        return prod;
    }

    public void setProd(Produto prod) {
        this.prod = prod;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
        this.valorInteiro = prod.getPreco() * qtde;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
}
