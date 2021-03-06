package br.com.unifieo.tmc.repository;

import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.domain.SolicitaRemocaoComentario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the SolicitaRemocaoComentario entity.
 */
public interface SolicitaRemocaoComentarioRepository extends JpaRepository<SolicitaRemocaoComentario, Long> {

    SolicitaRemocaoComentario findOneByComentario(Comentario comentario);

    SolicitaRemocaoComentario findOneByComentarioAndAprovado(Comentario comentario, Boolean aprovado);
}
