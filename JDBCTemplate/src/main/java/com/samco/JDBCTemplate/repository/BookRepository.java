package com.samco.JDBCTemplate.repository;

import java.util.List;

import com.samco.JDBCTemplate.model.BookList;


public interface BookRepository {
	  int save(BookList book);
	  int update(BookList book);
	  BookList findById(int id);
	  int deleteById(int id);
	  List<BookList> findAll();
	  List<BookList> findByPublished(boolean published);
	  List<BookList> findByTitleContaining(String title);
	  int deleteAll();
}
