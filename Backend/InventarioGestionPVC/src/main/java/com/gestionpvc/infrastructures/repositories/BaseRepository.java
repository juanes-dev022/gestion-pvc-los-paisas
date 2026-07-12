package com.gestionpvc.infrastructures.repositories;

import com.gestionpvc.applications.Exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.function.Function;

public abstract class BaseRepository {

    protected final DataSource dataSource;

    protected BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 🔍 Obtener uno
    protected <T> Optional<T> queryOne(String sql, Function<ResultSet, T> mapper, Object... params) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.apply(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error executing query: " + sql + e);
        }
    }

    // 📋 Obtener varios
    protected <T> List<T> queryMany(String sql, Function<ResultSet, T> mapper, Object... params) {
        List<T> results = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.apply(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error executing query: " + sql + e);
        }

        return results;
    }

    // ➕ Insert
    protected Long executeInsert(String sql, Object... params) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParams(ps, params);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new DataAccessException("No generated keys returned");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error executing insert: " + sql + e);
        }
    }

    // ✏️ Update / Delete
    protected int executeUpdate(String sql, Object... params) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setParams(ps, params);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error executing update: " + sql + e);
        }
    }

    private void setParams(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;

        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}