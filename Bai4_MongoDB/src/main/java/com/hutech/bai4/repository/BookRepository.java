package com.hutech.bai4.repository;

import com.hutech.bai4.model.Book;
import com.hutech.bai4.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Bài 4: Repository Book với MongoDB
 * Kế thừa MongoRepository (tương tự JpaRepository + PagingAndSortingRepository)
 */
@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    
    // Tìm sách theo category
    List<Book> findByCategory(Category category);
    
    // Tìm sách theo tiêu đề (chứa từ khóa, không phân biệt hoa thường)
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    // Tìm sách theo tác giả
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    // Tìm sách theo giá <= maxPrice
    List<Book> findByPriceLessThanEqual(Double maxPrice);
    
    // Custom query: Tìm kiếm sách theo từ khóa (tiêu đề hoặc tác giả)
    @Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'author': { $regex: ?0, $options: 'i' } } ] }")
    List<Book> searchByKeyword(String keyword);
    
    // Phân trang
    Page<Book> findAll(Pageable pageable);
}
