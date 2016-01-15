package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.service.MoradorService;
import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.repository.MoradorRepository;
import br.com.unifieo.tmc.web.rest.util.HeaderUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Morador.
 */
@RestController
@RequestMapping("/api")
public class MoradorResource {

    private final Logger log = LoggerFactory.getLogger(MoradorResource.class);

    @Inject
    private MoradorRepository moradorRepository;

    @Inject
    private MoradorService moradorService;

    /**
     * POST  /moradors -> Create a new morador.
     */
    @RequestMapping(value = "/moradors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Morador> createMorador(@Valid @RequestBody Morador morador) throws URISyntaxException {
        log.debug("REST request to save Morador : {}", morador);
        if (morador.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new morador cannot already have an ID").body(null);
        }
        Morador result = moradorService.save(morador);
        return ResponseEntity.created(new URI("/api/moradors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("morador", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /moradors -> Updates an existing morador.
     */
    @RequestMapping(value = "/moradors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Morador> updateMorador(@Valid @RequestBody Morador morador) throws URISyntaxException {
        log.debug("REST request to update Morador : {}", morador);
        if (morador.getId() == null) {
            return createMorador(morador);
        }
        Morador result = moradorRepository.save(morador);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("morador", morador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /moradors -> get all the moradors.
     */
    @RequestMapping(value = "/moradors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Morador> getAllMoradors() {
        log.debug("REST request to get all Moradors");
        return moradorRepository.findAll();
    }

    /**
     * GET  /moradors/:id -> get the "id" morador.
     */
    @RequestMapping(value = "/moradors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Morador> getMorador(@PathVariable Long id) {
        log.debug("REST request to get Morador : {}", id);
        return Optional.ofNullable(moradorRepository.findOne(id))
            .map(morador -> new ResponseEntity<>(
                morador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /moradors/:id -> delete the "id" morador.
     */
    @RequestMapping(value = "/moradors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMorador(@PathVariable Long id) {
        log.debug("REST request to delete Morador : {}", id);
        moradorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("morador", id.toString())).build();
    }

    @RequestMapping(value = "/moradors/print",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> printRegisterCode(HttpServletRequest request) {

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        String url = moradorService.getUrlRegisterCode(baseUrl);
        JsonObject json = new JsonObject();
        json.addProperty("url", url);
        return new ResponseEntity<>(new Gson().toJson(json), HttpStatus.OK);
    }


}
