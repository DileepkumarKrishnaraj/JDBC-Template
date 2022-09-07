package com.samco.JDBCTemplate.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.samco.JDBCTemplate.model.BookList;


@Repository
public class JdbcBookRepository implements BookRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int save(BookList book) {

		return jdbcTemplate.update("INSERT INTO BookList (title, description, published) VALUES(?,?,?)",
				new Object[] { book.getTitle(), book.getDescription(), book.isPublished() });

	}

	@Override
	public int update(BookList book) {

		return jdbcTemplate.update("UPDATE BookList SET title=?, description=?, published=? WHERE id=?",
				new Object[] { book.getTitle(), book.getDescription(), book.isPublished(), book.getId() });

	}

	@Override
	public BookList findById(int id) {

		try {
			BookList book = jdbcTemplate.queryForObject("SELECT * FROM BookList WHERE id=?",
					BeanPropertyRowMapper.newInstance(BookList.class), id);
			return book;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}

	}

	@Override
	public int deleteById(int id) {

		return jdbcTemplate.update("DELETE FROM BookList WHERE id=?", id);

	}

	@Override
	public List<BookList> findAll() {

		return jdbcTemplate.query("SELECT * from BookList", BeanPropertyRowMapper.newInstance(BookList.class));

	}

	@Override
	public List<BookList> findByPublished(boolean published) {

		return jdbcTemplate.query("SELECT * from BookList WHERE published=?",
				BeanPropertyRowMapper.newInstance(BookList.class), published);
	}

	@Override
	public List<BookList> findByTitleContaining(String title) {

		String string = "SELECT * from BookList WHERE title ILIKE '%" + title + "%'";
		return jdbcTemplate.query(string, BeanPropertyRowMapper.newInstance(BookList.class));

	}

	@Override
	public int deleteAll() {

		return jdbcTemplate.update("DELETE from BookList");

	}

}
