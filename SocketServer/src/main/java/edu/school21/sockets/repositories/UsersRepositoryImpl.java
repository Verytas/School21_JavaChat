package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static java.sql.Types.BIGINT;
import static java.sql.Types.VARCHAR;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsersRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findById(Long id) {
        return jdbcTemplate
                .query("SELECT * FROM userTable WHERE identifier = ?",
                        new Object[]{id},
                        new int[]{BIGINT},
                        new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate
                .query("SELECT * FROM userTable",
                        new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO userTable (name, password) VALUES (?, ?)",
                user.getName(),
                user.getPassword());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE userTable SET name = ? password=? WHERE identifier = ?",
                user.getName(),
                user.getPassword(),
                user.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM userTable WHERE identifier = ?", id);
    }

    @Override
    public Optional<User> findByName(String name) {
        return jdbcTemplate
                .query("SELECT * FROM userTable WHERE name = ?",
                        new Object[]{name},
                        new int[]{VARCHAR},
                        new BeanPropertyRowMapper<>(User.class))
                .stream().findAny();
    }
}
