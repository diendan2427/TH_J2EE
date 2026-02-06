package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.Category;
import com.hutech.bai8.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final Cart cart;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("cartCount", cart.getTotalItems());
        model.addAttribute("icons", getAvailableIcons());
        return "admin/categories/form";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("success", "Them danh muc thanh cong!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Category category = categoryService.getCategoryById(id).orElse(null);
        if (category == null) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("cartCount", cart.getTotalItems());
        model.addAttribute("icons", getAvailableIcons());
        return "admin/categories/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable String id, @ModelAttribute Category category, 
                                 RedirectAttributes redirectAttributes) {
        category.setId(id);
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("success", "Cap nhat danh muc thanh cong!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable String id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("success", "Xoa danh muc thanh cong!");
        return "redirect:/admin/categories";
    }

    private String[] getAvailableIcons() {
        return new String[]{
            "bi-book", "bi-code-slash", "bi-graph-up", "bi-lightbulb", 
            "bi-person-check", "bi-heart", "bi-star", "bi-globe",
            "bi-music-note", "bi-camera", "bi-controller", "bi-cup-hot",
            "bi-flower1", "bi-palette", "bi-building", "bi-airplane"
        };
    }
}
