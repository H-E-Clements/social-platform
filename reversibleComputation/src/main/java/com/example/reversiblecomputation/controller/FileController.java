package com.example.reversiblecomputation.controller;

import com.example.reversiblecomputation.domain.Feed;
import com.example.reversiblecomputation.domain.Paper;
import com.example.reversiblecomputation.repository.FeedRepository;
import com.example.reversiblecomputation.repository.PaperRepository;
import com.example.reversiblecomputation.service.PaperDateSortService;
import com.example.reversiblecomputation.service.SearchAndIdentifyService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    private PaperRepository paperRepo;
    @Autowired
    private SearchAndIdentifyService searchAndIdentifyService;

    @Autowired
    private FeedRepository feedRepo;

    String baseDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
    //MUST CHANGE FOR DIFFERENT HOSTS

    @GetMapping("/upload")
    public String paper(Model model, Authentication authentication){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);boolean navImg = searchAndIdentifyService.checkImg(authentication);model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);try {model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");} catch(Exception e) {}
        return "papers/upload";
    }

    @PostMapping("/uploadPaper")
    public String uploadPaper(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("fileDescription") String fileDescription,
                                    Authentication authentication) throws IOException {
        //RequestParam  matches 'name="file/fileName/fileDescription"' from form
        for(String str:fileName.split(" ")){if (str.length()>55){return "redirect:/upload?lenErr";}}
        for(String str:fileDescription.split(" ")){if (str.length()>55){return "redirect:/upload?lenErr";}}
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equals("pdf")){
            return "redirect:/upload?extErr";
        }//File type must be PDF, otherwise error is given
        if(fileName.isEmpty()){
            return "redirect:/upload?nameErr";
        }//User must input a File Name
        if(fileDescription.isEmpty()){
            return "redirect:/upload?descErr";
        }//User must input a Description for the File
        //Validation all above

        file.transferTo(new File(baseDir + fileName+".pdf"));
        //Stores file to base directory with inputted name
        //File storing all above

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Paper paper = new Paper();
        paper.setFileName(fileName+".pdf");
        paper.setDescription(fileDescription);
        paper.setAuthor(searchAndIdentifyService.userObject(authentication).getName());
        paper.setUploadDate(dtf.format(now));
        paperRepo.save(paper);

        Feed feed = new Feed();
        feed.setTitle(searchAndIdentifyService.userObject(authentication).getName()+" uploaded a paper: "+fileName);
        feed.setText(fileDescription+" | To view this paper, visit the 'Papers' section");
        feed.setAuthor(searchAndIdentifyService.userObject(authentication).getName());
        feed.setAuthorId(searchAndIdentifyService.userObject(authentication).getId());
        feed.setPostDate(dtf.format(now));
        feedRepo.save(feed);

        return "redirect:/viewPapers";
        //redirects to view all papers
    }

    @GetMapping("/viewPapers")
    public String viewPapers(Model model, String keyword, String searchType, Authentication authentication){
        boolean navUser = searchAndIdentifyService.checkIfLoggedIn(authentication);boolean navImg = searchAndIdentifyService.checkImg(authentication);model.addAttribute("navImg", navImg);model.addAttribute("navUser", navUser);try {model.addAttribute("id", searchAndIdentifyService.userObject(authentication).getId()+".png");} catch(Exception e) {}

        List<Paper> papers = null;

        if (keyword != null){
            if (searchType.equals("description")){papers = paperRepo.findByKeywordDescription(keyword);}
            if (searchType.equals("author")){papers = paperRepo.findByKeywordAuthor(keyword);}
            if (searchType.equals("file_name")){papers = paperRepo.findByKeywordFileName(keyword);}
            if (searchType.equals("upload_date")){papers = paperRepo.findByKeywordUploadDate(keyword);}
            model.addAttribute("papers", papers);
        }//Paper search feature^

        else {
            papers = paperRepo.findAll();
            model.addAttribute("papers", papers);
        }

        Collections.sort(papers, (new PaperDateSortService()).reversed());

        return "papers/view";
    }

    @GetMapping("/viewPaper/{fileName}")
    public void viewPaper(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        File file = new File("src\\main\\resources\\static.uploads\\"+fileName);
        //Getting file that user wants to download

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        //Setting up download response

        ServletOutputStream outputStream = response.getOutputStream();
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[8192]; //8KB buffer
        int bytesRead = -1;
        while((bytesRead=inputStream.read(buffer)) != -1)   {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close(); outputStream.close();
        //Reading content of file/pdf
    }

}
