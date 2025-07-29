package com.mybooks.library.controller;

import com.mybooks.library.data.AuthorDTO;
import com.mybooks.library.data.BookDTO;
import com.mybooks.library.service.AuthorService;
import com.mybooks.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/web/authors")
public class AuthorWebController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listAuthors(Model model) {
        List<AuthorDTO> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authors/list";
    }

    @GetMapping("/{id}")
    public String viewAuthorDetails(@PathVariable("id") Long id, Model model) {
        try {
            AuthorDTO author = authorService.findById(id);
            List<BookDTO> books = bookService.findBooksByAuthorId(id);
            model.addAttribute("author", author);
            model.addAttribute("books", books);
            return "authors/details";
        } catch (Exception e) {

            model.addAttribute("errorMessage", "Autor não encontrado ou erro ao carregar detalhes: " + e.getMessage());
            return "error";
        }
    }
    @GetMapping("/new")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new AuthorDTO());
        model.addAttribute("pageTitle", "Cadastrar Novo Autor");
        return "authors/form"; // Template do formulário
    }

    @GetMapping("/edit/{id}")
    public String showEditAuthorForm(@PathVariable("id") Long id, Model model) {
        try {
            AuthorDTO author = authorService.findById(id);
            model.addAttribute("author", author);
            model.addAttribute("pageTitle", "Editar Autor (ID: " + id + ")");
            return "authors/form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Autor não encontrado para edição.");
            return "redirect:/web/authors";
        }
    }

    @PostMapping
    public String saveAuthor(@Valid @ModelAttribute("author") AuthorDTO author,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", (author.getId() == null ? "Cadastrar Novo Autor" : "Editar Autor (ID: " + author.getId() + ")"));
            return "authors/form"; // Retorna ao formulário para mostrar erros
        }

        try {
            if (author.getId() == null) {
                authorService.create(author);
                redirectAttributes.addFlashAttribute("successMessage", "Autor cadastrado com sucesso!");
            } else {
                authorService.update(author);
                redirectAttributes.addFlashAttribute("successMessage", "Autor atualizado com sucesso!");
            }
            return "redirect:/web/authors";
        } catch (Exception e) {

            model.addAttribute("errorMessage", "Erro ao salvar o autor: " + e.getMessage());
            model.addAttribute("pageTitle", (author.getId() == null ? "Cadastrar Novo Autor" : "Editar Autor (ID: " + author.getId() + ")"));
            return "authors/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            authorService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Autor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir o autor: " + e.getMessage());
        }
        return "redirect:/web/authors";
    }
}
