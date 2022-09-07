package com.samco.JDBCTemplate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samco.JDBCTemplate.model.BookList;
import com.samco.JDBCTemplate.repository.BookRepository;

@RestController
@RequestMapping("")
public class BookController {

	@Autowired
	BookRepository bookRepository;

	@GetMapping("/BookList")
	public ResponseEntity<List<BookList>> getAllBooks(@RequestParam(required = false) String title) {
		try {
			List<BookList> books = new ArrayList<BookList>();
			if (title == null)
				bookRepository.findAll().forEach(books::add);
			else
				bookRepository.findByTitleContaining(title).forEach(books::add);
			if (books.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/BookList/{id}")
	public ResponseEntity<BookList> getTutorialById(@PathVariable("id") int id) {
		BookList books = bookRepository.findById(id);
		if (books != null) {
			return new ResponseEntity<>(books, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/BookList")
	public ResponseEntity<String> createTutorial(@RequestBody BookList books) {
		try {
			bookRepository.save(new BookList(books.getTitle(), books.getDescription(), false));
			return new ResponseEntity<>("Tutorial was created successfully.", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/BookList/{id}")
	public ResponseEntity<String> updateTutorial(@PathVariable("id") int id, @RequestBody BookList books) {
		BookList bookList = bookRepository.findById(id);
		if (bookList != null) {
			bookList.setId(id);
			bookList.setTitle(books.getTitle());
			bookList.setDescription(books.getDescription());
			bookList.setPublished(books.isPublished());
			bookRepository.update(bookList);
			return new ResponseEntity<>("Tutorial was updated successfully.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Cannot find Tutorial with id=" + id, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/BookList/{id}")
	public ResponseEntity<String> deleteTutorial(@PathVariable("id") int id) {
		try {
			int result = bookRepository.deleteById(id);
			if (result == 0) {
				return new ResponseEntity<>("Cannot find Tutorial with id=" + id, HttpStatus.OK);
			}
			return new ResponseEntity<>("Tutorial was deleted successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Cannot delete tutorial.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/BookList")
	public ResponseEntity<String> deleteAllTutorials() {
		try {
			int numRows = bookRepository.deleteAll();
			return new ResponseEntity<>("Deleted " + numRows + " Tutorial(s) successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Cannot delete tutorials.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/BookList/published")
	public ResponseEntity<List<BookList>> findByPublished() {
		try {
			List<BookList> books = bookRepository.findByPublished(true);
			if (books.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
