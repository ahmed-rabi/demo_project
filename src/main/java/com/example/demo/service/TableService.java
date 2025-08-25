package com.example.demo.service;

import com.example.demo.entity.RestaurantTable;
import com.example.demo.repository.TableRepository;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    private final TableRepository tableRepository;
    private final QRCodeService qrCodeService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Autowired
    public TableService(TableRepository tableRepository, QRCodeService qrCodeService) {
        this.tableRepository = tableRepository;
        this.qrCodeService = qrCodeService;
    }

    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    public Optional<RestaurantTable> getTableById(Long id) {
        return tableRepository.findById(id);
    }

    public RestaurantTable saveTable(RestaurantTable table) {
        return tableRepository.save(table);
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    public RestaurantTable generateQRCodeForTable(Long tableId) throws IOException, WriterException {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + tableId));

        // Generate QR code content (URL that points to the table)
        String qrContent = baseUrl + "/table/" + tableId;
        table.setQrCodeValue(qrContent);

        // Generate QR code image and get its URL
        String qrCodeImageUrl = qrCodeService.generateQRCodeForTable(tableId);
        table.setQrCodeImageUrl(qrCodeImageUrl);

        // Save updated table with QR code info
        return tableRepository.save(table);
    }

    public byte[] getTableQRCodeImage(Long tableId) throws IOException, WriterException {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + tableId));

        if (table.getQrCodeValue() == null) {
            String qrContent = baseUrl + "/table/" + tableId;
            return qrCodeService.generateQRCodeBytes(qrContent);
        } else {
            return qrCodeService.generateQRCodeBytes(table.getQrCodeValue());
        }
    }
}