package com.shopmate.app.web.rest;

import com.shopmate.app.domain.Vendor;
import com.shopmate.app.repository.VendorRepository;
import com.shopmate.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.shopmate.app.domain.Vendor}.
 */
@RestController
@RequestMapping("/api/vendors")
@Transactional
public class VendorResource {

    private final Logger log = LoggerFactory.getLogger(VendorResource.class);

    private static final String ENTITY_NAME = "vendor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendorRepository vendorRepository;

    public VendorResource(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    /**
     * {@code POST  /vendors} : Create a new vendor.
     *
     * @param vendor the vendor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vendor, or with status {@code 400 (Bad Request)} if the vendor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) throws URISyntaxException {
        log.debug("REST request to save Vendor : {}", vendor);
        if (vendor.getId() != null) {
            throw new BadRequestAlertException("A new vendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vendor = vendorRepository.save(vendor);
        return ResponseEntity.created(new URI("/api/vendors/" + vendor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, vendor.getId().toString()))
            .body(vendor);
    }

    /**
     * {@code PUT  /vendors/:id} : Updates an existing vendor.
     *
     * @param id the id of the vendor to save.
     * @param vendor the vendor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendor,
     * or with status {@code 400 (Bad Request)} if the vendor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vendor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vendor vendor)
        throws URISyntaxException {
        log.debug("REST request to update Vendor : {}, {}", id, vendor);
        if (vendor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vendor = vendorRepository.save(vendor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendor.getId().toString()))
            .body(vendor);
    }

    /**
     * {@code PATCH  /vendors/:id} : Partial updates given fields of an existing vendor, field will ignore if it is null
     *
     * @param id the id of the vendor to save.
     * @param vendor the vendor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendor,
     * or with status {@code 400 (Bad Request)} if the vendor is not valid,
     * or with status {@code 404 (Not Found)} if the vendor is not found,
     * or with status {@code 500 (Internal Server Error)} if the vendor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vendor> partialUpdateVendor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vendor vendor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vendor partially : {}, {}", id, vendor);
        if (vendor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vendor> result = vendorRepository
            .findById(vendor.getId())
            .map(existingVendor -> {
                if (vendor.getName() != null) {
                    existingVendor.setName(vendor.getName());
                }
                if (vendor.getCity() != null) {
                    existingVendor.setCity(vendor.getCity());
                }

                return existingVendor;
            })
            .map(vendorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendor.getId().toString())
        );
    }

    /**
     * {@code GET  /vendors} : get all the vendors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendors in body.
     */
    @GetMapping("")
    public List<Vendor> getAllVendors() {
        log.debug("REST request to get all Vendors");
        return vendorRepository.findAll();
    }

    /**
     * {@code GET  /vendors/:id} : get the "id" vendor.
     *
     * @param id the id of the vendor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vendor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendor(@PathVariable("id") Long id) {
        log.debug("REST request to get Vendor : {}", id);
        Optional<Vendor> vendor = vendorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vendor);
    }

    /**
     * {@code DELETE  /vendors/:id} : delete the "id" vendor.
     *
     * @param id the id of the vendor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable("id") Long id) {
        log.debug("REST request to delete Vendor : {}", id);
        vendorRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
