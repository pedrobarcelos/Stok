package estokapp.stok.vendas.repository;

import estokapp.stok.vendas.domain.Produto;
import estokapp.stok.vendas.domain.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {

}
