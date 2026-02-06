package com.hutech.bai5.model;

import com.hutech.bai5.validation.ValidCategoryId;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Bài 5: Entity Book với Validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 2, max = 200, message = "Tiêu đề phải từ 2-200 ký tự")
    private String title;
    
    @NotBlank(message = "Tác giả không được để trống")
    private String author;
    
    @Positive(message = "Giá phải là số dương")
    private Double price;
    
    private String description;
    private String imageUrl;
    
    @DBRef
    private Category category;
    
    // Transient field for validation
    @ValidCategoryId
    private transient String categoryId;
}
