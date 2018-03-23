package com.kronen.buddy.backend.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class LocalStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalStorageService.class);

    private static final String PROFILE_PICTURE_FILENAME = "profilePicture";

    @Value("${image.storage.temp.folder")
    private String storageFolder;

    public String storeProfileImage(MultipartFile uploadedFile, String username) throws IOException {
        String profileImagePath = null;
        if(uploadedFile != null && !uploadedFile.isEmpty()) {
            byte[] uploadedFileData = uploadedFile.getBytes();

            File imageStorageFolder = new File(storageFolder, username);
            if(!imageStorageFolder.exists()) {
                LOG.info("Creating the storage folder {} for user assets", imageStorageFolder.getAbsolutePath());
                imageStorageFolder.mkdirs();
            }

            File profileImageFile = new File(imageStorageFolder.getAbsolutePath(),
                    PROFILE_PICTURE_FILENAME + "."
                    + FilenameUtils.getExtension(uploadedFile.getOriginalFilename()));
            profileImagePath = imageStorageFolder.getPath();
            LOG.info("Profile image will be saved to {}", profileImageFile.getAbsolutePath());

            FileUtils.writeByteArrayToFile(profileImageFile, uploadedFileData);
        }

        return profileImagePath;
    }
}
