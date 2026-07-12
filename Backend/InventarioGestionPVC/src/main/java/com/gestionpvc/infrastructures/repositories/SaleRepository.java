package com.gestionpvc.infrastructures.repositories;

import com.gestionpvc.applications.interfaces.irepositories.ISaleRepository;
import com.gestionpvc.domains.SaleDetails;
import com.gestionpvc.domains.Product;
import com.gestionpvc.domains.Sale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.Optional;
import java.sql.ResultSet;
import java.util.List;


public class SaleRepository extends BaseRepository implements ISaleRepository {

    public SaleRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Sale> getById(String id) {
        String sql = """
            SELECT id, created_at, total, number_bill
            FROM sale
            WHERE id = ?
        """;

        return queryOne(sql, rs -> {
            try {
                return mapRowWithDetails(rs, id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, id);
    }

    @Override
    public List<Sale> getAll() {
        String sql = """
            SELECT id, created_at, total, number_bill
            FROM sale
            ORDER BY created_at DESC
        """;

        return queryMany(sql, rs -> {
            try {
                String saleId = rs.getString("id");
                return mapRowWithDetails(rs, saleId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Sale add(Sale sale) {
        String sql = """
            INSERT INTO sale(id, created_at, total, number_bill)
            VALUES (?, ?, ?, ?)
        """;

        executeUpdate(
                sql,
                sale.id(),
                sale.createdAt(),
                sale.total(),
                sale.numberBill()
        );

        // Insertar los detalles de la venta
        insertSaleDetails(sale.id(), sale.items());

        return sale;
    }

    @Override
    public Sale update(Sale sale) {
        String sql = """
            UPDATE sale
            SET total = ?, number_bill = ?, created_at = ?
            WHERE id = ?
        """;

        int rows = executeUpdate(
                sql,
                sale.total(),
                sale.numberBill(),
                sale.createdAt(),
                sale.id()
        );

        if (rows == 0) {
            throw new RuntimeException("Sale not found with id: " + sale.id());
        }

        // Eliminar detalles anteriores
        deleteSaleDetails(sale.id());

        // Insertar nuevos detalles
        insertSaleDetails(sale.id(), sale.items());

        return sale;
    }

    @Override
    public void delete(String id) {
        // Eliminar detalles primero (FK constraint)
        deleteSaleDetails(id);

        String sql = "DELETE FROM sale WHERE id = ?";
        executeUpdate(sql, id);
    }

    // 🔧 Métodos auxiliares
    private void insertSaleDetails(String saleId, List<SaleDetails> items) {
        String sql = """
            INSERT INTO sale_details(sale_id, product_id, name, quantity, sale_price, profit)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        for (SaleDetails item : items) {
            executeUpdate(
                    sql,
                    saleId,
                    item.productId(),
                    item.name(),
                    item.quantity(),
                    item.salePrice(),
                    item.profit()
            );
        }
    }

    private void deleteSaleDetails(String saleId) {
        String sql = "DELETE FROM sale_details WHERE sale_id = ?";
        executeUpdate(sql, saleId);
    }

    private Sale mapRowWithDetails(ResultSet rs, String saleId) throws SQLException {
        String id = rs.getString("id");
        LocalDateTime createdAt = parseTimestamp(rs.getString("created_at"));
        double total = rs.getDouble("total");
        String numberBill = rs.getString("number_bill");

        // Obtener los detalles de la venta
        List<SaleDetails> items = getSaleDetails(saleId);

        return new Sale(id, createdAt, items, total, numberBill);
    }

    private List<SaleDetails> getSaleDetails(String saleId) {
        String sql = """
            SELECT sd.product_id, sd.name AS item_name, sd.quantity, sd.sale_price, sd.profit,
                   p.id AS product_id_db, p.name AS product_name, p.purchase_value, p.stock, p.active
            FROM sale_details sd
            LEFT JOIN product p ON sd.product_id = p.id
            WHERE sd.sale_id = ?
        """;

        return queryMany(sql, rs -> {
            try {
                String productId = rs.getString("product_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double salePrice = rs.getDouble("sale_price");
                double profit = rs.getDouble("profit");

                // Mapear producto si existe
                Product product = null;
                if (rs.getString("product_id_db") != null) {
                    product = new Product(
                            rs.getString("product_id_db"),
                            rs.getString("product_name"),
                            rs.getDouble("purchase_value"),
                            rs.getInt("stock"),
                            rs.getBoolean("active")
                    );
                }

                return new SaleDetails(productId, product, itemName, quantity, salePrice, profit);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, saleId);
    }

    /**
     * Parsea timestamps en múltiples formatos (ISO 8601, SQLite estándar)
     * Maneja nanosegundos excesivos que SQLite no soporta
     */
    private LocalDateTime parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.trim().isEmpty()) {
            return LocalDateTime.now();
        }

        try {
            // Formato ISO 8601 con T: "2026-04-07T13:58:00.868591001"
            // Convertir T a espacio y recortar a 6 decimales
            String normalized = timestamp
                    .replace("T", " ")
                    .replaceAll("\\.(\\d{6})\\d*", ".$1"); // Mantener solo 6 decimales
            
            return LocalDateTime.parse(normalized, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
        } catch (Exception e1) {
            try {
                // Intenta formato SQLite estándar: "2026-04-07 13:58:00.868591"
                return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            } catch (Exception e2) {
                try {
                    // Intenta sin decimales: "2026-04-07 13:58:00"
                    return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e3) {
                    System.err.println("⚠️ No se pudo parsear timestamp: " + timestamp);
                    return LocalDateTime.now();
                }
            }
        }
    }
}
