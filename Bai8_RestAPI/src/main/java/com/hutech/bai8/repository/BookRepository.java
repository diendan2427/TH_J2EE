package com.hutech.bai8.repository;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByCategory(Category category);
    List<Book> findByCategoryId(String categoryId);
    List<Book> findByActiveTrue();
    
    // Phân trang
    Page<Book> findAll(Pageable pageable);
    Page<Book> findByCategory(Category category, Pageable pageable);
    Page<Book> findByCategoryId(String categoryId, Pageable pageable);
    
    @Query("{ $or: [ " +
           "{ 'title': { $regex: ?0, $options: 'i' } }, " +
           "{ 'author': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } } " +
           "] }")
    List<Book> searchByKeyword(String keyword);
    
    @Query("{ $or: [ " +
           "{ 'title': { $regex: ?0, $options: 'i' } }, " +
           "{ 'author': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } } " +
           "] }")
    Page<Book> searchByKeyword(String keyword, Pageable pageable);
    
    // Lọc theo khoảng giá
    @Query("{ 'price': { $gte: ?0, $lte: ?1 } }")
    Page<Book> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);
    
    // Lọc theo danh mục và khoảng giá
    @Query("{ 'category': ?0, 'price': { $gte: ?1, $lte: ?2 } }")
    Page<Book> findByCategoryAndPriceBetween(Category category, double minPrice, double maxPrice, Pageable pageable);
    
    // Tìm kiếm với lọc giá
    @Query("{ $and: [ " +
           "{ $or: [ " +
           "{ 'title': { $regex: ?0, $options: 'i' } }, " +
           "{ 'author': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } } " +
           "] }, " +
           "{ 'price': { $gte: ?1, $lte: ?2 } } " +
           "] }")
    Page<Book> searchByKeywordAndPriceBetween(String keyword, double minPrice, double maxPrice, Pageable pageable);
    
    // Tìm sách theo danh sách ID (cho autocomplete)
    List<Book> findByIdIn(List<String> ids);
    
    // Tìm sách có giá cao nhất
    List<Book> findTop5ByOrderByPriceDesc();
    
    // Tìm sách mới nhất
    List<Book> findTop10ByOrderByIdDesc();
}
