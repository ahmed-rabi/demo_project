package com.example.demo.controller;

import com.example.demo.entity.RestaurantTable;
import com.example.demo.service.TableService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
@PreAuthorize("hasRole('ADMIN')")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTable> getTableById(@PathVariable Long id) {
        return tableService.getTableById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @PostMapping("/createTable")
    public ResponseEntity<RestaurantTable> createTable(@RequestBody RestaurantTable table) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tableService.saveTable(table));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTable> updateTable(
            @PathVariable Long id,
            @RequestBody RestaurantTable table) {
        return tableService.getTableById(id)
                .map(existingTable -> {
                    table.setTableId(id);
                    return ResponseEntity.ok(tableService.saveTable(table));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        if (tableService.getTableById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/generate-qr")
    public ResponseEntity<RestaurantTable> generateTableQRCode(@PathVariable Long id) {
        try {
            RestaurantTable table = tableService.generateQRCodeForTable(id);
            return ResponseEntity.ok(table);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException | WriterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get QR code image for a table
     */
    @GetMapping("/{id}/qr-code")
    public ResponseEntity<byte[]> getTableQRCode(@PathVariable Long id) {
        try {
            byte[] qrCodeImage = tableService.getTableQRCodeImage(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException | WriterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
