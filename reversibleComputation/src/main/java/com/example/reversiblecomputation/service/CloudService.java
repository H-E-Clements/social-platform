package com.example.reversiblecomputation.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CloudService {

    @Autowired
    private Storage storage;

    public ResponseEntity<String> fileUpload(@RequestBody byte[] file, String fileName) throws IOException {
//        File file = new File(filePath);
        BlobId id = BlobId.of("publication-storage-leicester", fileName);
        BlobInfo info = BlobInfo.newBuilder(id).build();
//        byte[] arr = Files.readAllBytes(Paths.get(file.toURI()));
        storage.create(info, file);
        return ResponseEntity.ok("File Successfully Uploaded!");
    }

    public File fileDownload(@RequestParam String publicationId, @RequestParam String filePath) throws IOException {
        File myFile = File.createTempFile("myFile", ".txt");
        BlobId id = BlobId.of("publication-storage-leicester", publicationId);
        Blob myBlob = storage.get("publication-storage-leicester", "test.txt");
        myBlob.downloadTo(myFile.toPath());
        return myFile;
    }
}
