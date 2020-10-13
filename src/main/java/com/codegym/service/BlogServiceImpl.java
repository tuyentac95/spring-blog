package com.codegym.service;

import com.codegym.model.Blog;
import com.codegym.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.*;

public class BlogServiceImpl implements BlogService{

    public static final String CONTENT_SOURCE = "E:\\CodeGym\\SpringMVC\\SpringBlog\\src\\main\\webapp";

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Page<Blog> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Blog findById(Long id) {
        return blogRepository.findOne(id);
    }

    @Override
    public void save(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public void remove(Long id) {
        String content_id = findById(id).getContent_id();
        File file = new File(CONTENT_SOURCE + "\\WEB-INF\\content\\"+content_id+".txt");
        if(file.delete()){
            System.out.println("Deleted");
        }
        blogRepository.delete(id);
    }

    @Override
    public void saveContent(String content_id, String content) throws IOException {
        File file = new File(CONTENT_SOURCE + "\\WEB-INF\\content\\"+content_id+".txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(content);
        bufferedWriter.close();
    }

    @Override
    public String getContent(String content_id) throws IOException {
        String content = "";
        File file = new File(CONTENT_SOURCE + "\\WEB-INF\\content\\"+content_id+".txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String text;
        while ((text = bufferedReader.readLine()) != null){
            content += text;
        }
        return content;
    }

    @Override
    public Page<Blog> findPublish(Pageable pageable) {
        return blogRepository.findAllByStatusEquals("Published", pageable);
    }

}
