package estokapp.stok.vendas.repository;

import estokapp.stok.vendas.domain.Limites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimiteRepository extends JpaRepository<Limites, Long> {
}
