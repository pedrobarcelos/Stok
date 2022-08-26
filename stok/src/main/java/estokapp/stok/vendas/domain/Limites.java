package estokapp.stok.vendas.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Limites {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double descontoMAX = 0.0;

    @Deprecated
    public Limites() {
    }

    public Limites(double descontoMAX) {
        this.descontoMAX = descontoMAX;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDescontoMAX() {
        return descontoMAX;
    }

    public void setDescontoMAX(double descontoMAX) {
        this.descontoMAX = descontoMAX;
    }
}
