package com.mybooks.library.controller;

import com.mybooks.library.data.AuthorDTO;
import com.mybooks.library.data.BookDTO;
import com.mybooks.library.service.BookService;
import com.mybooks.library.service.AuthorService; // Vamos precisar do AuthorService também para o formulário de livro
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/web/books")
public class BookWebController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;


    @GetMapping
    public String listBooks(Model model) {
        List<BookDTO> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("/{id}")
    public String viewBookDetails(@PathVariable("id") Long id, Model model) {
        try {
            BookDTO book = bookService.findById(id);

            if (book.getAuthorId() != null) {
                model.addAttribute("author", authorService.findById(book.getAuthorId()));
            }
            model.addAttribute("book", book);
            return "books/details";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Livro não encontrado ou erro ao carregar detalhes: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Livro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir o livro: " + e.getMessage());
        }
        return "redirect:/web/books";
    }

    @GetMapping("/new")
    public String showBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        List<AuthorDTO> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("pageTitle", "Cadastrar Novo Livro");
        return "books/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model) {
        try {
            BookDTO book = bookService.findById(id);
            model.addAttribute("book", book);
            List<AuthorDTO> authors = authorService.findAll();
            model.addAttribute("authors", authors);
            model.addAttribute("pageTitle", "Editar Livro (ID: " + id + ")");
            return "books/form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Livro não encontrado para edição.");
            return "redirect:/web/books";
        }
    }

    @PostMapping
    public String saveBook(@ModelAttribute("book") BookDTO book,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", (book.getId() == null ? "Cadastrar Novo Livro" : "Editar Livro (ID: " + book.getId() + ")"));
            List<AuthorDTO> authors = authorService.findAll();
            model.addAttribute("authors", authors);
            return "books/form";
        }

        try {
            if (book.getId() == null) {
                bookService.create(book);
                redirectAttributes.addFlashAttribute("successMessage", "Livro cadastrado com sucesso!");
            } else {
                bookService.update(book);
                redirectAttributes.addFlashAttribute("successMessage", "Livro atualizado com sucesso!");
            }
            return "redirect:/web/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao salvar o livro: " + e.getMessage());
            model.addAttribute("pageTitle", (book.getId() == null ? "Cadastrar Novo Livro" : "Editar Livro (ID: " + book.getId() + ")"));
            List<AuthorDTO> authors = authorService.findAll();
            model.addAttribute("authors", authors);
            return "books/form";
        }
    }
}
