package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.repository.CepRepository;
import br.com.unifieo.tmc.repository.CondominioRepository;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.web.rest.dto.CondominioDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class CondominioService {

    private final Logger log = LoggerFactory.getLogger(CondominioService.class);

    private final CondominioRepository condominioRepository;
    private final CepRepository cepRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Inject
    public CondominioService(CondominioRepository condominioRepository, CepRepository cepRepository, FuncionarioRepository funcionarioRepository) {
        this.condominioRepository = condominioRepository;
        this.cepRepository = cepRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public Condominio save(CondominioDTO condominioDto) {
        Condominio condominio = new Condominio(condominioDto);
        cepRepository.save(condominio.getCep());
        Condominio condominioSaved = condominioRepository.save(condominio);
        condominioDto.setId(condominioSaved.getId());
        Funcionario funcionario = new Funcionario(condominioDto, condominio);
        funcionario.setResponsavel(true);
        cepRepository.save(funcionario.getCep());
        funcionarioRepository.save(funcionario);
        condominioDto.setId(condominio.getId());
        return condominio;
    }

    public void delete(Long id) {
        Condominio condominio = condominioRepository.findOne(id);
        Funcionario funcionario = funcionarioRepository.findOneByCondominioAndResponsavel(condominio, true);
        funcionario.setResponsavel(false);
        funcionario.setCondominio(null);
        funcionarioRepository.save(funcionario);
        condominioRepository.delete(id);
    }
}
