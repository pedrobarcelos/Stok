package estokapp.stok.vendas.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long code;

    private String nome;

    private Float preco;

    private String descricao;

    private Integer size;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "prod_code")
    private List<ItemCarrinho> itens;

    private String status;

    private Integer quantidade;

    public Produto(){}

    @Deprecated
    public Produto(String nome){
        this.nome = nome;
    }

    public Long getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return code.equals(produto.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "code=" + code +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", descricao='" + descricao + '\'' +
                ", size=" + size +
                ", status='" + status + '\'' +
                ", quantidade=" + quantidade +
                '}';
    }
}
