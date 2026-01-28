package com.andreadisimone.repository.host_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.model.Host;

public interface HostDAO {
    

    // ==================== CREATE ====================

    Host create(Host Host);

    // ==================== READ ====================

    List<Host> findAllHosts(Host idHost);

    Optional<Host> findById(Integer idHost);

    // ==================== UPDATE ====================

    Optional<Host> update(Host Host);

    // ==================== DELETE ====================

    int deleteAllHosts();

    boolean deleteById(Integer idHost);

    

}
