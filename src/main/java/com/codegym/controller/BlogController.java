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
    public static final String CONTENT_SOURCE = "E:\\CodeGym\\SpringMVC\\SpringBlog\\src\\main\\webapp";
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

        File file = new File(CONTENT_SOURCE + "\\WEB-INF\\content\\"+content_id+".txt");

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(content);
        bufferedWriter.close();

        ModelAndView modelAndView = new ModelAndView("/blog/test");
        modelAndView.addObject("content",content);
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
           String content = "";
           File file = new File(CONTENT_SOURCE + "\\WEB-INF\\content\\"+content_id+".txt");
           BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

           String text;
           while ((text = bufferedReader.readLine()) != null){
               content += text;
           }

           modelAndView.addObject("content",content);
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

}
