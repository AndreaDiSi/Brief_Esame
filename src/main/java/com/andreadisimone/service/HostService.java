package com.andreadisimone.service;

import java.util.List;

import com.andreadisimone.dtos.host_dtos.HostRequestDTO;
import com.andreadisimone.dtos.host_dtos.HostResponseDTO;
import com.andreadisimone.exceptions.not_found.HostNotFoundException;
import com.andreadisimone.repository.host_daos.HostDAO;
import com.andreadisimone.repository.host_daos.HostDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HostService {

    //dependency inversion per principi SOLID
    // passo l'interfaccia come attributo nello stato
    // inzializzo l'implementazione dell interfaccia assegnandolo all'interfaccia
    private final HostDAO hostDAO;

    public HostService() {
        hostDAO = new HostDAOImpl();

    }

    public HostResponseDTO insertHost(HostRequestDTO host) {
        log.info("Trying to insert host - name: {}", host.getHostName());

        try {

            HostRequestDTO host2save = new HostRequestDTO(host.getHostName(), host.getSurname(), host.getEmail(), host.getHostAddress(), host.isSuperhost());

            //manda request e riceve response
            return hostDAO.create(host2save);

        } catch (HostNotFoundException e) {
            log.error("host with name {} not found", host.getHostName());
            throw new HostNotFoundException("host with name " + host.getHostName() + " not found");
        }
    }

    public List<HostResponseDTO> getAllhost() {
        log.info("Trying to get all hosts");

        return hostDAO.findAll();
    }

    ;

    public HostResponseDTO getBestHost() {
        log.info("Trying to get best host");

        return hostDAO.getBestHost();
    }

    public List<HostResponseDTO> getAllSuperHosts() {
        log.info("Trying to get all superhosts");
        return hostDAO.getAllSuperHosts();
    }

    

    public boolean deleteById(Integer idhost) {
        log.info("Trying to delete host with id: {}", idhost);

        return hostDAO.deleteById(idhost);
    }

    public HostResponseDTO update(Integer idhost, HostResponseDTO host) {
        log.info("Trying to update host with id: {}", idhost);

        host.setIdHost(idhost);

        return hostDAO.update(idhost, host);
    }
}
