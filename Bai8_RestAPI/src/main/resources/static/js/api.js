/**
 * Bài 8: JavaScript AJAX để gọi REST API
 */

// Hiển thị modal thêm sách
function showApiModal() {
    const modal = new bootstrap.Modal(document.getElementById('addBookModal'));
    modal.show();
}

// Thêm sách qua API (POST)
async function addBookViaApi() {
    const book = {
        title: document.getElementById('apiTitle').value,
        author: document.getElementById('apiAuthor').value,
        price: parseFloat(document.getElementById('apiPrice').value),
        description: document.getElementById('apiDescription').value
    };

    try {
        const response = await fetch('/api/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        });

        if (response.ok) {
            alert('Thêm sách thành công!');
            location.reload(); // Reload để cập nhật danh sách
        } else {
            const error = await response.json();
            alert('Lỗi: ' + JSON.stringify(error));
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Có lỗi xảy ra!');
    }
}

// Xóa sách qua API (DELETE) - Sử dụng AJAX không tải lại trang
async function deleteBook(bookId) {
    if (!confirm('Bạn có chắc muốn xóa sách này?')) {
        return;
    }

    try {
        const response = await fetch(`/api/books/${bookId}`, {
            method: 'DELETE'
        });

        if (response.ok || response.status === 204) {
            // Xóa hàng khỏi bảng mà không cần reload trang
            const row = document.getElementById(`row-${bookId}`);
            if (row) {
                row.remove();
            }
            
            // Hiển thị thông báo
            showToast('Đã xóa sách thành công!');
        } else {
            alert('Không thể xóa sách!');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Có lỗi xảy ra!');
    }
}

// Hiển thị toast notification
function showToast(message) {
    // Tạo toast element
    const toastHtml = `
        <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
            <div class="toast show" role="alert">
                <div class="toast-header bg-success text-white">
                    <strong class="me-auto">Thông báo</strong>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast"></button>
                </div>
                <div class="toast-body">${message}</div>
            </div>
        </div>
    `;
    
    // Thêm vào body
    document.body.insertAdjacentHTML('beforeend', toastHtml);
    
    // Tự động ẩn sau 3 giây
    setTimeout(() => {
        const toast = document.querySelector('.toast');
        if (toast) {
            toast.remove();
        }
    }, 3000);
}

// Lấy danh sách sách (cho demo)
async function fetchBooks() {
    try {
        const response = await fetch('/api/books');
        const books = await response.json();
        console.log('Books from API:', books);
        return books;
    } catch (error) {
        console.error('Error fetching books:', error);
    }
}

// Lấy chi tiết sách theo ID (cho demo)
async function fetchBookById(id) {
    try {
        const response = await fetch(`/api/books/id/${id}`);
        if (response.ok) {
            const book = await response.json();
            console.log('Book detail:', book);
            return book;
        } else {
            console.log('Book not found');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Cập nhật sách qua API (PUT)
async function updateBookViaApi(id, bookData) {
    try {
        const response = await fetch(`/api/books/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bookData)
        });

        if (response.ok) {
            const updatedBook = await response.json();
            console.log('Updated book:', updatedBook);
            return updatedBook;
        } else {
            console.log('Update failed');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
