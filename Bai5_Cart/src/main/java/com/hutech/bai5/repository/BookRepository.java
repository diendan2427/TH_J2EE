package com.hutech.bai5.repository;

import com.hutech.bai5.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    
    // Tìm kiếm sách theo từ khóa (tiêu đề, tác giả, danh mục)
    @Query("{ $or: [ " +
           "{ 'title': { $regex: ?0, $options: 'i' } }, " +
           "{ 'author': { $regex: ?0, $options: 'i' } } " +
           "] }")
    List<Book> searchByKeyword(String keyword);
}
