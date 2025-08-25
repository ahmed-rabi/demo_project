package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.entity.RestaurantTable;
import com.example.demo.service.MenuService;
import com.example.demo.service.QRCodeService;
import com.example.demo.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/customer")
public class CustomerQRController {
    private final QRCodeService qrCodeService;
    private final TableService tableService;
    private final MenuService menuService;

    @Autowired
    public CustomerQRController(QRCodeService qrCodeService, TableService tableService, MenuService menuService) {
        this.qrCodeService = qrCodeService;
        this.tableService = tableService;
        this.menuService = menuService;
    }

    @PostMapping("/scan-qr")
    public ResponseEntity<?> scanQRAndGetMenu(@RequestParam("image") MultipartFile imageFile) {
        try {
            // Read QR code from the uploaded image
            String qrContent = qrCodeService.readQRCode(imageFile.getBytes());

            // Extract table ID from the QR code content
            // Assuming QR content format: "http://localhost:8080/table/{tableId}"
            Pattern pattern = Pattern.compile("/table/(\\d+)");
            Matcher matcher = pattern.matcher(qrContent);

            if (matcher.find()) {
                Long tableId = Long.parseLong(matcher.group(1));

                // Get table by ID
                RestaurantTable table = tableService.getTableById(tableId)
                        .orElseThrow(() -> new RuntimeException("Table not found"));

                // Get all menus (assuming one menu for now)
                List<Menu> menus = menuService.getAllMenus();
                if (menus.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                // Return the menu
                return ResponseEntity.ok(menus.get(0));
            } else {
                return ResponseEntity.badRequest()
                        .body("Invalid QR code format");
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Error reading QR code: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Server error: " + e.getMessage());
        }
    }
}
