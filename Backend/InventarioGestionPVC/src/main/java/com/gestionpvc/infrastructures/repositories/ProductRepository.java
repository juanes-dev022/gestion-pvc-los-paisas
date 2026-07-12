package com.gestionpvc.infrastructures.repositories;

import com.gestionpvc.applications.interfaces.irepositories.IProductRepository;
import com.gestionpvc.domains.Product;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepository extends BaseRepository implements IProductRepository {

    public ProductRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Product> getById(String id) {
        String sql = """
            SELECT id, name, purchase_value, stock, active
            FROM product
            WHERE id = ?
        """;

        return queryOne(sql, this::mapRow, id);
    }

    @Override
    public List<Product> getAll() {
        String sql = """
            SELECT id, name, purchase_value, stock, active
            FROM product
        """;

        return queryMany(sql, this::mapRow);
    }

    @Override
    public Product add(Product product) {
        String productId = UUID.randomUUID().toString();
        String sql = """
            INSERT INTO product(id, name, purchase_value, stock, active)
            VALUES (?, ?, ?, ?, ?)
        """;

        executeUpdate(
                sql,
                productId,
                product.name(),
                product.purchaseValue(),
                product.stock(),
                product.active()
        );

        return new Product(
                productId,
                product.name(),
                product.purchaseValue(),
                product.stock(),
                product.active()
        );
    }

    @Override
    public Product update(Product product) {
        String sql = """
            UPDATE product
            SET name = ?, purchase_value = ?, stock = ?, active = ?
            WHERE id = ?
        """;

        int rows = executeUpdate(
                sql,
                product.name(),
                product.purchaseValue(),
                product.stock(),
                product.active(),
                product.id()
        );

        if (rows == 0) {
            throw new RuntimeException("Product not found with id: " + product.id());
        }

        return product;
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM product WHERE id = ?";
        executeUpdate(sql, id);
    }

    private Product mapRow(ResultSet rs) {
        try {
            return new Product(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getDouble("purchase_value"),
                    rs.getInt("stock"),
                    rs.getBoolean("active")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e); // opcional: puedes mejorar esto
        }
    }
}