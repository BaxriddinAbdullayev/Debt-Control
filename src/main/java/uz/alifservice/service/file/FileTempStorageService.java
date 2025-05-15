package uz.alifservice.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.alifservice.domain.file.ResourceFile;
import uz.alifservice.dto.AppResponse;
import uz.alifservice.dto.file.ResourceFileDto;
import uz.alifservice.exps.AppBadException;
import uz.alifservice.mapper.file.ResourceFileMapper;
import uz.alifservice.repository.file.ResourceFileRepository;
import uz.alifservice.service.message.ResourceBundleService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileTempStorageService {

    private final ResourceFileRepository repository;
    private final ResourceFileMapper mapper;
    private final ResourceLoader resourceLoader;
    private final ResourceBundleService bundleService;

    @Value("${resource.file.upload.folder}")
    private String folderName;

    @Value("${resource.file.upload.url}")
    private String resourceFileUrl;

    public AppResponse<ResourceFileDto> upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppBadException(bundleService.getMessage("empty.file"));
        }

        try {
            String pathFolder = getYMDString(); // 2024/09/27
            String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
            String extension = getExtension(file.getOriginalFilename()); // .jpg, .png, .mp4

            // create folder if not exists
            File folder = new File(folderName + "/" + pathFolder);
            if (!folder.exists()) {
                boolean t = folder.mkdirs(); // images / 2025/01/19
            }

            // save to system
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension); //  files / 2025/01/19 / aaaa-bbbb-cccc-dddd . jpg
            Files.write(path, bytes);

            // save to db
            ResourceFile entity = new ResourceFile();
            entity.setResourceHash(key + "." + extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setOrigenName(file.getOriginalFilename());
            entity.setExtension(extension);
            entity.setVisible(true);
            repository.save(entity);

            ResourceFileDto resourceFileDto = mapper.toDto(entity);
            resourceFileDto.setUrl(openURL(entity.getResourceHash()));

            return AppResponse.success(resourceFileDto, bundleService.getMessage("success.upload"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AppResponse.error(bundleService.getMessage("error.upload"));
    }

    public Resource download(String resourceHash) { // burger-king.jpg
        ResourceFile entity = get(resourceHash);

        Path file = Paths.get(folderName + "/" + entity.getPath() + "/" + resourceHash);
        // images / attaches / burger-king.jpg
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                return resourceLoader.getResource("classpath:images/images.png");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<Resource> open(String resourceHash) {
        ResourceFile entity = get(resourceHash);
        Path filePath = Paths.get(folderName + "/" + entity.getPath() + "/" + entity.getResourceHash()).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new AppBadException(bundleService.getMessage("file.not.found") + " " + resourceHash);
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback content type
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public boolean delete(String resourceHash) {
        ResourceFile entity = get(resourceHash);
        repository.deleteByResourceHashAndDeletedFalse(resourceHash);
        File file = new File(folderName + "/" + entity.getPath() + "/" + entity.getResourceHash());
        boolean b = false;
        if (file.exists()) {
            b = file.delete();
        }
        return b;
    }

    public String openURL(String fileName) {
        return resourceFileUrl + "/file/resource-file/open/" + fileName;
        // http:localhost8080:/attach/ open/ aaaa-bbbb-cccc-dddd . jpg
    }

    private String getYMDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return fileName.substring(lastIndexOf + 1);
    }

    public ResourceFile get(String resourceHash) {
        return repository.findByResourceHashAndDeletedFalse(resourceHash).orElseThrow(() -> {
            throw new AppBadException(bundleService.getMessage("file.not.found") + " " + resourceHash);
        });
    }
}
