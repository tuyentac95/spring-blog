package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Category;
import com.codegym.service.BlogService;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> categories(){
        return categoryService.findAll();
    }

    @GetMapping("/blogs")
    public ModelAndView listBlogs(Pageable pageable){
        Page<Blog> blogs = blogService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/blog/list");
        modelAndView.addObject("blogs",blogs);
        return modelAndView;
    }

    @GetMapping("/create-blog")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/blog/create");
        modelAndView.addObject("blog",new Blog());
        return modelAndView;
    }

    @PostMapping("/create-blog")
    public ModelAndView createBlog(@ModelAttribute("blog") Blog blog,@RequestParam("content-html") String content) throws IOException {
        String content_id = blog.getTitle().replaceAll("\\s+","-").toLowerCase();
        blog.setContent_id(content_id);

        blogService.save(blog);
        blogService.saveContent(content_id, content);

        ModelAndView modelAndView = new ModelAndView("redirect:/blogs");
        return modelAndView;
    }

    @GetMapping("/view-blog/{id}")
    public ModelAndView showBlog(@PathVariable Long id) throws IOException {
        ModelAndView modelAndView;
        Blog blog = blogService.findById(id);
        if(blog != null){
           modelAndView  = new ModelAndView("/blog/show");
           modelAndView.addObject("blog",blog);

           String content_id = blog.getContent_id();
           String content = blogService.getContent(content_id);

           modelAndView.addObject("content",content);
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @GetMapping("/edit-blog/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) throws IOException {
        ModelAndView modelAndView;
        Blog blog = blogService.findById(id);
        if(blog != null){
            modelAndView = new ModelAndView("/blog/edit");
            modelAndView.addObject("blog",blog);

            String content_id = blog.getContent_id();
            String content = blogService.getContent(content_id);

            modelAndView.addObject("content",content);
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @PostMapping("/edit-blog")
    public ModelAndView editBlog(@ModelAttribute("blog") Blog blog, @RequestParam("content-html") String content) throws IOException {
        String content_id = blogService.findById(blog.getId()).getContent_id();
        blog.setContent_id(content_id);

        blogService.save(blog);
        blogService.saveContent(content_id,content);

        return new ModelAndView("redirect:/blogs");
    }

    @GetMapping("/delete-blog/{id}")
    public ModelAndView deleteBlog(@PathVariable Long id){
        Blog blog = blogService.findById(id);
        ModelAndView modelAndView;
        if(blog != null){
            blogService.remove(blog.getId());
            modelAndView = new ModelAndView("redirect:/blogs");
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

}
